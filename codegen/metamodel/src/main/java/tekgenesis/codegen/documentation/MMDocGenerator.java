
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.documentation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.function.Consumer;

import com.samskivert.mustache.Mustache;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.MultiMap;
import tekgenesis.common.core.Strings;
import tekgenesis.common.util.Files;
import tekgenesis.type.MetaModel;

import static tekgenesis.codegen.common.MMCodeGenConstants.DOC_EXT;
import static tekgenesis.codegen.common.MMCodeGenConstants.METAMODEL;
import static tekgenesis.codegen.common.MMCodeGenConstants.TITLE;
import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.collections.Maps.linkedHashMap;
import static tekgenesis.common.core.Strings.capitalizeFirst;
import static tekgenesis.common.core.Tuple.tuple;

/**
 * Abstract class generalizing MM documentation generator.
 */
@SuppressWarnings({ "AbstractMethodCallInConstructor", "DuplicateStringLiteralInspection" })
abstract class MMDocGenerator {

    //~ Instance Fields ..............................................................................................................................

    private final Mustache.Compiler        compiler;
    private final MultiMap<String, String> mmsMap;
    private final File                     modelOutputdir;
    private final File                     outputDir;

    //~ Constructors .................................................................................................................................

    MMDocGenerator(@NotNull File outputDir) {
        mmsMap         = MultiMap.createMultiMap();
        this.outputDir = new File(outputDir, getInnerFolder());
        modelOutputdir = new File(outputDir, METAMODEL);
        compiler       = Mustache.compiler().withFormatter(String::valueOf).emptyStringIsFalse(true).zeroIsFalse(true).nullValue("");
    }

    //~ Methods ......................................................................................................................................

    void renderMustache(Writer writer, Reader reader, Object context) {
        compiler.compile(reader).execute(context, writer);
    }

    File writeIndex(String project)
        throws IOException
    {
        final File file = new File(outputDir, notEmpty(project.toLowerCase(), "doc") + "-index.html");
        if (!outputDir.exists()) outputDir.mkdirs();
        if (!file.exists()) file.createNewFile();
        writeFile(file,
            INDEX_TEMPLATE,
            fileManager ->
                compiler.compile(fileManager.reader)
                        .execute(
                            linkedHashMap(tuple(TITLE, capitalizeFirst(project)),
                                tuple(METAMODEL, capitalizeFirst(getInnerFolder())),
                                tuple("entrySet", mmsMap.asMap().entrySet())),
                            fileManager.writer));
        return file;
    }

    File writeMetaModel(MetaModel metaModel)
        throws IOException
    {
        final String path     = metaModel.getFullName().replace('.', File.separatorChar) + DOC_EXT;
        final String fileName = path.substring(path.lastIndexOf(File.separatorChar) + 1);
        final String folder   = path.substring(0, path.lastIndexOf(File.separatorChar) + 1);

        mmsMap.put(folder.substring(0, folder.length() - 1), metaModel.getName());

        final File dir = new File(modelOutputdir, folder);

        if (!dir.exists()) dir.mkdirs();

        final File file = new File(dir, fileName);
        if (!file.exists()) file.createNewFile();
        writeFile(file, getMustacheTemplate(), fileManager -> getFinalHtml(fileManager.writer, fileManager.reader, metaModel));

        return file;
    }

    abstract void getFinalHtml(@NotNull Writer writer, @NotNull Reader reader, MetaModel metaModel);
    @NotNull abstract String getInnerFolder();
    @NotNull abstract <T extends MetaModel> Class<T> getMMClass();
    @NotNull abstract String getMustacheTemplate();

    @NotNull String getTargetDir(String entity) {
        final int           count   = Strings.count(entity, '.') + 1;
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++)
            builder.append("../");
        return builder.append(METAMODEL + "/").toString();
    }

    private void writeFile(File file, String template, Consumer<FileManager> block)
        throws IOException
    {
        final String templateDir  = DOC_TEMPLATES_DIR + "/" + template;
        final URL    templateFile = getClass().getClassLoader().getResource(templateDir);
        if (templateFile == null) throw new MMDocGenerationException(templateDir);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(templateFile.openStream()));
        final FileWriter     writer = new FileWriter(file);
        block.accept(new FileManager(writer, reader));
        Files.close(reader);
        Files.close(writer);
    }

    //~ Static Fields ................................................................................................................................

    private static final String INDEX_TEMPLATE    = "mm-doc-index.mustache";
    private static final String DOC_TEMPLATES_DIR = "mmdocumentation";

    //~ Inner Classes ................................................................................................................................

    private static class FileManager {
        private final Reader     reader;
        private final FileWriter writer;

        FileManager(FileWriter writer, Reader reader) {
            this.writer = writer;
            this.reader = reader;
        }
    }
}  // end class MMDocGenerator
