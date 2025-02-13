
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization.social;

/**
 * SuiGeneris social providers.
 */
public enum Provider {

    //~ Enum constants ...............................................................................................................................

    FACEBOOK, GOOGLE;

    //~ Methods ......................................................................................................................................

    /** Return provider id. */
    public String id() {
        return name().toLowerCase();
    }
}
