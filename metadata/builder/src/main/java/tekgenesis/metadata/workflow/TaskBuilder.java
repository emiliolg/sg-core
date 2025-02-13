
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.workflow;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.field.FieldOption;
import tekgenesis.field.MetaModelReference;
import tekgenesis.metadata.entity.FieldBuilder;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.form.widget.FormAction;

/**
 * Builder for case tasks.
 */
public class TaskBuilder<This extends TaskBuilder<This>> extends FieldBuilder<This> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final List<FormAction> actions;

    //~ Constructors .................................................................................................................................

    /** Builder for case tasks. */
    public TaskBuilder() {
        actions = new ArrayList<>();
    }

    //~ Methods ......................................................................................................................................

    /** Build a Task. */
    public Task build(Case model) {
        return new Task(model, getOptions(), Colls.emptyList(), actions);
    }

    /** Set task associated form. */
    public void form(MetaModelReference form)
        throws BuilderException
    {
        with(FieldOption.FORM, form);
    }

    /** Set task associated process. */
    public void process(String process)
        throws BuilderException
    {
        with(FieldOption.PROCESS, process);
    }

    /** Add an action to the task. */
    public void withAction(@NotNull final FormAction action) {
        actions.add(action);
    }

    @Override protected void checkOptionSupport(FieldOption option)
        throws BuilderException {}

    /** Make it package visible. */
    @Override protected String getName() {
        return super.getName();
    }
}  // end class TaskBuilder
