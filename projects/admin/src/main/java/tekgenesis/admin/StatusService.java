
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.admin;

import com.fasterxml.jackson.databind.node.ObjectNode;

import org.jetbrains.annotations.Nullable;

import tekgenesis.service.html.Html;

/**
 * Application Health Checker interface.
 */
public interface StatusService {

    //~ Methods ......................................................................................................................................

    /** Returns true if the application health is OK. */
    boolean check();

    /** Html status page. */
    Html html();

    /** Application Name. */
    String name();

    /** Returns properties to show in application status. */
    @Nullable ObjectNode status();
}
