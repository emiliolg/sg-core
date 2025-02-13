
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.module.ModuleUtil.findModuleForFile;

import static tekgenesis.lang.mm.FileUtils.isViewResourceFile;
import static tekgenesis.lang.mm.MMFileType.isMMFile;

/**
 * Listener for mm files.
 */
public class MMFileEditorListener implements FileEditorManagerListener {

    //~ Methods ......................................................................................................................................

    @Override public void fileClosed(@NotNull FileEditorManager fileEditorManager, @NotNull VirtualFile virtualFile) {}

    @Override public void fileOpened(@NotNull FileEditorManager fileEditorManager, @NotNull VirtualFile virtualFile) {}

    @Override
    @SuppressWarnings("AssignmentToStaticFieldFromInstanceMethod")
    public void selectionChanged(@NotNull FileEditorManagerEvent fileEditorManagerEvent) {
        final VirtualFile file = fileEditorManagerEvent.getOldFile();
        if (file != null && (isMMFile(file) || isViewResourceFile(file))) {
            final FileEditor selectedEditor = fileEditorManagerEvent.getManager().getSelectedEditor(file);
            if (selectedEditor != null && selectedEditor.isModified()) {
                final Project project = fileEditorManagerEvent.getManager().getProject();
                final Module  module  = findModuleForFile(file, project);
                if (module != null) FileUtils.saveFile(file);
            }
        }
    }

    //~ Static Fields ................................................................................................................................

    public static final String SOURCES_GENERATED = "Sources Generated";
    public static final String MM_GROUP          = "mmGroup";
}  // end class MMFileEditorListener
