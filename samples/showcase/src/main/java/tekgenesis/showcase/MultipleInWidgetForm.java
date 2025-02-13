
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

/**
 * User class for form: MultipleInWidgetForm
 */
public class MultipleInWidgetForm extends MultipleInWidgetFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override MultipleInWidget defineMultiple() {
        return new MultipleInWidget();
    }
}
