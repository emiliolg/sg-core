
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.JpsElement;
import org.jetbrains.jps.model.module.JpsModule;

/**
 * MetaModel Facet Settings.
 */
public interface MetaModelFacetSettings extends JpsElement {

    //~ Methods ......................................................................................................................................

    /** Return generated sources directory. */
    @Nullable File getGeneratedSourcesDir();

    /** Return facet modules. */
    @NotNull JpsModule getModule();
}
