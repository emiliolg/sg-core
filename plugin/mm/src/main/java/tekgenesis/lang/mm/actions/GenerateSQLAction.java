
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.actions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.util.Files;
import tekgenesis.common.util.Preprocessor;
import tekgenesis.database.Database;
import tekgenesis.database.DatabaseType;
import tekgenesis.lang.mm.FileUtils;
import tekgenesis.lang.mm.MMModuleComponent;
import tekgenesis.lang.mm.ProjectUtils;
import tekgenesis.lang.mm.actions.ui.DatabaseSelector;
import tekgenesis.lang.mm.ui.MMUIInformer;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.database.SchemaDefinition.DB_CURRENT;

/**
 * Generates SQL for a Specific Database.
 */
@SuppressWarnings("WeakerAccess")
public class GenerateSQLAction extends AnAction {

    //~ Methods ......................................................................................................................................

    @Override public void actionPerformed(@NotNull AnActionEvent e) {
        final Project project = e.getProject();
        if (project != null) {
            boolean processed = true;
            String  errorMsg  = "";
            try {
                final DatabaseSelector dialog = new DatabaseSelector(project);
                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);

                if (dialog.isCancel()) processed = false;
                else {
                    final Module      module        = ProjectUtils.findModuleByName(project, dialog.getSelectedModule());
                    final VirtualFile resourcesRoot = module == null ? null : FileUtils.getResourcesRoot(module);

                    if (resourcesRoot != null) generateSql(module, resourcesRoot, dialog, dialog.getOutputDir());
                }
            }
            catch (final Exception e1) {
                if (e1 instanceof FileNotFoundException) errorMsg = "Default Sql Not Generated";
                else errorMsg = GenerateDefaultSQLAction.JAVA_SRC_ROOT_SET_INCORRECTLY;
            }
            finally {
                if (processed) {
                    if (!errorMsg.isEmpty()) MMUIInformer.showErrorBalloonPopUp(project, errorMsg);
                    else MMUIInformer.showBalloonPopUp(project, GenerateDefaultSQLAction.SQL_GENERATED_CORRECTLY);
                }
            }
        }
    }  // end method actionPerformed

    private void generateSql(Module module, VirtualFile resourcesRoot, DatabaseSelector dialog, String outputDir)
        throws IOException
    {
        final DatabaseType databaseType = dialog.getResponse();
        final File         dbDir        = new File(resourcesRoot.getPath(), DB_CURRENT);
        Files.ensureDirExists(dbDir);
        final ModelRepository    modelRepository = module.getComponent(MMModuleComponent.class).getRepository();
        final Collection<String> schemas         = modelRepository.getSchemas();

        for (final String schema : schemas)
            genSQLForDomain(databaseType, dbDir, schema, new File(outputDir));
    }

    private void genSQLForDomain(DatabaseType databaseType, File dbDir, String schema, File outputDir)
        throws FileNotFoundException
    {
        final String lowerCaseSchema = schema.toLowerCase();
        outputDir.mkdirs();
        final File sqlFile = new File(outputDir, lowerCaseSchema + "_" + databaseType.name().toLowerCase() + ".sql");

        final Preprocessor preprocessor   = Database.createPreprocessor(databaseType, "", "currentUser", "userPassword", null);
        final PrintWriter  writer         = new PrintWriter(sqlFile);
        final File         defaultSqlFile = new File(dbDir, lowerCaseSchema + ".sql");
        for (final String line : preprocessor.process(new FileReader(defaultSqlFile)))
            writer.println(line);
        writer.close();
    }
}  // end class GenerateSQLAction
