
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.actions;

import java.util.List;

import javax.swing.*;

import com.intellij.codeInsight.FileModificationService;
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.codeInsight.hint.QuestionAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.logging.Logger;
import tekgenesis.lang.mm.MMFileType;
import tekgenesis.lang.mm.psi.PsiMetaModel;
import tekgenesis.lang.mm.psi.PsiMetaModelCodeReference;

import static tekgenesis.common.collections.Colls.exists;
import static tekgenesis.common.core.Predicates.not;

/**
 * Add import action.
 */
public class AddImportAction implements QuestionAction {

    //~ Instance Fields ..............................................................................................................................

    private final Editor                editor;
    private final List<PsiMetaModel<?>> models;

    private final Project                   project;
    private final PsiMetaModelCodeReference reference;

    //~ Constructors .................................................................................................................................

    /** Add import action constructor. */
    public AddImportAction(@NotNull Project project, @NotNull Editor editor, @NotNull PsiMetaModelCodeReference reference,
                           @NotNull List<PsiMetaModel<?>> models) {
        this.project   = project;
        this.reference = reference;
        this.models    = models;
        this.editor    = editor;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean execute() {
        PsiDocumentManager.getInstance(project).commitAllDocuments();

        // Return if reference is not valid
        if (!reference.getElement().isValid()) return false;

        // Return if any option is not valid
        if (exists(models, not(PsiMetaModel::isValid))) return false;

        if (models.size() == 1) addImportCommand(models.get(0));
        else chooseModelAndImport();

        return true;
    }

    private void addImport(PsiMetaModel<?> model) {
        if (!reference.getElement().isValid() || !model.isValid() || reference.resolve() == model) return;

        if (!FileModificationService.getInstance().preparePsiElementForWrite(reference.getElement())) return;

        final int             caret        = editor.getCaretModel().getOffset();
        final RangeMarker     marker       = editor.getDocument().createRangeMarker(caret, caret);
        final int             colByOffset  = editor.offsetToLogicalPosition(caret).column;
        final int             col          = editor.getCaretModel().getLogicalPosition().column;
        final int             virtualSpace = col == colByOffset ? 0 : col - colByOffset;
        final LogicalPosition before       = new LogicalPosition(editor.getCaretModel().getLogicalPosition().line, 0);
        editor.getCaretModel().moveToLogicalPosition(before);

        try {
            reference.bindToElement(model);
        }
        catch (final IncorrectOperationException e) {
            logger.error(e);
        }

        final LogicalPosition after = new LogicalPosition(editor.getCaretModel().getLogicalPosition().line, col);
        editor.getCaretModel().moveToLogicalPosition(after);

        if (marker.isValid()) {
            final LogicalPosition position = editor.offsetToLogicalPosition(marker.getStartOffset());
            final int             column   = position.column + virtualSpace;
            editor.getCaretModel().moveToLogicalPosition(new LogicalPosition(position.line, column));
            editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
        }

        ApplicationManager.getApplication().invokeLater(() -> {
            if (!project.isDisposed() && project.isOpen()) {
                final DaemonCodeAnalyzer analyzer = DaemonCodeAnalyzer.getInstance(project);
                if (analyzer != null) analyzer.restart(reference.getElement().getContainingFile());
            }
        });
    }

    private void addImportCommand(final PsiMetaModel<?> target) {
        CommandProcessor.getInstance().executeCommand(project, () -> ApplicationManager.getApplication().runWriteAction(() -> addImport(target)),
            "Add Import", null);
    }

    private void chooseModelAndImport() {
        final BaseListPopupStep<PsiMetaModel<?>> step = new BaseListPopupStep<PsiMetaModel<?>>("MetaModel to import", models) {
                @Override public boolean isAutoSelectionEnabled() {
                    return false;
                }

                @Override public boolean isSpeedSearchEnabled() {
                    return true;
                }

                @Override public PopupStep<?> onChosen(@Nullable PsiMetaModel<?> selectedValue, boolean finalChoice) {
                    if (selectedValue == null) return FINAL_CHOICE;

                    if (finalChoice) {
                        PsiDocumentManager.getInstance(project).commitAllDocuments();
                        addImportCommand(selectedValue);
                        return FINAL_CHOICE;
                    }

                    // todo add auto-import metamodel preferences :)
                    /*String qname = selectedValue.getQualifiedName();
                     * if (qname == null) return FINAL_CHOICE;
                     *
                     * List<String> toExclude = getAllExcludableStrings(qname);
                     *
                     * return new BaseListPopupStep<String>(null, toExclude) {
                     *  @NotNull
                     *  @Override
                     *  public String getTextFor(String value) {
                     *      return "Exclude '" + value + "' from auto-import";
                     *  }
                     *
                     *  @Override
                     *  public PopupStep onChosen(String selectedValue, boolean finalChoice) {
                     *      if (finalChoice) {
                     *          excludeFromImport(myProject, selectedValue);
                     *      }
                     *
                     *      return super.onChosen(selectedValue, finalChoice);
                     *  }
                     *};*/
                    return FINAL_CHOICE;
                }

                /*@Override public boolean hasSubstep(PsiMetaModel<?> selectedValue) {
                 *  return true;
                 *}*/

                @Override public boolean hasSubstep(PsiMetaModel<?> selectedValue) {
                    return false;
                }

                @NotNull @Override public String getTextFor(PsiMetaModel<?> value) {
                    return value.getFullName();
                }

                @Override public Icon getIconFor(PsiMetaModel<?> model) {
                    return MMFileType.getIconFor(model);
                }
            };

        JBPopupFactory.getInstance().createListPopup(step).showInBestPositionFor(editor);
    }  // end method chooseModelAndImport

    //~ Static Fields ................................................................................................................................

    /*public static void excludeFromImport(final Project project, final String prefix) {
     *  ApplicationManager.getApplication().invokeLater(new Runnable() {
     *          @Override public void run() {
     *              if (project.isDisposed()) return;
     *
     *              final AutoImportOptionsConfigurable configurable = new AutoImportOptionsConfigurable();
     *              ShowSettingsUtil.getInstance().editConfigurable(project, configurable, new Runnable() {
     *                      @Override public void run() {
     *                          final JavaAutoImportOptions options =
     *                              ContainerUtil.findInstance(configurable.getConfigurables(), JavaAutoImportOptions.class);
     *                          options.addExcludePackage(prefix);
     *                      }
     *                  });
     *          }
     *      });
     * }
     *
     * public static List<String> getAllExcludableStrings(@NotNull String qname) {
     *  List<String> toExclude = new ArrayList<String>();
     *  while (true) {
     *      toExclude.add(qname);
     *      final int i = qname.lastIndexOf('.');
     *      if (i < 0 || i == qname.indexOf('.')) break;
     *      qname = qname.substring(0, i);
     *  }
     *  return toExclude;
     *}*/

    // end method chooseModelAndImport

    /*public static void excludeFromImport(final Project project, final String prefix) {
     *  ApplicationManager.getApplication().invokeLater(new Runnable() {
     *          @Override public void run() {
     *              if (project.isDisposed()) return;
     *
     *              final AutoImportOptionsConfigurable configurable = new AutoImportOptionsConfigurable();
     *              ShowSettingsUtil.getInstance().editConfigurable(project, configurable, new Runnable() {
     *                      @Override public void run() {
     *                          final JavaAutoImportOptions options =
     *                              ContainerUtil.findInstance(configurable.getConfigurables(), JavaAutoImportOptions.class);
     *                          options.addExcludePackage(prefix);
     *                      }
     *                  });
     *          }
     *      });
     * }
     *
     * public static List<String> getAllExcludableStrings(@NotNull String qname) {
     *  List<String> toExclude = new ArrayList<String>();
     *  while (true) {
     *      toExclude.add(qname);
     *      final int i = qname.lastIndexOf('.');
     *      if (i < 0 || i == qname.indexOf('.')) break;
     *      qname = qname.substring(0, i);
     *  }
     *  return toExclude;
     *}*/

    private static final Logger logger = Logger.getLogger(AddImportAction.class);
}  // end class AddImportAction
