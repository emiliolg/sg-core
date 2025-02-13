
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.export;

import java.util.Locale;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Enumeration;

import static java.lang.String.valueOf;
import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getDateTimeInstance;

import static tekgenesis.common.env.context.Context.getContext;

/**
 * Interface that all form table exporter should implement.
 */
public interface FormTableExporter {

    //~ Instance Fields ..............................................................................................................................

    String CURRENT_ROW_WAS_CLOSED = "Current row was closed!";

    //~ Methods ......................................................................................................................................

    /** Builds and exports to the given OutputStream. */
    void build();

    /** Adds a header. */
    FormTableHeaderExporter header();

    /** Begins a row. */
    FormTableRowExporter row();

    /** Adds title to document. */
    void title(@NotNull final String title);

    //~ Inner Interfaces .............................................................................................................................

    interface FormTableHeaderExporter {
        /** Adds a header cell. */
        FormTableHeaderExporter addContent(@NotNull final String label);
    }

    interface FormTableRowExporter {
        /** Adds a cell a row. */
        FormTableRowExporter addContent(@NotNull final String content);

        /** Adds a cell a row. */
        default FormTableRowExporter addContent(@Nullable final Object content) {
            if (content == null) return addContent("");

            final Locale locale = getContext().getLocale();
            final String result;
            if (content instanceof DateOnly) result = ((DateOnly) content).format(getDateInstance(3, locale));
            else if (content instanceof DateTime) result = ((DateTime) content).format(getDateTimeInstance(3, 3, locale));
            else if (content instanceof Enumeration<?, ?>) result = ((Enumeration<?, ?>) content).label();
            else result = valueOf(content);

            return addContent(result.replace("\n", " "));
        }
    }
}  // end interface FormTableExporter
