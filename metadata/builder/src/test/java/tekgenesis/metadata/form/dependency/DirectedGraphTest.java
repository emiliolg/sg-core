
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

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class DirectedGraphTest {

    //~ Methods ......................................................................................................................................

    @Test public void adjacency() {
        final DirectedGraph acyclic = new DirectedGraph(7);

        /*
         * 4-5;
         * |/|
         * 0-3-2-6;
         * |
         * 1;
         */

        acyclic.addEdge(0, 3).addEdge(1, 3).addEdge(2, 3).addEdge(2, 5).addEdge(2, 6).addEdge(3, 4).addEdge(4, 5).addEdge(3, 5);

        assertThat(acyclic.getVerticesCount()).isEqualTo(7);

        assertAdjacency(acyclic, 0, 3);
        assertAdjacency(acyclic, 1, 3);
        assertAdjacency(acyclic, 2, 3, 5, 6);
        assertAdjacency(acyclic, 3, 4, 5);
        assertAdjacency(acyclic, 4, 5);
        assertAdjacency(acyclic, 5);
        assertAdjacency(acyclic, 6);
    }

    private void assertAdjacency(DirectedGraph graph, int vertex, Integer... adjacency) {
        assertThat(graph.adjacency(vertex)).containsExactly(adjacency);
    }
}
