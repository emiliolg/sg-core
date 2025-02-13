
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

import static java.lang.String.format;

/**
 * Exception to throw when a search_box is inside a form that is not searchable.
 */
public class EntityNotSearchableException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a EntityNotSearchableException. */
    public EntityNotSearchableException(WidgetType wt, String widget) {
        super(format("Widget %s '%s' on a non searchable entity binding", wt.toString(), widget), widget);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 8209184573227643661L;
}
