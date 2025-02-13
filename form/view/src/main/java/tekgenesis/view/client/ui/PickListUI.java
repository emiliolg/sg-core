
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;
import tekgenesis.view.client.ui.base.Icon;

import static tekgenesis.metadata.form.widget.IconType.*;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.div;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.list;

/**
 * A PickList UI widget.
 */
public class PickListUI extends HasOptionsUI {

    //~ Constructors .................................................................................................................................

    /** Creates a PickList UI widget. */
    public PickListUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        final int visibleRows = model.getVisibleRows() < 1 ? DEFAULT_VISIBLE_ROWS : model.getVisibleRows();
        init(new PickList(visibleRows));
    }

    //~ Methods ......................................................................................................................................

    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName("pickList");
    }

    // ** This widget doesn't support icon */
    @NotNull @Override Option<Element> createIcon() {
        return Option.empty();
    }

    //~ Static Fields ................................................................................................................................

    private static final int DEFAULT_VISIBLE_ROWS = 3;

    //~ Inner Classes ................................................................................................................................

    private class PickList extends FlowPanel implements HasOptionsComponent {
        private ValueChangeHandler<Object> handler = null;

        private final ListBox left;
        private final ListBox right;

        PickList(int rows) {
            left  = list(true, rows);
            right = list(true, rows);
            left.addStyleName(FormConstants.FORM_CONTROL);
            right.addStyleName(FormConstants.FORM_CONTROL);

            layout();
        }

        @Override public void addChangeHandler(ValueChangeHandler<Object> changeHandler) {
            handler = changeHandler;
        }

        @Override public void addOption(@NotNull String id, @NotNull String label) {
            final boolean selected = uiSelectionContains(id);
            if (selected) right.addItem(label, id);
            else left.addItem(label, id);
        }

        @Override public void clearOptions() {
            left.clear();
            right.clear();
        }

        @Override public void clearSelection() {
            uiSelectionClear();
        }

        @Override public void setFocus(boolean focus) {
            left.setFocus(focus);
        }

        @Override public void setSelectedOptions(@NotNull List<String> ids, boolean fireEvent) {
            // Nothing to do?
        }

        @NotNull private Button button(final IconType iconType, final ClickHandler clickHandler) {
            final Button button = HtmlWidgetFactory.button();
            Icon.inWidget(button, iconType.getClassName());
            button.addClickHandler(clickHandler);
            return button;
        }

        @NotNull private FlowPanel buttonPanel(Button... buttons) {
            final FlowPanel panel = div();
            for (final Button button : buttons)
                panel.add(button);
            panel.addStyleName(PICKLIST_BTNS_CLASS);
            return panel;
        }

        @NotNull private ClickHandler clickHandler(ListBox listBox, boolean all, boolean allSelected) {
            return event -> {
                       for (int i = 0; i < listBox.getItemCount(); i++)
                           uiSelectionUpdate(listBox.getValue(i), all ? allSelected : listBox.isItemSelected(i));
                       handler.onValueChange(null);         // null event may be dangerous
                          };
                          }

                          @NotNull private FlowPanel firstColumn() {
                          final Button toRight = button(ANGLE_RIGHT, clickHandler(left, false, false));
                          final Button allLeft = button(ANGLE_DOUBLE_LEFT, clickHandler(right, true, false));

                          return buttonPanel(toRight, allLeft);
                          }

                          private void layout() {
                          add(left);
                          add(firstColumn());
                          add(secondColumn());
                          add(right);
                          }

                          @NotNull private FlowPanel secondColumn() {
                          final Button allRight = button(ANGLE_DOUBLE_RIGHT, clickHandler(left, true, true));

                          final Button toLeft = button(ANGLE_LEFT,
                    event -> {
                        for (int i = 0; i < right.getItemCount(); i++) {
                            final boolean itemSelected = right.isItemSelected(i);
                            if (itemSelected) uiSelectionUpdate(right.getValue(i), false);
                        }
                        handler.onValueChange(null);  // null event may be dangerous
                    });

                          return buttonPanel(allRight, toLeft);
                          }

                          private static final String PICKLIST_BTNS_CLASS = "pickList-btns";
                          }  // end class PickList
                          }  // end class PickListUI
