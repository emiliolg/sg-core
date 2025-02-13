
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.model;

import java.io.Serializable;

/**
 * Metadata model simple value with count.
 */
@SuppressWarnings("FieldMayBeFinal")  // GWT
public class ValueCount implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private int    count;
    private String value;

    //~ Constructors .................................................................................................................................

    /** Serialization! */
    public ValueCount() {
        value = "";
        count = 0;
    }

    /** Create a new value with given count. */
    public ValueCount(String value, int count) {
        this.value = value;
        this.count = count;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValueCount)) return false;
        final ValueCount that = (ValueCount) o;
        return count == that.count && value.equals(that.value);
    }

    @Override public int hashCode() {
        int result = count;
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override public String toString() {
        return value + " (" + count + ")";
    }

    /** Return a new value with the given updated count. */
    public ValueCount withCount(int c) {
        return new ValueCount(value, c);
    }

    /** True if count is zero. */
    public boolean isZero() {
        return count == 0;
    }

    /** Get value. */
    public String getValue() {
        return value;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -1548600741576922808L;
}  // end class ValueCount
