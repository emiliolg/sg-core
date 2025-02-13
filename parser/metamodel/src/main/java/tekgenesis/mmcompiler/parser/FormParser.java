
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
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.type.MetaModelKind;

import static tekgenesis.common.core.Option.ofNullable;
import static tekgenesis.field.FieldOption.fromId;
import static tekgenesis.mmcompiler.ast.MMToken.*;

/**
 * Parser for form definitions.
 */
class FormParser extends UiModelParser {

    //~ Constructors .................................................................................................................................

    /** Creates a form parser. */
    private FormParser(@NotNull MetaModelParser parent) {
        super(parent, FORM, MetaModelKind.FORM);
    }

    //~ Methods ......................................................................................................................................

    @Override protected void parseOptions() {
        while (notCurrentAnyOf(LEFT_BRACE, SEMICOLON)) {
            loopCheck();
            //J-
            final MMToken token = getAnyOf(ENTITY, ENUM, PRIMARY_KEY, PARAMETERS, PERMISSIONS, ON_LOAD, ON_DISPLAY, //
                    ON_CANCEL, ON_SCHEDULE, ON_ROUTE, HANDLER, LISTING, UNRESTRICTED, PROJECT);
            //J+
            switch (token) {
            case ENTITY:
                parseQualifiedId(DATAOBJECT_REF, true);
                break;
            case LISTING:
                parseQualifiedId(LISTING, true);
                break;
            case PRIMARY_KEY:
                parseOptionWithFields(PRIMARY_KEY);
                break;
            case PARAMETERS:
                parseOptionWithFields(PARAMETERS);
                break;
            case PERMISSIONS:
                parseOptionWithFieldsExpecting(PERMISSIONS, IDENTIFIER);
                break;
            case UNRESTRICTED:
                parseOptionWithoutFields(UNRESTRICTED);
                break;
            case ON_ROUTE:
                parseLiteral(token);
                break;
            case ON_LOAD:
            case ON_DISPLAY:
            case ON_CANCEL:
                parseMethodIdentifier(token);
                break;
            case HANDLER:
                parseQualifiedId(HANDLER, true);
                break;
            case ON_SCHEDULE:
                parseOnSchedule(token);
                break;
            case PROJECT:
                parseLiteral(token);
                break;
            default:
                unexpectedAndAdvanceTo(LEFT_BRACE);
                break;
            }
        }
    }  // end method parseOptions

    private void parseLiteral(MMToken token) {
        beginTree();
        discard();
        match(STRING_LITERAL);
        endTree(token);
    }

    private void parseMethodIdentifier(final MMToken token) {
        if (currentByText(token)) {
            beginTree();
            discard();
            final Option<MMToken> wrapper = getWrapper(token);
            wrapper.ifPresent(t -> beginTree());
            matchOrError(IDENTIFIER);
            wrapper.ifPresent(this::endTree);
            endTree(token);
        }
    }

    private void parseOnSchedule(MMToken token) {
        beginTree();
        parseMethodIdentifier(token);
        parseOptionalInteger();
        endTree(ON_SCHEDULE);
    }

    @Nullable private Option<MMToken> getWrapper(MMToken token) {
        return ofNullable(fromId(token.getText())).flatMap(MMToken::nodeFor);
    }

    //~ Methods ......................................................................................................................................

    static void register() {
        registerDefinitionParser(MMToken.FORM, FormParser::new);
    }
}  // end class FormParser
