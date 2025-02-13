
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.introspect;

import java.io.PrintWriter;
import java.math.BigDecimal;

import org.jetbrains.annotations.Nullable;

/**
 * Metadata Information about a Sequence.
 */
public class SequenceInfo extends SchemaObject<SequenceInfo> {

    //~ Instance Fields ..............................................................................................................................

    private final int        cache;
    private final boolean    cycle;
    private final int        inc;
    private final BigDecimal last;
    private final BigDecimal max;
    private final BigDecimal min;
    private final BigDecimal start;

    //~ Constructors .................................................................................................................................

    SequenceInfo(final SchemaInfo schema, final String name) {
        this(schema, name, BigDecimal.ONE, BigDecimal.ONE, null, 1, false, 1, null);
    }

    @SuppressWarnings("ConstructorWithTooManyParameters")
    SequenceInfo(final SchemaInfo schema, final String name, final BigDecimal start, final BigDecimal min, @Nullable final BigDecimal max,
                 final int inc, final boolean cycle, final int cache, @Nullable final BigDecimal last) {
        super(schema, name, "");
        this.start = start;
        this.min   = min;
        this.max   = max;
        this.inc   = inc;
        this.cycle = cycle;
        this.cache = cache;
        this.last  = last;
    }

    //~ Methods ......................................................................................................................................

    /** Dump Sql for sequence. */
    public void dumpSql(final PrintWriter w) {
        w.printf("create sequence QName(%s, %s)%n", getSchema().getPlainName(), getName());
        w.printf("\tstart with SequenceStartValue(%d)%n", start.longValue());
        w.printf("\tincrement by %d SequenceCache;;%n", inc);
    }

    @Override public boolean sameAs(SequenceInfo to) {
        // to do add cache and start value
        // noinspection EqualsBetweenInconvertibleTypes
        return super.equals(to) && inc == to.inc;
    }

    /** Returns sequence cache size. */
    public int getCache() {
        return cache;
    }

    /** Returns true if sequence is cyclic. */
    public boolean isCycle() {
        return cycle;
    }

    /** Returns sequence increment. */
    public int getInc() {
        return inc;
    }

    /** Returns sequence last value. */
    @Nullable public BigDecimal getLast() {
        return last;
    }

    /** Returns sequence max value. */
    @Nullable public BigDecimal getMax() {
        return max;
    }

    /** Returns sequence min value. */
    public BigDecimal getMin() {
        return min;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -3741882772234848627L;
}  // end class SequenceInfo
