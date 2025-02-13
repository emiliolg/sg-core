
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.intellij;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lexer.CharSequenceStream;
import tekgenesis.lexer.Lexer;
import tekgenesis.lexer.TokenType;

/**
 * An adapter from {@link Lexer} to Intellij LexerBase.
 */
public class LexerAdapter<T extends TokenType<T>, I extends IElementType> extends LexerBase {

    //~ Instance Fields ..............................................................................................................................

    private int          bufferEnd;
    private CharSequence bufferSequence;

    private final Lexer<T>           lexer;
    private final TokenAdapter<T, I> tokenAdapter;

    //~ Constructors .................................................................................................................................

    /** Creates an adapter from {@link Lexer} to Intellij LexerBase. */
    public LexerAdapter(Lexer<T> lexer, TokenAdapter<T, I> tokenAdapter) {
        this.lexer        = lexer;
        this.tokenAdapter = tokenAdapter;
        bufferSequence    = new CharSequenceStream();
    }

    //~ Methods ......................................................................................................................................

    @Override public void advance() {
        lexer.advance();
    }
    @Override public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
        bufferSequence = buffer;
        bufferEnd      = endOffset;

        lexer.resetStream(new CharSequenceStream("", buffer, startOffset, endOffset));
        lexer.setState(initialState);
    }

    @Override public int getBufferEnd() {
        return bufferEnd;
    }

    @NotNull @Override public CharSequence getBufferSequence() {
        return bufferSequence;
    }

    @Override public int getState() {
        return lexer.getState();
    }

    @Override public int getTokenEnd() {
        return lexer.getCurrentTokenEnd();
    }

    @Override public int getTokenStart() {
        return lexer.getCurrentTokenStart();
    }

    @Override public I getTokenType() {
        return tokenAdapter.adapt(lexer.getCurrentToken());
    }
}  // end class LexerAdapter
