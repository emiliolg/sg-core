
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

import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;

/**
 * Map's Marker Configuration.
 */
public class MarkerConfig implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private String color = "";

    private int    row;
    private double size;

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public MarkerConfig() {}

    //~ Methods ......................................................................................................................................

    /** Deserialize Marker's fields. */
    public void deserializeFields(StreamReader r) {
        row   = r.readInt();
        size  = r.readDouble();
        color = r.readString();
    }

    /** Serialize Marker's fields. */
    public void serializeFields(StreamWriter w) {
        w.writeInt(row);
        w.writeDouble(size);
        w.writeString(color);
    }

    /** Returns the marker's color path. */
    public String getColor() {
        return color;
    }

    /**
     * Set the marker size and color, indicating to which row of the map and in what scale and what
     * color to display it.
     */
    public MarkerConfig setConfig(int rowNumber, double scale, String path) {
        row   = rowNumber;
        size  = scale;
        color = path;
        return this;
    }

    /** Returns the row index of the map (which marker). */
    public int getRow() {
        return row;
    }

    /** Returns the marker's proportion size. */
    public double getSize() {
        return size;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 4890119547113393838L;
}  // end class MarkerConfig
