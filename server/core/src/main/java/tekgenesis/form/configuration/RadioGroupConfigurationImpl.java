
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.configuration;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.configuration.RadioGroupConfig;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.widget.Widget;

class RadioGroupConfigurationImpl extends AbstractWidgetConfiguration<RadioGroupConfig> implements RadioGroupConfiguration {

    //~ Constructors .................................................................................................................................

    RadioGroupConfigurationImpl(Model model, Widget widget) {
        super(model, widget);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public RadioGroupConfiguration styleClasses(@NotNull List<String> classes) {
        config().styleClasses(classes);
        return this;
    }

    @NotNull @Override RadioGroupConfig createConfig() {
        return new RadioGroupConfig();
    }
}
