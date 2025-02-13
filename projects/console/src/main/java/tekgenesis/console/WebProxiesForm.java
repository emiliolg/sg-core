
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.console;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.env.context.Context;
import tekgenesis.common.invoker.HttpInvoker;
import tekgenesis.common.invoker.HttpInvokers;
import tekgenesis.form.Action;
import tekgenesis.properties.SchemaProps;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.common.Predefined.isNotEmpty;

/**
 * User class for Form: WebProxiesForm
 */
public class WebProxiesForm extends WebProxiesFormBase {

    //~ Methods ......................................................................................................................................

    @Override public void loadProxies() {
        final Collection<String> remoteSchemas = Context.getSingleton(ModelRepository.class).getSchemas();
        final Set<String>        proxies       = new HashSet<>();
        for (final String remoteSchema : remoteSchemas) {
            final SchemaProps properties   = Context.getProperties(remoteSchema, SchemaProps.class);
            final String      webProxyUrls = properties.remoteUrl;
            if (isNotEmpty(webProxyUrls)) {
                final String[] url = webProxyUrls.split(";");
                Collections.addAll(proxies, url);
            }
        }
        for (final String proxy : proxies)
            populateStatus(loadStatus(proxy));
    }

    private ProxyStatus loadStatus(String proxyUrl) {
        final HttpInvoker invoker = HttpInvokers.invoker(proxyUrl.substring(0, proxyUrl.lastIndexOf("/")));
        @SuppressWarnings("DuplicateStringLiteralInspection")
        final ProxyStatus status = invoker.resource("status").param("format", "json").get(ProxyStatus.class);
        status.setUrl(proxyUrl);

        return status;
    }

    private void populateBackends(ProxyStatus status) {
        for (final ProxyBackend proxyBackend : status.getBackends())
            getBackends().add().populate(proxyBackend);
    }

    private void populatePendingRequests(ProxyStatus status) {
        for (final ProxyPendingRequest pendingRequest : status.getPendingRequests())
            getPendindRequests().add().populate(pendingRequest);
    }

    private void populateRow(ProxyStatus proxyStatus) {
        final ProxiesRow proxiesRow = getProxies().add();
        proxiesRow.setUrl(proxyStatus.getUrl());
        try {
            final URL url = new URL(proxyStatus.getUrl());
            proxiesRow.setWebProxy(url.getHost() + (url.getPort() != HTTP_DEFAULT ? ":" + url.getPort() : ""));
        }
        catch (final MalformedURLException e) {
            proxiesRow.setWebProxy(proxyStatus.getUrl());
        }
        proxiesRow.setBackEnds(proxyStatus.getBackends().size());
        proxiesRow.setSent(proxyStatus.getBackends().stream().mapToInt(ProxyBackend::getSentMessages).sum());
        proxiesRow.setReceived(proxyStatus.getBackends().stream().mapToInt(ProxyBackend::getReceivedMessages).sum());
    }

    private void populateStatus(ProxyStatus status) {
        populateRow(status);
        populateBackends(status);
        populatePendingRequests(status);
    }

    //~ Static Fields ................................................................................................................................

    public static final int HTTP_DEFAULT = 80;

    //~ Inner Classes ................................................................................................................................

    public class BackendsRow extends BackendsRowBase {
        private void populate(ProxyBackend backend) {
            setName(backend.getName());
            setConnections(backend.getConnections());
            setBuffers(backend.getRequiredBuffers());
            setSentMsgs(backend.getSentMessages());
            setReceivedMsgs(backend.getReceivedMessages());
        }
    }

    public class PendindRequestsRow extends PendindRequestsRowBase {
        private void populate(ProxyPendingRequest req) {
            setClient(req.getClient());
            setMethod(req.getMethod());
            setRequest(req.getRequest());
        }
    }

    public class ProxiesRow extends ProxiesRowBase {
        @NotNull @Override public Action details() {
            getBackends().clear();
            getPendindRequests().clear();

            populateBackends(loadStatus(getUrl()));

            setDetailsDialog(true);
            return actions.getDefault();
        }
    }
}  // end class WebProxiesForm
