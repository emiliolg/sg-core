
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.ex.JpsElementChildRoleBase;
import org.jetbrains.jps.model.module.JpsModule;

import tekgenesis.lang.mm.compiler.model.MetaModelFacetSettings;

/**
 * Settings manager.
 */
public class MetaModelJpsUtils {

    //~ Constructors .................................................................................................................................

    private MetaModelJpsUtils() {}

    //~ Methods ......................................................................................................................................

    /** Get MetaModel facet settings. */
    @Nullable public static MetaModelFacetSettings getFacetSettings(@NotNull JpsModule module) {
        return module.getContainer().getChild(FACET_SETTINGS_ROLE);
    }

    //~ Static Fields ................................................................................................................................

    public static final String META_MODEL_SOURCES_PATH = "/src/main/mm";
    public static final String META_MODEL_TEST_PATH    = "/src/test/mm";
    public static final String RESOURCES_PATH          = "/src/main/resources";

    public static final JpsElementChildRoleBase<MetaModelFacetSettings> FACET_SETTINGS_ROLE = JpsElementChildRoleBase.create(
            "metamodel facet settings");
}
