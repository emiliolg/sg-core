
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
 * A Form Instance injected with helper objects.
 */
@SuppressWarnings({ "WeakerAccess", "InstanceVariableMayNotBeInitialized" })
public abstract class FormInstance<T> implements UiModelInstance {

    //~ Instance Fields ..............................................................................................................................

    /** Utility methods to deal with Actions. */
    @NotNull
    @SuppressWarnings("NullableProblems")
    protected Actions          actions;

    /** Utility methods to deal with Context. */
    @NotNull
    @SuppressWarnings("NullableProblems")
    protected ApplicationContext          context;

    /** Utility methods to deal with Forms. */
    @NotNull
    @SuppressWarnings("NullableProblems")
    protected Forms forms;

    //~ Methods ......................................................................................................................................

    /** Invoked when canceling a form instance. */
    @NotNull public Action cancel() {
        return actions.getDefault();
    }

    /** Invoked when creating a form instance. */
    @NotNull public Action create() {
        return actions.getDefault();
    }

    /** Invoked when deleting a form instance. */
    @NotNull public Action delete() {
        return actions.getDefault();
    }

    /** Invoked to change the deprecation status of an instance. */
    @NotNull public Action deprecate(boolean status) {
        return actions.getDefault();
    }

    /** Returns the Form key as String. */
    @NotNull public String keyAsString() {
        return "";
    }

    /** Invoked when the form is loaded. */
    public void load() {}

    /** Invoked when populating a form instance. */
    @NotNull public abstract T populate();

    /** Invoked when updating a form instance. */
    @NotNull public Action update() {
        return actions.getDefault();
    }

    /** @return  if the FormInstance is dynamic created */
    public boolean isDynamic() {
        return false;
    }

    /** Set the Form primary key. */
    public void setPrimaryKey(@NotNull String pk) {}
}  // end class FormInstance
