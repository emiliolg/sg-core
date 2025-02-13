
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
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.annotation.GwtIncompatible;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.metadata.form.widget.WidgetType;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

import static tekgenesis.common.Predefined.equalElements;
import static tekgenesis.common.collections.Colls.emptyList;

/**
 * Date widget serializable configuration.
 */
@SuppressWarnings("UnusedReturnValue")
public class DateConfig extends WidgetConfig implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private Map<DateOnly, String> daysStyles = new HashMap<>();

    private List<DateOnly> disabled     = emptyList();
    private List<Boolean>  disabledDays = new ArrayList<>(7);
    private List<DateOnly> enabled      = emptyList();

    //~ Constructors .................................................................................................................................

    /** Create default upload configuration. */
    public DateConfig() {
        resetDays();
    }

    //~ Methods ......................................................................................................................................

    /** Define a style class for a day. */
    public DateConfig addStyleToDay(DateOnly date, String styleClass) {
        daysStyles.put(date, styleClass);
        return this;
    }

    /** Disable a day of the week. */
    @GwtIncompatible @NotNull public DateConfig disable(@NotNull final DayOfWeek day) {
        disabledDays.set(day.ordinal(), true);
        return this;
    }

    /** Disables specific dates. */
    @NotNull public DateConfig disableDates(List<DateOnly> dates) {
        disabled = dates;
        return this;
    }

    /** Disables Saturdays. */
    @GwtIncompatible @NotNull public DateConfig disableSaturdays() {
        disabledDays.set(SATURDAY.ordinal(), true);
        return this;
    }

    /** Disables Sundays. */
    @GwtIncompatible @NotNull public DateConfig disableSundays() {
        disabledDays.set(SUNDAY.ordinal(), true);
        return this;
    }

    /** Disables weekends. */
    @GwtIncompatible @NotNull public DateConfig disableWeekends() {
        disableSaturdays();
        disableSundays();
        return this;
    }

    /** Enables specific dates. */
    @NotNull public DateConfig enabledDates(List<DateOnly> dates) {
        enabled = dates;
        return this;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DateConfig)) return false;

        final DateConfig that = (DateConfig) o;

        return equalElements(disabled, that.disabled) && equalElements(disabledDays, that.disabledDays) && equalElements(enabled, that.enabled) &&
               daysStyles != null && equalElements(daysStyles.keySet(), that.daysStyles.keySet());
    }

    @Override public int hashCode() {
        int result = disabled != null ? disabled.hashCode() : 0;
        result = 31 * result + (disabledDays != null ? disabledDays.hashCode() : 0);
        result = 31 * result + (enabled != null ? enabled.hashCode() : 0);
        result = 31 * result + (daysStyles != null ? daysStyles.hashCode() : 0);
        return result;
    }

    /** Resets this configuration. */
    @NotNull public DateConfig reset() {
        disabled     = emptyList();
        enabled      = emptyList();
        disabledDays = new ArrayList<>(7);
        daysStyles   = new HashMap<>();
        resetDays();
        return this;
    }

    /** Is day of week disabled?. */
    public boolean isDayOfWeekDisabled(final int index) {
        return disabledDays.get(index);
    }

    /** Returns the days styles. */
    public Map<DateOnly, String> getDaysStyles() {
        return daysStyles;
    }

    /** Returns the list of disabled dates. */
    public List<DateOnly> getDisabled() {
        return disabled;
    }

    /** Returns the list of enabled dates. */
    public List<DateOnly> getEnabled() {
        return enabled;
    }

    @Override void deserializeFields(StreamReader r) {
        disabledDays = r.readBooleans();
        disabled     = r.readDates();
        enabled      = r.readDates();
        daysStyles   = r.readDatesMap();
    }

    @Override void serializeFields(StreamWriter w) {
        w.writeBooleans(disabledDays);
        w.writeDates(disabled);
        w.writeDates(enabled);
        w.writeDatesMap(daysStyles);
    }

    @Override WidgetType getWidgetType() {
        return WidgetType.DATE_BOX;
    }

    private void resetDays() {
        for (int i = 0; i < 7; i++)
            disabledDays.add(false);
    }

    //~ Static Fields ................................................................................................................................

    public static final DateConfig DEFAULT = new DateConfig();

    private static final long serialVersionUID = 7903787808354995048L;
}  // end class DateConfig
