
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.util.Locale;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.exception.InvalidKeyToBeLocalized;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.model.KeyMap;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.mkString;
import static tekgenesis.common.util.Reflection.construct;
import static tekgenesis.common.util.Reflection.invoke;
import static tekgenesis.metadata.exception.InvalidKeyToBeLocalized.InvalidKeyLocalizeType.INVALID_KEY;
import static tekgenesis.metadata.form.widget.UiModelLocalizer.localizer;
import static tekgenesis.transaction.Transaction.invokeInTransaction;

/**
 * Utility class to deal with Forms on test environments.
 */
@SuppressWarnings("WeakerAccess")
public class FormTests {

    //~ Constructors .................................................................................................................................

    private FormTests() {}

    //~ Methods ......................................................................................................................................

    /** Initialize a given Form class. */
    @NotNull public static <T extends FormInstance<?>> T initialize(@NotNull final Class<T> clazz) {
        return invokeInTransaction(() -> FormsImpl.init(clazz, null).first());
    }

    /** Initialize a given Form class with a pk. */
    @NotNull public static <T extends FormInstance<?>> T initialize(@NotNull final Class<T> clazz, @NotNull final String pk) {
        return invokeInTransaction(() -> FormsImpl.init(clazz, pk).first());
    }
    /** Initialize a given Form class with a pk and parameters. */
    @NotNull public static <T extends FormInstance<?>> T initialize(@NotNull final Class<T> clazz, @Nullable final String pk,
                                                                    @NotNull FormParameters<T> parameters) {
        return invokeInTransaction(() -> FormsImpl.init(clazz, pk, parameters).first());
    }

    /**
     * Simulate form save or update. Initializes all fields and runs validations, before executing
     * create or update. Returns none Action if errors are found, or specific Action if form
     * contains no errors.
     */
    @NotNull public static Option<Action> submit(@NotNull final FormInstance<?> form) {
        return invokeInTransaction(t -> {
            final Forms forms = getForms(form);
            if (!forms.validate().isEmpty()) return Option.empty();

            final Action action = forms.isUpdate() ? form.update() : form.create();
            if (action.isError()) t.abort();
            return Option.of(action);
        });
    }

    /**
     * Simulate form save or update. Initializes all fields and runs validations, before executing
     * create or update. If errors are found, fails with a proper message derived from validation
     * errors else it returns Action.
     */
    @NotNull public static Action submitOrFail(@NotNull final FormInstance<?> form) {
        return invokeInTransaction(() -> {
            final Forms       forms      = getForms(form);
            final Validations validation = forms.validate();

            if (!validation.isEmpty()) fail(mkString(validation.getAllMessages()));

            return forms.isUpdate() ? form.update() : form.create();
        });
    }

    /** Runs validations and returns its result. */
    @NotNull public static Validations validate(@NotNull final FormInstance<?> form) {
        return getForms(form).validate();
    }

    /** Return the message associated with the action if any. */
    @NotNull public static String getActionMessage(@NotNull final Action action) {
        return ((ActionImpl) action).getMsg().orElse("");
    }

    /**
     * Return the navigation class of an action. Throws an exception if action does not navigate.
     */
    @NotNull public static Class<? extends FormInstance<?>> getActionNavigationClass(@NotNull final Action action) {
        return cast(getActionForm(action).getClass());
    }

    /**
     * Return the expected target action form instance. Throws an exception if action does not
     * navigate to the expected instance.
     */
    @NotNull public static <T extends FormInstance<?>> T getActionTarget(@NotNull final Action action, @NotNull final Class<T> targetForm) {
        final FormInstance<?> form = getActionForm(action);
        if (!targetForm.isInstance(form))
            throw new IllegalStateException("Expecting navigation to '" + targetForm + "', but found '" + form.getClass() + "'");

        return cast(form);
    }

    /** Set the multiple current slot. */
    public static void setCurrent(@NotNull final FormInstance<?> form, @NotNull final FormFieldRef multiple, int slot) {
        ReflectedFormInstance.wrap(form).setCurrent(multiple, slot);
    }

    /** Set the current widget. */
    public static void setCurrentWidget(@NotNull final FormInstance<?> form, @NotNull final FormFieldRef widget) {
        ReflectedFormInstance.wrap(form).setCurrentWidget(widget);
    }

    /** Return is the field is disabled. */
    public static boolean isDisabled(@NotNull final FormInstance<?> form, @NotNull final FormFieldRef field) {
        return isDisabled(form, field, -1);
    }

    /** Return is the field/row is disabled. */
    public static boolean isDisabled(@NotNull final FormInstance<?> form, @NotNull final FormFieldRef field, int row) {
        return new Expressions(form, field, row).isDisabled();
    }

    /**
     * Given a download action and a Form instance, it returns the DownloadWriter associated to it.
     */
    public static Download.DownloadWriter getDownloadWriter(@NotNull final DownloadImpl download, @NotNull final FormInstance<?> form) {
        return construct(download.getWriter(), form);
    }

    /** Call on_ui_change method when setting value to a field. */
    public static void setFieldCallingUiChange(@NotNull final FormInstance<?> form, @NotNull final FormFieldRef field, Object value) {
        final ReflectedFormInstance instance = ReflectedFormInstance.wrap(form);
        final Widget                widget   = instance.getWidget(field);
        final String                uiChange = widget.getOnUiChangeMethodName();
        instance.setField(widget, value);
        if (isNotEmpty(uiChange)) invoke(form, uiChange);
    }

    /** Call on_ui_change method when setting value to a field of the row instance. */
    public static void setFieldCallingUiChange(@NotNull final FormInstance<?> form, final FormFieldRef tableField, int row,
                                               @NotNull final FormFieldRef field, Object value) {
        final ReflectedFormInstance instance = ReflectedFormInstance.wrap(form);

        final Widget table = instance.getWidget(tableField);
        assertTrue(table.getWidgetType() == WidgetType.TABLE);
        final ReflectedMultipleInstance             tableInstance = instance.getMultipleInstance(table);
        final ReflectedMultipleInstance.RowInstance rowInstance   = tableInstance.get(row);

        final Widget widget   = rowInstance.getModel().widgetByName(field.id());
        final String uiChange = widget.getOnUiChangeMethodName();
        rowInstance.setField(widget, value);
        if (isNotEmpty(uiChange)) invoke(rowInstance.getInstance(), uiChange);
    }

    /** Return field Expressions. */
    @NotNull public static Expressions getFieldExpressions(@NotNull final FormInstance<?> form, @NotNull final FormFieldRef field) {
        return new Expressions(form, field, -1);
    }

    /** Return field Expressions. */
    @NotNull public static Expressions getFieldExpressions(@NotNull final FormInstance<?> form, @NotNull final FormFieldRef field, int row) {
        return new Expressions(form, field, row);
    }

    /** Return the specified field message text. */
    @NotNull public static String getFieldMessageText(@NotNull final FormInstance<?> form, @NotNull final FormFieldRef field) {
        return ReflectedFormInstance.wrap(form).getFieldMessage(field);
    }

    /** Return the specified field message text for a row. */
    @NotNull public static String getFieldMessageText(@NotNull final FormInstance<?> form, @NotNull final FormFieldRef field, int row) {
        return ReflectedFormInstance.wrap(form).getFieldMessage(field, row);
    }

    /** Return the options for the specified field. */
    @NotNull public static KeyMap getFieldOptions(@NotNull final FormInstance<?> form, @NotNull final FormFieldRef field) {
        return ReflectedFormInstance.wrap(form).getFieldOptions(field);
    }

    /** Return the options for the specified field. */
    @NotNull public static KeyMap getFieldOptions(@NotNull final FormInstance<?> form, @NotNull final FormFieldRef field, int row) {
        return ReflectedFormInstance.wrap(form).getFieldOptions(field, row);
    }

    /** Return is the field is hidden. */
    public static boolean isOptional(@NotNull final FormInstance<?> form, @NotNull final FormFieldRef field) {
        return isOptional(form, field, -1);
    }

    /** Return is the field/row is hidden. */
    public static boolean isOptional(@NotNull final FormInstance<?> form, @NotNull final FormFieldRef field, int row) {
        return new Expressions(form, field, row).isOptional();
    }

    /**
     * Fetch localized message from .properties file for give key. Notice that this key must be
     * exactly as it appears on the .properties file. Example: name, name.hint, name.check, name.is,
     * etc. If there is NO properties file, it will return the mm value.
     */
    @NotNull public static String getLocalizedMessage(@NotNull final FormInstance<?> formInstance, @NotNull Locale locale, @NotNull String key) {
        final ReflectedFormInstance instance       = ReflectedFormInstance.wrap(formInstance);
        final Form                  form           = instance.getForm();
        final String                localizeString = localizer(form, locale).localizeString(key);
        if (localizeString != null) return localizeString;
        throw new InvalidKeyToBeLocalized(key, INVALID_KEY, key);
    }

    /** Return true if the action is a download action. */
    public static boolean isDownloadAction(@NotNull final Action action) {
        return ((ActionImpl) action).isDownload();
    }

    /** Return true if the action is an error action. */
    public static boolean isErrorAction(@NotNull final Action action) {
        return action.isError();
    }

    /** Return is the field is hidden. */
    public static boolean isHidden(@NotNull final FormInstance<?> form, @NotNull final FormFieldRef field) {
        return isHidden(form, field, -1);
    }

    /** Return is the field/row is hidden. */
    public static boolean isHidden(@NotNull final FormInstance<?> form, @NotNull final FormFieldRef field, int row) {
        return new Expressions(form, field, row).isHidden();
    }

    /** Return is the field is hidden. */
    public static boolean isHiddenColumn(@NotNull final FormInstance<?> form, @NotNull final FormFieldRef field) {
        return isHiddenColumn(form, field, -1);
    }

    /** Return is the field/row is hidden. */
    public static boolean isHiddenColumn(@NotNull final FormInstance<?> form, @NotNull final FormFieldRef field, int row) {
        return new Expressions(form, field, row).isHiddenColumn();
    }

    /** Return true if the action is a stay action. */
    public static boolean isStayAction(@NotNull final Action action) {
        return ((ActionImpl) action).isStay();
    }

    private static FormInstance<?> getActionForm(Action action) {
        if (!(action instanceof NavigateImpl)) throw new IllegalArgumentException("Action does not navigate!");
        return ((NavigateImpl<?>) action).resolveForm();
    }

    @NotNull private static Forms getForms(@NotNull FormInstance<?> form) {
        final ReflectedFormInstance instance = ReflectedFormInstance.wrap(form);
        instance.getModel().markInitElements();
        return instance.getInstance().forms;
    }
}  // end class FormTests
