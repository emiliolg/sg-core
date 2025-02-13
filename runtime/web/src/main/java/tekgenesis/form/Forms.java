
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

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.common.env.security.PermissionException;
import tekgenesis.form.filter.Filter;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.type.permission.Permission;
import tekgenesis.workflow.WorkItemInstance;

/**
 * Utility methods to deal with Forms.
 */
public interface Forms {

    //~ Methods ......................................................................................................................................

    /** Mark given form as update. Useful for 'singleton' instances. */
    void asUpdate();

    /** Check permission for current user and current form. */
    void checkPermission(@NotNull Permission permission)
        throws PermissionException;

    /**
     * Check permission for current user and selected form. Throws a {@link PermissionException} if
     * not granted.
     */
    void checkPermission(@NotNull Class<? extends FormInstance<?>> form, @NotNull Permission permission)
        throws PermissionException;

    /**
     * Check permission for current user and the form identified by the {@link QName} Throws a
     * {@link PermissionException} if not granted.
     */
    void checkPermission(@NotNull QName name, @NotNull Permission permission)
        throws PermissionException;

    /**
     * Apply filters to filtered multiple. Filters section will be populated and client side ready.
     */
    void filter(@NotNull final Seq<? extends Filter> filters);

    /** Check permission for current user and the current form. */
    boolean hasPermission(@NotNull Permission permission);

    /** Check permission for current user and selected form. */
    boolean hasPermission(@NotNull Class<? extends FormInstance<?>> form, @NotNull Permission permission);

    /** Check permission for current user and the form identified by the {@link QName}. */
    boolean hasPermission(@NotNull QName name, @NotNull Permission permission);

    /** Initialize a given Form class. */
    @NotNull <T extends FormInstance<?>> T initialize(@NotNull final Class<T> clazz);

    /** Initialize a given Form class with a pk. */
    @NotNull <T extends FormInstance<?>> T initialize(@NotNull final Class<T> clazz, @Nullable final String pk);

    /** Initialize a given Form class with a map of parameters. */
    @NotNull <T extends FormInstance<?>> T initialize(@NotNull final Class<T> clazz, @Nullable FormParameters<T> params);

    /** Initialize a given Form class with a pk and a map of parameters. */
    @NotNull <T extends FormInstance<?>> T initialize(@NotNull final Class<T> clazz, @Nullable final String pk, @Nullable FormParameters<T> params);

    /** Updates the form's model's locking timestamp to avoid optimistic locking warning. */
    <T extends EntityInstance<T, K>, K> void lockingTimestamp(@NotNull T instance);

    /** Mark given form as read-only. */
    void readOnly(boolean readOnly);

    /** Validate a given Form instance. */
    @NotNull Validations validate();

    /**
     * Return current interaction widget. If no interaction widget is defined, an exception is
     * thrown.
     */
    @NotNull FormFieldRef getCurrentWidget();

    /**
     * Return current interaction widget enum Field. If no interaction widget is defined, an
     * exception is thrown.
     */
    @NotNull <T extends Enum<T>> T getCurrentWidgetEnum();

    /** Returns true if form is on update. */
    boolean isUpdate();

    /** Turns on/off a permission over the current form. */
    void setPermission(@NotNull String permission, boolean status);

    /** Returns true if current interaction widget matches given widget. */
    boolean isCurrentWidget(@NotNull FormFieldRef candidate);

    /** Returns current work item (if any). */
    @NotNull Option<WorkItemInstance<?, ?, ?, ?, ?, ?>> getWorkItem();

    /** Is form marked as read-only. */
    boolean isReadOnly();
}  // end interface Forms
