
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
import org.jetbrains.annotations.Nullable;

import tekgenesis.metadata.form.configuration.WidgetConfig;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.common.Predefined.cast;

abstract class AbstractWidgetConfiguration<T extends WidgetConfig> implements WidgetConfiguration {

    //~ Instance Fields ..............................................................................................................................

    @NotNull final Model  model;
    @NotNull final Widget widget;

    @Nullable private T config;

    //~ Constructors .................................................................................................................................

    AbstractWidgetConfiguration(@NotNull Model model, @NotNull Widget widget) {
        this.model  = model;
        this.widget = widget;
        config      = null;
    }

    //~ Methods ......................................................................................................................................

    @NotNull final T config() {
        if (config == null) {
            // If exists, retrieve it.
            config = cast(model.getFieldConfig(widget));
            if (config == null) {
                config = createConfig();
                model.setFieldConfig(widget, config);
            }
        }
        return config;
    }

    /** Create default configuration. */
    @NotNull abstract T createConfig();
}
