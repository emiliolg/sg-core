
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.handler;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Strings;

/**
 * Handler routes utility class.
 */
public class Routes {

    //~ Constructors .................................................................................................................................

    private Routes() {}

    //~ Methods ......................................................................................................................................

    /** Join route parts into string path. */
    @NotNull public static String join(@NotNull final Seq<Part> parts) {
        return normalize(parts.mkString(ROUTING_PATH_SLASH));
    }

    /** Normalize path: ["/some" -> "/some"], ["some" -> "/some"], ["some/" -> "/some"] */
    @NotNull public static String normalize(@NotNull final String path) {
        String result = path;
        if (!result.startsWith(ROUTING_PATH_SLASH)) result = ROUTING_PATH_SLASH + result;
        if (result.endsWith(ROUTING_PATH_SLASH) && result.length() > 1) result = result.substring(0, result.length() - 1);
        return result;
    }

    /** Normalize and split given path into string parts. */
    @NotNull public static Seq<String> split(@NotNull final String path) {
        final String normalized = normalize(path);
        return Strings.split(normalized, ROUTING_PATH_CHAR).drop(1);
    }

    //~ Static Fields ................................................................................................................................

    static final char   ROUTING_PATH_CHAR  = '/';
    static final String ROUTING_PATH_SLASH = String.valueOf(ROUTING_PATH_CHAR);
}
