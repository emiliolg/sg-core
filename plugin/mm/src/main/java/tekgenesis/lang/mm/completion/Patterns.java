
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.completion;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern.Capture;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.MMElementType;

import static com.intellij.patterns.StandardPatterns.and;
import static com.intellij.patterns.StandardPatterns.not;
import static com.intellij.patterns.StandardPatterns.or;
import static com.intellij.psi.tree.TokenSet.create;
import static com.intellij.psi.tree.TokenSet.orSet;

import static tekgenesis.lang.mm.MMElementType.*;

/**
 * Contains static patterns for metamodel completion.
 */
@SuppressWarnings({ "unchecked", "DuplicateStringLiteralInspection" })
class Patterns {

    //~ Constructors .................................................................................................................................

    private Patterns() {}

    //~ Methods ......................................................................................................................................

    /** psiElement with or without types (any element). */
    static Capture<PsiElement> e(MMElementType... types) {
        return types.length != 0 ? PlatformPatterns.psiElement().withElementType(create((IElementType[]) types)) : PlatformPatterns.psiElement();
    }

    static Capture<PsiElement> elementAtStartOf(@NotNull MMElementType type) {
        return e().atStartOf(e(type));
    }

    static Capture<PsiElement> ignorable(String s) {
        return e(MMElementType.IGNORABLE).withText(s);
    }

    static Capture<PsiElement> modelOption(TokenSet afterTokens, MMElementType parent) {
        return e().afterSiblingSkipping(whitespaceErrorEmpty(), e().withElementType(afterTokens).withParent(PlatformPatterns.psiElement(parent)));
    }

    static Capture<PsiElement> whitespace() {
        return e().whitespace();
    }

    static Capture<PsiElement> whitespaceErrorEmpty() {
        return e().whitespaceCommentEmptyOrError();
    }

    //~ Static Fields ................................................................................................................................

    static final Capture<PsiElement> REFERENCE = e().inside(e(MMElementType.REFERENCE));

    static final Capture<PsiElement> PACKAGE   = e().atStartOf(PlatformPatterns.psiFile());
    static final Capture<PsiElement> METAMODEL = e().withParent(PlatformPatterns.psiFile())
                                                 .andNot(or(PACKAGE, e().inside(e().withElementType(MODELS))));

    static final Capture<PsiElement> ENTITY_OPTION  = modelOption(orSet(ENTITY_OPTIONS, create(LABELED_ID, FORM_REF)), ENTITY);
    static final Capture<PsiElement> FORM_OPTION    = modelOption(orSet(FORM_OPTIONS, create(LABELED_ID, DATA_OBJECT_REF)), FORM);
    static final Capture<PsiElement> HANDLER_OPTION = modelOption(orSet(HANDLER_OPTIONS, create(LABELED_ID)), HANDLER);
    static final Capture<PsiElement> ENUM_OPTION    = modelOption(orSet(ENUM_OPTIONS, create(LABELED_ID, FORM_REF)), ENUM);
    static final Capture<PsiElement> CASE_OPTION    = modelOption(orSet(CASE_OPTIONS, create(LABELED_ID, FORM_REF, DATA_OBJECT_REF)), CASE);
    static final Capture<PsiElement> VIEW_OPTION    = modelOption(orSet(VIEW_OPTIONS, create(LABELED_ID, DATA_OBJECT_REF)), VIEW);
    static final Capture<PsiElement> TASK_OPTION    = modelOption(orSet(TASK_OPTIONS, create(LABELED_ID)), TASK);

    static final Capture<PsiElement>        WIDGET_TYPE                   = elementAtStartOf(MMElementType.WIDGET_TYPE).inside(e(WIDGET));
    static final Capture<PsiElement>        FORM_FIELD_TYPE               = e().atStartOf(e(TYPE).afterSiblingSkipping(whitespace(), ignorable(":")))
                                                                            .inside(e(WIDGET));
    static final Capture<PsiElement>        FORM_FIELD_OPTION_AFTER_COMMA = e().afterSiblingSkipping(whitespaceErrorEmpty(), ignorable(","))
                                                                            .withParent(e(WIDGET));
    static final ElementPattern<PsiElement> FORM_FIELD_OPTION             = or(FORM_FIELD_OPTION_AFTER_COMMA, WIDGET_TYPE);
    static final ElementPattern<PsiElement> WIDGET_OPTION                 = or(FORM_FIELD_TYPE, WIDGET_TYPE);

    static final ElementPattern<PsiElement> ENTITY_FIELD_OPTION = e().afterSiblingSkipping(whitespaceErrorEmpty(),
            ignorable(",").afterSiblingSkipping(whitespaceErrorEmpty(), or(e(TYPE), e(OPTION)))).inside(e(ENTITY_FIELD).inside(e(ENTITY)));

    static final ElementPattern<PsiElement> SEARCHABLE_FIELD_OPTION = e().afterSiblingSkipping(whitespaceErrorEmpty(),
            or(ignorable(","), ignorable(":"))).withParent(e(ENTITY_FIELD).inside(e(SEARCHABLE)));
    static final ElementPattern<PsiElement> AFTER_SEARCHABLE        = e().atStartOf(
            e().afterSiblingSkipping(whitespaceErrorEmpty(), and(not(e(SEARCHABLE).withChild(ignorable("by"))), e(SEARCHABLE))));

    static final Capture<PsiElement> MENU_ELEMENT = e().withParent(e(MMElementType.MENU_ELEMENT));

    static final Capture<PsiElement> ROLE_ELEMENT = e().withParent(e(MMElementType.ROLE_ELEMENT));

    static final Capture<PsiElement> ROLE_PERMISSION = e().withParent(e(MMElementType.ROLE_PERMISSION));

    static final Capture<PsiElement> ROUTE_OPTION = e().afterSiblingSkipping(whitespaceErrorEmpty(), ignorable(",")).withParent(e(ROUTE));

    static final Capture<PsiElement> TRANSACTION_TYPE = e().afterSiblingSkipping(whitespaceErrorEmpty(), ignorable("transaction"))
                                                        .inside(e(TRANSACTION));
}  // end class Patterns
