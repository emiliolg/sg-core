
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.project;

import java.io.File;
import java.io.IOException;
import java.util.List;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.mmcompiler.builder.BuilderErrorListener;

import static tekgenesis.common.collections.Colls.emptyList;

/**
 * Driver class for MMBuild.
 */
@SuppressWarnings("WeakerAccess")
public class MmBuild {

    //~ Instance Fields ..............................................................................................................................

    protected File outputDir = null;

    private final String[]                     args;
    private boolean                            entitiesOnly  = false;
    private final BuilderErrorListener.Default errorListener = new BuilderErrorListener.Default();

    private String                generatedDir = "";
    private String                mmDir        = "";
    private ImmutableList<String> models       = emptyList();
    private boolean               printGen     = false;
    private String                projectName  = "";
    private boolean               servicesOnly = false;
    private String                sourcesDir   = "";

    //~ Constructors .................................................................................................................................

    MmBuild(String[] args) {
        this.args = args;
    }

    //~ Methods ......................................................................................................................................

    final boolean build()
        throws IOException
    {
        final List<File> generated = doBuild(models, new File(sourcesDir), new File(generatedDir));
        if (printGen) generated.forEach(System.out::println);

        return errorListener.hasErrors();
    }

    List<File> doBuild(Seq<String> mms, File sources, File generated)
        throws IOException
    {
        if (mms.isEmpty()) return emptyList();

        final ProjectBuilder pb = new ProjectBuilder(new File(mmDir),
                outputDir,
                generated,
                errorListener,
                servicesOnly,
                entitiesOnly,
                Colls.emptyIterable()).withProjectName(projectName);
        if (errorListener.hasErrors()) return emptyList();
        return pb.withGeneratedSourceDir(generated).withSourcesDir(sources).withModels(mms).buildProject();
    }

    @SuppressWarnings("IfCanBeSwitch")
    final MmBuild parseArguments() {
        String outDir = "";
        int    i      = 0;
        while (i < args.length) {
            final String arg = args[i];
            // noinspection IfStatementWithTooManyBranches
            if ("-o".equals(arg)) outDir = args[++i];
            else if ("-g".equals(arg)) generatedDir = args[++i];
            else if ("-m".equals(arg)) mmDir = args[++i];
            else if ("-s".equals(arg)) sourcesDir = args[++i];
            else if ("-p".equals(arg)) printGen = true;
            else if ("-r".equals(arg)) servicesOnly = true;
            else if ("-e".equals(arg)) entitiesOnly = true;
            else if ("-n".equals(arg)) projectName = args[++i];
            else break;
            i++;
        }
        if (outDir.isEmpty() || mmDir.isEmpty()) {
            usage();
            System.exit(0);
        }
        if (i < args.length) models = ImmutableList.fromArray(args).subList(i, args.length);
        outputDir = new File(outDir);

        // Files.remove(outputDir);
        return this;
    }

    //~ Methods ......................................................................................................................................

    /** Main entry to invoke the project builder from the command line. */
    public static void main(String[] args)
        throws IOException
    {
        if (new MmBuild(args).parseArguments().build()) System.exit(1);
    }

    private static void usage() {
        System.out.println(
            "usage : -n <project name> -o <output dir> -g <generated sources dir> -s <sources dir> -m <meta models dir> " +
            "-p <print generated> -r <generate remote services only> -e <generate metamodels only> files...");
    }
}  // end class MmBuild
