
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

import tekgenesis.metadata.form.configuration.UploadConfig;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.widget.Widget;

class UploadConfigurationImpl extends AbstractWidgetConfiguration<UploadConfig> implements UploadConfiguration {

    //~ Constructors .................................................................................................................................

    UploadConfigurationImpl(@NotNull final Model model, @NotNull final Widget widget) {
        super(model, widget);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public UploadConfiguration crop(boolean crop) {
        config().crop(crop);
        return this;
    }

    @NotNull @Override public UploadConfiguration maxSize(int maxWidth, int maxHeight) {
        config().maxSize(maxWidth, maxHeight);
        return this;
    }

    @NotNull @Override public UploadConfiguration minSize(int minWidth, int minHeight) {
        config().minSize(minWidth, minHeight);
        return this;
    }

    @NotNull @Override public UploadConfiguration ratio(int widthRatio, int heightRatio) {
        config().ratio(widthRatio, heightRatio);
        return this;
    }

    @NotNull @Override public UploadConfiguration size(int width, int height) {
        config().size(width, height);
        return this;
    }

    @NotNull @Override UploadConfig createConfig() {
        return new UploadConfig();
    }
}
