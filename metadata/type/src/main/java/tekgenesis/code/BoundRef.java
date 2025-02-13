
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.code;

import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

/**
 * Bounded ref. Used to retrieve a value in a given context
 */
public interface BoundRef<T> extends Function<Object, T> {

    //~ Methods ......................................................................................................................................

    /** Return a value for a given context. */
    @Nullable @Override T apply(Object context);
}
