
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.field.ModelField;

/**
 * Scope of children ModelFields.
 */
public interface Scope {

    //~ Methods ......................................................................................................................................

    /** The first level list of fields. */
    @NotNull Seq<? extends ModelField> getChildren();
}
