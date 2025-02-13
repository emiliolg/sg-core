
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;

import static java.net.HttpURLConnection.*;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.core.Option.option;
import static tekgenesis.common.service.HeaderNames.X_FIELDS;
import static tekgenesis.transaction.Transaction.invokeInTransaction;

/**
 * Handles all http methods for a given form.
 */
@SuppressWarnings("InstanceVariableMayNotBeInitialized")
public abstract class HandlerInstance<T extends FormInstance<?>> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull
    @SuppressWarnings({ "WeakerAccess", "NullableProblems" })
    protected HttpServletRequest          req;

    @NotNull
    @SuppressWarnings({ "WeakerAccess", "NullableProblems" })
    protected HttpServletResponse resp;

    //~ Methods ......................................................................................................................................

    /** Handle DELETE operation for given routing. */
    public void handleDelete(@NotNull final Handler<T> handler)
        throws IOException
    {
        final T instance = handler.instance();

        final Action action = invokeInTransaction(t -> {
                final Action a = instance.delete();
                if (a.isError()) t.abort();
                return a;
            });

        if (action.isError()) responseError(handler.getMessage(action));
        else resp.setStatus(HTTP_OK);
    }

    /** Handle GET operation for given routing. */
    public void handleGet(@NotNull final Handler<T> handler)
        throws IOException
    {
        final FormExporter exporter = handler.exporter(option(req.getQueryString()));

        exporter.fields(req);

        exporter.usingJson().configureFrom(req);

        exporter.into(resp);
    }

    /** Handle HEAD operation for given routing. */
    @SuppressWarnings("all")
    public void handleHead(@NotNull final Handler<T> handler)
        throws IOException
    {
        throw toBeImplemented();
    }

    /** Handle OPTIONS operation for given routing. */
    @SuppressWarnings("all")
    public void handleOptions(@NotNull final Handler<T> handler)
        throws IOException
    {
        throw toBeImplemented();
    }

    /** Handle POST operation for given routing. */
    public void handlePost(@NotNull final Handler<T> handler)
        throws IOException
    {
        final FormImporter importer = handler.importer();

        importer.from(req);

        final T instance = handler.instance();

        final boolean update = handler.getRouteKey().isPresent();
        final Action  action = runInstance(instance, update);

        if (action.isError()) responseError(handler.getMessage(action));
        else responsePersist(update, handler.getRoutePath() + "/" + instance.keyAsString());

        if (isNotEmpty(req.getHeader(X_FIELDS))) {
            final FormExporter exporter = handler.exporter(Option.empty()).fields(req);
            exporter.usingJson().configureFrom(req);
            exporter.into(resp);
        }
    }

    /** Handle PUT operation for given routing. */
    @SuppressWarnings("UnusedParameters")
    public void handlePut(@NotNull final Handler<T> handler)
        throws IOException
    {
        handlePost(handler);  // Improve!
    }

    /** Response 500 with action message as content. */
    @SuppressWarnings("WeakerAccess")
    protected void responseError(@NotNull final String msg)
        throws IOException
    {
        resp.setStatus(HTTP_INTERNAL_ERROR);
        writeContent(msg);
    }

    /** Response 201 | 202. */
    @SuppressWarnings("WeakerAccess")
    protected void responsePersist(boolean update, @NotNull final String location) {
        resp.setStatus(update ? HTTP_ACCEPTED : HTTP_CREATED);
        resp.setHeader(LOCATION, location);
    }

    private Action runInstance(T instance, boolean update) {
        return invokeInTransaction(t -> {
            final Action action = update ? instance.update() : instance.create();
            if (action.isError()) t.abort();
            return action;
        });
    }

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private IllegalStateException toBeImplemented() {
        return new IllegalStateException("To be implemented!");
    }

    private void writeContent(@NotNull final String msg)
        throws IOException
    {
        final OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream());
        writer.write(msg);
        writer.close();
    }

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings({ "WeakerAccess", "DuplicateStringLiteralInspection" })
    public static final String LOCATION = "location";
}  // end class HandlerInstance
