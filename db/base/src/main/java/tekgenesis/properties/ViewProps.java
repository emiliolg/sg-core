
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

import tekgenesis.common.env.Mutable;
import tekgenesis.common.env.Properties;

/**
 * View properties.
 */
@Mutable
@Named("view")
public class ViewProps implements Properties {

    //~ Instance Fields ..............................................................................................................................

    public Integer batchSize = null;

    public boolean ignoreDeletions = false;
}
