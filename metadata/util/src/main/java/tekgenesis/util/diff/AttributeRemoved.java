
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util.diff;

import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.util.diff.DiffConstants.HAS_BEEN_REMOVED_FROM;

/**
 * A {@link Change} that indicates that an {@link Attribute} has been removed from the
 * {@link ModelRepository}.
 */
public class AttributeRemoved implements Change {

    //~ Instance Fields ..............................................................................................................................

    private final String attributeName;
    private final Entity entity;

    //~ Constructors .................................................................................................................................

    /** Constructor. Receives the entity and the attribute removed */
    public AttributeRemoved(Entity entity, String attributeName) {
        this.entity        = entity;
        this.attributeName = attributeName;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getMessage() {
        return DiffConstants.ATTRIBUTE_SPC + attributeName + HAS_BEEN_REMOVED_FROM + entity.getFullName();
    }
}
