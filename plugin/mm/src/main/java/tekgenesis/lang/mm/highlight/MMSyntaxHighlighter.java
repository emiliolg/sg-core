
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.highlight;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;

import org.jetbrains.annotations.NotNull;

import tekgenesis.intellij.LexerAdapter;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.mmcompiler.ast.MMToken;

import static tekgenesis.lang.mm.highlight.MMHighlighterColors.forKind;

/**
 * Syntax HighLighter for EDL.
 */
public class MMSyntaxHighlighter extends SyntaxHighlighterBase {

    //~ Methods ......................................................................................................................................

    @NotNull public Lexer getHighlightingLexer() {
        return new LexerAdapter<>(MMToken.lexer(), MMElementType::forToken);
    }

    @NotNull public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        final TextAttributesKey[] result;

        if (tokenType instanceof MMElementType) result = pack(forKind(((MMElementType) tokenType).getTokenType().getHighlight()));
        else result = EMPTY;

        return result;
    }
}
