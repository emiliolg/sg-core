
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

/**
 * User class for widget: NestedOnChangesWidget
 */
public abstract class NestedOnChangesWidget extends NestedOnChangesWidgetBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when text_field(e) value changes. */
    @NotNull @Override public Action eValueChanged() {
        call("e");
        return actions().getDefault();
    }

    abstract void call(@NotNull final String source);

    //~ Inner Classes ................................................................................................................................

    public class WidgetsRow extends WidgetsRowBase {
        @NotNull @Override public Action dValueChanged() {
            call("d");
            return actions().getDefault();
        }

        @NotNull @Override OnChangesWidget defineNested() {
            return new OnChangesWidget() {
                @Override void call(@NotNull String source) {
                    NestedOnChangesWidget.this.call(source);
                }
            };
        }
    }
}
