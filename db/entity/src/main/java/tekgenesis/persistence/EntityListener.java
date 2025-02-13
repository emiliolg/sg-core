
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

/**
 * Top Class for EntityListeners.
 */
@FunctionalInterface public interface EntityListener<E extends EntityInstance<E, ?>> {

    //~ Methods ......................................................................................................................................

    /**
     * Invoke the listener. If the listener is a before one and you return false then the operation
     * will not be performed
     */
    boolean invoke(E instance);

    //~ Inner Interfaces .............................................................................................................................

    @FunctionalInterface interface Update<E extends EntityInstance<E, ?>> extends EntityListener<E> {
        @Override default boolean invoke(E instance) {
            throw new IllegalStateException();
        }
        /**
         * Invoke the listener, passing as parameters the old and new values for the instance. If
         * the listener is a before one and you return false then the operation will not be
         * performed
         */
        boolean invoke(E oldInstance, E newInstance);
    }
}  // end interface EntityListener
