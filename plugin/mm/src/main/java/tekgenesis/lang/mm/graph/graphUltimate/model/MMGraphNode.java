
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

import tekgenesis.common.collections.Seq;
import tekgenesis.field.ModelField;

/**
 * MMGraphNode interface.
 */
public interface MMGraphNode<T> {

    //~ Methods ......................................................................................................................................

    /** Gets desired BackGround Color For Node. */
    Color getBackgroundColor();
    /** gets Icon for nodes Children. */
    Icon getChildIcon(ModelField modelField);
    /** Gets children. */
    Seq<? extends ModelField> getChildren();
    /** Gets Full Name. */
    String getFullName();

    /** Gets Node's Icon. */
    Icon getIcon();
    /** Gets Model. */
    T getModel();
    /** Gets if attributes are showing for node. */
    boolean isShowChildren();
    /** Gets Node Name. */
    String getName();
    /** Gets PsiElement. */
    PsiElement getPsi();
    /** Sets if attributes should be showed. */
    void setShowChildren(boolean showAttributes1);
}
