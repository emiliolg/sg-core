
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.menu;

/**
 * Username configuration.
 */
public interface UsernameConfiguration {

    //~ Instance Fields ..............................................................................................................................

    /** Default BoxConfiguration. */
    UsernameConfiguration DEFAULT = new Default();

    //~ Methods ......................................................................................................................................

    /** Specifies if Username has user picture. */
    boolean picture();

    /** Specifies if Username has username string. */
    boolean username();

    //~ Inner Classes ................................................................................................................................

    /**
     * Default MenuConfiguration.
     */
    class Default implements UsernameConfiguration {
        @Override public boolean picture() {
            return true;
        }

        @Override public boolean username() {
            return false;
        }
    }
}
