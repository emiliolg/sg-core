
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

/**
 * Swipe loader class to be used by users to define Swipe loaders.
 */
@SuppressWarnings({ "WeakerAccess", "InstanceVariableMayNotBeInitialized" })
public interface SwipeLoader<T extends FormInstance<?>> {

    //~ Methods ......................................................................................................................................

    /** Callback called to load swipe instances. */
    T load(final int index);
}
