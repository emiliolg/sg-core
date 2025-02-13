
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.jmx;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.DateTime;
import tekgenesis.sg.MemberStatus;
import tekgenesis.sg.NodeEntry;

/**
 * Simple helper class just because an Entities are not serializable.
 */
public class NodeStatus implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private DateTime     creationTime = DateTime.EPOCH;
    @NotNull private String       log          = "";
    @NotNull private String       name         = "";
    @NotNull private MemberStatus status       = MemberStatus.FAILED;
    @NotNull private DateTime     updateTime   = DateTime.EPOCH;

    //~ Constructors .................................................................................................................................

    /** default constructor. */
    public NodeStatus() {}

    //~ Methods ......................................................................................................................................

    /** creation time. */
    @NotNull public DateTime getCreationTime() {
        return creationTime;
    }

    /** Member's status log messages, if exists. */
    @NotNull public String getLog() {
        return log;
    }

    /** Node name. */
    @NotNull public String getName() {
        return name;
    }

    /** Status. */
    @NotNull public MemberStatus getStatus() {
        return status;
    }

    /** Update time. */
    @NotNull public DateTime getUpdateTime() {
        return updateTime;
    }

    //~ Methods ......................................................................................................................................

    /** Creates an NodeStatus from a NodeEntry. */
    public static NodeStatus create(@NotNull NodeEntry nodeEntry) {
        final NodeStatus s = new NodeStatus();

        s.name         = nodeEntry.getName();
        s.status       = nodeEntry.getStatus();
        s.log          = nodeEntry.getLog();
        s.updateTime   = nodeEntry.getUpdateTime();
        s.creationTime = nodeEntry.getCreationTime();

        return s;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -4466874329079018190L;
}  // end class NodeStatus
