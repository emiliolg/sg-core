
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.intellij;

import com.intellij.psi.tree.IElementType;

import tekgenesis.lexer.TokenType;

/**
 * An Adaptor from a {@link TokenType } to a {@link IElementType }.
 */
public interface TokenAdapter<T extends TokenType<T>, I extends IElementType> {

    //~ Methods ......................................................................................................................................

    /** Adapt from a {@link TokenType } to a {@link IElementType }. */
    I adapt(T t);
}
