
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.compiler.builder;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.intellij.openapi.util.io.FileUtilRt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.ModuleChunk;
import org.jetbrains.jps.builders.DirtyFilesHolder;
import org.jetbrains.jps.builders.java.JavaSourceRootDescriptor;
import org.jetbrains.jps.incremental.*;
import org.jetbrains.jps.incremental.fs.CompilationRound;
import org.jetbrains.jps.incremental.messages.BuildMessage;
import org.jetbrains.jps.incremental.messages.CompilerMessage;
import org.jetbrains.jps.model.module.JpsModule;
import org.jetbrains.jps.model.module.JpsModuleSourceRoot;

import tekgenesis.codegen.project.IxMmBuild;
import tekgenesis.common.collections.MultiMap;
import tekgenesis.common.util.Files;
import tekgenesis.dsl.schema.SchemaCodeGenerator;
import tekgenesis.lang.mm.MetaModelJpsUtils;
import tekgenesis.lang.mm.compiler.model.MetaModelFacetSettings;

import static org.jetbrains.jps.incremental.messages.BuildMessage.Kind.ERROR;
import static org.jetbrains.jps.incremental.messages.BuildMessage.Kind.INFO;

import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.lang.mm.MetaModelJpsUtils.META_MODEL_SOURCES_PATH;
import static tekgenesis.lang.mm.compiler.CompilerConstants.*;
import static tekgenesis.lang.mm.compiler.builder.MetaModelSourceGeneratorBuilder.NO_FACET_EXTENSION;
import static tekgenesis.lang.mm.compiler.builder.MetaModelSourceGeneratorBuilder.NO_METAMODEL_SOURCE_ROOT;

/**
 * Ix Jps sources generation builder.
 */
public class IxSourceGeneratorBuilder extends ModuleLevelBuilder {

    //~ Constructors .................................................................................................................................

    protected IxSourceGeneratorBuilder() {
        super(BuilderCategory.SOURCE_GENERATOR);
    }

    //~ Methods ......................................................................................................................................

    @Override public ExitCode build(final CompileContext context, ModuleChunk chunk,
                                    DirtyFilesHolder<JavaSourceRootDescriptor, ModuleBuildTarget> dirty, OutputConsumer output)
        throws ProjectBuildException, IOException
    {
        final Map<JpsModule, MetaModelFacetSettings> modules = computeModules(chunk.getModules());
        if (modules.isEmpty()) return ExitCode.NOTHING_DONE;

        final MultiMap<ModuleBuildTarget, File> files = MultiMap.createMultiMap();

        dirty.processDirtyFiles((target, file, descriptor) -> {
            if (FileUtilRt.extensionEquals(file.getName(), SC_EXT)) files.put(target, file);
            return true;
        });

        MetaModelSourceGeneratorBuilder.computeRemovedFiles(chunk, dirty, files, ".*\\.sc");

        return !files.isEmpty() ? generateSourceForFiles(context, files, modules, output) ? ExitCode.OK : ExitCode.ABORT : ExitCode.NOTHING_DONE;
    }

    @Override public List<String> getCompilableFileExtensions() {
        return Collections.singletonList(SC_EXT);
    }

    @NotNull @Override public String getPresentableName() {
        return "Ix Builder";
    }

    private boolean generateSourceForFiles(CompileContext context, MultiMap<ModuleBuildTarget, File> dirty,
                                           Map<JpsModule, MetaModelFacetSettings> modules, OutputConsumer output) {
        for (final Map.Entry<ModuleBuildTarget, Collection<File>> entry : dirty.asMap().entrySet()) {
            final Collection<File>  files  = entry.getValue();
            final ModuleBuildTarget target = entry.getKey();
            final JpsModule         module = target.getModule();

            final MetaModelFacetSettings settings = modules.get(module);
            if (settings == null) {
                processErrorMessage(context, String.format(NO_FACET_EXTENSION, module.getName()));
                return false;
            }

            final JpsModuleSourceRoot mm = getMetaModelSourceDirectory(module);
            if (mm == null) {
                processErrorMessage(context, String.format(NO_METAMODEL_SOURCE_ROOT, module.getName()));
                return false;
            }

            processInfoMessage(context, String.format("Generating sources for %s IdeaFix files on module '%s'.", files.size(), module.getName()));

            try {
                final List<File> generated = new ArrayList<>();
                for (final File sc : files) {
                    final List<File> updated = new SchemaCodeGenerator(sc,
                            new File(mm.getFile().getParent(), JAVA_EXT),
                            new File(settings.getGeneratedSourcesDir(), META_MODEL_EXT)).generate(true);
                    for (final File f : updated)
                        FSOperations.markDirty(context, CompilationRound.CURRENT, f);
                    // output.registerOutputFile(target, f, Colls.singleton(sc.getPath()).toList());
                    generated.addAll(updated);
                }
                // noinspection DuplicateStringLiteralInspection
                final File ixEntities = new File(target.getOutputDir(), "META-INF/ix-entity-list");

                Files.writeLines(ixEntities, map(generated, IxMmBuild::writeLineFor));
            }
            catch (final IOException e) {
                processErrorMessage(context, String.format(MetaModelSourceGeneratorBuilder.AN_ERROR_OCCURRED, module.getName(), e.getMessage()));
                return false;
            }
        }

        return true;
    }  // end method generateSourceForFiles

    private void processErrorMessage(CompileContext context, String message) {
        processMessage(context, message, ERROR);
    }

    private void processInfoMessage(CompileContext context, String message) {
        processMessage(context, message, INFO);
    }

    private void processMessage(CompileContext context, String message, BuildMessage.Kind kind) {
        context.processMessage(new CompilerMessage(META_MODEL_FACET_TYPE_ID, kind, message));
    }

    private JpsModuleSourceRoot getMetaModelSourceDirectory(JpsModule module) {
        for (final JpsModuleSourceRoot sourceRoot : module.getSourceRoots()) {
            if (sourceRoot.getUrl().endsWith(META_MODEL_SOURCES_PATH)) return sourceRoot;
        }
        return null;
    }

    //~ Methods ......................................................................................................................................

    @NotNull private static Map<JpsModule, MetaModelFacetSettings> computeModules(@NotNull Collection<JpsModule> modules) {
        final Map<JpsModule, MetaModelFacetSettings> result = new HashMap<>();

        for (final JpsModule module : modules) {
            final MetaModelFacetSettings extension = MetaModelJpsUtils.getFacetSettings(module);
            if (extension != null) result.put(module, extension);
        }

        return result;
    }
}  // end class IxSourceGeneratorBuilder
