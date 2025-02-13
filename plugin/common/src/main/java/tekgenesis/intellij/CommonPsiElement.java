
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.intellij;

import com.intellij.lang.annotation.AnnotationHolder;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lexer.TokenType;

/**
 * Common Interface for all PsiElements.
 */
public interface CommonPsiElement<E extends CommonElementType<T>, T extends TokenType<T>> {

    //~ Methods ......................................................................................................................................

    /** Method to annotate nodes in editor with warnings and errors. */
    void annotate(AnnotationHolder holder);

    /** Returns the ElementType of the Node. */
    @NotNull E getElementType();
}
