
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.metadata.form.widget.TabType;
import tekgenesis.view.client.ui.base.HtmlList;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;
import tekgenesis.view.client.ui.base.Icon;

import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.core.Constants.TO_BE_IMPLEMENTED;
import static tekgenesis.metadata.form.model.FormConstants.ACTIVE_STYLE;
import static tekgenesis.metadata.form.widget.TabType.*;
import static tekgenesis.metadata.form.widget.WidgetType.TABS;
import static tekgenesis.view.client.event.Events.fireNativeClickEvent;

/**
 * Tab Group Widget.
 */
public class TabGroupUI extends ContainerUI implements HasScalarValueUI {

    //~ Instance Fields ..............................................................................................................................

    private final List<Anchor> anchorTabs = new ArrayList<>();

    private ValueChangeHandler<Object> changeHandler = null;

    private int selectedTab;

    private final FlowPanel          tabContent;
    private final HtmlList.Unordered ul;

    //~ Constructors .................................................................................................................................

    /** TabGroupUi constructor. */
    public TabGroupUI(@NotNull final ModelUI container, @NotNull final tekgenesis.metadata.form.widget.Widget model) {
        super(container, model);
        ul = HtmlWidgetFactory.ul();
        final TabType tabType = model.getTabType();
        ul.setStyleName(tabType.isPill() ? "nav nav-pills" : "nav nav-tabs");

        tabContent = new FlowPanel();
        tabContent.setStyleName("tab-content");

        if (tabType.isVertical()) {
            ul.addStyleName("nav-stacked col-md-3");
            tabContent.addStyleName("col-md-9");
        }

        if (tabType == BOTTOM || tabType == VERTICAL_RIGHT) {
            div.add(tabContent);
            div.add(ul);
        }
        else {
            div.add(ul);
            div.add(tabContent);
        }
        selectedTab = 0;
    }

    //~ Methods ......................................................................................................................................

    @Override public void addChangeHandler(ValueChangeHandler<Object> handler) {
        changeHandler = handler;
    }

    /** Click on a specific Tab by its label. Just for testing. */
    public void clickTab(@NotNull final String tabName) {
        for (final Anchor anchor : Colls.first(anchorTabs, a -> a != null && tabName.equals(a.getText())))
            fireNativeClickEvent(anchor);
    }

    /** Select a tab by tab index. */
    public void focusActiveTab(int tabNumber) {
        final int tabIndex = Math.max(0, Math.min(tabNumber, anchorTabs.size() - 1));
        setActiveTab(tabIndex);
        anchorTabs.get(tabIndex).setFocus(true);
    }

    @Override public void setReadOnly(boolean readOnly) {
        if (isTabGroup()) return;
        super.setReadOnly(readOnly);
    }

    /** Returns tab label, for testing purposes. */
    public String getTabLabel(int i) {
        if (anchorTabs.size() <= i) return "";
        return anchorTabs.get(i).getText();
    }

    @Override public Object getValue() {
        return selectedTab;
    }

    @Override public void setValue(@Nullable Object modelValue) {
        if (modelValue != null) setActiveTab((Integer) modelValue);
    }

    @Override public void setValue(@Nullable Object modelValue, boolean fireEvents) {
        setValue(modelValue);
        if (fireEvents) throw new UnsupportedOperationException(TO_BE_IMPLEMENTED);
    }

    @Override protected void addChildPanel(final WidgetUI w) {
        w.addStyleName("tab-pane");
        w.removeStyleName("row");
        final tekgenesis.metadata.form.widget.Widget childModel = w.getModel();

        w.setVisibilityListener(target -> {
            for (int i = 0; i < tabContent.getWidgetCount(); i++) {
                if (tabContent.getWidget(i) == target.first()) {
                    ul.getWidget(i).setVisible(target.second());
                    break;
                }
            }
            calculateGroupVisibility();
        });

        tabContent.add(w);

        final HtmlList.Item li     = HtmlWidgetFactory.li();
        final Anchor        anchor = HtmlWidgetFactory.anchor(childModel.getLabel());
        adaptWidgetForTab(w);
        Icon.inWidget(anchor, childModel.getIconStyle());
        anchorTabs.add(anchor);
        if (w instanceof ContainerUI) ((ContainerUI) w).setLabelHandler(anchor::setText);

        final int tabIndex = ul.getWidgetCount();
        anchor.addClickHandler(event -> setActiveTab(tabIndex));

        if (tabContent.getWidgetCount() == 1) {
            li.setStyleName(ACTIVE_STYLE);
            w.addStyleName(ACTIVE_STYLE);
            selectedTab = 0;
        }
        if (getModel().getTabType() == RIGHT) li.addStyleName("pull-right");

        li.add(anchor);
        ul.add(li);
    }

    @Override protected void addStyleNames() {
        final String display;
        switch (getModel().getTabType()) {
        case LEFT:
            display = "tabs-left";
            break;
        case BOTTOM:
            display = "tabs-below";
            break;
        case RIGHT:
            display = "tabs-right";
            break;
        case PILL:
        case TOP:
        default:
            display = "";
        }
        // noinspection SpellCheckingInspection
        addStyleName("tabbable " + display);
    }

    /** Select a tab by passing the child. */
    void setActiveTab(Widget child) {
        setActiveTab(tabContent.getWidgetIndex(child));
    }

    /** Method's responsibility is adapting widget so that all inside the tab is well displayed. */
    private void adaptWidgetForTab(WidgetUI w) {
        if (w instanceof GroupUI) {
            ((GroupUI) w).removeLegendElement();
            w.getElement().removeClassName("form-actions");  // removes class for a footer so that footer lines doesn't shows
        }
    }

    private void calculateGroupVisibility() {
        setVisible(immutable(ul).exists(Widget::isVisible));
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
                changeHandler.onValueChange(null);
            }
        }
    }

    /** Returns if this TabGroupUI represents a Tab Group. */
    private boolean isTabGroup() {
        return getModel().getWidgetType() == TABS;
    }
}  // end class TabGroupUI
