
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.ix;

import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.util.Collections;
import java.util.Iterator;

import static tekgenesis.ix.IxCharset.*;

/**
 * User: emilio Date: 4/12/11 Time: 18:06
 */
class IxCharsetProvider extends CharsetProvider {

    //~ Methods ......................................................................................................................................

    @Override public Charset charsetForName(String name) {
        return IDEAFIX_CHARSET.name().equals(name) ? IDEAFIX_CHARSET : null;
    }
    @Override public Iterator<Charset> charsets() {
        return Collections.singleton(IDEAFIX_CHARSET).iterator();
    }
}
