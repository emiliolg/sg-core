
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Strings;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.metadata.handler.Part.createStatic;
import static tekgenesis.metadata.handler.Part.createWildcard;

/**
 * Utility class to deal with route parts.
 */
public class Parts {

    //~ Constructors .................................................................................................................................

    private Parts() {}

    //~ Methods ......................................................................................................................................

    /** Retrieve parts for given path. */
    public static Seq<Part> retrieveParts(@NotNull final String path) {
        final List<Part>  parts = new ArrayList<>();
        final Set<String> names = new HashSet<>();

        for (final String section : Routes.split(path)) {
            final Part part;
            if (section.startsWith("$")) {
                if (section.endsWith("*")) part = createWildcard(section.substring(1, section.length() - 1));
                else part = retrieveDynamicPart(section.substring(1));
            }
            else part = createStatic(section);

            if ((part.isDynamic() || part.isWildcard()) && !names.add(part.getName())) throw duplicateDynamicPartName(part.getName());

            parts.add(part);
        }

        return seq(parts);
    }

    private static IllegalArgumentException duplicateDynamicPartName(String name) {
        return new IllegalArgumentException(String.format("Dynamic part '%s' already defined on route", name));
    }

    private static IllegalArgumentException invalidTypeForDynamicPart(String name, String type) {
        return new IllegalArgumentException(String.format("Invalid type '%s' specified on dynamic part '%s'", type, name));
    }

    private static Type retrieveBasicType(String t) {
        final int parenthesis = t.indexOf('(');
        Type      result      = Types.fromString(parenthesis == -1 ? t : t.substring(0, parenthesis));
        if (parenthesis != -1) {
            final String params = t.substring(parenthesis + 1, t.length() - 1);
            result = result.applyParameters(Strings.split(params, ','));
        }
        return result;
    }

    private static Part retrieveDynamicPart(@NotNull final String part) {
        final int    typeSpecified = part.indexOf(":");
        final Type   t;
        final String name;
        if (typeSpecified != -1) {
            name = part.substring(0, typeSpecified);
            t    = retrieveBasicType(part.substring(typeSpecified + 1));
            if (t.isNull() || t.isResource()) throw invalidTypeForDynamicPart(name, part.substring(typeSpecified + 1));
        }
        else {
            name = part;
            t    = Types.stringType();
        }
        return Part.createDynamic(name, t);
    }
}  // end class Parts
