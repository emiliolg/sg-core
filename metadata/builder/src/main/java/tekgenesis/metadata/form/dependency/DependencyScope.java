
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.dependency;

import java.util.*;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.IntIntTuple;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.metadata.form.dependency.TopologicalSorter.run;

class DependencyScope implements Function<Integer, String> {

    //~ Instance Fields ..............................................................................................................................

    private final List<IntIntTuple> dependencies;

    private final ReferenceDictionary dictionary;
    private DirectedGraph             graph = null;

    //~ Constructors .................................................................................................................................

    DependencyScope() {
        dictionary   = new ReferenceDictionary();
        dependencies = new ArrayList<>();
    }

    //~ Methods ......................................................................................................................................

    @Override public String apply(Integer value) {
        return dictionary.asString(value);
    }

    /** Return all dependencies in any order. */
    public List<String> nodes() {
        return dictionary.decode;
    }

    void addDependencies(@NotNull final Widget widget, @NotNull final Collection<String> refs) {
        if (!refs.isEmpty()) {
            final int right = dictionary.put(widget.getName());

            for (final String ref : refs) {
                final int left = dictionary.put(ref);

                // skip auto reference (aka: this)
                if (right != left) dependencies.add(tuple(left, right));
            }
        }
    }

    TopologicalSorter.CycleDetectedPrettyException createCyclicException(TopologicalSorter.CycleDetectedException e) {
        return new TopologicalSorter.CycleDetectedPrettyException(dictionary.asString(e.getEdge()),
            dictionary.asString(e.getVertex()),
            map(e.getVisited(), this));
    }

    void initGraph() {
        graph = new DirectedGraph(dictionary.size());

        for (final IntIntTuple dependency : dependencies)
            graph.addEdge(dependency.first(), dependency.second());
    }

    Iterable<String> topologicalSort() {
        return map(run(graph, false), this);
    }

    Iterable<String> topologicalSort(@NotNull final Seq<String> widgetIds) {
        return map(run(graph, dictionary.asInteger(widgetIds), false), this);
    }

    Iterable<String> topologicalSort(@NotNull final String qualification) {
        return map(run(graph, dictionary.qualifiedAsInteger(qualification), false), this);
    }

    Seq<String> topologicalSort(@NotNull final String widgetId, boolean failOnCycle) {
        final Integer vertex = dictionary.asInteger(widgetId);
        return vertex != null ? map(run(graph, vertex, failOnCycle), this) : Colls.emptyIterable();
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Bidirectional mapping between string and integer references representation fieldA <--> 0
     * fieldB <--> 1 fieldC <--> 2 total <--> 3.
     */
    private static class ReferenceDictionary {
        private final List<String> decode;

        private final Map<String, Integer> encode;

        ReferenceDictionary() {
            encode = new HashMap<>();
            decode = new ArrayList<>();
        }

        Iterable<Integer> asInteger(Seq<String> references) {
            return references.map(this::asInteger);
        }

        Iterable<Integer> qualifiedAsInteger(final String qualification) {
            return map(encode.keySet(), value -> value.startsWith(qualification) ? asInteger(value) : null);
        }

        private Integer asInteger(String reference) {
            return encode.get(reference);
        }

        private String asString(Integer reference) {
            return decode.get(reference);
        }

        private Integer put(final String reference) {
            final Integer index;

            if (encode.containsKey(reference)) index = encode.get(reference);
            else {
                index = decode.size();
                decode.add(reference);
                encode.put(reference, index);
            }

            return index;
        }  // end method put

        private int size() {
            return encode.size();
        }
    }  // end class ReferenceDictionary
}
