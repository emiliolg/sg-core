
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;

/**
 * Form handler for custom handlers.
 */
public interface Handler<T extends FormInstance<?>> {

    //~ Methods ......................................................................................................................................

    /**
     * Return handled instance exporter. Form instance is initialized with given optional
     * parameters.
     */
    FormExporter exporter(@NotNull final Option<String> parameters);

    /** Return handled instance exporter. */
    FormImporter importer();

    /** Return handled instance. */
    T instance();

    /** Get action message. */
    String getMessage(@NotNull final Action action);

    /** Get route optional key. */
    Option<String> getRouteKey();

    /** Get route normalized path. */
    String getRoutePath();
}
