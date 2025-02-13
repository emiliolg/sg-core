
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.runner.ui;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.ui.ComponentWithBrowseButton;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.TextFieldWithStoredHistory;

import org.jetbrains.annotations.Nullable;

/**
 * MMFacet Tab.
 */
public class MMFacetTabForm {

    //~ Instance Fields ..............................................................................................................................

    private TextFieldWithStoredHistory generatedOutputField;
    private JPanel                     mainPanel;
    @Nullable private final Module     module;

    //~ Constructors .................................................................................................................................

    /** Creates a MMFacetTabForm. */
    public MMFacetTabForm(@Nullable Module module) {
        this.module = module;
        createUIComponents();
    }

    //~ Methods ......................................................................................................................................

    /** Returns generated output directory. */
    public String getGenOutputDir() {
        return generatedOutputField.getText();
    }
    /** Sets the Installation Dir. */
    public void setGenSourcesDir(String dir) {
        generatedOutputField.setText(dir);
        generatedOutputField.addCurrentTextToHistory();
    }

    /** Returns main panel. */
    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void createUIComponents() {
        mainPanel = new JPanel(new BorderLayout());
        final JPanel otherSettings = new JPanel();
        otherSettings.setLayout(new BoxLayout(otherSettings, BoxLayout.Y_AXIS));

        final String modulePropertyId = "suigen-generated-sources-dir-" + (module != null ? module.getName() : "");
        generatedOutputField = new TextFieldWithStoredHistory(modulePropertyId, true);

        if (module != null) {
            final File parent = new File(module.getModuleFilePath()).getParentFile();
            // noinspection DuplicateStringLiteralInspection
            generatedOutputField.setText(parent.getAbsolutePath() + File.separatorChar + "src_generated" + File.separatorChar + "main");

            final ComponentWithBrowseButton<TextFieldWithStoredHistory> generatedOutputBrowse = new ComponentWithBrowseButton<>(generatedOutputField,
                    e -> {
                        final FileChooserDescriptor fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
                        fileChooserDescriptor.setTitle("Choose Generated Output Dir");
                        final VirtualFile file = FileChooser.chooseFile(fileChooserDescriptor, null, null);
                        if (file != null) generatedOutputField.setText(file.getPath());
                    });
            final JPanel       genOutPanel     = new JPanel(new BorderLayout());
            final TitledBorder genTitledBorder = BorderFactory.createTitledBorder("Sui Generis Generated Sources Directory");
            genTitledBorder.setTitleJustification(TitledBorder.LEFT);

            genOutPanel.setBorder(genTitledBorder);
            genOutPanel.add(generatedOutputBrowse, BorderLayout.CENTER);
            otherSettings.add(genOutPanel);
        }

        mainPanel.add(otherSettings, BorderLayout.NORTH);
    }  // end method createUIComponents

    //~ Static Fields ................................................................................................................................

    static final String SUI_GENERIS_INSTALLATION_DIR = "Sui Generis Installation Directory";
}  // end class MMFacetTabForm
