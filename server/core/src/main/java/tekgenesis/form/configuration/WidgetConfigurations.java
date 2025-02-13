
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.configuration;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.common.Predefined.cast;

/**
 * Widget configurations utility class.
 */
public class WidgetConfigurations {

    //~ Constructors .................................................................................................................................

    private WidgetConfigurations() {}

    //~ Methods ......................................................................................................................................

    /** Create specific widget configuration. */
    public static <T extends WidgetConfiguration> T create(@NotNull final Model model, int ordinal) {
        final Widget widget = model.widgetByEnumOrdinal(ordinal);
        return createWidgetConfiguration(model, widget);
    }

    private static <T extends WidgetConfiguration> T createWidgetConfiguration(@NotNull final Model model, @NotNull Widget widget) {
        switch (widget.getWidgetType()) {
        case MAP:
            return cast(new MapConfigurationImpl(model, widget));
        case CHART:
            return cast(new ChartConfigurationImpl(model, widget));
        case DYNAMIC:
            return cast(new DynamicConfigurationImpl(model, widget));
        case SUBFORM:
            return cast(new SubformConfigurationImpl(model, widget));
        case UPLOAD:
            return cast(new UploadConfigurationImpl(model, widget));
        case DATE_BOX:
        case DATE_PICKER:
        case DATE_TIME_BOX:
        case DOUBLE_DATE_BOX:
            return cast(new DateConfigurationImpl(model, widget));
        case RADIO_GROUP:
            return cast(new RadioGroupConfigurationImpl(model, widget));
        default:
            throw new IllegalStateException("Cannot configure widget of type: '" + widget.getWidgetType() + "'");
        }
    }
}
