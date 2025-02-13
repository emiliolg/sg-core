
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.facet;

import com.intellij.facet.FacetConfiguration;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.facet.ui.FacetValidatorsManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;

import org.jdom.Element;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.compiler.model.MetaModelFacetSettingsState;

/**
 * MM FacetConfiguration.
 */
public class MMFacetConfiguration implements FacetConfiguration, PersistentStateComponent<MetaModelFacetSettingsState> {

    //~ Instance Fields ..............................................................................................................................

    private MetaModelFacetSettingsState state = new MetaModelFacetSettingsState();

    //~ Methods ......................................................................................................................................

    @Override public FacetEditorTab[] createEditorTabs(FacetEditorContext facetEditorContext, FacetValidatorsManager facetValidatorsManager) {
        return new FacetEditorTab[] { new MMFacetEditorTab(this, facetEditorContext) };
    }

    @Override public void loadState(MetaModelFacetSettingsState s) {
        state = s;
    }

    @Deprecated @Override public void readExternal(Element element)
        throws InvalidDataException {}

    @Deprecated @Override public void writeExternal(Element element)
        throws WriteExternalException {}

    /** Returns generated sources dir. */
    public String getGenSourcesDir() {
        return state.generatedSourcesDir;
    }

    /** Sets generated sources dir. */
    public void setGenSourcesDir(String genSourcesDir) {
        state.generatedSourcesDir = genSourcesDir;
    }

    @Nullable @Override public MetaModelFacetSettingsState getState() {
        return state;
    }
}
