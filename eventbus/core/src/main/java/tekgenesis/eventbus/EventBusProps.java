
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.eventbus;

import javax.inject.Named;

/**
 * Eventbus property.
 */
@Named("eventbus")
public class EventBusProps {

    //~ Instance Fields ..............................................................................................................................

    public boolean enabled        = false;
    public String  implementation = null;
}
