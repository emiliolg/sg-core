
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
 * User class for form: OnChangeRecursiveWidgetWithMultipleAndDefaultForm
 */
public class OnChangeRecursiveWidgetWithMultipleAndDefaultForm extends OnChangeRecursiveWidgetWithMultipleAndDefaultFormBase {

    //~ Instance Fields ..............................................................................................................................

    private int changes;
    private int rootChanges;

    //~ Methods ......................................................................................................................................

    /** Invoked when widget(child) value changes. */
    @NotNull @Override public Action rootChanged() {
        rootChanges++;
        return actions().getDefault();
    }

    /** Return on_changes stats. */
    public int[] stats() {
        return new int[] { changes, rootChanges };
    }

    @NotNull @Override OnChangeRecursiveWidgetWithMultipleAndDefault defineChild() {
        return new OnChangeRecursiveWidgetWithMultipleAndDefault() {
            @Override void valueChangeCount() {
                changes++;
            }
        };
    }
}
