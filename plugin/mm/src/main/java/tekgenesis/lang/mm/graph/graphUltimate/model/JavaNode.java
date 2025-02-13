
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
import java.sql.Types;
import java.util.ArrayList;

import javax.swing.*;

import com.intellij.psi.*;
import com.intellij.ui.JBColor;
import com.intellij.util.PlatformIcons;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.field.FieldOptions;
import tekgenesis.field.ModelField;
import tekgenesis.type.Kind;
import tekgenesis.type.Type;

import static tekgenesis.common.Predefined.notNull;

/**
 * Java graph node.
 */
public class JavaNode implements MMGraphNode<PsiClass> {

    //~ Instance Fields ..............................................................................................................................

    private final PsiClass clazz;
    private boolean        showChildren;

    //~ Constructors .................................................................................................................................

    JavaNode(PsiClass clazz) {
        this.clazz   = clazz;
        showChildren = false;
    }

    //~ Methods ......................................................................................................................................

    @Override public Color getBackgroundColor() {
        return COLOR;
    }
    /** Gets full name of class referred. */
    @Nullable public String getBoundMMFullName() {
        return clazz.getQualifiedName();
    }

    @Override public Icon getChildIcon(ModelField modelField) {
        if (modelField instanceof JavaAttribute) return PlatformIcons.FIELD_ICON;
        return PlatformIcons.METHOD_ICON;
    }

    @Override public Seq<? extends ModelField> getChildren() {
        final ArrayList<ModelField> fields = new ArrayList<>();
        for (final PsiField psiField : clazz.getAllFields())
            fields.add(new JavaAttribute(psiField));
        for (final PsiMethod psiMethod : clazz.getMethods())
            fields.add(new JavaMethod(psiMethod));
        return Colls.seq(fields);
    }

    @Override public String getFullName() {
        return clazz.getQualifiedName() + ".java";
    }

    @Override public Icon getIcon() {
        return PlatformIcons.CLASS_ICON;
    }

    @Override public PsiClass getModel() {
        return clazz;
    }

    @Override public boolean isShowChildren() {
        return showChildren;
    }

    @Nullable @Override public String getName() {
        return clazz.getName();
    }

    @Override public PsiElement getPsi() {
        return clazz.getParent();
    }

    @Override public void setShowChildren(boolean showAttributes1) {
        showChildren = showAttributes1;
    }

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("MagicNumber")
    private static final Color COLOR = new JBColor(new Color(145, 145, 255), new Color(145, 145, 255));

    //~ Inner Classes ................................................................................................................................

    private static class JavaAttribute implements ModelField {
        private final PsiField psiField;

        private JavaAttribute(PsiField psiField) {
            this.psiField = psiField;
        }

        @Override public boolean hasChildren() {
            return false;
        }

        @NotNull @Override public ImmutableList<? extends ModelField> getChildren() {
            return Colls.emptyList();
        }

        @NotNull @Override public String getLabel() {
            return psiField.getNameIdentifier().toString();
        }

        @NotNull @Override public String getName() {
            return notNull(psiField.getName());
        }

        @NotNull @Override public FieldOptions getOptions() {
            return new FieldOptions();
        }

        @NotNull @Override public Type getType() {
            return new JavType(psiField.getType());
        }

        @Override public void setType(@NotNull Type type) {}
    }  // end class JavaAttribute

    private static class JavaMethod implements ModelField {
        private final PsiMethod psiMethod;

        private JavaMethod(@NotNull PsiMethod psiMethod) {
            this.psiMethod = psiMethod;
        }

        @Override public boolean hasChildren() {
            return false;
        }

        @NotNull @Override public ImmutableList<? extends ModelField> getChildren() {
            return Colls.emptyList();
        }

        @NotNull @Override public String getLabel() {
            return psiMethod.getNameIdentifier() != null ? psiMethod.getNameIdentifier().toString() : "";
        }

        @NotNull @Override public String getName() {
            return psiMethod.getName();
        }

        @NotNull @Override public FieldOptions getOptions() {
            return new FieldOptions();
        }

        @NotNull @Override public Type getType() {
            final PsiType returnType = psiMethod.getReturnType();
            return new JavType(returnType != null ? returnType : PsiType.VOID);
        }

        @Override public void setType(@NotNull Type type) {}
    }

    private static class JavType implements Type {
        private final PsiType type;

        public JavType(PsiType type) {
            this.type = type;
        }

        @Nullable @Override public Type applyParameters(Seq<String> strings) {
            return null;
        }

        @Override public String asString(Object obj) {
            return null;
        }

        @NotNull @Override public Type commonSuperType(@NotNull Type that) {
            return this;
        }

        @Override public boolean equivalent(Type t) {
            return false;
        }
        public String toString() {
            return type.getPresentableText();
        }

        @Nullable @Override public Object valueOf(String str) {
            return null;
        }

        @Override public boolean isUndefined() {
            return type == null;
        }

        @Override public boolean isVoid() {
            return false;
        }

        @Override public Object getDefaultValue() {
            return null;
        }

        @Override public boolean isComposite() {
            return false;
        }

        @Override public boolean isResource() {
            return false;
        }

        @Override public boolean isTime() {
            return false;
        }

        @Override public boolean isType() {
            return false;
        }

        @Nullable @Override public Type getFinalType() {
            return null;
        }

        @Override public boolean isString() {
            return false;
        }

        @Override public Class<?> getImplementationClass() {
            return null;
        }

        @Nullable @Override public String getImplementationClassName() {
            return null;
        }

        @NotNull @Override public Kind getKind() {
            return Kind.ANY;
        }

        @Override public boolean isHtml() {
            return false;
        }

        @Override public boolean isNull() {
            return false;
        }

        @Override public Option<Integer> getLength() {
            return Option.empty();
        }

        @Override public boolean isEnum() {
            return false;
        }

        @Override public boolean isBoolean() {
            return false;
        }

        @Override public int getParametersCount() {
            return 0;
        }

        @Override public boolean isInner() {
            return false;
        }

        @Override public boolean isNumber() {
            return false;
        }

        @NotNull @Override public String getSqlImplementationType(boolean multiple) {
            return "";
        }

        @Override public int getSqlType() {
            return Types.NULL;
        }

        @Override public boolean isDatabaseObject() {
            return false;
        }

        @Override public boolean isView() {
            return false;
        }

        @Override public boolean isAny() {
            return false;
        }

        @Override public boolean isArray() {
            return false;
        }

        @Override public boolean isEntity() {
            return false;
        }

        private static final long serialVersionUID = 3993137526771257956L;
    }  // end class JavType
}  // end class JavaNode
