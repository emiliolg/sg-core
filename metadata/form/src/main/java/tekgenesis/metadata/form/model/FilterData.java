
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.model;

import java.io.Serializable;

import tekgenesis.common.core.BitArray;
import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;

/**
 * Data used for filtering.
 */
public class FilterData implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private boolean accepted;

    private BitArray bits;
    private boolean  exclusive;
    private int      offset;

    //~ Constructors .................................................................................................................................

    private FilterData() {
        offset    = -1;
        bits      = null;
        accepted  = true;
        exclusive = false;
    }

    //~ Methods ......................................................................................................................................

    /** Set accepted flag. */
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    /** Get data bits. */
    public BitArray getBits() {
        return bits;
    }

    /** Return accepted flag. */
    public boolean isAccepted() {
        return accepted;
    }

    /** Return exclusive flag. */
    public boolean isExclusive() {
        return exclusive;
    }

    /** Set exclusive flag. */
    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    void serialize(StreamWriter w) {
        if (this != NONE) {
            w.writeInt(offset);
            w.writeBoolean(exclusive);
            w.writeBoolean(accepted);
            bits.serialize(w);
        }
        else w.writeInt(NONE_INSTANCE);
    }

    int getOffset() {
        return offset;
    }

    //~ Methods ......................................................................................................................................

    static FilterData bits(int bits) {
        final FilterData result = new FilterData();
        result.bits = new BitArray(bits);
        return result;
    }

    static FilterData data(int bits, int offset, boolean exclusive) {
        final FilterData result = new FilterData();
        result.bits      = new BitArray(bits);
        result.offset    = offset;
        result.exclusive = exclusive;
        return result;
    }

    static FilterData initialize(StreamReader r) {
        final int        offset = r.readInt();
        final FilterData result;

        if (offset == NONE_INSTANCE) result = NONE;
        else {
            result           = new FilterData();
            result.offset    = offset;
            result.exclusive = r.readBoolean();
            result.accepted  = r.readBoolean();
            result.bits      = BitArray.initialize(r);
        }

        return result;
    }  // end method initialize

    //~ Static Fields ................................................................................................................................

    public static final FilterData NONE = new FilterData() {
            @Override int getOffset() {
                throw empty();
            }

            @Override public BitArray getBits() {
                throw empty();
            }

            private IllegalStateException empty() {
                return new IllegalStateException("Empty data!");
            }

            private static final long serialVersionUID = 1807654124949285778L;
        };

    private static final int NONE_INSTANCE = -2;

    private static final long serialVersionUID = 7490689476305353711L;
}  // end class FilterData
