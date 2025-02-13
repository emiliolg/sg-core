
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.app.sse;

import javax.inject.Named;

/**
 * Notification Service.
 */
@Named("sse")
public class SSEProps {

    //~ Instance Fields ..............................................................................................................................

    public String  host = null;
    public SSEType type = SSEType.NONE;
}
