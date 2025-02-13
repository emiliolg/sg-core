
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasWidgets;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.widget.Widget;

/**
 * Utility class to deal with Widgets.
 */
class Widgets {

    //~ Constructors .................................................................................................................................

    private Widgets() {}

    //~ Methods ......................................................................................................................................

    static void checkArrayAccess(Widget model) {
        checkArrayAccess(model.isMultiple(), model.getName());
    }

    static void checkArrayAccess(boolean accepts, String name) {
        if (!accepts) throw new IllegalStateException("Array access to scalar instance widget '" + name + "'.");
    }

    static void checkScalarAccess(Widget model) {
        checkScalarAccess(!model.isMultiple(), model.getName());
    }

    static void checkScalarAccess(boolean accepts, String name) {
        if (!accepts) throw new IllegalStateException("Scalar access to multiple instance widget '" + name + "'.");
    }

    /** Focus first or remove focus from all. */
    static void setFocus(@NotNull final HasWidgets parent, boolean focus) {
        for (final com.google.gwt.user.client.ui.Widget child : parent) {
            if (child instanceof Focusable) {
                ((Focusable) child).setFocus(focus);
                if (focus) break;
            }
        }
    }
}
