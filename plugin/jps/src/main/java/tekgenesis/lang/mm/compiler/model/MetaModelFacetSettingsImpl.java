
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.compiler.model;

import java.io.File;

import com.intellij.util.xmlb.XmlSerializerUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.ex.JpsElementBase;
import org.jetbrains.jps.model.module.JpsModule;

/**
 * MetaModel Facet Settings.
 */
public class MetaModelFacetSettingsImpl extends JpsElementBase<MetaModelFacetSettingsImpl> implements MetaModelFacetSettings {

    //~ Instance Fields ..............................................................................................................................

    private final MetaModelFacetSettingsState state;

    //~ Constructors .................................................................................................................................

    /** Settings default constructor. */
    public MetaModelFacetSettingsImpl(MetaModelFacetSettingsState state) {
        this.state = state;
    }

    //~ Methods ......................................................................................................................................

    @Override public void applyChanges(@NotNull MetaModelFacetSettingsImpl modified) {
        XmlSerializerUtil.copyBean(modified.state, state);
        fireElementChanged();
    }

    @NotNull @Override public MetaModelFacetSettingsImpl createCopy() {
        return new MetaModelFacetSettingsImpl(XmlSerializerUtil.createCopy(state));
    }

    @Nullable @Override public File getGeneratedSourcesDir() {
        return new File(state.generatedSourcesDir);
    }

    @NotNull @Override public JpsModule getModule() {
        return (JpsModule) getParent();
    }

    MetaModelFacetSettingsState getState() {
        return state;
    }
}
