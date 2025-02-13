
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization;

import tekgenesis.authorization.g.ApplicationAuditBase;
import tekgenesis.persistence.Initialize;

import static tekgenesis.authorization.g.ApplicationAuditTable.APPLICATION_AUDIT;
import static tekgenesis.persistence.EntityListenerType.BEFORE_DELETE;
import static tekgenesis.persistence.Sql.deleteFrom;

/**
 * User class for Model: ApplicationAudit
 */
public class ApplicationAudit extends ApplicationAuditBase {

    //~ Methods ......................................................................................................................................

    @Initialize static void init() {
        User.addListener(BEFORE_DELETE,
            instance -> {
                deleteFrom(APPLICATION_AUDIT).where(APPLICATION_AUDIT.USER_ID.eq(instance.getId())).execute();
                return true;
            });
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 1449310910991872227L;
}
