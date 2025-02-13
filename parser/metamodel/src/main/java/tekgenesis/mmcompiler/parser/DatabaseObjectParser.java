
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler.parser;

import tekgenesis.mmcompiler.ast.MMToken;

import static tekgenesis.mmcompiler.ast.MMToken.*;

/**
 * Database Object Parser.
 */
class DatabaseObjectParser extends MetaModelParser {

    //~ Constructors .................................................................................................................................

    DatabaseObjectParser(MetaModelParser parent) {
        super(parent);
    }

    //~ Methods ......................................................................................................................................

    protected void parseCache() {
        beginTree();
        discard();
        if (currentByText(ALL)) consume();
        else parseOptionalInteger();
        endTree(CACHE);
    }

    protected void parseIndex(MMToken token) {
        beginTree();
        discard();
        final MMToken mmToken = lookAhead(1);
        boolean       withId  = false;
        if (mmToken == MMToken.LEFT_PAREN) {
            withId = true;
            beginTree();
            matchOrError(IDENTIFIER);
            endTree(IDENTIFIER);
            discard();
        }
        parseRef(FIELD_REF);
        while (discard(COMMA))
            parseRef(FIELD_REF);
        if (withId) matchOrError(MMToken.RIGHT_PAREN);
        endTree(token);
    }

    protected void parseTableName() {
        beginTree();
        discard();
        matchIdOrError();
        if (discard(DOT)) matchIdOrError();
        endTree(TABLE);
    }
}  // end class DatabaseObjectParser
