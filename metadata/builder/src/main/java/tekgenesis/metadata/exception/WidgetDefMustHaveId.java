
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

/**
 * Exception thrown when there is a widget definition and it doesn't have an id defined.
 */
public class WidgetDefMustHaveId extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Constructor for WidgetDefMustHaveId exception. */
    public WidgetDefMustHaveId(String name) {
        super("Widget definition " + name + "must have an id.", name);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -6287645367477448009L;
}
