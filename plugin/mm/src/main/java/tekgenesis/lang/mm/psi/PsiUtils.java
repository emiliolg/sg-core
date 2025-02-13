
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.intellij.ide.DataManager;
import com.intellij.ide.IdeView;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.ProjectScope;
import com.intellij.usageView.UsageInfo;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.collections.Stack;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.common.util.JavaReservedWords;
import tekgenesis.intellij.CommonPsiElement;
import tekgenesis.lang.mm.*;
import tekgenesis.lang.mm.psi.PsiMethodReference.MethodDescriptor;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModel;

import static com.intellij.codeInsight.CodeInsightUtilCore.findElementInRange;
import static com.intellij.psi.search.GlobalSearchScope.allScope;
import static com.intellij.psi.search.GlobalSearchScope.getScopeRestrictedByFileTypes;

import static junit.framework.Assert.assertNotNull;

import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.lang.mm.FileUtils.findVirtualFile;

/**
 * Useful Utils for working with Psi.
 */
@SuppressWarnings("ClassWithTooManyMethods")
public class PsiUtils {

    //~ Constructors .................................................................................................................................

    private PsiUtils() {}

    //~ Methods ......................................................................................................................................

    /** Convert PsiReference references to UsageInfo[]. Fill set with affected files. */
    public static UsageInfo[] convertReferencesToUsageInfo(@NotNull final Collection<PsiReference> references,
                                                           @NotNull final Set<PsiFile>             affectedFiles) {
        final UsageInfo[] usages = new UsageInfo[references.size()];
        int               i      = 0;
        for (final PsiReference reference : references) {
            final UsageInfo usage = new UsageInfo(reference);
            affectedFiles.add(usage.getFile());
            usages[i++] = usage;
        }
        return usages;
    }

    /** Util for creating a PsiFile. */
    public static PsiFile createFile(final Project project, final VirtualFile vDir, final String fileName, final String text) {
        return new WriteAction<PsiFile>() {
                @Override protected void run(@NotNull Result<PsiFile> result)
                    throws Throwable
                {
                    final VirtualFile vFile = vDir.createChildData(vDir, fileName);
                    VfsUtil.saveText(vFile, text);
                    assertNotNull(vFile);
                    final PsiManager myPsiManager = PsiManager.getInstance(project);
                    final PsiFile    file         = myPsiManager.findFile(vFile);
                    assertNotNull(file);
                    result.setResult(file);
                }
            }.execute().getResultObject();
    }

    /** Find expression in range.* */
    public static PsiElement findExpressionInRange(PsiFile file, int startOffset, int endOffset) {
        return findElementInRange(file, startOffset, endOffset, PsiElement.class, MMLanguage.INSTANCE);
    }

    /** Find MMFile. */
    @NotNull public static Option<MMFile> findMMFile(@NotNull final Project project, @NotNull final String path) {
        final VirtualFile virtualFile = findVirtualFile(path);

        Option<MMFile> result = empty();

        if (virtualFile != null) {
            final PsiFile file = PsiManager.getInstance(project).findFile(virtualFile);
            if (file instanceof MMFile) result = some((MMFile) file);
        }
        return result;
    }

    /** Finds MMFile, more complete than FindMMFile, looks in jars dependencies. */
    public static Option<MMFile> findMMFileIncludingNonProjectItems(@NotNull final Project project, @NotNull Module module,
                                                                    @NotNull final String path) {
        Option<MMFile> result = findMMFile(project, path);
        if (!result.isPresent()) {
            final String  pathName = path.substring(5);
            final PsiFile file     = findChildInRoots(project, module.getComponent(MMModuleComponent.class).getLibraryRoots(), pathName);
            if (file instanceof MMFile) result = Option.some((MMFile) file);
        }

        return result;
    }

    /** Return optional parent field. */
    public static Option<PsiModelField> findParentField(@Nullable final PsiElement element) {
        return findParentOfType(element, PsiModelField.class);
    }

    /** Return optional parent meta model. */
    public static Option<PsiMetaModel<?>> findParentModel(@Nullable final PsiElement element) {
        return cast(findParentOfType(element, PsiMetaModel.class));
    }

    /** Return optional parent field of given type. */
    public static <T extends PsiElement> Option<T> findParentOfType(@Nullable final PsiElement element, @NotNull Class<T> type) {
        PsiElement parent = element;
        while (parent != null) {
            if (type.isInstance(parent)) return some(type.cast(parent));
            else parent = parent.getParent();
        }
        return empty();
    }

    /** Return optional parent field of given type. */
    public static Option<PsiElement> findParentWithElementType(@Nullable final PsiElement element, @NotNull MMElementType type) {
        PsiElement parent = element;
        while (parent != null) {
            if (parent instanceof CommonPsiElement && ((CommonPsiElement<?, ?>) parent).getElementType() == type) return some(parent);
            else parent = parent.getParent();
        }
        return empty();
    }

    /** Gets PsiDirectory for VirtualFile. */
    @Nullable public static PsiDirectory findPsiDirectory(Module module, VirtualFile file) {
        return PsiManager.getInstance(module.getProject()).findDirectory(file);
    }

    /** Find PsiMetaModel for given project and repository. */
    public static Option<PsiMetaModel<?>> findPsiMetaModel(@NotNull Project project, Module module, @NotNull ModelRepository repository,
                                                           @NotNull QName fqn) {         //
        final String modelName = fqn.getName();
        return repository.getModel(fqn).flatMap(mm ->
                findMMFileIncludingNonProjectItems(project, module, mm.getSourceName())  //
                .flatMap(f -> f.getMetaModel(modelName)));
    }

    /**
     * Opens editor with specified element and scrolls to the element.
     *
     * @param  elem  PSI element
     */
    @Nullable public static Editor scrollTo(PsiElement elem) {
        final Editor editor = openEditor(elem);
        if (editor == null) return null;
        editor.getCaretModel().moveToOffset(elem.getTextOffset());
        editor.getScrollingModel().scrollToCaret(ScrollType.MAKE_VISIBLE);
        return editor;
    }

    /** returns all MMFiles in Module. */
    public static List<MMFile> getAllMMFilesInModule(Module module) {
        final ArrayList<MMFile> mmFiles = new ArrayList<>();

        for (final VirtualFile virtualFile : ModuleRootManager.getInstance(module).getSourceRoots()) {
            if (virtualFile.getName().equals(MMFileType.DEFAULT_EXTENSION)) {
                final PsiDirectory psiDirectory = PsiManager.getInstance(module.getProject()).findDirectory(virtualFile);
                getMMFilesInDir(psiDirectory, mmFiles);
            }
        }

        return mmFiles;
    }

    /** returns all Dependent Modules of Modules. */
    public static Module[] getDependentModules(Module module) {
        return ModuleRootManager.getInstance(module).getDependencies(false);
    }

    /** Gets Editor. */
    @Nullable public static Editor getEditor(@NotNull DataContext dataContext) {
        return PlatformDataKeys.EDITOR_EVEN_IF_INACTIVE.getData(dataContext);
    }
    /** Gets Implementation Class string for form. */
    public static String getImplementationClass(@NotNull final PsiMetaModel<?> model) {
        return model.getDomain() + "." + model.getName();
    }
    /** get LibRepository for Module. */
    public static ModelRepository getLibRepository(@Nullable final Module module) {
        if (module == null) return new ModelRepository();
        final ModelRepository libRepo = module.getComponent(MMModuleComponent.class).getLibrariesRepository();
        return libRepo == null ? new ModelRepository() : libRepo;
    }

    /** returns all MMFiles in Directory. */
    public static void getMMFilesInDir(@Nullable PsiDirectory psiDirectory, ArrayList<MMFile> mmFiles) {
        if (psiDirectory != null) {
            for (final PsiElement psiElement : psiDirectory.getChildren()) {
                if (psiElement instanceof PsiDirectory) getMMFilesInDir((PsiDirectory) psiElement, mmFiles);
                else if (MMFileType.isMMFile(psiElement)) mmFiles.add((MMFile) psiElement);
            }
        }
    }
    /** get ModelRepository for Module. */
    public static ModelRepository getModelRepository(@Nullable final Module module) {
        if (module == null) return new ModelRepository();
        final MMModuleComponent component = module.getComponent(MMModuleComponent.class);
        return component.getRepository();
    }

    /** Gets ModelRepository for element. */
    public static Option<ModelRepository> getModelRepository(@NotNull final PsiElement element) {
        final Module module = getModule(element);
        return module != null ? Option.of(getModelRepository(module)) : empty();
    }

    /** Gets Module for element. */
    @Nullable public static Module getModule(@NotNull final PsiElement element) {
        return ModuleUtil.findModuleForPsiElement(element);
    }

    /** Gets Module for element. */
    @NotNull public static Module getModuleOrFail(@NotNull final PsiElement element) {
        return ensureNotNull(getModule(element));
    }

    /** Finds NavigationItem(ej: Entity, Enum...) for MetaModel */
    public static Option<PsiMetaModel<?>> getNavigationItemForMetaModel(Project project, Module module, final MetaModel metaModel,
                                                                        boolean includeNonProjectItems) {
        final String sourcePath = metaModel.getSourceName();

        return findMMFile(project, module, sourcePath, includeNonProjectItems).flatMap(file -> file.getMetaModel(metaModel.getName()));
    }
    /**
     * Package name of Java file with specified element, or null if the element isn't in Java file.
     *
     * @param   elem  PSI element
     *
     * @return  package name of Java file with specified element
     */
    @Nullable public static PsiPackage getPackage(PsiElement elem) {
        final PsiFile file = elem.getContainingFile();
        if (!(file instanceof PsiJavaFile)) return null;
        final PsiJavaFile javaFile = (PsiJavaFile) file;
        return JavaPsiFacade.getInstance(elem.getProject()).findPackage(javaFile.getPackageName());
    }

    /** Returns the path to a file's source root. */
    @NotNull public static String getPathToSourceRoot(@NotNull final MMFile file) {
        final VirtualFile   root  = getSourceRootForFile(file);
        final Stack<String> stack = Stack.createStack();

        if (root != null) {
            PsiDirectory domain = file.getParent();
            while (domain != null && !root.equals(domain.getVirtualFile())) {
                stack.push(domain.getName());
                domain = domain.getParent();
            }
        }

        return stack.mkString(".");
    }

    /** Get project from focus context. */
    @Nullable public static Project getProject() {
        final DataContext dataContext = DataManager.getInstance().getDataContextFromFocus().getResult();
        return PlatformDataKeys.PROJECT.getData(dataContext);
    }
    /** Gets all classes in a specified Domain. */
    public static PsiClass[] getPsiClassesForDirectory(Module module, String domain) {
        for (final VirtualFile virtualFile : FileUtils.getSourceRoots(module)) {
            if ("java".equals(virtualFile.getName())) {
                final VirtualFile vFile = findVirtualFile(virtualFile.getPath() + "/" + domain.replaceAll("\\.", "/"));
                if (vFile != null && vFile.getChildren().length != 0) {
                    final PsiDirectory psiDirectory = findPsiDirectory(module, vFile);
                    if (psiDirectory != null) JavaDirectoryService.getInstance().getClasses(psiDirectory);
                }
            }
        }
        return EMPTY_PSI_CLASSES;
    }
    /** gets PsiClassForComposite. */
    @Nullable public static PsiClass getPsiClassForComposite(MMCommonComposite composite, MetaModel model) {
        return JavaPsiFacade.getInstance(composite.getProject())
               .findClass(model.getFullName(), GlobalSearchScope.projectScope(composite.getProject()));
    }

    /** Gets PsiClass for given full qualified name. */
    @Nullable public static PsiClass getPsiClassForFqnNullable(@NotNull final Project project, @NotNull final String fqn) {
        return JavaPsiFacade.getInstance(project).findClass(fqn, GlobalSearchScope.projectScope(project));
    }

    /** True if given class inherits from {@link EntityInstance}. */
    public static boolean isUserMetaModelClass(@NotNull Project project, @NotNull PsiClass userClass) {
        return inheritsFromMetaModelInstanceClass(project, userClass);
    }

    /** Return source root folder for given mm file. */
    @Nullable public static VirtualFile getSourceRootForFile(@NotNull final MMFile file) {
        final ProjectFileIndex index   = ProjectRootManager.getInstance(file.getProject()).getFileIndex();
        final VirtualFile      virtual = file.getVirtualFile();
        return virtual != null ? index.getSourceRootForFile(virtual) : null;
    }

    /** Get directory from focus context. Selected directory in IDE view */
    @Nullable public static PsiDirectory getViewDirectory(AnActionEvent event) {
        final IdeView view = LangDataKeys.IDE_VIEW.getData(event.getDataContext());

        return view == null ? null : view.getOrChooseDirectory();
    }

    /** Return MMFile form Text. */
    static MMFile createMMFileFromText(Project project, String filename, String text) {
        return (MMFile) PsiFileFactory.getInstance(project).createFileFromText(filename, MMFileType.INSTANCE, text);
    }

    /** Return first node found in file for the given name. */
    @Nullable static MetaModelAST findAnyNodeInFileByName(MMFile mmFile, String name) {
        final Pattern pattern = Pattern.compile("(\\W)" + name + "(\\W)");
        final Matcher matcher = pattern.matcher(mmFile.getText());
        return matcher.find() ? (MetaModelAST) mmFile.findElementAt(matcher.start() + 1) : null;
    }
    /** finds mm file in library Roots. */
    @Nullable static PsiFile findChildInRoots(Project project, VirtualFile[] libraryRoots, String sourceName) {
        final String[] split = sourceName.split("!");
        for (final VirtualFile libraryRoot : libraryRoots) {
            if (libraryRoot.getPath().equals(split[0] + "!/")) return findChildInJar(project, libraryRoot, split[1]);
        }
        return null;
    }
    /** Resolves a Java Class for a given metamodel. */
    @Nullable static PsiClass resolveJavaClass(@NotNull Project project, @NotNull String fqn) {
        return JavaPsiFacade.getInstance(project).findClass(fqn, getScopeRestrictedByFileTypes(allScope(project), JavaFileType.INSTANCE));
    }
    /** Gets PsiMethod form a method reference in a specified Class. */
    static Option<PsiMethod> resolveJavaMethod(@Nullable PsiClass clazz, @NotNull String methodName, @Nullable MethodDescriptor descriptor) {
        PsiMethod result = null;
        if (clazz != null && isNotEmpty(methodName)) {
            final Seq<PsiMethod> methods = findMethodsByName(clazz, methodName);
            if (!methods.isEmpty()) {
                result = methods.getFirst().get();
                // Choose most suitable method from all available, or default to first one
                if (descriptor != null) {
                    for (final PsiMethod method : methods) {
                        if (descriptor.accepts(method)) {
                            result = method;
                            break;
                        }
                    }
                }
            }
        }
        return Option.ofNullable(result);
    }
    /** Gets PsiClass for given full qualified name. */
    static Option<PsiClass> getPsiClassForFqn(@NotNull final Project project, @NotNull final String fqn) {
        return Option.ofNullable(JavaPsiFacade.getInstance(project).findClass(fqn, GlobalSearchScope.projectScope(project)));
    }

    @Nullable
    @SuppressWarnings("UnsafeVfsRecursion")
    private static PsiFile findChildInJar(Project project, VirtualFile libraryRoot, String s) {
        final String[] split = s.split("/");
        for (final VirtualFile virtualFile : libraryRoot.getChildren()) {
            if (virtualFile.getName().equals(split[1])) {
                if (split.length <= 2) return PsiManager.getInstance(project).findFile(virtualFile);
                else return findChildInJar(project, virtualFile, s.split(split[1])[1]);
            }
        }
        return null;
    }

    private static Seq<PsiMethod> findMethodsByName(PsiClass clazz, String methodName) {
        return ImmutableList.fromArray(clazz.findMethodsByName(methodName, true)).filter(psiMethod ->
                psiMethod != null && !psiMethod.hasModifierProperty(JavaReservedWords.ABSTRACT));
    }

    /** Find MMFile (boolean to include non project items). */
    @NotNull private static Option<MMFile> findMMFile(@NotNull final Project project, @NotNull Module module, @NotNull final String path,
                                                      boolean includeNonProjectItems) {
        return includeNonProjectItems ? findMMFileIncludingNonProjectItems(project, module, path) : findMMFile(project, path);
    }

    private static boolean inheritsFromMetaModelInstanceClass(@NotNull Project project, @NotNull PsiClass userClass) {
        final GlobalSearchScope scope = ProjectScope.getLibrariesScope(project);

        final PsiClass entity = JavaPsiFacade.getInstance(project).findClass(ENTITY_INSTANCE, scope);
        if (entity != null && userClass.isInheritor(entity, true)) return true;

        final PsiClass form = JavaPsiFacade.getInstance(project).findClass(FORM_INSTANCE, scope);
        if (form != null && userClass.isInheritor(form, true)) return true;

        final PsiClass handler = JavaPsiFacade.getInstance(project).findClass(HANDLER_INSTANCE, scope);
        return handler != null && userClass.isInheritor(handler, true);
    }

    /**
     * Opens file with specified element in IDE editor.
     *
     * @param   elem  PSI element
     *
     * @return  file editor for opened file
     */
    @Nullable private static Editor openEditor(PsiElement elem) {
        if (elem == null) return null;

        final PsiFile psiFile = elem.getContainingFile();
        if (psiFile == null) return null;
        final VirtualFile file = psiFile.getVirtualFile();
        if (file == null) return null;
        final OpenFileDescriptor descriptor = new OpenFileDescriptor(elem.getProject(), file);
        return FileEditorManager.getInstance(elem.getProject()).openTextEditor(descriptor, true);
    }

    //~ Static Fields ................................................................................................................................

    private static final PsiClass[] EMPTY_PSI_CLASSES = new PsiClass[0];
}  // end class PsiUtils
