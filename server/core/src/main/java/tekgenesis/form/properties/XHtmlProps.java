
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.properties;

import javax.inject.Named;

import tekgenesis.common.env.Properties;

/**
 * Map properties.
 */
@Named("xhtml")
@SuppressWarnings("DuplicateStringLiteralInspection")
public class XHtmlProps implements Properties {

    //~ Instance Fields ..............................................................................................................................

    /** Html cache time in seconds. */
    public int cacheExpiration = 0;
}
