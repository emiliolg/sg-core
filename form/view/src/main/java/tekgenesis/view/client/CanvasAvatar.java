
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.Style;

import org.jetbrains.annotations.NotNull;

import static com.google.gwt.dom.client.Style.Unit.PX;

/**
 * Avatar Canvas creator.
 */
@SuppressWarnings({ "NonJREEmulationClassesInClientCode", "DuplicateStringLiteralInspection", "MagicNumber" })
public class CanvasAvatar {

    //~ Instance Fields ..............................................................................................................................

    private final String name;

    //~ Constructors .................................................................................................................................

    private CanvasAvatar(@NotNull final String name) {
        this.name = name;
    }

    //~ Methods ......................................................................................................................................

    /** Builds avatar at given canvas. */
    public void buildAt(@NotNull final Canvas canvas) {
        final String[] nameSplit = name.split(" ");

        final String initials = (nameSplit.length == 1 ? "" + nameSplit[0].charAt(0) : "" + nameSplit[0].charAt(0) + nameSplit[1].charAt(0))
                                .toUpperCase();

        final int charIndex = ((int) initials.charAt(0)) - KEY_A;

        final int cWidth = 30;
        canvas.getElement().setAttribute("width", "" + cWidth);
        final int cHeight = 30;
        canvas.getElement().setAttribute("height", "" + cHeight);

        final Context2d context = canvas.getContext2d();
        final int       ratio   = devicePixelRatio();
        if (ratio != -1) {
            canvas.getElement().setAttribute("width", (cWidth * ratio) + "");
            canvas.getElement().setAttribute("height", (cHeight * ratio) + "");

            canvas.getElement().getStyle().setWidth(cWidth, PX);
            canvas.getElement().getStyle().setHeight(cHeight, PX);

            context.scale(ratio, ratio);
        }

        final int colourIndex = charIndex % COLORS.length;
        context.setFillStyle(COLORS[colourIndex]);
        context.fillRect(0, 0, cWidth * ratio, cHeight * ratio);
        context.setFont("16px Arial");
        context.setTextAlign("center");
        context.setFillStyle("#FFF");
        context.fillText(initials, cWidth / 2, cHeight / 1.5);

        canvas.getElement().setId(ID);
        canvas.getElement().getStyle().setVerticalAlign(Style.VerticalAlign.MIDDLE);
    }

    //J-
    private static native int devicePixelRatio() /*-{ return $wnd.devicePixelRatio ? $wnd.devicePixelRatio : -1; }-*/;
    //J+

    //~ Methods ......................................................................................................................................

    /** Creates a canvas avatar for given name initials. */
    public static CanvasAvatar createFor(@NotNull final String name) {
        return new CanvasAvatar(name);
    }

    //~ Static Fields ................................................................................................................................

    private static final int   KEY_A = 65;
    public static final String ID    = "user-initials-avatar";

    private static final String[] COLORS = {
        "#1abc9c", "#2ecc71", "#3498db", "#9b59b6", "#34495e",
        "#16a085", "#27ae60", "#2980b9", "#8e44ad", "#2c3e50", "#f1c40f", "#e67e22", "#e74c3c",
        "#95a5a6", "#f39c12", "#d35400", "#c0392b", "#bdc3c7", "#7f8c8d"
    };
}  // end class CanvasAvatar
