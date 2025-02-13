
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type;

import tekgenesis.repository.ModelRepository;

/**
 * A Linker is in charge of the last step for building a {@link ModelRepository} it is responsibly
 * for solving references between symbols.
 */
public interface ModelLinker {

    //~ Methods ......................................................................................................................................

    /** Try to link a Model returns true if successful. */
    boolean link(MetaModel model);

    /** Set error reporting to true. */
    ModelLinker setLastStage(boolean b);
}
