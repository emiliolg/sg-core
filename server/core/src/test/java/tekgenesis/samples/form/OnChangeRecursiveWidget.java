
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
 * User class for widget: OnChangeRecursiveWidget
 */
public abstract class OnChangeRecursiveWidget extends OnChangeRecursiveWidgetBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public OnChangeRecursiveWidget defineChild() {
        return new OnChangeRecursiveWidget() {
            @Override void valueChangeCount() {
                OnChangeRecursiveWidget.this.valueChangeCount();
            }
        };
    }

    @NotNull @Override public Action valueChanged() {
        valueChangeCount();
        return actions().getDefault();
    }

    abstract void valueChangeCount();
}
