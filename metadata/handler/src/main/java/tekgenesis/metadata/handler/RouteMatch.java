
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.handler;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;

import static tekgenesis.common.collections.Colls.mkString;

/**
 * Represents a concrete route match for a given path.
 */
public class RouteMatch {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final List<Object> dynamicArguments;

    @NotNull private final Route route;

    //~ Constructors .................................................................................................................................

    RouteMatch(@NotNull Route route, @NotNull List<String> sections) {
        this.route       = route;
        dynamicArguments = parseDynamicArguments(route.getParts(), sections);
    }

    //~ Methods ......................................................................................................................................

    /** Get method class name. */
    @NotNull public String getClassName() {
        return route.getMethodClass();
    }

    /** Get matching dynamicArguments. */
    @NotNull public List<Object> getDynamicArguments() {
        return dynamicArguments;
    }

    /** Get method name. */
    @NotNull public String getMethodName() {
        return route.getMethodName();
    }

    /** Get matching route. */
    @NotNull public Route getRoute() {
        return route;
    }

    /** Parse dynamic parts. */
    private List<Object> parseDynamicArguments(@NotNull final Seq<Part> parts, @NotNull final List<String> sections) {
        final List<Object> args    = new ArrayList<>();
        int                section = 0;
        for (final Part part : parts) {
            if (part.isDynamic()) args.add(part.getType().valueOf(sections.get(section)));
            else if (part.isWildcard()) {
                final String spanning = mkString(sections.subList(section, sections.size()), Routes.ROUTING_PATH_SLASH);
                args.add(spanning);
                break;
            }
            section++;
        }
        return args;
    }  // end method parseDynamicArguments
}
