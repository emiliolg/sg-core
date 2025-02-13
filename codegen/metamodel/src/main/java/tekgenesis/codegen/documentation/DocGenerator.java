
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
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableCollection;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.util.FileFilters;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModel;

import static tekgenesis.common.collections.Colls.filter;
import static tekgenesis.common.core.Constants.META_MODEL_EXT;
import static tekgenesis.common.util.Files.list;

/**
 * Utility class to generate html documentation.
 */
class DocGenerator {

    //~ Instance Fields ..............................................................................................................................

    private final List<File>           generated;
    private final File                 mmDir;
    private final List<MMDocGenerator> mmDocGenerators;
    private List<MetaModel>            models;
    private final String               name;

    private final ModelRepository repository;

    //~ Constructors .................................................................................................................................

    /**
     * @param  repository  : model repository to obtain metadata
     * @param  mmFiles     : the files of the module
     * @param  outputDir   : output directory where files will be generated
     * @param  mmDir       : directory where to look for mms to be documented.
     * @param  name        : the project name
     */
    DocGenerator(ModelRepository repository, ImmutableList<String> mmFiles, File outputDir, File mmDir, String name) {
        this.repository = repository;
        this.mmDir      = mmDir;
        this.name       = name;
        mmDocGenerators = new ArrayList<>();
        addGenerators(outputDir);
        models = new ArrayList<>();
        fillModels(mmFiles);
        generated = new ArrayList<>();
    }

    //~ Methods ......................................................................................................................................

    /** Generates the Html for all entities in the given Model Repository. */
    List<File> generate()
        throws IOException
    {
        if (models.isEmpty()) return generated;

        for (final File updated : generateDoc())
            generated.add(updated);

        return generated;
    }  // end method generate

    private void addGenerators(File outputDir) {
        mmDocGenerators.add(new EntityDocGenerator(outputDir));
        mmDocGenerators.add(new EnumDocGenerator(outputDir));
        mmDocGenerators.add(new StructTypeDocGenerator(outputDir));
    }

    private void fillModels(ImmutableList<String> mmFiles) {
        models = (mmFiles.isEmpty() ? list(mmDir, FileFilters.withExtension(META_MODEL_EXT)) : mmFiles).flatMap(s ->
                    getMetaModelsFiltered(repository.getModelsByFile(s))).toList();
    }

    private List<File> generateDoc()
        throws IOException
    {
        final ArrayList<File> doc = new ArrayList<>();
        for (final MMDocGenerator mmDocGenerator : mmDocGenerators) {
            final Seq<MetaModel> metaModels = filter(models, mmDocGenerator.getMMClass());
            for (final MetaModel model : metaModels)
                doc.add(mmDocGenerator.writeMetaModel(model));
            if (!metaModels.isEmpty()) doc.add(mmDocGenerator.writeIndex(name));
        }
        return doc;
    }

    @NotNull private List<MetaModel> getMetaModelsFiltered(ImmutableCollection<MetaModel> metaModels) {
        final List<MetaModel> modelList = new ArrayList<>();
        for (final MMDocGenerator generator : mmDocGenerators)
            modelList.addAll(filter(metaModels, generator.getMMClass()).toList());
        return modelList;
    }
}  // end class DocGenerator
