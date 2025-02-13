
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster;

import java.util.UUID;

import javax.inject.Named;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.notEmpty;

/**
 * Cluster Properties.
 */
@Named("cluster")
public class ClusterProps {

    //~ Instance Fields ..............................................................................................................................

    // Amazon Web Services Configuration

    public String awsAccessKey = "";
    public String awsBucket    = "";
    public String awsSecretKey = "";
    public String awsTagName   = "";
    /** Cluster name. */
    public String clusterName = "";

    public String members     = "";
    public String props       = "";
    public int    tcpBindPort = DEFAULT_TCP_BIND_PORT;

    private String generatedClusterName = null;

    //~ Methods ......................................................................................................................................

    /** Return cluster name. */
    public String getClusterName() {
        if (generatedClusterName == null) generatedClusterName = UUID.randomUUID().toString();
        return notEmpty(clusterName, generatedClusterName);
    }

    /** Returns true if is a one node cluster. */
    public boolean isLocal() {
        return isEmpty(clusterName);
    }

    //~ Static Fields ................................................................................................................................

    private static final int DEFAULT_TCP_BIND_PORT = 7800;
}
