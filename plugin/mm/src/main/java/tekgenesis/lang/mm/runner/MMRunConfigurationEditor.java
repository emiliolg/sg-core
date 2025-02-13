
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

import javax.swing.*;

import com.intellij.application.options.ModulesComboBox;
import com.intellij.execution.ui.ConfigurationModuleSelector;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;

import static tekgenesis.lang.mm.i18n.PluginMessages.MSGS;

/**
 * MM Configuration Editor.
 */
class MMRunConfigurationEditor extends AbstractMMRunConfigurationEditor<MMRunConfiguration> {

    //~ Instance Fields ..............................................................................................................................

    private JTextField argumentsField = null;

    private JCheckBox autoLoginCheckBox = null;

    private JCheckBox                         createDBCheckBox      = null;
    private JCheckBox                         enabledLifeCycle      = null;
    private JCheckBox                         enabledTaskManager    = null;
    private JCheckBox                         launchBrowserCheckBox = null;
    private final ModulesComboBox             modulesCombo;
    private final ConfigurationModuleSelector myModuleSelector;
    private JCheckBox                         noCluster             = null;
    private JPasswordField                    passTextField         = null;
    private JTextField                        portField             = null;
    private JTextField                        userTextField         = null;
    private JTextField                        vmOptionsField        = null;

    //~ Constructors .................................................................................................................................

    /** Constructor. Receives the Project */
    public MMRunConfigurationEditor(final Project project) {
        modulesCombo     = new ModulesComboBox();
        myModuleSelector = new ConfigurationModuleSelector(project, modulesCombo);
    }

    //~ Methods ......................................................................................................................................

    @Override protected void applyEditorTo(@NotNull MMRunConfiguration mmRunConfiguration)
        throws ConfigurationException
    {
        myModuleSelector.applyTo(mmRunConfiguration);
        mmRunConfiguration.setPort(portField.getText());
        mmRunConfiguration.setCleanRunDir(createDBCheckBox.isSelected());
        mmRunConfiguration.setEnableTaskExecution(enabledTaskManager.isSelected());
        mmRunConfiguration.setEnableLifeCycleExecution(enabledLifeCycle.isSelected());
        mmRunConfiguration.setNoCluster(noCluster.isSelected());
        mmRunConfiguration.setRunDir(runDirField.getText());
        mmRunConfiguration.setPropertiesFile(propertiesFile.getText());
        mmRunConfiguration.setVmOptions(vmOptionsField.getText());
        mmRunConfiguration.setProgramArguments(argumentsField.getText());
        mmRunConfiguration.setLaunchBrowser(launchBrowserCheckBox.isSelected());
        mmRunConfiguration.setAutoLogin(autoLoginCheckBox.isSelected());
        mmRunConfiguration.setAutologinUser(userTextField.getText());
        mmRunConfiguration.setAutologinPass(passTextField.getPassword());
    }

    @NotNull @Override
    @SuppressWarnings("OverlyLongMethod")
    protected JComponent createEditor() {
        portField             = new JTextField(5);
        createDBCheckBox      = new JCheckBox();
        enabledTaskManager    = new JCheckBox();
        enabledLifeCycle      = new JCheckBox();
        noCluster             = new JCheckBox();
        launchBrowserCheckBox = new JCheckBox();
        autoLoginCheckBox     = new JCheckBox();
        userTextField         = new JTextField(10);
        passTextField         = new JPasswordField(COLUMNS);

        autoLoginCheckBox.addChangeListener(e -> {
            userTextField.setEnabled(autoLoginCheckBox.isSelected());
            passTextField.setEnabled(autoLoginCheckBox.isSelected());
        });

        final Component runDirComponent    = createRunDirComponent();
        final Component propsFileComponent = createPropertiesFileComponent();

        final JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        final JPanel modulesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        modulesPanel.add(new JLabel(MSGS.moduleToRunMessage()));
        modulesPanel.add(modulesCombo);

        jPanel.add(modulesPanel);

        final JPanel portPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        portPanel.add(new JLabel("Port: "));
        portPanel.add(portField);
        jPanel.add(portPanel);

        final JPanel launchBrowserPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        launchBrowserPanel.add(new JLabel(MSGS.launchBrowserMessage()));
        launchBrowserPanel.add(launchBrowserCheckBox);
        jPanel.add(launchBrowserPanel);

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

        final JPanel argumentsPanel = new JPanel(new BorderLayout(5, 0));
        argumentsPanel.add(new JLabel(MSGS.argumentsMessage()), BorderLayout.WEST);
        argumentsField = new JTextField();
        argumentsPanel.add(argumentsField, BorderLayout.CENTER);
        jPanel.add(argumentsPanel);

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

        final JPanel lifeCycleServicePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        lifeCycleServicePanel.add(new JLabel("Lifecycle Service"));
        lifeCycleServicePanel.add(enabledLifeCycle);
        jPanel.add(lifeCycleServicePanel);

        final JPanel taskEnabledPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        taskEnabledPanel.add(new JLabel("Activate Task Service"));
        taskEnabledPanel.add(enabledTaskManager);
        jPanel.add(taskEnabledPanel);

        final JPanel noClusterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        noClusterPanel.add(new JLabel("Standalone mode"));
        noClusterPanel.add(noCluster);
        jPanel.add(noClusterPanel);

        final JPanel autoLoginPanel = new JPanel(new BorderLayout(5, 0));
        autoLoginPanel.setBorder(BorderFactory.createTitledBorder("Auto Login"));
        final JPanel autoLoginCheckPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        autoLoginCheckPanel.add(new JLabel("Enable Auto Login"));
        autoLoginCheckPanel.add(autoLoginCheckBox);

        final JPanel userPassPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        userPassPanel.add(new JLabel("User: "));
        userPassPanel.add(userTextField);
        userPassPanel.add(new JLabel("Password: "));
        userPassPanel.add(passTextField);

        autoLoginPanel.add(autoLoginCheckPanel, BorderLayout.NORTH);
        autoLoginPanel.add(userPassPanel, BorderLayout.CENTER);

        jPanel.add(autoLoginPanel);
        jPanel.validate();

        return jPanel;
    }  // end method createEditor

    @Override protected void disposeEditor() {}
    @Override protected void resetEditorFrom(@NotNull MMRunConfiguration mmRunConfiguration) {
        myModuleSelector.reset(mmRunConfiguration);
        portField.setText(mmRunConfiguration.getPort());
        propertiesFile.setText(mmRunConfiguration.getPropertiesFile());
        createDBCheckBox.setSelected(mmRunConfiguration.cleanRunDir());
        enabledTaskManager.setSelected(mmRunConfiguration.isEnableTaskExecution());
        enabledLifeCycle.setSelected(mmRunConfiguration.isEnableLifecycleExecution());
        noCluster.setSelected(mmRunConfiguration.isNoCluster());
        runDirField.setText(mmRunConfiguration.getRunDir());
        vmOptionsField.setText(mmRunConfiguration.getVmOptions());
        argumentsField.setText(mmRunConfiguration.getProgramArguments());
        launchBrowserCheckBox.setSelected(mmRunConfiguration.isLaunchBrowser());
        autoLoginCheckBox.setSelected(mmRunConfiguration.isAutoLogin());
        if (autoLoginCheckBox.isSelected()) {
            userTextField.setText(mmRunConfiguration.getAutologinUser());
            passTextField.setText(mmRunConfiguration.getAutologinPass());
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final int COLUMNS = 20;

    public static final String GENERATED_SOURCES_DIR = "Generated Sources Dir";
}  // end class MMRunConfigurationEditor
