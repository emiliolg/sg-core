
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.workflow;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.field.FieldOptions;
import tekgenesis.field.MetaModelReference;
import tekgenesis.field.ModelField;
import tekgenesis.metadata.form.widget.FormAction;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.field.FieldOption.*;

/**
 * This class represents a case Task.
 */
public class Task implements ModelField {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Seq<FormAction> actions;

    @NotNull private final ImmutableList<Task> children;
    @NotNull private final FieldOptions        options;
    @NotNull private final Case                owner;

    //~ Constructors .................................................................................................................................

    Task(@NotNull Case owner, @NotNull FieldOptions options, @NotNull ImmutableList<Task> children, @NotNull List<FormAction> actions) {
        this.owner    = owner;
        this.options  = options;
        this.children = children;
        this.actions  = immutable(actions);
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean equals(Object obj) {
        return obj instanceof Task && getFullName().equals(((Task) obj).getFullName());
    }

    @Override public boolean hasChildren() {
        return !children.isEmpty();
    }

    @Override public int hashCode() {
        return getFullName().hashCode();
    }

    /** Get actions for this task with default labels. */
    @NotNull public Seq<FormAction> getActions() {
        return actions;
    }

    @NotNull @Override public ImmutableList<? extends ModelField> getChildren() {
        return children;
    }

    /** Get the full qualified name of the Form associated to this task. */
    @NotNull public MetaModelReference getForm() {
        return options.getMetaModelReference(FORM);
    }

    @NotNull public String getLabel() {
        return options.getString(LABEL);
    }

    @NotNull @Override public String getName() {
        return options.getString(ID);
    }

    @NotNull @Override public FieldOptions getOptions() {
        return options;
    }

    /** Get the name of the Process associated to this task. */
    @NotNull public String getProcess() {
        return options.getString(PROCESS);
    }

    @NotNull @Override public Type getType() {
        return Types.anyType();
    }

    @Override public void setType(@NotNull Type type) {
        throw new IllegalStateException();
    }

    /** Returns the task full name. */
    private String getFullName() {
        return owner.getFullName() + "." + getName();
    }
}  // end class Task
