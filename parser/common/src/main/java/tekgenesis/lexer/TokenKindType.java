
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lexer;

import org.jetbrains.annotations.NotNull;

import tekgenesis.parser.Highlight;

/**
 * A class to implements decorators for a TokenKind.
 */
public interface TokenKindType {

    //~ Methods ......................................................................................................................................

    /** Return the Highlight for the Token. */
    @NotNull Highlight getHighlight();
    /** The original {@link TokenKind}. */
    @NotNull TokenKind getTokenKind();
}
