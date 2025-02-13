
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.external;

import java.util.LinkedHashSet;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.app.properties.ExternalProjectProps;
import tekgenesis.common.env.Environment;
import tekgenesis.form.ActionsImpl;
import tekgenesis.form.ExternalNavigate;

import static java.lang.String.format;

import static tekgenesis.common.Predefined.isEmpty;

/**
 * Navigation factory.
 */
public class NavigationFactory {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final ActionsImpl actions;
    @NotNull private final Environment environment;

    //~ Constructors .................................................................................................................................

    /** Navigation factory constructor receiving actions implementation. */
    public NavigationFactory(@NotNull final ActionsImpl actions, @NotNull final Environment environment) {
        this.actions     = actions;
        this.environment = environment;
    }

    //~ Methods ......................................................................................................................................

    <T extends ExternalInstance<T>> ExternalNavigate<T> external(@NotNull final String project, @NotNull final Class<T> form) {
        final ExternalProjectProps props = environment.get(project, ExternalProjectProps.class);
        return actions.external(assertEndpoint(props, project), form.getCanonicalName(), null);
    }

    <T extends ExternalInstance<T>> ExternalNavigate<T> external(@NotNull final String project, @NotNull final Class<T> form,
                                                                 @NotNull final String key) {
        final ExternalProjectProps props = environment.get(project, ExternalProjectProps.class);
        return actions.external(assertEndpoint(props, project), form.getCanonicalName(), key);
    }

    /** Assert endpoint is defined. */
    @NotNull private String assertEndpoint(@NotNull final ExternalProjectProps props, String project) {
        if (isEmpty(props.endpoint))
            throw new IllegalStateException(
                format("Project '%s' does not defines endpoint property. Missing: '%s.external.endpoint=example.com'", project, project));
        return props.endpoint;
    }

    //~ Methods ......................................................................................................................................

    /** Sets the singleton. */
    public static void setFactory(@NotNull final NavigationFactory factory) {
        instance = factory;
        registered.forEach(NavigationFactory::register);  // Re-register previously registered
    }

    static <T extends ExternalInstance<T>> NavigationAction<T> register(@NotNull final NavigationAction<T> action) {
        action.factory = factory();
        registered.add(action);
        return action;
    }

    private static NavigationFactory factory() {
        if (instance == null) throw new IllegalStateException("Navigation factory not initialized");
        return instance;
    }

    //~ Static Fields ................................................................................................................................

    private static final Set<NavigationAction<?>> registered = new LinkedHashSet<>();

    @Nullable private static NavigationFactory instance = null;
}  // end class NavigationFactory
