
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import javax.inject.Named;

import tekgenesis.common.env.Mutable;
import tekgenesis.common.env.Properties;

/**
 * ServiceProps.
 */
@Mutable
@Named("service")
@SuppressWarnings("DuplicateStringLiteralInspection")
public class ServiceProps implements Properties {

    //~ Instance Fields ..............................................................................................................................

    public boolean enabled = true;

    /** Shutdown timeout in milliseconds. */
    public long shutdownTimeout = 1_000;
}
