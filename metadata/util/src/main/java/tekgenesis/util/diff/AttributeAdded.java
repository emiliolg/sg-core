
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

import static tekgenesis.util.diff.DiffConstants.HAS_BEEN_ADDED_TO;

/**
 * A {@link Change} that indicate thar an {@link Attribute} has been added to an {@link Entity}.
 */
public class AttributeAdded implements Change {

    //~ Instance Fields ..............................................................................................................................

    private final String attributeName;
    private final Entity entity;

    //~ Constructors .................................................................................................................................

    /** Constructor. */
    public AttributeAdded(Entity entity, String attributeName) {
        this.entity        = entity;
        this.attributeName = attributeName;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getMessage() {
        return DiffConstants.ATTRIBUTE_SPC + attributeName + HAS_BEEN_ADDED_TO + entity.getFullName();
    }
}
