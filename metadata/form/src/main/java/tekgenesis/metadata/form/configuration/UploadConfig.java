
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.configuration;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.metadata.form.widget.WidgetType;

/**
 * Upload widget serializable configuration.
 */
@SuppressWarnings("UnusedReturnValue")
public class UploadConfig extends WidgetConfig implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private boolean crop;
    private int     heightR;
    private int     maxH;

    private int maxW;
    private int minH;
    private int minW;
    private int widthR;

    //~ Constructors .................................................................................................................................

    /** Create default upload configuration. */
    public UploadConfig() {
        maxW    = 0;
        maxH    = 0;
        minW    = 0;
        minH    = 0;
        widthR  = 0;
        heightR = 0;
        crop    = false;
    }

    //~ Methods ......................................................................................................................................

    /** Set image ratio. */
    @NotNull public UploadConfig crop(boolean withCrop) {
        crop = withCrop;
        return this;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UploadConfig)) return false;
        final UploadConfig that = (UploadConfig) o;
        return maxH == that.maxH && minH == that.minH && maxW == that.maxW && minW == that.minW && widthR == that.widthR && heightR == that.heightR &&
               crop == that.crop;
    }

    @Override public int hashCode() {
        int result = 31 * maxW;
        result = 31 * result + minW;
        result = 31 * result + maxH;
        result = 31 * result + minH;
        result = 31 * result + widthR;
        result = 31 * result + heightR;
        result = result + (crop ? 1 : 0);
        return result;
    }

    /** Set max image size. */
    @NotNull public UploadConfig maxSize(final int maxWidth, final int maxHeight) {
        maxW = maxWidth;
        maxH = maxHeight;
        return this;
    }

    /** Set min image size. */
    @NotNull public UploadConfig minSize(final int minWidth, final int minHeight) {
        minW = minWidth;
        minH = minHeight;
        return this;
    }

    /** Set image ratio. */
    @NotNull public UploadConfig ratio(final int widthRatio, final int heightRatio) {
        widthR  = widthRatio;
        heightR = heightRatio;
        return this;
    }

    /** Set exact image size. */
    @NotNull public UploadConfig size(final int width, final int height) {
        minW = width;
        maxW = width;
        minH = height;
        maxH = height;
        return this;
    }

    /** Return height ratio. */
    public boolean getCrop() {
        return crop;
    }

    /** Return height ratio. */
    public int getHeightRatio() {
        return heightR;
    }

    /** Return max height. */
    public int getMaxHeight() {
        return maxH;
    }

    /** Return max width. */
    public int getMaxWidth() {
        return maxW;
    }

    /** Return min height. */
    public int getMinHeight() {
        return minH;
    }

    /** Return min width. */
    public int getMinWidth() {
        return minW;
    }

    /** Return size ratio. */
    public float getSizeRatio() {
        return heightR != 0 ? ((float) widthR) / heightR : 0;
    }

    /** Return width ratio. */
    public int getWidthRatio() {
        return widthR;
    }

    @Override void deserializeFields(StreamReader r) {
        maxW    = r.readInt();
        maxH    = r.readInt();
        minW    = r.readInt();
        minH    = r.readInt();
        widthR  = r.readInt();
        heightR = r.readInt();
        crop    = r.readBoolean();
    }

    @Override void serializeFields(StreamWriter w) {
        w.writeInt(maxW);
        w.writeInt(maxH);
        w.writeInt(minW);
        w.writeInt(minH);
        w.writeInt(widthR);
        w.writeInt(heightR);
        w.writeBoolean(crop);
    }

    @Override WidgetType getWidgetType() {
        return WidgetType.UPLOAD;
    }

    //~ Static Fields ................................................................................................................................

    public static UploadConfig DEFAULT = new UploadConfig();

    private static final long serialVersionUID = 7903787808354995048L;
}  // end class UploadConfig
