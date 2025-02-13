
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Builder;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.logging.Logger;
import tekgenesis.lexer.CharSequenceStream;
import tekgenesis.lexer.FileStream;
import tekgenesis.lexer.UrlStream;
import tekgenesis.metadata.exception.BuilderError;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.mmcompiler.builder.BuilderErrorListener;
import tekgenesis.mmcompiler.builder.BuilderFromAST;
import tekgenesis.mmcompiler.parser.MetaModelCompiler;
import tekgenesis.parser.Diagnostic;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.common.core.Constants.META_MODEL_EXT;
import static tekgenesis.common.util.FileFilters.withExtension;
import static tekgenesis.common.util.Files.list;
import static tekgenesis.common.util.Resources.readResources;

/**
 * Loads a MetaModel Repository.
 */
public class ModelRepositoryLoader implements Builder<ModelRepository> {

    //~ Instance Fields ..............................................................................................................................

    private BuilderErrorListener errorListener = new BuilderErrorListener.Default();

    private final List<CharSequenceStream> modelStreams;
    private final ModelRepository          repository;

    //~ Constructors .................................................................................................................................

    /** Prepares a ModelRepositoryLoader for further models or streams loading into repository. */
    public ModelRepositoryLoader(@NotNull final ModelRepository seed) {
        repository   = seed;
        modelStreams = new ArrayList<>();
    }

    /** Create a ModelRepositoryLoader to load all models from the specified class loader. */
    public ModelRepositoryLoader(ClassLoader classLoader) {
        this(classLoader, new ModelRepository());
    }

    /** Create a ModelRepositoryLoader to load all models from the specified path. */
    public ModelRepositoryLoader(@NotNull final File path) {
        this(path, new ModelRepository());
    }

    /**
     * Create a ModelRepositoryLoader to load all models from the specified class loader into the
     * seed repository.
     */
    public ModelRepositoryLoader(ClassLoader classLoader, @NotNull final ModelRepository seed) {
        this(seed);
        loadStreams(classLoader);
    }

    /**
     * Create a ModelRepositoryLoader to load all models from the specified path into the seed
     * repository.
     */
    public ModelRepositoryLoader(@NotNull final File path, @NotNull final ModelRepository seed) {
        this(seed);
        if (path.isDirectory()) list(path, withExtension(META_MODEL_EXT)).map(ModelRepositoryLoader::createStream).into(modelStreams);
        else modelStreams.add(createStream(path.getPath()));
    }

    //~ Methods ......................................................................................................................................

    /**
     * Add all the models under the specified directory to this Repository if they do not exists.
     */
    public void addModels(@NotNull File mmDir) {                     //
        loadMetaData(list(mmDir, withExtension(META_MODEL_EXT))      //
            .filter(mm -> repository.getModelsByFile(mm).isEmpty())  //
            .map(ModelRepositoryLoader::createStream));
    }

    @Override public ModelRepository build() {
        return loadMetaData(seq(modelStreams));
    }

    /** Load given class-loaders streams. */
    public void loadStreams(ClassLoader classLoader) {
        for (final String mm : readResources(classLoader, META_MODEL_LIST)) {
            final UrlStream stream = createStream(classLoader, mm);
            if (stream != null) modelStreams.add(stream);
            else logger.error("Cannot load MM Resource: " + mm);
        }
    }

    /** Specified an Error Listener for the build. */
    @SuppressWarnings("UnusedReturnValue")
    public ModelRepositoryLoader withErrorListener(BuilderErrorListener e) {
        errorListener = e;
        return this;
    }

    private ModelRepository loadMetaData(Seq<CharSequenceStream> streams) {
        final BuilderFromAST builder = new BuilderFromAST(repository, errorListener);
        return builder.build(streams.map(new Parse()));
    }

    //~ Methods ......................................................................................................................................

    /** Load a {@link ModelRepository} from the classpath. */
    public static ModelRepository loadFromClasspath() {
        final ModelRepositoryLoader builder = new ModelRepositoryLoader(Thread.currentThread().getContextClassLoader());
        return builder.build();
    }

    @NotNull private static CharSequenceStream createStream(final String value) {
        try {
            return new FileStream(value);
        }
        catch (final IOException e) {
            logger.error("Cannot create stream for: " + value, e);
            throw new UncheckedIOException(e);
        }
    }

    @Nullable private static UrlStream createStream(ClassLoader classLoader, String mm) {
        final URL url = classLoader.getResource(mm);
        try {
            if (url != null) return new UrlStream(url, Constants.UTF8);
        }
        catch (final IOException e) {
            logger.error(e);
        }
        return null;
    }

    //~ Static Fields ................................................................................................................................

    @NonNls public static final String META_MODEL_LIST = "META-INF/meta-model-files";

    private static final Logger logger = Logger.getLogger(ModelRepositoryLoader.class);

    //~ Inner Classes ................................................................................................................................

    private class Parse implements Function<CharSequenceStream, Tuple<String, MetaModelAST>> {
        @Nullable @Override public Tuple<String, MetaModelAST> apply(CharSequenceStream stream) {
            // Apparently the are some weird scenarios where streams might be null (ex. new empty projects).
            if (stream == null) return null;

            final MetaModelCompiler mm  = new MetaModelCompiler(stream, stream.getSourceName());
            final MetaModelAST      ast = mm.getAST();
            // todo polish this....
            for (final Diagnostic error : mm.getMessages())
                // logger.error(error.getFormattedMessage());
                errorListener.error(new BuilderError() {
                        @Override public String getMessage() {
                            return error.getFormattedMessage();
                        }

                        @Nullable @Override public String getModelName() {
                            return null;
                        }
                    });
            return Tuple.tuple(mm.getSourceName(), ast);
        }
    }
}  // end class ModelRepositoryLoader
