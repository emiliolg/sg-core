
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.Iterator;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Image;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.model.KeyMap;
import tekgenesis.view.client.formatter.InputHandler;
import tekgenesis.view.client.ui.base.DraggableList;
import tekgenesis.view.client.ui.base.HtmlList;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.core.Constants.ITEM_IMAGE_CLASS;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.Strings.coverText;
import static tekgenesis.metadata.form.model.FormConstants.ELLIPSIS;
import static tekgenesis.view.client.formatter.InputHandler.none;
import static tekgenesis.view.client.ui.HasLinkUI.FormLinkUtil.*;
import static tekgenesis.view.client.ui.Widgets.checkArrayAccess;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.li;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.ul;

/**
 * Text UI Widget.
 */
public class MultipleDisplayUI extends FieldWidgetUI implements HasArrayValueUI, HasInputHandlerUI, HasLinkUI {

    //~ Instance Fields ..............................................................................................................................

    @Nullable private String currentLinkPk = null;
    private InputHandler<?>  inputHandler;
    private int              length;

    private final ComplexPanel ul;
    private Iterable<?>        values      = Colls.emptyIterable();

    //~ Constructors .................................................................................................................................

    /** Creates Text UI Widget. */
    public MultipleDisplayUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        inputHandler = none();
        ul           = (model.isDraggable()) ? new DraggableList() : ul();
        initWidget(ul);
    }

    //~ Methods ......................................................................................................................................

    @Override public void addChangeHandler(ValueChangeHandler<Object> changeHandler) {  /* read-only */
    }

    @Override public void addClickHandler(ClickHandler handler) {
        ul.addDomHandler(handler, ClickEvent.getType());
    }

    @Override public void setInputHandler(InputHandler<?> inputHandler) {
        this.inputHandler = inputHandler;
    }

    /** Sets the maximum length of the field. */
    public void setLength(int length) {
        this.length = length;
    }

    @Override public void setLink(@Nullable String url) {
        for (final com.google.gwt.user.client.ui.Widget widget : ul)
            ((Anchor) widget).setHref(url);
    }

    @Nullable @Override public String getLinkPk() {
        return currentLinkPk;
    }

    @Override public void setLinkPk(@Nullable String pk) {
        currentLinkPk = pk;
    }

    /** Sets the text to display. */
    public void setOptionsText(final Iterable<?> options, KeyMap images) {
        ul.clear();

        final Widget  model   = getModel();
        final boolean hasLink = hasLink(model);

        final Iterator<?> valuesIterator = values.iterator();
        for (final Object option : options) {
            final InputHandler<Object> objectInputHandler = cast(inputHandler);
            final String               text               = objectInputHandler.format(option);

            final String        pk = valuesIterator.hasNext() ? notNull(valuesIterator.next(), "").toString() : "";
            final HtmlList.Item li = li();

            if (getModel().hasImage()) {
                final String image = images.get(pk);
                if (isNotEmpty(image)) li.add(img(image));
            }

            final HasText label;
            if (hasLink) {
                final Anchor link = link(model);
                if (hasAutoLinkPk(model)) {
                    updateLinkPk(link, model, pk);
                    link.addClickHandler(event -> currentLinkPk = pk);
                }
                li.add(link);
                label = link;
            }
            else label = li;

            final boolean mustCover = getModel().isEntity() && !getModel().isFullText();
            final String  result    = mustCover ? coverText(text) : text;

            if (result != null && result.length() > length) {
                label.setText(result.substring(0, length) + ELLIPSIS);
                li.setTitle(text);
            }
            else {
                label.setText(result);
                if (mustCover) li.setTitle(text);
            }

            ul.add(li);
        }
    }  // end method setOptionsText

    @NotNull @Override public Iterable<?> getValues() {
        checkArrayAccess(getModel());
        return values;
    }

    @Override public void setValues(@NotNull Iterable<Object> values) {
        final Widget model = getModel();
        checkArrayAccess(model);
        this.values = values;
    }

    @Override public void setValues(@NotNull Iterable<Object> v, boolean fireEvents) {
        setValues(v);
    }

    @Override void addStyleNames() {
        super.addStyleNames();
        addStyleName("multiple-text");
    }

    @NotNull @Override Option<Element> createIcon() {
        return some(ul.getElement());
    }

    private Image img(String image) {
        final Image img = HtmlWidgetFactory.img(image);
        img.addStyleName(ITEM_IMAGE_CLASS);
        return img;
    }
}  // end class MultipleDisplayUI
