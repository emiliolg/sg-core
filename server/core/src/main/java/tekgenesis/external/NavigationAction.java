
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.external;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.form.ExternalNavigate;

import static tekgenesis.common.Predefined.ensureNotNull;

/**
 * Navigation action to SuiGeneris project external model.
 */
class NavigationAction<T extends ExternalInstance<T>> {

    //~ Instance Fields ..............................................................................................................................

    @Nullable NavigationFactory     factory = null;
    @NotNull private final Class<T> form;

    @NotNull private final String project;

    //~ Constructors .................................................................................................................................

    NavigationAction(@NotNull final String project, @NotNull final Class<T> form) {
        this.project = project;
        this.form    = form;
    }

    //~ Methods ......................................................................................................................................

    ExternalNavigate<T> navigate() {
        return factory().external(project, form);
    }

    ExternalNavigate<T> navigate(@NotNull final String key) {
        return factory().external(project, form, key);
    }

    boolean isRegistered() {
        return factory != null;
    }

    @NotNull private NavigationFactory factory() {
        return ensureNotNull(factory);
    }
}
