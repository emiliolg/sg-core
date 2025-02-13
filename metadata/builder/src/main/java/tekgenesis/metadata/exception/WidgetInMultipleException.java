
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
 * Exception thrown when an anchor is found inside a multiple widget.
 */
public class WidgetInMultipleException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a new exception. */
    public WidgetInMultipleException(String widgetTypeName, String multipleName, String widgetName) {
        super(widgetTypeName + " '" + widgetName + "' can't be a column inside '" + multipleName + "'", widgetName);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -2521793025164720396L;
}
