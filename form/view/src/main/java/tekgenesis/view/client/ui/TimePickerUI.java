
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.ListBox;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.IntIntTuple;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.common.Predefined.mapNullable;
import static tekgenesis.common.core.Times.MINUTES_HOUR;
import static tekgenesis.common.core.Times.SECONDS_MINUTE;
import static tekgenesis.common.core.Times.stringify;
import static tekgenesis.common.core.Times.toHoursMinutes;
import static tekgenesis.metadata.form.model.FormConstants.BIG_COMBO;
import static tekgenesis.metadata.form.model.FormConstants.FORM_CONTROL;
import static tekgenesis.view.client.ui.BaseDateUI.getClientOffsetTimeZone;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.div;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.list;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.span;

/**
 * A TimePicker UI widget.
 */
public class TimePickerUI extends BaseHasScalarValueUI implements HasFromTo {

    //~ Instance Fields ..............................................................................................................................

    private final TimePicker picker;

    //~ Constructors .................................................................................................................................

    /** Creates a DateTimeBox UI widget. */
    public TimePickerUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);

        picker = new TimePicker(model.isMidnightAs24(), model.getStep(), model.hasTimezone());
        init(picker, false);
    }

    //~ Methods ......................................................................................................................................

    public void setFrom(Double f) {
        picker.setFrom(f.intValue());
    }

    public void setTo(Double t) {
        picker.setTo(t.intValue());
    }

    // ** This widget doesn't support icon */
    @NotNull @Override Option<Element> createIcon() {
        return Option.empty();
    }

    //~ Inner Classes ................................................................................................................................

    private static class TimePicker extends Composite implements HasValueChangeHandlers<Integer>, HasValue<Integer> {
        private Integer from;

        private ListBox hours;

        private final boolean midnightAs24;
        private ListBox       mins;
        private final int     step;
        private final boolean timezone;
        private Integer       to;

        private Integer value = 0;

        private TimePicker(boolean mAs24, int s, boolean tz) {
            midnightAs24 = mAs24;
            step         = s == 0 ? 1 : s;
            timezone     = tz;

            from = ZERO;
            to   = HOUR_23;

            build();
        }

        @Override public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Integer> h) {
            return addHandler(h, ValueChangeEvent.getType());
        }

        public void setFrom(Integer f) {
            from = f;
            updateHoursOptions();
        }

        public void setTo(Integer t) {
            to = t;
            updateHoursOptions();
        }

        @Override public Integer getValue() {
            return value;
        }

        @Override public void setValue(Integer v) {
            setValue(v, false);
        }

        @Override public void setValue(Integer v, boolean fireEvents) {
            if (v != null) {
                final IntIntTuple hsMs = toHoursMinutes(v);
                selectIndex(hours, hsMs.first());
                selectIndex(mins, hsMs.second());
            }

            value = timezone ? adjustTz(v) : v;

            if (fireEvents) ValueChangeEvent.fire(this, value);
        }

        @Nullable private Integer adjustTz(@Nullable Integer v) {
            return mapNullable(v, i -> i + getClientOffsetTimeZone() * -SECONDS_MINUTE);
        }

        private void attachChangeHandlers() {
            final ChangeHandler h = event -> setValue(buildValue(), true);
            hours.addChangeHandler(h);
            mins.addChangeHandler(h);
        }

        private void build() {
            final FlowPanel p = div();

            hours = list(false, 1);
            hours.addStyleName(COMBO_CLASS + " " + BIG_COMBO + " " + FORM_CONTROL);

            mins = list(false, 1);
            mins.addStyleName(COMBO_CLASS + " " + BIG_COMBO + " " + FORM_CONTROL);

            final InlineHTML dots = span(":");
            dots.addStyleName("timePicker-dots");
            dots.addMouseOverHandler(event -> {
                if (dots.getStyleName().contains(BLINK_ME)) dots.removeStyleName(BLINK_ME);
                else dots.addStyleName(BLINK_ME);
            });

            p.add(hours);
            p.add(dots);
            p.add(mins);

            initWidget(p);

            attachChangeHandlers();
            updateHoursOptions();
            updateMinutesOptions();
        }

        @SuppressWarnings("NonJREEmulationClassesInClientCode")
        private Integer buildValue() {
            final String h = hours.getValue(hours.getSelectedIndex());
            final String m = mins.getValue(mins.getSelectedIndex());

            final Integer hs = h.equals(HOUR_24_STR) ? ZERO : Integer.valueOf(h);
            final Integer ms = Integer.valueOf(m);

            return hs * MINUTES_HOUR * SECONDS_MINUTE + ms * SECONDS_MINUTE;
        }

        private void selectIndex(ListBox list, Integer hs) {
            int selectedIndex = 0;
            for (int i = 0; i < list.getItemCount(); i++) {
                if (list.getItemText(i).equals(stringify(hs))) selectedIndex = i;
            }
            list.setSelectedIndex(selectedIndex);
        }

        private void updateHoursOptions() {
            hours.clear();
            final int innerFrom = from == ZERO && midnightAs24 ? 1 : from;
            final int innerTo   = to == HOUR_23 && midnightAs24 ? HOUR_24 : to;
            for (int i = innerFrom; i <= innerTo; i++)
                hours.addItem(stringify(i));
        }

        private void updateMinutesOptions() {
            mins.clear();
            for (int i = ZERO; i <= MINUTE_59; i += step)
                mins.addItem(stringify(i));
        }

        private static final int    ZERO        = 0;
        private static final int    HOUR_23     = 23;
        private static final int    MINUTE_59   = 59;
        private static final int    HOUR_24     = 24;
        private static final String HOUR_24_STR = "24";
        private static final String COMBO_CLASS = "timePicker-combo";
        private static final String BLINK_ME    = "blink-me";
    }  // end class TimePicker
}  // end class TimePickerUI
