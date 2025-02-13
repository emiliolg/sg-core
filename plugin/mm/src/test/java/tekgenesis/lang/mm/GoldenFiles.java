
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

/**
 * Golden File base Directories.
 */
public interface GoldenFiles {

    //~ Instance Fields ..............................................................................................................................

    File dataDir   = new File("plugin/mm/src/test/resources/actionFiles");
    File goldenDir = new File("plugin/mm/src/test/resources/goldenFiles");
}
