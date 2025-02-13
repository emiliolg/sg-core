
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.authorization;

import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Organizational Unit.
 */
public interface OrganizationalUnit {

    //~ Methods ......................................................................................................................................

    /**
     * Returns an Organizational Unit's company OU. (a.k.a. the root OrgUnit from which the base one
     * descends, discarding 'root OU'). Or 'null' if this Organizational Unit is already the
     * company.
     */
    @Nullable OrganizationalUnit getCompany();

    /** Returns the Organizational Unit's description. */
    @NotNull String getDescription();

    /** Returns Organizational Unit's hierarchy, including self and all descendants. */
    @NotNull Set<String> getHierarchy();

    /** Returns the Organizational Unit's name. */
    @NotNull String getName();

    /** Returns the Organizational Unit's parent or 'null' if this is a root Organizational Unit. */
    @Nullable OrganizationalUnit getParent();

    /** Returns user properties. */
    @NotNull Iterable<Property> getProperties();

    /** Returns property or 'null' if the property doesn't exist. */
    @Nullable Property getProperty(String key);
}
