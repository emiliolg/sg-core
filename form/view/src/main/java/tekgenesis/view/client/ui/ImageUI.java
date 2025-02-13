
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
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Resource;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.type.ArrayType;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;
import tekgenesis.view.shared.utils.ResourceUtils;

import static tekgenesis.view.client.ui.Widgets.checkArrayAccess;
import static tekgenesis.view.client.ui.Widgets.checkScalarAccess;
import static tekgenesis.view.shared.utils.ResourceUtils.getSmallestThumbUri;

/**
 * Image Field Widget.
 */
@SuppressWarnings("ClassTooDeepInInheritanceTree")
public class ImageUI extends FieldWidgetUI implements HasScalarValueUI, HasArrayValueUI, MouseMoveHandler, MouseOutHandler, HasClickUI {

    //~ Instance Fields ..............................................................................................................................

    private final FlowPanel images;

    private final Image  img;
    private final String thumbVariant;

    private WidgetMessages tooltipMessages = null;

    //~ Constructors .................................................................................................................................

    /** PasswordTextFieldUI constructor. */
    public ImageUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        thumbVariant = model.getThumbVariant();

        final com.google.gwt.user.client.ui.Widget result;
        if (model.isMultiple()) {
            images = new FlowPanel();
            images.setStyleName(Constants.MULTIPLE);
            img    = null;
            result = images;
        }
        else {
            images = null;
            img    = HtmlWidgetFactory.img();
            result = img;
        }

        initWidget(result);

        // When having a tooltip expression, initialize a tooltip object and add mouse handlers
        if (!model.getTooltip().isEmpty()) {
            tooltipMessages = WidgetMessages.create((FlowPanel) div, true);
            result.addDomHandler(this, MouseMoveEvent.getType());
            result.addDomHandler(this, MouseOutEvent.getType());
        }

        if (getModel().hasOnClickMethod()) {
            if (img != null) img.addStyleName(CLICKABLE_STYLE);
            else images.addStyleName(CLICKABLE_STYLE);
        }
    }

    //~ Methods ......................................................................................................................................

    @Override public void addChangeHandler(final ValueChangeHandler<Object> handler) {
        // value doesn't change internally, it's a read-only/display component
    }

    /**
     * Register the clickHandler to the ImageUI.
     *
     * @param  handler  @see ClickHandler
     */
    public void addClickHandler(final ClickHandler handler) {
        final ClickHandler clickHandler = event -> handler.onClick(null);

        if (img != null) img.addClickHandler(clickHandler);
        else images.addDomHandler(clickHandler, ClickEvent.getType());
    }

    @Override public void onMouseMove(MouseMoveEvent event) {
        // Move the tooltip along with the mouse position
        tooltipMessages.hide();
        tooltipMessages.show(event.getClientX(), event.getClientY());
    }

    @Override public void onMouseOut(MouseOutEvent event) {
        tooltipMessages.hide();
    }

    /**
     * Override the default tooltip behavior so it is displayed when hovering the image and avoid
     * creating an extra icon.
     *
     * @param  tip  the string message to be displayed
     */
    @Override public void setTooltip(String tip) {
        if (tooltipMessages != null) tooltipMessages.tooltipmsg(tip);
    }

    @Override public Object getValue() {
        checkScalarAccess(getModel());
        return img.getUrl();
    }

    @Override public void setValue(@Nullable Object value) {
        checkScalarAccess(getModel());
        if (value != null) {
            final Resource resource = ResourceUtils.getResourceValue(getModel().getType(), value);
            setImgValue(img, resource);
        }
        else img.setUrl("");
    }

    @Override public void setValue(@Nullable Object modelValue, boolean e) {
        setValue(modelValue);
    }

    @NotNull @Override public Iterable<?> getValues() {
        checkArrayAccess(getModel());
        return Colls.map(images, ImageUI::imageUrl);
    }

    @Override public void setValues(@NotNull Iterable<Object> values) {
        checkArrayAccess(getModel());
        images.clear();
        final List<Resource> resourceValues = ResourceUtils.getResourceValues(((ArrayType) getModel().getType()).getElementType(), values);

        for (final Resource r : resourceValues) {
            final Image image = HtmlWidgetFactory.img();
            setImgValue(image, r);
            images.add(image);
        }
    }

    @Override public void setValues(@NotNull Iterable<Object> values, boolean e) {
        setValues(values);
    }

    // ** This widget doesn't support icon */
    @NotNull @Override Option<Element> createIcon() {
        return Option.empty();
    }

    private void setImgValue(Image target, Resource value) {
        target.setUrl(getSmallestThumbUri(value, thumbVariant).first());
    }

    //~ Methods ......................................................................................................................................

    private static String imageUrl(final com.google.gwt.user.client.ui.Widget value) {
        return ((Image) value).getUrl();
    }

    //~ Static Fields ................................................................................................................................

    private static final String CLICKABLE_STYLE = FormConstants.CLICKABLE;
}  // end class ImageUI
