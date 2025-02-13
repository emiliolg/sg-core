
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.graph.graphUltimate.model;

import java.awt.*;

import javax.swing.*;

import com.intellij.psi.PsiElement;
import com.intellij.ui.JBColor;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.field.ModelField;
import tekgenesis.lang.mm.MMFileType;
import tekgenesis.metadata.form.widget.Form;

/**
 * Node used to visualize a form in Graph.
 */
public class FormNode implements MMGraphNode<Form> {

    //~ Instance Fields ..............................................................................................................................

    private final Form       form;
    private final PsiElement formPsi;
    private boolean          showChildren;

    //~ Constructors .................................................................................................................................

    /** Node used to visualize a form in Graph. */
    public FormNode(Form form, @Nullable PsiElement formPsi) {
        this.form    = form;
        this.formPsi = formPsi;
    }

    //~ Methods ......................................................................................................................................

    @Override public Color getBackgroundColor() {
        return FORM_COLOR;
    }

    @Override public Icon getChildIcon(ModelField modelField) {
        final String widget = modelField.getName();
        for (final String other : form.getPrimaryKeyAsStrings()) {
            if (widget.equals(other)) return MMFileType.FORM_PRIMARY_KEY_ICON;
        }
        return MMFileType.WIDGET_ICON;
    }

    @Override public Seq<? extends ModelField> getChildren() {
        return form.getChildren();
    }

    @Override public String getFullName() {
        return form.getFullName();
    }

    @Override public Icon getIcon() {
        return MMFileType.FORM_ICON;
    }
    @Override public Form getModel() {
        return form;
    }

    @Override public boolean isShowChildren() {
        return showChildren;
    }

    @Override public String getName() {
        return form.getName();
    }

    @Override public PsiElement getPsi() {
        return formPsi;
    }

    @Override public void setShowChildren(boolean showAttributes1) {
        showChildren = showAttributes1;
    }

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("MagicNumber")
    private static final Color FORM_COLOR = new JBColor(new Color(50, 200, 100), new Color(50, 200, 100));
}  // end class FormNode
