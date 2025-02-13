
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.generation;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.MultiMap;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Tuple2;
import tekgenesis.common.util.Sha;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModel;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.Predefined.option;
import static tekgenesis.common.collections.MultiMap.createMultiMap;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.lang.mm.FileUtils.findVirtualFile;
import static tekgenesis.lang.mm.psi.PsiUtils.isUserMetaModelClass;

/**
 * Generated classes session manager controls the newly added user classes to delete them if meta
 * model is deleted and classes has no modifications.
 */
public class GeneratedClassesSessionManager {

    //~ Constructors .................................................................................................................................

    private GeneratedClassesSessionManager() {}

    //~ Methods ......................................................................................................................................

    /** Register generated classes if applicable. */
    public static void snapshot(Project project, ModelRepository repository, List<File> generated) {
        registerNewClasses(project, generated);
        deleteOldClasses(project, repository);
    }

    private static Option<PsiJavaFile> asPsiJavaFile(Project project, VirtualFile file) {
        Option<PsiJavaFile> result = Option.empty();
        if (file != null && JavaFileType.INSTANCE == file.getFileType()) {
            final PsiFile psi = PsiManager.getInstance(project).findFile(file);
            result = option(psi).castTo(PsiJavaFile.class);
        }
        return result;
    }

    private static void deleteOldClasses(final Project project, ModelRepository repository) {
        final Seq<String> last = repository.getModels().map(MetaModel::getFullName);  // Repository models
        final Set<String> keys = models.get(project).into(new HashSet<>());           // Snapshot models
        keys.removeAll(last.toList());
        for (final String removed : keys)
            removeClassIfNotModified(project, removed);
        models.removeAll(project);                                                    // Update snapshot
        last.into(models.put(project));
    }

    @Nullable private static String findBaseClass(Project project, VirtualFile user) {
        for (final PsiJavaFile javaFile : asPsiJavaFile(project, user)) {
            for (final PsiClass javaClass : javaFile.getClasses()) {
                final PsiClass superClass = javaClass.getSuperClass();
                final String   name       = superClass == null ? null : superClass.getName();
                if (name != null && name.endsWith("Base")) return superClass.getQualifiedName();
            }
        }
        return null;
    }

    private static void register(@NotNull String fqn, @NotNull VirtualFile file) {
        for (final JavaMetaModelFile snap : JavaMetaModelFile.createSnap(file))
            classes.put(fqn, snap);
    }

    private static void registerNewClasses(Project project, List<File> generated) {
        for (final File g : generated) {
            final VirtualFile virtualFile = findVirtualFile(g.getPath());
            if (virtualFile != null) {
                for (final PsiJavaFile file : asPsiJavaFile(project, virtualFile)) {
                    for (final PsiClass javaClass : file.getClasses()) {
                        final String fqn = javaClass.getQualifiedName();
                        if (isUserMetaModelClass(project, javaClass) && fqn != null) register(fqn, virtualFile);
                    }
                }
            }
        }
    }

    private static void removeClassIfNotModified(@NotNull Project project, @NotNull String removed) {
        final JavaMetaModelFile snap = classes.get(removed);
        if (snap != null) {
            final VirtualFile user = findVirtualFile(snap.path());
            if (user != null) {
                if (snap.matches(user)) {
                    try {
                        final String base = findBaseClass(project, user);
                        if (isNotEmpty(base)) removeClassIfNotModified(project, base);
                        user.delete(project);
                    }
                    catch (final IOException ignored) {}
                }
            }
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final Map<String, JavaMetaModelFile> classes = new HashMap<>();
    private static final MultiMap<Project, String>      models  = createMultiMap();

    //~ Inner Classes ................................................................................................................................

    private static class JavaMetaModelFile extends Tuple2<String, String> {
        private JavaMetaModelFile(@NotNull String path, @NotNull String sha) {
            super(path, sha);
        }

        private boolean matches(@NotNull VirtualFile other) {
            for (final String sha : shaForFile(other))
                return second().equals(sha);
            return false;
        }

        private String path() {
            return first();
        }

        private static Option<JavaMetaModelFile> createSnap(@NotNull VirtualFile file) {
            final Option<String>      sha    = shaForFile(file);
            final String              path   = file.getCanonicalPath();
            Option<JavaMetaModelFile> result = Option.empty();
            if (sha.isPresent() && path != null) result = some(new JavaMetaModelFile(path, sha.get()));
            return result;
        }

        private static Option<String> shaForFile(@NotNull VirtualFile file) {
            final Sha      sha    = new Sha();
            Option<String> result = Option.empty();
            try {
                sha.process(file.getInputStream());
                result = some(sha.getDigestAsString());
            }
            catch (final IOException ignored) {}
            return result;
        }

        private static final long serialVersionUID = 3833922509127012014L;
    }
}  // end class GeneratedClassesSessionManager
