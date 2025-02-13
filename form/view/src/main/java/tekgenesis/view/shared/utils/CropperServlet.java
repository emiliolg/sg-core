
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.node.ObjectNode;

import net.coobird.thumbnailator.Thumbnails;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Resource;
import tekgenesis.common.json.JsonMapping;
import tekgenesis.form.image.ImageResizer;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.form.image.Images.build;
import static tekgenesis.type.resource.Variant.LARGE;
import static tekgenesis.type.resource.Variant.THUMB;

/**
 * Common servlet crop actions.
 */
@SuppressWarnings("NonJREEmulationClassesInClientCode")
public class CropperServlet extends HttpServlet {

    //~ Methods ......................................................................................................................................

    /** For images, we generate predefined thumb and large variants. */
    protected Resource generateImageVariants(@NotNull final Resource resource, long originalSize)
        throws UncheckedIOException
    {
        try {
            build(THUMB).of(resource, originalSize).using(ImageResizer.getInstance());
            build(LARGE).of(resource, originalSize).using(ImageResizer.getInstance());
            return resource;
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** Generate cropped image from stream. */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    protected InputStream getInputStream(HttpServletRequest req, InputStream bis)
        throws IOException
    {
        final String cropWidth = req.getParameter("width");  // if has crop
        if (!isEmpty(cropWidth)) {
            final int left   = Integer.parseInt(req.getParameter("left"));
            final int top    = Integer.parseInt(req.getParameter("top"));
            final int width  = Integer.parseInt(cropWidth);
            final int height = Integer.parseInt(req.getParameter("height"));
            return cropImage(bis, left, top, width, height);
        }
        return bis;
    }

    private InputStream cropImage(InputStream inputStream, int left, int top, int width, int height)
        throws IOException
    {
        final Thumbnails.Builder<? extends InputStream> builder = Thumbnails.of(inputStream).sourceRegion(left, top, width, height).scale(1.0);
        final ByteArrayOutputStream                     os      = new ByteArrayOutputStream();
        builder.toOutputStream(os);
        return new ByteArrayInputStream(os.toByteArray());
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 3693571790395338255L;

    //~ Inner Classes ................................................................................................................................

    public static class PostResponse {
        private PostResponse() {}

        /** Error message. */
        public static ObjectNode error(final Throwable e) {
            return error(e.getMessage());
        }

        /** Error message. */
        public static ObjectNode error(final String errorMsg) {
            final ObjectNode json = create(false);
            json.put("msg", errorMsg);
            return json;
        }

        /** Success message. */
        @SuppressWarnings("DuplicateStringLiteralInspection")
        public static ObjectNode success(String uuid, boolean external, String name, String masterUrl, @Nullable final String thumbUrl,
                                         final String mimeType) {
            final ObjectNode json = create(true);
            json.put("uuid", uuid);
            json.put("external", external);
            json.put("name", name);
            json.put("url", masterUrl);
            json.put("thumbUrl", thumbUrl);
            json.put("mimeType", mimeType);
            return json;
        }

        private static ObjectNode create(boolean uploaded) {
            final ObjectNode json = JsonMapping.shared().createObjectNode();
            json.put("uploaded", uploaded);
            return json;
        }
    }
}  // end class CropperServlet
