
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.Resource;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.media.Mime;
import tekgenesis.persistence.ResourceHandler;
import tekgenesis.view.shared.utils.CropperServlet;
import tekgenesis.view.shared.utils.ResourceUtils;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.Predefined.unreachable;
import static tekgenesis.common.core.Constants.UTF8;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.media.Mime.APPLICATION_OCTET_STREAM;
import static tekgenesis.common.media.Mimes.*;
import static tekgenesis.metadata.form.model.FormConstants.*;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.view.shared.utils.CropperServlet.PostResponse.error;

/**
 * Receives an image from the client for post operation. For get it writes a resource. Supports:
 * /sg/resource/myPath/myFile.png to return ANY resource /sg/resource/?enum=MyEnum&name=NAME to load
 * the image of an enum /sg/resource/?id=MyResourceId&variant=MyVariant to load a Resource form the
 * DB
 */
public class DbResourceUploadServlet extends CropperServlet {

    //~ Instance Fields ..............................................................................................................................

    private final ResourceHandler resourceHandler = Context.getSingleton(ResourceHandler.class);

    //~ Methods ......................................................................................................................................

    @Override protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
        throws ServletException, IOException
    {
        resp.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        final ObjectNode jsonResponse = buildJsonResponse(req);

        resp.setContentType(Mime.APPLICATION_JSON.getMime());
        resp.setCharacterEncoding(UTF8);
        if (jsonResponse != null) resp.getOutputStream().print(jsonResponse.toString());
    }  // end method doPost

    private ObjectNode buildExternalResource(Resource.Factory factory, String url) {
        final ExternalData data = new ExternalData(url);
        if (data.isValid()) {
            final Resource resource = invokeInTransaction(() -> {
                    final String   name = data.getName();
                    final Resource r    = factory.upload(name, data.getMain());

                    final String thumb = data.getThumb();
                    if (!isEmpty(thumb)) r.addThumb().upload(name, thumb);

                    return r;
                });

            // send the content info back to the client
            final Resource.Entry master   = resource.getMaster();
            final Resource.Entry thumb    = resource.getThumb();
            final String         type     = resource.getMaster().getMimeType();
            final String         mimeType = isEmpty(type) ? ResourceUtils.getMimeType(resource) : type;
            return PostResponse.success(resource.getUuid(),
                master.isExternal(),
                master.getName(),
                master.getUrl(),
                thumb == null ? "" : thumb.getUrl(),
                mimeType.isEmpty() ? "" : mimeType);
        }
        return error("External resource type not supported");
    }

    private ObjectNode buildFileResource(HttpServletRequest req, Resource.Factory factory, FileItem file) {
        // write resource content
        final String contentType = file.getContentType();
        final String mimeType    = isUnresolved(contentType) ? getMimeType(contentType) : contentType;
        final String name        = shortenName(file.getName());

        try(final InputStream inputStream = getInputStream(req, file.getInputStream())) {
            return invokeInTransaction(() -> {
                // If text we could assume the request encoding but this can break, so in the future we could do something
                // like:
                // http://svn.guessencoding.codehaus.org/browse/guessencoding/trunk/src/main/java/com/glaforge/i18n/io/CharsetToolkit.java
                final Resource resource;
                try {
                    resource = isText(mimeType)
                        ? factory.upload(name, mimeType, new InputStreamReader(inputStream, notNull(req.getCharacterEncoding(), UTF8)))
                        : factory.upload(name, mimeType, inputStream);
                }
                catch (final UnsupportedEncodingException e) {
                    return error(e);
                }

                // if it's an image (not PSD, not SVG, and not WEBp), generate thumb and large predefined variants.
                if (isVariantSupportedImage(mimeType, name)) generateImageVariants(resource, file.getSize());

                final Resource.Entry master = resource.getMaster();
                final Resource.Entry thumb  = resource.getThumb();

                return PostResponse.success(resource.getUuid(),
                    master.isExternal(),
                    name,
                    master.getUrl(),
                    thumb != null ? thumb.getUrl() : null,
                    mimeType);
            });
        }
        catch (final Exception e) {
            return error(e);
        }
    }  // end method buildFileResource

    @Nullable private ObjectNode buildJsonResponse(HttpServletRequest req) {
        final Resource.Factory factory = resourceHandler.create();
        final String           url     = req.getParameter("url");
        if (isNotEmpty(url)) return buildExternalResource(factory, url);

        final Option<ObjectNode> objectNodes = parseFileItems(req).map(file -> buildFileResource(req, factory, file));
        return objectNodes.orElse(error("Cannot Parse File from Request"));
    }

    private Option<FileItem> parseFileItems(final HttpServletRequest request) {
        final DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
        fileItemFactory.setSizeThreshold(MEMORY_SIZE_THRESHOLD);  // use one much bigger than default, to avoid disk use
        try {
            final List<?> files = new ServletFileUpload(fileItemFactory).parseRequest(request);
            return files.isEmpty() ? empty() : Option.of(files.get(0)).castTo(FileItem.class);
        }
        catch (final FileUploadException e) {
            return empty();
        }
    }

    private String shortenName(String name) {
        return name.length() > LENGTH && name.lastIndexOf(".") > 0 ? name.substring(0, LENGTH) + name.substring(name.lastIndexOf("."), name.length())
                                                                   : name;
    }

    private boolean isVariantSupportedImage(String mimeType, String name) {
        return isImage(mimeType) && !isPSD(name) && !mimeType.contains("svg") && !mimeType.contains("webp");
    }

    //~ Static Fields ................................................................................................................................

    private static final int LENGTH = 250;

    private static final int  MEMORY_SIZE_THRESHOLD = 10485760;  // 10MB images should escape from disk use
    private static final long serialVersionUID      = -6422211503408730776L;

    @NonNls private static final String YOUTUBE_THUMB_URL = "https://img.youtube.com/vi/";

    //~ Enums ........................................................................................................................................

    private enum VideoData {
        NAME, THUMB;

        private String resolveVideoData(@NotNull String id, boolean isYoutube) {
            final String url;
            final String DATA_TAG;
            final String DATA_TAG_END;

            if (isYoutube) {
                url          = "https://gdata.youtube.com/feeds/api/videos/" + id + "?v=2";
                DATA_TAG     = "<media:title type='plain'>";
                DATA_TAG_END = "</media:title>";
            }
            else {
                url = "http://vimeo.com/api/v2/video/" + id + ".xml";
                switch (this) {
                case NAME:
                    DATA_TAG     = TITLE_TAG;
                    DATA_TAG_END = TITLE_CLOSING_TAG;
                    break;
                case THUMB:
                    DATA_TAG     = "<thumbnail_large>";
                    DATA_TAG_END = "</thumbnail_large>";
                    break;
                default:
                    throw unreachable();
                }
            }

            String videoData = id;
            try {
                final URL               obj       = new URL(url);
                final HttpURLConnection con       = (HttpURLConnection) obj.openConnection();
                final BufferedReader    in        = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String                  inputLine;
                final StringBuilder     response  = new StringBuilder();

                while ((inputLine = in.readLine()) != null)
                    response.append(inputLine);
                final String r = response.toString();
                videoData = r.substring(r.indexOf(DATA_TAG) + DATA_TAG.length(), r.indexOf(DATA_TAG_END));
                in.close();
            }
            catch (final IOException e) {
                error(e);
            }

            return videoData;
        }  // end method resolveVideoData
    }

    //~ Inner Classes ................................................................................................................................

    private class ExternalData {
        String  main  = null;
        String  name  = null;
        String  thumb = null;
        boolean valid = true;

        private ExternalData(String url) {
            // assume external file
            main  = url;
            valid = true;
            name  = url.substring(url.lastIndexOf('/') + 1, url.length());
            thumb = "";

            // check if video
            if (getMimeType(url).contains(APPLICATION_OCTET_STREAM.getMime())) resolveVideo(url);
        }

        private void resolveVideo(String url) {
            if (url.contains(YOUTUBE_EMBED_SHORT)) {
                main = url;
                final String id = url.substring(url.lastIndexOf("/") + 1, url.length());
                thumb = YOUTUBE_THUMB_URL + id + "/0.jpg";
                name  = VideoData.NAME.resolveVideoData(id, true);
            }
            else if (url.contains(VIMEO_EMBED_SHORT)) {
                main = url;
                final String id = url.substring(url.lastIndexOf("/") + 1, url.length());
                thumb = VideoData.THUMB.resolveVideoData(id, false);
                name  = VideoData.NAME.resolveVideoData(id, false);
            }
            else {
                final RegExp      a    = RegExp.compile(
                        "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:[youtu\\.be\\/|youtube\\.com | vimeo.com]\\S*[^\\w\\-\\s])([\\w\\-]{4,15})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*",
                        "i");
                final MatchResult exec = a.exec(url);
                valid = exec != null && (url.contains("y") || url.contains("v"));
                if (valid) {
                    final String id = exec.getGroup(1);
                    if (isEmpty(id)) valid = false;
                    if (id.length() == YOUTUBE_ID_LENGTH) {
                        main  = YOUTUBE_EMBED + id;
                        thumb = YOUTUBE_THUMB_URL + id + "/0.jpg";
                        name  = VideoData.NAME.resolveVideoData(id, true);
                    }
                    else {
                        main  = VIMEO_EMBED + id;
                        thumb = VideoData.THUMB.resolveVideoData(id, false);
                        name  = VideoData.NAME.resolveVideoData(id, false);
                    }
                }
            }
        }

        private boolean isValid() {
            return valid;
        }
        @NotNull private String getMain() {
            return main;
        }
        @NotNull private String getName() {
            return name;
        }
        @NotNull private String getThumb() {
            return thumb;
        }
    }  // end class ExternalData
}  // end class DbResourceUploadServlet
