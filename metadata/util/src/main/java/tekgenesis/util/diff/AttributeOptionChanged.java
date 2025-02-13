
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
 * A {@link Change} that indicates that an {@link Attribute} option has changed.
 */
public class AttributeOptionChanged implements Change {

    //~ Instance Fields ..............................................................................................................................

    private final Attribute attribute;
    private final Object    newValue;
    private final Object    oldValue;
    private final String    option;

    //~ Constructors .................................................................................................................................

    /**
     * Constructor. Receives the {@link Attribute}, the old and new value, and the option to
     * display.
     */
    public AttributeOptionChanged(Attribute attribute, Object oldValue, Object newValue, FieldOption option) {
        this(attribute, oldValue, newValue, option.getId());
    }

    /**
     * Constructor. Receives the {@link Attribute}, the old and new value, and the text to display
     */
    public AttributeOptionChanged(Attribute attribute, Object oldValue, Object newValue, String option) {
        this.oldValue  = oldValue;
        this.newValue  = newValue;
        this.option    = option;
        this.attribute = attribute;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getMessage() {
        return OPTION_SPC + quoted(option) + IN_ATTRIBUTE + attribute.getFullName() + HAS_CHANGED + oldValue + " -> " + newValue;
    }
}
