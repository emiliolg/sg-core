
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.es;

import javax.inject.Named;

import tekgenesis.common.Predefined;
import tekgenesis.common.env.Properties;

/**
 * Properties for ElasticSearch.
 */
@Named(ESConstants.INDEX)
public class IndexProperties implements Properties {

    //~ Instance Fields ..............................................................................................................................

    /** Port to run embedded server. 9200 is the default port. */
    public int port = DEFAULT_PORT;

    /** Define a prefix for indices. */
    public String prefix = null;

    /** Set elastic search url. Null value will run embedded option. */
    public String url = null;

    //~ Methods ......................................................................................................................................

    /** Returns if url is defined or if embedded server must be started. */
    public boolean isEmbedded() {
        return Predefined.isEmpty(url);
    }

    //~ Static Fields ................................................................................................................................

    public static final int             DEFAULT_PORT = 9200;
    public static final IndexProperties DEFAULT      = new IndexProperties();
}
