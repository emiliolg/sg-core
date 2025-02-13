
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler.parser;

import tekgenesis.metadata.menu.Menu;
import tekgenesis.parser.Parser;

import static tekgenesis.mmcompiler.ast.MMToken.*;

/**
 * Parser for {@link Menu} definition.
 */
class MenuParser extends MetaModelParser {

    //~ Constructors .................................................................................................................................

    private MenuParser(MetaModelParser parent) {
        super(parent);
    }

    //~ Methods ......................................................................................................................................

    public void parse() {
        parseLabeledId(false);
        parseMenuElements();
        endTree(MENU);
    }

    private void parseMenuElements() {
        if (!parseList(LIST, LEFT_BRACE, null, RIGHT_BRACE, new MenuElementParser())) discardOrError(SEMICOLON);
    }

    //~ Methods ......................................................................................................................................

    static void register() {
        registerDefinitionParser(MENU, MenuParser::new);
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Parser for Menu Elements.
     */
    private final class MenuElementParser implements Parser {
        @Override public void parse() {
            beginTree();

            if (getCurrent() == IDENTIFIER) {
                if (!isCompletion()) parseQualifiedId();
                else unexpectedAndAdvanceTo(SEMICOLON, RIGHT_BRACE);
            }
            else unexpectedAndAdvanceTo(SEMICOLON, RIGHT_BRACE);

            discardOrError(SEMICOLON);
            endTree(MENU_ELEMENT);
        }
    }  // end class MenuElementParser
}  // end class MenuParser
