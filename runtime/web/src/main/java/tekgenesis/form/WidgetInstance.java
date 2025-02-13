
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import org.jetbrains.annotations.NotNull;

/**
 * Interface to represent a widget instance.
 */
@SuppressWarnings("InstanceVariableMayNotBeInitialized")
public abstract class WidgetInstance<T> implements UiModelInstance {

    //~ Instance Fields ..............................................................................................................................

    /** Utility methods to deal with Actions. */
    @NotNull
    @SuppressWarnings("NullableProblems")
    protected Actions          actions;

    /** Utility methods to deal with Context. */
    @NotNull
    @SuppressWarnings("NullableProblems")
    protected ApplicationContext context;
}
