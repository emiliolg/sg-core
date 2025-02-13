
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
import java.io.Writer;

import tekgenesis.common.util.Files;
import tekgenesis.database.introspect.SchemaInfo;

/**
 * Generate Sql DDL statements to evolve from a given schema version to a new one.
 */
public class DeltaGenerator {

    //~ Instance Fields ..............................................................................................................................

    private final SequenceDiffer sequenceDiffer;

    private final TableDiffer tableDiffer;

    private final ViewsDiffer viewsDiffer;

    //~ Constructors .................................................................................................................................

    /** Create the generator. */
    public DeltaGenerator(final SchemaInfo from, final SchemaInfo to) {
        tableDiffer    = new TableDiffer(from, to);
        sequenceDiffer = new SequenceDiffer(from, to);
        viewsDiffer    = new ViewsDiffer(from, to);
    }

    //~ Methods ......................................................................................................................................

    /** Diff the sequence and returns the Delta between them. */
    public MdDelta diffSequences() {
        return sequenceDiffer.diff();
    }

    /** Diff the tables and returns the Delta between them. */
    public TableDeltas diffTables() {
        return tableDiffer.diff();
    }

    /** Diff the sequence and returns the Delta between them. */
    public MdDelta diffViews() {
        return viewsDiffer.diff();
    }

    /** Generate the SQL statements. */
    public void generate(Writer writer) {
        final PrintWriter pw = Files.printWriter(writer);
        sequenceDiffer.generateCreates(pw);
        tableDiffer.generate(pw);
        sequenceDiffer.generateDrops(pw);
        viewsDiffer.generate(pw);
    }

    /** Check that the difference is a minor one. */
    public boolean isMinor() {
        return diffSequences().isMinor() && diffTables().isMinor();
    }
}  // end class DeltaGenerator
