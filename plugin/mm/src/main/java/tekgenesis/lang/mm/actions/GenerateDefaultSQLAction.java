
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

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.util.PsiUtilBase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.lang.mm.FileUtils;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.ui.MMUIInformer;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.codegen.sql.SqlCodeGenerator.generateSchema;

/**
 */
@SuppressWarnings("WeakerAccess")
public class GenerateDefaultSQLAction extends MMAction {

    //~ Methods ......................................................................................................................................

    @Override public void actionPerformed(@NotNull AnActionEvent e) {
        final DataContext dataContext = e.getDataContext();
        final Module      module      = LangDataKeys.MODULE.getData(dataContext);

        if (module != null) {
            final Editor  data    = PlatformDataKeys.EDITOR_EVEN_IF_INACTIVE.getData(e.getDataContext());
            final Project project = e.getProject();

            if (data != null && project != null) {
                final MMFile file = (MMFile) PsiUtilBase.getPsiFileInEditor(data, project);

                final VirtualFile resourcesRoot = FileUtils.getResourcesRoot(module);
                if (file != null) {
                    boolean error = false;
                    try {
                        final ModelRepository repository = file.getModelRepository();
                        if (resourcesRoot != null)
                            generateSchema(new File(resourcesRoot.getPath()), repository.getModels(), repository, Colls.emptyIterable());
                        else error = true;
                    }
                    catch (final Exception e1) {
                        error = true;
                    }
                    finally {
                        if (error) MMUIInformer.showErrorBalloonPopUp(module.getProject(), JAVA_SRC_ROOT_SET_INCORRECTLY);
                        else MMUIInformer.showBalloonPopUp(module.getProject(), SQL_GENERATED_CORRECTLY);
                    }
                }
            }
        }
    }

    //~ Static Fields ................................................................................................................................

    public static final String SQL_GENERATED_CORRECTLY       = "SQL Generated Correctly";
    public static final String JAVA_SRC_ROOT_SET_INCORRECTLY = "Java Src Root Set Incorrectly";
}  // end class GenerateDefaultSQLAction
