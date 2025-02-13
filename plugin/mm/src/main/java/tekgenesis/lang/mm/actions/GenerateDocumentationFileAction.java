
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.actions;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.util.PsiUtilBase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.lang.mm.document.DocumentationManager;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.PsiMetaModel;
import tekgenesis.type.MetaModel;
import tekgenesis.type.MetaModelKind;

/**
 * Generate markdown file for current mm file.
 */
@SuppressWarnings("WeakerAccess")
public class GenerateDocumentationFileAction extends MMAction {

    //~ Instance Fields ..............................................................................................................................

    private final List<MetaModel> docModels;

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    @SuppressWarnings("WeakerAccess")
    public GenerateDocumentationFileAction() {
        docModels = new ArrayList<>();
    }

    //~ Methods ......................................................................................................................................

    /** Action perform. */
    @Override public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        final Editor  data    = PlatformDataKeys.EDITOR_EVEN_IF_INACTIVE.getData(anActionEvent.getDataContext());
        final Project project = anActionEvent.getProject();

        if (data != null && project != null) {
            final MMFile file = (MMFile) PsiUtilBase.getPsiFileInEditor(data, project);

            if (file != null) generateDocument(file);
        }
    }

    /** Append all translatable meta-models in file. */
    private void generateDocument(@NotNull final MMFile file) {
        generateDocument(file, file.getMetaModels());
    }

    /** Append all translatable meta-models in given sequence. */
    private void generateDocument(@NotNull final MMFile dispatcher, @NotNull final Seq<PsiMetaModel<? extends MetaModel>> models) {
        docModels.clear();
        for (final PsiMetaModel<? extends MetaModel> model : models) {
            final MetaModel mm = model.getModelOrNull();
            if (mm != null && CAN_BE_DOCUMENTED.contains(mm.getMetaModelKind())) docModels.add(mm);
        }
        new DocumentationManager(dispatcher).document(docModels);
    }

    //~ Static Fields ................................................................................................................................

    private static final EnumSet<MetaModelKind> CAN_BE_DOCUMENTED = EnumSet.of(MetaModelKind.FORM);
}  // end class GenerateDocumentationFileAction
