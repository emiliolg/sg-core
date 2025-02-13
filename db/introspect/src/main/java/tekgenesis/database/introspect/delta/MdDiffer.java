
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.introspect.delta;

import java.io.PrintWriter;
import java.util.*;

import tekgenesis.common.core.Tuple3;
import tekgenesis.database.introspect.MetadataObject;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.core.Tuple.tuple;

/**
 * Base class for Delta Generators.
 */
abstract class MdDiffer<T extends MetadataObject<T>> implements MdDelta {

    //~ Instance Fields ..............................................................................................................................

    private final List<String> changed;
    private final List<String> fromOnly;
    private boolean            mustDiff;

    private final Map<String, String> renamed;
    private final String              schemaName;
    private final List<String>        toOnly;

    //~ Constructors .................................................................................................................................

    MdDiffer(String schemaName) {
        this.schemaName = schemaName;
        toOnly          = new ArrayList<>();
        fromOnly        = new ArrayList<>();
        changed         = new ArrayList<>();
        renamed         = new LinkedHashMap<>();
        mustDiff        = true;
    }

    //~ Methods ......................................................................................................................................

    @Override public void createElements(final PrintWriter pw) {
        for (final String name : getToOnly())
            generateCreate(pw, getTo(name));
    }

    @Override public void dropElements(final PrintWriter pw) {
        for (final String name : getFromOnly())
            generateDrop(pw, name);
    }

    @Override public void generate(final PrintWriter pw) {
        dropElements(pw);

        // Rename elements
        for (final Map.Entry<String, String> e : getRenamed().entrySet()) {
            if (!e.getKey().equals(e.getValue())) generateRename(pw, e.getKey(), e.getValue());
        }

        createElements(pw);

        // Changed Elements
        for (final String name : getChanged())
            generateAlter(pw, name);
    }

    @Override public List<String> getChanged() {
        return changed;
    }

    @Override public List<String> getFromOnly() {
        return fromOnly;
    }

    @Override public boolean isMinor() {
        return getFromOnly().isEmpty() && getRenamed().isEmpty() && getChanged().isEmpty();
    }

    @Override public Map<String, String> getRenamed() {
        return renamed;
    }

    @Override public List<String> getToOnly() {
        return toOnly;
    }

    @Override public boolean isEmpty() {
        return toOnly.isEmpty() && fromOnly.isEmpty() && changed.isEmpty() && renamed.isEmpty();
    }

    /** Calculate differences. */
    MdDelta diff() {
        if (mustDiff) {
            matchRenames();
            findRenames();
            mustDiff = false;
        }
        return this;
    }

    int diffWeight(T f, T t) {
        return f.sameAs(t) ? 0 : Integer.MAX_VALUE;
    }

    void findRenames() {
        final List<Tuple3<String, String, Integer>> differences = new ArrayList<>();
        for (final String fnm : fromOnly) {
            final T f = getFrom(fnm);
            for (final String tnm : toOnly) {
                final T   t    = getTo(tnm);
                final int diff = diffWeight(f, t);
                if (diff < getDiffThreshold()) differences.add(tuple(fnm, tnm, diff));
            }
        }
        Collections.sort(differences, Comparator.comparingInt(Tuple3::_3));
        for (final Tuple3<String, String, Integer> d : differences) {
            final String nameFrom = d._1();
            final String nameTo   = d._2();
            if (fromOnly.remove(nameFrom) && toOnly.remove(nameTo)) {
                renamed.put(nameFrom, nameTo);
                if (!getFrom(nameFrom).sameAs(getTo(nameTo))) changed.add(nameFrom);
            }
        }
    }

    void generateAlter(final PrintWriter pw, final String name) {
        final T e = getRenamedTo(name);
        generateDrop(pw, e.getName());
        generateCreate(pw, e);
    }

    abstract void generateCreate(final PrintWriter pw, final T element);
    abstract void generateDrop(final PrintWriter pw, final String name);

    void generateRename(final PrintWriter pw, final String from, final String to) {
        generateDrop(pw, from);
        generateCreate(pw, getTo(to));
    }

    int getDiffThreshold() {
        return 1;
    }
    abstract T getFrom(String nm);
    abstract Iterable<T> getFromElements();

    T getRenamedTo(final String fromName) {
        return getTo(notNull(renamed.get(fromName), fromName));
    }

    String getSchemaName() {
        return schemaName;
    }
    abstract T getTo(String nm);
    abstract Iterable<T> getToElements();

    private void matchRenames() {
        for (final T tf : getFromElements()) {
            final String name = tf.getName();
            final T      tt   = getTo(name);
            if (tt == null) fromOnly.add(name);
            else if (!tf.sameAs(tt)) changed.add(name);
        }
        for (final T tt : getToElements()) {
            final String name = tt.getName();
            if (getFrom(name) == null) toOnly.add(name);
        }
    }
}  // end class MdDiffer
