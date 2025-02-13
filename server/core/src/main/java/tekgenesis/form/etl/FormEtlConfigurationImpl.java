
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.etl;

import javax.servlet.http.HttpServletRequest;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.FormEtlConfiguration;

import static tekgenesis.common.Predefined.cast;

class FormEtlConfigurationImpl<This extends FormEtlConfiguration<This>> implements FormEtlConfiguration<This> {

    //~ Instance Fields ..............................................................................................................................

    private boolean includeGeneratedFields;
    private boolean includeNullFields;

    //~ Methods ......................................................................................................................................

    @Override public This configureFrom(@NotNull HttpServletRequest req) {
        if (Boolean.valueOf(req.getHeader(X_SERIALIZE_GENERATED))) includeGeneratedFields();
        if (Boolean.valueOf(req.getHeader(X_SERIALIZE_NULLS))) includeNullFields();
        return cast(this);
    }

    @Override public This includeGeneratedFields() {
        includeGeneratedFields = true;
        return cast(this);
    }

    @Override public This includeNullFields() {
        includeNullFields = true;
        return cast(this);
    }

    boolean isIncludeGeneratedFields() {
        return includeGeneratedFields;
    }

    boolean isIncludeNullFields() {
        return includeNullFields;
    }
}
