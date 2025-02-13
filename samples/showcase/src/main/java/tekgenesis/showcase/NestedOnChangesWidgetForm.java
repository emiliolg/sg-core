
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;

import static tekgenesis.common.Predefined.isEmpty;

/**
 * User class for form: NestedOnChangesWidgetForm
 */
public class NestedOnChangesWidgetForm extends NestedOnChangesWidgetFormBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        // Initialize nested nested widget
        getWidgets().add().getChanges().getWidgets().add();
    }

    /** Invoked when text_field(sum) value changes. */
    @NotNull @Override public Action sumChanged() {
        call("sum");
        return actions().getDefault();
    }

    private void call(@NotNull final String source) {
        final String calls = getCalls();
        if (isEmpty(calls)) setCalls(source);
        else setCalls(calls + "," + source);
    }

    //~ Inner Classes ................................................................................................................................

    public class WidgetsRow extends WidgetsRowBase {
        /** Invoked when text_field(f) value changes. */
        @NotNull @Override public Action fValueChanged() {
            call("f");
            return actions().getDefault();
        }

        @NotNull @Override NestedOnChangesWidget defineChanges() {
            return new NestedOnChangesWidget() {
                @Override void call(@NotNull final String source) {
                    NestedOnChangesWidgetForm.this.call(source);
                }
            };
        }
    }
}
