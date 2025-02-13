
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
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.View;

import static tekgenesis.common.Predefined.cast;

/**
 * Node for view Relation Graph Ultimate.
 */
@SuppressWarnings("MagicNumber")
public class ViewNode implements MMGraphNode<View> {

    //~ Instance Fields ..............................................................................................................................

    private boolean showChildren;

    private final View       view;
    private final PsiElement viewPsi;

    //~ Constructors .................................................................................................................................

    ViewNode(View view, @Nullable PsiElement viewPsi) {
        this.view    = view;
        this.viewPsi = viewPsi;
        showChildren = false;
    }

    //~ Methods ......................................................................................................................................

    public boolean equals(Object obj) {
        return obj instanceof ViewNode && getModel().equals(((ViewNode) obj).getModel());
    }

    public int hashCode() {
        return view.hashCode();
    }

    @Override public Color getBackgroundColor() {
        return VIEW_COLOR;
    }

    @Override public Icon getChildIcon(ModelField modelField) {
        final Attribute attribute = cast(modelField);
        for (final Attribute other : view.getPrimaryKey()) {
            if (attribute.equals(other)) return MMFileType.PRIMARY_KEY_ICON;
        }
        for (final ModelField other : view.describes()) {
            if (attribute.equals(other)) return MMFileType.DESCRIBED_BY_ICON;
        }
        return MMFileType.ATTRIBUTE_ICON;
    }

    @Override public Seq<? extends ModelField> getChildren() {
        return view.getChildren();
    }

    /** Gets Nodes's FullName. */
    public String getFullName() {
        return view.getFullName();
    }

    /** Gets Node's Icon. */
    public Icon getIcon() {
        return MMFileType.VIEW_ICON;
    }

    /** Gets Entity Model. */
    public View getModel() {
        return view;
    }
    /** Gets if children are showing for node. */
    public boolean isShowChildren() {
        return showChildren;
    }

    /** Gets Nodes's Name. */
    public String getName() {
        return view.getName();
    }

    /** Gets Entity Psi. */
    public PsiElement getPsi() {
        return viewPsi;
    }
    /** Sets if children should be showed. */
    public void setShowChildren(boolean showChildren) {
        this.showChildren = showChildren;
    }

    //~ Static Fields ................................................................................................................................

    private static final Color VIEW_COLOR = new JBColor(new Color(153, 104, 255), new Color(153, 104, 255));
}  // end class ViewNode
