
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.admin;

import java.lang.management.ManagementFactory;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zaxxer.hikari.HikariPoolMXBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.admin.sg.AdminViews;
import tekgenesis.admin.status.DatabaseInfo;
import tekgenesis.admin.status.MemoryInfo;
import tekgenesis.cluster.ClusterManager;
import tekgenesis.common.core.Constants;
import tekgenesis.common.env.Version;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.json.JsonMapping;
import tekgenesis.service.html.Html;

import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.task.jmx.JmxConstants.HIKARI_MBEAN;

/**
 * Sui Generis StatusService implementation.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public class SuiGenerisStatusService implements StatusService {

    //~ Methods ......................................................................................................................................

    @Override public boolean check() {
        return true;
    }

    @Override public Html html() {
        return AdminViews.statusSuiGeneris(getVersion(), new MemoryInfo(), getDatabaseInfo());
    }

    @Override public String name() {
        return Constants.SUI_GENERIS;
    }

    @Nullable @Override public ObjectNode status() {
        final ObjectMapper objectMapper = JsonMapping.shared();
        final ObjectNode   objectNode   = objectMapper.createObjectNode();
        objectNode.put("version", getVersion());
        objectNode.put("server", Context.getSingleton(ClusterManager.class).getCurrentMember());
        objectNode.set("memory", objectMapper.valueToTree(new MemoryInfo()));
        objectNode.set("database", objectMapper.valueToTree(getDatabaseInfo()));
        return objectNode;
    }

    @NotNull protected String getVersion() {
        return notEmpty(Version.getInstance().getComponent(Constants.SUI_GENERIS).getVersion(), "Dev");
    }

    private DatabaseInfo getDatabaseInfo() {
        final DatabaseInfo info        = new DatabaseInfo();
        final MBeanServer  mBeanServer = ManagementFactory.getPlatformMBeanServer();
        final ObjectName   poolName;
        try {
            poolName = new ObjectName(HIKARI_MBEAN);
        }
        catch (final MalformedObjectNameException e) {
            return info;
        }
        final HikariPoolMXBean poolProxy = JMX.newMXBeanProxy(mBeanServer, poolName, HikariPoolMXBean.class);
        return new DatabaseInfo(poolProxy.getTotalConnections(),
            poolProxy.getActiveConnections(),
            poolProxy.getIdleConnections(),
            poolProxy.getThreadsAwaitingConnection());
    }
}  // end class SuiGenerisStatusService
