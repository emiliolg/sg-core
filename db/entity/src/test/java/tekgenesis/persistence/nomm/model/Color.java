
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.nomm.model;

import java.util.EnumSet;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.common.env.i18n.I18nBundle;

/**
 * A test class.
 */
public enum Color implements Enumeration<Color, Integer> {

    //~ Enum constants ...............................................................................................................................

    BLACK(0), RED(1), GREEN(2), BLUE(4);

    //~ Instance Fields ..............................................................................................................................

    private final int id;

    //~ Constructors .................................................................................................................................

    Color(final int id) {
        this.id = id;
    }

    //~ Methods ......................................................................................................................................

    /** Returns the field image path. */
    @NotNull public final String imagePath() {
        return BUNDLE.getString(name() + "." + Constants.IMAGE_EXT, "");
    }

    public boolean in(Color first, Color... rest) {
        return EnumSet.of(first, rest).contains(this);
    }

    @Override public int index() {
        return key();
    }

    /** Returns the enum id. */
    @NotNull public final Integer key() {
        return id;
    }

    /** Returns the field label in the current locale. */
    @NotNull public final String label() {
        return BUNDLE.getString(name(), name());
    }

    //~ Methods ......................................................................................................................................

    /** Get a Color from the id. */
    @NotNull public static Option<Color> fromId(int id) {
        return Option.ofNullable(COLOR_MAP.get(id));
    }

    /** Returns the Map: key -> value. */
    @NotNull public static Map<Integer, Color> map() {
        return COLOR_MAP;
    }

    //~ Static Fields ................................................................................................................................

    @NotNull private static final Map<Integer, Color> COLOR_MAP = Enumerations.buildMap(values());

    @NotNull private static final I18nBundle BUNDLE = I18nBundle.getBundle(Color.class);
}
