
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.dependency;

import org.junit.Test;

import tekgenesis.metadata.form.dependency.TopologicalSorter.CycleDetectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;

import static tekgenesis.common.collections.Colls.listOf;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class TopologicalSorterTest {

    //~ Methods ......................................................................................................................................

    @Test public void cyclicException() {
        final DirectedGraph cyclic = new DirectedGraph(3).addEdge(0, 1).addEdge(1, 2).addEdge(2, 0);

        try {
            sort(cyclic);
            failBecauseExceptionWasNotThrown(CycleDetectedException.class);
        }
        catch (final CycleDetectedException e) {
            assertThat(e).hasMessageContaining(TopologicalSorter.CYCLE_DETECTED_MSG);
        }
    }

    @Test public void simpleSort() {
        /*
         * 6 < 4 > 5;
         *   ^ / ^
         * 0 > 3 < 2;
         *   ^
         *   1;
         */

        final DirectedGraph acyclic = new DirectedGraph(7).addEdge(0, 3)
                                      .addEdge(1, 3)
                                      .addEdge(2, 3)
                                      .addEdge(2, 5)
                                      .addEdge(3, 4)
                                      .addEdge(4, 5)
                                      .addEdge(4, 6)
                                      .addEdge(3, 5);

        assertThat(sort(acyclic)).containsExactly(2, 1, 0, 3, 4, 6, 5);

        assertTSort(acyclic, 0, 0, 3, 4, 6, 5);
        assertTSort(acyclic, 1, 1, 3, 4, 6, 5);
        assertTSort(acyclic, 2, 2, 3, 4, 6, 5);
        assertTSort(acyclic, 3, 3, 4, 6, 5);
        assertTSort(acyclic, 4, 4, 6, 5);
        assertTSort(acyclic, 5, 5);
        assertTSort(acyclic, 6, 6);

        final Iterable<Integer> result = TopologicalSorter.run(acyclic, listOf(2, 3, 4), true);
        assertThat(result).containsExactly(2, 3, 4, 6, 5);
    }

    @Test public void sortSubset() {
        /*
         * 4 > 5;
         * ^
         * 3 > 0 > 2 > 6 < 7 > 8 > 9;
         *   ^
         *   1;
         */

        final DirectedGraph acyclic = new DirectedGraph(10).addEdge(0, 2)
                                      .addEdge(1, 0)
                                      .addEdge(3, 4)
                                      .addEdge(3, 0)
                                      .addEdge(4, 5)
                                      .addEdge(2, 6)
                                      .addEdge(7, 8)
                                      .addEdge(7, 6)
                                      .addEdge(8, 9);

        assertThat(sort(acyclic)).containsExactly(7, 8, 9, 3, 4, 5, 1, 0, 2, 6);

        assertTSort(acyclic, 0, 0, 2, 6);
        assertTSort(acyclic, 1, 1, 0, 2, 6);
        assertTSort(acyclic, 2, 2, 6);
        assertTSort(acyclic, 3, 3, 4, 5, 0, 2, 6);
        assertTSort(acyclic, 4, 4, 5);
        assertTSort(acyclic, 5, 5);
        assertTSort(acyclic, 6, 6);
        assertTSort(acyclic, 7, 7, 8, 9, 6);
        assertTSort(acyclic, 8, 8, 9);
        assertTSort(acyclic, 9, 9);

        final Iterable<Integer> result = TopologicalSorter.run(acyclic, listOf(1, 7, 4), true);
        assertThat(result).containsExactly(4, 5, 7, 8, 9, 1, 0, 2, 6);
    }

    private void assertTSort(DirectedGraph graph, int vertex, Integer... adjacency) {
        assertThat(TopologicalSorter.run(graph, vertex, true)).containsExactly(adjacency);
    }

    //~ Methods ......................................................................................................................................

    private static Iterable<Integer> sort(DirectedGraph graph) {
        return TopologicalSorter.run(graph, true);
    }
}  // end class TopologicalSorterTest
