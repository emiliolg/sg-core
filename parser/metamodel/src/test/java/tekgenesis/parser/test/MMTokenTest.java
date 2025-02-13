
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.parser.test;

import org.junit.Test;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Tuple5;
import tekgenesis.lexer.TokenKind;
import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.parser.Highlight;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.mmcompiler.ast.MMToken.*;
import static tekgenesis.parser.Highlight.*;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class MMTokenTest {

    //~ Methods ......................................................................................................................................

    @Test public void tokenDescriptions() {
        final ImmutableList<Tuple5<MMToken, Highlight, TokenKind, String, String>> tokens =  //
                                                                                            listOf(
                tuple(COMMENT, COMMENT_H, TokenKind.COMMENT, "<comment>", "COMMENT"),
                tuple(GE, OPERATOR_H, TokenKind.OPERATOR, "'>='", ">="),
                tuple(PACKAGE, KEYWORD_H, TokenKind.KEYWORD, "'package'", "package"),
                tuple(DOCUMENTATION, DOCUMENTATION_H, TokenKind.DOCUMENTATION, "<documentation>", "DOCUMENTATION"),
                tuple(IDENTIFIER, PLAIN_H, TokenKind.IDENTIFIER, "'IDENTIFIER'", "IDENTIFIER"));

        for (final Tuple5<MMToken, Highlight, TokenKind, String, String> entry : tokens) {
            final MMToken token = entry.first();
            assertThat(token.getHighlight()).isEqualTo(entry.second());
            assertThat(token.getKind()).isEqualTo(entry.third());
            assertThat(token.getDescription("")).isEqualTo(entry.fourth());
            assertThat(token.getText()).isEqualTo(entry.fifth());
        }
    }
}
