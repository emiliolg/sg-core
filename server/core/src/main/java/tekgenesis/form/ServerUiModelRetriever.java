
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

import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.metadata.form.UiModelRetriever;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.common.env.context.Context.getSingleton;

/**
 * Wraps the context {@link ModelRepository} as a {@link UiModelRetriever}.
 */
public class ServerUiModelRetriever implements UiModelRetriever {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final ModelRepository repository;

    //~ Constructors .................................................................................................................................

    ServerUiModelRetriever(@NotNull ModelRepository repository) {
        this.repository = repository;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public final Option<UiModel> getUiModel(@NotNull QName key) {
        return repository.getModel(key, UiModel.class);
    }

    //~ Methods ......................................................................................................................................

    /** Gets the ContextFormRetriever instance. */
    public static UiModelRetriever getRetriever() {
        return InstanceHolder.instance;
    }

    //~ Inner Classes ................................................................................................................................

    private static class InstanceHolder {
        private static final ServerUiModelRetriever instance = new ServerUiModelRetriever(getSingleton(ModelRepository.class));
    }
}
