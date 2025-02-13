
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;

/**
 * Entity with resource image.
 */
public interface HasChildren<T> {

    //~ Methods ......................................................................................................................................

    /** Children instances. */
    @NotNull Seq<T> children();
}
