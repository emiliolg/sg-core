
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.runner;

import java.util.List;

import javax.swing.*;

import com.intellij.openapi.module.Module;
import com.intellij.ui.ListCellRendererWrapper;
import com.intellij.ui.SortedComboBoxModel;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.MMFileType;
import tekgenesis.lang.mm.MMModuleComponent;
import tekgenesis.metadata.task.Task;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.common.Predefined.isNotEmpty;

/**
 * Task Selector for MM Task Configuration. Created by Jose on 9/2/14.
 */
public class ConfigurationTaskSelector {

    //~ Instance Fields ..............................................................................................................................

    private Module                          module         = null;
    private final SortedComboBoxModel<Task> taskComboModel;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private final JComboBox tasksComboBox;

    //~ Constructors .................................................................................................................................

    /** Configuration Task selector constructor. */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ConfigurationTaskSelector(final JComboBox comboBox, SortedComboBoxModel<Task> taskComboModel) {
        tasksComboBox       = comboBox;
        this.taskComboModel = taskComboModel;
    }

    //~ Methods ......................................................................................................................................

    /** Populates the Task combo box whit every one found inside the previous selected module. */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void initTasks() {
        if (module == null) return;
        final ModelRepository repo = module.getComponent(MMModuleComponent.class).getRepository();

        final List<Task> tasksList = repo.getModels().filter(Task.class).toList();

        taskComboModel.clear();
        if (tasksList.isEmpty()) taskComboModel.add(null);
        else taskComboModel.addAll(tasksList);

        tasksComboBox.setModel(taskComboModel);
        tasksComboBox.setRenderer(new ListCellRendererWrapper() {
                @Override public void customize(final JList list, final Object value, final int index, final boolean selected,
                                                final boolean hasFocus) {
                    if (value instanceof Task) {
                        final Task task = (Task) value;
                        setIcon(MMFileType.TASK_ICON);
                        setText(task.getName());
                    }
                    else if (value == null) {
                        setIcon(null);
                        setText("<None>");
                    }
                }
            });

        if (!tasksList.isEmpty()) tasksComboBox.setSelectedItem(taskComboModel.get(taskComboModel.getSize() - 1));
    }

    /** specify the module selected in the configuration. */
    public void setModule(Module module) {
        this.module = module;
    }

    /** Get selected Task, actually we are working whit the Task name. */
    @Nullable public Task getTask() {
        return taskComboModel.getSelectedItem();
    }

    /** Set selected Task. */
    public void setTask(String taskId) {
        if (module == null) return;
        final ModelRepository repo       = module.getComponent(MMModuleComponent.class).getRepository();
        final Option<Task>    taskOption = isNotEmpty(taskId) ? repo.getModel(taskId, Task.class) : Option.empty();
        if (taskOption.isPresent()) taskComboModel.setSelectedItem(taskOption.get());
    }
}  // end class ConfigurationTaskSelector
