
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

import tekgenesis.database.introspect.SchemaInfo;
import tekgenesis.database.introspect.SequenceInfo;

import static tekgenesis.database.introspect.SchemaInfo.generateSequences;
import static tekgenesis.database.introspect.TableInfo.getQName;

/**
 * Generate Sql DDL statements to evolve from a given schema version to a new one.
 */
class SequenceDiffer extends MdDiffer<SequenceInfo> {

    //~ Instance Fields ..............................................................................................................................

    private final SchemaInfo from;
    private final SchemaInfo to;

    //~ Constructors .................................................................................................................................

    /** Create the generator. */
    public SequenceDiffer(final SchemaInfo from, final SchemaInfo to) {
        super(to.getPlainName());
        this.from = from;
        this.to   = to;
    }

    //~ Methods ......................................................................................................................................

    @Override public void generate(final PrintWriter pw) {
        if (diff().isEmpty()) return;
        generateSequences(pw, () -> SequenceDiffer.super.generate(pw));
    }

    public void generateCreates(final PrintWriter pw) {
        if (diff().getToOnly().isEmpty()) return;
        generateSequences(pw, () -> SequenceDiffer.super.createElements(pw));
    }
    public void generateDrops(final PrintWriter pw) {
        if (diff().getFromOnly().isEmpty()) return;
        generateSequences(pw, () -> SequenceDiffer.super.dropElements(pw));
    }

    @Override MdDelta diff() {
        return super.diff();
    }

    @Override void generateCreate(final PrintWriter pw, final SequenceInfo seq) {
        seq.dumpSql(pw);
        pw.println();
    }

    @Override void generateDrop(final PrintWriter pw, final String name) {
        pw.printf("drop   sequence %s /* Ignore Errors */;;%n%n", getQName(getSchemaName(), name));
    }

    @Override SequenceInfo getFrom(String nm) {
        return from.getSequence(nm);
    }

    @Override Iterable<SequenceInfo> getFromElements() {
        return from.getSequences();
    }

    @Override SequenceInfo getTo(String nm) {
        return to.getSequence(nm);
    }

    @Override Iterable<SequenceInfo> getToElements() {
        return to.getSequences();
    }
}  // end class SequenceDiffer
