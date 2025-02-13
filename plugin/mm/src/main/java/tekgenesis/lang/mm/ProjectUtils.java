
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.PsiDomain;
import tekgenesis.lang.mm.psi.PsiUtils;

import static tekgenesis.common.collections.Colls.seq;

/**
 */
public class ProjectUtils {

    //~ Constructors .................................................................................................................................

    private ProjectUtils() {}

    //~ Methods ......................................................................................................................................

    /** gets a MMFile in a given Project. */
    public static Option<MMFile> findMmFileInProject(Project project, String domain, String name) {
        for (final MMFile mmFile : getAllMMFilesInProject(project)) {
            final PsiDomain domain1           = mmFile.getDomain();
            final String    nameWithExtention = name.endsWith("." + MMFileType.DEFAULT_EXTENSION) ? name : name + "." + MMFileType.DEFAULT_EXTENSION;
            if (domain1 != null && domain.equals(domain1.getDomain()) && mmFile.getName().equals(nameWithExtention)) return Option.some(mmFile);
        }
        return Option.empty();
    }
    /** Finds module by name. */
    @Nullable public static Module findModuleByName(Project project, String name) {
        return ModuleManager.getInstance(project).findModuleByName(name);
    }

    /** gets all MMFiles in a given Project. */
    public static List<MMFile> getAllMMFilesInProject(Project project) {
        final ArrayList<MMFile> mmFiles = new ArrayList<>();

        final Module[] modules = ModuleManager.getInstance(project).getModules();
        for (final Module module : modules) {
            for (final VirtualFile virtualFile : ModuleRootManager.getInstance(module).getSourceRoots()) {
                if (virtualFile.getName().equals(MMFileType.DEFAULT_EXTENSION)) {
                    final PsiDirectory psiDirectory = PsiManager.getInstance(module.getProject()).findDirectory(virtualFile);
                    PsiUtils.getMMFilesInDir(psiDirectory, mmFiles);
                }
            }
        }
        return mmFiles;
    }

    /** Get all Modules for Project. */
    @NotNull public static Seq<Module> getAllModules(@NotNull final Project project) {
        final Module[] modules = ModuleManager.getInstance(project).getModules();

        return seq(Arrays.asList(modules));
    }

    /** gets all Module Names for Project. */
    public static Seq<String> getAllModulesNames(Project project) {
        return getAllModules(project).map(Module::getName);
    }

    /** Get dependent Modules for Module. */
    @NotNull public static Seq<Module> getDependentModules(@NotNull final Module module) {
        final Module[] modules = PsiUtils.getDependentModules(module);
        return seq(Arrays.asList(modules));
    }

    /** Returns true if is the Sui Generis project. */
    public static boolean isSuiGenerisProject(Project project) {
        return findModuleByName(project, Constants.PLUGIN_MM) != null;
    }
}  // end class ProjectUtils
