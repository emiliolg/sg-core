
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.parser.test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.tools.test.Tests;
import tekgenesis.lexer.FileStream;
import tekgenesis.metadata.exception.BuilderError;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.mmcompiler.builder.BuilderErrorListener;
import tekgenesis.mmcompiler.builder.BuilderFromAST;
import tekgenesis.mmcompiler.parser.MetaModelCompiler;
import tekgenesis.parser.Diagnostic;
import tekgenesis.repository.ModelRepository;

import static org.junit.runners.Parameterized.Parameter;

import static tekgenesis.common.tools.test.Tests.wrapForParameters;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class CompilerErrorTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public File file = null;

    final File dataDir = new File(".");

    final File errorsDataDir = new File("parser/metamodel/src/test/data/errors");

    final File errorsOutputDir = new File("target/metamodel/entity/test-output/errors");

    //~ Methods ......................................................................................................................................

    @Test public void compilerErrors()
        throws IOException
    {
        final String outName = file.getName() + ".err";
        errorsOutputDir.mkdirs();
        final File outFile = new File(errorsOutputDir, outName);

        final PrintWriter writer = new PrintWriter(outFile);
        writer.println("File: " + badModels + "/" + file.getName());

        final MetaModelCompiler builder = new MetaModelCompiler(new FileStream(file), file.getPath());
        builder.getAST();

        writer.println("Total Errors: " + builder.getMessages().size());
        for (final Diagnostic d : builder.getMessages())
            writer.printf("%7s: %s\n", d.getPosition(), d.getMessage());

        writer.flush();

        final File goldenFile = new File(errorsDataDir, outName);
        Tests.checkDiff(outFile, goldenFile);
    }

    @Test public void makerErrors()
        throws IOException
    {
        final ModelRepository repository = new ModelRepository();

        final MetaModelCompiler builder = new MetaModelCompiler(new FileStream(file), file.getPath());
        final MetaModelAST      ast     = builder.getAST();

        final String outName = file.getName() + ".merr";
        errorsOutputDir.mkdirs();
        final File outFile = new File(errorsOutputDir, outName);

        final PrintWriter writer = new PrintWriter(outFile);
        writer.println("File: " + file);

        final MyErrorListener listener = new MyErrorListener(writer);
        final BuilderFromAST  b        = new BuilderFromAST(repository, listener);
        b.build(file.getPath(), ast);

        writer.flush();

        final File goldenFile = new File(errorsDataDir, outName);
        Tests.checkDiff(outFile, goldenFile);
    }

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "{0}")
    public static Seq<Object[]> listFiles() {
        return wrapForParameters(new File(badModels).listFiles(CompilerTest.MMS));
    }

    //~ Static Fields ................................................................................................................................

    static String badModels = "parser/metamodel/src/test/data/badModels/tekgenesis/sales";

    //~ Inner Classes ................................................................................................................................

    public class MyErrorListener extends BuilderErrorListener.DefaultWriter {
        private final PrintWriter writer;

        public MyErrorListener(PrintWriter writer) {
            super(writer);
            this.writer = writer;
        }

        @Override public void error(BuilderError error) {
            writer.println(error.getMessage());
        }

        @Override public void error(MetaModelAST node, BuilderError error) {
            writer.println(node.getPosition() + ": " + error.getMessage());
        }
    }
}  // end class CompilerErrorTest
