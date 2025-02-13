
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.base;

import com.google.gwt.user.client.ui.HTML;

import org.jetbrains.annotations.NonNls;

import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.view.client.ui.WidgetMessages;

/**
 * Tooltip.
 */
public class Tooltip extends ClickablePanel {

    //~ Instance Fields ..............................................................................................................................

    private final Icon icon;

    private final HTML           msg             = new HTML();
    private final WidgetMessages tooltipMessages;

    //~ Constructors .................................................................................................................................

    /** Creates an empty Tooltip. */
    public Tooltip() {
        icon = new Icon(IconType.INFO);
        add(icon);
        addStyleName("tooltip-toggle");
        tooltipMessages = WidgetMessages.create(this, true);
        addMouseOverHandler(event -> tooltipMessages.show());
        addMouseOutHandler(event -> tooltipMessages.hide());
    }

    //~ Methods ......................................................................................................................................

    /** Changes icon type. */
    public void setIcon(IconType t) {
        icon.setType(t);
    }

    /** Returns tooltip message. */
    public String getMsg() {
        return msg.getHTML();
    }

    /** Sets tooltip message. */
    public void setMsg(String msgString) {
        tooltipMessages.tooltipmsg(msgString);
    }

    //~ Static Fields ................................................................................................................................

    @NonNls public static final String TOOLTIP_ARROW = "tooltip-arrow";
}  // end class Tooltip
