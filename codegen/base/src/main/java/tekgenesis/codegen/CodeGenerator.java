
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen;

import java.io.File;

import org.jetbrains.annotations.NotNull;

/**
 * A Generic generator for Code.
 */
@SuppressWarnings("WeakerAccess")
public abstract class CodeGenerator<T extends ArtifactGenerator> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private String     currentPackage;
    @NotNull private final File rootDir;

    //~ Constructors .................................................................................................................................

    /**
     * Default constructor.
     *
     * @param  currentPackage  current package
     * @param  rootDir         the root dir
     */
    protected CodeGenerator(@NotNull String currentPackage, @NotNull File rootDir) {
        this.currentPackage = currentPackage;
        this.rootDir        = rootDir;
    }

    //~ Methods ......................................................................................................................................

    /**
     * Create a new Artifact Generator.
     *
     * @param   name  The name
     *
     * @return  an ArtifactGenerator
     */
    public abstract T newArtifactGenerator(String name);

    /** Return current package name.* */
    @NotNull public String getCurrentPackage() {
        return currentPackage;
    }

    protected void setCurrentPackage(@NotNull String currentPackage) {
        this.currentPackage = currentPackage;
    }

    @NotNull protected File getRootDir() {
        return rootDir;
    }
}  // end class CodeGenerator
