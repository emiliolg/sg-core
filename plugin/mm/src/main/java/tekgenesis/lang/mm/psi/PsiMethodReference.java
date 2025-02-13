
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.intellij.codeInsight.daemon.impl.quickfix.AddMethodFix;
import com.intellij.codeInsight.generation.PsiMethodMember;
import com.intellij.codeInsight.hint.HintManager;
import com.intellij.codeInsight.intention.AbstractIntentionAction;
import com.intellij.ide.util.MemberChooser;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiType;
import com.intellij.psi.infos.CandidateInfo;
import com.intellij.psi.util.PsiUtil;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.Tuple;
import tekgenesis.field.FieldOption;
import tekgenesis.lang.mm.FileUtils;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetDef;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.type.ArrayType;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static com.intellij.codeInsight.generation.OverrideImplementExploreUtil.getMethodSignaturesToImplement;
import static com.intellij.codeInsight.generation.OverrideImplementExploreUtil.getMethodsToOverrideImplement;
import static com.intellij.codeInsight.generation.OverrideImplementUtil.getContextClass;
import static com.intellij.codeInsight.generation.OverrideImplementUtil.overrideOrImplementMethodsInRightPlace;
import static com.intellij.codeInsight.generation.OverrideImplementUtil.showOverrideImplementChooser;
import static com.intellij.openapi.util.TextRange.create;

import static tekgenesis.codegen.common.MMCodeGenConstants.FORM_ACTION;
import static tekgenesis.codegen.common.MMCodeGenConstants.QUERY_METHOD;
import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.core.Constants.DOUBLE;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.option;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.Strings.capitalizeFirst;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.field.FieldOption.*;
import static tekgenesis.lang.mm.i18n.PluginMessages.MSGS;
import static tekgenesis.lang.mm.psi.PsiUtils.resolveJavaClass;
import static tekgenesis.mmcompiler.ast.MMToken.*;

/**
 * Form method references. Searches the parent Form to get the class and find specific methods.
 */
public class PsiMethodReference extends ElementWithReferences {

    //~ Constructors .................................................................................................................................

    /** Creates an Identifier. */
    PsiMethodReference(MMElementType t) {
        super(t);
    }

    //~ Methods ......................................................................................................................................

    @Override
    @SuppressWarnings("ConstantConditions")
    public void annotate(AnnotationHolder holder) {
        final MethodDescriptor descriptor = createDescriptor();
        final PsiMethod        method     = (PsiMethod) getReference().resolve();
        final PsiMetaModel<?>  model      = getMetaModelAncestor().getOrFail(MSGS.methodOutsideFromScope());
        if (method == null) {
            final PsiElement identifier = getIdentifierChild();
            if (identifier != null) {
                if (isAbstractInvocation()) return;

                final String   text         = identifier.getText();
                final String   fqn          = getClassFqn(model, descriptor);
                final PsiClass psiJavaClass = resolveJavaClass(getProject(), fqn);

                // Annotate error...
                final Annotation        annotation;
                final Option<WidgetDef> widgetDef = model.getModel().castTo(WidgetDef.class);
                if (widgetDef.isPresent() && widgetDef.get().isAbstract())
                    annotation = holder.createWeakWarningAnnotation(identifier.getTextRange(), MSGS.missingMethodAnnotation(text, fqn));
                else annotation = holder.createErrorAnnotation(identifier.getTextRange(), MSGS.missingMethodAnnotation(text, fqn));

                // Register fix based on field option.
                if (psiJavaClass != null) {
                    final Option<FieldOption> option = getEnclosingOption();
                    if (option.isPresent() && option.get() == ON_SUGGEST)
                        // For now, only ON_SUGGEST has different treatment.
                        annotation.registerFix(new MissingStaticMethodFix(psiJavaClass, text, descriptor));
                    else annotation.registerFix(new MMOverrideMethods(psiJavaClass));
                }
            }
        }
        else if (!matchesSignature(method, descriptor))
            holder.createErrorAnnotation(getIdentifier().getTextRange(),
                MSGS.wrongMethodDeclaration(descriptor.getReturnType(), method.getReturnType().getCanonicalText()));
    }

    /** gets method Descriptor. */
    public MethodDescriptor createDescriptor() {
        final MethodDescriptor descriptor = new MethodDescriptor();

        final Option<FieldOption> fieldOption = getEnclosingOption();

        if (fieldOption.isPresent()) createDescriptor(descriptor, fieldOption.get());
        else if (hasSibling(MMToken.ON_SCHEDULE)) descriptor.type(ACTIONS_GET_DEFAULT, FORM_ACTION);

        return descriptor;
    }  // end method createDescriptor

    /** Get metamodel ancestor if defined. */
    public Option<PsiMetaModel<?>> getMetaModelAncestor() {
        return cast(getAncestor(PsiMetaModel.class));
    }

    /** Gets Method name. */
    public String getMethodName() {
        final PsiElement identifier = getIdentifierChild();
        return identifier != null ? identifier.getText() : "";
    }

    @Override public PsiReference getReference() {
        for (final PsiMetaModel<?> model : getMetaModelAncestor()) {
            final PsiElement id     = getIdentifierChild();
            final int        offset = id.getStartOffsetInParent();
            final TextRange  range  = create(offset, offset + id.getTextLength());
            return new MethodReference(getClassFqn(model, createDescriptor()), id.getText(), createDescriptor(), range, this);
        }
        return null;
    }

    private void createDescriptor(MethodDescriptor descriptor, FieldOption option) {
        if (option == ON_CLICK) descriptor.avoidParamsCheck();

        if (option == FieldOption.ON_SUGGEST || option == ON_SUGGEST_SYNC) createDescriptorOnSuggest(descriptor, option);
        else {
            if (option == ON_NEW) descriptor.param("text", String.class);

            if (option == ON_NEW_LOCATION) {
                descriptor.param("lat", DOUBLE);
                descriptor.param("lng", DOUBLE);
            }

            if (option == ON_NEW || option == ON_CHANGE || option == ON_UI_CHANGE || option == ON_BLUR || option == ON_CLICK ||
                option == ON_SELECTION || option == FieldOption.ON_SCHEDULE || option == FieldOption.ON_DISPLAY)
                descriptor.type(ACTIONS_GET_DEFAULT, FORM_ACTION);
        }
    }  // end method createDescriptor

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private void createDescriptorOnSuggest(MethodDescriptor descriptor, FieldOption option)
    {
        if (option == FieldOption.ON_SUGGEST) descriptor.asStatic();

        // We always have this parameter.
        descriptor.param(QUERY_METHOD, String.class);

        for (final WidgetType widgetType : findWidgetType()) {
            if (widgetType == WidgetType.SEARCH_BOX) {
                for (final Form f : findForm())
                    if (f.isDeprecableBoundModel()) descriptor.param("deprecated", "boolean");

                descriptor.type("new java.util.ArrayList<>();", "java.lang.Iterable<tekgenesis.form.Suggestion>");
            }
            else {
                for (final Widget w : findWidget()) {
                    final Type   t            = w.getType().getFinalType();
                    final Type   type         = t.isArray() ? ((ArrayType) t).getElementType() : t;
                    final String fqnClassName = type.getImplementationClassName();

                    descriptor.type("new java.util.ArrayList<>();",
                        "java.lang.Iterable<" + (type.isString() ? "tekgenesis.form.Suggestion" : fqnClassName) + ">");

                    // Add fallback for entities.
                    if (type.isEntity()) descriptor.fallback("java.lang.Iterable<tekgenesis.form.Suggestion>");
                }
            }
        }

        // But we could have more if it's on_suggest.
        if (option == FieldOption.ON_SUGGEST) {
            final Type type = findOnSuggestExpressionType();
            if (!type.isNull()) descriptor.param("arg", type.getImplementationClassName());
        }
    }

    private Option<Form> findForm() {
        for (final PsiForm psiForm : getAncestor(PsiForm.class))
            return psiForm.getModel();
        return empty();
    }

    private Type findOnSuggestExpressionType() {
        for (final PsiElement child : getIdentifier().getFirstChild().getChildren()) {
            if (child instanceof PsiFieldReference) {
                final String id = child.getText();
                for (final Form f : findForm()) {
                    if (f.containsElement(id)) {
                        final Widget widget = f.getElement(id);
                        return widget.getType();
                    }
                }
            }
        }
        return Types.nullType();
    }

    private Option<Widget> findWidget() {
        return getAncestor(PsiWidget.class).flatMap(PsiWidget::getWidget);
    }

    private Option<WidgetType> findWidgetType() {
        return getAncestor(PsiWidget.class).map(PsiWidget::getWidgetType);
    }

    private boolean hasSibling(@NotNull final MMToken token) {
        return equal(token.getText(), getParent().getFirstChild().getText());
    }

    private boolean matchesSignature(@NotNull final PsiMethod method, @NotNull final MethodDescriptor descriptor) {
        // To be implemented signature matching for routing methods
        return getAncestor(PsiHandler.class).isPresent() || descriptor.accepts(method);
    }

    private Option<PsiWidget> outerMultiple(Option<PsiWidget> widget) {
        return widget.flatMap(f -> outerMultipleFrom(f.getAncestor(PsiWidget.class)));
    }

    private Option<PsiWidget> outerMultipleFrom(Option<PsiWidget> widget) {
        return widget.flatMap(f -> f.getMultiplePsiClass() != null ? widget : outerMultipleFrom(f.getAncestor(PsiWidget.class)));
    }

    @NotNull private String getClassFqn(PsiMetaModel<?> model, MethodDescriptor descriptor) {
        String               result   = model.getFullName();
        final Option<Widget> widget   = findWidget();
        Option<Widget>       multiple = empty();
        if (widget.isPresent()) multiple = widget.get().getMultiple().castTo(Widget.class);
        else {
            final Option<PsiWidget> outerMultiple = outerMultiple(getAncestor(PsiWidget.class));
            if (outerMultiple.isPresent()) multiple = outerMultiple.get().getWidget();
        }

        if (multiple.isPresent() && !descriptor.isStatic) result += "." + capitalizeFirst(multiple.get().getName()) + "Row";

        return result;
    }  // end method getClassFqn

    private Option<FieldOption> getEnclosingOption() {
        final Option<PsiFieldOption> ancestor = getAncestor(PsiFieldOption.class);
        final Option<FieldOption>    result;
        if (ancestor.isPresent()) result = some(ancestor.get().getOption());
        else result = option(FieldOption.fromId(getParent().getFirstChild().getText()));
        return result;
    }

    private PsiElement getIdentifierChild() {
        final PsiElement first = getFirstChild();
        if (first.getChildren().length > 0) return first.getFirstChild();
        return first;
    }

    private boolean isAbstractInvocation() {
        return findWidget().map(Widget::isAbstractInvocation).orElse(false);
    }

    //~ Static Fields ................................................................................................................................

    @NonNls private static final String ACTIONS_GET_DEFAULT = "actions.getDefault();";

    private static final Key<Object> DESCRIPTOR_KEY = Key.create("method.descriptor");

    //~ Inner Classes ................................................................................................................................

    public static class MethodDescriptor implements Predicate<PsiMethod> {
        private boolean                   checkParams = true;
        private String                    fallback;
        private boolean                   isStatic;
        private final Map<String, String> parameters;
        private Tuple<String, String>     type;

        private MethodDescriptor() {
            parameters = new LinkedHashMap<>();
            type       = tuple("", Void.TYPE.getName());
            fallback   = "";
            isStatic   = false;
        }

        /** check id method declaration is correct. */
        public boolean accepts(@NotNull final PsiMethod method) {
            boolean result = true;

            if (checkParams) {
                final PsiParameter[] params = method.getParameterList().getParameters();

                result = params.length == parameters.size();

                if (result && params.length > 0) {
                    int i = 0;
                    for (final String p : parameters.values())
                        result &= params[i++].getType().getCanonicalText().equals(p);
                }
            }

            final PsiType returnType = method.getReturnType();

            return result && returnType != null &&
                   (type.second().endsWith(returnType.getCanonicalText()) || fallback.endsWith(returnType.getCanonicalText()));
        }

        @Override public boolean test(@Nullable PsiMethod method) {
            return method != null && accepts(method);
        }

        private void asStatic() {
            isStatic = true;
        }

        private void avoidParamsCheck() {
            checkParams = false;
        }

        private void fallback(@NotNull final String fqnClazz) {
            fallback = fqnClazz;
        }

        private void param(@NotNull final String paramName, @NotNull final Class<?> paramType) {
            parameters.put(paramName, paramType.getCanonicalName());
        }

        private void param(@NotNull final String paramName, @NotNull final String paramType) {
            parameters.put(paramName, paramType);
        }

        private void type(@NotNull final String statement, @NotNull final String clazz) {
            type = tuple(statement, clazz);
        }

        private String getParameters() {
            final StringBuilder sb = new StringBuilder();
            sb.append(LEFT_PAREN.getText());
            int count = 1;
            for (final String paramName : parameters.keySet()) {
                sb.append(parameters.get(paramName)).append(" ").append(paramName);
                if (count != parameters.size()) sb.append(COMMA.getText()).append(" ");
                count++;
            }

            sb.append(RIGHT_PAREN.getText());
            return sb.toString();
        }

        private String getReturn() {
            return "return " + type.first();
        }

        private String getReturnType() {
            return type.second();
        }
    }  // end class MethodDescriptor

    private class MissingStaticMethodFix extends AbstractIntentionAction {
        @NotNull private final PsiClass         clazz;
        @NotNull private final MethodDescriptor method;
        @NotNull private final String           name;

        public MissingStaticMethodFix(@NotNull final PsiClass clazz, @NotNull final String name, @NotNull final MethodDescriptor method) {
            this.clazz  = clazz;
            this.name   = name;
            this.method = method;
        }

        @Override public void invoke(@NotNull Project project, Editor editor, PsiFile file)
            throws IncorrectOperationException
        {
            final String params = method.getParameters();
            final String type   = method.getReturnType();

            new AddMethodFix(
                "@org.jetbrains.annotations.NotNull public static " + type + " " + name + params + " " + LEFT_BRACE.getText() + " " +
                method.getReturn() + " " + RIGHT_BRACE.getText(),
                clazz).invoke(project, editor, file);
        }

        @Override public boolean startInWriteAction() {
            return true;
        }

        @NotNull @Override public String getText() {
            return MSGS.addMethodTo(name, clazz.getName());
        }
    }

    private class MMOverrideMethods extends AbstractIntentionAction {
        private final PsiClass clazz;

        public MMOverrideMethods(@NotNull final PsiClass clazz) {
            this.clazz = clazz;
        }

        @Override public void invoke(@NotNull final Project project, final Editor editor, PsiFile psiFile)
            throws IncorrectOperationException
        {
            final Editor e = PsiUtils.scrollTo(clazz);

            if (e != null) FileUtils.updateChangesOnDiskAndRun(() -> {
                if (DumbService.isDumb(getProject())) {
                    DumbService.getInstance(getProject())
                               .showDumbModeNotification("Implement MetaModel method not available while indexing (save and implement).");return;}
                final PsiFile  containingFile = clazz.getContainingFile();final PsiClass aClass = getContextClass(project,
                        e,
                        containingFile,
                        PsiUtil.isLanguageLevel8OrHigher(containingFile));if (aClass == null) return;

                if (getMethodSignaturesToImplement(aClass).isEmpty()) {
                    HintManager.getInstance().showErrorHint(e, "No methods to implement have been found");
                    return;
                }

                final Collection<CandidateInfo> candidates = getMethodsToOverrideImplement(clazz, true);

                // filtering candidates by checking if the containing class of the method is LoggableInstance
                candidates.removeIf(candidateInfo -> {
                    final PsiElement element = candidateInfo.getElement();
                    if (!(element instanceof PsiMember)) return false;
                    final PsiClass containingClass = ((PsiMember) element).getContainingClass();
                    return containingClass != null && equal("LoggableInstance", containingClass.getName());
                });

                final MemberChooser<PsiMethodMember> chooser = showOverrideImplementChooser(e, clazz, false, candidates, Collections.emptyList());
                if (chooser == null) return;

                final List<PsiMethodMember> selectedElements = chooser.getSelectedElements();
                if (selectedElements == null || selectedElements.isEmpty()) return;

                PsiDocumentManager.getInstance(project).commitDocument(e.getDocument());
                new WriteCommandAction<Object>(project, clazz.getContainingFile()) {
                    @Override protected void run(@NotNull final Result<Object> result)
                        throws Throwable
                    {
                        overrideOrImplementMethodsInRightPlace(e,
                            clazz,
                            selectedElements,
                            chooser.isCopyJavadoc(),
                            chooser.isInsertOverrideAnnotation());
                    }
                }.execute();
            });
        }  // end method invoke

        @NotNull @Override public String getText() {
            return MSGS.implementMissingMethods();
        }
    }  // end class MMOverrideMethods
}  // end class PsiMethodReference
