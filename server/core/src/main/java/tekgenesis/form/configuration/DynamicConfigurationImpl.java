
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.configuration;

import java.util.EnumMap;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.configuration.DynamicConfig;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.metadata.form.widget.WidgetTypes;
import tekgenesis.type.DynamicTypeConverter;
import tekgenesis.type.DynamicTypeConverterImpl;
import tekgenesis.type.Type;

import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.metadata.form.widget.WidgetTypes.supports;

class DynamicConfigurationImpl extends AbstractWidgetConfiguration<DynamicConfig> implements DynamicConfiguration {

    //~ Constructors .................................................................................................................................

    DynamicConfigurationImpl(@NotNull final Model model, @NotNull final Widget widget) {
        super(model, widget);
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean isMultiple() {
        return config().isMultiple();
    }

    @NotNull @Override public DynamicConfiguration setMultiple(boolean m) {
        config().setMultiple(m);
        return this;
    }

    @NotNull @Override public DynamicConfiguration setSecret(boolean s) {
        config().setSecret(s);
        return this;
    }

    public void setSigned() {
        config().signed();
    }

    @Override public boolean isSecret() {
        return config().isSecret();
    }

    @NotNull @Override public DynamicTypeConfiguration getTypeConfiguration() {
        return new DynamicTypeConfigurationImpl(this);
    }

    @NotNull @Override public DynamicTypeConverter getTypeConverter() {
        final Type t = config().getType();
        return new DynamicTypeConverterImpl(t);
    }

    @NotNull @Override public DynamicWidget getWidget() {
        final WidgetType w = config().getWidget();
        return ensureNotNull(TYPE_TO_WIDGET.get(w), "Unsupported widget for type '" + w + "'");
    }

    @NotNull @Override public DynamicConfiguration setWidget(@NotNull final DynamicWidget w) {
        final WidgetType widgetType = ensureNotNull(WIDGET_TO_TYPE.get(w), "Unsupported type for widget '" + w + "'");
        if (widgetType != WidgetType.NONE && !supports(widgetType, config().getType()))
            throw new RuntimeException(
                "Attempting widget '" + widgetType + "' with incompatible type '" + config().getType() + "'. " +
                "Set a compatible type first. ");
        config().setWidget(widgetType);
        return this;
    }

    @NotNull @Override DynamicConfig createConfig() {
        return new DynamicConfig();
    }

    void setType(@NotNull Type type) {
        config().setType(type).setWidget(notNull(WidgetTypes.fromType(type, false, false), WidgetType.TEXT_FIELD));
    }

    //~ Methods ......................................................................................................................................

    private static void put(@NotNull final DynamicWidget widget, @NotNull final WidgetType type) {
        WIDGET_TO_TYPE.put(widget, type);
        TYPE_TO_WIDGET.put(type, widget);
    }

    //~ Static Fields ................................................................................................................................

    private static final EnumMap<DynamicWidget, WidgetType> WIDGET_TO_TYPE = new EnumMap<>(DynamicWidget.class);

    private static final EnumMap<WidgetType, DynamicWidget> TYPE_TO_WIDGET = new EnumMap<>(WidgetType.class);

    static {
        // Dynamic widget mappings
        put(DynamicWidget.TEXT_FIELD, WidgetType.TEXT_FIELD);
        put(DynamicWidget.MAIL_FIELD, WidgetType.MAIL_FIELD);
        put(DynamicWidget.TAGS, WidgetType.TAGS);
        put(DynamicWidget.TAGS_COMBO_BOX, WidgetType.TAGS_COMBO_BOX);
        put(DynamicWidget.RADIO_GROUP, WidgetType.RADIO_GROUP);
        put(DynamicWidget.CHECK_BOX_GROUP, WidgetType.CHECK_BOX_GROUP);
        put(DynamicWidget.COMBO_BOX, WidgetType.COMBO_BOX);
        put(DynamicWidget.RANGE, WidgetType.RANGE);
        put(DynamicWidget.RANGE_VALUE, WidgetType.RANGE_VALUE);
        put(DynamicWidget.NONE, WidgetType.NONE);
    }
}  // end class DynamicConfigurationImpl
