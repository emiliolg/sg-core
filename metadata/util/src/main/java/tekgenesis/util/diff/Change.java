
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util.diff;

import tekgenesis.repository.ModelRepository;

/**
 * Interface for a {@link ModelRepository} change.
 */
@SuppressWarnings("WeakerAccess")
public interface Change {

    //~ Methods ......................................................................................................................................

    /** Returns the change displayable message. */
    String getMessage();
}
