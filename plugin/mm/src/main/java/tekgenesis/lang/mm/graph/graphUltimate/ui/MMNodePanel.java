
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.graph.graphUltimate.ui;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import com.intellij.ui.JBColor;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.field.ModelField;
import tekgenesis.lang.mm.graph.graphUltimate.model.MMGraphNode;

class MMNodePanel extends JPanel {

    //~ Instance Fields ..............................................................................................................................

    private JPanel               attributePanel = null;
    private JLabel               label          = null;
    private final FontMetrics    metrics;
    private final MMGraphNode<?> node;

    //~ Constructors .................................................................................................................................

    public MMNodePanel(MMGraphNode<?> node) {
        super(new BorderLayout());
        this.node = node;
        metrics   = getFontMetrics(getFont());
        setupAttributePanel();
        setupJLabel();
        add(label, BorderLayout.NORTH);
        if (node.isShowChildren()) {
            add(attributePanel, BorderLayout.CENTER);
            setPreferredSize(
                new Dimension((int) Math.max(label.getPreferredSize().getWidth(), attributePanel.getPreferredSize().getWidth()),
                    (int) (label.getPreferredSize().getHeight() + attributePanel.getPreferredSize().getHeight())));
        }
        else setPreferredSize(new Dimension((int) label.getPreferredSize().getWidth(), (int) (label.getPreferredSize().getHeight())));
    }

    //~ Methods ......................................................................................................................................

    private void setupAttributePanel() {
        if (attributePanel == null) {
            final Seq<? extends ModelField> children = node.getChildren();
            attributePanel = new JPanel(new GridLayout(children.size(), 1));
            final ArrayList<JLabel> labels = new ArrayList<>();
            for (final ModelField modelField : children) {
                final String[] typeParts = modelField.getType().toString().split("\\.");
                final JLabel   jLabel    = new JLabel(modelField.getName() + " (" + typeParts[typeParts.length - 1] + ")",
                        node.getChildIcon(modelField),
                        JLabel.LEFT);
                attributePanel.add(jLabel);
                labels.add(jLabel);
            }
            attributePanel.setBorder(BorderFactory.createLineBorder(JBColor.WHITE));
            attributePanel.setPreferredSize(getListDimension(Colls.seq(labels)));
        }
    }

    private void setupJLabel() {
        if (label == null) {
            label = new JLabel(node.getName(), node.getIcon(), JLabel.CENTER);
            label.setForeground(JBColor.BLACK);
            label.setBackground(node.getBackgroundColor());
            label.setOpaque(true);
            label.setPreferredSize(getLabelDimension());
        }
    }

    private Dimension getLabelDimension() {
        final Icon   icon = label.getIcon();
        final String text = label.getText();
        return new Dimension(metrics.stringWidth(text) + icon.getIconWidth() + ICON_ERROR_MARGIN, Math.max(MAIN_LABEL_HEIGHT, icon.getIconHeight()));
    }

    @SuppressWarnings("rawtypes")
    private Dimension getListDimension(Seq labels) {
        int maxWidth = 0;

        for (final Object s : labels) {
            final int width;
            if (s instanceof JLabel) {
                final JLabel jLabel = (JLabel) s;
                width = metrics.stringWidth(jLabel.getText()) + jLabel.getIcon().getIconWidth() + ICON_ERROR_MARGIN;
            }
            else width = metrics.stringWidth(s.toString());

            if (width > maxWidth) maxWidth = width;
        }
        final int height;

        final Object first = labels.getFirst().getOrNull();
        if (first instanceof JLabel)
            height = labels.size() * (Math.max((metrics.getMaxAscent()), ((JLabel) first).getIcon().getIconHeight()) + SPACE_BETWEEN_ATTR);
        else height = labels.size() * (metrics.getMaxAscent() + SPACE_BETWEEN_ATTR);

        return new Dimension(maxWidth, height);
    }

    //~ Static Fields ................................................................................................................................

    private static final int MAIN_LABEL_HEIGHT = 30;

    private static final long serialVersionUID = -8346101780893997009L;

    private static final int SPACE_BETWEEN_ATTR = 5;
    private static final int ICON_ERROR_MARGIN  = 10;
}  // end class MMNodePanel
