
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.introspect;

import java.util.EnumSet;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.logging.Logger;

/**
 * A Table Type.
 */
public enum TableType {

    //~ Enum constants ...............................................................................................................................

    TABLE, VIEW, ALIAS, SYNONYM, SYSTEM_TABLE, GLOBAL_TEMPORARY, FOREIGN_TABLE, INDEX, SEQUENCE, SYSTEM_INDEX, SYSTEM_TOAST_INDEX, SYSTEM_TOAST_TABLE,
    SYSTEM_VIEW, TEMPORARY_INDEX, TEMPORARY_SEQUENCE, TEMPORARY_TABLE, TEMPORARY_VIEW, TYPE, OTHER;

    //~ Methods ......................................................................................................................................

    /** Table Type is a Table. */
    public boolean isTable() {
        return this == TABLE || this == SYSTEM_TABLE || this == SYSTEM_TOAST_TABLE || this == TEMPORARY_TABLE;
    }

    /** Table Type is a View. */
    public boolean isView() {
        return this == VIEW || this == SYSTEM_VIEW || this == TEMPORARY_VIEW;
    }

    //~ Methods ......................................................................................................................................

    /** Create from String. */
    public static TableType fromString(final String value) {
        try {
            return valueOf(value.trim().replace(' ', '_').toUpperCase());
        }
        catch (final IllegalArgumentException e) {
            logger.warning("Unknown " + value + " using OTHER.");
            return OTHER;
        }
    }

    @Nullable static String[] setToArray(EnumSet<TableType> set) {
        if (EnumSet.allOf(TableType.class).equals(set)) return null;
        final String[] result = new String[set.size()];
        int            i      = 0;
        for (final TableType t : set)
            result[i++] = t.name().replace('_', ' ');
        return result;
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(TableType.class);
}
