
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.type.Type;

/**
 * Exception to throw when a Widget does not supports a given feature.
 */
public class InvalidTypeForWidgetException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Constructor used when the Widget does not have a type. */
    public InvalidTypeForWidgetException(WidgetType widget) {
        super(String.format("Widget '%s' does not have type", widget.getId()), widget.getId());
    }

    /** Constructor used when I only have the type. */
    public InvalidTypeForWidgetException(Type type) {
        super(String.format("Type '%s' does not have a default widget", type), type.toString());
    }

    /** Constructor to signal an invalid type for a widget. */
    public InvalidTypeForWidgetException(WidgetType widget, Type t) {
        super(String.format("Widget '%s' does not accept type '%s'", widget.getId(), t.toString()), widget.getId());
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5741411929254380781L;
}
