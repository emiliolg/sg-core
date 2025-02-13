
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.MultiMap;
import tekgenesis.common.core.Option;
import tekgenesis.form.etl.FormExporterImpl;
import tekgenesis.form.etl.FormImporterImpl;
import tekgenesis.metadata.form.widget.Form;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.option;
import static tekgenesis.common.service.Parameters.queryStringToMap;
import static tekgenesis.form.ReflectedFormInstance.createFormInstance;

/**
 * Handler implementation class.
 */
public class HandlerImpl<T extends FormInstance<?>> implements Handler<T> {

    //~ Instance Fields ..............................................................................................................................

    private final Form                form;
    private BaseReflectedFormInstance instance;
    private final Option<String>      key;
    private final String              path;

    //~ Constructors .................................................................................................................................

    /** Handler constructor. */
    public HandlerImpl(Form form, String path, Option<String> key) {
        this.form = form;
        this.path = path;
        this.key  = key;
        instance  = null;
    }

    //~ Methods ......................................................................................................................................

    @Override public FormExporter exporter(@NotNull final Option<String> parameters) {
        return new FormExporterImpl(reflected(option(queryStringToMap(parameters.getOrNull()))));
    }

    @Override public FormImporter importer() {
        return new FormImporterImpl(reflected(EMPTY_PARAMETERS));
    }

    @Override public T instance() {
        return cast(reflected(EMPTY_PARAMETERS).getInstance());
    }

    @Override public String getMessage(@NotNull Action action) {
        return ((ActionImpl) action).getMsg().orElse("");
    }

    @Override public Option<String> getRouteKey() {
        return key;
    }

    @Override public String getRoutePath() {
        return path;
    }

    /** Ensure reflected instance. */
    @NotNull private BaseReflectedFormInstance reflected(@NotNull final Option<MultiMap<String, String>> parameters) {
        if (instance == null) instance = createFormInstance(form, key.getOrNull(), parameters.getOrNull());
        return instance;
    }

    //~ Static Fields ................................................................................................................................

    private static final Option<MultiMap<String, String>> EMPTY_PARAMETERS = Option.empty();
}  // end class HandlerImpl
