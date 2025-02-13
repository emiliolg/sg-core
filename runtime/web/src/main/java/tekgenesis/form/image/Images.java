
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Resource;
import tekgenesis.type.resource.Variant;

/**
 * Utility class to deal with Images.
 */
public class Images {

    //~ Constructors .................................................................................................................................

    private Images() {}

    //~ Methods ......................................................................................................................................

    /**
     * Builds a variant of the specified resource and stores it into a file or outstream.
     *
     * <p>Sample :</p>
     *
     * <p>Images.build(Variant.THUMB).of(resource).using(ImageResizer.getInstance());</p>
     */
    public static Builder build(@NotNull Variant variant) {
        return new Builder(variant);
    }

    /**
     * Builds a variant of the specified resource and stores it into a file or outstream. This
     * constructor is used to build custom variants. In order to do it, the builder requires a
     * variant name and the width and height of that variant.
     *
     * <p>Sample :</p>
     *
     * <p>Images.build(customName, WIDTH, HEIGHT).of(resource,
     * resource.getMaster().getMetadata().getSize()) .using(ImageResizer.getInstance());</p>
     *
     * @param   variantName:  the name of the variant
     * @param   width:        the width of the variant
     * @param   height:       the height of the variant
     *
     * @return  Builder
     */
    public static Builder build(String variantName, int width, int height) {
        return new Builder(variantName, width, height);
    }

    /** Input stream for given resource. */
    @NotNull public static InputStream inputStream(@NotNull Resource.Content content) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        content.copyTo(outputStream);

        // There is no need of closing ByteArray*Streams
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    /** Get format type based on mime type. */
    @NotNull
    @SuppressWarnings("IfStatementWithTooManyBranches")
    public static String getFormatType(@NotNull Resource.Content content) {
        final String mimeType = content.getMimeType().toLowerCase();

        if (mimeType.contains("jpg") || mimeType.contains("jpeg")) return "jpeg";
        else if (mimeType.contains("png")) return "png";
        else if (mimeType.contains("gif")) return "gif";
        else if (mimeType.contains("bmp")) return "bmp";
        else if (mimeType.contains("svg")) return "svg";
        else throw new IllegalStateException("MIME type: " + mimeType + " not supported.");
    }

    //~ Inner Classes ................................................................................................................................

    public static class Builder {
        private final int    height;
        private long         originalSize;
        private Resource     resource    = null;
        private final String variantName;
        private final int    width;

        /** Image resizer builder. */
        public Builder(@NotNull Variant variant) {
            variantName = variant.getVariantName();
            width       = variant.getWidth();
            height      = variant.getHeight();
        }

        /** Image resizer builder. */
        private Builder(String variantName, int width, int height) {
            this.variantName = variantName;
            this.width       = width;
            this.height      = height;
        }

        /** Image resizer of given resource. */
        @NotNull public Builder of(@NotNull final Resource r, final long s) {
            resource     = r;
            originalSize = s;
            return this;
        }

        /** Resize algotrithm. */
        public void using(@NotNull final ImageResizer resizer)
            throws IOException
        {
            if (resource != null && resource.getEntry(variantName) == null) {
                final byte[]           resized = resizer.resize(resource.getMaster(), ImageResizer.OUTPUT_QUALITY, width, height);
                final Resource.Factory factory = resource.addVariant(variantName);
                if (resized.length < originalSize)
                    factory.upload(resource.getMaster().getName(), resource.getMaster().getMimeType(), new ByteArrayInputStream(resized));
                else factory.uploadFromSha(resource.getMaster().getName(), resource.getMaster().getSha(), resource.getMaster().getMimeType());
            }
        }
    }  // end class Builder
}  // end class Images
