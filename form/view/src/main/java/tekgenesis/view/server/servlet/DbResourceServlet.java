
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.servlet;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.app.properties.ApplicationProps;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Resource;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.util.LruCache;
import tekgenesis.form.image.ImageResizer;
import tekgenesis.persistence.ResourceHandler;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.media.Mimes.isImage;
import static tekgenesis.common.media.Mimes.isText;
import static tekgenesis.common.service.HeaderNames.CONTENT_DISPOSITION;
import static tekgenesis.common.util.Files.copy;
import static tekgenesis.form.image.ImageResizer.OUTPUT_QUALITY;
import static tekgenesis.view.server.servlet.Servlets.addCacheHeaders;
import static tekgenesis.view.server.servlet.Servlets.addKeepAlive;

/**
 * Receives an image from the client for post operation. For get it writes a resource. Supports:
 * /sg/resource/myPath/myFile.png to return ANY resource /sg/resource/?enum=MyEnum&name=NAME to load
 * the image of an enum /sg/resource/?id=MyResourceId&variant=MyVariant to load a Resource form the
 * DB
 */
public class DbResourceServlet extends HttpServlet {

    //~ Instance Fields ..............................................................................................................................

    private final ResourceHandler resourceHandler = Context.getSingleton(ResourceHandler.class);

    //~ Methods ......................................................................................................................................

    @Override
    @SuppressWarnings("AssignmentToStaticFieldFromInstanceMethod")
    public void init()
        throws ServletException
    {
        if (resourcesCacheMap == null) {
            final int resourceCacheSize = Context.getProperties(ApplicationProps.class).resourcesCacheSize;
            if (resourceCacheSize > 0) resourcesCacheMap = LruCache.createLruCache(resourceCacheSize);
        }
    }

    @Override protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
        throws ServletException, IOException
    {
        try {
            addCacheHeaders(resp, 100);  // url includes sha
            addKeepAlive(resp);

            final String fileName = req.getParameter(FILE_NAME);
            if (isNotEmpty(fileName)) resp.setHeader(CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");

            final Resource.Content content  = findContent(req);
            final String           mimeType = content.getMimeType();
            resp.setContentType(mimeType);

            final int size;
            if (isText(mimeType)) size = content.copyTo(new OutputStreamWriter(resp.getOutputStream()));
            else if (isImage(mimeType)) {
                final String width   = req.getParameter(WIDTH);
                final String height  = req.getParameter(HEIGTH);
                final double quality = retrieveQuality(req.getParameter(QUALITY));

                if (isNotEmpty(width) && isNotEmpty(height)) {
                    final byte[] resized = ImageResizer.getInstance().resize(content, quality, parseInt(width), parseInt(height));
                    size = copy(new ByteArrayInputStream(resized), resp.getOutputStream());
                }
                else if (isNotEmpty(width)) {
                    final byte[] resized = ImageResizer.getInstance().width(content, quality, parseInt(width));
                    size = copy(new ByteArrayInputStream(resized), resp.getOutputStream());
                }
                else size = content.copyTo(resp.getOutputStream());
            }
            else size = content.copyTo(resp.getOutputStream());

            resp.setContentLength(size);
        }
        catch (final IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        resp.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    private Resource.Content cacheContent(final String sha, Resource.Content content) {
        if (resourcesCacheMap == null) return content;

        final Resource.Content cached = new CachedContent(content);
        resourcesCacheMap.put(sha, cached);
        return cached;
    }

    @Nullable private Resource.Content findCachedContent(final String sha) {
        return resourcesCacheMap == null ? null : resourcesCacheMap.get(sha);
    }

    private Resource.Content findContent(HttpServletRequest req) {
        String sha = notEmpty(req.getPathInfo(), req.getParameter(SHA_PARAMETER));
        if (isEmpty(sha)) return findContentFromIdParam(req);

        final int shaSeparator = sha.lastIndexOf("/");
        if (shaSeparator > -1) sha = sha.substring(shaSeparator + 1);

        final Resource.Content cached = findCachedContent(sha);
        if (cached != null) return cached;

        final Option<Resource.Content> r = resourceHandler.findContent(sha);
        if (r.isEmpty()) reportError("SHA not found: " + sha);

        return cacheContent(sha, r.get());
    }

    @NotNull private Resource.Content findContentFromIdParam(final HttpServletRequest req) {
        final String id = req.getParameter(ID_PARAMETER);
        if (isEmpty(id)) reportError("You must specify sha or id.");

        final Option<Resource> r = resourceHandler.findResource(id);
        if (r.isEmpty()) reportError("Id Not Found: " + id);

        final String         variant = req.getParameter(VARIANT_PARAMETER);
        final Resource.Entry e       = r.get().getEntry(variant);
        if (e == null) reportError("Variant not found: " + variant);

        return e;
    }

    private double retrieveQuality(final String parameter) {
        if (parameter == null) return OUTPUT_QUALITY;
        else {
            try {
                final int asInt = Integer.parseInt(parameter);
                return ((double) asInt / 100);
            }
            catch (final NumberFormatException e) {
                return OUTPUT_QUALITY;
            }
        }
    }  // end method retrieveQuality

    //~ Methods ......................................................................................................................................

    private static void reportError(String msg) {
        throw new IllegalArgumentException(msg);
    }

    //~ Static Fields ................................................................................................................................

    @Nullable private static LruCache<String, Resource.Content> resourcesCacheMap = null;

    private static final String SHA_PARAMETER     = "sha";
    private static final String FILE_NAME         = "filename";
    private static final String WIDTH             = "w";
    private static final String HEIGTH            = "h";
    private static final String QUALITY           = "q";
    private static final String ID_PARAMETER      = "id";
    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String VARIANT_PARAMETER = "variant";

    private static final long serialVersionUID = -6422211503408730776L;

    //~ Inner Classes ................................................................................................................................

    private static class CachedContent implements Resource.Content {
        @Nullable private final byte[] bytes;
        private final String           mimeType;
        private final int              size;
        @Nullable private final String text;

        private CachedContent(Resource.Content content) {
            mimeType = content.getMimeType();

            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            if (isText(mimeType)) {
                size  = content.copyTo(new OutputStreamWriter(outputStream));
                text  = outputStream.toString();
                bytes = null;
            }
            else {
                size  = content.copyTo(outputStream);
                bytes = outputStream.toByteArray();
                text  = null;
            }
        }

        @Override public int copyTo(final Writer writer) {
            if (text == null) reportError(format("OutputStream expected for %s", mimeType));
            try {
                writer.write(text);
            }
            catch (final IOException e) {
                reportError(e.getMessage());
            }
            return size;
        }

        @Override public int copyTo(final OutputStream os) {
            if (bytes == null) reportError(format("Writer expected for %s", mimeType));
            try {
                os.write(bytes);
            }
            catch (final IOException e) {
                reportError(e.getMessage());
            }
            return size;
        }

        @Override public String getMimeType() {
            return mimeType;
        }
    }  // end class CachedContent
}  // end class DbResourceServlet
