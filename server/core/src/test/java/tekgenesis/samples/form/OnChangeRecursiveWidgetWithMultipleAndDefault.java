
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
 * User class for widget: OnChangeRecursiveWidgetWithMultipleAndDefault
 */
public abstract class OnChangeRecursiveWidgetWithMultipleAndDefault extends OnChangeRecursiveWidgetWithMultipleAndDefaultBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override OnChangeRecursiveWidgetWithMultipleAndDefault defineChild() {
        return new OnChangeRecursiveWidgetWithMultipleAndDefault() {
            @Override void valueChangeCount() {
                OnChangeRecursiveWidgetWithMultipleAndDefault.this.valueChangeCount();
            }
        };
    }

    abstract void valueChangeCount();

    //~ Inner Classes ................................................................................................................................

    public class ValuesRow extends ValuesRowBase {
        @NotNull @Override public Action valueChanged() {
            valueChangeCount();
            return actions().getDefault();
        }
    }
}
