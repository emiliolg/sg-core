
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler.parser;

import tekgenesis.metadata.role.Role;
import tekgenesis.parser.Parser;

import static tekgenesis.mmcompiler.ast.MMToken.*;

/**
 * Parser for {@link Role} definition.
 */
class RoleParser extends MetaModelParser {

    //~ Constructors .................................................................................................................................

    private RoleParser(MetaModelParser parent) {
        super(parent);
    }

    //~ Methods ......................................................................................................................................

    public void parse() {
        parseLabeledId(false);
        parseRolePermissions();
        endTree(ROLE);
    }

    private void parseRolePermissions() {
        if (!parseList(LIST, LEFT_BRACE, null, RIGHT_BRACE, new RolePermissionsParser())) discardOrError(SEMICOLON);
    }

    //~ Methods ......................................................................................................................................

    static void register() {
        registerDefinitionParser(ROLE, RoleParser::new);
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Parser for Role permissions.
     */
    private final class RolePermissionsParser implements Parser {
        @Override public void parse() {
            beginTree();

            if (getCurrent() == IDENTIFIER) {
                if (!isCompletion()) {
                    parseQualifiedId();
                    parseList(LIST, LEFT_PAREN, COMMA, RIGHT_PAREN, () -> {
                            beginTree();
                            matchIdOrError();
                            endTree(ROLE_PERMISSION);
                        });
                }
                else unexpectedAndAdvanceTo(SEMICOLON, RIGHT_BRACE);
            }
            else unexpectedAndAdvanceTo(SEMICOLON, RIGHT_BRACE);

            discardOrError(SEMICOLON);
            endTree(ROLE_ELEMENT);
        }
    }
}
