
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util.diff;

import tekgenesis.metadata.entity.Entity;
import tekgenesis.type.EnumType;
import tekgenesis.type.MetaModel;

/**
 * A {@link Change} that indicates that a {@link MetaModel} type has changed. For instance an
 * {@link Entity} has become an {@link EnumType}
 */
public class ModelTypeChanged implements Change {

    //~ Instance Fields ..............................................................................................................................

    private final MetaModel leftModel;
    private final MetaModel rightModel;

    //~ Constructors .................................................................................................................................

    /** Constructor. Receives the old and new {@link MetaModel} */
    public ModelTypeChanged(MetaModel leftModel, MetaModel rightModel) {
        this.leftModel  = leftModel;
        this.rightModel = rightModel;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getMessage() {
        return "The model type for " + leftModel.getFullName() + " has been changed from " + leftModel.getClass() + " to " + rightModel.getClass();
    }
}
