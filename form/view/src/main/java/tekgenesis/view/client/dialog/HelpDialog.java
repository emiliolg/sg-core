
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.dialog;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

import org.jetbrains.annotations.NonNls;

import tekgenesis.common.core.Tuple;
import tekgenesis.view.client.ui.base.HtmlList;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;
import tekgenesis.view.client.ui.modal.ModalContent;

import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.metadata.form.model.FormConstants.ACTIVE_STYLE;
import static tekgenesis.view.client.FormViewMessages.MSGS;

/**
 * Shortcuts help modal.
 */
public class HelpDialog extends ModalContent {

    //~ Instance Fields ..............................................................................................................................

    private int                      bigger;
    private int                      selectedTab;
    private final FlowPanel          tabContent;
    private final HtmlList.Unordered ul;

    //~ Constructors .................................................................................................................................

    private HelpDialog() {
        ul         = HtmlWidgetFactory.ul();
        tabContent = new FlowPanel();
        setBody(createPanel());
        setTitle(MSGS.keyboardShortcuts());
        setCloseButton(true);
    }

    //~ Methods ......................................................................................................................................

    private void addChildPanel(final Widget w, String label) {
        w.addStyleName("tab-pane");
        w.removeStyleName("row");

        tabContent.add(w);

        final HtmlList.Item li     = HtmlWidgetFactory.li();
        final Anchor        anchor = HtmlWidgetFactory.anchor(label);

        final int tabIndex = ul.getWidgetCount();
        anchor.addClickHandler(event -> setActiveTab(tabIndex));

        if (tabContent.getWidgetCount() == 1) {
            li.setStyleName(ACTIVE_STYLE);
            w.addStyleName(ACTIVE_STYLE);
            selectedTab = 0;
        }

        li.add(anchor);
        ul.add(li);
    }

    private List<Tuple<String, String>> createCalcShortcutList() {
        final List<Tuple<String, String>> result = new ArrayList<>();

        result.add(tuple(key("c"), MSGS.calculatorShortcut()));
        result.add(tuple(key("0") + " " + MSGS.to().toLowerCase() + " " + key("9"), MSGS.commonCalculatorBehavior()));
        result.add(tuple(key(SHIFT_KEY, "c"), MSGS.calculatorClear()));
        result.add(tuple(key(BACKSPACE_KEY), MSGS.calculatorBackspace()));
        result.add(tuple(key(SHIFT_KEY, "q"), MSGS.calculatorAppendsTwoZeros()));
        result.add(tuple(key(SHIFT_KEY, "w"), MSGS.calculatorAppendsThreeZeros()));
        result.add(tuple(key("_"), MSGS.calculatorChangeSign()));
        result.add(tuple(key(SHIFT_KEY, "m"), MSGS.calculatorInsertMemory()));
        result.add(tuple(key(SHIFT_KEY, "r"), MSGS.calculatorRecoverMemory()));
        result.add(tuple(key(SHIFT_KEY, "x"), MSGS.calculatorExchangeMemory()));
        result.add(tuple(key(SHIFT_KEY, "l"), MSGS.calculatorCleanMemory()));
        result.add(tuple(key(SHIFT_KEY, "a"), MSGS.calculatorAddMemory()));
        result.add(tuple(key(SHIFT_KEY, "s"), MSGS.calculatorSubtractMemory()));
        result.add(tuple(key("!"), MSGS.calculatorFactorial()));

        return result;
    }

    private List<Tuple<String, String>> createGralShortcutList() {
        final List<Tuple<String, String>> result = new ArrayList<>();

        result.add(tuple(key("?"), MSGS.keyboardShortcuts()));
        result.add(tuple(key("e"), MSGS.recent()));
        result.add(tuple(key("n"), MSGS.searchForm()));
        // result.add(tuple(key("m"), MSGS.hideMenu()));
        result.add(tuple(key("F2"), MSGS.favorites()));
        result.add(tuple(key(ALT_KEY, "l"), MSGS.switchUser()));
        result.add(tuple(key(ALT_KEY, SHIFT_KEY, "l"), MSGS.logout()));
        result.add(tuple(key(ALT_KEY, "m"), MSGS.menuFocus()));
        result.add(tuple(key(MOD_KEY, "F1"), MSGS.help()));
        result.add(tuple(key(MOD_KEY, ENTER_KEY), MSGS.save()));
        result.add(tuple(key(MOD_KEY, SHIFT_KEY, "c"), MSGS.cancel()));
        result.add(tuple(key(MOD_KEY, SHIFT_KEY, "d"), MSGS.delete()));
        result.add(tuple(key("/") + " " + MSGS.or() + " " + key("-"), MSGS.search()));
        result.add(tuple(key("1") + " " + MSGS.to().toLowerCase() + " " + key("9"), MSGS.focusTabOrField()));
        result.add(tuple(key(MOD_KEY, UP_KEY), MSGS.nextRow()));
        result.add(tuple(key(MOD_KEY, DOWN_KEY), MSGS.previousRow()));
        result.add(tuple(key(MOD_KEY, RIGHT_KEY), MSGS.nextPage()));
        result.add(tuple(key(MOD_KEY, LEFT_KEY), MSGS.previousPage()));
        result.add(tuple(key(MOD_KEY, SHIFT_KEY, RIGHT_KEY), MSGS.nextYear()));
        result.add(tuple(key(MOD_KEY, SHIFT_KEY, LEFT_KEY), MSGS.previousYear()));

        return result;
    }

    private void createGrid(List<Tuple<String, String>> shortcuts, String label) {
        final Grid grid = new Grid(bigger, 2);
        grid.addStyleName("shortcutTable table-condensed table-striped");
        for (int row = 0; row < shortcuts.size(); ++row) {
            final Tuple<String, String> tuple = shortcuts.get(row);
            grid.setHTML(row, 0, tuple.first() + " :");
            grid.setText(row, 1, tuple.second());
        }
        addChildPanel(grid, label);
    }

    private Panel createPanel() {
        ul.setStyleName("nav nav-tabs");

        tabContent.setStyleName("tab-content");
        final FlowPanel div = HtmlWidgetFactory.div();
        div.add(ul);
        div.add(tabContent);
        selectedTab = 0;

        final List<Tuple<String, String>> shortcuts     = createGralShortcutList();
        final List<Tuple<String, String>> calcShortcuts = createCalcShortcutList();
        bigger = shortcuts.size() > calcShortcuts.size() ? shortcuts.size() : calcShortcuts.size();

        createGrid(shortcuts, MSGS.general());
        createGrid(calcShortcuts, MSGS.calculator());
        div.addStyleName("tabbable tabs-left");
        return div;
    }

    private void setActiveTab(int tabIndex) {
        for (int i = 0; i < tabContent.getWidgetCount(); i++) {
            final Widget tab           = ul.getWidget(i);
            final Widget contentWidget = tabContent.getWidget(i);

            tab.removeStyleName(ACTIVE_STYLE);
            contentWidget.removeStyleName(ACTIVE_STYLE);

            if (i == tabIndex) {
                tab.addStyleName(ACTIVE_STYLE);
                contentWidget.addStyleName(ACTIVE_STYLE);
                selectedTab = i;
            }
        }
    }

    //~ Methods ......................................................................................................................................

    /** Return helpDialog instance. */
    @SuppressWarnings("NonThreadSafeLazyInitialization")  // running on client side
    public static HelpDialog getInstance() {
        if (instance == null) instance = new HelpDialog();
        return instance;
    }

    private static String key(String key) {
        return "<kbd>" + key + "</kbd>";
    }

    private static String key(String first, String... rest) {
        return listOf(first, rest).map(HelpDialog::key).mkString(PLUS);
    }

    //~ Static Fields ................................................................................................................................

    private static HelpDialog instance = null;

    private static final boolean IS_MAC_PLATFORM = Window.Navigator.getPlatform().contains("Mac");

    @NonNls private static final String MOD_KEY       = IS_MAC_PLATFORM ? "⌘" : "Ctrl";
    @NonNls private static final String SHIFT_KEY     = "Shift";
    @NonNls private static final String BACKSPACE_KEY = "Backspace";
    @NonNls private static final String ALT_KEY       = "Alt";
    @NonNls private static final String ENTER_KEY     = "Enter";
    @NonNls private static final String DEL_KEY       = "Del";
    @NonNls private static final String PLUS          = " + ";
    @NonNls private static final String RIGHT_KEY     = "→";
    @NonNls private static final String LEFT_KEY      = "←";
    @NonNls private static final String UP_KEY        = "↑";
    @NonNls private static final String DOWN_KEY      = "↓";
}  // end class HelpDialog
