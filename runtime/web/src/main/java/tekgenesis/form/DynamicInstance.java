
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
import org.jetbrains.annotations.Nullable;

import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.form.widget.FormBuilder;

/**
 * Dynamic Instance.
 */
@SuppressWarnings({ "WeakerAccess", "InstanceVariableMayNotBeInitialized" })
public abstract class DynamicInstance {

    //~ Instance Fields ..............................................................................................................................

    /** Utility methods to deal with Actions. */
    @NotNull
    @SuppressWarnings("NullableProblems")
    protected Actions          actions;

    /** Utility methods to deal with Context. */
    @NotNull
    @SuppressWarnings("NullableProblems")
    protected ApplicationContext context;

    //~ Methods ......................................................................................................................................

    /**
     * Build dynamic Form.
     *
     * @param  builder  FormBuilder
     * @param  param    Parameter
     */
    public abstract void build(@NotNull final FormBuilder builder, @Nullable final String param)
        throws BuilderException;

    /** Invoked when cancelling a form instance. */
    @NotNull public abstract Action cancel(@NotNull DynamicFormAccessor dynamicForm, String param);
    /** Invoked when creating a form instance. */
    @NotNull public abstract Action create(@NotNull DynamicFormAccessor dynamicForm, @Nullable final String param);
    /** Invoked when deleting a form instance. */
    @NotNull public abstract Action delete(@NotNull DynamicFormAccessor dynamicForm, String param);
    /** Invoked when on_click a widget on a form instance. */
    @NotNull public abstract Action handleClick(@NotNull DynamicFormAccessor dynamicForm, @NotNull String methodName);
    /** Invoked when populating a form instance. */
    public abstract void populate(@NotNull DynamicFormAccessor dynamicForm, @NotNull final String param);
    /** Invoked when updating a form instance. */
    @NotNull public abstract Action update(@NotNull DynamicFormAccessor dynamicForm, @NotNull final String param);
}  // end class DynamicInstance
