
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.role;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.field.FieldOption;
import tekgenesis.field.FieldOptions;
import tekgenesis.field.MetaModelReference;
import tekgenesis.field.ModelField;
import tekgenesis.type.MetaModel;
import tekgenesis.type.Type;

import static tekgenesis.common.collections.ImmutableList.empty;
import static tekgenesis.type.Types.anyType;

/**
 * Class that contains metadata for a role permission.
 */
public class RolePermission implements ModelField {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final FieldOptions options;

    @NotNull private final String permission;

    //~ Constructors .................................................................................................................................

    RolePermission(@NotNull final String permission, @NotNull final FieldOptions options) {
        this.permission = permission;
        this.options    = options;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean hasChildren() {
        return false;
    }

    @NotNull @Override public ImmutableList<? extends ModelField> getChildren() {
        return empty();
    }

    @NotNull @Override public String getLabel() {
        return getName();
    }

    /** Return metamodel to which permission is set. */
    @NotNull public MetaModel getMetaModel() {
        return getMetaModelReference().get();
    }

    @NotNull @Override public String getName() {
        return getMetaModelReference().getFullName() + "." + permission;
    }

    @NotNull @Override public FieldOptions getOptions() {
        return options;
    }

    /** Return string permission. */
    @NotNull public String getPermission() {
        return permission;
    }

    @NotNull @Override public Type getType() {
        return anyType();
    }

    @Override public void setType(@NotNull Type type) {
        throw new UnsupportedOperationException("RolePermission has no type");
    }

    private MetaModelReference getMetaModelReference() {
        return options.getMetaModelReference(FieldOption.ROLE_APPLICATION_REF);
    }
}  // end class RolePermission
