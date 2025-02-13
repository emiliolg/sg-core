
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

@SuppressWarnings({ "WeakerAccess", "JavaDoc", "MagicNumber", "EmptyMethod" })
public class FormExpressionsTestRecursion extends FormExpressionsTestRecursionBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the form is loaded. */
    @Override public void load() {
        System.out.println("Do Nothing");  /* Do nothing */
    }

    /** Invoked when text_field(c) value changes. */
    @NotNull @Override public Action setATo20() {
        // Increment on_change calls count
        setCount(getCount() + 1);

        // Set an arbitrary value to A to re-trigger field C on_change
        setA(20);

        return actions.getDefault();
    }
}
