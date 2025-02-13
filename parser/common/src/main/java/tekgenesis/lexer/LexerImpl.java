
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lexer;

import java.util.SortedMap;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Constants;
import tekgenesis.parser.Position;

import static java.lang.Character.isDigit;
import static java.lang.Character.isJavaIdentifierStart;
import static java.lang.Character.isWhitespace;

import static tekgenesis.lexer.CharStream.EOF_CHAR;

/**
 * A default implementation of the {@link Lexer} interface.
 *
 * @param  <T>
 */
public class LexerImpl<T extends TokenType<T>> implements Lexer<T> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull
    @SuppressWarnings({ "InstanceVariableMayNotBeInitialized", "NullableProblems" })
    private T          currentToken;
    private int        currentTokenEnd;
    private int        currentTokenStart;

    private int state;

    @NotNull private CharStream      stream;
    @NotNull private final Tokens<T> tokens;

    //~ Constructors .................................................................................................................................

    /** Creates a Lexer to analyze the specified Character Stream. */
    public LexerImpl(final Tokens<T> ts) {
        this(new CharSequenceStream(), ts);
    }

    /** Creates a Lexer to analyze the specified Character Stream. */
    private LexerImpl(@NotNull CharStream cs, @NotNull final Tokens<T> ts) {
        stream = cs;
        tokens = ts;
        resetStream(cs);
    }

    //~ Methods ......................................................................................................................................

    public final void advance() {
        currentTokenStart = stream.index();
        currentToken      = findNextToken();
        currentTokenEnd   = stream.index();
    }

    @Override public Lexer<T> resetStream(CharStream cs) {
        stream = cs;
        state  = 0;
        advance();
        return this;
    }

    @NotNull @Override public Position getCurrentPosition() {
        return stream.getPosition();
    }

    @NotNull @Override public T getCurrentToken() {
        return currentToken;
    }

    @Override public int getCurrentTokenEnd() {
        return currentTokenEnd;
    }

    @Override public int getCurrentTokenStart() {
        return currentTokenStart;
    }

    public final CharSequence getCurrentTokenText() {
        return stream.subSequence(currentTokenStart, stream.index());
    }

    @Override public int getState() {
        return state;
    }

    @Override public void setState(int state) {
        this.state = state;
    }

    protected T caseInsensitiveIdentifier() {
        while (Character.isJavaIdentifierPart(currentChar()))
            consume();
        return tokens.findKeyword(getCurrentTokenText().toString().toLowerCase());
    }

    protected T octalNumber(final T octLong, final T octInt) {
        match('0');
        matchDigits(Constants.OCTAL_RADIX);
        return matchLongSuffix(octLong, octInt);
    }

    private T badChar() {
        consume();
        return tokens.getBadChar();
    }

    private T comment() {
        final boolean doc = currentChar() == '*';
        while (!match(EOF_CHAR) && !match('*', '/'))
            consume();
        return doc ? tokens.getDocumentation() : tokens.getComment();
    }

    private void consume() {
        stream.consume();
    }
    private void consume(int i) {
        for (int j = 0; j < i; j++)
            stream.consume();
    }

    private int currentChar() {
        return stream.currentChar();
    }

    @NotNull
    @SuppressWarnings({ "OverlyLongMethod", "OverlyComplexMethod", "MethodWithMultipleReturnPoints" })
    private T findNextToken() {
        final int chr = currentChar();

        //J-
        switch (chr) {
        case EOF_CHAR:
            return tokens.getEofToken();

        case ' ': case '\t': case '\n': case '\r': case '\f':
            return whiteSpace();

        case '+': case '-': case '*': case ':': case ';': case '.': case ',':
        case '{': case '}': case '[': case ']': case '(': case ')': case '?':
        case '=': case '>': case '<': case '$': case '&' : case '|':case '!':
            return operator(chr);

        case '"':
            final int c1 = lookAhead(1);
            final int c2 = lookAhead(2);
            return c1 == '"' && c2 == '"' ? multiLineString(chr) : string(chr);

        case '/':
            final int a = lookAhead(1);
            if(a == '*' || a == '/') {
                consume();
                consume();
            }
            return a == '*' ? comment() : a == '/' ? lineComment() : operator('/');

        case '0':
            final int c = lookAhead(1);
            return c == 'x' || c == 'X' ? hexadecimalNumber() : c != '.' ? octalNumber() : number();

        case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
            return number();

        case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h': case 'i':
        case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p': case 'q': case 'r':
        case 's': case 't': case 'u': case 'v': case 'w': case 'x': case 'y': case 'z':
        case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G': case 'H': case 'I':
        case 'J': case 'K': case 'L': case 'M': case 'N': case 'O': case 'P': case 'Q': case 'R':
        case 'S': case 'T': case 'U': case 'V': case 'W': case 'X': case 'Y': case 'Z': case '_':
            return identifier();
        default:
            // Check other unicode characters
            return isJavaIdentifierStart(chr) ? identifier()
                    : isDigit(chr) ? number()
                    : isWhitespace(chr) ? whiteSpace()
                    : badChar();
        }
        //J+
    }  // end method findNextToken

    private T hexadecimalNumber() {
        match('0');

        final T hexInt  = tokens.getHexInt();
        final T hexLong = tokens.getHexLong();

        if (hexInt == null && hexLong == null) return tokens.getDecInt();

        matchAny('x', 'X');
        matchDigits(Constants.HEXADECIMAL_RADIX);
        return hexLong == null ? hexInt : matchLongSuffix(hexLong, hexInt);
    }

    private T identifier() {
        while (Character.isJavaIdentifierPart(currentChar()))
            consume();
        return tokens.findKeyword(getCurrentTokenText().toString());
    }

    private T lineComment() {
        final boolean doc = currentChar() == '-';
        while (!matchAny('\n', EOF_CHAR))
            consume();
        return doc ? tokens.getDocumentation() : tokens.getLineComment();
    }

    private int lookAhead(int i) {
        return stream.lookAhead(i);
    }

    private boolean match(int c) {
        return stream.match(c);
    }

    private boolean match(int... cs) {
        return stream.match(cs);
    }

    private boolean matchAny(int... cs) {
        return stream.matchAny(cs);
    }

    private void matchDigits(int radix) {
        while (Character.digit(currentChar(), radix) != -1)
            consume();
    }

    private T matchLongSuffix(final T longToken, final T intToken) {
        return match('l', 'L') ? longToken : intToken;
    }

    private T multiLineString(final int quoteChar) {
        consume(3);

        int chr;
        while ((chr = currentChar()) != EOF_CHAR) {
            if (chr == quoteChar && lookAhead(1) == '"' && lookAhead(2) == '"') {
                consume(3);
                break;
            }
            consume();
        }
        return tokens.getStringLiteral();
    }

    private T number() {
        final T intToken    = tokens.getDecInt();
        final T doubleToken = tokens.getDouble();

        matchDigits(10);
        T t = intToken;
        if (match('.')) {
            matchDigits(10);
            t = tokens.getFixedDecimal();
            if (t == null) t = doubleToken;

            if (match('e', 'E')) {
                match('+', '-');
                matchDigits(10);
                t = doubleToken;
            }
        }
        if (match('f', 'F')) {
            t = tokens.getFloat();
            if (t == null) t = doubleToken;
        }
        else if (match('d', 'D')) t = doubleToken;
        else if (t == intToken && tokens.getDecLong() != null && match('l', 'L')) t = tokens.getDecLong();

        return t;
    }

    private T octalNumber() {
        match('0');

        final T octInt  = tokens.getOctInt();
        final T octLong = tokens.getOctLong();

        if (octInt == null && octLong == null) return tokens.getDecInt();

        matchDigits(Constants.OCTAL_RADIX);
        return octLong == null ? octInt : matchLongSuffix(octLong, octInt);
    }

    private T operator(int chr) {
        consume();
        final SortedMap<String, T> m = tokens.findOperator(chr);
        // Logic not found
        if (m.isEmpty()) return tokens.getBadChar();

        // Check if there are more operators starting with this char or the length of the key is > 1
        if (m.size() > 1 || m.firstKey().length() > 1) {
            final int next = currentChar();
            if (next != EOF_CHAR) {
                final String op = new String(new char[] { (char) chr, (char) next });
                final T      t  = tokens.findOperator(op);
                if (t != null) {
                    consume();
                    return t;
                }
            }
        }
        return m.values().iterator().next();
    }

    private T string(final int quoteChar) {
        match(quoteChar);
        while (!matchAny(quoteChar, '\n', EOF_CHAR)) {
            if (currentChar() == '\\') {
                final int c = lookAhead(1);
                if (c != EOF_CHAR && c != '\n') consume();
            }
            consume();
        }
        return tokens.getStringLiteral();
    }

    private T whiteSpace() {
        while (isWhitespace(currentChar()))
            consume();
        return tokens.getWhiteSpaceToken();
    }
}  // end class LexerImpl
