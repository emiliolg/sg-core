
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.etl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.media.Mimes;
import tekgenesis.common.service.HeaderNames;
import tekgenesis.form.BaseReflectedFormInstance;
import tekgenesis.form.FormExporter;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.core.Strings.split;

/**
 * Form exporter implementation.
 */
public class FormExporterImpl extends FormEtlImpl<FormExporter> implements FormExporter {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final List<Widget> fields;

    //~ Constructors .................................................................................................................................

    /** Form exporter constructor. */
    public FormExporterImpl(@NotNull final BaseReflectedFormInstance instance) {
        super(instance);
        fields = new ArrayList<>();
    }

    //~ Methods ......................................................................................................................................

    @Override public FormExporter fields(@NotNull Enum<?>... fs) {
        seq(fs).map(this::widgetByEnumOrdinal).into(fields);
        return cast(this);
    }

    @Override public FormExporter fields(@NotNull String... fs) {
        seq(fs).map(this::widgetByName).into(fields);
        return cast(this);
    }

    @Override public FormExporter fields(@NotNull HttpServletRequest req) {
        split(req.getHeader(HeaderNames.X_FIELDS), ',').map(this::widgetByName).into(fields);
        return cast(this);
    }

    @Override public void into(@NotNull HttpServletResponse resp)
        throws IOException
    {
        resp.setCharacterEncoding(charset.name());
        resp.setContentType(Mimes.getMimeType(builder.getExtension()));

        final OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), charset);
        writeTo(builder.createOutput(writer, charset), false);
    }

    @Override public void into(@NotNull File file)
        throws IOException
    {
        final OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), charset);
        writeTo(builder.createOutput(writer, charset), true);
    }

    @Override public void into(@NotNull OutputStream stream)
        throws IOException
    {
        final OutputStreamWriter writer = new OutputStreamWriter(stream, charset);
        writeTo(builder.createOutput(writer, charset), false);
    }

    private void writeTo(@NotNull FormOutput out, boolean close)
        throws IOException
    {
        out.write(instance.getModel(), getFormFields());
        if (close) out.close();
    }

    private Seq<Widget> getFormFields() {
        return immutable(fields);
    }
}  // end class FormExporterImpl
