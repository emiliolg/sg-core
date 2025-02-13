
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization;

import org.jetbrains.annotations.NotNull;

import tekgenesis.authorization.g.RolePermissionBase;
import tekgenesis.common.core.QName;

import static tekgenesis.common.core.QName.createQName;

/**
 * User class for Entity: RolePermission
 */
public class RolePermission extends RolePermissionBase {

    //~ Methods ......................................................................................................................................

    /** make description custom. */
    @NotNull @Override
    @SuppressWarnings("WeakerAccess")
    public String toString() {
        return makeDescription();
    }

    @NotNull @Override public String getDesc() {
        return toString();
    }

    /** Get QName for RolePermission. */
    QName getQName() {
        return createQName(getDomain(), getApplication());
    }

    private String makeDescription() {
        final StringBuilder builder = new StringBuilder(getRole().getName());
        builder.append(" / ");
        if ("*".equals(getDomain())) builder.append(ALL_DOMAINS);
        else {
            builder.append(super.getApplication());
            builder.append(" (").append(getDomain()).append(")");
        }
        return builder.toString();
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 6382074716490113347L;

    private static final String ALL_DOMAINS = "All Domains";
}
