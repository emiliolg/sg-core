
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

import tekgenesis.check.CheckType;
import tekgenesis.field.FieldOption;
import tekgenesis.metadata.form.widget.WidgetType;

/**
 * Exception to throw when a Widget does not supports a given feature.
 */
public class UnsupportedOptionException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a new exception. */
    public UnsupportedOptionException(FieldOption option) {
        super(String.format("Feature '%s' is not supported", option.getId()), option.getId());
    }

    /** Creates a new exception. */
    public UnsupportedOptionException(FieldOption option, WidgetType widget) {
        super(String.format("Feature '%s' is not supported by widget '%s'", option.getId(), widget), widget.getId());
    }

    /** Creates a new exception. */
    public UnsupportedOptionException(CheckType checkType, String widgetName) {
        super(String.format("message(%s) not supported when form is not bound to entity", checkType.toString().toLowerCase()), widgetName);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5741411929254380781L;
}
