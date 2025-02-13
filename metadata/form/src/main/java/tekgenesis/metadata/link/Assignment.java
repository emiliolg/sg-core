
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.link;

import java.io.Serializable;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import tekgenesis.field.ModelField;

/**
 * An assignment of a parameter of a form in a form menu item including values corresponding to
 * 'field=value'.
 */
public class Assignment implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    @SuppressWarnings("GwtInconsistentSerializableClass")
    private ModelField   field;
    private Serializable value;

    //~ Constructors .................................................................................................................................

    Assignment() {
        field = null;
        value = null;
    }

    Assignment(@NotNull ModelField field, @NotNull Serializable value) {
        this.field = field;
        this.value = value;
    }

    //~ Methods ......................................................................................................................................

    /** Solves all references on form fields. */
    public void solve(Function<ModelField, ModelField> solver) {
        field = solver.apply(field);
    }

    /** Get parameter field. */
    @NotNull public ModelField getField() {
        return field;
    }

    /** Get parameter value corresponding to this field. */
    @NotNull public Serializable getValue() {
        return value;
    }

    /** Sets the value. */
    public void setValue(@NotNull Serializable value) {
        this.value = value;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 6886244208241714010L;
}  // end class Assignment
