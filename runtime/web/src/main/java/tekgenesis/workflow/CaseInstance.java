
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.workflow;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.PersistableInstance;

/**
 * A Case instance.
 */
public interface CaseInstance<This extends CaseInstance<This, Key, Payload, PayloadKey, WorkItem, WorkItemKey>,
                              Key,
                              Payload extends EntityInstance<Payload, PayloadKey>,
                              PayloadKey, WorkItem extends WorkItemInstance<WorkItem, WorkItemKey, This, Key, Payload, PayloadKey>, WorkItemKey>
    extends PersistableInstance<This, Key>
{

    //~ Methods ......................................................................................................................................

    /** Process. */
    void process(@NotNull final WorkItem item, @NotNull String result);

    /** Returns the creation date. */
    @NotNull DateTime getCreation();

    /** Returns the case entity. */
    @NotNull Payload getEntity();
}
