
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
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.ArtifactGenerator;
import tekgenesis.common.IndentedWriter;
import tekgenesis.common.core.Constants;
import tekgenesis.common.util.Files;

import static tekgenesis.codegen.impl.js.JsArtifactGenerator.LastLine.NEW_LINE;

/**
 * An artifact is a generated File with inside elements.
 */
public class JsArtifactGenerator extends ArtifactGenerator {

    //~ Constructors .................................................................................................................................

    JsArtifactGenerator(File rootDir, @NotNull String currentPackage, String name) {
        super(rootDir, currentPackage, name, "." + Constants.JS_EXT);
    }

    //~ Methods ......................................................................................................................................

    /** Generate artifacts. */
    @SuppressWarnings("UnusedReturnValue")
    public File generate(@NotNull JsItemGenerator<?> main) {
        FileWriter w    = null;
        final File file = getTargetFile();
        try {
            getDirectory().mkdirs();
            w = new FileWriter(file);

            final StringWriter sw = new StringWriter();
            main.generate(new IndentedWriter(sw), this, NEW_LINE);
            w.write(sw.toString());
        }
        catch (final IOException e) {
            // todo Log ??? or make generate returns an IOException ?
            throw new RuntimeException(e);
        }
        finally {
            Files.close(w);
        }
        return file;
    }

    //~ Enums ........................................................................................................................................

    /**
     * Last line policy.
     */
    public enum LastLine {
        NEW_LINE { @Override public void print(IndentedWriter w, String s) { w.println(s); } },
        NO_NEW_LINE { @Override public void print(IndentedWriter w, String s) { w.print(s); } };

        /** Print last line. */
        public abstract void print(IndentedWriter w, String s);
    }
}  // end class JsArtifactGenerator
