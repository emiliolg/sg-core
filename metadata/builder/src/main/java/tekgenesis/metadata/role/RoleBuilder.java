
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.role;

import java.util.*;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.field.MetaModelReference;
import tekgenesis.metadata.common.ModelBuilder;
import tekgenesis.metadata.exception.BuilderError;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.exception.DuplicateRolePermissionsException;
import tekgenesis.metadata.exception.MetaModelAlreadyDefinedException;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.Strings.fromCamelCase;
import static tekgenesis.common.core.Strings.toWords;
import static tekgenesis.field.FieldOption.ROLE_APPLICATION_REF;

/**
 * Role builder.
 */
public class RoleBuilder extends ModelBuilder.Default<Role, RoleBuilder> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Set<String> applications;

    @NotNull private final List<RolePermissionBuilder> permissions;

    //~ Constructors .................................................................................................................................

    /** Builder constructor. */
    public RoleBuilder(@NotNull String src, @NotNull String pkg, @NotNull String name) {
        super(src, pkg, name);
        permissions  = new ArrayList<>();
        applications = new HashSet<>();
    }

    //~ Methods ......................................................................................................................................

    @Override public Role build()
        throws BuilderException
    {
        if (isEmpty(label)) label = toWords(fromCamelCase(id));
        final List<RolePermission> ps   = new ArrayList<>();
        final Role                 role = new Role(createQName(domain, id), label, modifiers, sourceName, ps);
        permissions.forEach(p -> ps.add(p.build()));
        return role;
    }

    @NotNull @Override public List<BuilderError> check() {
        return Collections.emptyList();
    }

    /** Create {@link RolePermissionBuilder}. */
    public void permissions(@NotNull final MetaModelReference model, @NotNull final Seq<String> nodes)
        throws BuilderException
    {
        if (!applications.add(model.getFullName())) throw new MetaModelAlreadyDefinedException(model.getFullName());

        final Set<String> duplicates = new HashSet<>();
        final Set<String> previous   = new HashSet<>();

        for (final String permission : nodes) {
            if (previous.add(permission)) {
                final RolePermissionBuilder builder = new RolePermissionBuilder(permission);
                builder.with(ROLE_APPLICATION_REF, model);
                permissions.add(builder);
            }
            else duplicates.add(permission);
        }

        if (!duplicates.isEmpty()) throw new DuplicateRolePermissionsException(duplicates, model.getFullName());
    }
}
