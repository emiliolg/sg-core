
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.app.properties;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Times;
import tekgenesis.common.env.Mutable;
import tekgenesis.common.env.Properties;

/**
 * Application properties.
 */
@Mutable
@Named("application")
@SuppressWarnings({ "DuplicateStringLiteralInspection", "WeakerAccess" })
public class ApplicationProps implements Properties {

    //~ Instance Fields ..............................................................................................................................

    /** SuiGeneris admin initial password. */
    public String adminPassword = Constants.SHIRO_ADMIN_PASS;

    /** SuiGeneris admin user id. */
    public String adminUser = Constants.SHIRO_ADMIN_USER;

    /** Host for qualifying cdn resources such as css, js and img. */
    public String cdnHost = "";

    /** Concurrent task execution per node. */
    public int concurrentTasks = 5;

    /** Default roles assigned to user when logged for the first time. */
    public String defaultRoles = null;

    /** External documentation server url. */
    public String externalDocUrl = "";

    /** Handlers cache map size per node. */
    public int handlersCacheSize = 0;

    /** Enable/disable lifecycle Task execution.* */
    public boolean lifecycleTaskEnabled = true;

    /** no cluster configuration. */
    public boolean noCluster = false;

    /** Indicates that the existing database will be recreated. */
    public boolean resetDB = false;

    /** Compiled resources dir for html, css, js and img. Defaults to run dir. */
    public String resourceOutDir = "";

    /** Resources cache map size per node (for serving resources from the DB). */
    public int resourcesCacheSize = 0;

    /** Resources dir for html, css, js and img. */
    public String resourceSrcDir = "";

    /** The full path of the cdn image server url (ex: http://myimageserver.com/images). */
    public String resourceUrl = Constants.RESOURCE_SERVLET_PATH;

    /** List of restricted paths names to filter by ip separated by ','. */
    public String restrictedPaths = "";

    /** Application run dir. */
    public String runDir = System.getProperty(Constants.SYSTEM_RUNDIR, "");

    /** Indicates whether the Database seed needs to be imported. */
    public boolean seedDB = false;

    /** Optional additional seed dir. */
    public String seedDir = "";

    /** Seed only from dir. */
    public boolean seedDirOnly = false;

    /** Enable sync servlet. */
    public boolean syncEnabled = false;

    /** Task Service timeout for shutdown Service.* */
    public long taskServiceTimeout = Times.MILLIS_MINUTE;

    /** WebProxytimeout in seconds. */
    public int webProxyTimeout = Times.SECONDS_MINUTE;

    /** WebProxy url's separated by ';'. */
    public String webProxyUrl = "";

    //~ Methods ......................................................................................................................................

    /** Get the resourceOutDir if exists or the resourceSrcDir otherwise. */
    @NotNull public File getResourceOutDir() {
        if (!resourceOutDir.isEmpty()) return new File(resourceOutDir);
        return new File(runDir, "resources.out");
    }

    /** Get the resourceSrcDir if exists or <code>null</code> otherwise. */
    @NotNull public Seq<File> getResourceSrcDir() {
        if (resourceSrcDir.isEmpty()) return Colls.emptyIterable();

        final List<File> result = new ArrayList<>();
        for (final String path : Strings.split(resourceSrcDir, ',')) {
            if (!path.isEmpty()) {
                final File d = new File(path);
                if (d.exists()) result.add(d);
            }
        }
        return Colls.seq(result);
    }

    /** Get the SeedDir if exists or <code>null</code> otherwise. */
    @Nullable public File getSeedDir() {
        if (seedDir.isEmpty()) return null;
        final File d = new File(seedDir);
        return d.exists() ? d : null;
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Shiro user properties.
     */
    @Mutable
    @Named("filter")
    public static class PathFilter implements Properties {
        /** ip address regular expressions separated by ','. */
        public String allowed = "";
        /** path pattern. */
        public String path = null;
    }
}  // end class ApplicationProps
