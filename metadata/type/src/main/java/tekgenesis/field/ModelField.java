
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.field;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.type.Typed;

/**
 * Common operations for Fields.
 */
public interface ModelField extends Typed {

    //~ Methods ......................................................................................................................................

    /** Returns true if the field has children. */
    boolean hasChildren();

    /** returns the children of this element. */
    @NotNull ImmutableList<? extends ModelField> getChildren();

    /** returns the documentation of the field. */
    @NotNull default String getFieldDocumentation() {
        return "";
    }

    /** Get the label of the Field. */
    @NotNull String getLabel();
    /** Get the name of the Field. */
    @NotNull String getName();

    /** Return the options for the field. */
    @NotNull FieldOptions getOptions();
}
