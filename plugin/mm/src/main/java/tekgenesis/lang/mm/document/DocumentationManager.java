
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.document;

import java.util.ArrayList;
import java.util.List;

import com.intellij.openapi.project.Project;
import com.memetix.mst.language.Language;

import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.util.BackgroundTask;
import tekgenesis.type.MetaModel;

import static tekgenesis.lang.mm.translate.LanguageManager.getInstance;
import static tekgenesis.lang.mm.util.Utils.getMMSourcePath;

/**
 * Documentation generation manager.
 */
public class DocumentationManager {

    //~ Instance Fields ..............................................................................................................................

    private final String           mmRootPath;
    @NotNull private final Project project;

    //~ Constructors .................................................................................................................................

    /** Translation Manager. */
    public DocumentationManager(@NotNull final MMFile dispatcher) {
        project    = dispatcher.getProject();
        mmRootPath = getMMSourcePath(dispatcher);
    }

    //~ Methods ......................................................................................................................................

    /** Localize given models. */
    public void document(@NotNull final List<MetaModel> models) {
        final List<DocumentationItem> items = new ArrayList<>();

        // Combine languages and models into DocumentationItems
        final Language origin = getInstance().getLanguage();
        for (final MetaModel model : models) {
            // Default language
            items.add(new DocumentationItem(model, origin, mmRootPath));
            for (final Language language : getLanguagesToTranslate())
                items.add(new DocumentationItem(model, language, mmRootPath));
        }

        // Execute translation task
        new BackgroundTask("Document", project, items).executeWithProgress();
    }

    @NotNull private List<Language> getLanguagesToTranslate() {
        return getInstance().consolidate().getTranslations();
    }
}
