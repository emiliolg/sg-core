
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.ApplicationContext;
import tekgenesis.form.ServiceFilter;

/**
 * ServiceFilter Example.
 */
public class ShowcaseServiceFilter implements ServiceFilter {

    //~ Methods ......................................................................................................................................

    @Override public boolean accepts(@NotNull final String url) {
        return true;
    }

    @Override public boolean needsAuthentication() {
        return false;
    }

    @Override public boolean process(@NotNull final HttpServletRequest request, @NotNull final HttpServletResponse response,
                                     @NotNull final ApplicationContext context) {
        // System.out.println("request.getPathInfo() = " + request.getPathInfo());
        return true;
    }
}
