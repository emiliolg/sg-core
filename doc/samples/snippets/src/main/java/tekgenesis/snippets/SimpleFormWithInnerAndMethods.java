
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.snippets;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;

/**
 * User class for Form: SimpleFormWithInnerAndMethods
 */
public class SimpleFormWithInnerAndMethods extends SimpleFormWithInnerAndMethodsBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when text_area(formElement1) value changes. */
    @NotNull @Override public Action anotherChange() {
        throw new IllegalStateException("Not implemented yet");
    }

    //~ Inner Classes ................................................................................................................................

    public class TableIdRow extends TableIdRowBase {}
}
