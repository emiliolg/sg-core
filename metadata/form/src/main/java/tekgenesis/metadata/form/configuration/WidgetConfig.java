
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
 * Widget serializable configuration tagging interface.
 */
@SuppressWarnings("UnusedReturnValue")
public abstract class WidgetConfig implements Serializable {

    //~ Constructors .................................................................................................................................

    WidgetConfig() {}

    //~ Methods ......................................................................................................................................

    /** Configuration serialization to writer. */
    public void serialize(StreamWriter w) {
        w.writeInt(getWidgetType().ordinal());
        serializeFields(w);
    }

    /** Configuration deserialization from reader. */
    @SuppressWarnings("WeakerAccess")
    abstract void deserializeFields(StreamReader r);
    abstract void serializeFields(StreamWriter w);
    abstract WidgetType getWidgetType();

    //~ Methods ......................................................................................................................................

    /** Configuration instantiation from reader. */
    public static WidgetConfig instantiate(StreamReader r) {
        final WidgetType w = WidgetType.values()[r.readInt()];

        switch (w) {
        case MAP:
            final MapConfig mapConfig = new MapConfig();
            mapConfig.deserializeFields(r);
            return mapConfig;
        case CHART:
            final ChartConfig chartConfig = new ChartConfig();
            chartConfig.deserializeFields(r);
            return chartConfig;
        case DYNAMIC:
            final DynamicConfig dynamicConfig = new DynamicConfig();
            dynamicConfig.deserializeFields(r);
            return dynamicConfig;
        case SUBFORM:
            final SubformConfig subformConfig = new SubformConfig();
            subformConfig.deserializeFields(r);
            return subformConfig;
        case UPLOAD:
            final UploadConfig uploadConfig = new UploadConfig();
            uploadConfig.deserializeFields(r);
            return uploadConfig;
        case DATE_BOX:
        case DATE_PICKER:
        case DATE_TIME_BOX:
        case DOUBLE_DATE_BOX:
            final DateConfig dateConfig = new DateConfig();
            dateConfig.deserializeFields(r);
            return dateConfig;
        case RADIO_GROUP:
            final RadioGroupConfig radioGroupConfig = new RadioGroupConfig();
            radioGroupConfig.deserializeFields(r);
            return radioGroupConfig;
        default:
            throw new IllegalStateException("Attempting to deserialize an unknown configuration for type " + w);
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -826097309459230484L;
}  // end class WidgetConfig
