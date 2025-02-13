
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
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.intellij.compiler.actions.CompileActionBase;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.codegen.project.ProjectBuilder;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.exception.ViewsInvalidArgumentTypeException;
import tekgenesis.common.util.Files;
import tekgenesis.lang.mm.FileUtils;
import tekgenesis.lang.mm.MMFileEditorListener;
import tekgenesis.lang.mm.MMHotSwapManager;
import tekgenesis.lang.mm.MMModuleComponent;
import tekgenesis.lang.mm.facet.MMFacet;
import tekgenesis.lang.mm.generation.GeneratedClassesSessionManager;
import tekgenesis.lang.mm.psi.PsiUtils;
import tekgenesis.lang.mm.ui.MMUIInformer;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.mmcompiler.builder.BuilderErrorListener;
import tekgenesis.mmcompiler.builder.BuilderFromAST;
import tekgenesis.repository.ModelRepository;

import static java.util.Collections.singleton;

import static tekgenesis.lang.mm.FileUtils.*;

/**
 * Action to generate source from mm.
 */
@SuppressWarnings("ComponentNotRegistered")
public class GenerateSourcesAction extends CompileActionBase {

    //~ Methods ......................................................................................................................................

    public void update(AnActionEvent event) {
        super.update(event);
        final Presentation presentation = event.getPresentation();
        if (!presentation.isEnabled()) return;
        final DataContext dataContext = event.getDataContext();
        final Module      module      = LangDataKeys.MODULE.getData(dataContext);
        final Module[]    modules     = LangDataKeys.MODULE_CONTEXT_ARRAY.getData(dataContext);
        final boolean     isEnabled   = module != null || modules != null;
        presentation.setEnabled(isEnabled);
        final String actionName = GENERATE_SOURCES_FOR_MODULE;

        final String presentationText;
        if (modules != null) {
            String text = actionName;
            for (int i = 0; i < modules.length; i++) {
                if (text.length() > MAX_LENGTH) {
                    text = GENERATE_SOURCES_FOR_MODULE;
                    break;
                }
                final Module toMake = modules[i];
                if (i != 0) text += ",";
                text += " '" + toMake.getName() + "'";
            }
            presentationText = text;
        }
        else if (module != null) presentationText = actionName + " '" + module.getName() + "'";
        else presentationText = actionName;
        presentation.setText(presentationText);
        presentation.setVisible(isEnabled || !ActionPlaces.PROJECT_VIEW_POPUP.equals(event.getPlace()));
    }

    @Override protected void doAction(DataContext dataContext, Project project) {
        project.save();
        Module[] modules = LangDataKeys.MODULE_CONTEXT_ARRAY.getData(dataContext);
        if (modules == null) {
            final Module module = LangDataKeys.MODULE.getData(dataContext);
            if (module == null) return;
            modules = new Module[] { module };
        }

        for (final Module m : modules)
            if (MMFacet.hasFacet(m)) generateSources(m, true);

        MMUIInformer.showLogMessage(project, MMFileEditorListener.SOURCES_GENERATED);
    }

    //~ Methods ......................................................................................................................................

    /** Updates module repository and generates mm sources if there are no errors. */
    public static void generateSources(@NotNull final Module module, boolean isRefresh) {
        generateSources(module, null, isRefresh);
    }

    /**
     * Updates module repository and generates mm sources for models if there are no errors. If
     * models is empty, it will generate it for all models in module.
     */
    public static void generateSources(@NotNull final Module module, @Nullable VirtualFile modelFile, boolean isRefresh) {
        final VirtualFile mmDirVF = MMModuleComponent.getMMDir(module);
        if (mmDirVF != null) {
            final MMModuleComponent component  = module.getComponent(MMModuleComponent.class);
            final ModelRepository   repository = component.getRepository();
            updateModuleComponent(module, modelFile, component, repository);
            if (!isRefresh && !component.hasErrors()) {
                final Project project           = module.getProject();
                final File    mmDir             = new File(mmDirVF.getPath());
                final boolean test              = Files.toSystemIndependentName(mmDir.getPath()).endsWith("test/mm");
                final String  compilerOutputDir = FileUtils.getCompilerOutputDir(module, test);
                if (compilerOutputDir != null) generateSourcesFiles(module, modelFile, repository, project, mmDir, compilerOutputDir);
            }
        }
    }  // end method generateSources

    private static void generateSourcesFiles(@NotNull Module module, @Nullable VirtualFile modelFile, ModelRepository repository, Project project,
                                             File mmDir, String compilerOutputDir) {
        final File           generatedSourcesFile = new File(MMFacet.getGeneratedSourcesPath(module), "mm");
        final Seq<File>      resourcesDirs        = getResourcesDirs(module);
        final File           outputDir            = new File(compilerOutputDir);
        final ProjectBuilder pb                   = new ProjectBuilder(repository, mmDir, outputDir, generatedSourcesFile, resourcesDirs);
        if (modelFile != null) pb.withModels(singleton(modelFile.getPath()));

        try {
            final List<File> results = pb.buildProject();

            refreshFiles(() -> {
                    final Application application = ApplicationManager.getApplication();
                    application.executeOnPooledThread(() -> MMHotSwapManager.getInstance(project).refreshSessions());
                    DumbService.getInstance(project).runWhenSmart(() -> GeneratedClassesSessionManager.snapshot(project, repository, results));
                },
                getSourceRoots(module));
            refreshAndFindVFile(generatedSourcesFile);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
        catch (final ViewsInvalidArgumentTypeException ignored) {}
    }

    private static void updateModuleComponent(@NotNull Module module, @Nullable VirtualFile modelFile, MMModuleComponent component,
                                              ModelRepository repository) {
        if (modelFile != null && !component.upToDate(modelFile.getTimeStamp())) {
            FileUtils.synchronizeFiles();
            final BuilderErrorListener listener = new BuilderErrorListener.Default();
            final BuilderFromAST       b        = new BuilderFromAST(repository, listener);

            final Set<Tuple<String, MetaModelAST>> tuples = ApplicationManager.getApplication().runReadAction(new GetMmComputable(module));

            ApplicationManager.getApplication().runReadAction(() -> { b.build(tuples); });
            component.updateLastBuilt(listener.hasErrors());
        }
    }

    @NotNull private static Seq<File> getResourcesDirs(@NotNull Module module) {
        return Colls.immutable(Arrays.asList(PsiUtils.getDependentModules(module))).map(m -> {
            final VirtualFile mmDir1 = MMModuleComponent.getMMDir(m);
            if (mmDir1 != null) {
                final VirtualFile resources = mmDir1.getParent().findChild(Constants.RESOURCES);
                if (resources != null) return new File(resources.getPath());
            }
            return null;
        });
    }

    //~ Static Fields ................................................................................................................................

    private static final String GENERATE_SOURCES_FOR_MODULE = "Generate sources for module";

    private static final int MAX_LENGTH = 30;

    //~ Inner Classes ................................................................................................................................

    private static class GetMmComputable implements Computable<Set<Tuple<String, MetaModelAST>>> {
        private final Module module;

        public GetMmComputable(Module module) {
            this.module = module;
        }

        @Override public Set<Tuple<String, MetaModelAST>> compute() {
            return FileUtils.getAllMMFilesRoots(module);
        }
    }
}  // end class GenerateSourcesAction
