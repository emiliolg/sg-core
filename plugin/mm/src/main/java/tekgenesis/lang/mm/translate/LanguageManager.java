
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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.memetix.mst.language.Language;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Manager for Languages used in Translation.
 */
@State(name     = "LanguageManager", storages = @Storage(id = "lang_manager", file = "$APP_CONFIG$/lang_manager.xml"))
public class LanguageManager implements PersistentStateComponent<LanguageManager>, ApplicationComponent {

    //~ Instance Fields ..............................................................................................................................

    private Language language = Language.ENGLISH;

    private List<Language> translations = new ArrayList<>();

    //~ Constructors .................................................................................................................................

    /** Manager for Languages used in Translation. */
    public LanguageManager() {
        translations.add(Language.SPANISH);
    }

    //~ Methods ......................................................................................................................................

    /** Consolidate values to ensure saving a valid state. */
    public LanguageManager consolidate() {
        translations.remove(language);
        return this;
    }

    @Override public void disposeComponent() {}

    @Override public void initComponent() {}

    @Override public void loadState(@Nullable LanguageManager state) {
        if (state != null) XmlSerializerUtil.copyBean(state, this);
    }

    @NotNull @Override public String getComponentName() {
        return "Language Manager";
    }

    /** Get base language. */
    public Language getLanguage() {
        return language;
    }

    /** Set base language. */
    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override public LanguageManager getState() {
        return this;
    }

    /** Get list of translations. */
    public List<Language> getTranslations() {
        return translations;
    }

    /** Set list of translations. */
    public void setTranslations(List<Language> ts) {
        translations = ts;
    }

    //~ Methods ......................................................................................................................................

    /** Get persisted instance of LanguageManager. */
    public static LanguageManager getInstance() {
        return ApplicationManager.getApplication().getComponent(LanguageManager.class);
    }
}  // end class LanguageManager
