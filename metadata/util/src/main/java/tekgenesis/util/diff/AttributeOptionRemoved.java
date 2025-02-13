
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util.diff;

import tekgenesis.field.FieldOption;
import tekgenesis.metadata.entity.Attribute;

import static tekgenesis.common.core.Strings.quoted;
import static tekgenesis.util.diff.DiffConstants.*;

/**
 * A {@link Change} that indicates an {@link Attribute} option has been removed.
 */
public class AttributeOptionRemoved implements Change {

    //~ Instance Fields ..............................................................................................................................

    private final Attribute   attribute;
    private final FieldOption option;

    //~ Constructors .................................................................................................................................

    /** Attribute Option Added. */
    public AttributeOptionRemoved(Attribute attribute, FieldOption option) {
        this.attribute = attribute;
        this.option    = option;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getMessage() {
        return OPTION_SPC + quoted(option.getId()) + HAS_BEEN_REMOVED + IN_ATTRIBUTE + attribute.getFullName();
    }
}
