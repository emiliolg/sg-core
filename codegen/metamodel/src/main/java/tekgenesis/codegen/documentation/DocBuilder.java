
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
import tekgenesis.common.core.Constants;
import tekgenesis.mmcompiler.builder.BuilderErrorListener;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.codegen.project.ProjectBuilder.loadFromClassPath;

/**
 * A utility builder for documentation.
 */
class DocBuilder {

    //~ Instance Fields ..............................................................................................................................

    private final File                  mmDir;
    private final ImmutableList<String> mmFiles;
    private final ModelRepository       modelRepository;
    private final String                name;
    private final File                  outputDir;

    //~ Constructors .................................................................................................................................

    DocBuilder(File mmDir, ImmutableList<String> models, File outputDir, File generated, String name, BuilderErrorListener errorListener) {
        this.outputDir  = outputDir;
        this.mmDir      = mmDir;
        this.name       = name;
        modelRepository = loadFromClassPath(getClass().getClassLoader(), mmDir, errorListener);
        mmFiles         = models.filter(m -> m != null && m.endsWith(Constants.META_MODEL_EXT)).toList();
    }

    //~ Methods ......................................................................................................................................

    public List<File> buildProject()
        throws IOException
    {
        return new DocGenerator(modelRepository, mmFiles, outputDir, mmDir, name).generate();
    }
}
