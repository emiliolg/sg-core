
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

/**
 * User class for form: RecursiveWidgetWithinMultipleForm
 */
public class RecursiveWidgetWithinMultipleForm extends RecursiveWidgetWithinMultipleFormBase {

    //~ Instance Fields ..............................................................................................................................

    private int changes;

    //~ Methods ......................................................................................................................................

    public int getChanges() {
        return changes;
    }

    @NotNull @Override RecursiveWidgetWithinMultiple defineChild() {
        return new RecursiveWidgetWithinMultiple() {
            @Override void valueChangedCount() {
                changes++;
            }
        };
    }
}
