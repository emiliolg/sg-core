
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import javax.servlet.http.HttpServletRequest;

import org.jetbrains.annotations.NotNull;

/**
 * Form Etl Configuration.
 */
public interface FormEtlConfiguration<This extends FormEtlConfiguration<This>> {

    //~ Instance Fields ..............................................................................................................................

    String X_SERIALIZE_GENERATED = "X-Serialize-Gen";

    String X_SERIALIZE_NULLS = "X-Serialize-Null";

    //~ Methods ......................................................................................................................................

    /** Configure from request. */
    This configureFrom(@NotNull final HttpServletRequest req);

    /** Include fields with auto-generated id. */
    This includeGeneratedFields();

    /** Include fields with null value. */
    This includeNullFields();
}
