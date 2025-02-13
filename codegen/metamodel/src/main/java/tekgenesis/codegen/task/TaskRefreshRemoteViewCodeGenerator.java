
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.task;

import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.common.MMCodeGenerator;
import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.metadata.entity.View;

import static tekgenesis.codegen.common.MMCodeGenConstants.WEAKER_ACCESS;
import static tekgenesis.common.core.Strings.*;

/**
 * TaskRefreshRemoteViewCodeGenerator.
 */
public class TaskRefreshRemoteViewCodeGenerator extends ClassGenerator implements MMCodeGenerator {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final View view;

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public TaskRefreshRemoteViewCodeGenerator(JavaCodeGenerator generator, @NotNull View v, String name) {
        super(generator, name);
        view = v;
    }

    //~ Methods ......................................................................................................................................

    public void populate() {
        // generate code for form base class
        withComments("Generated RefreshViewTask class for remote view : " + view.getName() + ".");
        withComments(MODIFICATION_WARNING_LINE_1);
        withComments(MODIFICATION_WARNING_LINE_2);
        suppressWarnings(WEAKER_ACCESS);
        // noinspection DuplicateStringLiteralInspection
        constructor().asPrivate().arg("task", extractImport("tekgenesis.task.ScheduledTask")).superArg().notNull();
        withSuperclass(extractImport("tekgenesis.task.RefreshRemoteViewTaskBase"));

        method("getDomain", String.class).override().return_(quoted(view.getDomain()));
        addLogger(view.getFullName());
        super.populate();
    }

    @Override public String getSourceName() {
        return view.getSourceName();
    }
}
