
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.handler;

import org.jetbrains.annotations.NotNull;

import tekgenesis.field.FieldOptions;
import tekgenesis.field.TypeField;
import tekgenesis.type.ArrayType;
import tekgenesis.type.Type;

/**
 * Route parameter.
 */
public class Parameter extends TypeField {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final String label;

    //~ Constructors .................................................................................................................................

    /** Parameter constructor. */
    public Parameter(@NotNull String name, Type type, String route, @NotNull String description, @NotNull FieldOptions options) {
        super(name, type, options);
        label = description;
    }

    //~ Methods ......................................................................................................................................

    /** Return deepest type (element type if type if array). */
    public Type getDeepestType() {
        final Type type = getFinalType();
        return type.isArray() ? ((ArrayType) type).getElementType() : type;
    }

    /** Return true if parameters type is array. */
    public boolean isMultiple() {
        return getFinalType().isArray();
    }

    @NotNull public String getLabel() {
        return label;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 2731492500108122015L;
}
