
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.image;

// ...............................................................................................................................
//
// (C) Copyright  2011/2013 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.resizers.configurations.AlphaInterpolation;
import net.coobird.thumbnailator.resizers.configurations.Antialiasing;
import net.coobird.thumbnailator.resizers.configurations.Rendering;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Resource;
import tekgenesis.common.util.Files;

import static tekgenesis.form.image.Images.getFormatType;
import static tekgenesis.form.image.Images.inputStream;

/**
 * Image resizer implementations. For now, we are using Thumbnailator, but Scalr was also tested in
 * Genesis project.
 */
public enum ImageResizer {

    //~ Enum constants ...............................................................................................................................

    THUMBNAILATOR {
        @Override public byte[] resize(@NotNull final Resource.Content resource, final double quality, final int width, final int height)
            throws IOException
        {
            final InputStream is = inputStream(resource);
            try {
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();

                commonBuilder(getFormatType(resource), baos, Thumbnails.of(is).size(width, height).outputQuality(quality));

                return baos.toByteArray();
            }
            finally {
                Files.close(is);
            }
        }

        @Override public byte[] width(@NotNull Resource.Content resource, double quality, int width)
            throws IOException
        {
            final InputStream is = inputStream(resource);
            try {
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();

                commonBuilder(getFormatType(resource), baos, Thumbnails.of(is).width(width).outputQuality(quality));

                return baos.toByteArray();
            }
            finally {
                Files.close(is);
            }
        }

        private void commonBuilder(String formatType, ByteArrayOutputStream baos, Thumbnails.Builder<? extends InputStream> size)
            throws IOException
        {
            size.alphaInterpolation(AlphaInterpolation.QUALITY)
                .antialiasing(Antialiasing.ON)
                .rendering(Rendering.QUALITY)
                .outputFormat(formatType)
                .toOutputStream(baos);
        }};

    //~ Methods ......................................................................................................................................

    /**
     * Resize given resource content to fit specified width and height. The aspect ratio of the
     * original image will be preserved.
     */
    public abstract byte[] resize(@NotNull final Resource.Content resource, final double outputQuality, final int width, final int height)
        throws IOException;

    /**
     * Resize given resource content to specified width. The aspect ratio of the original image will
     * be preserved.
     */
    public abstract byte[] width(@NotNull final Resource.Content resource, final double outputQuality, final int width)
        throws IOException;

    //~ Methods ......................................................................................................................................

    /** Image resizer default implementation. */
    public static ImageResizer getInstance() {
        return THUMBNAILATOR;
    }

    //~ Static Fields ................................................................................................................................

    public static final double OUTPUT_QUALITY = 0.75;
}
