
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
 * Task Parser.
 */
class TaskParser extends MetaModelParser {

    //~ Constructors .................................................................................................................................

    private TaskParser(MetaModelParser parent) {
        super(parent);
    }

    //~ Methods ......................................................................................................................................

    /** Task Parser. */
    public void parse() {
        parseLabeledId(false);
        parseTaskOptions();
        discardOrError(SEMICOLON);
        endTree(TASK);
    }

    private void parseExclusionGroup() {
        beginTree();
        discard();
        matchOrError(MMToken.STRING_LITERAL);
        endTree(EXCLUSION_GROUP);
    }

    private void parsePatternOption() {
        beginTree();
        discard();
        matchOrError(MMToken.STRING_LITERAL);
        endTree(PATTERN);
    }
    private void parseSchedule() {
        beginTree();
        discard();
        if (currentByText(MMToken.AFTER)) parseQualifiedId(TASK_REF, true);
        else matchOrError(MMToken.STRING_LITERAL);
        endTree(SCHEDULE);
    }

    private void parseTaskOptions() {
        while (notCurrentAnyOf(SEMICOLON)) {
            loopCheck();
            final MMToken token = getAnyOf(SCHEDULE, CLUSTER, NODE, TRANSACTION, PATTERN, EXCLUSION_GROUP);
            switch (token) {
            case SCHEDULE:
                parseSchedule();
                break;
            case CLUSTER:
                beginTree();
                discard();
                endTree(CLUSTER);
                break;
            case NODE:
                beginTree();
                discard();
                endTree(NODE);
                break;
            case TRANSACTION:
                parseTransactionOption();
                break;
            case PATTERN:
                parsePatternOption();
                break;
            case EXCLUSION_GROUP:
                parseExclusionGroup();
                break;
            default:
                unexpectedAndAdvanceTo(SEMICOLON);
                break;
            }
        }
    }

    private void parseTransactionOption() {
        beginTree();
        discard();
        final MMToken token = getAnyOf(NONE, ALL, EACH, ISOLATED);
        switch (token) {
        case NONE:
            beginTree();
            discard();
            endTree(NONE);
            break;
        case ALL:
            beginTree();
            discard();
            endTree(ALL);
            break;
        case ISOLATED:
            beginTree();
            discard();
            endTree(ISOLATED);
            break;
        case EACH:
            beginTree();
            discard();
            matchOrError(MMToken.DEC_INT);
            endTree(EACH);
            break;
        default:
            unexpectedAndAdvanceTo(SEMICOLON);
        }
        endTree(TRANSACTION);
    }

    //~ Methods ......................................................................................................................................

    static void register() {
        registerDefinitionParser(MMToken.TASK, TaskParser::new);
    }
}  // end class TaskParser
