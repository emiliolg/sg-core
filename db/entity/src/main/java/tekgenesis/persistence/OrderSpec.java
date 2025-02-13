
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

/**
 * Order Specification.
 */
public interface OrderSpec<T> {

    //~ Methods ......................................................................................................................................

    /** The Sql value of the Expression. */
    String asSql(boolean qualify);

    /** Make the OrderSpec a descending one. */
    OrderSpec<T> descending();

    /** Make the OrderSpec use specific sort . */
    OrderSpec<T> nlsSort(String sort);

    /** Make the OrderSpec one that sorts using <code>nulls first</code>. */
    OrderSpec<T> nullsFirst();

    /** Make the OrderSpec one that sorts using <code>nulls last</code>. */
    OrderSpec<T> nullsLast();
}
