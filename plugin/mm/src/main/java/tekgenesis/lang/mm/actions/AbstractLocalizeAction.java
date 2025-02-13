
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.actions;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.PsiMetaModel;
import tekgenesis.lang.mm.translate.LocalizeManager;
import tekgenesis.type.MetaModel;
import tekgenesis.type.MetaModelKind;

import static tekgenesis.type.MetaModelKind.ENUM;
import static tekgenesis.type.MetaModelKind.FORM;
import static tekgenesis.type.MetaModelKind.LINK;
import static tekgenesis.type.MetaModelKind.MENU;
import static tekgenesis.type.MetaModelKind.WIDGET;

/**
 * AbstractLocalizeAction.
 */
abstract class AbstractLocalizeAction extends MMActionTree {

    //~ Instance Fields ..............................................................................................................................

    private final List<MetaModel> translations;

    //~ Constructors .................................................................................................................................

    @SuppressWarnings("WeakerAccess")
    protected AbstractLocalizeAction() {
        translations = new ArrayList<>();
    }

    //~ Methods ......................................................................................................................................

    // not global localize actions should override this method
    @Override protected boolean hasValidContext() {
        return true;
    }

    @Override protected void setupFromEditor(Project p, Editor e) {}

    @Override protected void setupFromTree(DataContext context) {}

    /** Append all translatable meta models in file. */
    void internationalize(@NotNull final MMFile mmFile) {
        translations.clear();
        for (final PsiMetaModel<? extends MetaModel> psiModel : mmFile.getMetaModels()) {
            final MetaModel mm = psiModel.getModelOrNull();
            if (mm != null && canBeTranslated(mm)) translations.add(mm);
        }
        new LocalizeManager(mmFile).localize(translations);
    }

    /** Translate the specified mm. */
    void internationalize(@NotNull final MMFile mmFile, Seq<MetaModel> mms) {
        translations.clear();
        for (final MetaModel mm : mms) {
            if (canBeTranslated(mm)) translations.add(mm);
        }
        new LocalizeManager(mmFile).localize(translations);
    }

    private boolean canBeTranslated(final MetaModel mm) {
        return CAN_BE_TRANSLATED.contains(mm.getMetaModelKind());
    }

    //~ Static Fields ................................................................................................................................

    private static final EnumSet<MetaModelKind> CAN_BE_TRANSLATED = EnumSet.of(FORM, WIDGET, ENUM, MENU, LINK);
}  // end class AbstractLocalizeAction
