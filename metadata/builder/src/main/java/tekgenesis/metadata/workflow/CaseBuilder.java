
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.workflow;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.QName;
import tekgenesis.metadata.common.ModelBuilder;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.metadata.exception.BuilderError;
import tekgenesis.metadata.exception.DuplicateFieldException;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.type.Names;

import static tekgenesis.common.core.QName.EMPTY;

/**
 * Collects the data to build a {@link Case}.
 */
public class CaseBuilder extends ModelBuilder.Default<Case, CaseBuilder> {

    //~ Instance Fields ..............................................................................................................................

    private DbObject boundModel;

    @NotNull private QName                             defaultForm;
    private boolean                                    notifyCase = false;
    @NotNull private final String                      schema;
    @NotNull private final Map<String, TaskBuilder<?>> tasks;

    //~ Constructors .................................................................................................................................

    private CaseBuilder(String sourceName, @NotNull String pkg, @NotNull String name, @NotNull String schemaId) {
        super(sourceName, pkg, name);
        boundModel  = null;
        defaultForm = EMPTY;
        tasks       = new LinkedHashMap<>();
        schema      = Names.validateSchemaId(schemaId, pkg);
    }

    //~ Methods ......................................................................................................................................

    /** Add a task to the builder. */
    public final void addTask(TaskBuilder<?> builder)
        throws DuplicateFieldException
    {
        if (tasks.containsKey(builder.getName())) throw DuplicateFieldException.onCase(builder.getName(), id);
        tasks.put(builder.getName(), builder);
    }

    @Override public Case build() {
        final Map<String, Task> tasksMap = new LinkedHashMap<>();

        final Case model = new Case(sourceName, domain, id, tasksMap, boundModel, defaultForm, schema, notifyCase);

        buildTasks(tasksMap, model);

        return model;
    }

    @NotNull public List<BuilderError> check() {
        return Colls.emptyList();
    }

    /** Mark case a notify.* */
    public void notifyCase() {
        notifyCase = true;
    }

    /** Builds a Task builder. */
    public <T extends TaskBuilder<T>> TaskBuilder<T> task() {
        return new TaskBuilder<>();
    }

    /** Sets the case default Form. */
    public void withDefaultForm(@NotNull Form f) {
        defaultForm = f.getKey();
    }

    /** Sets the case Entity. */
    public void withEntity(@NotNull Entity e) {
        boundModel = e;
    }

    private void buildTasks(Map<String, Task> result, Case model) {
        for (final TaskBuilder<?> b : tasks.values()) {
            final Task a = b.build(model);
            result.put(a.getName(), a);
        }
    }

    //~ Methods ......................................................................................................................................

    /** Creates a {@link CaseBuilder}. */
    public static CaseBuilder create(String sourceName, final String packageId, final String caseName, final String schemaId) {
        return new CaseBuilder(sourceName, packageId, caseName, schemaId);
    }
}  // end class CaseBuilder
