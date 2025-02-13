
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

import tekgenesis.type.DynamicTypeConverter;

/**
 * Configuration for Dynamic widgets.
 */
@SuppressWarnings("UnusedReturnValue")
public interface DynamicConfiguration extends WidgetConfiguration {

    //~ Methods ......................................................................................................................................

    /** Return dynamic multiple option. */
    boolean isMultiple();

    /** Set dynamic multiple option. */
    @NotNull DynamicConfiguration setMultiple(boolean m);

    /** Set dynamic secret option. */
    @NotNull DynamicConfiguration setSecret(boolean s);

    /** Return dynamic secret option. */
    boolean isSecret();

    /** Get dynamic type configuration. */
    @NotNull DynamicTypeConfiguration getTypeConfiguration();

    /** Get dynamic type converter. */
    @NotNull DynamicTypeConverter getTypeConverter();

    /** Get dynamic widget. */
    @NotNull DynamicWidget getWidget();

    /**
     * Set dynamic widget. Throws IllegalStateException if setting an incompatible widget for the
     * specified type.
     */
    @NotNull DynamicConfiguration setWidget(@NotNull final DynamicWidget widget);

    //~ Enums ........................................................................................................................................

    enum DynamicWidget { NONE, TEXT_FIELD, TAGS, TAGS_COMBO_BOX, RADIO_GROUP, CHECK_BOX_GROUP, COMBO_BOX, RANGE_VALUE, RANGE, MAIL_FIELD }
}  // end interface DynamicConfiguration
