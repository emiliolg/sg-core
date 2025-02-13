
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.common;

import java.io.File;

/**
 * Common interface for MetaModel code generators.
 */
public interface MMCodeGenerator {

    //~ Methods ......................................................................................................................................

    /** Generate code. */
    @SuppressWarnings("UnusedReturnValue")
    void generate();

    /** Generate code if already generated is older than the source file. */
    @SuppressWarnings("UnusedReturnValue")
    boolean generateIfOlder(File source);

    /** Returns the source name to be generated. */
    String getSourceName();

    /** Returns target file to generate. */
    File getTargetFile();
}
