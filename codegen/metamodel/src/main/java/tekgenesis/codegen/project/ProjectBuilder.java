
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
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.util.FileFilters;
import tekgenesis.mmcompiler.ModelRepositoryLoader;
import tekgenesis.mmcompiler.builder.BuilderErrorListener;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.core.Constants.META_MODEL_EXT;
import static tekgenesis.common.util.Files.list;

/**
 * Utility class to invoke the Project Builder.
 */
@SuppressWarnings({ "WeakerAccess", "ConstructorWithTooManyParameters" })  // Builder
public class ProjectBuilder {

    //~ Instance Fields ..............................................................................................................................

    private final boolean entitiesOnly;

    private boolean forceBase;

    private File                  generatedSourcesDir;
    private File                  javaSourcesDir;
    private final File            mmDir;
    private final List<String>    mmFiles;
    private final ModelRepository modelRepository;
    private final File            outputDir;
    private String                project;
    private boolean               remoteServices;
    private final Iterable<File>  resourcesDirs;

    //~ Constructors .................................................................................................................................

    /** Create a Project Builder. */
    public ProjectBuilder(File mmDir, File outputDir, File generatedSourcesDir) {
        this(mmDir, outputDir, generatedSourcesDir, new BuilderErrorListener.Default(), false, false, Collections.emptyList());
    }

    /** Create a Project Builder. */
    public ProjectBuilder(ModelRepository repository, File mmDir, @Nullable File outputDir, File generatedSourcesDir, Iterable<File> resourcesDirs) {
        this(repository, mmDir, outputDir, generatedSourcesDir, false, false, resourcesDirs);
    }

    /** Create a Project Builder. */
    public ProjectBuilder(File mmDir, File outputDir, File generatedSourcesDir, BuilderErrorListener errorListener, boolean remoteServices,
                          boolean entitiesOnly, Iterable<File> resourcesDirs) {
        this(loadFromClassPath(ProjectBuilder.class.getClassLoader(), mmDir, errorListener),
            mmDir,
            outputDir,
            generatedSourcesDir,
            remoteServices,
            entitiesOnly,
            resourcesDirs);
    }

    /** Create a Project Builder. */
    ProjectBuilder(ModelRepository repository, File mmDir, @Nullable File outputDir, File generatedSourcesDir, boolean remoteServices,
                   boolean entitiesOnly, Iterable<File> resourcesDirs) {
        this.outputDir      = outputDir;
        this.mmDir          = mmDir;
        this.remoteServices = remoteServices;
        this.entitiesOnly   = entitiesOnly;
        this.resourcesDirs  = resourcesDirs;

        // Defaults
        mmFiles = new ArrayList<>();
        final File sourceBase = mmDir.getParentFile();
        javaSourcesDir           = new File(sourceBase, "java");
        this.generatedSourcesDir = generatedSourcesDir;
        forceBase                = false;
        project                  = "";
        modelRepository          = repository;
    }

    //~ Methods ......................................................................................................................................

    /** Build the list of specified MM files. Return all updated class files on build. */
    public List<File> buildProject()
        throws IOException
    {
        final ProjectCodeGenerator generator = new ProjectCodeGenerator(project,
                modelRepository,
                javaSourcesDir,
                generatedSourcesDir,
                outputDir,
                mmDir,
                resourcesDirs);
        generator.withModels(listMetaModels());
        if (forceBase || remoteServices) generator.withForceBaseGeneration();
        if (entitiesOnly) generator.withMetaModelsOnly();
        else if (remoteServices) generator.withRemoteServicesOnlyGeneration();
        return generator.generate();
    }

    /** Specify force base generation. */
    public ProjectBuilder withForceBaseGeneration() {
        forceBase = true;
        return this;
    }

    /** Sets the java source directory. */
    @SuppressWarnings("UnusedReturnValue")  // Builder
    public ProjectBuilder withGeneratedSourceDir(File dir) {
        generatedSourcesDir = dir;
        return this;
    }

    /** Specify the list of MM files. */
    @SuppressWarnings("UnusedReturnValue")  // Builder
    public ProjectBuilder withModels(Iterable<String> models) {
        for (final String model : models)
            if (model.endsWith(META_MODEL_EXT)) mmFiles.add(model);
        return this;
    }

    /** Specify project name. */
    public ProjectBuilder withProjectName(@NotNull String projectName) {
        project = projectName;
        return this;
    }

    /** Specify remote services generation. */
    public ProjectBuilder withRemoteServices() {
        remoteServices = true;
        return this;
    }

    /** Sets the java source directory. */
    public ProjectBuilder withSourcesDir(File dir) {
        javaSourcesDir = dir;
        return this;
    }

    /** Sets the java source directory. */
    @SuppressWarnings("UnusedReturnValue")  // builder, it could be eventually used.
    public ProjectBuilder withSourcesDir(String javaSourceDir) {
        return withSourcesDir(new File(javaSourceDir));
    }

    /** Returns the generatedSources Dir. */
    public File getGeneratedSourcesDir() {
        return generatedSourcesDir;
    }

    private Seq<String> listMetaModels() {
        if (mmFiles.isEmpty()) mmFiles.addAll(list(mmDir, FileFilters.withExtension(META_MODEL_EXT)));

        // final int prefix = mmDir.getPath().length() + 1;
        //
        // return Colls.map(mmFiles, new Function<String, String>() {
        // @Override public String apply(String value) { return value.substring(prefix); }
        // });

        return immutable(mmFiles);
    }

    //~ Methods ......................................................................................................................................

    //
    // /** Return the generated Sources dir relative to the target directory. */
    // public static File generatedSourcesDir(File targetDir, String mainOrTest) {
    // return new File(new File(targetDir, GENERATED_SOURCES), mainOrTest.isEmpty() ? "mm" : mainOrTest + "/mm");
    // }

    /** Loads a ModelRepository from a classloader and adds models from mmDir. */
    public static ModelRepository loadFromClassPath(ClassLoader classLoader, File mmDir, BuilderErrorListener errorListener) {
        final ModelRepositoryLoader loader = new ModelRepositoryLoader(classLoader);
        loader.withErrorListener(errorListener);

        final ModelRepository repository = loader.build();
        loader.addModels(mmDir);
        return repository;
    }
}  // end class ProjectBuilder
