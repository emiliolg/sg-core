
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.intellij;

import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lexer.TokenType;

/**
 * Common {@link IElementType} for different plugins.
 */
public class CommonElementType<T extends TokenType<T>> extends IElementType {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final T tokenType;

    //~ Constructors .................................................................................................................................

    protected CommonElementType(@NotNull T tokenType, Language language) {
        super(tokenType.getText(), language);
        this.tokenType = tokenType;
    }

    //~ Methods ......................................................................................................................................

    /** Get the TokenType for this Element. */
    @NotNull public T getTokenType() {
        return tokenType;
    }
}
