
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.aws.eventbus;

import javax.inject.Named;

/**
 * Kinesis properties.
 */
@Named("kinesis")
@SuppressWarnings({ "WeakerAccess", "MagicNumber" })
public class KinesisProps {

    //~ Instance Fields ..............................................................................................................................

    public String appName                       = "";
    public long   failoverTimemillis            = DEFAULT_FAILOVER_MS;
    public long   idleTimeBetweenReads          = 1000;
    public int    maxRecords                    = DEFAULT_MAX_RECORDS;
    public long   parentShardPollIntervalMillis = 10000;
    public String profileName                   = "";
    public String region                        = "us-east-1";
    public long   shardSyncIntervalMillis       = 10000;

    //~ Static Fields ................................................................................................................................

    private static final long DEFAULT_FAILOVER_MS = 15000L;
    private static final int  DEFAULT_MAX_RECORDS = 200;
}
