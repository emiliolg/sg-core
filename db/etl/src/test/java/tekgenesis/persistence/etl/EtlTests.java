
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.etl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.UncheckedIOException;
import java.util.List;

import tekgenesis.common.util.Diff;

import static org.assertj.core.api.Assertions.*;

class EtlTests {

    //~ Constructors .................................................................................................................................

    private EtlTests() {}

    //~ Methods ......................................................................................................................................

    static void checkFile(File file) {
        try {
            final File                     f2    = new File(dataDir, file.getName());
            final List<Diff.Delta<String>> diffs = Diff.caseSensitive().diff(new FileReader(file), new FileReader(f2));

            if (!diffs.isEmpty()) fail("diff " + file + " " + f2 + "\n" + Diff.makeString(diffs));
        }
        catch (final FileNotFoundException e) {
            throw new UncheckedIOException(e);
        }
    }

    //~ Static Fields ................................................................................................................................

    static final File outDir  = new File("target/db/etl/etl-test");
    static final File dataDir = new File("db/etl/src/test/data");
    static final File xmlDir  = new File(outDir, "xml");

    static {
        xmlDir.mkdirs();
    }
}
