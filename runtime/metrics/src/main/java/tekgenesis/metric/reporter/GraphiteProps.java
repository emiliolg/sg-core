
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metric.reporter;

import javax.inject.Named;

import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Times;

/**
 * Graphite props.
 */
@Named("graphite")
public class GraphiteProps {

    //~ Instance Fields ..............................................................................................................................

    /** hostname. */
    public String host = Constants.LOCALHOST;
    /** port. */
    public int port = DEFAULT_PORT;

    /** publish rate in seconds. */
    public int publishRate = Times.SECONDS_MINUTE;

    //~ Static Fields ................................................................................................................................

    private static final int DEFAULT_PORT = 2003;
}
