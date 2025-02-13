
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import javax.swing.*;

import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.MMFileType;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.type.MetaModelKind;

import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.common.core.QName.qualify;
import static tekgenesis.lang.mm.i18n.PluginMessages.MSGS;
import static tekgenesis.lang.mm.psi.PsiUtils.*;
import static tekgenesis.lang.mm.psi.PsiUtils.getPsiClassForFqnNullable;
import static tekgenesis.mmcompiler.ast.MMToken.CACHE;

/**
 * Entity Psi.
 */
public class PsiEntity extends PsiDatabaseObject<Entity> {

    //~ Constructors .................................................................................................................................

    /** Creates an Identifier. */
    PsiEntity(MMElementType t) {
        super(t, MetaModelKind.ENTITY, Entity.class);
    }

    //~ Methods ......................................................................................................................................

    @Override public void annotate(AnnotationHolder holder) {
        annotateCache(holder);
        super.annotate(holder);
    }

    @Override public Icon getIcon(int flags) {
        return MMFileType.ENTITY_ICON;
    }

    private void annotateCache(AnnotationHolder holder) {
        for (final PsiElement child : getChildren()) {
            if (child instanceof MMCommonComposite && ((MMCommonComposite) child).getType() == CACHE)
                checkInnerClassError(holder, (MMCommonComposite) child, DATA_CLASS_NAME, OPEN_DATA_CLASS_NAME, true);
        }
    }

    private void checkInnerClassError(AnnotationHolder holder, ASTNode node, String innerClassName, String superClassName, boolean withSerialUID) {
        if (getPsiClass(getClassFqnInEntity(innerClassName)) == null) {
            final PsiClass entityClass = resolveJavaClass(getProject(), getFullName());
            if (entityClass != null) {
                final Annotation annotation = holder.createErrorAnnotation(node, MSGS.missingDataClass());
                annotation.registerFix(
                    CreateMissingInnerClassIntention.create(entityClass, innerClassName, superClassName)
                                                    .withSerialUID(withSerialUID)
                                                    .asStatic()
                                                    .asFinal());
            }
        }
    }

    private String getClassFqnInEntity(@NotNull String innerClassName) {
        return qualify(getFullName(), innerClassName);
    }

    @Nullable private PsiClass getPsiClass(@NotNull String classFqn) {
        return getPsiClassForFqnNullable(getProject(), classFqn);
    }
}  // end class PsiEntity
