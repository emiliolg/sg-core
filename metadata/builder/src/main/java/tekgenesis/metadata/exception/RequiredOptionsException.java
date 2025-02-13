
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

import tekgenesis.common.collections.Seq;
import tekgenesis.field.FieldOption;
import tekgenesis.metadata.form.widget.WidgetType;

import static java.lang.String.format;

/**
 * Exception to throw when a Widget requires a certain option/feature.
 */
public class RequiredOptionsException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a new exception. */
    public RequiredOptionsException(WidgetType widget, FieldOption incompatibleOption, Seq<FieldOption> requiredOptions) {
        super(  //
            format("'%s' must be followed after '%s'", incompatibleOption.getId(), requiredOptions.map(FieldOption::getId).mkString(" or ")),
            widget.getId());
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -38886022269882294L;
}
