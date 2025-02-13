
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.test;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;

/**
 * User class for Form: FormA
 */
public class FormA extends FormABase {

    //~ Methods ......................................................................................................................................

    /** Invoked when creating a form instance. */
    @NotNull @Override public Action create() {
        throw new IllegalStateException("To be implemented");
    }

    /** Invoked to find an entity instance. */
    @NotNull @Override public EA find() {
        throw new IllegalStateException("To be implemented");
    }
}
