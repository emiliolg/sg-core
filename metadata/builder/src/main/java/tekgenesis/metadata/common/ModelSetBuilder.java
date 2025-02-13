
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.common;

import java.util.*;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.exception.BuilderError;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.exception.DuplicateDefinitionException;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModel;
import tekgenesis.type.ModelLinker;

/**
 * Builder for a set of {@link MetaModel} metadata objects.
 *
 * <p>It collects {@link ModelBuilder} and in the build method builds the Types.</p>
 */
public class ModelSetBuilder {

    //~ Instance Fields ..............................................................................................................................

    private final Map<String, ModelBuilder<?, ?>> modelBuilders = new HashMap<>();

    //~ Methods ......................................................................................................................................

    /** Add a {@link ModelBuilder} to the set to be build. */
    public void addModel(@NotNull ModelBuilder<?, ?> e)
        throws DuplicateDefinitionException
    {
        if (modelBuilders.containsKey(e.getId())) throw new DuplicateDefinitionException(e.getFullName());
        modelBuilders.put(e.getId(), e);
    }

    /**
     * Build all Models and add them to the specified {@link ModelRepository}. It will call
     * recursively the build method of the models builders.
     */
    public void build(ModelRepository repository)
        throws BuilderException
    {
        final List<MetaModel> unsolved = new ArrayList<>();

        final ModelLinker linker = new ModelLinkerImpl(repository);

        for (final ModelBuilder<?, ?> builder : modelBuilders.values()) {
            final MetaModel model = builder.build();
            repository.add(model);
            if (!linker.link(model)) unsolved.add(model);

            for (final BuilderError builderError : builder.check()) {
                if (builderError instanceof BuilderException) throw ((BuilderException) builderError);
            }
        }

        // second pass to finish build this will resolve references to entities

        linker.setLastStage(true);
        for (final MetaModel model : unsolved)
            linker.link(model);
        modelBuilders.clear();
    }

    //~ Methods ......................................................................................................................................

    /** Build al Models. */
    public static ModelRepository buildAll(ModelBuilder<?, ?>... ds)
        throws BuilderException
    {
        final ModelSetBuilder b = new ModelSetBuilder();
        for (final ModelBuilder<?, ?> builder : ds)
            b.addModel(builder);
        final ModelRepository r = new ModelRepository();
        b.build(r);
        return r;
    }
}  // end class ModelSetBuilder
