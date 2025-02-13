
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

import static java.lang.String.format;

/**
 * Exception to be thrown when a type default widget cannot be resolved.
 */
public class MissingDefaultWidgetForTypeException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Constructor for exception. */
    public MissingDefaultWidgetForTypeException(String type, String widget) {
        super(format("Missing default widget for type '%s' on widget definition '%s'", type, widget), widget);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1139016270504921995L;
}
