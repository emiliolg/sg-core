
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

import javax.servlet.http.HttpServletRequest;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.BaseReflectedFormInstance;
import tekgenesis.form.FormImporter;

/**
 * Form importer implementation.
 */
public class FormImporterImpl extends FormEtlImpl<FormImporter> implements FormImporter {

    //~ Constructors .................................................................................................................................

    /** Form importer constructor. */
    public FormImporterImpl(@NotNull final BaseReflectedFormInstance instance) {
        super(instance);
    }

    //~ Methods ......................................................................................................................................

    @Override public void from(@NotNull File file)
        throws IOException
    {
        from(new FileInputStream(file));
    }

    @Override public void from(@NotNull HttpServletRequest req)
        throws IOException
    {
        from(req.getInputStream());
    }

    @Override public void from(@NotNull InputStream stream)
        throws IOException
    {
        readFrom(builder.createInput(new InputStreamReader(stream, charset), charset, instance.getModel()));
    }

    private void readFrom(@NotNull FormInput input)
        throws IOException
    {
        input.read();
        input.close();
    }
}
