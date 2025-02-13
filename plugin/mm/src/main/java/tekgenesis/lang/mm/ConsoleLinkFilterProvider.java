
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import com.intellij.execution.filters.ConsoleFilterProvider;
import com.intellij.execution.filters.Filter;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;

/**
 * Console link provider.
 */
@SuppressWarnings("WeakerAccess")
public class ConsoleLinkFilterProvider implements ConsoleFilterProvider {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Filter[] getDefaultFilters(@NotNull Project project) {
        final Filter filter = new ConsoleLinkFilter();
        return new Filter[] { filter };
    }
}
