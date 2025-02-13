
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.graph.graphUltimate.model;

import org.jetbrains.annotations.NotNull;

/**
 * Graph edge with label.
 */
public class Edge<T> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private T from;

    @NotNull private String label = "";
    @NotNull private T      to;

    //~ Constructors .................................................................................................................................

    /** Edge without Label. */
    public Edge(@NotNull T from, @NotNull T to) {
        this(from, to, "");
    }

    /** Edge with Label. */
    public Edge(@NotNull T from, @NotNull T to, String label) {
        this.from = from;
        this.to   = to;
        setLabel(label);
    }

    //~ Methods ......................................................................................................................................

    @Override
    @SuppressWarnings("rawtypes")
    public boolean equals(Object obj) {
        if (obj instanceof Edge) {
            final Edge dep = (Edge) obj;
            return from.equals(dep.getFrom()) && to.equals(dep.getTo());
        }
        return false;
    }

    @Override public int hashCode() {
        return from.hashCode();
    }
    /** Reverses Edge. */
    public void reverse() {
        final T temp = from;
        from = to;
        to   = temp;
    }

    @Override public String toString() {
        return "Edge(" + from + ", " + to + ")";
    }
    /** Gets Edge's Origin. */
    @NotNull public T getFrom() {
        return from;
    }
    /** Sets Edge's Origin. */
    public void setFrom(@NotNull T from) {
        this.from = from;
    }
    /** Gets Edge's Label. */
    @NotNull public String getLabel() {
        return label;
    }
    /** Gets Edge's Name. */
    public String getName() {
        return label;
    }
    /** Gets Edge's Destination. */
    @NotNull public T getTo() {
        return to;
    }
    /** Sets Edge's Destination. */
    public void setTo(@NotNull T to) {
        this.to = to;
    }
    /** Sets Edge's Label. */
    private void setLabel(@NotNull String label) {
        this.label = label;
    }
}  // end class Edge
