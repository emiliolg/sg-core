
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

import tekgenesis.common.core.Option;
import tekgenesis.field.HasFieldOption;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.WidgetDef;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.metadata.form.widget.WidgetTypes;
import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.parser.Parser;
import tekgenesis.type.MetaModelKind;

import static tekgenesis.mmcompiler.ast.MMToken.*;
import static tekgenesis.mmcompiler.ast.MMToken.LIST;

/**
 * Parser for graphical definitions (such as {@link Form} and {@link WidgetDef}).
 */
abstract class UiModelParser extends MetaModelParser {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final MetaModelKind kind;
    @NotNull private final MMToken       token;

    //~ Constructors .................................................................................................................................

    /** Creates a graphical token parser. */
    UiModelParser(@NotNull MetaModelParser parent, @NotNull MMToken token, @NotNull MetaModelKind kind) {
        super(parent);
        this.token = token;
        this.kind  = kind;
    }

    //~ Methods ......................................................................................................................................

    public final void parse() {
        parseLabeledId(false);
        parseBinding();
        parseOptions();
        parseElements();
        endTree(token);
    }

    protected abstract void parseOptions();

    private void parseBinding() {
        if (discard(COLON)) parseType(false);
    }

    private void parseElements() {
        if (!parseList(LIST, LEFT_BRACE, null, RIGHT_BRACE, new WidgetParser())) discardOrError(SEMICOLON);
    }

    private void parseTableAggregates() {
        parseList(AGGREGATE, LEFT_BRACE, null, RIGHT_BRACE, new TableAggregateParser());
    }

    private boolean parseWith() {
        final boolean w = currentByText(WITH);
        if (w) {
            beginTree();
            discard();
            endTree(WITH);
        }
        return w;
    }

    //~ Inner Classes ................................................................................................................................

    private class TableAggregateParser implements Parser {
        @Override public void parse() {
            beginTree();
            parseLabeledId(false);
            if (discard(COLON)) {
                do {
                    parseRef(FIELD_REF);
                }
                while (discard(COMMA));
            }
            endTree(AGGREGATE);
            discardOrError(SEMICOLON);
        }
    }

    /**
     * Parser for widgets.
     */
    private class WidgetParser implements Parser {
        @Override public void parse() {
            beginTree();

            if (ahead(COLON, 1) || ahead(COLON, 2)) {
                parseLabeledId(true);
                discardOrError(COLON);
            }

            if (currentOrError(IDENTIFIER)) {
                final boolean commaConsumed = parseWidgetAndType();

                // Process the remaining options
                parseFieldOptions(kind, commaConsumed);
                // Check for table, group, header, footer
                if (current(LEFT_BRACE)) parseElements();

                // Check for table aggregates
                if (parseWith()) parseTableAggregates();

                discardOrError(SEMICOLON);
            }
            else {
                advanceTo(SEMICOLON, RIGHT_BRACE);
                discard(SEMICOLON);
            }
            endTree(WIDGET_FIELD);
        }

        private boolean parseWidgetAndType() {
            if (parseWidgetType(false)) return false;

            parseType(false);

            boolean commaConsumed = discard(COMMA);
            if (commaConsumed) {            // Have type.. check if I have also a WidgetType
                if (parseWidgetType(true))  // If I have a widget type mark comma as not consumed
                    commaConsumed = false;
            }
            return commaConsumed;
        }                                   // end method parseWidgetAndType

        private boolean parseWidgetType(boolean complete)
        {
            final WidgetType wt = WidgetTypes.fromId(getCurrentText());
            if (wt == null && (!complete || !isCompletion())) return false;
            beginTree();
            matchOrError(IDENTIFIER);

            /* Parse widget arguments. */
            parseList(LIST, LEFT_PAREN, COMMA, RIGHT_PAREN, new ArgumentParser(wt));

            endTree(WIDGET_TYPE);
            return true;
        }  // end method parseWidgetType

        private class ArgumentParser implements Parser {
            private int count;

            private final WidgetType type;

            private ArgumentParser(WidgetType type) {
                this.type = type;
                count     = 0;
            }

            @Override public void parse() {
                // Get next argument option or null. If null, advance, error is thrown on maker.
                final HasFieldOption argument = WidgetTypes.getArgument(type, count++);

                if (argument == null) consumeInvalidArgument();
                else {
                    final Option<MMToken> wrapper = MMToken.nodeFor(argument.getFieldOption());
                    wrapper.ifPresent(w -> beginTree());
                    parseFieldOptionValue(argument.getFieldOption());
                    wrapper.ifPresent(UiModelParser.this::endTree);
                }
            }  // end method parse

            private void consumeInvalidArgument() {
                beginTree();
                while (notCurrentAnyOf(COMMA, RIGHT_PAREN))
                    consume();
                endTree(LIST);
            }
        }  // end class ArgumentParser
    }  // end class WidgetParser
}
