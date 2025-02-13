
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.quickfix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.intellij.codeInsight.CodeInsightSettings;
import com.intellij.codeInsight.FileModificationService;
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzerSettings;
import com.intellij.codeInsight.daemon.impl.DaemonListeners;
import com.intellij.codeInsight.daemon.impl.ShowAutoImportPass;
import com.intellij.codeInsight.hint.HintManager;
import com.intellij.codeInsight.hint.QuestionAction;
import com.intellij.codeInsight.intention.HighPriorityAction;
import com.intellij.codeInspection.HintAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.impl.LaterInvocator;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.proximity.PsiProximityComparator;
import com.intellij.util.IncorrectOperationException;
import com.jgoodies.common.base.Strings;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.lang.mm.actions.AddImportAction;
import tekgenesis.lang.mm.i18n.PluginMessages;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.MetaModelReferenceKind;
import tekgenesis.lang.mm.psi.PsiImport;
import tekgenesis.lang.mm.psi.PsiMetaModel;
import tekgenesis.lang.mm.psi.PsiMetaModelCodeReference;
import tekgenesis.lang.mm.psi.PsiMetaModelCodeReferenceElement;
import tekgenesis.lang.mm.psi.PsiUtils;
import tekgenesis.type.MetaModel;

import static java.util.Collections.emptyList;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.QName.extractName;
import static tekgenesis.lang.mm.psi.MMCommonComposite.findPsiMetaModel;
import static tekgenesis.lang.mm.psi.MetaModelReferences.getReferenceKind;
import static tekgenesis.lang.mm.psi.MetaModelReferences.isNotBasicType;
import static tekgenesis.lang.mm.psi.MetaModelReferences.isReferenceName;

/**
 * Import metamodel fix.
 */
public class ImportMetaModelFix implements HintAction, HighPriorityAction {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final PsiMetaModelCodeReferenceElement element;
    @NotNull private final PsiMetaModelCodeReference        reference;

    //~ Constructors .................................................................................................................................

    /** Import meta model fix constructor. */
    public ImportMetaModelFix(@NotNull PsiMetaModelCodeReferenceElement element, @NotNull PsiMetaModelCodeReference reference) {
        this.element   = element;
        this.reference = reference;
    }

    //~ Methods ......................................................................................................................................

    @Override public void invoke(@NotNull Project project, Editor editor, PsiFile file)
        throws IncorrectOperationException
    {
        if (!FileModificationService.getInstance().prepareFileForWrite(file)) return;

        ApplicationManager.getApplication().runWriteAction(() -> {
            final List<PsiMetaModel<?>> modelsToImport = getModelsToImport();
            if (modelsToImport.isEmpty()) return;

            createAddImportAction(editor, modelsToImport, project).execute();
        });
    }

    @Override public boolean showHint(@NotNull final Editor editor) {
        if (!canImport()) return false;
        final Result result = doFix(editor, true, false);
        return result == Result.POPUP_SHOWN || result == Result.CLASS_AUTO_IMPORTED;
    }

    @Override public boolean startInWriteAction() {
        return false;
    }

    @Override public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        return element.isValid() && file.getManager().isInProject(file) && canImport() && !getModelsToImport().isEmpty();
    }

    @NotNull @Override public String getFamilyName() {
        return getText();
    }

    @NotNull @Override public String getText() {
        return PluginMessages.MSGS.importMetaModel();
    }

    private boolean canImport() {
        final boolean result = !reference.isQualified() && isReferenceName(element) && isNotBasicType(element) &&
                               !hasUnresolvedImportWhichCanImport();
        if (result) {
            final MetaModelReferenceKind kind = getReferenceKind(element);
            return kind != MetaModelReferenceKind.PACKAGE_NAME_KIND;
        }
        return false;
    }

    private AddImportAction createAddImportAction(Editor editor, List<PsiMetaModel<?>> modelsToImport, Project project) {
        return new AddImportAction(project, editor, reference, modelsToImport);
    }

    private Result doFix(@NotNull final Editor editor, boolean allowPopup, final boolean allowCaretNearRef) {
        final List<PsiMetaModel<?>> modelsToImport = getModelsToImport();
        if (modelsToImport.isEmpty()) return Result.POPUP_NOT_SHOWN;

        try {
            final PsiReference r = element.getReference();
            if (r != null) {
                final String name = r.getCanonicalText();
                if (!isEmpty(name)) {
                    final Pattern pattern = Pattern.compile(DaemonCodeAnalyzerSettings.getInstance().NO_AUTO_IMPORT_PATTERN);
                    final Matcher matcher = pattern.matcher(name);
                    if (matcher.matches()) return Result.POPUP_NOT_SHOWN;
                }
            }
        }
        catch (final PatternSyntaxException ignored) {}

        final PsiFile           psiFile = element.getContainingFile();
        final PsiMetaModel<?>[] models  = modelsToImport.toArray(new PsiMetaModel<?>[modelsToImport.size()]);
        final Project           project = element.getProject();
        sortIdenticalShortNameModels(models, reference);

        final QuestionAction action = createAddImportAction(editor, Arrays.asList(models), project);

        final boolean canImportHere = canImport();

        if (models.length == 1 && CodeInsightSettings.getInstance().ADD_UNAMBIGIOUS_IMPORTS_ON_THE_FLY &&
            (ApplicationManager.getApplication().isUnitTestMode() || DaemonListeners.canChangeFileSilently(psiFile)) &&
            !LaterInvocator.isInModalContext())
        {
            CommandProcessor.getInstance().runUndoTransparentAction(action::execute);
            return Result.CLASS_AUTO_IMPORTED;
        }

        if (allowPopup && canImportHere) {
            final String hintText = ShowAutoImportPass.getMessage(models.length > 1, models[0].getFullName());
            if (!ApplicationManager.getApplication().isUnitTestMode() && !HintManager.getInstance().hasShownHintsThatWillHideByOtherHint(true))
                HintManager.getInstance().showQuestionHint(editor, hintText, element.getTextOffset(), element.getTextRange().getEndOffset(), action);
            return Result.POPUP_SHOWN;
        }
        return Result.POPUP_NOT_SHOWN;
    }  // end method doFix

    private boolean hasUnresolvedImportWhichCanImport() {
        final MMFile      file    = element.getContainingFile();
        final PsiImport[] imports = file.findChildrenByClass(PsiImport.class);
        final String      name    = reference.getReferenceName();
        if (name == null) return false;
        for (final PsiImport i : imports) {
            final PsiReference r = i.getReference();
            if (r != null && r.resolve() == null && name.equals(extractName(i.getImportReferenceText()))) return true;
        }
        return false;
    }

    @NotNull private List<PsiMetaModel<?>> getModelsToImport() {
        if (reference.resolve(true) != null) return emptyList();

        final String name = element.getReferenceName();
        if (Strings.isEmpty(name)) return emptyList();

        // Filter only MetaModel with matching name
        final Seq<MetaModel> models = Colls.filter(element.getModelRepository().getModels(true), m -> m != null && m.getName().equals(name));

        final Module                module = PsiUtils.getModule(element);
        final List<PsiMetaModel<?>> result = new ArrayList<>();
        if (module == null) return result;

        for (final MetaModel model : models) {
            final PsiMetaModel<?> mm = findPsiMetaModel(model, module, element.getProject());
            if (mm != null) result.add(mm);
        }

        return result;
               /*
                * PsiClass[] classes = PsiShortNamesCache.getInstance(myElement.getProject()).getClassesByName(name, scope);
                * if (classes.length == 0) return emptyList();
                * List<PsiClass> classList = new ArrayList<PsiClass>(classes.length);
                * boolean isAnnotationReference = myElement.getParent() instanceof PsiAnnotation;
                * final PsiFile file = myElement.getContainingFile();
                * for (PsiClass aClass : classes) {
                *  if (isAnnotationReference && !aClass.isAnnotationType()) continue;
                *  if (JavaCompletionUtil.isInExcludedPackage(aClass, false)) continue;
                *  if (referenceHasTypeParameters && !aClass.hasTypeParameters()) continue;
                *  String qName = aClass.getQualifiedName();
                *  if (qName != null) { //filter local classes
                *      if (qName.indexOf('.') == -1) continue; //do not show classes from default package)
                *      if (qName.endsWith(name) && (file == null || ImportFilter.shouldImport(file, qName))) {
                *          if (isAccessible(aClass, myElement)) {
                *              classList.add(aClass);
                *          }
                *      }
                *  }
                * }
                *
                * classList = filterByRequiredMemberName(classList);
                *
                * List<PsiClass> filtered = filterByContext(classList, myElement);
                * if (!filtered.isEmpty()) {
                *  classList = filtered;
                * }
                *
                * filterAlreadyImportedButUnresolved(classList);
                *return classList;*/
    }  // end method getModelsToImport

    //~ Methods ......................................................................................................................................

    /** Sort name by proximity. */
    public static void sortIdenticalShortNameModels(PsiMetaModel<?>[] models, @NotNull PsiReference context) {
        if (models.length <= 1) return;
        // The same proximity weighers are used in completion, where the leafness is critical
        final PsiElement leaf = context.getElement().getFirstChild();
        Arrays.sort(models, new PsiProximityComparator(leaf));
    }

    //~ Enums ........................................................................................................................................

    public enum Result { POPUP_SHOWN, CLASS_AUTO_IMPORTED, POPUP_NOT_SHOWN }

    /*private boolean canImportHere(boolean allowCaretNearRef, Editor editor, PsiFile psiFile, String exampleClassName) {
     *  return (allowCaretNearRef || !isCaretNearRef(editor, myRef)) &&
     *          !hasUnresolvedImportWhichCanImport(psiFile, exampleClassName);
     *}*/

}  // end class ImportMetaModelFix
