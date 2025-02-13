
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

/**
 * A {@link Change} that indicates that an {@link Attribute} type has changed.
 */
public class AttributeTypeChanged implements Change {

    //~ Instance Fields ..............................................................................................................................

    private final Attribute attribute;
    private final String    newType;

    //~ Constructors .................................................................................................................................

    /** Constructor. Receives the attribute and the new type */
    public AttributeTypeChanged(Attribute attribute, String newType) {
        this.attribute = attribute;
        this.newType   = newType;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getMessage() {
        return DiffConstants.ATTRIBUTE_SPC + attribute.getFullName() + " type changed: " + attribute.getTypeAsString() + " -> " + newType;
    }
}
