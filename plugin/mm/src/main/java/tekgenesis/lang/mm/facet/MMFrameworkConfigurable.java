
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.facet;

import javax.swing.*;

import com.intellij.compiler.CompilerConfiguration;
import com.intellij.facet.FacetManager;
import com.intellij.facet.ModifiableFacetModel;
import com.intellij.framework.addSupport.FrameworkSupportInModuleConfigurable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModifiableModelsProvider;
import com.intellij.openapi.roots.ModifiableRootModel;

import org.jetbrains.annotations.NotNull;

import static tekgenesis.lang.mm.MMPluginConstants.SUI_GENERIS;

/**
 * MM Framework Configurable.
 */
public class MMFrameworkConfigurable extends FrameworkSupportInModuleConfigurable {

    //~ Methods ......................................................................................................................................

    @Override public void addSupport(@NotNull final Module module, @NotNull final ModifiableRootModel modifiableRootModel,
                                     @NotNull ModifiableModelsProvider modifiableModelsProvider) {
        addFacet(module);
        addCompilerExtensions(modifiableRootModel);
    }

    @Override public JComponent createComponent() {
        return null;
    }

    private void addCompilerExtensions(ModifiableRootModel modifiableRootModel) {
        final CompilerConfiguration instance = CompilerConfiguration.getInstance(modifiableRootModel.getProject());
        if (!instance.isResourceFile("pepe.sql")) instance.addResourceFilePattern("?*.sql");
        if (!instance.isResourceFile("pepe.csv")) instance.addResourceFilePattern("?*.csv");
        if (!instance.isResourceFile("/html/pepe.html")) instance.addResourceFilePattern("/**/?*.html");
        if (!instance.isResourceFile("/html/pepe.xhtml")) instance.addResourceFilePattern("/**/?*.xhtml");
        if (!instance.isResourceFile("/css/pepe.css")) instance.addResourceFilePattern("/**/?*.css");
        if (!instance.isResourceFile("/img/pepe.png")) instance.addResourceFilePattern("/**/?*.png");
        if (!instance.isResourceFile("/img/pepe.jpeg")) instance.addResourceFilePattern("/**/?*.jpeg");
        if (!instance.isResourceFile("/js/pepe.js")) instance.addResourceFilePattern("/**/?*.js");
        if (!instance.isResourceFile("/META-INF/services/Pepe")) instance.addResourceFilePattern("/META-INF/services/*");
    }

    private MMFacet addFacet(Module module) {
        final FacetManager         facetManager = FacetManager.getInstance(module);
        final ModifiableFacetModel model        = facetManager.createModifiableModel();
        MMFacet                    facet        = model.getFacetByType(MMFacet.ID);

        if (facet == null) {
            facet = facetManager.createFacet(MMFacet.getFacetType(), SUI_GENERIS, null);
            model.addFacet(facet);
        }
        model.commit();
        return facet;
    }
}  // end class MMFrameworkConfigurable
