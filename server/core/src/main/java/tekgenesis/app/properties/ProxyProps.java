
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
import tekgenesis.service.ServiceProps;

/**
 * Application properties.
 */
@Mutable
@Named("proxy")
@SuppressWarnings({ "WeakerAccess", "DuplicateStringLiteralInspection" })
public class ProxyProps extends ServiceProps {

    //~ Instance Fields ..............................................................................................................................

    public String  backend = "";
    @SuppressWarnings("FieldNameHidesFieldInSuperclass")
    public boolean enabled = true;
    public String  host    = "";
    public int     port    = DEFAULT_PORT;

    //~ Static Fields ................................................................................................................................

    private static final int DEFAULT_PORT = 7777;
}
