
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

import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.common.util.Reflection.Instance;
import tekgenesis.metadata.form.model.ParentModel;
import tekgenesis.metadata.form.model.UiModelBase;
import tekgenesis.metadata.form.model.WidgetDefModel;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.codegen.common.MMCodeGenConstants.FIELDS_ENUM;
import static tekgenesis.codegen.form.FormBaseCodeGenerator.getFieldEnumName;
import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.core.Strings.getterName;
import static tekgenesis.common.core.Strings.setterName;

/**
 * Handler for {@link UiModel ui model} class instance handlers.
 */
public interface UiModelInstanceHandler extends FieldAccessor, WidgetDefineMethodInvoker {

    //~ Methods ......................................................................................................................................

    /** Invoke given user method and return invocation result. */
    @Nullable default <T> T invokeUserMethod(@NotNull final String method, @NotNull final Object... args) {
        return wrapped().invoke(method, args);
    }

    /** Return nested {@link UiModelInstanceHandler widget definition reflected instance}. */
    @NotNull default UiModelInstanceHandler nested(@NotNull final WidgetDefModel wdm) {
        final ParentModel       parent   = wdm.parent().getOrFail("Nested widgets must always have a defined parent");
        final WidgetInstance<?> instance = resolveAnchorInstance(parent).invokeDefine(parent.anchor().getName());
        return new ReflectedWidgetDefInstance(root(), instance).setModel(wdm);
    }

    /** Resolve anchor instance. This method should be private ;) */
    default WidgetDefineMethodInvoker resolveAnchorInstance(ParentModel parent) {
        return parent.anchor()
               .getMultiple()
               .map(multiple -> (WidgetDefineMethodInvoker) getMultipleInstance(multiple).get(parent.item().get()))
               .orElse(this);
    }

    /** Return {@link ReflectedFormInstance root model instance}. */
    ReflectedFormInstance root();

    /** Return {@link Instance wrapped instance}. */
    @NotNull Instance wrapped();

    /** Set current ui model widget. */
    void setCurrentWidget(Widget widget);

    /** Returns enum instance of the given field. */
    default FormFieldRef getEnumField(final Widget field) {
        final Class<?> clazz    = wrapped().findInnerClass(FIELDS_ENUM).getOrFail("Enum 'Field' expected on ui model");
        final Enum<?>  formEnum = Enumerations.valueOf(clazz.getName(), getFieldEnumName(field.getName()));
        return (FormFieldRef) formEnum;
    }

    /** Returns form field ref of the given field. */
    FormFieldRef getEnumFieldRef(@NotNull final Widget widget);

    /** Return form extended or original instance. */
    FormInstance<?> getExtendedInstance();

    /** Set a field value using class setter. */
    @Override default void setField(@NotNull final Widget field, @Nullable final Object value) {
        final Object[] arg = { value };
        wrapped().invoke(setterName(field.getName()), arg);
    }

    /** Return underlying {@link UiModelBase model}. */
    UiModelBase<?> getModel();

    /** Get {@link ReflectedMultipleInstance instance} wrapping multiple defined class. */
    default ReflectedMultipleInstance getMultipleInstance(@NotNull final Widget multiple) {
        final Object result = ensureNotNull(wrapped().invoke(getterName(multiple.getName(), multiple.getType().getImplementationClassName())));
        return new ReflectedMultipleInstance((FormTable<?>) result);
    }

    //~ Methods ......................................................................................................................................

    /**
     * Wrap {@link UiModelInstance object} into corresponding
     * {@link UiModelInstanceHandler instance handler}.
     */
    static UiModelInstanceHandler wrap(@NotNull final ReflectedFormInstance root, @NotNull final UiModelInstance instance) {
        return instance instanceof FormInstance ? new ReflectedFormInstance((FormInstance<?>) instance)
                                                : new ReflectedWidgetDefInstance(root, (WidgetInstance<?>) instance);
    }
}  // end interface UiModelInstanceHandler
