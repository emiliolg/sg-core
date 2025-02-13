
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.tablefilters;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TekSuggestBox;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.type.Kind;
import tekgenesis.view.client.suggest.ItemSuggestion;
import tekgenesis.view.client.suggest.KeySuggestOracle;
import tekgenesis.view.client.ui.FlexTableUI;
import tekgenesis.view.client.ui.base.ExtButton;
import tekgenesis.view.client.ui.base.Icon;
import tekgenesis.view.client.ui.base.Popover;
import tekgenesis.view.client.ui.base.TextField;

import static com.google.gwt.user.client.Event.FOCUSEVENTS;
import static com.google.gwt.user.client.Event.KEYEVENTS;
import static com.google.gwt.user.client.Event.ONCHANGE;
import static com.google.gwt.user.client.Event.ONCLICK;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.metadata.form.model.FormConstants.BIG_COMBO;
import static tekgenesis.metadata.form.model.FormConstants.FORM_CONTROL;
import static tekgenesis.metadata.form.widget.IconType.CLOSE;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.list;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.textField;
import static tekgenesis.view.client.ui.tablefilters.Comparison.comparisonsFor;
import static tekgenesis.view.client.ui.tablefilters.Comparison.valueOf;
import static tekgenesis.view.client.ui.tablefilters.FilterUI.FilterWidgets.createBooleanComboPanel;
import static tekgenesis.view.client.ui.tablefilters.FilterUI.FilterWidgets.createComboPanel;
import static tekgenesis.view.client.ui.tablefilters.FilterUI.FilterWidgets.createSuggestPanel;
import static tekgenesis.view.client.ui.tablefilters.FilterUI.FilterWidgets.createTextFieldPanel;

/**
 * Table filter UI.
 */
public class FilterUI {

    //~ Instance Fields ..............................................................................................................................

    private final Option<FilterWidget> panel;

    //~ Constructors .................................................................................................................................

    /** Creates a Filter UI. */
    public FilterUI(Widget model) {
        panel = buildUIFor(model.getType().getKind());
    }

    //~ Methods ......................................................................................................................................

    /** Returns an option that represents a possible filter ui. */
    public Option<FilterWidget> getPanel() {
        return panel;
    }

    private Option<FilterWidget> buildUIFor(Kind k) {
        switch (k) {
        case INT:
        case REAL:
        case DECIMAL:
        case STRING:
        case DATE:
        case DATE_TIME:
            return some(createTextFieldPanel(k));
        case REFERENCE:
            return some(createSuggestPanel());
        case BOOLEAN:
            return some(createBooleanComboPanel());
        case ENUM:
            return some(createComboPanel());
        default:
            return empty();
        }
    }

    //~ Inner Interfaces .............................................................................................................................

    public interface FilterWidget {
        /** Add option for an option filter widget. */
        void addOption(String key, String label);

        /** Returns this as a FlowPanel. */
        FlowPanel asFlowPanel();

        /** Attaches DOM events to elements of widgets. */
        void attach();

        /** Detaches DOM events to elements of widgets. */
        void detach();

        /** Resets this filter. */
        void reset();

        /** Selects this filter as the active one or not. */
        void select(boolean s);

        /** Returns the column that this filter widget represents. */
        String getColumn();

        /** Returns selected comparison. */
        Comparison getComparison();

        /** Registers the filter handler to the filter UI. */
        void setFilterHandler(String col, FlexTableUI.FilterHandler handler);

        /** Registers parent popover. */
        void setParentPopover(Popover popover);
    }

    //~ Inner Classes ................................................................................................................................

    private abstract static class BaseFilterWidget extends FlowPanel implements FilterWidget {
        String                    column        = null;
        FlexTableUI.FilterHandler filterHandler = null;
        private Popover           popover       = null;
        private ExtButton         reset         = null;

        @Override public FlowPanel asFlowPanel() {
            return this;
        }

        @Override public void detach() {
            DOM.setEventListener(reset.getElement(), null);
        }

        @Override public void reset() {
            popover.getLink().removeStyleName("filter-active");
        }

        @Override
        @SuppressWarnings({ "DuplicateStringLiteralInspection", "GWTStyleCheck" })
        public void select(boolean s) {
            if (s) popover.getLink().addStyleName("filter-active");
            else {
                if (popover.getLink().getStyleName().contains("filter-active")) reset();
            }
        }

        @Override public String getColumn() {
            return column;
        }

        @Override public void setFilterHandler(String col, FlexTableUI.FilterHandler handler) {
            column        = col;
            filterHandler = handler;
        }

        @Override public void setParentPopover(Popover p) {
            popover = p;
        }

        final void addReset() {
            reset = new ExtButton(new Icon(CLOSE));
            reset.setStyleName("filter-reset");
            add(reset);

            DOM.setEventListener(reset.getElement(), reset);
            DOM.sinkEvents(reset.getElement(), ONCLICK);

            reset.addClickHandler(e -> filterHandler.filtered(column, getComparison(), ""));
        }

        abstract void initUi();
        abstract void prepareEvents();
    }  // end class BaseFilterWidget

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static class BooleanComboPanel extends ComboPanel {
        @Override void initUi() {
            super.initUi();
            addOption("true", MSGS.trueLabel());
            addOption("false", MSGS.falseLabel());
        }
    }

    private static class ComboPanel extends BaseFilterWidget implements FilterWidget {
        private ListBox      combo  = null;
        private List<String> values = null;

        private ComboPanel() {
            values = new ArrayList<>();
        }

        @Override public void addOption(String value, String label) {
            if (!values.contains(value)) {
                values.add(value);
                combo.addItem(label, value);
            }
        }

        @Override public void attach() {
            DOM.setEventListener(combo.getElement(), combo);
            DOM.sinkEvents(combo.getElement(), ONCHANGE);
        }

        @Override public void detach() {
            super.detach();
            DOM.setEventListener(combo.getElement(), null);
        }

        @Override public void reset() {
            super.reset();
            combo.setSelectedIndex(0);
        }

        @Override public Comparison getComparison() {
            return Comparison.EQUALS;
        }

        @Override void initUi() {
            combo = list(false, 1);
            combo.addStyleName("filter-combo");
            combo.addStyleName(FORM_CONTROL);
            combo.addStyleName(BIG_COMBO);

            // Reset option
            combo.addItem("", "");

            add(combo);
        }

        //J-
        @Override void prepareEvents() {
            combo.addChangeHandler(e -> filterHandler.filtered(column, getComparison(), combo.getValue(combo.getSelectedIndex())));
        }
        //J+

    }  // end class ComboPanel

    static class FilterWidgets {
        private FilterWidgets() {}

        static FilterWidget createBooleanComboPanel() {
            return init(new BooleanComboPanel());
        }

        static FilterWidget createComboPanel() {
            return init(new ComboPanel());
        }

        static FilterWidget createSuggestPanel() {
            return init(new SuggestPanel());
        }

        static FilterWidget createTextFieldPanel(Kind k) {
            return init(new TextFieldPanel(k));
        }

        static FilterWidget init(BaseFilterWidget w) {
            w.initUi();
            w.addReset();
            w.prepareEvents();
            return w;
        }
    }

    private static class SuggestPanel extends BaseFilterWidget implements FilterWidget {
        private TekSuggestBox    suggestBox    = null;
        private KeySuggestOracle suggestOracle = null;

        public void addOption(String value, String label) {
            final ItemSuggestion suggestion = new ItemSuggestion(value, label);
            if (!immutable(suggestOracle.getOptions()).exists(s -> s.getKey().equals(value))) suggestOracle.addSuggestion(suggestion);
        }

        public void attach() {
            DOM.setEventListener(suggestBox.getTextBox().getElement(), suggestBox.getTextBox());
            DOM.sinkEvents(suggestBox.getTextBox().getElement(), KEYEVENTS | FOCUSEVENTS);
        }

        @Override public void detach() {
            super.detach();
            DOM.setEventListener(suggestBox.getTextBox().getElement(), null);
        }

        @Override public void reset() {
            super.reset();
            suggestBox.setText("");
        }

        @Override public Comparison getComparison() {
            return Comparison.EQUALS;
        }

        @Override void initUi() {
            suggestOracle = new KeySuggestOracle();
            suggestBox    = new TekSuggestBox(suggestOracle, true, true);
            suggestBox.addStyleName("filter-suggest");
            suggestBox.setPlaceholder(MSGS.searchPlaceHolder());

            add(suggestBox);
        }

        //J-
        @Override void prepareEvents() {
            suggestBox.addSelectionHandler(e -> {
                final ItemSuggestion item = ((ItemSuggestion) e.getSelectedItem());
                final String t = item != null ? item.getKey() : "";
                filterHandler.filtered(column, getComparison(), t);
            });
        }
        //J+
    }  // end class SuggestPanel

    private static class TextFieldPanel extends BaseFilterWidget implements FilterWidget {
        private ListBox    comparisons = null;
        private final Kind kind;
        private TextField  text        = null;

        private TextFieldPanel(Kind k) {
            kind = k;
        }

        @Override public void addOption(String key, String label) {}

        @Override public void attach() {
            DOM.setEventListener(text.getElement(), text);
            DOM.sinkEvents(text.getElement(), ONCHANGE);

            DOM.setEventListener(comparisons.getElement(), comparisons);
            DOM.sinkEvents(comparisons.getElement(), ONCHANGE);
        }

        @Override public void detach() {
            super.detach();
            DOM.setEventListener(text.getElement(), null);
            DOM.setEventListener(comparisons.getElement(), null);
        }

        @Override public void reset() {
            super.reset();
            text.setText("");
            comparisons.setSelectedIndex(0);
        }

        public Comparison getComparison() {
            return valueOf(comparisons.getValue(comparisons.getSelectedIndex()));
        }

        @Override void initUi() {
            comparisons = list(false, 1);
            comparisons.addStyleName("filter-combo");
            comparisons.addStyleName(FORM_CONTROL);
            comparisons.addStyleName(BIG_COMBO);

            text = textField();
            text.addStyleName("filter-text");
            text.addStyleName(FORM_CONTROL);

            comparisonsFor(kind).forEach(c -> comparisons.addItem(c.label(), c.value()));

            add(comparisons);
            add(text);
        }

        @Override void prepareEvents() {
            comparisons.addChangeHandler(e -> {
                if (isNotEmpty(text.getValue())) filterHandler.filtered(column, getComparison(), text.getValue());
            });
            text.addChangeHandler(e -> filterHandler.filtered(column, getComparison(), text.getValue()));
        }
    }  // end class TextFieldPanel
}  // end class FilterUI
