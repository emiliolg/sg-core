
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

@SuppressWarnings({ "MagicNumber", "StatementWithEmptyBody" })
class Timings {

    //~ Constructors .................................................................................................................................

    private Timings() {}

    //~ Static Fields ................................................................................................................................

    static {
        final long start = System.currentTimeMillis();
        BigInteger k     = new BigInteger("1");
        for (int i = 0; i < 1000000; i++)
            k = k.add(new BigInteger("1"));
        for (int i = 0; i < 15; i++) {
            try {
                final File       tempFile = File.createTempFile("test", "test" + i);
                final FileWriter writer   = new FileWriter(tempFile);
                for (int j = 0; j < 15; j++) {
                    writer.write("test" + j);
                    writer.flush();
                }
                writer.close();
                final FileReader reader = new FileReader(tempFile);
                while (reader.read() >= 0) {}
                reader.close();
                tempFile.delete();
            }
            catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }

        MACHINE_TIMING = System.currentTimeMillis() - start;
    }

    public static final long MACHINE_TIMING;
}
