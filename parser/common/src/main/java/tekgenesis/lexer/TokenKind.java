
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

import tekgenesis.common.core.Strings;
import tekgenesis.parser.Highlight;

import static tekgenesis.parser.Highlight.*;

/**
 * This enum is used to classify the Tokens according to a Kind.
 */
public enum TokenKind implements TokenKindType {

    //~ Enum constants ...............................................................................................................................

    EMPTY, WHITE_SPACE, BAD_CHAR, NODE, IDENTIFIER, IGNORABLE, OPERATOR(OPERATOR_H), KEYWORD(KEYWORD_H), COMMENT(COMMENT_H), NUMBER(NUMBER_H),
    STRING(STRING_H), CTX_KEYWORD(KEYWORD_H), DOCUMENTATION(DOCUMENTATION_H);

    //~ Instance Fields ..............................................................................................................................

    private final Highlight highlight;

    //~ Constructors .................................................................................................................................

    @SuppressWarnings("WeakerAccess")  // Clashes with 'private' in enum constructor is redundant.
    TokenKind() {
        this(PLAIN_H);
    }

    @SuppressWarnings("WeakerAccess")  // Clashes with 'private' in enum constructor is redundant.
    TokenKind(Highlight hi) {
        highlight = hi;
    }

    //~ Methods ......................................................................................................................................

    /** Returns a description of the token. */
    @SuppressWarnings("MethodWithMultipleReturnPoints")
    public String description(@NotNull String tokenText) {
        //J-
        switch (this) {
        case NUMBER:        return tokenText;
        case CTX_KEYWORD:
        case KEYWORD:
        case IDENTIFIER:    return "'" + tokenText + "'";
        case STRING:        return "\"" + tokenText + "\"";
        case NODE:          return "[" + tokenText.toLowerCase() + "]";
        case WHITE_SPACE:   return "' '";
        case OPERATOR:      return "'" + tokenText.toLowerCase() + "'";
        case EMPTY:
        case IGNORABLE:
        case DOCUMENTATION:
        case COMMENT:       return "<" + tokenText.toLowerCase() + ">";
        case BAD_CHAR:
            if (tokenText.length() != 1)
                return "<bad_char>";
            return "'" + Strings.encodeChar(tokenText.charAt(0)) + "'";
        }
        return "";
        //J+
    }

    /** Returns a decorated token kind with a custom {@link Highlight}. */
    public TokenKindType withHighlight(final Highlight hi) {
        return new TokenKindType() {
            @NotNull @Override public TokenKind getTokenKind() {
                return TokenKind.this;
            }
            @NotNull @Override public Highlight getHighlight() {
                return hi;
            }
        };
    }

    /** Returns true if the TokenKind is a KEYWORD. */
    public boolean isKeyword() {
        return this == KEYWORD;
    }

    /** Returns true if its an ignorable TokenKind. */
    public boolean isIgnorable() {
        return this == IGNORABLE || this == WHITE_SPACE || this == COMMENT;
    }

    /**
     * Returns true if the token does not have semantic meaning and is usually ignored from the
     * input.
     */
    public boolean isWhiteSpace() {
        return this == COMMENT || this == WHITE_SPACE;
    }

    /** Returns HighlightKin for MMToken. */
    @NotNull @Override public Highlight getHighlight() {
        return highlight;
    }

    /** Returns true if the token is an Identifier. */
    public boolean isIdentifier() {
        return this == IDENTIFIER;
    }
    @NotNull @Override public TokenKind getTokenKind() {
        return this;
    }

    //~ Static Fields ................................................................................................................................

    public static final char MAX_ISO_CHAR = 0xFF;
}
