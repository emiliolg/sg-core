
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.workflow;

import tekgenesis.metadata.form.InstanceReference;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.EntityTable;

import static tekgenesis.common.Predefined.cast;

/**
 * WorkItemInstance from InstanceReference.
 */
public class WorkItemReferences {

    //~ Constructors .................................................................................................................................

    private WorkItemReferences() {}

    //~ Methods ......................................................................................................................................

    /** Return specified WorkItemInstance. */
    public static WorkItemInstance<?, ?, ?, ?, ?, ?> createWorkInstance(final InstanceReference r) {
        final EntityInstance<?, ?> instance = EntityTable.forName(r.getFqn().getFullName()).findByString(r.getKey());
        if (instance == null) throw new IllegalStateException();
        return cast(instance);
    }
}
