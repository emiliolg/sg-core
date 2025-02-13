
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.kafka.eventbus;

import javax.inject.Named;

/**
 * Kafka properties.
 */
@Named("kafka")
@SuppressWarnings({ "WeakerAccess", "MagicNumber" })
public class KafkaProps {

    //~ Instance Fields ..............................................................................................................................

    public long    autoCommitInterval = DEFAULT_AUTO_COMMIT_INTERVAL;  // ms
    public int     batchSize          = 16384;
    public String  bootstrapServers   = "";
    public boolean enableAutoCommit   = true;
    public int     memoryBuffer       = 33554432;
    public long    sessionTimeout     = DEFAULT_SESSION_TIMEOUT;       // ms
    public String  zookeeperServers   = "";

    //~ Static Fields ................................................................................................................................

    private static final long DEFAULT_AUTO_COMMIT_INTERVAL = 500;
    private static final int  DEFAULT_SESSION_TIMEOUT      = 30000;
}
