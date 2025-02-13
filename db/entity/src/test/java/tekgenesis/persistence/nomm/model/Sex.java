
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

import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.common.env.i18n.I18nBundle;

/**
 * A test class.
 */
public enum Sex implements Enumeration<Sex, String> {

    //~ Enum constants ...............................................................................................................................

    F("Nena"), M("Nene");

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final String label;

    //~ Constructors .................................................................................................................................

    Sex(@NotNull String label) {
        this.label = label;
    }

    //~ Methods ......................................................................................................................................

    /** Returns the field image path. */
    @NotNull public final String imagePath() {
        return BUNDLE.getString(name() + "." + Constants.IMAGE_EXT, "");
    }

    @Override public int index() {
        return ordinal();
    }

    /** Returns the enum id. */
    @NotNull public final String key() {
        return name().toLowerCase();
    }

    /** Returns the field label in the current locale. */
    @NotNull public final String label() {
        return BUNDLE.getString(name(), label);
    }

    //~ Methods ......................................................................................................................................

    /** Returns the Sex Values. */
    @NotNull public static Map<String, Sex> map() {
        return SEX_MAP;
    }

    //~ Static Fields ................................................................................................................................

    @NotNull private static final Map<String, Sex> SEX_MAP = Enumerations.buildMap(values());

    @NotNull private static final I18nBundle BUNDLE = I18nBundle.getBundle(Sex.class);
}
