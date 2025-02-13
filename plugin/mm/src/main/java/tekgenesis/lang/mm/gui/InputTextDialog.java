
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.gui;

import java.awt.*;

import javax.swing.*;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.util.ui.JBUI;

import org.jetbrains.annotations.Nullable;

/**
 * A dialog with a simple input text.
 */
public class InputTextDialog extends DialogWrapper {

    //~ Instance Fields ..............................................................................................................................

    private JTextField myField = null;
    private String     myLabel = "";

    //~ Constructors .................................................................................................................................

    protected InputTextDialog(@Nullable final Project project, final String title, final String myLabel) {
        super(project);
        this.myLabel = myLabel;
        setTitle(title);
        init();
    }

    //~ Methods ......................................................................................................................................

    @Nullable @Override public JComponent getPreferredFocusedComponent() {
        return myField;
    }

    /** Get the text in the field. */
    public String getText() {
        return myField.getText();
    }

    @Nullable @Override protected JComponent createCenterPanel() {
        return null;
    }

    @Nullable @Override protected JComponent createNorthPanel() {
        class MyTextField extends JTextField {
            private static final long serialVersionUID = -4010246997240461998L;

            public MyTextField() {
                super("");
            }

            public Dimension getPreferredSize() {
                final Dimension d = super.getPreferredSize();
                // noinspection MagicNumber
                return new Dimension(200, d.height);
            }
        }

        final JPanel             panel         = new JPanel(new GridBagLayout());
        final GridBagConstraints gbConstraints = new GridBagConstraints();

        gbConstraints.insets  = JBUI.insets(4, 0, 8, 8);
        gbConstraints.fill    = GridBagConstraints.VERTICAL;
        gbConstraints.weightx = 0;
        gbConstraints.weighty = 1;
        gbConstraints.anchor  = GridBagConstraints.EAST;
        final JLabel label = new JLabel(myLabel);
        panel.add(label, gbConstraints);

        gbConstraints.fill    = GridBagConstraints.BOTH;
        gbConstraints.weightx = 1;
        myField               = new MyTextField();
        panel.add(myField, gbConstraints);

        return panel;
    }
}  // end class InputTextDialog
