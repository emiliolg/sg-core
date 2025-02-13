
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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.view.client.ui.base.Icon;
import tekgenesis.view.client.ui.base.Popover;
import tekgenesis.view.client.ui.modal.ModalListener;

import static tekgenesis.common.core.Option.some;
import static tekgenesis.metadata.form.model.FormConstants.OFFSET_PREFIX;
import static tekgenesis.metadata.form.model.FormConstants.WIDGET;
import static tekgenesis.metadata.form.model.FormConstants.WITH_OFFSET;
import static tekgenesis.view.client.Application.getInstance;
import static tekgenesis.view.client.Application.messages;
import static tekgenesis.view.client.ui.base.Popover.POPOVER_TITLE;

/**
 * Popover Group Widget.
 */
public class PopoverUI extends ContainerUI implements HasScalarValueUI, ClickHandler, ModalListener {

    //~ Instance Fields ..............................................................................................................................

    private ValueChangeHandler<Object> changeHandler = null;
    @SuppressWarnings("FieldMayBeFinal")
    private boolean                    opened  = false;
    private final Popover              popover;

    //~ Constructors .................................................................................................................................

    /** Popover constructor. */
    public PopoverUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model, new Popover(model.getPopoverType()));
        popover = (Popover) div;
        addCols();
        popover.setListener(this);
        if (model.hasHover()) popover.addHoverTrigger();
    }

    //~ Methods ......................................................................................................................................

    @Override public void addChangeHandler(ValueChangeHandler<Object> handler) {
        changeHandler = handler;
    }

    @Override public void addStyleName(String style) {
        if (popover != null) popover.addLinkStyleName(style);
    }

    @Override public void click() {
        popover.clickLink();
    }

    // open the dialog when clicking the link
    @Override public void onClick(ClickEvent event) {
        setValue(!opened);
    }

    @Override public void onHide(ModalButtonType buttonClicked) {
        popover.setLinkIcon(getModel().getIconStyle());
        opened = false;
        changeHandler.onValueChange(null);
    }

    @Override public void onShow() {
        popover.setLinkIcon(getModel().getIconSelectedStyle());
        opened = true;
        changeHandler.onValueChange(null);
        getInstance().setCurrentPopover(popover);
    }

    /** Content style added to popover . */
    public void setContentStyleName(String styleName) {
        popover.addContentStyleName(styleName);
    }

    @Override public void setLabelFromExpression(String label) {
        popover.setLinkTextSafe(label);
    }

    // use custom 'opened' flag instead of dialog.isActive() because of expression evaluation
    @Override public Object getValue() {
        return opened;
    }

    @Override public void setValue(@Nullable Object modelValue) {
        final Popover currentPopover = getInstance().getCurrentPopover();
        final boolean show           = modelValue != null && (Boolean) modelValue;
        if (show && currentPopover != null && currentPopover.isShowing())
            messages().error("Popover is already showing", getInstance().getActiveOrMain());
        else if (show) popover.showContent();
        else popover.hideContent();
    }

    @Override public void setValue(@Nullable Object value, boolean fireEvents) {
        setValue(value);
        if (fireEvents) throw new UnsupportedOperationException(Constants.TO_BE_IMPLEMENTED);
    }

    @Override protected void addChildPanel(final WidgetUI w) {
        final WidgetType type = w.getModel().getWidgetType();
        if (type == WidgetType.FOOTER) {
            w.addStyleName("popover-footer");
            popover.addFooter(w);
        }
        else if (type == WidgetType.HEADER) {
            w.addStyleName(POPOVER_TITLE);
            popover.addHeader(w);
        }
        else popover.addContent(w);
        w.addStyleName("in-popover");
    }

    @NotNull @Override Option<Element> createIcon() {
        return some(popover.getElement());
    }

    @NotNull @Override Option<Element> createLabel() {
        setLabelFromExpression(getModel().getLabel());
        return Option.empty();
    }

    @Override Option<Icon> iconInWidget(Element icon, String iconStyle) {
        popover.setLinkIcon(iconStyle);
        return Option.empty();
    }

    private void addCols() {
        popover.addStyleName(WIDGET);
        final int col    = getModel().getCol();
        final int offset = getModel().getOffsetCol();
        if (col > 0) popover.addStyleName("col-sm-" + col);
        if (offset > 0) {
            popover.addStyleName(OFFSET_PREFIX + offset);
            popover.addStyleName(WITH_OFFSET);
        }
    }
}  // end class PopoverUI
