
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

import tekgenesis.field.FieldOption;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.type.Type;

/**
 * Exception to throw when a Widget or Attribute does not supports a given feature.
 */
public class IncompatibleOptionsException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a new exception. */
    public IncompatibleOptionsException(FieldOption incompatibleOption, WidgetType widget, Type finalType) {
        super(
            String.format("Feature '%s' is duplicated, or redundant in widget '%s(%s)'",
                incompatibleOption.getId(),
                widget.getId(),
                finalType.toString()),
            widget.getId());
    }

    /** Creates a new exception. */
    public IncompatibleOptionsException(FieldOption incompatibleOption, FieldOption option, WidgetType widget) {
        super(String.format("Feature '%s' is incompatible with '%s'", incompatibleOption.getId(), option.getId()), widget.getId());
    }

    /** Creates a new exception. */
    public IncompatibleOptionsException(FieldOption incompatibleOption, Type type, String attribute) {
        super(String.format("Feature '%s' is incompatible with type '%s'", incompatibleOption.getId(), type.toString()), attribute);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -38886022269882294L;
}
