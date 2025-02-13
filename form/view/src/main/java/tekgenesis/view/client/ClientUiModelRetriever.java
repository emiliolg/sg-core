
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.metadata.form.UiModelRetriever;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.UiModel;

import static tekgenesis.common.core.Option.ofNullable;
import static tekgenesis.metadata.form.exprs.Expressions.bind;

/**
 * Wraps a client side cache as a {@link UiModelRetriever model retriever}.
 */
public class ClientUiModelRetriever implements UiModelRetriever {

    //~ Instance Fields ..............................................................................................................................

    final Map<QName, UiModel> cache;

    //~ Constructors .................................................................................................................................

    ClientUiModelRetriever(@NotNull final Map<QName, UiModel> cache) {
        this.cache = cache;
    }

    //~ Methods ......................................................................................................................................

    /** Clear entries that should be deleted once the form is un-loaded. */
    public void clearTransient() {
        final List<QName> keys = new ArrayList<>();
        for (final UiModel form : cache.values()) {
            if (!form.isCached()) keys.add(form.getKey());
        }
        for (final QName key : keys)
            cache.remove(key);
    }

    /** Update retriever's cache with given {@link Form}. */
    public void put(@NotNull final Form form) {
        if (doPut(form)) bind(this, form);
    }

    /** Update retriever's cache with given {@link UiModel}s. */
    public void putAll(@NotNull final Iterable<UiModel> references) {
        final List<UiModel> added = new ArrayList<>();
        for (final UiModel model : references) {
            if (doPut(model)) added.add(model);
        }

        for (final UiModel model : added)
            bind(this, model);
    }

    @NotNull @Override public final Option<UiModel> getUiModel(@NotNull QName key) {
        return ofNullable(cache.get(key));
    }

    private boolean doPut(@NotNull final UiModel model) {
        final boolean put = !model.isCached() || !cache.containsKey(model.getKey());
        if (put) cache.put(model.getKey(), model);
        return put;
    }

    //~ Methods ......................................................................................................................................

    /** Gets the ContextFormRetriever instance. */
    public static ClientUiModelRetriever getRetriever() {
        return ClientUiModelRetriever.InstanceHolder.instance;
    }

    //~ Inner Classes ................................................................................................................................

    private static class InstanceHolder {
        private static final ClientUiModelRetriever instance = new ClientUiModelRetriever(new HashMap<>());
    }
}  // end class ClientUiModelRetriever
