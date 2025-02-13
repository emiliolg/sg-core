
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.MDC;

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.invoker.HttpInvoker;
import tekgenesis.common.invoker.HttpInvokers;
import tekgenesis.common.rest.exception.RestInvocationException;
import tekgenesis.common.util.Files;

import static java.net.URLEncoder.encode;

import static tekgenesis.common.core.Constants.LIFECYCLE_KEY;
import static tekgenesis.common.core.Constants.UTF8;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.common.service.HeaderNames.X_MDC_UUID;

/**
 * Conversion and other utilities for Ideafix values.
 */
public abstract class IxUtil {

    //~ Methods ......................................................................................................................................

    /**
     * @param   mode  IxMode
     *
     * @return  RestInvoker
     */
    @NotNull public static HttpInvoker createInvoker() {
        return createInvoker(null);
    }

    /**
     * @param   mode  IxMode
     *
     * @return  RestInvoker
     */
    @NotNull public static <T extends IxInstance<T, K>, K> HttpInvoker createInvoker(@Nullable Class<T> clazz) {
        final IxProps props = Context.getEnvironment().get(IxService.getDomain(), IxProps.class);

        final IxJsonMessageConverter<T, K> ixJsonMessageConverter = new IxJsonMessageConverter<>(clazz);
        final Tuple<String, String>        mdcHeader              = getMdcHeader();
        return HttpInvokers.invoker(props.url)
               .header(mdcHeader.first(), mdcHeader.second())
               .withReadTimeout(props.timeout)
               .withConnectTimeout(props.connectTimeout)
               .withCommandPool("IxCommandPool")
               .withConverter(ixJsonMessageConverter)
               .withErrorHandler((status, headers, data) -> {
                throw RestInvocationException.createException(status.code(), asString(data));
            });
    }

    /** Extract date part. */
    @Nullable public static DateOnly toDate(@Nullable DateTime dateTime) {
        return dateTime == null ? null : dateTime.toDateOnly();
    }

    /** Build a DateTime from Ideafix parts. */
    @Nullable public static DateTime toDateTime(@Nullable DateOnly date, @Nullable Integer time) {
        return date == null || time == null ? null : date.toDateTime().addSeconds(time);
    }

    /** Extract time part. */
    @Nullable public static Integer toTime(@Nullable DateTime dateTime) {
        return dateTime == null ? null : dateTime.getDaySeconds();
    }

    /** Encode the url in UTF 8. */
    public static String utf8Encode(String str) {
        try {
            return encode(str, UTF8);
        }
        catch (final UnsupportedEncodingException e) {
            // UTF8 should be supported
            throw new RuntimeException(e);
        }
    }

    @Nullable private static String asString(@Nullable InputStream is) {
        if (is == null) return null;
        return Files.readInput(new InputStreamReader(is));
    }

    private static Tuple<String, String> getMdcHeader() {
        final String lifecycleKey = MDC.get(LIFECYCLE_KEY);

        final String mdc = lifecycleKey != null ? lifecycleKey : UUID.randomUUID().toString();

        return tuple(X_MDC_UUID, mdc);
    }
}  // end class IxUtil
