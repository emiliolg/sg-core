
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.base;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.InlineHTML;

import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.IconType;

/**
 * Extended Button.
 */
@SuppressWarnings("GWTStyleCheck")
public class ExtButton extends Anchor implements HasClickHandlers {

    //~ Instance Fields ..............................................................................................................................

    private final Icon icon = new Icon();
    // private final InlineLabel label = new InlineLabel();

    //~ Constructors .................................................................................................................................

    /** Creates a Button. */
    public ExtButton() {
        super(true);
        // setElement(DOM.createElement("a"));
        setStyleName("btn btn-default");
        // add(label);
    }

    /** Creates a Button with the given caption. */
    public ExtButton(String caption) {
        this();
        setText(caption);
    }

    /** Creates a Button with the given icon. */
    public ExtButton(Icon icon) {
        this();
        setIcon(icon.getIconType());
    }

    /** Creates a Button with the given caption and icon. */
    public ExtButton(String caption, IconType icon) {
        this(caption);
        setIcon(icon);
    }

    //~ Methods ......................................................................................................................................

    public void toggle() {
        getElement().toggleClassName(FormConstants.ACTIVE_STYLE);
    }

    /** Sets Caret for button. */
    public void setCaret() {
        final InlineHTML caret = HtmlWidgetFactory.span();
        caret.setStyleName("caret");
        getElement().appendChild(caret.getElement());
    }

    /** Check if Button is enabled. */
    public boolean isEnabled() {
        return !getStyleName().contains(FormConstants.DISABLED_STYLE);
    }

    /** Toggles enabled status. */
    public void setEnabled(boolean enabled) {
        if (enabled) removeStyleName(FormConstants.DISABLED_STYLE);
        else addStyleName(FormConstants.DISABLED_STYLE);
    }

    /** Get Button href. */
    public String getHref() {
        return getElement().getAttribute("href");
    }

    /** Set Button href. */
    public void setHref(String href) {
        getElement().setAttribute("href", href);
    }

    /** Gets the button's icon. */
    public Icon getIcon() {
        return icon;
    }

    /** Set an icon for Button. */
    public void setIcon(IconType type) {
        // remove prev icon
        icon.getElement().removeFromParent();
        icon.setType(type);
        setIcon(icon);
    }

    /** Sets Button text. */
    public void setText(String text) {
        super.setText(" " + text + " ");
        setIcon(icon);
    }

    private void setIcon(Icon icon) {
        getElement().insertFirst(icon.getElement());
    }
}  // end class ExtButton
