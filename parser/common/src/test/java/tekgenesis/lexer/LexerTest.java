
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lexer;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.util.JavaReservedWords;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.lexer.LexerTest.TestToken.*;
import static tekgenesis.lexer.TokenKind.*;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber", "unchecked" })
public class LexerTest {

    //~ Methods ......................................................................................................................................

    @Test public void lexer() {
        assertThat(lex("TRUE FALSE 10")).containsExactly(tuple(TRUE, "TRUE"), tuple(FALSE, "FALSE"), tuple(DEC_INT, "10"));
        assertThat(lex("\"Hello\" \"World\"")).containsExactly(tuple(STRING_LITERAL, "\"Hello\""), tuple(STRING_LITERAL, "\"World\""));
        assertThat(lex(MULTI_LINE.mkString("\n"))).containsExactly(tuple(LINE_COMMENT, "// A Comment\n"),
            tuple(STRING_LITERAL, "\"A String\""),
            tuple(STRING_LITERAL, Q3 + "A\n" + "\"Multi line\"\n" + "String\n" + Q3),
            tuple(TestToken.COMMENT, "/*\n* A star comment used as a common comment\n*/"),
            tuple(TestToken.DOCUMENTATION, "//- A Doc Comment\n"),
            tuple(TestToken.DOCUMENTATION, "/**\n* A double star comment used to document entities\n*/"));
    }

    //~ Methods ......................................................................................................................................

    private static List<Tuple<TestToken, String>> lex(final String input) {
        final Lexer<TestToken> lexer = TestToken.tokens.lexer();
        lexer.resetStream(new CharSequenceStream("", input));

        final List<Tuple<TestToken, String>> tokens = new ArrayList<>();
        TestToken                            t;
        while (!(t = lexer.getCurrentToken()).isEmpty()) {
            if (!t.isWhiteSpace()) tokens.add(tuple(t, lexer.getCurrentTokenText().toString()));
            lexer.advance();
        }
        return tokens;
    }

    //~ Static Fields ................................................................................................................................

    private static final String Q3 = "\"\"\"";

    private static final Seq<String> MULTI_LINE = listOf("// A Comment",
            "\"A String\"",
            Q3 + "A",
            "\"Multi line\"",
            "String",
            Q3,
            "/*\n* A star comment used as a common comment\n*/",
            "//- A Doc Comment",
            "/**\n* A double star comment used to document entities\n*/");

    //~ Enums ........................................................................................................................................

    enum TestToken implements TokenType<TestToken> {
        EMPTY_TOKEN(EMPTY),

        // Illegal Character

        BAD_CHAR(TokenKind.BAD_CHAR),

        // WhiteSpace

        WHITE_SPACE(TokenKind.WHITE_SPACE), STRING_LITERAL(TokenKind.STRING), IDENTIFIER(TokenKind.IDENTIFIER), COMMENT(TokenKind.COMMENT),
        LINE_COMMENT(TokenKind.COMMENT), DOCUMENTATION(TokenKind.DOCUMENTATION), NULL(KEYWORD), TRUE(KEYWORD), FALSE(KEYWORD), HEX_INT(NUMBER),
        DEC_INT(NUMBER), FIXED_POINT_DECIMAL(NUMBER), DOUBLE_LITERAL(NUMBER);

        private final TokenKind kind;

        TestToken(final TokenKind kind) {
            this.kind = kind;
        }

        @NotNull @Override public TestToken target() {
            return this;
        }

        @NotNull @Override public String getDescription(final String tokenText) {
            return name();
        }

        @Override public boolean isIgnorable() {
            return false;
        }

        @Override public boolean isWhiteSpace() {
            return this == WHITE_SPACE;
        }

        @NotNull @Override public TokenKind getKind() {
            return kind;
        }

        @NotNull @Override public String getText() {
            return name();
        }

        @Override public boolean isEmpty() {
            return this == EMPTY_TOKEN;
        }

        private static final Tokens<TestToken> tokens = new Tokens<TestToken>(TestToken.class) {
                @Override public TestToken getHexInt() {
                    return HEX_INT;
                }
                @NotNull @Override public TestToken getDecInt() {
                    return DEC_INT;
                }
                @Override public TestToken getFixedDecimal() {
                    return FIXED_POINT_DECIMAL;
                }
                @NotNull @Override public TestToken getDouble() {
                    return DOUBLE_LITERAL;
                }
                @Override public TestToken getLineComment() {
                    return LINE_COMMENT;
                }

                @NotNull @Override public TestToken findKeyword(@NotNull String name) {
                    final TestToken value = keywordMap.get(name);
                    return value != null ? value : JavaReservedWords.isReserved(name) ? NULL : IDENTIFIER;
                }
            };
    }
}  // end class LexerTest
