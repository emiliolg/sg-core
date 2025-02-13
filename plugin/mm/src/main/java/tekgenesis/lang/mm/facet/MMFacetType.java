
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

import com.intellij.facet.Facet;
import com.intellij.facet.FacetType;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.MMFileType;

import static tekgenesis.lang.mm.MMPluginConstants.SUI_GENERIS;

/**
 * MM Facet Type.
 */
public class MMFacetType extends FacetType<MMFacet, MMFacetConfiguration> {

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public MMFacetType() {
        super(MMFacet.ID, SUI_GENERIS, SUI_GENERIS);
    }

    //~ Methods ......................................................................................................................................

    @Override public MMFacetConfiguration createDefaultConfiguration() {
        return new MMFacetConfiguration();
    }

    @Override public MMFacet createFacet(@NotNull Module module, String s, @NotNull MMFacetConfiguration mmFacetConfiguration,
                                         @Nullable
                                         @SuppressWarnings("rawtypes")
                                         Facet facet) {
        return new MMFacet(module, s, mmFacetConfiguration);
    }

    @Override public boolean isSuitableModuleType(@SuppressWarnings("rawtypes") ModuleType moduleType) {
        return moduleType instanceof JavaModuleType;
    }

    @Override public Icon getIcon() {
        return MMFileType.ENTITY_FILE_ICON;
    }
}
