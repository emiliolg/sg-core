
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.Date;

import com.google.gwt.event.logical.shared.ShowRangeEvent;
import com.google.gwt.event.logical.shared.ShowRangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.datepicker.client.DatePicker;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.DateOnly;
import tekgenesis.metadata.form.configuration.DateConfig;
import tekgenesis.metadata.form.widget.DateType;
import tekgenesis.metadata.form.widget.Widget;

import static com.google.gwt.i18n.client.DateTimeFormat.getFormat;

import static tekgenesis.common.core.Constants.DEFAULT;
import static tekgenesis.common.core.DateOnly.fromDate;
import static tekgenesis.common.core.Times.*;
import static tekgenesis.metadata.form.configuration.ChartConfig.toGetDay;

/**
 * Base class to Date UI components.
 */
public class BaseDateUI extends BaseHasScalarValueUI implements HasFromTo {

    //~ Instance Fields ..............................................................................................................................

    Long               from   = null;
    Long               to     = null;
    private DateConfig config = null;

    //~ Constructors .................................................................................................................................

    BaseDateUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        applyConfig(DateConfig.DEFAULT, false);
    }

    //~ Methods ......................................................................................................................................

    /** Sets configuration. */
    public void applyConfig(DateConfig c, boolean refresh) {
        config = c;
    }

    /** Sets the 'from' String. */
    public void setFrom(Double from) {
        this.from = from.longValue();
    }

    /** Sets the 'to' String. */
    public void setTo(Double to) {
        this.to = to.longValue();
    }

    //~ Methods ......................................................................................................................................

    static native int getClientOffsetTimeZone()  /*-{
        return new Date().getTimezoneOffset();
    }-*/;

    static DateTimeFormat getDateFormat(DateType dateType) {
        final String locale = LocaleInfo.getCurrentLocale().getLocaleName();
        if ("en".equals(locale) || DEFAULT.equals(locale)) return getFormat("MM/dd/yyyy");

        final DateTimeFormat format;
        switch (dateType) {
        case MEDIUM_FORMAT:
            format = getFormat("dd/MM/yyyy");
            break;
        case LONG_FORMAT:
            format = getFormat("d 'de' MMMM 'de' y");
            break;
        case FULL_FORMAT:
            format = getFormat("EEEE, d 'de' MMMM 'de' y");
            break;
        case SHORT_FORMAT:
        default:
            format = getFormat("dd/MM/yy");
            break;
        }
        return format;
    }

    static DateTimeFormat getDateTimeFormat(DateType dateType) {
        final String locale = LocaleInfo.getCurrentLocale().getLocaleName();
        if ("en".equals(locale) || DEFAULT.equals(locale)) return getFormat("MM/dd/yyyy HH:mm:ss");

        final DateTimeFormat format;
        switch (dateType) {
        case MEDIUM_FORMAT:
            format = getFormat("dd/MM/yyyy H:mm:ss");
            break;
        case LONG_FORMAT:
            format = getFormat("d 'de' MMMM 'de' y H:mm:ss z");
            break;
        case FULL_FORMAT:
            format = getFormat("EEEE, d 'de' MMMM 'de' y H:mm:ss (zzzz)");
            break;
        case SHORT_FORMAT:
        default:
            format = getFormat("dd/MM/yy H:mm");
            break;
        }
        return format;
    }

    //~ Inner Classes ................................................................................................................................

    class FromToValueChangeHandler implements ValueChangeHandler<Date> {
        private final HasValue<Date> box;

        FromToValueChangeHandler(HasValue<Date> box) {
            this.box = box;
        }

        @Override public void onValueChange(ValueChangeEvent<Date> event) {
            final Date date = event.getValue();
            if (date != null) {
                if (from != null && toMidnight(date) < from) box.setValue(millisToDate(from, true), false);

                if (to != null && toMidnight(date) > to) box.setValue(millisToDate(to, true), false);
            }
        }
    }

    class PickerShowRangeHandler implements ShowRangeHandler<Date> {
        private final DatePicker datePicker;

        PickerShowRangeHandler(DatePicker picker) {
            datePicker = picker;
        }

        @Override
        @SuppressWarnings("deprecation")
        public void onShowRange(ShowRangeEvent<Date> event) {
            long       start = toMidnight(event.getStart());
            final long end   = toMidnight(event.getEnd());
            while (start <= end) {
                final Date d       = millisToDate(start, true);
                boolean    enabled = true;
                if (from != null && start < from || to != null && start > to) {
                    disableDay(d);
                    enabled = false;
                }

                final DateOnly fromDate = fromDate(d);

                if (enabled) {
                    if (config.isDayOfWeekDisabled(toGetDay(d.getDay()))) disableDay(d);

                    if (config.getDisabled().contains(fromDate)) disableDay(d);
                    if (config.getEnabled().contains(fromDate)) enableDay(d);
                }

                if (config.getDaysStyles().containsKey(fromDate)) {
                    final String styleName = config.getDaysStyles().get(fromDate);
                    datePicker.addTransientStyleToDates(styleName, d);
                }

                start = addDays(start, 1);
            }
        }

        private void disableDay(Date d) {
            datePicker.setTransientEnabledOnDates(false, d);
        }
        private void enableDay(Date d) {
            datePicker.setTransientEnabledOnDates(true, d);
        }
    }  // end class PickerShowRangeHandler
}  // end class BaseDateUI
