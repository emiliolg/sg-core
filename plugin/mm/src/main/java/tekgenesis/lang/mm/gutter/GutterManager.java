
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.gutter;

import javax.swing.*;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiClass;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.lang.mm.psi.PsiMetaModel;
import tekgenesis.lang.mm.psi.PsiUtils;
import tekgenesis.type.MetaModel;

import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.lang.mm.psi.PsiUtils.*;

/**
 * Manager for gutter used in plugin.
 */
public class GutterManager {

    //~ Constructors .................................................................................................................................

    private GutterManager() {}

    //~ Methods ......................................................................................................................................

    /** Gets MMPsi for the specified Java Class. */
    public static Option<PsiMetaModel<?>> getMM(PsiClass clazz) {
        if (clazz.getQualifiedName() == null) return Option.empty();
        return getMM(clazz, clazz.getQualifiedName());
    }

    /** Gets PsiClass for a Composite. */
    @Nullable public static PsiClass getPsiClass(PsiMetaModel<?> psiModel) {
        final PsiClass c = getPsiClassForFqnNullable(psiModel.getProject(), psiModel.getFullName());
        if (c != null) return c;

        final Option<? extends MetaModel> modelOption = psiModel.getModel();
        if (!modelOption.isPresent()) return null;

        final MetaModel model = modelOption.get();

        final PsiClass clazz = getPsiClassForComposite(psiModel, model);
        if (clazz != null) return clazz;

        final Module module = PsiUtils.getModule(psiModel);
        if (module == null) return null;

        final PsiClass[] psiClassesForDirectory = getPsiClassesForDirectory(module, model.getDomain());
        if (psiClassesForDirectory == null) return null;
        for (final PsiClass clazzy : psiClassesForDirectory) {
            if (model.getFullName().equals(clazzy.getQualifiedName())) return clazzy;
        }
        return null;
    }

    /** Gets MMPsi for the specified Java Class. */
    static Option<PsiMetaModel<?>> getMM(PsiClass clazz, String qualifiedName) {
        final Module module = getModule(clazz);
        if (module == null) return Option.empty();

        return getModelRepository(clazz).flatMap(r -> findPsiMetaModel(clazz.getProject(), module, r, createQName(qualifiedName)));
    }

    //~ Static Fields ................................................................................................................................

    static final String        IMPLEMENTS     = "Implements ";
    public static final String IMPLEMENTED_IN = "Implemented in ";

    static final Icon        IMPLEMENTING_ICON = AllIcons.Gutter.ImplementingMethod;
    public static final Icon IMPLEMENTED_ICON  = AllIcons.Gutter.ImplementedMethod;
}  // end class GutterManager
