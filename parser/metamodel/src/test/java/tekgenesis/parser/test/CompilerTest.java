
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.parser.test;

import java.io.*;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.Diff;
import tekgenesis.common.util.Files;
import tekgenesis.lexer.FileStream;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.mmcompiler.parser.MetaModelCompiler;
import tekgenesis.parser.ASTNode;
import tekgenesis.parser.Diagnostic;

import static java.lang.Boolean.getBoolean;

import static org.assertj.core.api.Assertions.fail;

import static tekgenesis.common.tools.test.Tests.wrapForParameters;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class CompilerTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameterized.Parameter public String mm = null;

    private final File astDataDir   = new File("parser/metamodel/src/test/data/ast");
    private final File astOutputDir = new File("target/metamodel/entity/test-output/ast");

    //~ Methods ......................................................................................................................................

    @Before public void setup() {
        astOutputDir.mkdirs();
    }

    @Test public void treeGeneration()
        throws IOException
    {
        final File   f       = new File(mm);
        final String outName = f.getName() + ".ast";

        final MetaModelCompiler builder = new MetaModelCompiler(new FileStream(f), f.getPath());
        final MetaModelAST      ast     = builder.getAST();

        final List<Diagnostic> errors = builder.getMessages();
        if (!errors.isEmpty()) fail(Colls.mkString(errors, "\n"));

        final File outFile = new File(astOutputDir, outName);
        ASTNode.Utils.printTree(ast, new PrintWriter(outFile));

        final File                              goldenFile = new File(astDataDir, outName);
        final ImmutableList<Diff.Delta<String>> deltas     = Diff.ignoreAllSpace().diff(new FileReader(outFile), new FileReader(goldenFile));

        deltas(outFile, goldenFile, deltas);
    }

    private void deltas(File outFile, File goldenFile, ImmutableList<Diff.Delta<String>> deltas)
        throws IOException
    {
        if (!deltas.isEmpty()) {
            if (getBoolean("golden.force")) Files.copy(outFile, goldenFile, true);
            else {
                // Tests.checkDiff hangs if failure message contains diff string...
                final String diff = "\ndiff -y -W 150 " + outFile.toString() + " " + goldenFile.toString();
                logger.error((diff + "\n" + Diff.makeString(deltas)));
                fail(diff);
            }
        }
    }  // end method deltas

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "{0}")
    public static Seq<Object[]> listFiles() {
        return wrapForParameters(Files.list(samplesBasicDir, MMS));
    }

    //~ Static Fields ................................................................................................................................

    public static final Logger logger = Logger.getLogger(CompilerTest.class);

    private static final File samplesBasicDir = new File("samples/basic/src/main/mm");

    static final FileFilter MMS = path -> path.isFile() && path.getName().endsWith(".mm");
}  // end class CompilerTest
