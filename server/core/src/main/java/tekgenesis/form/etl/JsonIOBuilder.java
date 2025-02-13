
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.etl;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.json.JsonMapping;
import tekgenesis.form.JsonConfiguration;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.form.etl.FormEtlImpl.*;

class JsonIOBuilder implements FormIOBuilder<JsonConfiguration> {

    //~ Instance Fields ..............................................................................................................................

    private final JsonConfigurationImpl configuration;

    //~ Constructors .................................................................................................................................

    JsonIOBuilder() {
        configuration = new JsonConfigurationImpl();
    }

    //~ Methods ......................................................................................................................................

    @Override public JsonConfiguration configure() {
        return configuration;
    }

    @Override public FormInput createInput(@NotNull Reader reader, @NotNull Charset charset, @NotNull FormModel model) {
        return new FormInputImpl(configuration, reader, charset, model);
    }

    @Override public FormOutput createOutput(@NotNull Writer writer, @NotNull Charset charset) {
        return new FormOutputImpl(configuration, writer, charset);
    }

    @Override public String getExtension() {
        return "json";
    }

    //~ Inner Classes ................................................................................................................................

    private abstract static class AbstractFormIO<T extends Closeable> implements Closeable {
        @NotNull final Charset               charset;
        @NotNull final T                     closeable;
        @NotNull final JsonConfigurationImpl conf;

        private AbstractFormIO(@NotNull JsonConfigurationImpl conf, @NotNull Charset charset, @NotNull T closeable) {
            this.conf      = conf;
            this.charset   = charset;
            this.closeable = closeable;
        }

        @Override public void close()
            throws IOException
        {
            closeable.close();
        }

        ObjectMapper mapper() {
            final SimpleModule module = new SimpleModule("JsonHandler", new Version(1, 0, 0, null, null, null));
            registerInto(module);
            final ObjectMapper mapper = JsonMapping.json();
            mapper.registerModule(module);
            return mapper;
        }

        /** Register serialization artifact into module. */
        abstract void registerInto(@NotNull SimpleModule module);
    }

    private static class FormInputImpl extends AbstractFormIO<Reader> implements FormInput {
        @NotNull private final FormModel model;

        private FormInputImpl(@NotNull JsonConfigurationImpl c, @NotNull Reader reader, @NotNull Charset charset, @NotNull FormModel model) {
            super(c, charset, reader);
            this.model = model;
        }

        @Override public void read()
            throws IOException
        {
            mapper().readValue(closeable, FormModel.class);
        }

        @Override protected void registerInto(@NotNull SimpleModule module) {
            final FormJsonDeserializer deserializer = new FormJsonDeserializer(model);
            module.addDeserializer(FormModel.class, deserializer);
        }
    }

    private static class FormOutputImpl extends AbstractFormIO<Writer> implements FormOutput {
        @NotNull private final FormJsonSerializer serializer;

        private FormOutputImpl(@NotNull JsonConfigurationImpl c, @NotNull Writer writer, @NotNull Charset charset) {
            super(c, charset, writer);
            serializer = new FormJsonSerializer(conf);
        }

        @Override public void write(@NotNull FormModel model, @NotNull Seq<Widget> widgets)
            throws IOException
        {
            if (!widgets.isEmpty()) serializer.setSelection(widgets);
            final ObjectMapper mapper = mapper();
            final ObjectWriter writer = conf.isPrettyPrinting() ? mapper.writerWithDefaultPrettyPrinter() : mapper.writer();
            writer.writeValue(closeable, model);
        }

        @Override void registerInto(@NotNull SimpleModule module) {
            module.addSerializer(FormModel.class, serializer);
        }
    }
}  // end class JsonIOBuilder
