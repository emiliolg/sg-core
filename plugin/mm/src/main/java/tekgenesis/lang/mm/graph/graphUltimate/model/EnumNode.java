
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
import tekgenesis.type.EnumType;

/**
 * Node for enum Relation Graph Ultimate.
 */
@SuppressWarnings("MagicNumber")
public class EnumNode implements MMGraphNode<EnumType> {

    //~ Instance Fields ..............................................................................................................................

    private final EnumType   enumModel;
    private final PsiElement enumPsi;
    private boolean          showChildren;

    //~ Constructors .................................................................................................................................

    EnumNode(EnumType enumModel, @Nullable PsiElement enumPsi) {
        this.enumModel = enumModel;
        this.enumPsi   = enumPsi;
        showChildren   = false;
    }

    //~ Methods ......................................................................................................................................

    public boolean equals(Object obj) {
        return obj instanceof EnumNode && getModel().equals(((EnumNode) obj).getModel());
    }

    public int hashCode() {
        return enumModel.hashCode();
    }

    @Override public Color getBackgroundColor() {
        return COLOR;
    }

    @Override public Icon getChildIcon(ModelField modelField) {
        return MMFileType.ENUM_VALUE_ICON;
    }

    @Override public Seq<? extends ModelField> getChildren() {
        return enumModel.getValues();
    }

    /** Gets Nodes's FullName. */
    public String getFullName() {
        return enumModel.getFullName();
    }
    /** Gets Node's Icon. */
    public Icon getIcon() {
        return MMFileType.ENUM_ICON;
    }

    /** Gets Enum Model. */
    @Override public EnumType getModel() {
        return enumModel;
    }

    /** Gets if children are showing for node. */
    public boolean isShowChildren() {
        return showChildren;
    }
    /** Gets Nodes's Name. */
    public String getName() {
        return enumModel.getName();
    }

    /** Gets Enum Psi. */
    public PsiElement getPsi() {
        return enumPsi;
    }

    /** Sets if children should be showed. */
    public void setShowChildren(boolean showChildren) {
        this.showChildren = showChildren;
    }

    //~ Static Fields ................................................................................................................................

    private static final Color COLOR = new JBColor(new Color(255, 184, 112), new Color(255, 184, 112));
}  // end class EnumNode
