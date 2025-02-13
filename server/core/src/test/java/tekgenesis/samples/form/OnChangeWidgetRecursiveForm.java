
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
 * User class for form: OnChangeWidgetRecursiveForm
 */
public class OnChangeWidgetRecursiveForm extends OnChangeWidgetRecursiveFormBase {

    //~ Instance Fields ..............................................................................................................................

    private int all;
    private int root;

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action rootChanged() {
        root++;
        return actions().getDefault();
    }

    /** Return on_changes stats. */
    public int[] stats() {
        return new int[] { root, all };
    }

    @NotNull @Override OnChangeRecursiveWidget defineChild() {
        return new OnChangeRecursiveWidget() {
            @Override void valueChangeCount() {
                all++;
            }
        };
    }
}
