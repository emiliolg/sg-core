
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.servlet;

import java.util.*;

import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.media.MediaType;
import tekgenesis.common.service.Status;
import tekgenesis.service.*;
import tekgenesis.service.html.Html;
import tekgenesis.service.html.HtmlBuilder;
import tekgenesis.service.html.HtmlInstanceBuilder;
import tekgenesis.view.shared.response.ResponseError;

import static java.lang.Boolean.getBoolean;

import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.common.core.Constants.SUIGEN_DEVMODE;
import static tekgenesis.common.core.Strings.toWords;
import static tekgenesis.common.service.Status.INTERNAL_SERVER_ERROR;
import static tekgenesis.common.service.Status.NOT_FOUND;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.shared.response.ResponseError.deepest;

@ParametersAreNonnullByDefault class DispatcherServletError {

    //~ Constructors .................................................................................................................................

    private DispatcherServletError() {}

    //~ Methods ......................................................................................................................................

    static Result<Html> errorPage(Factory factory, Dispatcher dispatcher, Status status) {
        return errorPage(factory, dispatcher, status, null);
    }

    static Result<Html> errorPage(Factory factory, Dispatcher dispatcher, Status status, @Nullable Throwable e) {
        Option<Result<Html>> result = Option.empty();

        for (final CustomError customError : CustomErrorService.getCustomErrors()) {
            result = customError.forError(factory, status, e);
            if (result.isPresent()) break;
        }

        final Result<Html> html = result.isPresent() ? result.get() : defaultErrorPage(factory.results(), dispatcher, status, e);
        return html.withContentType(MediaType.TEXT_HTML);
    }

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static Map<String, Object> createErrorArguments(@NotNull Status status, Dispatcher dispatcher, @Nullable Throwable e) {
        final boolean             devMode   = getBoolean(SUIGEN_DEVMODE);
        final Map<String, Object> arguments = e != null ? new ResponseError(deepest(e), devMode).asMapArguments() : new HashMap<>();

        arguments.put("code", String.valueOf(status.code()));

        if (!devMode || status == NOT_FOUND) arguments.put("devModeStyle", "none");
        if (!devMode || status == INTERNAL_SERVER_ERROR) arguments.put("notFound", "none");

        final String error = toWords(status.name());

        arguments.put("title", status.code() + " " + error);

        switch (status) {
        case NOT_FOUND:
            arguments.put("message", MSGS.pageNotFoundMessage());
            arguments.put("lastTriedRoutes", seq(dispatcher.getLastTriedRoutes()));
            break;
        case INTERNAL_SERVER_ERROR:
            arguments.put("message", MSGS.serverErrorMessage());
            break;
        default:
            arguments.put("message", error);
        }

        return arguments;
    }

    private static Result<Html> defaultErrorPage(Results results, Dispatcher dispatcher, Status status, @Nullable Throwable e) {
        final Map<String, Object> arguments = createErrorArguments(status, dispatcher, e);

        final HtmlInstanceBuilder.Xhtml xhtml = Context.getSingleton(HtmlBuilder.class).html("/sg/html/error.xhtml");
        arguments.entrySet().forEach(entry -> xhtml.param(entry.getKey(), entry.getValue()));
        return results.fromStatus(status, xhtml.build());
    }

    //~ Inner Classes ................................................................................................................................

    private static class CustomErrorService {
        private final List<CustomError> customErrors;

        private CustomErrorService() {
            final ServiceLoader<CustomError> loader = ServiceLoader.load(CustomError.class);
            customErrors = new ArrayList<>();
            for (final CustomError filter : loader)
                customErrors.add(filter);
        }

        private static Iterable<CustomError> getCustomErrors() {
            return getInstance().customErrors;
        }

        private static synchronized CustomErrorService getInstance() {
            if (service == null) service = new CustomErrorService();
            return service;
        }

        private static CustomErrorService service = null;
    }
}  // end class DispatcherServletError
