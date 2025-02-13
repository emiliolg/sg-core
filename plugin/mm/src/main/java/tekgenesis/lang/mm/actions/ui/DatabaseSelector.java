
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.actions.ui;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComponentWithBrowseButton;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.database.DatabaseType;
import tekgenesis.lang.mm.FileUtils;
import tekgenesis.lang.mm.ProjectUtils;

/**
 * Dialog for Selecting Database in Generate Sql Action.
 */
public class DatabaseSelector extends JDialog {

    //~ Instance Fields ..............................................................................................................................

    private JButton                               buttonCancel;
    private JButton                               buttonOK;
    private ComponentWithBrowseButton<JTextField> componentWithBrowseButton1;
    private JPanel                                contentPane;
    private JComboBox<DatabaseType>               databaseCombo;
    private boolean                               isCancel;
    private JComboBox<String>                     moduleComboBox;

    private Project      project    = null;
    private DatabaseType response   = DatabaseType.values()[0];
    private JTextField   textField1;

    //~ Constructors .................................................................................................................................

    /** Dialog for Selecting Database in Generate Sql Action. */
    public DatabaseSelector(final Project project) {
        this.project = project;
        $$$setupUI$$$();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        isCancel = false;
        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
        databaseCombo.setModel(new DefaultComboBoxModel<>(DatabaseType.values()));
        final ImmutableList<String> modules = ProjectUtils.getAllModulesNames(project).filter(s -> {
                    final Module      module        = ProjectUtils.findModuleByName(project, s);
                    final VirtualFile resourcesRoot = FileUtils.getResourcesRoot(module);
                    return resourcesRoot != null;
                }).toList();
        moduleComboBox.setModel(new DefaultComboBoxModel<>(modules.toArray(new String[modules.size()])));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    onCancel();
                }
            });
        moduleComboBox.addActionListener(new UpdateTextAction(textField1));
        databaseCombo.addActionListener(new UpdateTextAction(textField1));
        final Module      module        = ProjectUtils.findModuleByName(project, getSelectedModule());
        final VirtualFile resourcesRoot = FileUtils.getResourcesRoot(module);
        if (resourcesRoot != null) textField1.setText(resourcesRoot.getPath() + "/" + getSelectedDBType().toLowerCase());

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }  // end ctor DatabaseSelector

    //~ Methods ......................................................................................................................................

    /** @noinspection  ALL */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    /** is true if dialog was canceled. */
    public boolean isCancel() {
        return isCancel;
    }

    /** gets Database Selected. */
    public String getOutputDir() {
        return textField1.getText();
    }

    /** gets Database Selected. */
    public DatabaseType getResponse() {
        return response;
    }

    /** get Selected Module. */
    public String getSelectedModule() {
        return moduleComboBox.getSelectedItem().toString();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR
     * call it in your code!
     *
     * @noinspection  ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(4, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1,
            new GridConstraints(3,
                0,
                1,
                1,
                GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                1,
                null,
                null,
                null,
                0,
                false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1,
            new GridConstraints(0,
                0,
                1,
                1,
                GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_WANT_GROW,
                1,
                null,
                null,
                null,
                0,
                false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2,
            new GridConstraints(0,
                1,
                1,
                1,
                GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null,
                null,
                null,
                0,
                false));
        buttonOK = new JButton();
        buttonOK.setText("OK");
        panel2.add(buttonOK,
            new GridConstraints(0,
                0,
                1,
                1,
                GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_FIXED,
                null,
                null,
                null,
                0,
                false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel2.add(buttonCancel,
            new GridConstraints(0,
                1,
                1,
                1,
                GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_FIXED,
                null,
                null,
                null,
                0,
                false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3,
            new GridConstraints(1,
                0,
                1,
                1,
                GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null,
                null,
                null,
                0,
                false));
        panel3.setBorder(
            BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Select Database",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                null,
                UIManager.getColor("Label.foreground")));
        databaseCombo = new JComboBox<DatabaseType>();
        panel3.add(databaseCombo,
            new GridConstraints(0,
                0,
                1,
                3,
                GridConstraints.ANCHOR_WEST,
                GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_FIXED,
                null,
                null,
                null,
                0,
                false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2,
            new GridConstraints(1,
                0,
                1,
                3,
                GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_VERTICAL,
                1,
                GridConstraints.SIZEPOLICY_WANT_GROW,
                null,
                null,
                null,
                0,
                false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel4,
            new GridConstraints(0,
                0,
                1,
                1,
                GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null,
                null,
                null,
                0,
                false));
        panel4.setBorder(
            BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Select Module",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                null,
                UIManager.getColor("List.foreground")));
        moduleComboBox = new JComboBox<String>();
        panel4.add(moduleComboBox,
            new GridConstraints(0,
                0,
                1,
                1,
                GridConstraints.ANCHOR_WEST,
                GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_FIXED,
                null,
                null,
                null,
                0,
                false));
        final Spacer spacer3 = new Spacer();
        panel4.add(spacer3,
            new GridConstraints(1,
                0,
                1,
                1,
                GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_VERTICAL,
                1,
                GridConstraints.SIZEPOLICY_WANT_GROW,
                null,
                null,
                null,
                0,
                false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new BorderLayout(0, 0));
        contentPane.add(panel5,
            new GridConstraints(2,
                0,
                1,
                1,
                GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null,
                null,
                null,
                0,
                false));
        panel5.setBorder(
            BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Select Output Dir",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                null,
                UIManager.getColor("Label.foreground")));
        textField1.setHorizontalAlignment(0);
        panel5.add(textField1, BorderLayout.CENTER);
        panel5.add(componentWithBrowseButton1, BorderLayout.EAST);
    }  // end method $$$setupUI$$$

    private void createUIComponents() {
        textField1 = new JTextField();
        textField1.setHorizontalAlignment(0);
        componentWithBrowseButton1 = new ComponentWithBrowseButton<>(textField1,
                e -> {
                    final FileChooserDescriptor fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
                    fileChooserDescriptor.setTitle(SELECT_OUTPUT_DIR);
                    final Module      module        = ProjectUtils.findModuleByName(project, getSelectedModule());
                    final VirtualFile resourcesRoot = FileUtils.getResourcesRoot(module);
                    final VirtualFile file          = FileChooser.chooseFile(fileChooserDescriptor, null, resourcesRoot);
                    if (file != null) textField1.setText(file.getPath());
                });
    }

    private void onCancel() {
        isCancel = true;
        dispose();
    }

    private void onOK() {
        response = (DatabaseType) databaseCombo.getSelectedItem();
        dispose();
    }

    /** get Selected DBType. */
    private String getSelectedDBType() {
        return databaseCombo.getSelectedItem().toString();
    }

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String SELECT_OUTPUT_DIR = "Select Output Dir";

    private static final long serialVersionUID = 7356986717617195399L;

    //~ Inner Classes ................................................................................................................................

    private class UpdateTextAction implements ActionListener {
        private final JTextField textField;

        public UpdateTextAction(JTextField textField) {
            this.textField = textField;
        }

        @Override public void actionPerformed(ActionEvent e) {
            final Module      module        = ProjectUtils.findModuleByName(project, getSelectedModule());
            final VirtualFile resourcesRoot = FileUtils.getResourcesRoot(module);
            if (resourcesRoot != null) textField.setText(resourcesRoot.getPath() + "/" + getSelectedDBType().toLowerCase());
        }
    }
}  // end class DatabaseSelector
