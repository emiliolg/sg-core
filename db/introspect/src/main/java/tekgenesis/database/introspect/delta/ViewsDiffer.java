
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

import org.jetbrains.annotations.Nullable;

import tekgenesis.database.introspect.SchemaInfo;
import tekgenesis.database.introspect.ViewInfo;

import static tekgenesis.database.introspect.TableInfo.getQName;

/**
 * Generate Sql DDL statements to evolve from a given schema version to a new one.
 */
class ViewsDiffer extends MdDiffer<ViewInfo> {

    //~ Instance Fields ..............................................................................................................................

    private final SchemaInfo from;
    private final SchemaInfo to;

    //~ Constructors .................................................................................................................................

    /** Create the generator. */
    public ViewsDiffer(final SchemaInfo from, final SchemaInfo to) {
        super(to.getPlainName());
        this.from = from;
        this.to   = to;
    }

    //~ Methods ......................................................................................................................................

    @Override public void generate(final PrintWriter pw) {
        if (diff().isEmpty()) return;

        for (final String name : getFromOnly())
            generateDrop(pw, name);

        // Drop foreign keys
        for (final String name : getChanged())
            generateAlter(pw, getTo(name));

        for (final String name : getToOnly())
            generateCreate(pw, getTo(name));
    }

    @Override public boolean isMinor() {
        return true;
    }

    @Override MdDelta diff() {
        return super.diff();
    }
    void generateAlter(final PrintWriter pw, final ViewInfo view) {
        view.dumpSql(pw, false);
        pw.println();
    }

    @Override void generateCreate(final PrintWriter pw, final ViewInfo view) {
        view.dumpSql(pw, true);
        pw.println();
    }

    @Override void generateDrop(final PrintWriter pw, final String name) {
        pw.printf("drop view %s;;", getQName(getSchemaName(), name));
        pw.println();
    }

    @Nullable @Override ViewInfo getFrom(String nm) {
        return from.getView(nm).getOrNull();
    }

    @Override Iterable<ViewInfo> getFromElements() {
        return from.getViews();
    }

    @Nullable @Override ViewInfo getTo(String nm) {
        return to.getView(nm).getOrNull();
    }

    @Override Iterable<ViewInfo> getToElements() {
        return to.getViews();
    }
}  // end class ViewsDiffer
