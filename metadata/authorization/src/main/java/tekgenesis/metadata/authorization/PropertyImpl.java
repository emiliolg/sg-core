
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.authorization;

import static tekgenesis.common.Predefined.cast;

/**
 * Implementation of Context Property.
 */
public class PropertyImpl<T> implements Property {

    //~ Instance Fields ..............................................................................................................................

    private final String id;
    private final String name;

    private T value;

    //~ Constructors .................................................................................................................................

    private PropertyImpl(String id, String name, T value) {
        this.id    = id;
        this.name  = name;
        this.value = value;
    }

    //~ Methods ......................................................................................................................................

    @Override public Boolean asBoolean() {
        if (value instanceof String) return Boolean.valueOf((String) value);
        return cast(value);
    }

    @Override public Integer asInt() {
        if (value instanceof String) return Integer.valueOf((String) value);
        return cast(value);
    }

    @Override public String asString() {
        return String.valueOf(value);
    }

    @Override public String getId() {
        return id;
    }

    @Override public String getName() {
        return name;
    }

    @Override public T getValue() {
        return value;
    }

    @Override public void setValue(Object value) {
        this.value = cast(value);
    }

    //~ Methods ......................................................................................................................................

    /** Creates a Context Property. */
    public static <T> Property createProperty(String id, String name, T value) {
        return new PropertyImpl<>(id, name, value);
    }
}  // end class PropertyImpl
