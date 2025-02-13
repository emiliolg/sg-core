
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.impl.js;

import java.io.File;

import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.CodeGenerator;

/**
 * A generic generator for JS Code.
 */
@SuppressWarnings({ "WeakerAccess", "ClassEscapesDefinedScope" })
public class JsCodeGenerator extends CodeGenerator<JsArtifactGenerator> {

    //~ Constructors .................................................................................................................................

    /**
     * Creates a Code generator, that will generate source files in the specified root directory It
     * initialize the <code>package</code> for the items generated to the specified one.
     */
    public JsCodeGenerator(@NotNull File rootDir, @NotNull String packageName) {
        super(packageName, rootDir);
    }

    //~ Methods ......................................................................................................................................

    /** Create a new Artifact Generator for Js code. */
    @Override public JsArtifactGenerator newArtifactGenerator(String name) {
        return new JsArtifactGenerator(getRootDir(), getCurrentPackage(), name);
    }

    /** Creates a new script to be generated. */
    public ScriptGenerator newScript(String name) {
        return new ScriptGenerator(this, name);
    }
}  // end class JsCodeGenerator
