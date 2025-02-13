
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import org.jetbrains.annotations.NotNull;

import tekgenesis.persistence.InnerEntitySeq;
import tekgenesis.showcase.g.DynamicPropertyBase;
import tekgenesis.type.DynamicTypeConverter;

/**
 * User class for Entity: DynamicProperty
 */
@Generated(value = "tekgenesis/showcase/DynamicShowcase.mm", date = "1371479419360")
public class DynamicProperty extends DynamicPropertyBase {

    //~ Methods ......................................................................................................................................

    @NotNull public Iterable<Object> getObjectValues(DynamicTypeConverter typeConverter) {
        final List<Object> values = new ArrayList<Object>(getValues().size());
        for (DynamicValue value : getValues())
            values.add(typeConverter.fromString(value.getValue()));
        return values;
    }

    public void setObjectValues(@NotNull final Iterable<Object> objects, DynamicTypeConverter typeConverter) {
        final InnerEntitySeq<DynamicValue> values = getValues();
        for (Object object : objects)
            values.add().setValue(typeConverter.toString(object));
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1449310910991872227L;
}
