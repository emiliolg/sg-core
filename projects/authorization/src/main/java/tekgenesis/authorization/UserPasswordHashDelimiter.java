
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization;

/**
 * Returns the delimiter for a user password.
 */
public interface UserPasswordHashDelimiter {

    //~ Methods ......................................................................................................................................

    /** Returns the first token. */
    String getFirstToken(final String username);
}
