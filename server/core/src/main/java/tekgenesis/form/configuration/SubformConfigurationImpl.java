
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

import tekgenesis.metadata.form.configuration.SubformConfig;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.widget.Widget;

class SubformConfigurationImpl extends AbstractWidgetConfiguration<SubformConfig> implements SubformConfiguration {

    //~ Constructors .................................................................................................................................

    SubformConfigurationImpl(@NotNull Model model, @NotNull Widget widget) {
        super(model, widget);
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean isVisible() {
        return config().isVisible();
    }

    @NotNull @Override public SubformConfiguration setVisible(boolean visible) {
        config().setVisible(visible);
        return this;
    }

    @NotNull @Override SubformConfig createConfig() {
        return new SubformConfig();
    }
}
