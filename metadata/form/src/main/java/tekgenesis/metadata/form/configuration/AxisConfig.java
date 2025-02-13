
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

import static java.lang.Double.compare;

/**
 * Charts axis config.
 */
public class AxisConfig implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private String  label;
    private double  maximum;
    private boolean maxsSet;
    private double  minimum;
    private boolean minsSet;
    private boolean secondary;
    private boolean visible;

    //~ Constructors .................................................................................................................................

    /** Creates an axis config. */
    public AxisConfig() {
        visible   = true;
        minimum   = 0D;
        label     = "";
        secondary = false;
    }

    //~ Methods ......................................................................................................................................

    /** Sets axis label. */
    public void axisLabel(String l) {
        label = l;
    }

    /** Sets this axis minimum. */
    public void axisMaximum(double value) {
        maximum = value;
        maxsSet = true;
    }

    /** Sets this axis minimum. */
    public void axisMinimum(double value) {
        minimum = value;
        minsSet = true;
    }

    /** Renders axis on the right (left is by default). */
    public void axisToSecondary() {
        secondary = true;
    }

    /** Sets this axis visibility. */
    public void axisVisible(boolean v) {
        visible = v;
    }

    /** Deserialize. */
    public void deserializeFields(StreamReader r) {
        label     = r.readString();
        minimum   = r.readDouble();
        minsSet   = r.readBoolean();
        maximum   = r.readDouble();
        maxsSet   = r.readBoolean();
        visible   = r.readBoolean();
        secondary = r.readBoolean();
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final AxisConfig that = (AxisConfig) o;

        return compare(that.minimum, minimum) == 0 && minsSet == that.minsSet && compare(that.maximum, maximum) == 0 && maxsSet == that.maxsSet &&
               visible == that.visible && !(label != null ? !label.equals(that.label) : that.label != null) && secondary == that.secondary;
    }

    @Override public int hashCode() {
        int        result = label != null ? label.hashCode() : 0;
        final long temp   = Double.doubleToLongBits(minimum);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        final long temp2 = Double.doubleToLongBits(maximum);
        result = 31 * result + (int) (temp2 ^ (temp2 >>> 32));
        result = 31 * result + (minsSet ? 1 : 0);
        result = 31 * result + (maxsSet ? 1 : 0);
        result = 31 * result + (secondary ? 1 : 0);
        result = 31 * result + (visible ? 1 : 0);
        return result;
    }

    /** Resets minimums. */
    public void resetMaximums() {
        maximum = 0D;
        maxsSet = false;
    }

    /** Resets minimums. */
    public void resetMinimums() {
        minimum = 0D;
        minsSet = false;
    }

    /** Serialize. */
    public void serializeFields(StreamWriter w) {
        w.writeString(label);
        w.writeDouble(minimum);
        w.writeBoolean(minsSet);
        w.writeDouble(maximum);
        w.writeBoolean(maxsSet);
        w.writeBoolean(visible);
        w.writeBoolean(secondary);
    }

    /** Returns true if axis is visible. */
    public boolean isVisible() {
        return visible;
    }

    /** Returns this axis label. */
    public String getLabel() {
        return label;
    }

    /** Returns this axis maximums. */
    public double getMaximum() {
        return maximum;
    }

    /** Returns this axis minimums. */
    public double getMinimum() {
        return minimum;
    }

    /** Is maximum set? */
    public boolean isMaximumSet() {
        return maxsSet;
    }

    /** Is minimum set? */
    public boolean isMinimumSet() {
        return minsSet;
    }

    /** Renders axis on the secondary axis right for Columns and Lines and top for Bars. */
    public boolean isAxisOnSecondary() {
        return secondary;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5190822285293786787L;

    //~ Enums ........................................................................................................................................

    public enum Axis { X, Y }
}  // end class AxisConfig
