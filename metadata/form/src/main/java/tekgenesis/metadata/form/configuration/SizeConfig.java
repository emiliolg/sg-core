
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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;

/**
 * Size serializable configuration.
 */
@SuppressWarnings("UnusedReturnValue")
abstract class SizeConfig extends WidgetConfig implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private int height;
    private int width;

    //~ Constructors .................................................................................................................................

    SizeConfig() {
        width  = DEFAULT_WIDTH;
        height = DEFAULT_HEIGHT;
    }

    //~ Methods ......................................................................................................................................

    /** Sets width and height. */
    @NotNull public SizeConfig dimension(final Integer w, final Integer h) {
        width  = w;
        height = h;
        return this;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SizeConfig)) return false;

        final SizeConfig that = (SizeConfig) o;

        return height == that.height && width == that.width;
    }

    @Override public int hashCode() {
        int result = height;
        result = 31 * result + width;
        return result;
    }

    /** Set height. */
    @NotNull public SizeConfig height(final Integer h) {
        height = h;
        return this;
    }

    /** Set width. */
    @NotNull public SizeConfig width(final Integer w) {
        width = w;
        return this;
    }

    /** Return height. */
    public int getHeight() {
        return height;
    }

    /** Returns height in String 'px' format. */
    public String getHeightPx() {
        return height + PX;
    }

    /** Returns width. */
    public int getWidth() {
        return width;
    }

    /** Returns width in String 'px' format. */
    public String getWidthPx() {
        return width + PX;
    }

    @Override void deserializeFields(StreamReader r) {
        width  = r.readInt();
        height = r.readInt();
    }

    @Override void serializeFields(StreamWriter w) {
        w.writeInt(width);
        w.writeInt(height);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -826097309459230484L;

    @NonNls private static final String PX = "px";

    private static final int DEFAULT_WIDTH  = 700;
    private static final int DEFAULT_HEIGHT = 500;
}  // end class SizeConfig
