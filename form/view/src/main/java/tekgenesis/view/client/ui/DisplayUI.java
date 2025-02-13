
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
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Image;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.formatter.InputHandler;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;
import tekgenesis.view.client.ui.base.Icon;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.core.Constants.ITEM_IMAGE_CLASS;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.Strings.coverText;
import static tekgenesis.metadata.form.model.FormConstants.DISPLAY_WIDGET;
import static tekgenesis.metadata.form.model.FormConstants.ELLIPSIS;
import static tekgenesis.view.client.formatter.InputHandler.none;
import static tekgenesis.view.client.ui.HasLinkUI.FormLinkUtil.hasAutoLinkPk;
import static tekgenesis.view.client.ui.HasLinkUI.FormLinkUtil.hasLink;
import static tekgenesis.view.client.ui.HasLinkUI.FormLinkUtil.link;
import static tekgenesis.view.client.ui.HasLinkUI.FormLinkUtil.updateLinkPk;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.div;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.inlineLabel;
import static tekgenesis.view.client.ui.base.Icon.replaceInWidget;

/**
 * Text UI Widget.
 */
public class DisplayUI extends FieldWidgetUI implements HasScalarValueUI, HasInputHandlerUI, HasLinkUI, HasTextLengthUI, HasMaskUI {

    //~ Instance Fields ..............................................................................................................................

    private final com.google.gwt.user.client.ui.Widget component;

    @Nullable private Image image = null;

    private InputHandler<?>  inputHandler;
    private int              length;
    @Nullable private String linkPk     = null;
    @Nullable private Object modelValue = null;

    //~ Constructors .................................................................................................................................

    /** Creates Text UI Widget. */
    public DisplayUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        inputHandler = none();

        component = hasLink(model) ? link(model) : inlineLabel();

        if (model.hasImage()) {
            final FlowPanel panel = div();
            image = HtmlWidgetFactory.img();
            image.addStyleName(ITEM_IMAGE_CLASS);
            panel.add(image);
            panel.add(component);
            initWidget(panel);
        }
        else initWidget(component);
    }

    //~ Methods ......................................................................................................................................

    @Override public void addChangeHandler(ValueChangeHandler<Object> changeHandler) {
        /* read-only */
    }

    /** Adds a click handler to this Button UI widget. */
    public void addClickHandler(final ClickHandler handler) {
        ((HasClickHandlers) component).addClickHandler(handler);
    }

    /** When bound to Enum or Entity used to set the describe by text to display. */
    public void setDescribeByValue(final String value, String imagePath) {
        setText(value, imagePath);
    }

    @Override public void setDisabled(boolean disabled) {
        // first call super, this will set disabled state on widget, but not on the UI.
        super.setDisabled(disabled);

        // disable/enable UI element now
        disableElement(disabled);
    }

    @Override public void setInputHandler(InputHandler<?> inputHandler) {
        this.inputHandler = inputHandler;
    }

    @Override public void setLength(int width, boolean expand) {
        // setTextLength(length);
    }

    /** Sets a custom href to the text. */
    public void setLink(final String href) {
        ((Anchor) component).setHref(href);
    }

    @Nullable @Override public String getLinkPk() {
        return linkPk;
    }

    @Override public void setLinkPk(@Nullable String pk) {
        updateLinkPk((Anchor) component, getModel(), linkPk = pk);
    }

    @Override public void setPlaceholder(String placeholder) {
        if (!isEmpty(placeholder) && (modelValue == null || isEmpty(String.valueOf(modelValue)))) setPrimitiveValue(placeholder);
    }

    /** When bound to a primitive type used to set the text to display. */
    public void setPrimitiveValue(@Nullable final Object value) {
        final InputHandler<Object> objectInputHandler = cast(inputHandler);
        setText(notNull(objectInputHandler.format(value)), "");
    }

    /** Sets the maximum length of the field. */
    @Override public void setTextLength(int length) {
        this.length = length;
        setPrimitiveValue(getValue());
    }

    @Override public void setTooltip(String message) {
        super.setTooltip(message);

        if (isNotEmpty(getModel().getIconStyle())) tooltipDiv.setIcon(getModel().getIconStyleEnum());
    }

    @Nullable @Override public Object getValue() {
        return modelValue;
    }

    @Override public void setValue(@Nullable final Object modelValue) {
        setValue(modelValue, false);
    }

    @Override public void setValue(@Nullable final Object value, boolean fireEvents) {
        modelValue = value;
        setPrimitiveValue(value);
        if (hasAutoLinkPk(getModel())) setLinkPk(value != null ? value.toString() : null);
    }

    @Override void addStyleNames() {
        super.addStyleNames();
        addStyleName(DISPLAY_WIDGET);
    }

    @NotNull @Override Option<Element> createIcon() {
        return some(component.getElement());
    }

    @Override Option<Icon> iconInWidget(Element icon, String iconStyle) {
        return replaceIcon(iconStyle);
    }

    private boolean hasTooltip() {
        return getModel().getTooltip().isEmpty();
    }

    private Option<Icon> replaceIcon(@NotNull String iconStyle) {
        if (hasTooltip()) {
            final Option<Icon> iconOption = replaceInWidget(component, iconStyle);
            iconOption.forEach(i -> i.addStyleName(CUSTOM_ICON));
            return iconOption;
        }
        return Option.empty();
    }

    private void setText(String value, String imagePath) {
        if (getModel().hasImage() && isNotEmpty(imagePath) && image != null) {
            image.setUrl(imagePath);
            image.setVisible(true);
        }
        else if (image != null) image.setVisible(false);

        final boolean mustCover   = getModel().isEntity() && !getModel().isFullText();
        final String  coveredText = mustCover ? coverText(value) : value;

        String text  = coveredText;
        String title = "";

        if (text != null && text.length() > length) {
            text  = coveredText.substring(0, length) + ELLIPSIS;
            title = value;
        }
        else if (text != null)
            if (mustCover) title = value;

        ((HasText) component).setText(text);
        component.setTitle(title);

        replaceIcon(getModel().getIconStyle());
    }
}  // end class DisplayUI
