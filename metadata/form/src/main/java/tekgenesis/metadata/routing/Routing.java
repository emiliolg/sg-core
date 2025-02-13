
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.routing;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Strings;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.core.Strings.split;
import static tekgenesis.metadata.routing.Route.defined;
import static tekgenesis.metadata.routing.Route.nowhere;

/**
 * Manages /path/to -> source routing.
 */
public class Routing<T> {

    //~ Instance Fields ..............................................................................................................................

    private final Map<String, T> routes;
    private final Set<String>    successions;

    //~ Constructors .................................................................................................................................

    Routing() {
        routes      = new TreeMap<>();
        successions = new TreeSet<>();
    }

    //~ Methods ......................................................................................................................................

    /** Check route colliding paths. */
    public boolean checkCollisions(@NotNull final String route) {
        final String      normalized = normalize(route);
        final Set<String> parts      = routes.keySet();
        return !successions.contains(normalized) && !splitSuccessions(normalized).exists(parts::contains);
    }

    /** Route given path to optional target. */
    public Route<T> route(@NotNull final String path) {
        if (isValid(path)) {
            final String normalized = normalize(path);

            /* Attempt with full path. => a/b/c */
            final T routing = routes.get(normalized);
            if (routing != null) return defined(routing, normalized);
            else {
                /* Attempt each succession. => a/, /a/b, a/b/c */
                for (final String succession : splitSuccessions(normalized)) {
                    final T result = routes.get(succession);
                    if (result != null) return defined(result, succession, normalized.substring(succession.length() + 1));
                }
            }
        }
        return nowhere();
    }  // end method route

    @SuppressWarnings("UnusedReturnValue")
    boolean addRoute(@NotNull final String route, @NotNull final T source) {
        if (isValid(route) && checkCollisions(route)) {
            final String normalized = normalize(route);
            routes.put(normalized, source);
            splitSuccessions(normalized).into(successions);
            return true;
        }
        return false;
    }

    //~ Methods ......................................................................................................................................

    /** Normalize path: ["/some" -> "/some"], ["some" -> "/some"], ["some/" -> "/some"] */
    @NotNull public static String normalize(@NotNull final String path) {
        String result = path;
        if (!result.startsWith(ROUTING_PATH_SLASH)) result = ROUTING_PATH_SLASH + result;
        if (result.endsWith(ROUTING_PATH_SLASH) && result.length() > 1) result = result.substring(0, result.length() - 1);
        return result;
    }

    /** Split route into sections: "/a/b/c" -> ["a", "b", "c"]. */
    @SuppressWarnings("WeakerAccess")
    public static Seq<String> splitSections(@NotNull final String route) {
        final String normalized = normalize(route);
        return normalized.length() > 1 ? split(normalized.substring(1), ROUTING_PATH_CHAR) : Colls.emptyList();
    }

    /** Split route into successions: "/a/b/c" -> ["/a", "/a/b", "/a/b/c"]. */
    @SuppressWarnings("WeakerAccess")
    public static Seq<String> splitSuccessions(@NotNull final String route) {
        return splitSections(route).map(new Function<String, String>() {
                private String last = "";

                @Override public String apply(String part) {
                    final String next = last + ROUTING_PATH_CHAR + part;
                    last = next;
                    return next;
                }
            });
    }

    /** Return true is path is a valid one. */
    public static boolean isValid(@NotNull final String path) {
        if (isNotEmpty(path)) {
            final Seq<String> sections = splitSections(path);
            return !sections.isEmpty() && sections.forAll(Strings::isNotBlank);
        }
        return false;
    }

    //~ Static Fields ................................................................................................................................

    static final char           ROUTING_PATH_CHAR  = '/';
    private static final String ROUTING_PATH_SLASH = String.valueOf(ROUTING_PATH_CHAR);
}  // end class Routing
