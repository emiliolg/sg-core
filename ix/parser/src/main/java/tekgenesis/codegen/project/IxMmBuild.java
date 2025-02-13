
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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.util.Files;
import tekgenesis.dsl.schema.SchemaCodeGenerator;

import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.common.core.Predicates.matches;

/**
 * Implementation of MMBuild that also process 'sc' files.
 */
@SuppressWarnings("WeakerAccess")
public class IxMmBuild extends MmBuild {

    //~ Constructors .................................................................................................................................

    private IxMmBuild(String[] args) {
        super(args);
    }

    //~ Methods ......................................................................................................................................

    @Override List<File> doBuild(Seq<String> mms, File sources, File genDir)
        throws IOException
    {
        final List<File>        generated = new ArrayList<>();
        final Predicate<String> scs       = matches(".*.sc");

        for (final String sc : mms.filter(scs)) {
            final File schemaFile = new File(sc);

            generated.addAll(new SchemaCodeGenerator(schemaFile, sources, genDir).generate());
            // final File classesDir = new File(outputDir,sc.substring(sc.indexOf("mm"),sc.lastIndexOf("/")));
            // Files.copyDirectory(schemaFile.getParentFile(),classesDir);
        }

        final List<File> c = super.doBuild(mms.filter(scs.negate()), sources, genDir);
        generated.addAll(c);

        // noinspection DuplicateStringLiteralInspection
        final File ixEntities = new File(outputDir, "META-INF/ix-entity-list");

        Files.writeLines(ixEntities, map(generated, IxMmBuild::writeLineFor));

        return generated;
    }

    //~ Methods ......................................................................................................................................

    /** Main entry to invoke the project builder from the command line. */
    @SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
    public static void main(String[] args)
        throws IOException
    {
        if (new IxMmBuild(args).parseArguments().build()) System.exit(1);
    }

    /** Write line for ix entities file. */
    @Nullable public static String writeLineFor(final File value) {
        final String absolutePath = value.getAbsolutePath();
        final int    mm           = absolutePath.indexOf("mm");
        final int    base         = absolutePath.lastIndexOf("Base");
        return base == -1 ? null : absolutePath.substring(mm + 3, base).replace(File.separatorChar, '.').replace(".g.", ".");
    }
}  // end class IxMmBuild
