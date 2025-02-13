
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.completion;

import com.intellij.codeInsight.TailType;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.psi.PsiMenuField;
import tekgenesis.lang.mm.psi.PsiRoleField;
import tekgenesis.type.MetaModelKind;

import static com.intellij.codeInsight.completion.CompletionType.BASIC;
import static com.intellij.codeInsight.lookup.TailTypeDecorator.withTail;

import static tekgenesis.common.collections.ImmutableList.fromArray;
import static tekgenesis.lang.mm.MMElementType.*;
import static tekgenesis.lang.mm.completion.Completions.providerFromCompletions;
import static tekgenesis.lang.mm.completion.Patterns.*;

/**
 * MetaModel language completion contributor.
 */

public class MetaModelCompletionContributor extends CompletionContributor {

    //~ Constructors .................................................................................................................................

    /** Constructor defining contribution extensions. */
    public MetaModelCompletionContributor() {
        extendRootModels();

        extendModelOptions();

        extendSearchableFields();

        extendFormFields();

        extendEntityFields();

        extendOptionOptionsFromEnum();

        extendMenuFields();

        extendRoleFields();

        extendPermissions();

        extendRouteFields();

        extendTasks();

        extendReferences();
    }

    //~ Methods ......................................................................................................................................

    private void extendBasic(ElementPattern<PsiElement> pattern, CompletionProvider<CompletionParameters> provider) {
        extend(BASIC, pattern, provider);
    }

    private void extendEntityFields() {
        extendBasic(ENTITY_FIELD_OPTION, providerFromCompletions(ENTITY_FIELD));
    }

    private void extendFormFields() {
        extendBasic(FORM_FIELD_OPTION, new FormFieldOptionCompletionProvider());
        extendBasic(WIDGET_OPTION, new WidgetCompletionProvider());
    }

    private void extendFromEnums(MMElementType... enumTypes) {
        for (final MMElementType type : enumTypes)
            extendBasic(elementAtStartOf(type), providerFromCompletions(type, false));
    }

    private void extendMenuFields() {
        //J-
        extendBasic(Patterns.MENU_ELEMENT,
            new MetaModelCompletionProvider(MetaModelKind.FORM, MetaModelKind.LINK, MetaModelKind.MENU)
                    .withSiblingsFilter(new MenuElementSiblingsFilter())
                    .withSiblingsDecorator((e) -> withTail(e, TailType.SEMICOLON)));
        //J+
    }

    private void extendModelOptions() {
        extendBasic(ENTITY_OPTION, providerFromCompletions(ENTITY));
        extendBasic(FORM_OPTION, providerFromCompletions(FORM));
        extendBasic(HANDLER_OPTION, providerFromCompletions(HANDLER));
        extendBasic(ENUM_OPTION, providerFromCompletions(ENUM));
        extendBasic(CASE_OPTION, providerFromCompletions(CASE));
        extendBasic(VIEW_OPTION, providerFromCompletions(VIEW));
        extendBasic(TASK_OPTION, providerFromCompletions(TASK));
    }

    private void extendOptionOptionsFromEnum() {
        extendFromEnums(ICON,
            HTTP_METHOD,
            CHECK_TYPE,
            MASK,
            FILE_TYPE,
            BUTTON_TYPE,
            DATE_TYPE,
            TAB_TYPE,
            POPOVER_TYPE,
            MAP_TYPE,
            EXPORT_TYPE,
            TOGGLE_BUTTON_TYPE,
            QUERY_MODE,
            RATING_TYPE,
            MAIL_VALIDATION_TYPE);
    }

    private void extendPermissions() {
        extendBasic(Patterns.ROLE_PERMISSION, new RolePermissionsCompletionProvider());
    }

    private void extendReferences() {
        extendBasic(Patterns.REFERENCE, new ReferenceCompletionProvider());
    }

    private void extendRoleFields() {
        //J-
        extendBasic(Patterns.ROLE_ELEMENT,
                new MetaModelCompletionProvider(MetaModelKind.FORM)
                        .withSiblingsFilter(new RoleElementSiblingsFilter())
                        .withSiblingsDecorator((e) -> withTail(e, TailType.SEMICOLON)));
        //J+
    }

    private void extendRootModels() {
        extendBasic(Patterns.PACKAGE, providerFromCompletions(MMElementType.PACKAGE));
        extendBasic(METAMODEL, providerFromCompletions(MODEL_OPTIONS, false));
    }

    private void extendRouteFields() {
        extendBasic(ROUTE_OPTION, providerFromCompletions(ROUTE));
    }

    private void extendSearchableFields() {
        extendBasic(SEARCHABLE_FIELD_OPTION, providerFromCompletions(SEARCHABLE));
        extendBasic(Patterns.AFTER_SEARCHABLE, providerFromCompletions(MMElementType.AFTER_SEARCHABLE));
    }

    private void extendTasks() {
        extendBasic(METAMODEL, providerFromCompletions(TASK_TYPE, false));
        extendBasic(TRANSACTION_TYPE, providerFromCompletions(TRANSACTION));
    }

    //~ Inner Classes ................................................................................................................................

    private static class MenuElementSiblingsFilter implements SiblingsFilter {
        @Override public boolean allow(@NotNull PsiElement element) {
            return element instanceof PsiMenuField;
        }

        @NotNull @Override public Seq<PsiElement> siblings(@NotNull PsiElement position) {
            return fromArray(position.getParent().getParent().getChildren());
        }

        @NotNull @Override public String stringify(PsiElement e) {
            return e.getFirstChild().getText();
        }
    }

    private static class RoleElementSiblingsFilter implements SiblingsFilter {
        @Override public boolean allow(@NotNull PsiElement element) {
            return element instanceof PsiRoleField;
        }

        @NotNull @Override public Seq<PsiElement> siblings(@NotNull PsiElement position) {
            return fromArray(position.getParent().getParent().getChildren());
        }

        @NotNull @Override public String stringify(PsiElement e) {
            return e.getFirstChild().getText();
        }
    }
}  // end class MetaModelCompletionContributor
