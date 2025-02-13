
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.compiler.model;

/**
 * Jps serialization properties for internal jdk.
 */
public class SuiGenerisJpsSdkProperties {

    //~ Instance Fields ..............................................................................................................................

    private final String internalJdk;

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public SuiGenerisJpsSdkProperties(String internalJdk) {
        this.internalJdk = internalJdk;
    }

    //~ Methods ......................................................................................................................................

    /** Get internal jdk name. */
    public String getJdkName() {
        return internalJdk;
    }
}
