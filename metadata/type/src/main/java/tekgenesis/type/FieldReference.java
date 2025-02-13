
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.field.FieldOptions;
import tekgenesis.field.ModelField;
import tekgenesis.type.exception.ReverseReferenceException;
import tekgenesis.type.exception.UnresolvedTypeReferenceException;

/**
 * This class represents a ModelField reference. The reference contains the name of the attribute
 */
public class FieldReference implements ModelField, UnresolvedReference<ModelField> {

    //~ Instance Fields ..............................................................................................................................

    @Nullable private ModelField finalField = null;

    @NotNull private final String name;

    //~ Constructors .................................................................................................................................

    protected FieldReference(@NotNull String name) {
        this.name = name;
    }

    //~ Methods ......................................................................................................................................

    /** Retrieves an instance of the appropriate type. */
    @NotNull @Override public ModelField get() {
        if (finalField == null) throw new IllegalStateException("Reference to field '" + name + "' is unresolved. Or field does not exist");
        return finalField;
    }

    @Override public boolean hasChildren() {
        return get().hasChildren();
    }

    /** Report an error when a reverse reference cannot be solved. */
    @Override public void reverseError(String attributeName, int errType) {
        throw new ReverseReferenceException(attributeName, false);
    }

    /** Solve the reference to the specified type. */
    @Override public ModelField solve(ModelField model) {
        if (model == this) error();
        else finalField = model;
        return model;
    }

    @NotNull @Override public ImmutableList<? extends ModelField> getChildren() {
        return get().getChildren();
    }

    @NotNull @Override public String getLabel() {
        return get().getLabel();
    }

    @NotNull @Override public String getName() {
        return name;
    }

    @NotNull @Override public FieldOptions getOptions() {
        return get().getOptions();
    }

    @NotNull @Override public Type getType() {
        return get().getType();
    }

    @Override public void setType(@NotNull Type type) {
        get().setType(type);
    }

    protected void error() {
        throw new UnresolvedTypeReferenceException(getName());
    }

    //~ Methods ......................................................................................................................................

    /**  */
    public static FieldReference unresolvedFieldRef(@NotNull String name) {
        return new FieldReference(name);
    }
}  // end class UnresolvedFieldReference
