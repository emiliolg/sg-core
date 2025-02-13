
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.field;

/**
 * Specifies underlying field maybe signed.
 */
public interface Signed {

    //~ Methods ......................................................................................................................................

    /** Returns true when the number can be positive or negative. */
    boolean isSigned();
}
