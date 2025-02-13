
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

import tekgenesis.form.ExternalNavigate;

import static tekgenesis.external.NavigationFactory.register;

/**
 * External form instance.
 */
public class ExternalInstance<T extends ExternalInstance<T>> {

    //~ Instance Fields ..............................................................................................................................

    private final NavigationAction<T> action;

    //~ Constructors .................................................................................................................................

    protected ExternalInstance(@NotNull final String project, @NotNull final Class<T> target) {
        action = new NavigationAction<>(project, target);
    }

    //~ Methods ......................................................................................................................................

    protected ExternalNavigate<T> navigation() {
        return action().navigate();
    }

    protected ExternalNavigate<T> navigation(@NotNull final String key) {
        return action().navigate(key);
    }

    private NavigationAction<T> action() {
        return action.isRegistered() ? action : register(action);
    }
}
