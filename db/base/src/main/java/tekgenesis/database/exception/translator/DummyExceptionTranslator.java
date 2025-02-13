
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.exception.translator;

/**
 * Dummy Exception translator for SQL Exceptions. Will only delegate to State translation
 */
public class DummyExceptionTranslator extends SQLExceptionTranslator {

    //~ Methods ......................................................................................................................................

    @Override
    @SuppressWarnings("MagicNumber")
    protected void populateErrorCodeMapping() {
        // Empty
    }
}
