
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import javax.servlet.RequestDispatcher;

import org.jetbrains.annotations.NotNull;

/**
 * Service forwarder. Assembles {@link RequestDispatcher} forward.
 */
public interface Forwarder {

    //~ Methods ......................................................................................................................................

    /**
     * Forward current request to path, using given response. If routing specified, use handler
     * routing else consume request.
     */
    void forward(@NotNull String uri, @NotNull ResultHandler resultHandler, boolean routing);

    /** Returns absolute url. */
    String url(@NotNull String uri);
}
