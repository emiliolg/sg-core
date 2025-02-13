
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.app.properties;

import javax.inject.Named;

import tekgenesis.common.env.Mutable;
import tekgenesis.common.env.Properties;

/**
 * View properties.
 */
@Mutable
@Named("view")
@SuppressWarnings("WeakerAccess")
public class ViewProps implements Properties {

    //~ Instance Fields ..............................................................................................................................

    public int refresh = ONE_HOUR;

    //~ Static Fields ................................................................................................................................

    public static final int ONE_HOUR = 60;
}
