
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
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateTime;
import tekgenesis.metadata.authorization.Assignee;
import tekgenesis.metadata.authorization.OrganizationalUnit;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.PersistableInstance;

/**
 * A Work Item instance.
 */
public interface WorkItemInstance<This extends WorkItemInstance<This, Key, Case, CaseKey, Payload, PayloadKey>,
                                  Key,
                                  Case extends CaseInstance<Case, CaseKey, Payload, PayloadKey, This, Key>,
                                  CaseKey,                                              //
                                  Payload extends EntityInstance<Payload, PayloadKey>,  //
                                  PayloadKey>
    //
    extends PersistableInstance<This, Key>
{

    //~ Methods ......................................................................................................................................

    /** Returns the item assignee. */
    @NotNull String getAssignee();

    /** Set item assignee. */
    void setAssignee(@NotNull Assignee assignee);

    /** Returns the item associated business key (if any). */
    @Nullable String getBusinessKey();

    /** Set the item associated business key (if any). */
    This setBusinessKey(@Nullable String businessKey);

    /** Returns the creation date. */
    @NotNull DateTime getCreation();

    /** Returns true if item is closed. */
    boolean isClosed();

    /** Get item custom description. */
    @NotNull String getDescription();

    /** Set item custom description. */
    This setDescription(@NotNull String description);

    /** Set optional organizational unit. */
    void setOrganizationalUnit(@NotNull OrganizationalUnit orgUnit);

    /** Get optional organizational unit. */
    @Nullable String getOuName();

    /** Returns the parent case. */
    @NotNull Case getParentCase();

    /** Returns the priority. */
    @NotNull WorkItemPriority getPriority();

    /** Get optional reporter. */
    @Nullable String getReporter();

    /** Sets the value of the Reporter. */
    This setReporter(@Nullable String reporter);

    /** Returns the task to which this item is associated. */
    @NotNull String getTask();

    /** Get item custom title. */
    @Nullable String getTitle();

    /** Set item custom title. */
    This setTitle(@NotNull String title);
}  // end interface WorkItemInstance
