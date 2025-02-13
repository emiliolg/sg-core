
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.samples.form;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;

/**
 * User class for widget: RecursiveWidgetWithinMultiple
 */
public abstract class RecursiveWidgetWithinMultiple extends RecursiveWidgetWithinMultipleBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action valueChanged() {
        valueChangedCount();
        return actions().getDefault();
    }

    abstract void valueChangedCount();

    //~ Inner Classes ................................................................................................................................

    public class WidgetsRow extends WidgetsRowBase {
        @NotNull @Override RecursiveWidgetWithinMultiple defineInner() {
            return new RecursiveWidgetWithinMultiple() {
                @Override void valueChangedCount() {
                    RecursiveWidgetWithinMultiple.this.valueChangedCount();
                }
            };
        }
    }
}
