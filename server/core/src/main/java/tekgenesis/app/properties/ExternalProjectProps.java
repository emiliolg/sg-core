
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

/**
 * External project properties.
 */
@Mutable
@Named("external")
@SuppressWarnings("DuplicateStringLiteralInspection")
public class ExternalProjectProps {

    //~ Instance Fields ..............................................................................................................................

    /** Project endpoint host. */
    public String endpoint = "";
}
