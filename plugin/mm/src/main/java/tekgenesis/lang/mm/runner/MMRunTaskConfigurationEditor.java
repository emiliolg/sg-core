
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.runner;

import java.awt.*;
import java.util.Comparator;

import javax.swing.*;

import com.intellij.application.options.ModulesComboBox;
import com.intellij.execution.ui.ConfigurationModuleSelector;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.SortedComboBoxModel;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.task.Task;

import static tekgenesis.lang.mm.i18n.PluginMessages.MSGS;

/**
 * MM Task Configuration Editor. Created by Jose on 8/27/14.
 */
public class MMRunTaskConfigurationEditor extends AbstractMMRunConfigurationEditor<MMRunTaskConfiguration> {

    //~ Instance Fields ..............................................................................................................................

    private JCheckBox createDBCheckBox = null;

    private JTextField dataField = null;

    private final ModulesComboBox             modulesCombo;
    private final ConfigurationModuleSelector myModuleSelector;
    private final ConfigurationTaskSelector   myTaskSelector;
    private JCheckBox                         noCluster = null;
    private final ComboBox<Task>              taskCombo;

    private JTextField vmOptionsField = null;

    //~ Constructors .................................................................................................................................

    /** Constructor. Receives the Project */
    public MMRunTaskConfigurationEditor(final Project project) {
        modulesCombo     = new ModulesComboBox();
        taskCombo        = new ComboBox<>();
        createDBCheckBox = new JCheckBox();

        final SortedComboBoxModel<Task> taskComboModel = new SortedComboBoxModel<>(Comparator.comparing(Task::getName));
        myTaskSelector   = new ConfigurationTaskSelector(taskCombo, taskComboModel);
        myModuleSelector = new ConfigurationModuleSelector(project, modulesCombo);
    }

    //~ Methods ......................................................................................................................................

    @Override protected void applyEditorTo(@NotNull MMRunTaskConfiguration mmRunConfiguration)
        throws ConfigurationException
    {
        myModuleSelector.applyTo(mmRunConfiguration);
        if (myTaskSelector.getTask() != null) mmRunConfiguration.setTaskId(myTaskSelector.getTask().getFullName());
        mmRunConfiguration.setData(dataField.getText());
        mmRunConfiguration.setCleanRunDir(createDBCheckBox.isSelected());
        mmRunConfiguration.setNoCluster(noCluster.isSelected());
        mmRunConfiguration.setRunDir(runDirField.getText());
        mmRunConfiguration.setPropertiesFile(propertiesFile.getText());
        mmRunConfiguration.setVmOptions(vmOptionsField.getText());
    }

    @NotNull @Override
    @SuppressWarnings("OverlyLongMethod")
    protected JComponent createEditor() {
        noCluster = new JCheckBox();

        final Component runDirComponent    = super.createRunDirComponent();
        final Component propsFileComponent = createPropertiesFileComponent();

        final JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        final JPanel modulesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        modulesPanel.add(new JLabel(MSGS.moduleToRunMessage()));
        modulesPanel.add(modulesCombo);

        modulesCombo.addItemListener(e -> {
            myTaskSelector.setModule(myModuleSelector.getModule());
            myTaskSelector.initTasks();
        });

        final JPanel tasksPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        modulesPanel.add(new JLabel("Task to run:"));
        modulesPanel.add(taskCombo);

        jPanel.add(modulesPanel);
        jPanel.add(tasksPanel);

        jPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        final JPanel runPanel = new JPanel(new BorderLayout(5, 0));
        runPanel.add(new JLabel("Run Dir: "), BorderLayout.WEST);
        runPanel.add(runDirComponent, BorderLayout.CENTER);
        jPanel.add(runPanel);

        jPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        final JPanel vmOptionsPanel = new JPanel(new BorderLayout(5, 0));
        vmOptionsPanel.add(new JLabel(MSGS.vmOptionsMessage()), BorderLayout.WEST);
        vmOptionsField = new JTextField();
        vmOptionsPanel.add(vmOptionsField, BorderLayout.CENTER);
        jPanel.add(vmOptionsPanel);

        jPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        final JPanel propsPanel = new JPanel(new BorderLayout(5, 0));
        propsPanel.add(new JLabel(MSGS.propertiesFileMessage()), BorderLayout.WEST);
        propsPanel.add(propsFileComponent, BorderLayout.CENTER);
        jPanel.add(propsPanel);

        jPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        final JPanel createDBPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        // noinspection DuplicateStringLiteralInspection
        createDBPanel.add(new JLabel("Clean Run Dir"));
        createDBPanel.add(createDBCheckBox);
        jPanel.add(createDBPanel);

        jPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        final JPanel dataPanel = new JPanel(new BorderLayout(5, 0));
        dataPanel.add(new JLabel(MSGS.dataMessage()), BorderLayout.WEST);
        dataField = new JTextField();
        dataPanel.add(dataField, BorderLayout.CENTER);
        jPanel.add(dataPanel);

        jPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        return jPanel;
    }  // end method createEditor

    @Override protected void resetEditorFrom(@NotNull MMRunTaskConfiguration mmRunConfiguration) {
        myModuleSelector.reset(mmRunConfiguration);
        propertiesFile.setText(mmRunConfiguration.getPropertiesFile());
        dataField.setText(mmRunConfiguration.getData());
        myTaskSelector.setTask(mmRunConfiguration.getTaskId());
        createDBCheckBox.setSelected(mmRunConfiguration.cleanRunDir());
        noCluster.setSelected(mmRunConfiguration.isNoCluster());
        runDirField.setText(mmRunConfiguration.getRunDir());
        vmOptionsField.setText(mmRunConfiguration.getVmOptions());
    }
}  // end class MMRunTaskConfigurationEditor
