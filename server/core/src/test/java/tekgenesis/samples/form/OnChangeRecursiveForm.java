
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
 * User class for form: OnChangeRecursiveForm
 */
public class OnChangeRecursiveForm extends OnChangeRecursiveFormBase {

    //~ Instance Fields ..............................................................................................................................

    private int all;

    private int value1;
    private int value2;
    private int value3;
    private int value4;

    //~ Methods ......................................................................................................................................

    /** Return on_changes stats. */
    public int[] stats() {
        return new int[] { value1, value2, value3, value4, all };
    }

    @NotNull @Override public Action value1Changed() {
        value1++;
        return actions().getDefault();
    }

    @NotNull @Override public Action value2Changed() {
        value2++;
        return actions().getDefault();
    }

    @NotNull @Override public Action value3Changed() {
        value3++;
        return actions().getDefault();
    }

    @NotNull @Override public Action value4Changed() {
        value4++;
        return actions().getDefault();
    }

    @NotNull @Override OnChangeRecursiveWidget defineChild() {
        return new OnChangeRecursiveWidget() {
            @Override void valueChangeCount() {
                all++;
            }
        };
    }
}  // end class OnChangeRecursiveForm
