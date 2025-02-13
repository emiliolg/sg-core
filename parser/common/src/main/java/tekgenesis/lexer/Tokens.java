
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lexer;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static tekgenesis.common.Predefined.notNull;

/**
 * Utilities to get information about the Tokens.
 */
public abstract class Tokens<T extends TokenType<T>> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull protected final Map<String, T> keywordMap = new HashMap<>();

    @NotNull private final T badChar;

    @NotNull private final T comment;
    @NotNull private final T documentation;

    @NotNull private final T eofToken;
    @NotNull private final T identifier;
    @NotNull private final T ignorableToken;

    @NotNull private final TreeMap<String, T> operatorMap = new TreeMap<>();

    @NotNull private final T stringLiteral;
    @NotNull private final T wsToken;

    //~ Constructors .................................................................................................................................

    /** Creates an instance for a given class of tokens. */
    @SuppressWarnings({ "ConstantConditions", "OverlyLongMethod", "OverlyComplexMethod" })
    protected Tokens(@NotNull Class<T> tokenClass) {
        T eof       = null;
        T bad       = null;
        T ws        = null;
        T string    = null;
        T id        = null;
        T comm      = null;
        T doc       = null;
        T ignorable = null;

        for (final T token : tokenClass.getEnumConstants()) {
            switch (token.getKind()) {
            case CTX_KEYWORD:
                // todo review
                break;
            case KEYWORD:
                keywordMap.put(token.getText(), token.target());
                break;
            case NUMBER:
                break;
            case IDENTIFIER:
                id = token;
                break;
            case STRING:
                string = token;
                break;
            case DOCUMENTATION:
                doc = token;
                break;
            case COMMENT:
                if (token != getLineComment()) comm = token;
                break;
            case NODE:
                break;
            case OPERATOR:
                operatorMap.put(token.getText(), token.target());
                break;
            case EMPTY:
                eof = token;
                break;
            case WHITE_SPACE:
                ws = token;
                break;
            case BAD_CHAR:
                bad = token;
                break;
            case IGNORABLE:
                ignorable = token;
                break;
            }
        }
        if (eof == null || bad == null || ws == null || string == null || id == null || comm == null || doc == null)
            throw new IllegalStateException("Incomplete Token Set");
        eofToken       = eof;
        wsToken        = ws;
        stringLiteral  = string;
        identifier     = id;
        badChar        = bad;
        comment        = comm;
        documentation  = doc;
        ignorableToken = ignorable;
    }  // end ctor Tokens

    //~ Methods ......................................................................................................................................

    // end constructor Tokens

    /**
     * Find the Token corresponding to a Keyword name. If none return the specified default value
     */
    @NotNull public T findKeyword(@NotNull String name) {
        return notNull(keywordMap.get(name), identifier);
    }

    /** The sub-map of all operators starting with the specified character. */
    @NotNull public final SortedMap<String, T> findOperator(int c) {
        final String from = String.valueOf((char) c);
        final String to   = String.valueOf((char) (c + 1));
        return operatorMap.subMap(from, to);
    }
    /** The operator with the specified sequence of characters. */
    @Nullable public final T findOperator(String str) {
        return operatorMap.get(str);
    }

    /** Creates a lexer for this set of tokens. */
    public Lexer<T> lexer() {
        return new LexerImpl<>(this);
    }

    /** The Bad Char token for this set. */
    @NotNull public final T getBadChar() {
        return badChar;
    }

    /** The Standard (Block Comment) token for this set. */
    @NotNull public final T getComment() {
        return comment;
    }

    /** The Decimal Integer token for this set. */
    @NotNull public abstract T getDecInt();
    /** The Decimal Long token for this set. */
    @Nullable public T getDecLong() {
        return null;
    }

    /** The Documentation token for this set. */
    @NotNull public final T getDocumentation() {
        return documentation;
    }

    /** The Double token for this set. */
    @NotNull public abstract T getDouble();
    /** The EMPTY token for this set. */
    @NotNull public final T getEofToken() {
        return eofToken;
    }
    /** The Fixed Decimal Point token for this set. */
    @Nullable public T getFixedDecimal() {
        return null;
    }
    /** The Float token for this set. */
    @Nullable public T getFloat() {
        return null;
    }

    /** The Hexadecimal Integer token for this set. */
    @Nullable public T getHexInt() {
        return null;
    }
    /** The Hexadecimal Long token for this set. */
    @Nullable public T getHexLong() {
        return null;
    }
    /** The Ignorable token for this set. */
    @NotNull public final T getIgnorableToken() {
        return ignorableToken;
    }
    /** The Line Comment token for this set. */
    @Nullable public T getLineComment() {
        return null;
    }

    /** The Octal Integer token for this set. */
    @Nullable public T getOctInt() {
        return null;
    }
    /** The Octal Long token for this set. */
    @Nullable public T getOctLong() {
        return null;
    }
    /** The String Literal token for this set. */
    @NotNull public final T getStringLiteral() {
        return stringLiteral;
    }
    /** The WhiteSpace token for this set. */
    @NotNull public final T getWhiteSpaceToken() {
        return wsToken;
    }
}  // end class Tokens
