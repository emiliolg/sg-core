
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import java.util.List;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;

import org.jetbrains.annotations.NotNull;

import static tekgenesis.lang.mm.FileUtils.isResourceFile;

/**
 * Listener for resources files.
 */
class MMResourceEditorListener implements BulkFileListener {

    //~ Instance Fields ..............................................................................................................................

    private final Project project;

    //~ Constructors .................................................................................................................................

    MMResourceEditorListener(final Project project) {
        this.project = project;
    }

    //~ Methods ......................................................................................................................................

    @Override public void after(@NotNull final List<? extends VFileEvent> vfs) {
        for (final VFileEvent vf : vfs) {
            if (isResourceFile(vf.getPath())) {
                final Application application = ApplicationManager.getApplication();
                application.executeOnPooledThread(() -> MMHotSwapManager.getInstance(project).refreshSessions(vf.getPath()));
            }
        }
    }

    @Override public void before(@NotNull final List<? extends VFileEvent> vfs) {
        System.out.println("Before... vfs = " + vfs);
    }
}
