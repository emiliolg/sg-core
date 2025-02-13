
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler.parser;

import org.jetbrains.annotations.NotNull;

import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.mmcompiler.parser.TypeParser.TypeFieldParser;
import tekgenesis.parser.Parser;
import tekgenesis.type.MetaModelKind;

import static tekgenesis.mmcompiler.ast.MMToken.*;

/**
 * Parser for Case definitions.
 */
class CaseParser extends MetaModelParser {

    //~ Constructors .................................................................................................................................

    /** Creates a Case Parser. */
    private CaseParser(@NotNull MetaModelParser parent) {
        super(parent);
    }

    //~ Methods ......................................................................................................................................

    public void parse() {
        parseLabeledId(false);
        parseCaseOptions();
        parseCaseTasks();
        endTree(CASE);
    }

    private void parseCaseOptions() {
        while (notCurrentAnyOf(LEFT_BRACE)) {
            loopCheck();
            final MMToken token = getAnyOf(ENTITY, FORM, WITH, NOTIFY);
            switch (token) {
            case ENTITY:
                parseQualifiedId(ENTITY_REF, true);
                break;
            case FORM:
                parseQualifiedId(FORM_REF, true);
                break;
            case WITH:
                discard();
                parseList(WITH, LEFT_BRACE, null, RIGHT_BRACE, new TypeFieldParser(this));
                break;
            case NOTIFY:
                parseOptionWithoutFields(NOTIFY);
                break;
            default:
                unexpectedAndAdvanceTo(LEFT_BRACE);
                break;
            }
        }
    }

    private void parseCaseTasks() {
        if (!parseList(LIST, LEFT_BRACE, null, RIGHT_BRACE, new CaseTaskParser())) discardOrError(SEMICOLON);
    }

    //~ Methods ......................................................................................................................................

    static void register() {
        registerDefinitionParser(MMToken.CASE, CaseParser::new);
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Parser for Case Tasks.
     */
    private class CaseTaskParser implements Parser {
        @Override public void parse() {
            beginTree();
            parseLabeledId(false);
            if (discard(COLON)) parseFieldOptions(MetaModelKind.CASE, true);
            discard(SEMICOLON);
            endTree(FIELD);
        }
    }
}  // end class CaseParser
