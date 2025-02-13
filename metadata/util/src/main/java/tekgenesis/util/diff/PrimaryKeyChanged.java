
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util.diff;

import tekgenesis.common.collections.ImmutableCollection;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.Entity;

/**
 * A {@link Change} that indicates that the primary key has been changed.
 */
public class PrimaryKeyChanged implements Change {

    //~ Instance Fields ..............................................................................................................................

    private final Entity entity;

    private final ImmutableCollection<Attribute> primaryKey;

    //~ Constructors .................................................................................................................................

    /** Constructor. */
    @SuppressWarnings("ParameterHidesMemberVariable")
    public PrimaryKeyChanged(Entity entity, ImmutableCollection<Attribute> primaryKey) {
        this.entity     = entity;
        this.primaryKey = primaryKey;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getMessage() {
        return DiffConstants.MODEL_SPC + entity.getFullName() + " has its primary key changed to " + primaryKey.mkString("(", ", ", ")");
    }
}
