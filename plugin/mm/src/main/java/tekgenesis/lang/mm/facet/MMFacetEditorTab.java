
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.facet;

import java.io.File;

import javax.swing.*;

import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.projectRoot.ModuleStructureConfigurable;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import tekgenesis.lang.mm.runner.ui.MMFacetTabForm;

import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.lang.mm.MMPluginConstants.SUI_GEN_CONFIGURATION;

/**
 * MM Facet Editor.
 */
class MMFacetEditorTab extends FacetEditorTab {

    //~ Instance Fields ..............................................................................................................................

    private final MMFacetConfiguration configuration;
    private final FacetEditorContext   context;
    private MMFacetTabForm             mmFacetTab = null;

    //~ Constructors .................................................................................................................................

    /** Constructor. */
    public MMFacetEditorTab(MMFacetConfiguration mmFacetConfiguration, FacetEditorContext facetEditorContext) {
        configuration = mmFacetConfiguration;
        context       = facetEditorContext;
    }

    //~ Methods ......................................................................................................................................

    @Override public void apply()
        throws ConfigurationException
    {
        if (!isModified()) return;

        final String oldGeneratedSourcesPath = configuration.getGenSourcesDir();
        configuration.setGenSourcesDir(mmFacetTab.getGenOutputDir());
        ApplicationManager.getApplication().invokeLater(() ->
                ApplicationManager.getApplication().runWriteAction(() -> {
                    final ModuleStructureConfigurable instance            = ModuleStructureConfigurable.getInstance(context.getProject());
                    final ModifiableRootModel         modifiableRootModel = instance.getContext()
                                                                                    .getModulesConfigurator()
                                                                                    .getOrCreateModuleEditor(context.getModule())
                                                                                    .getModifiableRootModel();
                    MMFacet.refreshGeneratedOutputDir(oldGeneratedSourcesPath, modifiableRootModel);
                    modifiableRootModel.commit();
                }));
    }

    @NotNull @Override public JComponent createComponent() {
        mmFacetTab = new MMFacetTabForm(context.getModule());
        return mmFacetTab.getMainPanel();
    }

    @Override public void disposeUIResources() {}

    @Override public void reset() {
        final File parent = new File(context.getModule().getModuleFilePath()).getParentFile();

        if (isEmpty(configuration.getGenSourcesDir()))
            configuration.setGenSourcesDir(FacetConfigurations.guessGeneratedSourcesDir(parent).getAbsolutePath());

        mmFacetTab.setGenSourcesDir(configuration.getGenSourcesDir());
    }

    @Override public boolean isModified() {
        return !equal(configuration.getGenSourcesDir(), mmFacetTab.getGenOutputDir());
    }

    @Nls @Override public String getDisplayName() {
        return SUI_GEN_CONFIGURATION;
    }
}  // end class MMFacetEditorTab
