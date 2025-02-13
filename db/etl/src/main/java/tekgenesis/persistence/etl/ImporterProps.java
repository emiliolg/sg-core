
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.etl;

import javax.inject.Named;

import tekgenesis.common.env.Mutable;
import tekgenesis.service.ServiceProps;

/**
 * Importer Service Props.
 */
@Mutable
@Named("importer")
@SuppressWarnings("WeakerAccess")
public class ImporterProps extends ServiceProps {

    //~ Instance Fields ..............................................................................................................................

    /** Importer Dir. If set will start importer service on startup */
    public String dir = null;
}
