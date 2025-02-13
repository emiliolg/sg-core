
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.metadata.authorization.OrganizationalUnit;
import tekgenesis.metadata.authorization.User;

/**
 * Utility methods to deal with Application Context stuff.
 */
@SuppressWarnings("unused")
public interface ApplicationContext {

    //~ Methods ......................................................................................................................................

    /**
     * Returns current Organizational Unit's company OU. (a.k.a. the root OrgUnit from which the
     * base one descends, discarding 'root OU'). Or 'null' if current OU is null.
     */
    @Nullable OrganizationalUnit getCompany();

    /** Returns current history form fqn as a QName. */
    @NotNull Option<QName> getCurrentHistoryForm();

    /** Returns true if session is authenticated. */
    boolean isAuthenticated();

    /** Returns current request host. */
    @NotNull String getHost();

    /** Return user interaction map. InteractionMap lives during form life cycle. */
    @NotNull InteractionMap getInteractionMap();

    /** Returns the Organizational Unit to which the current User is logged. */
    @NotNull OrganizationalUnit getOrganizationalUnit();

    /**
     * Returns current logged User. Throws exception if there is no authenticated session. Use
     * #isAuthenticated() to check first.
     */
    @NotNull User getUser();
}
