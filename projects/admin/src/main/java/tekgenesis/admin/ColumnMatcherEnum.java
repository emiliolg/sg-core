
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.admin;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.common.env.i18n.I18nBundle;

/**
 * SQL Column Matcher expressions.
 */
public enum ColumnMatcherEnum implements Enumeration<ColumnMatcherEnum, String> {

    //~ Enum constants ...............................................................................................................................

    EQUALS("=", "="), NOT_EQUALS("!=", "!="), GT(">", ">"), GTE(">=", ">="), LT("<", "<"), LTE("<=", "<="), LIKE("LIKE", "like"),
    DISLIKE("NOT LIKE", "not like"), IN("IN", "in"), NOT_IN("NOT IN", "not in"), IS_NULL("IS NULL", "is null"),
    IS_NOT_NULL("IS NOT NULL", "is not null");

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final String label;
    @NotNull private final String sql;

    //~ Constructors .................................................................................................................................

    ColumnMatcherEnum(@NotNull String label, @NotNull String sql) {
        this.label = label;
        this.sql   = sql;
    }

    //~ Methods ......................................................................................................................................

    /** Returns the field image path. */
    @NotNull
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public final String imagePath() {
        return BUNDLE.getString(name() + ".image", "");
    }

    @Override public int index() {
        return ordinal();
    }

    @Override public String key() {
        return name();
    }

    /** Returns the field label in the current locale. */
    @NotNull public final String label() {
        return BUNDLE.getString(name(), label);
    }

    /** @return  The associated SQL expression */
    @NotNull public final String getSql() {
        return sql;
    }

    //~ Methods ......................................................................................................................................

    /** Returns the Bands Map. */
    @NotNull public static Map<String, ColumnMatcherEnum> map() {
        return COLUMN_MATCHERVALUES_MAP;
    }

    /** Returns the Column Matchervalues. */
    @NotNull public static ImmutableList<ColumnMatcherEnum> getValues() {
        return COLUMN_MATCHERVALUES;
    }

    //~ Static Fields ................................................................................................................................

    @NotNull private static final ImmutableList<ColumnMatcherEnum> COLUMN_MATCHERVALUES     = ImmutableList.fromArray(values());
    @NotNull private static final Map<String, ColumnMatcherEnum>   COLUMN_MATCHERVALUES_MAP = Enumerations.buildMap(values());

    @NotNull private static final I18nBundle BUNDLE = I18nBundle.getBundle(ColumnMatcherEnum.class);
}
