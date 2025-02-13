
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.compiler.builder;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.incremental.BuilderService;
import org.jetbrains.jps.incremental.ModuleLevelBuilder;

/**
 * MetaModel Builder Service Registration class.
 */
public class MetaModelBuilderService extends BuilderService {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public List<? extends ModuleLevelBuilder> createModuleLevelBuilders() {
        return Arrays.asList(new MetaModelSourceGeneratorBuilder(), new IxSourceGeneratorBuilder());
    }
}
