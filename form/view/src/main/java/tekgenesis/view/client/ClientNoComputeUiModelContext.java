
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.UiModelContext;
import tekgenesis.metadata.form.model.WidgetDefModel;

/**
 * Simple client side {@link UiModelContext ui model context} implementation. Compute operation is
 * not supported.
 */
public class ClientNoComputeUiModelContext extends ClientUiModelRetriever implements UiModelContext {

    //~ Constructors .................................................................................................................................

    private ClientNoComputeUiModelContext() {
        super(ClientUiModelContext.getRetriever().cache);
    }

    //~ Methods ......................................................................................................................................

    @Override public void compute(@NotNull WidgetDefModel wdm) {
        throw new UnsupportedOperationException("Compute is not supported on given UiModelContext");
    }

    //~ Methods ......................................................................................................................................

    /** Gets the {@link ClientNoComputeUiModelContext instance}. */
    public static UiModelContext getNoComputeUiModelContext() {
        return InstanceHolder.instance;
    }

    //~ Inner Classes ................................................................................................................................

    private static class InstanceHolder {
        private static final ClientNoComputeUiModelContext instance = new ClientNoComputeUiModelContext();
    }
}
