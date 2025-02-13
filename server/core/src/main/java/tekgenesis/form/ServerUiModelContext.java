
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

import tekgenesis.form.exprs.ServerExpressions;
import tekgenesis.metadata.form.UiModelContext;
import tekgenesis.metadata.form.model.WidgetDefModel;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.common.env.context.Context.getSingleton;
import static tekgenesis.form.ModelPopulate.modelPopulate;

/**
 * Server side {@link UiModelContext ui model context} implementation. Keeps a reference to
 * {@link UiModelInstanceHandler class instance}.
 */
public class ServerUiModelContext extends ServerUiModelRetriever implements UiModelContext {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final UiModelInstanceHandler instance;

    //~ Constructors .................................................................................................................................

    private ServerUiModelContext(@NotNull final UiModelInstanceHandler instance, @NotNull final ModelRepository repository) {
        super(repository);
        this.instance = instance;
    }

    //~ Methods ......................................................................................................................................

    @Override public void compute(@NotNull WidgetDefModel wdm) {
        final UiModelInstanceHandler nested = instance.nested(wdm);
        ServerExpressions.bindAndCompute(nested).stopListening();
        modelPopulate(wdm.metadata(), wdm);
    }

    //~ Methods ......................................................................................................................................

    /** Creates a {@link UiModelContext server ui model context}. */
    @NotNull public static UiModelContext createServerContext(@NotNull final UiModelInstanceHandler handler) {
        return new ServerUiModelContext(handler, getSingleton(ModelRepository.class));
    }
}
