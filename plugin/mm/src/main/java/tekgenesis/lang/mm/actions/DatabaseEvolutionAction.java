
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.actions;

import java.io.*;
import java.util.List;
import java.util.TreeSet;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import org.apache.log4j.lf5.util.StreamUtils;
import org.jetbrains.annotations.NotNull;

import tekgenesis.common.Predefined;
import tekgenesis.common.util.Diff;
import tekgenesis.lang.mm.FileUtils;
import tekgenesis.lang.mm.ProjectUtils;
import tekgenesis.lang.mm.actions.ui.DatabaseEvolution;

import static tekgenesis.lang.mm.actions.ui.DatabaseEvolution.*;
import static tekgenesis.lang.mm.util.Utils.*;

/**
 * Database automatic Evolution action.
 */
@SuppressWarnings("WeakerAccess")
public class DatabaseEvolutionAction extends AnAction {

    //~ Methods ......................................................................................................................................

    @Override public void actionPerformed(@NotNull AnActionEvent event) {
        final Project project = event.getProject();

        if (project == null) throw new IllegalStateException("Project not set");

        try {
            final DatabaseEvolution dialog = new DatabaseEvolution(project);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);

            if (dialog.isOk()) {
                final String selectedModule = dialog.getSelectedModule();

                final Module module = ProjectUtils.findModuleByName(project, selectedModule);
                if (module == null) return;

                final VirtualFile resourcesRoot = FileUtils.getResourcesRoot(module);
                if (resourcesRoot == null) return;

                final VirtualFile dbDir = resourcesRoot.findChild(DB_DIR);
                if (dbDir == null) return;

                final VirtualFile currentDir = dbDir.findChild(CURRENT_DIR);
                if (currentDir == null) return;

                final String          newVersion = dialog.getNewVersion();
                final TreeSet<String> versions   = getVersions(project, selectedModule);

                for (final VirtualFile currentSchemasFile : currentDir.getChildren())
                    generateVersion(dbDir, currentSchemasFile, newVersion, versions);
            }
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateDeltaFile(@NotNull String newVersion, @NotNull VirtualFile deltaDir, @NotNull String schemaName, @NotNull String content)
        throws IOException
    {
        final VirtualFile newDelta = getOrCreateDirectory(deltaDir, "v" + newVersion);

        final File         outputDeltaFile = new File(newDelta.getCanonicalPath(), schemaName);
        final OutputStream out             = new FileOutputStream(outputDeltaFile);
        out.write(content.getBytes("UTF-8"));
        out.close();
        FileUtils.refreshAndFindVFile(outputDeltaFile.getCanonicalPath());
    }

    private void generateVersion(@NotNull VirtualFile rootDir, @NotNull VirtualFile currentSchemasFile, @NotNull String newVersion,
                                 @NotNull final TreeSet<String> versions)
        throws IOException
    {
        final VirtualFile deltaDir   = getOrCreateDirectory(rootDir, DELTA_DIR);
        final VirtualFile versionDir = getOrCreateDirectory(rootDir, VERSION_DIR);

        if (deltaDir == null) throw new IllegalArgumentException("Delta dir null");
        if (versionDir == null) throw new IllegalArgumentException("Version dir null");

        final String schemaName = currentSchemasFile.getName();

        String content = "";

        final TreeSet<String> versionNumbers = new TreeSet<>(versions);

        String  version;
        boolean continueFetchVersion = true;
        // Find a version to compare and make diff
        while ((version = versionNumbers.pollLast()) != null && continueFetchVersion) {
            final InputStream inputStream = currentSchemasFile.getInputStream();

            final File file = new File(versionDir.getCanonicalPath(), version);

            final File schemaFile = new File(file, schemaName);
            if (schemaFile.exists()) {
                continueFetchVersion = false;
                final FileReader               reader = new FileReader(schemaFile);
                final List<Diff.Delta<String>> diff   = Diff.caseSensitive().diff(new InputStreamReader(inputStream), reader);
                content = Diff.makeString(diff);
            }
        }

        if (Predefined.isNotEmpty(content))
        // Generate Delta file
        generateDeltaFile(newVersion, deltaDir, schemaName, content);

        // Copy current to version dir
        final VirtualFile newVersionFile = getOrCreateDirectory(versionDir, "v" + newVersion);
        final File        file           = new File(newVersionFile.getCanonicalPath(), schemaName);
        StreamUtils.copy(currentSchemasFile.getInputStream(), new FileOutputStream(file));
        FileUtils.refreshAndFindVFile(file.getCanonicalPath());
    }  // end method generateVersion

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String DELTA_DIR   = "delta";
    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String CURRENT_DIR = "current";
}  // end class DatabaseEvolutionAction
