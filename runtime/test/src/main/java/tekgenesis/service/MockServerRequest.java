
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.MultiMap;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.serializer.SerializerException;
import tekgenesis.common.service.HeaderNames;
import tekgenesis.common.service.Headers;
import tekgenesis.common.service.Method;
import tekgenesis.common.service.Parameters;
import tekgenesis.common.service.cookie.Cookie;
import tekgenesis.common.service.server.Request;

import static tekgenesis.common.Predefined.notNull;

/**
 * Mock implementation of server Request.
 */
@SuppressWarnings("ArrayEquality")
public class MockServerRequest implements Request {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final byte[] content;

    @NotNull private final Headers headers;

    @NotNull private final Method                   method;
    @NotNull private final MultiMap<String, String> parameters;

    //~ Constructors .................................................................................................................................

    MockServerRequest(@NotNull Method method) {
        this(method, null);
    }

    MockServerRequest(@NotNull Method method, @Nullable byte[] content) {
        this.method  = method;
        this.content = notNull(content, no_content);
        headers      = new Headers();
        parameters   = MultiMap.createMultiMap();
    }

    //~ Methods ......................................................................................................................................

    @Override public void close() {}

    /** Add header value. */
    public void putHeader(@NotNull String header, @Nullable String value) {
        if (value != null) headers.put(header, value);
    }

    /** Add parameter value. */
    public void putParameter(@NotNull String parameter, @Nullable String value) {
        if (value != null) parameters.put(parameter, value);
    }

    @Override public Object getAttribute(@NotNull String name) {
        return "";
    }

    @Override public InputStream getContent() {
        return new ByteArrayInputStream(content);
    }

    @Override public int getContentLength() {
        return content != no_content ? content.length : -1;
    }

    @Override public Seq<Cookie> getCookies() {
        return Colls.emptyList();
    }

    @NotNull @Override public Headers getHeaders() {
        return headers;
    }

    /** Set header host. */
    public void setHost(@NotNull String host) {
        putHeader(HeaderNames.HOST, host);
    }

    @NotNull @Override public Method getMethod() {
        return method;
    }

    @NotNull @Override public MultiMap<String, String> getParameters() {
        return parameters;
    }

    @Override public String getPath() {
        return "";
    }

    @Override public String getQueryString() {
        return Parameters.mapToQueryString(parameters);
    }

    @NotNull @Override public String getScheme() {
        return "http";
    }

    @NotNull @Override public String getUri() {
        return "";
    }

    @NotNull @Override public String getUrl() {
        return getScheme() + "://" + getDomain() + ":" + getPort() + getUri();
    }

    private void assertHasContent() {
        if (content == no_content) throw new SerializerException("Empty body!");
    }

    //~ Static Fields ................................................................................................................................

    private static final byte[] no_content = {};
}  // end class MockServerRequest
