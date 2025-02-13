
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.nomm.model;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.common.env.i18n.I18nBundle;

public enum Country implements Enumeration<Country, String> {

    //~ Enum constants ...............................................................................................................................

    ARGENTINA("Argentina"), GERMANY("Germany"), ITALY("Italy"), JAPAN("Japan"), SPAIN("Spain"), UNITED_STATES("United States");

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final String label;

    //~ Constructors .................................................................................................................................

    Country(@NotNull String label) {
        this.label = label;
    }

    //~ Methods ......................................................................................................................................

    /** Returns the field image path. */
    @NotNull public final String imagePath() {
        return BUNDLE.getString(name() + ".image", "");
    }

    @Override public int index() {
        return ordinal();
    }

    /** Returns the enum primary key. */
    @NotNull public final String key() {
        return name();
    }

    /** Returns the field label in the current locale. */
    @NotNull public final String label() {
        return BUNDLE.getString(name(), label);
    }

    //~ Methods ......................................................................................................................................

    /** Returns the Country Map. */
    @NotNull public static Map<String, Country> map() {
        return COUNTRY_MAP;
    }

    //~ Static Fields ................................................................................................................................

    @NotNull private static final Map<String, Country> COUNTRY_MAP = Enumerations.buildMap(values());
    @NotNull private static final I18nBundle           BUNDLE      = I18nBundle.getBundle(Country.class);
}
