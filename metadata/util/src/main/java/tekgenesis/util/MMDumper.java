
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.IndentedWriter;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModel;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.util.Files.normalizeLineSeparators;

/**
 * Utility to dump MetaModels.
 */
public class MMDumper {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final List<MetaModel> models;
    @NotNull private final Preferences     preferences;
    @NotNull private final ModelRepository repository;

    //~ Constructors .................................................................................................................................

    private MMDumper(@NotNull String project, @NotNull ModelRepository repository) {
        this.repository     = repository;
        models              = new ArrayList<>();
        preferences         = new Preferences();
        preferences.project = project;
    }

    //~ Methods ......................................................................................................................................

    /** All all the models in the file to the set to be dumped. */
    public MMDumper file(String filePath) {
        return models(repository.getModelsByFile(filePath));
    }

    /** The dump will include inherit attributes. */
    public MMDumper full() {
        preferences.full = true;
        return this;
    }

    /** Add model to be dumped. */
    public MMDumper model(MetaModel model) {
        models.add(model);
        return this;
    }

    /** All the models to the set to be dumped. */
    @SuppressWarnings("WeakerAccess")
    public MMDumper models(Iterable<MetaModel> modelsToDump) {
        for (final MetaModel model : modelsToDump)
            models.add(model);
        return this;
    }

    /** Remote forms will be printed as external. */
    public MMDumper printRemoteAsExternal() {
        preferences.printRemoteAsExternal = true;
        return this;
    }
    /** Dump the models to the specified File. */
    public void toFile(File file) {
        try {
            toWriter(new FileWriter(file));
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Dump the models to a String. */
    @Override public String toString() {
        final StringWriter writer = new StringWriter();
        new RootModelDumper(repository, writer, preferences).dumpModels(models);
        writer.flush();
        return normalizeLineSeparators(writer.toString());
    }

    /** Dump the models to the specified Writer. */
    public void toWriter(Writer writer) {
        new RootModelDumper(repository, writer, preferences).dumpModels(models);
        try {
            writer.flush();
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Sets the length where the label will be wrapped. */
    public MMDumper withLabelWrapLength(int labelWrapLength) {
        preferences.labelWrapLength = labelWrapLength;
        return this;
    }

    /** Sets the length where a name will be wrapped. */
    public MMDumper withNameWrapLength(int nameWrapLength) {
        preferences.nameWrapLength = nameWrapLength;
        return this;
    }

    /** The dump will include the package statement. */
    public MMDumper withPackage() {
        preferences.includePackage = true;
        return this;
    }

    /** Set the Wrap Column. */
    public MMDumper withWrapColumn(int wrapColumn) {
        preferences.wrapColumn = wrapColumn;
        return this;
    }

    //~ Methods ......................................................................................................................................

    /** Create a dumper. */
    public static MMDumper createDumper(@NotNull ModelRepository repository) {
        return createDumper("", repository);
    }

    /** Create a dumper specifying project name. */
    public static MMDumper createDumper(@NotNull String project, @NotNull ModelRepository repository) {
        return new MMDumper(project, repository);
    }

    //~ Inner Classes ................................................................................................................................

    public static class Preferences {
        public boolean full;
        public boolean includePackage;

        public int labelWrapLength = DEFAULT_LABEL_WRAP_LENGTH;

        public int nameWrapLength = DEFAULT_NAME_WRAP_LENGTH;

        public boolean printRemoteAsExternal = false;
        public String  project               = "";

        public int wrapColumn = DEFAULT_WRAP_COLUMN;

        private static final int DEFAULT_NAME_WRAP_LENGTH  = 18;
        private static final int DEFAULT_LABEL_WRAP_LENGTH = 35;

        private static final int DEFAULT_WRAP_COLUMN = 130;
    }

    private static class RootModelDumper extends ModelDumper {
        private RootModelDumper(ModelRepository repository, Writer writer, Preferences preferences) {
            super(repository, new IndentedWriter(writer), preferences);
        }

        @Override void dumpModelOptions() {}

        private void dumpModels(Iterable<MetaModel> mms) {
            if (isIncludePackage()) printPackage(mms);
            for (final MetaModel metaModel : mms) {
                newLine();
                if (!metaModel.isInner()) createModelDumper(metaModel).dump().newLine();
            }
        }

        private void printPackage(Iterable<MetaModel> metaModel) {
            MetaModel model = null;
            for (final MetaModel mm : metaModel) {
                model = mm;
                if (mm instanceof Entity) break;
            }
            if (model != null) {
                final String schema = model.getSchema();
                final String domain = model.getDomain();
                print(MMToken.PACKAGE).space().print(domain);
                final boolean includeSchema = isFull() || !schema.equalsIgnoreCase(domain.substring(domain.lastIndexOf(".") + 1));
                if (!isEmpty(schema) && includeSchema) space().print(MMToken.SCHEMA).space().print(schema);
                semicolon().newLine();
            }
        }
    }
}  // end class MMDumper
