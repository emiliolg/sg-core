
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

/**
 * JsonConfiguration.
 */
public interface JsonConfiguration extends FormEtlConfiguration<JsonConfiguration> {

    //~ Instance Fields ..............................................................................................................................

    String X_PRETTY_PRINT = "X-Pretty";

    //~ Methods ......................................................................................................................................

    /** Pretty print json formatting. */
    JsonConfiguration prettyPrinting();
}
