
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

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.ComponentWithBrowseButton;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.TextFieldWithStoredHistory;

/**
 * Abstract class for Run Configuration Editors.
 */
public abstract class AbstractMMRunConfigurationEditor<T> extends SettingsEditor<T> {

    //~ Instance Fields ..............................................................................................................................

    protected JTextField propertiesFile = null;
    protected JTextField runDirField    = null;

    //~ Methods ......................................................................................................................................

    protected Component createPropertiesFileComponent() {
        propertiesFile = new JTextField();
        propertiesFile.setPreferredSize(new Dimension(DIRFIELD_TEXT_WIDTH, DIRFIELD_HEIGHT));
        return new ComponentWithBrowseButton<>(propertiesFile,
            e -> {
                final FileChooserDescriptor fileChooserDescriptor = FileChooserDescriptorFactory.createSingleLocalFileDescriptor();
                fileChooserDescriptor.setTitle("Choose Properties File");
                // final VirtualFile file = FileChooser.chooseFile(
                // propertiesFile,
                // fileChooserDescriptor,
                // null);
                final VirtualFile file = FileChooser.chooseFile(fileChooserDescriptor, null, null);
                if (file != null) propertiesFile.setText(file.getPath());
            });
    }

    protected Component createRunDirComponent() {
        runDirField = new JTextField();
        runDirField.setPreferredSize(new Dimension(DIRFIELD_TEXT_WIDTH, DIRFIELD_HEIGHT));
        return new ComponentWithBrowseButton<>(runDirField,
            e -> {
                final FileChooserDescriptor fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
                fileChooserDescriptor.setTitle("Choose Run Directory");
                final VirtualFile file = FileChooser.chooseFile(fileChooserDescriptor, null, null);
                if (file != null) runDirField.setText(file.getPath());
            });
    }

    //~ Static Fields ................................................................................................................................

    private static final int DIRFIELD_HEIGHT = 25;

    private static final int DIRFIELD_TEXT_WIDTH = 300;

    //~ Inner Classes ................................................................................................................................

    /**
     * Component to choose the SuiGen installation Dir.
     */
    public static class DirComponent extends JPanel {
        private TextFieldWithStoredHistory dirTextField      = null;
        private TextFieldWithStoredHistory genSourceDirField = null;

        /** Constructor. Receives the Layout Manager */
        public DirComponent(LayoutManager layout) {
            super(layout);
        }

        /** Returns the Installation Dir. */
        public String getFile() {
            return dirTextField.getText();
        }

        /** Sets the Installation Dir. */
        public void setFile(String dir) {
            dirTextField.setText(dir);
            dirTextField.addCurrentTextToHistory();
        }

        /** Get source direction. */
        public String getGenSourceDir() {
            return genSourceDirField != null ? genSourceDirField.getText() : null;
        }

        /** Set method for source direction field. */
        public void setGenSourceDirField(TextFieldWithStoredHistory genSourceDirField) {
            this.genSourceDirField = genSourceDirField;
        }

        void setDirTextField(TextFieldWithStoredHistory textField) {
            dirTextField = textField;
        }

        private static final long serialVersionUID = 3051321109434543159L;
    }
}  // end class AbstractMMRunConfigurationEditor
