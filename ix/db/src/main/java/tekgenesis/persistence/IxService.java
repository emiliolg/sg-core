
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.env.Environment;
import tekgenesis.common.env.impl.PropertiesEnvironment;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.util.Resources;
import tekgenesis.persistence.ix.IxBatchHandler;
import tekgenesis.persistence.ix.IxStoreHandlerType;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.collections.Colls.emptyList;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.common.env.context.Context.getEnvironment;
import static tekgenesis.common.net.Ping.ping;
import static tekgenesis.common.util.Reflection.findField;
import static tekgenesis.common.util.Reflection.getFieldValue;

/**
 * Ix Service.
 */
@SuppressWarnings("WeakerAccess")
public final class IxService {

    //~ Constructors .................................................................................................................................

    private IxService() {}

    //~ Methods ......................................................................................................................................

    /** Abort batch operations. Discarding all the operations */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static void abortBatch() {
        IxBatchHandler.abortBatch();
    }

    /** Set the current domain as mutable/immutable. */
    public static void asImmutable(boolean b) {
        immutableBoolean.set(b);
    }

    /** end batch operations. */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static void endBatch() {
        IxBatchHandler.commit();
    }

    /** check current batch content. If it is open -> abort it. */
    public static void ensureCloseBatch() {
        if (isBatchEnabled()) {
            logger.error(
                String.format("Batch operation not closed. The current batch operation will be aborted (Batch size: %d)",
                    IxBatchHandler.current().size()));
            abortBatch();
        }
    }

    /** Initialize batch operation. If batch operation is already */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static void startBatch() {
        IxBatchHandler.begin();
    }

    /** unset the current domain. */
    public static void unsetDomain() {
        domainService.remove();
    }

    /** @return  true/false if Batch processing is enabled */
    public static boolean isBatchEnabled() {
        return IxBatchHandler.isBatchEnabled();
    }

    /** @return  the domain */
    public static String getDomain() {
        return notNull(domainService.get(), "");
    }

    /** @param  domain  The new domain */
    public static void setDomain(@NotNull String domain) {
        if (isImmutable() && !domain.equals(domainService.get()))
            throw new IllegalStateException(
                String.format("Current ixservice is set as immutable. Trying to set domain to %s where current domain is %s", domain, getDomain()));
        domainService.set(domain);
    }

    /**
     * Ping the Ix Server for availability. The ping status is keep in memory until
     * ixProps.pingInterval is exceeded, then a new ping command is executed
     *
     * @return  true if the service is available
     */
    public static boolean isAvailable() {
        final String scope = getDomain();

        final IxProps            ixProps            = getEnvironment().get(scope, IxProps.class);
        final IxStoreHandlerType ixStoreHandlerType = IxStoreHandlerType.get(ixProps.url);

        boolean available = false;
        switch (ixStoreHandlerType) {
        case NULL:
            available = false;
            break;
        case MOCKED:
            available = true;
            break;
        case STANDARD:
            available = ping(ixProps.url, ixProps.pingTimeout);
            break;
        }
        logger.debug("isAvailable %b", available);
        return available;
    }

    /** Return if the current domain set is mutable or not. */
    public static boolean isImmutable() {
        return notNull(immutableBoolean.get(), false);
    }

    /** Returns the list of generated Ix Entities.* */
    public static List<String> getIxEntities() {
        // noinspection DuplicateStringLiteralInspection
        return Resources.readResources("META-INF/ix-entity-list");
    }

    /** Returns the registered End points. */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static ImmutableList<Tuple<String, IxProps>> getRegisteredEndpoints(Environment env) {
        return findField(PropertiesEnvironment.class, "properties")  //
               .map(properties -> {
                final Map<String, String> propMap = getFieldValue(env, properties);
                final Seq<String>         props   = propMap == null ? emptyList() : immutable(propMap.keySet());

                return props.filter(k -> k.endsWith("ix.url"))      //
                .map(s -> {
                                final String scope = s.substring(0, s.indexOf("."));
                                return tuple(scope, env.get(scope, IxProps.class));
                            }).toList();
            }).orElse(emptyList());
    }

    //~ Static Fields ................................................................................................................................

    private static final ThreadLocal<String>  domainService    = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> immutableBoolean = new ThreadLocal<>();

    private static final Logger logger = Logger.getLogger(IxService.class);
}  // end class IxService
