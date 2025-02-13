
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

import static tekgenesis.mmcompiler.ast.MMToken.*;

/**
 * Parser for anonymous paths definitions. Path parser is injected on handler's string literal
 * paths.
 */
class InterpolationParser extends MetaModelParser {

    //~ Constructors .................................................................................................................................

    /** Creates a Handler Parser. */
    private InterpolationParser(@NotNull MetaModelParser parent) {
        super(parent);
    }

    //~ Methods ......................................................................................................................................

    public void parse() {
        if (discard(COLON)) {
            while (!eof() && !interpolationEnd()) {
                if (discard(DOLLAR)) {
                    if (!discard(DOLLAR)) parseId(PARAMETER);
                }
                else discard();
            }
        }
        else {
            do
                advanceTo(INTERPOLATION);
            while (!interpolationEnd());
        }

        discard(INTERPOLATION);
        discardOrError(SEMICOLON);

        endTree(INTERPOLATION);
    }

    private boolean interpolationEnd() {
        return current(INTERPOLATION) && next(SEMICOLON);
    }

    //~ Methods ......................................................................................................................................

    static void register() {
        registerDefinitionParser(MMToken.INTERPOLATION, InterpolationParser::new);
    }
}  // end class InterpolationParser
