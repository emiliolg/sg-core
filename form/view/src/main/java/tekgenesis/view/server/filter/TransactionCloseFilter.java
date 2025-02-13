
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NotNull;

import tekgenesis.cluster.ClusterManager;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.properties.LoggingProps;
import tekgenesis.persistence.IxService;
import tekgenesis.transaction.Transaction;

/**
 * Closes the transaction after all filters.
 */
@SuppressWarnings("WeakerAccess")
public class TransactionCloseFilter extends BaseFilter {

    //~ Methods ......................................................................................................................................

    @Override void doFilter(@NotNull HttpServletRequest req, @NotNull HttpServletResponse resp, @NotNull FilterChain chain)
        throws IOException, ServletException
    {
        final LoggingProps props = Context.getProperties(LoggingProps.class);
        if (props.addIdRefResponseHeader) resp.setHeader("X-Id-Ref", getCurrentMemberId());

        // continue chain with authentication
        try {
            advance(req, resp, chain);
        }
        finally {
            Transaction.getCurrent().ifPresent(Transaction::commit);
            IxService.ensureCloseBatch();
        }
    }

    //~ Methods ......................................................................................................................................

    @SuppressWarnings("NonThreadSafeLazyInitialization")
    private static String getCurrentMemberId() {
        if (currentMemberId == null) currentMemberId = Context.getSingleton(ClusterManager.class).getCurrentMemberId();
        return currentMemberId;
    }

    //~ Static Fields ................................................................................................................................

    private static String currentMemberId = null;
}
