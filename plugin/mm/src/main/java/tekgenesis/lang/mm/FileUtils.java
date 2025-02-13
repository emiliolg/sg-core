
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.CompilerModuleExtension;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.openapi.vfs.newvfs.NewVirtualFile;
import com.intellij.openapi.vfs.newvfs.RefreshQueue;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiPackage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.util.Files;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.PsiUtils;
import tekgenesis.mmcompiler.ast.MetaModelAST;

import static com.intellij.openapi.vfs.VirtualFileManager.getInstance;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Constants.DEFAULT_PACKAGE;
import static tekgenesis.common.core.Constants.TEMPLATE_EXTS;
import static tekgenesis.common.core.Tuple.tuple;

/**
 * Useful Utils for Managing Files in Idea.
 */
public class FileUtils {

    //~ Constructors .................................................................................................................................

    private FileUtils() {}

    //~ Methods ......................................................................................................................................

    /**
     * Checks file name in tuple (split by dot) and creates subdirectories in given directory, until
     * no more dots are found in file name. If creating directories, should run inside
     *
     * @see  Application#runWriteAction(Runnable)
     */
    @NotNull public static Tuple<PsiDirectory, String> checkAndCreateDirectoriesForFileName(PsiDirectory directory, String fileName,
                                                                                            String extension) {
        String className = fileName;

        if (StringUtil.isNotEmpty(extension)) className = StringUtil.trimEnd(className, "." + extension);

        PsiDirectory dir = directory;
        if (className.contains(".")) {
            final String[] names = className.split("\\.");

            for (int i = 0; i < names.length - 1; i++) {
                final String name   = names[i];
                PsiDirectory subDir = dir.findSubdirectory(name);

                if (subDir == null) subDir = dir.createSubdirectory(name);

                dir = subDir;
            }

            className = names[names.length - 1];
        }
        return tuple(dir, className);
    }

    /** Check if a file can be created in the current IDE context. */
    public static boolean fileCanBeCreatedInIde(final DataContext dataContext) {
        final Project project = PlatformDataKeys.PROJECT.getData(dataContext);
        final IdeView view    = LangDataKeys.IDE_VIEW.getData(dataContext);
        if (project == null || view == null || view.getDirectories().length == 0) return false;

        final ProjectFileIndex projectFileIndex = ProjectRootManager.getInstance(project).getFileIndex();
        for (final PsiDirectory dir : view.getDirectories()) {
            if (projectFileIndex.isInSourceContent(dir.getVirtualFile()) && checkPackageExists(dir)) return true;
        }
        return false;
    }

    /** gets virtual file in Path. */
    @Nullable public static VirtualFile findVirtualFile(String path) {
        return getInstance().findFileByUrl("file://" + path);
    }

    /** find VFile for given Path. */
    @Nullable public static VirtualFile refreshAndFindVFile(String filePath) {
        return getInstance().refreshAndFindFileByUrl("file://" + filePath);
    }

    /** find VFile for given File and refresh given file and predecessors. */
    @Nullable public static VirtualFile refreshAndFindVFile(final File file) {
        return VfsUtil.findFileByIoFile(file, true);
    }

    /** Refresh files. */
    public static void refreshFiles(Runnable runnable, VirtualFile... files) {
        for (final VirtualFile file : files) {
            final VirtualFileSystem fs = file.getFileSystem();
            if (fs instanceof LocalFileSystem && file instanceof NewVirtualFile) ((NewVirtualFile) file).markDirtyRecursively();
        }
        RefreshQueue.getInstance().refresh(true, true, runnable, files);
    }

    /** find VFile for given Path. */
    @Nullable public static VirtualFile refreshVFile(String filePath) {
        return getInstance().refreshAndFindFileByUrl("file://" + filePath);
    }

    /** Save and synchronize files. */
    public static void saveAndSynchronizeFiles() {
        saveFiles();
        synchronizeFiles();
    }

    /** Save a file. */
    public static void saveFile(@NotNull final VirtualFile file) {
        final Document document = FileDocumentManager.getInstance().getDocument(file);
        if (document != null) FileDocumentManager.getInstance().saveDocument(document);
    }

    /** Saves a set of files. */
    public static void saveFiles(@NotNull final Iterable<PsiFile> files) {
        for (final PsiFile file : files) {
            final VirtualFile virtual = file.getVirtualFile();
            if (virtual != null) saveFile(virtual);
        }
    }

    /** Synchronize files. */
    public static void synchronizeFiles() {
        getInstance().refreshWithoutFileWatcher(true);
    }

    /** runs changes on Disk and Executes Runnable. */
    public static void updateChangesOnDiskAndRun(final Runnable runnable) {
        final Application manager = ApplicationManager.getApplication();
        manager.invokeLater(() ->
                manager.runWriteAction(() -> {
                    FileDocumentManager.getInstance().saveAllDocuments();
                    getInstance().asyncRefresh(runnable);
                }));
    }

    /** Return a Set Of all MMFiles In Module. */
    @NotNull public static Set<Tuple<String, MetaModelAST>> getAllMMFilesRoots(@NotNull final Module module) {
        final VirtualFile                          mm     = getMMSourceRoot(module);
        final HashSet<Tuple<String, MetaModelAST>> result = new HashSet<>();
        if (mm != null) {
            for (final VirtualFile virtualFile : mm.getChildren())
                findMMFiles(virtualFile, result, module.getProject());
        }
        return result;
    }

    /** gets Compiler Output directory. */
    @Nullable public static String getCompilerOutputDir(Module module, boolean test) {
        final CompilerModuleExtension instance = CompilerModuleExtension.getInstance(module);
        if (instance != null) {
            final String url = test ? instance.getCompilerOutputUrlForTests() : instance.getCompilerOutputUrl();
            if (url != null) {
                try {
                    final File outputFile = new File(Files.getURIWithoutFileAuthority(url));
                    outputFile.mkdirs();
                    return outputFile.getAbsolutePath();
                }
                catch (final URISyntaxException ignored) {}
            }
        }
        return null;
    }

    /** gets Java Source Root in Module. */
    @Nullable public static VirtualFile getJavaSourceRoot(Module module) {
        for (final VirtualFile virtualFile : getSourceRoots(module)) {
            if (virtualFile.getPath().contains("/src/main/java")) return virtualFile;
        }
        return null;
    }

    /** Returns a directory creating it if it not exists. */
    @Nullable public static VirtualFile getOrCreateDirectory(Project project, @NotNull VirtualFile parent, String name) {
        final VirtualFile child = parent.findChild(name);
        try {
            return child != null ? child : parent.createChildDirectory(project, name);
        }
        catch (final IOException ignore) {
            return null;
        }
    }

    /**
     * Get package name for given directory. If no package name is found, change directory to
     * default and return new directory with default package.
     */
    @NotNull public static Tuple<PsiDirectory, PsiPackage> getPackageForDirectory(@NotNull final PsiDirectory psiDirectory) {
        PsiDirectory directory = psiDirectory;
        PsiPackage   aPackage  = JavaDirectoryService.getInstance().getPackage(directory);
        if (aPackage == null || isEmpty(aPackage.getQualifiedName())) {
            directory = psiDirectory.findSubdirectory(DEFAULT_PACKAGE);
            if (directory == null) directory = psiDirectory.createSubdirectory(DEFAULT_PACKAGE);
            aPackage = JavaDirectoryService.getInstance().getPackage(directory);
        }
        assert aPackage != null;
        return tuple(directory, aPackage);
    }

    /** Get Resources Root in Module. */
    @Nullable public static VirtualFile getResourcesRoot(@Nullable Module module) {
        if (module == null) return null;
        for (final VirtualFile virtualFile : getSourceRoots(module)) {
            if (virtualFile.getPath().contains(MetaModelJpsUtils.RESOURCES_PATH)) return virtualFile;
        }
        return null;
    }

    /** Get Source Roots in Module. */
    public static VirtualFile[] getSourceRoots(@NotNull final Module module) {
        return ModuleRootManager.getInstance(module).getSourceRoots();
    }

    /** Return true if vf has accepted resource extension. */
    static boolean isResourceFile(final String path) {
        return path.contains("/src/main/resources/");
    }

    /** Return true if vf has accepted resource view template extension. */
    static boolean isViewResourceFile(VirtualFile vf) {
        return TEMPLATE_EXTS.contains(vf.getExtension());
    }

    private static boolean checkPackageExists(PsiDirectory directory) {
        return JavaDirectoryService.getInstance().getPackage(directory) != null;
    }

    @SuppressWarnings("UnsafeVfsRecursion")
    private static void findMMFiles(VirtualFile virtualFile, HashSet<Tuple<String, MetaModelAST>> mmFiles, Project project) {
        for (final VirtualFile file : virtualFile.getChildren()) {
            if (file.getChildren().length > 0) findMMFiles(file, mmFiles, project);
            else {
                final Option<MMFile> mmFile = PsiUtils.findMMFile(project, file.getPath());
                if (mmFile.isPresent()) mmFiles.add(tuple(mmFile.get().getPath(), mmFile.get().getFirstRoot()));
            }
        }
    }  // end method findMMFiles

    /** Save all files. */
    private static void saveFiles() {
        ApplicationManager.getApplication().runWriteAction(() -> {
            FileDocumentManager.getInstance().saveAllDocuments();
            getInstance().syncRefresh();
        });
    }

    /** Get MM Source Root in Module. */
    @Nullable private static VirtualFile getMMSourceRoot(Module module) {
        for (final VirtualFile virtualFile : getSourceRoots(module)) {
            if (virtualFile.getPath().contains(MetaModelJpsUtils.META_MODEL_SOURCES_PATH)) return virtualFile;
        }
        return null;
    }

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String GENERATED = Constants.GENERATED;

    public static final String TEST_CLASSES = "test-classes";

    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String TARGET = "target";
}  // end class FileUtils
