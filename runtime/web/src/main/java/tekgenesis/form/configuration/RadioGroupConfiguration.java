
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.configuration;

import java.util.List;

import org.jetbrains.annotations.NotNull;

/**
 * Configuration for radio group widgets.
 */
public interface RadioGroupConfiguration extends WidgetConfiguration {

    //~ Methods ......................................................................................................................................

    /** Style classes list. */
    @NotNull RadioGroupConfiguration styleClasses(@NotNull List<String> classes);
}
