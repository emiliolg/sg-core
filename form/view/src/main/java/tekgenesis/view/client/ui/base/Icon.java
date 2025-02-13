
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.base;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.IconType;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.metadata.form.model.FormConstants.DEFAULT_LENGTH;
import static tekgenesis.view.client.ui.base.HtmlDomUtils.setMaxWidth;

/**
 * Icon.
 */
public class Icon extends Widget {

    //~ Instance Fields ..............................................................................................................................

    private IconType type = null;

    //~ Constructors .................................................................................................................................

    /** Creates a widget but doesn't set an icon yet. */
    public Icon() {
        final Element i = DOM.createElement("i");
        setElement(i);
        addStyleName(FA_ICON);
    }

    /** Creates an Icon with its type. */
    public Icon(IconType type) {
        this(type.getClassName());
        this.type = type;
    }

    /** Creates an Icon given the Element. */
    public Icon(Element icon) {
        setElement(icon);
    }

    /** Creates an Icon with a style. */
    private Icon(String iconStyle) {
        this();
        addStyleName(FA_PREFIX + iconStyle);
    }

    //~ Methods ......................................................................................................................................

    /** Returns Icon Type. */
    public IconType getIconType() {
        return type;
    }

    /** Sets Icon type. */
    public void setType(IconType type) {
        if (this.type != null) removeStyleName(FA_PREFIX + this.type.getClassName());
        this.type = type;
        addStyleName(FA_PREFIX + type.getClassName());
    }

    //~ Methods ......................................................................................................................................

    /**
     * Inserts the icon in front of the passed textBox. It returns the icon just added on the
     * widget. It returns none if the icon wasn't created
     */
    public static Option<Icon> inTextBox(Widget textBox, String iconType, boolean expand) {
        if (isNotEmpty(iconType)) {
            final Widget parent = textBox.getParent();
            if (parent.getStyleName().contains(FormConstants.INPUT_GROUP))
            // textBox has icon, icon should be replaced in icon-containing span
            return replaceInWidget(((HasWidgets) parent).iterator().next(), iconType);
            else {
                final Icon      icon    = new Icon(iconType);
                final FlowPanel prepend = HtmlWidgetFactory.div();
                prepend.addStyleName(FormConstants.INPUT_GROUP);
                if (!expand) setMaxWidth(prepend.getElement(), DEFAULT_LENGTH);
                final InlineHTML span = HtmlWidgetFactory.span();
                span.addStyleName(FormConstants.INPUT_GROUP_ADDON);
                span.getElement().appendChild(icon.getElement());
                textBox.removeFromParent();
                prepend.add(span);
                prepend.add(textBox);
                ((HasWidgets) parent).add(prepend);
                return Option.some(icon);
            }
        }
        return Option.empty();
    }  // end method inTextBox

    /**
     * Inserts the icon in the passed widget and returns the icon just created. It returns none if
     * the icon wasn't created
     */
    public static Option<Icon> inWidget(Widget widget, String iconType) {
        return inWidget(widget.getElement(), iconType);
    }

    /**
     * Inserts the icon in the passed widget and returns the icon just created. It returns none if
     * the icon wasn't created
     */
    public static Option<Icon> inWidget(Widget widget, IconType iconType) {
        return inWidget(widget, iconType.getClassName());
    }

    /**
     * Inserts the icon in the passed element and returns the icon just created. It returns none if
     * the icon wasn't created
     */
    public static Option<Icon> inWidget(Element element, String iconType) {
        if (isNotEmpty(iconType)) {
            final Icon icon = new Icon(iconType);
            element.insertFirst(icon.getElement());
            return Option.some(icon);
        }
        return Option.empty();
    }

    /**
     * Replaces the icon in the passed widget with the given icon and returns the icon just created.
     * It returns none if the icon wasn't created
     */
    public static Option<Icon> replaceInElement(Element elem, String iconType) {
        if (isNotEmpty(iconType)) {
            // remove old if present
            final Node firstChild = elem.getFirstChild();
            if (firstChild != null && "I".equals(firstChild.getNodeName())) firstChild.removeFromParent();

            // add new
            return inWidget(elem, iconType);
        }
        return Option.empty();
    }

    /**
     * Replaces the icon in the passed widget with the given icon and returns the icon just created.
     * It returns none if the icon wasn't created
     */
    public static Option<Icon> replaceInWidget(Widget widget, String iconType) {
        if (isNotEmpty(iconType)) return replaceInElement(widget.getElement(), iconType);
        return Option.empty();
    }

    //~ Static Fields ................................................................................................................................

    private static final String FA_ICON   = "fa";
    private static final String FA_PREFIX = "fa-";
}  // end class Icon
