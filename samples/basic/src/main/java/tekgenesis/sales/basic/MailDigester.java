
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic;

import static tekgenesis.sales.basic.MailDigest.NONE;

/**
 * Mail digester interface with default methods to extend enum behaviour.
 */
public interface MailDigester {

    //~ Methods ......................................................................................................................................

    /** True if MailDigest is none. */
    default boolean isNone() {
        return this == NONE;
    }
}
