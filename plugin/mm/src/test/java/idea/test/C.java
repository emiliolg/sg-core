
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package idea.test;

import org.jetbrains.annotations.NotNull;

/**
 * Fake user class for Form: C
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public class C {

    //~ Methods ......................................................................................................................................

    /** Invoked when the form is canceled. */
    /*@Override*/ public void cancel() {
        throw new IllegalStateException("To be implemented");
    }

    /** Invoked when the form is loaded. */
    /*@Override*/ public void load() {
        throw new IllegalStateException("To be implemented");
    }

    /** Invoked when populating a form instance. */
    /*@Override*/ @NotNull public Object populate() {
        throw new IllegalStateException("To be implemented");
    }
}
