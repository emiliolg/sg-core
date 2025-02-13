
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.extension;

import tekgenesis.form.FormInstance;

/**
 * FormListeners function class.
 */
@FunctionalInterface public interface FormListener<T extends FormInstance<?>> {

    //~ Methods ......................................................................................................................................

    /** Invoke the listener. */
    void invoke(T instance);
}
