
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
import tekgenesis.type.MetaModelKind;

import static tekgenesis.mmcompiler.ast.MMToken.*;

/**
 * Parser for widget definitions.
 */
public class WidgetDefParser extends UiModelParser {

    //~ Constructors .................................................................................................................................

    /** Creates a component parser. */
    private WidgetDefParser(@NotNull MetaModelParser parent) {
        super(parent, WIDGET, MetaModelKind.WIDGET);
    }

    //~ Methods ......................................................................................................................................

    @Override protected void parseOptions() {
        while (notCurrentAnyOf(LEFT_BRACE, SEMICOLON)) {
            loopCheck();
            final MMToken token = getAnyOf(PARAMETERS);
            switch (token) {
            case PARAMETERS:
                parseOptionWithFields(PARAMETERS);
                break;
            default:
                unexpectedAndAdvanceTo(LEFT_BRACE);
                break;
            }
        }
    }

    //~ Methods ......................................................................................................................................

    static void register() {
        registerDefinitionParser(WIDGET, WidgetDefParser::new);
    }
}
