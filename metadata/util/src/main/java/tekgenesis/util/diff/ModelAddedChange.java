
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
import tekgenesis.type.MetaModel;

/**
 * A {@link Change} that indicates that a new {@link MetaModel} has been added to the
 * {@link ModelRepository}.
 */
public class ModelAddedChange implements Change {

    //~ Instance Fields ..............................................................................................................................

    private final MetaModel model;

    //~ Constructors .................................................................................................................................

    /** Constructor. Receives the MetaModel that has been added. */
    public ModelAddedChange(MetaModel model) {
        this.model = model;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getMessage() {
        return DiffConstants.MODEL_SPC + model.getFullName() + DiffConstants.HAS_BEEN_ADDED;
    }
}
