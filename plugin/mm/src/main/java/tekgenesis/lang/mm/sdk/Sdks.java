
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.sdk;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;

import org.jetbrains.annotations.Nullable;

/**
 * Util class for sdks.
 */
public class Sdks {

    //~ Constructors .................................................................................................................................

    private Sdks() {}

    //~ Methods ......................................................................................................................................

    /** Sdk from file element. */
    @Nullable public static Sdk moduleSdkFromFile(@Nullable PsiFile f) {
        if (f == null) return null;

        final Project     project = f.getProject();
        final VirtualFile file    = f.getVirtualFile();

        return getSdk(file, project);
    }

    /** Get module sdk from action. */
    @Nullable public static Sdk sdkFromAction(final AnActionEvent e) {
        final VirtualFile file    = CommonDataKeys.VIRTUAL_FILE.getData(e.getDataContext());
        final Project     project = e.getProject();

        return getSdk(file, project);
    }

    @Nullable private static Sdk getSdk(@Nullable VirtualFile file, @Nullable Project project) {
        if (file != null && project != null) {
            final Module module = ModuleUtilCore.findModuleForFile(file, project);

            if (module != null) return ModuleRootManager.getInstance(module).getSdk();
        }
        return null;
    }
}
