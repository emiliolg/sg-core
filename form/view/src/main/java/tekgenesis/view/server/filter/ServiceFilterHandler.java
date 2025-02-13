
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
import java.util.List;
import java.util.ServiceLoader;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.ApplicationContext;
import tekgenesis.form.ApplicationContextImpl;
import tekgenesis.form.ServiceFilter;
import tekgenesis.service.InterfaceTypeProxy;
import tekgenesis.view.shared.exception.GwtRPCSerializableException;

import static java.nio.charset.Charset.forName;

import static com.google.gwt.user.server.rpc.RPCServletUtils.CHARSET_UTF8_NAME;

import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.env.security.SecurityUtils.getSession;
import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.common.media.Mime.TEXT_X_GWT_RPC;
import static tekgenesis.security.shiro.web.URLConstants.INTERFACE_TYPE_PROXY;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * Filter that looks for {@link ServiceFilter} implementations and calls them the apply() method.
 */
@SuppressWarnings("WeakerAccess")
public class ServiceFilterHandler extends BaseFilter {

    //~ Methods ......................................................................................................................................

    @Override void doFilter(@NotNull HttpServletRequest req, @NotNull HttpServletResponse resp, @NotNull FilterChain chain)
        throws IOException, ServletException
    {
        /* Filter that initializes html interaction context for further usage. */
        req.setAttribute(INTERFACE_TYPE_PROXY, new InterfaceTypeProxy());

        final ApplicationContext  context         = ApplicationContextImpl.getInstance();
        final List<ServiceFilter> acceptedFilters = ServiceFilterService.getFilters(req);

        // before request chain
        boolean continueChain = true;
        try {
            for (final ServiceFilter filter : acceptedFilters) {
                if (continueChain) continueChain = invokeInTransaction(() -> filter.process(req, resp, context));
            }
        }
        catch (final Throwable t) {
            logger.warning("Exception processing filter", t);

            if (isGwtRPC(req.getContentType())) {
                try {
                    writeExceptionToResponse(resp, t);
                }
                catch (final SerializationException | IOException ignored) {
                    throw t;
                }
            }
            else throw t;
        }

        if (continueChain) advance(req, resp, chain);

        // after request chain
        for (final ServiceFilter filter : acceptedFilters) {
            try {
                runInTransaction(() -> filter.processAfter(req, resp, context));
            }
            catch (final Throwable t) {
                logger.warning("Exception processing after filter", t);
                throw t;
            }
        }
    }

    /**
     * This mimics GWT RPC expception writing by 'wrapping' the original exception in a GWT
     * Serializable one. Only the message of the exception is serialized.
     */
    private void writeExceptionToResponse(@NotNull HttpServletResponse resp, Throwable t)
        throws SerializationException, IOException
    {
        final GwtRPCSerializableException exception = new GwtRPCSerializableException(t.getMessage());
        final String                      s         = RPC.encodeResponseForFailedRequest(null, exception);
        final byte[]                      respBytes = s.getBytes(forName(CHARSET_UTF8_NAME));

        resp.setContentLength(respBytes.length);
        resp.setContentType("application/json; charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getOutputStream().write(respBytes);
    }

    //~ Methods ......................................................................................................................................

    private static boolean isGwtRPC(@NotNull final String contentType) {
        return contentType.startsWith(TEXT_X_GWT_RPC.getType());
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = getLogger(ServiceFilterHandler.class);

    //~ Inner Classes ................................................................................................................................

    private static class ServiceFilterService {
        private final ImmutableList<ServiceFilter> filters;

        private ServiceFilterService() {
            filters = ImmutableList.fromIterable(ServiceLoader.load(ServiceFilter.class));
        }

        private static boolean applies(ServiceFilter filter, HttpServletRequest req) {
            return (!filter.needsAuthentication() || getSession().isAuthenticated()) && filter.accepts(notEmpty(req.getRequestURI(), ""));
        }

        private static List<ServiceFilter> getFilters(final HttpServletRequest req) {  //
            return getInstance().filters.filter(f -> applies(f, req)).toList();
        }

        private static ServiceFilterService getInstance() {
            return ServiceHolder.service;
        }

        private static class ServiceHolder {
            private static final ServiceFilterService service = new ServiceFilterService();
        }
    }
}  // end class ServiceFilterHandler
