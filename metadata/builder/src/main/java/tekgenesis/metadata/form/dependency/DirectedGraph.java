
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.dependency;

import java.util.Set;
import java.util.TreeSet;

import static tekgenesis.common.Predefined.cast;

/**
 * Simple Directed Graph with an adjacency set to represent edges.
 */
@SuppressWarnings("UnusedReturnValue")
class DirectedGraph {

    //~ Instance Fields ..............................................................................................................................

    private final Set<Integer>[] adjacency;

    //~ Constructors .................................................................................................................................

    DirectedGraph(int vertices) {
        adjacency = cast(new TreeSet<?>[vertices]);

        init();
    }

    //~ Methods ......................................................................................................................................

    DirectedGraph addEdge(int v, int w) {
        adjacency[v].add(w);

        return this;
    }

    /**
     * Adjacency for a given vertex.
     *
     * @param   v  vertex
     *
     * @return  all vertices to which given vertex has direct edges
     */
    Iterable<Integer> adjacency(int v) {
        return adjacency[v];
    }

    int getVerticesCount() {
        return adjacency.length;
    }

    private void init() {
        for (int v = 0; v < adjacency.length; v++)
            adjacency[v] = new TreeSet<>();
    }
}  // end class DirectedGraph
