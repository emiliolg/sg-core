
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen;

import java.io.*;

import org.jetbrains.annotations.NotNull;

/**
 * An Artifact is a Generated File with inside elements.
 */
public abstract class ArtifactGenerator {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final File directory;

    @NotNull private final File targetFile;

    //~ Constructors .................................................................................................................................

    protected ArtifactGenerator(File rootDir, @NotNull String currentPackage, String name, @NotNull String fileExt) {
        directory  = new File(rootDir, currentPackage.replace('.', File.separatorChar));
        targetFile = new File(directory, name + fileExt);
    }

    //~ Methods ......................................................................................................................................

    /** @return  The target file */
    @NotNull public File getTargetFile() {
        return targetFile;
    }

    /** @return  the directory */
    @NotNull protected File getDirectory() {
        return directory;
    }
}
