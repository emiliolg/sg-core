
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Anchor;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.base.CustomPanel;
import tekgenesis.view.client.ui.base.DropdownButton;
import tekgenesis.view.client.ui.base.Icon;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.forAll;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.of;
import static tekgenesis.common.core.Option.ofNullable;
import static tekgenesis.metadata.form.model.FormConstants.DISABLED_STYLE;
import static tekgenesis.metadata.form.model.FormConstants.LINK_DISABLED;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.anchorPanel;

/**
 * DropDown UI widget.
 */
public class DropDownUI extends ContainerUI implements HasTooltipUI {

    //~ Instance Fields ..............................................................................................................................

    private final DropdownButton dropdownButton;
    private boolean              dropdownVisible = true;
    private final boolean        isSplit;
    private Anchor               mainButton;

    //~ Constructors .................................................................................................................................

    /** Creates DropDown UI widget. */
    public DropDownUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model, new DropdownButton());
        addStyleName("d-down");
        dropdownButton = (DropdownButton) div;
        mainButton     = null;
        final String text = getModel().getLabel();
        isSplit = getModel().isSplit();
        setDropDownButton(text);

        // When having a tooltip expression, initialize a tooltip object and add mouse handlers
        if (!model.getTooltip().isEmpty()) dropdownButton.addTooltip();
        setFocusTarget(ofNullable(dropdownButton.getFocusTarget()));
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean acceptsFocus() {
        return getFocusTarget().isPresent();
    }

    @Override public void setContentStyleName(String styleName) {
        dropdownButton.setStyle(styleName);
        if (mainButton != null) mainButton.addStyleName(styleName);
    }

    @Override public void setDisabled(boolean disabled) {
        if (mainButton != null) {
            super.setDisabled(disabled, mainButton);
            if (disabled) mainButton.addStyleName(LINK_DISABLED + " " + DISABLED_STYLE);
            else mainButton.removeStyleName(LINK_DISABLED + " " + DISABLED_STYLE);
        }
        else {
            super.setDisabled(disabled);
            if (disabled) addStyleName(LINK_DISABLED);
            else removeStyleName(LINK_DISABLED);
        }
    }

    @Override public void setFocus(boolean focused) {
        if (mainButton != null) mainButton.setFocus(focused);
        else dropdownButton.getFocusTarget().setFocus(focused);
    }

    @Override public void setTooltip(String tooltip) {
        dropdownButton.setTooltip(tooltip);
    }

    @Override public void setVisible(boolean visible) {
        super.setVisible(visible);
        dropdownVisible = visible;
        if (forAll(this, w -> w != null && !w.isVisible())) super.setVisible(false);
    }

    @Override protected void addChildPanel(final WidgetUI w) {
        w.setVisibilityListener(t -> hideDropdown());
        if (isSplit && mainButton == null && w instanceof LabelUI) mainButton = dropdownButton.setMainButton((LabelUI) w);
        else {
            final CustomPanel anchor = anchorPanel();
            anchor.addStyleName("sub-anchor");
            anchor.add(w);
            dropdownButton.add(anchor);
        }
    }  // end method addChildPanel

    @NotNull @Override Option<Element> createIcon() {
        return of(dropdownButton.getElement());
    }

    @Override Option<Icon> iconInWidget(Element icon, String iconStyle) {
        dropdownButton.setIcon(getModel().getIconStyleEnum());
        return empty();
    }

    private void hideDropdown() {
        super.setVisible(true);
        if (forAll(this, w -> w != null && !w.isVisible())) super.setVisible(false);
        else super.setVisible(dropdownVisible);
    }

    private void setDropDownButton(String text) {
        if (isNotEmpty(text)) dropdownButton.setMainText(text);
        dropdownButton.setCaret();
    }
}  // end class DropDownUI
