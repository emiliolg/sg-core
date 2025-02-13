
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.dependency;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.jetbrains.annotations.NonNls;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;

/**
 * A topological sort of a {@link DirectedGraph} is a linear ordering of its vertices such that, for
 * every edge u-v, u comes before v in the ordering (draw the graph so that all edges point from
 * left to right).
 *
 * <p>This sorter aims to solve the ordering of expression evaluation when recomputing expression
 * values in forms</p>
 *
 * <p>This sorter will also detect cycles, since those are obstacles for topological order to exist!
 * :)</p>
 */
public class TopologicalSorter {

    //~ Instance Fields ..............................................................................................................................

    @SuppressWarnings("FieldMayBeFinal")  // it cannot be final, see '--count' below...
    private int             count;
    private final boolean   failOnCycle;
    private final boolean[] marked;
    private final Integer[] sorted;

    //~ Constructors .................................................................................................................................

    private TopologicalSorter(final int vertices, boolean failOnCycle) {
        count            = vertices;
        marked           = new boolean[count];
        sorted           = new Integer[count];
        this.failOnCycle = failOnCycle;
    }

    //~ Methods ......................................................................................................................................

    private void sort(final DirectedGraph graph) {
        final Set<Integer> visited = new TreeSet<>();

        for (int vertex = 0; vertex < marked.length; vertex++) {
            visited.add(vertex);

            if (!marked[vertex]) tsort(graph, vertex, visited);

            visited.remove(vertex);
        }
    }

    private void sort(final DirectedGraph graph, Integer vertex) {
        final Set<Integer> visited = new TreeSet<>();
        visited.add(vertex);
        tsort(graph, vertex, visited);
        visited.remove(vertex);
    }

    private void sort(final DirectedGraph graph, Iterable<Integer> vertices) {
        final Set<Integer> visited = new TreeSet<>();

        for (final Integer vertex : vertices) {
            visited.add(vertex);

            if (!marked[vertex]) tsort(graph, vertex, visited);

            visited.remove(vertex);
        }
    }

    private void tsort(final DirectedGraph graph, int vertex, Set<Integer> visited) {
        marked[vertex] = true;

        for (final Integer w : graph.adjacency(vertex)) {
            if (!visited.add(w) && failOnCycle) throw new CycleDetectedException(vertex, w, visited);
            if (!marked[w]) tsort(graph, w, visited);
            visited.remove(w);
        }

        sorted[--count] = vertex;
    }

    //~ Methods ......................................................................................................................................

    /**
     * Performs a topological sort of the whole graph.
     *
     * @param   graph  to be sorted
     *
     * @return  correct precedence ordering of vertices
     */
    public static Iterable<Integer> run(final DirectedGraph graph, boolean failOnCycle) {
        final TopologicalSorter sorter = new TopologicalSorter(graph.getVerticesCount(), failOnCycle);
        sorter.sort(graph);
        return ImmutableList.fromArray(sorter.sorted);
    }

    /**
     * Performs a topological sort of all vertices reachable from a given vertex (can be the whole
     * graph).
     *
     * @return  correct precedence ordering of affected vertices
     */
    public static Iterable<Integer> run(final DirectedGraph graph, Integer vertex, boolean failOnCycle) {
        final TopologicalSorter sorter = new TopologicalSorter(graph.getVerticesCount(), failOnCycle);
        sorter.sort(graph, vertex);
        return ImmutableList.fromArray(Arrays.copyOfRange(sorter.sorted, sorter.count, sorter.sorted.length));
    }

    /**
     * Performs a topological sort of all vertices reachable from a given set of vertices (can be
     * the whole graph).
     *
     * @return  correct precedence ordering of affected vertices
     */
    public static Iterable<Integer> run(final DirectedGraph graph, Iterable<Integer> vertices, boolean failOnCycle) {
        final TopologicalSorter sorter = new TopologicalSorter(graph.getVerticesCount(), failOnCycle);
        sorter.sort(graph, vertices);
        return ImmutableList.fromArray(Arrays.copyOfRange(sorter.sorted, sorter.count, sorter.sorted.length));
    }

    //~ Static Fields ................................................................................................................................

    @NonNls public static final String  CYCLE_DETECTED_MSG        = "Cycle Detected!";
    @NonNls private static final String CYCLE_DETECTED_MSG_DETAIL = CYCLE_DETECTED_MSG + " Attempting to link '%s' with '%s' having already: \n%s";

    //~ Inner Classes ................................................................................................................................

    public static class CycleDetectedException extends IllegalStateException {
        private final Integer      edge;
        private final Integer      vertex;
        private final Set<Integer> visited;

        private CycleDetectedException(Integer vertex, Integer edge, Set<Integer> visited) {
            super(CYCLE_DETECTED_MSG);

            this.vertex  = vertex;
            this.edge    = edge;
            this.visited = visited;
        }

        /** Return the edge. */
        public Integer getEdge() {
            return edge;
        }

        @Override public String getMessage() {
            return String.format(CYCLE_DETECTED_MSG_DETAIL, getVertex(), getEdge(), Colls.mkString(getVisited()));
        }

        /** Return the vertex. */
        public Integer getVertex() {
            return vertex;
        }

        /** Return the set of visited. */
        public Set<Integer> getVisited() {
            return visited;
        }

        private static final long serialVersionUID = 1220083365197995854L;
    }  // end class CycleDetectedException

    public static class CycleDetectedPrettyException extends IllegalStateException {
        private final String           edge;
        private final String           vertex;
        private final Iterable<String> visited;

        CycleDetectedPrettyException(String vertex, String edge, Iterable<String> visited) {
            super(CYCLE_DETECTED_MSG);

            this.vertex  = vertex;
            this.edge    = edge;
            this.visited = visited;
        }

        /** Return the edge. */
        public String getEdge() {
            return edge;
        }

        @Override public String getMessage() {
            return String.format(CYCLE_DETECTED_MSG_DETAIL, getVertex(), getEdge(), getVisited());
        }

        /** Return the vertex. */
        public String getVertex() {
            return vertex;
        }

        /** Return the set of visited. */
        public Iterable<String> getVisited() {
            return visited;
        }

        private static final long serialVersionUID = -438671275785662286L;
    }
}  // end class TopologicalSorter
