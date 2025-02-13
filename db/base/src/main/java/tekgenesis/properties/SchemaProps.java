
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.properties;

import javax.inject.Named;

import tekgenesis.common.core.Times;
import tekgenesis.common.env.Mutable;
import tekgenesis.common.env.Properties;

/**
 * Database properties.
 */
@Mutable
@Named("schemaConfig")
public class SchemaProps implements Properties {

    //~ Instance Fields ..............................................................................................................................

    public String database = "";

    public String indexTablespace = "";

    public boolean remote = false;

    /** Remote timeout for refresh view in minutes. Default 2 hours */
    public int remoteTimeout = 2 * Times.MINUTES_HOUR;

    public String remoteUrl = "";

    public String tableTablespace = "";
}
