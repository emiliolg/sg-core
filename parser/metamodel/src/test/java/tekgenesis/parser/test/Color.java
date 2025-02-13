
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.parser.test;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.enumeration.Enumerations;

/**
 * An Enum used for test.
 */
public enum Color implements Enumeration<Color, String> {

    //~ Enum constants ...............................................................................................................................

    RED, GREEN, BLUE;

    //~ Methods ......................................................................................................................................

    @Override public String imagePath() {
        return "";
    }

    @Override public int index() {
        return ordinal();
    }

    @Override public String key() {
        return name();
    }

    @NotNull @Override public String label() {
        return Strings.toWords(name());
    }

    //~ Methods ......................................................................................................................................

    /** Returns the Map: key -> value. */
    @NotNull public static Map<String, Color> map() {
        return COLOR_MAP;
    }

    //~ Static Fields ................................................................................................................................

    @NotNull private static final Map<String, Color> COLOR_MAP = Enumerations.buildMap(values());
}
