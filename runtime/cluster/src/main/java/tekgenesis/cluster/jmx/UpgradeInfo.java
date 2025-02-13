
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
import org.jetbrains.annotations.Nullable;

/**
 */
public class UpgradeInfo implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final String  ip;
    @Nullable private final String log;
    @NotNull private final Status  status;

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public UpgradeInfo(@NotNull String ip, @NotNull Status status, @Nullable String log) {
        this.ip     = ip;
        this.status = status;
        this.log    = log;
    }

    //~ Methods ......................................................................................................................................

    /** Member Ip. */
    @NotNull public String getIp() {
        return ip;
    }

    /** output message. */
    @Nullable public String getLog() {
        return log;
    }

    /** Status. */
    @NotNull public Status getStatus() {
        return status;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 4449415502286582236L;

    //~ Enums ........................................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    public enum Status {
        FAILED("Failed"), READY("Ready"), RESTARTING("Restarting"), STOP("Stopped"), UPDATED("Updated"), UPDATING("Updating"), SAFE_MODE("Safe Mode");

        private final String name;

        Status(String n) {
            name = n;
        }

        /**  */
        public String getName() {
            return name;
        }
    }
}  // end class UpgradeInfo
