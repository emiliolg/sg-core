
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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Anchor;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.model.KeyMap;
import tekgenesis.view.client.ui.base.HtmlList;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.anchor;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.li;

/**
 * Breadcrumb UI widget.
 */
public class BreadcrumbUI extends HasOptionsUI {

    //~ Instance Fields ..............................................................................................................................

    private int itemsSize;

    //~ Constructors .................................................................................................................................

    /** Creates RadioGroup UI widget. */
    public BreadcrumbUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        init(new BreadcrumbComponent());
    }

    //~ Methods ......................................................................................................................................

    @Override public void setDisabled(boolean disabled) {
        super.setDisabled(disabled);
        if (disabled) addStyleName(FormConstants.LINK_DISABLED);
        else removeStyleName(FormConstants.LINK_DISABLED);
    }

    public void setOptions(final KeyMap items) {
        if (items != null) {
            itemsSize = items.size();
            super.setOptions(items);
        }
    }

    @NotNull @Override Option<Element> createIcon() {
        return Option.empty();
    }

    //~ Inner Classes ................................................................................................................................

    private class BreadcrumbComponent extends HtmlList.Unordered implements HasOptionsComponent, ClickHandler {
        private ValueChangeHandler<String> valueChangeHandler = null;

        @SuppressWarnings("DuplicateStringLiteralInspection")
        private BreadcrumbComponent() {
            addStyleName("breadcrumb");
        }

        @Override public void addChangeHandler(ValueChangeHandler<Object> changeHandler) {
            valueChangeHandler = cast(changeHandler);
        }

        @Override public void addOption(@NotNull final String id, @NotNull final String label) {
            final int     widgetIndex = getWidgetCount();
            final boolean last        = itemsSize - 1 == widgetIndex;

            final HtmlList.Item li = li();

            if (last) {
                li.setText(label);
                li.addStyleName(FormConstants.ACTIVE_STYLE);
            }
            else {
                final Anchor anchor = anchor(label);
                anchor.getElement().setAttribute("data-key", id);
                anchor.addClickHandler(this);
                li.add(anchor);
            }

            add(li);
        }

        @Override public void clearOptions() {
            clear();
        }

        @Override public void clearSelection() {
            throw new UnsupportedOperationException("Unsupported operation '" + "clearSelected" + "' on Breadcrumb component");
        }

        @Override public void onClick(ClickEvent event) {
            uiSelectionClear();
            final String id = getKey((Anchor) event.getSource());
            uiSelectionUpdate(id, true);
            valueChangeHandler.onValueChange(null);
        }

        @Override public void setFocus(boolean focus) {
            Widgets.setFocus(this, focus);
        }

        // Breadcrumbs are special HasOptions component that only works for navigation, does not retain value in the model.
        @Override
        @SuppressWarnings("EmptyMethod")
        public void setSelectedOptions(@NotNull List<String> ids, boolean e) {
            // Ignored.
        }

        private String getKey(com.google.gwt.user.client.ui.Widget widget) {
            return widget.getElement().getAttribute("data-key");
        }
    }  // end class BreadcrumbComponent
}  // end class BreadcrumbUI
