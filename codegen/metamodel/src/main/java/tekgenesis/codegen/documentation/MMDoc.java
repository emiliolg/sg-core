
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.documentation;

import java.io.File;
import java.io.IOException;
import java.util.List;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.mmcompiler.builder.BuilderErrorListener;

import static tekgenesis.common.collections.Colls.emptyList;

/**
 * Driver class for MM Doc.
 */
public class MMDoc {

    //~ Instance Fields ..............................................................................................................................

    protected File outputDir = null;

    private final String[]                     args;
    private final BuilderErrorListener.Default errorListener = new BuilderErrorListener.Default();

    private String                generatedDir = "";
    private String                mmDir        = "";
    private ImmutableList<String> models       = emptyList();
    private String                name         = "";
    private boolean               printGen     = false;

    //~ Constructors .................................................................................................................................

    private MMDoc(String[] args) {
        this.args = args;
    }

    //~ Methods ......................................................................................................................................

    /** @return  returns true if the build had errors */
    public boolean build()
        throws IOException
    {
        final List<File> generated = doBuild(new File(generatedDir));
        if (printGen) generated.forEach(System.out::println);

        return errorListener.hasErrors();
    }

    private List<File> doBuild(File generated)
        throws IOException
    {
        final DocBuilder docBuilder = new DocBuilder(new File(mmDir), models, outputDir, generated, name, errorListener);
        if (errorListener.hasErrors()) return emptyList();
        else return docBuilder.buildProject();
    }
    private MMDoc parseArguments() {
        String outDir = "";
        int    i      = 0;
        while (i < args.length) {
            final String arg = args[i];
            // noinspection IfStatementWithTooManyBranches
            if ("-o".equals(arg)) outDir = args[++i];
            else if ("-g".equals(arg)) generatedDir = args[++i];
            else if ("-m".equals(arg)) mmDir = args[++i];
            else if ("-p".equals(arg)) printGen = true;
            else if ("-n".equals(arg)) name = args[++i];
            else break;
            i++;
        }
        if (outDir.isEmpty() || mmDir.isEmpty()) {
            usage();
            System.exit(0);
        }
        if (i < args.length) models = ImmutableList.fromArray(args).subList(i, args.length);
        outputDir = new File(outDir);
        return this;
    }

    //~ Methods ......................................................................................................................................

    /** @param  args  : run arguments */
    public static void main(String[] args)
        throws IOException
    {
        if (new MMDoc(args).parseArguments().build()) System.exit(1);
    }

    private static void usage() {
        System.out.println("usage : -o <output dir> -g <generated sources dir> -m <meta models dir> " +
            "-p <print generated>");
    }
}  // end class MMDoc
