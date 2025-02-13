
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.aggregate;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.type.Type;

import static tekgenesis.type.Types.anyType;
import static tekgenesis.type.Types.intType;

/**
 * Aggregate function.
 */
@SuppressWarnings("WeakerAccess")
public enum AggregateFn {

    //~ Enum constants ...............................................................................................................................

    /** Returns the average value. */
    AVG(anyType()),
    /** Returns the sum of all values. */
    SUM(anyType()),
    /** Returns the largest value. */
    MAX(anyType()),
    /** Returns the smallest value. */
    MIN(anyType()),
    /** Returns the number of not null values. */
    COUNT(intType()),
    /** Returns the number of rows. */
    ROWS(intType()),
    /** Returns the first value. */
    FIRST(anyType()),
    /** Returns the last value. */
    LAST(anyType());

    //~ Instance Fields ..............................................................................................................................

    private final Type type;

    //~ Constructors .................................................................................................................................

    AggregateFn(Type type) {
        this.type = type;
    }

    //~ Methods ......................................................................................................................................

    /** Serialize to a Stream. */
    public void serialize(@NotNull final StreamWriter w) {
        w.writeInt(ordinal());
    }

    /** Get aggregate return type. */
    public Type getType() {
        return type;
    }

    //~ Methods ......................................................................................................................................

    /** Instantiate from an Stream. */
    public static AggregateFn instantiate(@NotNull final StreamReader r) {
        return values()[r.readInt()];
    }
}
