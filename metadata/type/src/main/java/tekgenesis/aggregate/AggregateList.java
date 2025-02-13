
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.aggregate;

import java.io.Serializable;
import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.common.serializer.Streams;
import tekgenesis.expr.Expression;
import tekgenesis.expr.RefTypeSolver;

/**
 * Aggregate list.
 */
public class AggregateList extends ArrayList<Aggregate> implements Serializable {

    //~ Constructors .................................................................................................................................

    /** Gwt constructor. */
    public AggregateList() {}

    //~ Methods ......................................................................................................................................

    /** Add new aggregate to list. */
    public AggregateList add(@NotNull final Expression expr, @NotNull final AggregateFn fn, @NotNull final String ref) {
        add(new Aggregate(expr, fn, ref));
        return this;
    }

    /** Compile Expressions. */
    public AggregateList compile(@NotNull final RefTypeSolver solver) {
        for (final Aggregate aggregate : this)
            aggregate.compile(solver);
        return this;
    }

    /** Serialize the List. */
    public void serialize(StreamWriter w) {
        Streams.writeList(w, this, Aggregate.WRITER);
    }

    @Override public String toString() {
        return Colls.mkString(this);
    }

    //~ Methods ......................................................................................................................................

    /** Instantiate the List from an Stream. */
    public static AggregateList instantiate(StreamReader reader) {
        return Streams.readList(reader, new AggregateList(), Aggregate.READER);
    }

    //~ Static Fields ................................................................................................................................

    public static final AggregateList EMPTY = new AggregateList();

    private static final long serialVersionUID = 7481206143924092013L;
}  // end class AggregateList
