
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cluster.aws;

import java.net.UnknownHostException;
import java.util.*;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.*;
import com.amazonaws.util.EC2MetadataUtils;

import org.jgroups.Address;
import org.jgroups.Event;
import org.jgroups.Message;
import org.jgroups.PhysicalAddress;
import org.jgroups.annotations.Property;
import org.jgroups.conf.ClassConfigurator;
import org.jgroups.protocols.Discovery;
import org.jgroups.protocols.PingData;
import org.jgroups.protocols.PingHeader;
import org.jgroups.stack.IpAddress;
import org.jgroups.util.Responses;
import org.jgroups.util.UUID;

import tekgenesis.common.core.Tuple;
import tekgenesis.common.logging.Logger;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Tuple.tuple;

/**
 * AWS Discovery protocol for JGroups.
 */
public class AWSPING extends Discovery {

    //~ Instance Fields ..............................................................................................................................

    private AmazonEC2 ec2     = null;
    @Property(description     = "Port")
    @SuppressWarnings({ "FieldMayBeFinal", "FieldCanBeLocal" })
    private int       port    = DEFAULT_PORT;
    @Property(description     = "AWS tag name")
    @SuppressWarnings("FieldMayBeFinal")
    private String    tagName = "";

    //~ Methods ......................................................................................................................................

    @Override public void start()
        throws Exception
    {
        super.start();

        ec2 = AmazonEC2ClientBuilder.defaultClient();
    }

    @Override public void stop() {
        if (ec2 != null) ec2.shutdown();
        super.stop();
    }

    @Override public boolean isDynamic() {
        return false;
    }

    @Override protected void findMembers(List<Address> m, boolean initial_discovery, Responses responses) {
        final PhysicalAddress physical_addr = (PhysicalAddress) down(new Event(Event.GET_PHYSICAL_ADDRESS, local_addr));

        // https://issues.jboss.org/browse/JGRP-1670
        final PingData   data = new PingData(local_addr, false, UUID.get(local_addr), physical_addr);
        final PingHeader hdr  = new PingHeader(PingHeader.GET_MBRS_REQ).clusterName(cluster_name);

        final Set<PhysicalAddress> cluster_members = getPrivateIpAddresses();

        for (final PhysicalAddress addr : cluster_members) {
            if (physical_addr == null || !addr.equals(physical_addr)) {
                // the message needs to be DONT_BUNDLE, see explanation above
                final Message msg = new Message(addr).setFlag(Message.Flag.INTERNAL, Message.Flag.DONT_BUNDLE, Message.Flag.OOB)
                                    .putHeader(id, hdr)
                                    .setBuffer(marshal(data));
                log.trace("%s: sending discovery request to %s", local_addr, msg.getDest());
                down_prot.down(new Event(Event.MSG, msg));
            }
        }
    }

    private Filter[] getFilters() {
        final Filter[] filters = new Filter[2];

        final Tuple<String, String> tag = getTag(isEmpty(tagName) ? "aws:autoscaling:groupName" : tagName);
        filters[0] = new Filter("tag:" + tag.first()).withValues(tag.second());
        filters[1] = new Filter("instance-state-name").withValues(InstanceStateName.Running.toString());

        return filters;
    }

    private Set<PhysicalAddress> getPrivateIpAddresses() {
        final Set<PhysicalAddress>     result  = new HashSet<>();
        final DescribeInstancesRequest request = new DescribeInstancesRequest().withFilters(getFilters());

        final DescribeInstancesResult describeInstancesResult = ec2.describeInstances(request);

        for (final Reservation reservation : describeInstancesResult.getReservations()) {
            for (final Instance instance : reservation.getInstances()) {
                try {
                    result.add(new IpAddress(instance.getPrivateIpAddress(), port));
                }
                catch (final UnknownHostException e) {
                    logger.error(e);
                }
            }
        }
        return result;
    }

    private Tuple<String, String> getTag(String t) {
        // resolve the instanceId
        final String instanceId = EC2MetadataUtils.getInstanceId();

        final DescribeInstancesResult describeInstancesResult = ec2.describeInstances(new DescribeInstancesRequest().withInstanceIds(instanceId));
        for (final Reservation reservation : describeInstancesResult.getReservations()) {
            for (final Instance instance : reservation.getInstances()) {
                for (final Tag tag : instance.getTags()) {
                    if (tag.getKey().equals(t)) return tuple(t, tag.getValue());
                }
            }
        }
        throw new IllegalStateException("No tag " + tagName + " defined ");
    }

    //~ Static Fields ................................................................................................................................

    private static final short    ID   = 666;
    protected static final String NAME = "AWSPING";

    private static final int    DEFAULT_PORT = 7800;
    private static final Logger logger       = Logger.getLogger(AWSPING.class);

    static {
        ClassConfigurator.addProtocol(ID, AWSPING.class);  // ID needs to be unique
    }
}  // end class AWSPING
