
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type;

import java.util.function.Supplier;

/**
 * An unresolved reference to a model object.
 */
public interface UnresolvedReference<T> extends Supplier<T> {

    //~ Instance Fields ..............................................................................................................................

    int DUPLICATE = 1;
    int NOT_FOUND = 2;
    int OK        = 0;

    //~ Methods ......................................................................................................................................

    /** Report an error when a reverse reference cannot be solved. */
    void reverseError(String attributeName, int errType);

    /** Solve the reference to the specified type. */
    T solve(T model);
}
