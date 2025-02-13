
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
import tekgenesis.metadata.form.widget.WidgetType;

/**
 * Subform widget serializable configuration.
 */
public class SubformConfig extends WidgetConfig implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private boolean visible;

    //~ Constructors .................................................................................................................................

    /** Create default subform configuration. */
    public SubformConfig() {
        visible = false;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubformConfig)) return false;
        final SubformConfig that = (SubformConfig) o;
        return visible == that.visible;
    }

    @Override public int hashCode() {
        return (visible ? 1 : 0);
    }

    /** Return subform visibility. */
    public boolean isVisible() {
        return visible;
    }

    /** Set subform visibility. */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override void deserializeFields(StreamReader r) {
        visible = r.readBoolean();
    }

    @Override void serializeFields(StreamWriter w) {
        w.writeBoolean(visible);
    }

    @Override WidgetType getWidgetType() {
        return WidgetType.SUBFORM;
    }

    //~ Static Fields ................................................................................................................................

    public static SubformConfig DEFAULT = new SubformConfig();

    private static final long serialVersionUID = 4972025091358273449L;
}  // end class SubformConfig
