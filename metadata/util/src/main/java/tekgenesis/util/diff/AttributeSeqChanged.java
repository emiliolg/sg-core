
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
import tekgenesis.field.ModelField;
import tekgenesis.metadata.entity.Attribute;

/**
 * A {@link Change{ that indicates that a sequence of {@link Attribute} has changed. i.e. the
 * primary_key of entities
 */
public class AttributeSeqChanged implements Change {

    //~ Instance Fields ..............................................................................................................................

    private final Seq<? extends ModelField> newAttributes;
    private final Seq<? extends ModelField> oldAttributes;
    private final String                    text;

    //~ Constructors .................................................................................................................................

    /**
     * Constructor. Receives the old and new sequence and the text of the thing that has changed.
     */
    public AttributeSeqChanged(Seq<? extends ModelField> oldAttributes, Seq<? extends ModelField> newAttributes, String text) {
        this.oldAttributes = oldAttributes;
        this.newAttributes = newAttributes;
        this.text          = text;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getMessage() {
        return text + DiffConstants.HAS_CHANGED + oldAttributes.toString() + " -> " + newAttributes.toString();
    }
}
