
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.service.Headers;
import tekgenesis.common.service.Status;
import tekgenesis.service.html.HtmlInstance;

import static tekgenesis.service.ResultImpl.ObjectResult;
import static tekgenesis.service.ResultImpl.RedirectResult;

/**
 * Delegates Html responses.
 */
public class HtmlResultHandler extends AbstractResultHandler {

    //~ Instance Fields ..............................................................................................................................

    private HtmlInstance htmlInstance;

    //~ Constructors .................................................................................................................................

    /** Html response constructor. */
    public HtmlResultHandler() {
        htmlInstance = null;
    }

    //~ Methods ......................................................................................................................................

    /** Returns true if it has an associated html instance. */
    public boolean hasHtmlInstance() {
        return htmlInstance != null;
    }

    /** Returns the associated html instance. */
    public HtmlInstance getHtmlInstance() {
        return htmlInstance;
    }

    @Override void handleObject(@NotNull ObjectResult result, @NotNull Forwarder forwarder) {
        final Object content = result.content;
        if (content instanceof HtmlInstance) htmlInstance = (HtmlInstance) content;
    }

    @Override void handleRedirect(@NotNull final RedirectResult result) {}

    @Override void handleResponseHeaders(@NotNull Headers result) {}

    @Override void handleResponseStatus(@NotNull Status status) {}

    @Override void handlerForward(@NotNull ResultImpl.ForwardResult result, Forwarder forwarder) {}
}
