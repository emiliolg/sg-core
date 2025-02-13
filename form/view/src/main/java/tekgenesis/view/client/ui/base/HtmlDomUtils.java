
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.base;

import java.util.Map;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HasNativeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;

import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.util.JavaReservedWords.CLASS;
import static tekgenesis.view.client.ui.FormUI.FORM_CLASS_NAME;

/**
 * Html dom utility methods.
 */
public class HtmlDomUtils {

    //~ Constructors .................................................................................................................................

    private HtmlDomUtils() {}

    //~ Methods ......................................................................................................................................

    /** Clears the style name. */
    public static void clearStyleName(final UIObject uiObject) {
        if (uiObject != null) uiObject.getElement().removeAttribute(CLASS);
    }

    /** Clears the style name. */
    public static void clearStyleName(final Element element) {
        if (element != null) element.removeAttribute(CLASS);
    }

    /** Clicks Element if clickable. */
    public static void clickElement(Element elem) {
        final ButtonElement element = elem.cast();
        element.click();
    }

    /** Returns an optional <T> if any associated key is found on element ancestors. */
    @NotNull public static <T> Option<T> findParentBox(final Map<Element, T> boxes, final Element element) {
        for (Element e = element; e != null; e = e.getParentElement()) {
            if (boxes.containsKey(e)) return some(boxes.get(e));
        }
        return Option.empty();
    }

    /** Returns an element matching given searchClassName or returns null. */
    @Nullable public static Element findParentElement(final String searchClassName, Element element, boolean containToForm) {
        for (Element e = element; e != null; e = e.getParentElement()) {
            final String className = e.getClassName();
            if (className != null) {
                if (className.contains(searchClassName)) return e;
                if (className.contains(FORM_CLASS_NAME) && containToForm) return null;
            }
        }
        return null;
    }
    /** Returns an element matching given parentTag or returns null. */
    @Nullable public static Element findParentElementByTag(final String tag, Element element) {
        for (Element e = element.getParentElement(); e != null; e = e.getParentElement()) {
            final String tagName = e.getTagName();
            if (tagName.contains(tag)) return e;
            if ("BODY".equals(tagName)) return null;
        }
        return null;
    }

    /** Native html5 query selectors. */
    public static Element hrefQuerySelector(final Element parent, final String href) {
        return querySelector(parent, "a[href='" + href + "']");
    }

    /** Native html5 query selectors. */
    public static native Element querySelector(final Element parent, final String query)  /*-{ return parent.querySelector(query); }-*/;

    /** Native html5 query selectors. */
    public static native JsArray<Element> querySelectorAll(final Element parent, final String query)  /*-{ return parent.querySelectorAll(query); }-*/;

    /** Return the target element of the event. */
    public static Element targetElement(final HasNativeEvent event) {
        return targetElement(Event.as(event.getNativeEvent()));
    }

    /** Return the target element of the event. */
    public static Element targetElement(final Event event) {
        return DOM.eventGetTarget(event);
    }

    /** Return the currently active/focused element. */
    public static native Element getActiveElement()  /*-{ return $wnd.document.activeElement; }-*/;

    /** Return if the given element is the focused element. */
    public static boolean isFocused(final Element e) {
        final Element activeElement = getActiveElement();
        return activeElement != null && activeElement.equals(e);
    }

    /** Return if a widget is visible. If a parent element is hidden this will work to. */
    public static boolean isVisible(final Widget e) {
        return e.getElement().getOffsetHeight() != 0 || e.getElement().getOffsetWidth() != 0;
    }

    /** Sets Element max-width. */
    public static native void setMaxWidth(Element e, int value)  /*-{ e.setAttribute("style","max-width:" + value + "em") }-*/;

    /** Sets Placeholder property to the DOM element. */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static void setPlaceholder(final UIObject uiObject, final String placeholder) {
        if (uiObject != null) uiObject.getElement().setPropertyString("placeholder", placeholder);
    }

    //~ Static Fields ................................................................................................................................

    // KeyPress codes
    public static final int KEY_F_PRESS  = 102;
    public static final int KEY_F2_PRESS = 102;
    public static final int KEY_SLASH    = 47;
    public static final int KEY_DASH     = 45;  // ????  - - - - - - - -
    public static final int KEY_QUESTION = 63;
}                                               // end class HtmlDomUtils
