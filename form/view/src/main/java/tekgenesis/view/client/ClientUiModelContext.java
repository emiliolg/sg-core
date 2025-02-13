
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

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.UiModelContext;
import tekgenesis.metadata.form.model.WidgetDefModel;
import tekgenesis.view.client.controller.EventHandlerVisitor;
import tekgenesis.view.client.controller.FormController;
import tekgenesis.view.client.exprs.ExpressionEvaluatorFactory;
import tekgenesis.view.client.exprs.OnChangeNotifications;
import tekgenesis.view.client.ui.ModelUI;
import tekgenesis.view.client.ui.WidgetDefUI;

/**
 * Client side {@link UiModelContext ui model context} implementation. Manages nested ui contexts...
 */
public class ClientUiModelContext extends ClientUiModelRetriever implements UiModelContext {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final ModelUI        container;
    @NotNull private final FormController controller;

    @NotNull private final ExpressionEvaluatorFactory factory;
    @NotNull private final OnChangeNotifications      pendings;

    //~ Constructors .................................................................................................................................

    private ClientUiModelContext(@NotNull final ExpressionEvaluatorFactory factory, @NotNull final ModelUI container,
                                 @NotNull final FormController controller, @NotNull final OnChangeNotifications pendings) {
        super(getRetriever().cache);
        this.factory    = factory;
        this.container  = container;
        this.controller = controller;
        this.pendings   = pendings;
    }

    //~ Methods ......................................................................................................................................

    @Override public void compute(@NotNull WidgetDefModel wdm) {
        final Option<WidgetDefUI> ui = wdm.parent().flatMap(parent -> container.finder().find(parent.anchor(), parent.item()));
        ui.ifPresent(widget -> {
            EventHandlerVisitor.create(controller, wdm).traverse(widget);
            factory.bindAndCompute(widget, wdm, pendings);
        });
    }

    //~ Methods ......................................................................................................................................

    /** Creates a {@link UiModelContext client ui model context}. */
    @NotNull public static UiModelContext createClientContext(@NotNull final ExpressionEvaluatorFactory factory, @NotNull final ModelUI container,
                                                              @NotNull final FormController controller,
                                                              @NotNull final OnChangeNotifications pendings) {
        return new ClientUiModelContext(factory, container, controller, pendings);
    }
}  // end class ClientUiModelContext
