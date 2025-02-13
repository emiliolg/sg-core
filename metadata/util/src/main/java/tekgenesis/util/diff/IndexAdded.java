
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util.diff;

import tekgenesis.common.collections.Seq;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.Entity;

import static tekgenesis.util.diff.DiffConstants.*;

/**
 * A {@link Change} that indicates that an index has been added.
 */
public class IndexAdded implements Change {

    //~ Instance Fields ..............................................................................................................................

    private final Seq<Attribute> attributes;
    private final Entity         entity;
    private final boolean        unique;

    //~ Constructors .................................................................................................................................

    /** Constructor. Receives the entity and the index attributes added */
    @SuppressWarnings("ParameterHidesMemberVariable")
    public IndexAdded(Entity entity, Seq<Attribute> attributes, boolean unique) {
        this.entity     = entity;
        this.attributes = attributes;
        this.unique     = unique;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getMessage() {
        return (unique ? UNIQUE_INDEX : INDEX) + attributes.mkString("(", ", ", ")") + HAS_BEEN_ADDED_TO + entity.getFullName();
    }
}
