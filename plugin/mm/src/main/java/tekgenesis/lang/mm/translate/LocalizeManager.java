
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.translate;

import java.util.ArrayList;
import java.util.List;

import com.intellij.openapi.project.Project;
import com.memetix.mst.language.Language;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Constants;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.type.MetaModel;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.lang.mm.translate.LanguageManager.getInstance;
import static tekgenesis.lang.mm.translate.TranslationItem.item;
import static tekgenesis.lang.mm.util.Utils.getMMSourcePath;

/**
 * Translation Manager.
 */
public class LocalizeManager {

    //~ Instance Fields ..............................................................................................................................

    private final boolean connected;

    @NotNull private final String  mmRootPath;
    @NotNull private final Project project;

    //~ Constructors .................................................................................................................................

    /** Translation Manager. */
    public LocalizeManager(@NotNull final MMFile dispatcher) {
        project    = dispatcher.getProject();
        mmRootPath = getMMSourcePath(dispatcher);
        connected  = KeyManager.getInstance().validKeys();
    }

    //~ Methods ......................................................................................................................................

    /** Localize given models. */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public void localize(@NotNull final List<MetaModel> translations) {
        if (isEmpty(mmRootPath)) return;

        final List<TranslationItem> items = new ArrayList<>();

        // Combine languages and models into TranslationItems
        for (final Language language : getLanguagesToTranslate()) {
            final Language origin = getInstance().getLanguage();
            for (final MetaModel model : translations)
                items.add(item(project, model, origin, language, mmRootPath));
        }

        // Execute translation task
        new InternationalizationTask("Translation", project, items, connected).executeWithProgress();
    }

    @NotNull private List<Language> getLanguagesToTranslate() {
        return getInstance().consolidate().getTranslations();
    }

    //~ Static Fields ................................................................................................................................

    static final String PROPERTY_EXTENSION = Constants.PROPERTIES_EXT;
}  // end class LocalizeManager
