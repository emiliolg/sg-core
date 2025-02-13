
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

import tekgenesis.field.FieldOption;
import tekgenesis.metadata.entity.FieldBuilder;
import tekgenesis.metadata.exception.BuilderException;

/**
 * Collects the data to build a {@link Role}.
 */
public class RolePermissionBuilder extends FieldBuilder<RolePermissionBuilder> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final String permission;

    //~ Constructors .................................................................................................................................

    /** Builder for case tasks. */
    RolePermissionBuilder(@NotNull final String permission) {
        this.permission = permission;
    }

    //~ Methods ......................................................................................................................................

    /** Build a Menu Item. */
    public RolePermission build() {
        return new RolePermission(permission, getOptions());
    }

    @Override protected void checkOptionSupport(FieldOption option)
        throws BuilderException {}
}
