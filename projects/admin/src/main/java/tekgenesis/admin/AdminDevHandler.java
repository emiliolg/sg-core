
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.admin;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.WebResourceManager;
import tekgenesis.mmcompiler.ModelRepositoryLoader;
import tekgenesis.repository.ModelRepository;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;

import static tekgenesis.admin.AdminHandler.isDevMode;
import static tekgenesis.common.env.context.Context.getContext;

/**
 * User class for Handler: AdminUnrestrictedHandler
 */
public class AdminDevHandler extends AdminDevHandlerBase {

    //~ Constructors .................................................................................................................................

    AdminDevHandler(@NotNull Factory factory) {
        super(factory);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Result<String> refresh() {
        if (!isDevMode()) return badRequest("Refresh metadata submitted, but not in dev mode.");

        final ModelRepositoryLoader loader = new ModelRepositoryLoader(Thread.currentThread().getContextClassLoader());
        getContext().setSingleton(ModelRepository.class, loader.build());

        final String msg = "Metadata refreshed.";
        logInfo(msg);
        return ok(msg);
    }

    @NotNull @Override public Result<String> refreshResource(@NotNull final String resource) {
        if (!isDevMode()) return badRequest("Refresh resource submitted, but not in dev mode.");

        WebResourceManager.getInstance().processResourceChange(resource);

        final String msg = "Resource '" + resource + "' refreshed.";
        logInfo(msg);
        return ok(msg);
    }
}
