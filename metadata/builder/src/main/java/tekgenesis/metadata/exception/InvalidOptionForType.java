
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

import static java.lang.String.format;

/**
 * Exception to throw when a Type does not supports a given field option.
 */
public class InvalidOptionForType extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Constructor used when the Type does not supports a specific field option. */
    public InvalidOptionForType(WidgetType widget, Type type, FieldOption fieldOption) {
        super(format("Widget with type '%s' does not allow field option '%s'", type.toString(), fieldOption.name()), widget.getId());
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 8778884939690987921L;
}
