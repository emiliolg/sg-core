
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
 * Entity initializer service class.
 *
 * <p>An entity initializer is a concrete subclass of this class that has a zero-argument
 * constructor and have an initialize method that will be invoked when the EntityTable specified by
 * the getEntityType() method is created. The class will be loaded using the
 * {@link java.util.ServiceLoader} facility.</p>
 */
public interface EntityInitializer {

    //~ Methods ......................................................................................................................................

    /** Method invoked on EntityTable creation. */
    void initialize();
    /** Specify the Entity. */
    Class<? extends EntityInstance<?, ?>> getEntityType();
}
