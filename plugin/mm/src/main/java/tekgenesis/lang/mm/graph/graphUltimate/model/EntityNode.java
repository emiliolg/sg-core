
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
import tekgenesis.metadata.entity.Entity;

import static tekgenesis.common.Predefined.cast;

/**
 * Node for entity Relation Graph Ultimate.
 */
@SuppressWarnings("MagicNumber")
public class EntityNode implements MMGraphNode<Entity> {

    //~ Instance Fields ..............................................................................................................................

    private final Entity     entity;
    private final PsiElement entityPsi;
    private boolean          showChildren;

    //~ Constructors .................................................................................................................................

    EntityNode(Entity entity, @Nullable PsiElement entityPsi) {
        this.entity    = entity;
        this.entityPsi = entityPsi;
        showChildren   = false;
    }

    //~ Methods ......................................................................................................................................

    public boolean equals(Object obj) {
        return obj instanceof EntityNode && getModel().equals(((EntityNode) obj).getModel());
    }

    public int hashCode() {
        return entity.hashCode();
    }

    @Override public Color getBackgroundColor() {
        return entity.isInner() ? INNER_ENTITY_COLOR : ENTITY_COLOR;
    }

    @Override public Icon getChildIcon(ModelField modelField) {
        final Attribute attribute = cast(modelField);
        for (final Attribute other : entity.getPrimaryKey()) {
            if (attribute.equals(other)) return MMFileType.PRIMARY_KEY_ICON;
        }
        for (final ModelField other : entity.describes()) {
            if (attribute.equals(other)) return MMFileType.DESCRIBED_BY_ICON;
        }
        return MMFileType.ATTRIBUTE_ICON;
    }

    @Override public Seq<? extends ModelField> getChildren() {
        return entity.getChildren();
    }

    /** Gets Node FullName. */
    public String getFullName() {
        return entity.getFullName();
    }

    /** Gets Node's Icon. */
    public Icon getIcon() {
        return MMFileType.ENTITY_ICON;
    }

    /** Gets Entity Model. */
    public Entity getModel() {
        return entity;
    }
    /** Gets if children are showing for node. */
    public boolean isShowChildren() {
        return showChildren;
    }

    /** Gets Node Name. */
    public String getName() {
        return entity.getName();
    }

    /** Gets Entity Psi. */
    @Nullable public PsiElement getPsi() {
        return entityPsi;
    }
    /** Sets if children should be showed. */
    public void setShowChildren(boolean showChildren) {
        this.showChildren = showChildren;
    }

    //~ Static Fields ................................................................................................................................

    private static final Color ENTITY_COLOR       = new JBColor(new Color(153, 204, 255), new Color(153, 204, 255));
    private static final Color INNER_ENTITY_COLOR = new JBColor(new Color(3, 204, 255), new Color(3, 204, 255));
}  // end class EntityNode
