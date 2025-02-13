
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

import tekgenesis.form.JsonConfiguration;

class JsonConfigurationImpl extends FormEtlConfigurationImpl<JsonConfiguration> implements JsonConfiguration {

    //~ Instance Fields ..............................................................................................................................

    private boolean prettyPrinting;

    //~ Methods ......................................................................................................................................

    @Override public JsonConfiguration configureFrom(@NotNull HttpServletRequest req) {
        if (Boolean.valueOf(req.getHeader(X_PRETTY_PRINT))) prettyPrinting();
        return super.configureFrom(req);
    }

    @Override public JsonConfiguration prettyPrinting() {
        prettyPrinting = true;
        return this;
    }

    boolean isPrettyPrinting() {
        return prettyPrinting;
    }
}
