
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.*;
import com.intellij.psi.PsiManager;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Constants;
import tekgenesis.lang.mm.actions.GenerateSourcesAction;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.PsiDomain;
import tekgenesis.repository.ModelRepository;

import static com.intellij.openapi.module.ModuleUtil.findModuleForFile;

import static tekgenesis.lang.mm.FileUtils.isViewResourceFile;
import static tekgenesis.lang.mm.MMFileType.isMMFile;
import static tekgenesis.lang.mm.psi.PsiUtils.getPathToSourceRoot;

/**
 * Note that the VFS listeners are application-level, and will receive events for changes happening
 * in all the projects opened by the user. You may need to filter out events which aren't relevant
 * to your task.
 */
class MMProjectListener implements VirtualFileListener {

    //~ Instance Fields ..............................................................................................................................

    private final Project project;

    //~ Constructors .................................................................................................................................

    /** Listener in charge of managing all MMFile events. */
    public MMProjectListener(Project project) {
        this.project = project;
    }

    //~ Methods ......................................................................................................................................

    @Override public void beforeContentsChange(@NotNull VirtualFileEvent event) {}

    @Override public void beforeFileDeletion(@NotNull VirtualFileEvent event) {}

    @Override public void beforeFileMovement(@NotNull VirtualFileMoveEvent event) {
        final VirtualFile vf = event.getFile();
        if (isMMFile(vf)) {
            final Module module = findModuleForFile(vf, project);
            if (module != null) {
                final ModelRepository repository = module.getComponent(MMModuleComponent.class).getRepository();
                repository.deleteAll(vf.getPath());
            }
        }
    }

    @Override public void beforePropertyChange(@NotNull VirtualFilePropertyEvent event) {}

    @Override public void contentsChanged(@NotNull VirtualFileEvent event) {
        final VirtualFile vf = event.getFile();
        if (isMMFile(vf) || isViewResourceFile(vf)) {
            final Module module = findModuleForFile(vf, project);
            if (module != null) GenerateSourcesAction.generateSources(module, vf, event.isFromRefresh());
        }
    }

    @Override public void fileCopied(@NotNull VirtualFileCopyEvent event) {
        fileAdded(event.getFile());
    }

    @Override public void fileCreated(@NotNull VirtualFileEvent event) {}

    @Override public void fileDeleted(@NotNull VirtualFileEvent event) {
        final VirtualFile vf = event.getFile();
        if (isMMFile(vf)) {
            try {
                final Module module = findModuleForFile(vf, project);
                if (module != null) {
                    final ModelRepository repository = module.getComponent(MMModuleComponent.class).getRepository();
                    repository.deleteAll(vf.getPath());
                }
            }
            catch (final IllegalStateException e) {
                logger.debug("Error in listener");
            }
        }
    }

    @Override public void fileMoved(@NotNull VirtualFileMoveEvent event) {
        final VirtualFile vf = event.getFile();
        if (isMMFile(vf)) {
            final Module module = findModuleForFile(vf, project);
            if (module != null) {
                final MMFile mmfile = (MMFile) PsiManager.getInstance(project).findFile(vf);
                if (mmfile != null) {
                    final PsiDomain domain = mmfile.getDomain();
                    TextRange       range  = new TextRange(1, 1);

                    if (domain != null) range = domain.getTextRange();

                    final String newPackageString = Constants.PACKAGE_SPC + getPathToSourceRoot(mmfile) + ";";
                    mmfile.replaceText(range.getStartOffset(), range.getEndOffset(), newPackageString);

                    FileUtils.synchronizeFiles();

                    GenerateSourcesAction.generateSources(module, vf, !event.isFromRefresh());
                }
            }
        }
    }

    @Override public void propertyChanged(@NotNull VirtualFilePropertyEvent event) {}

    private void fileAdded(VirtualFile file) {
        if (isMMFile(file)) {
            final Module module = findModuleForFile(file, project);
            if (module != null) GenerateSourcesAction.generateSources(module, file, true);
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getInstance(MMProjectListener.class);
}  // end class MMProjectListener
