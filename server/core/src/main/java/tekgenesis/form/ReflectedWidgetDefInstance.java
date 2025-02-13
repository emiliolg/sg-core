
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

import tekgenesis.common.core.Lazy;
import tekgenesis.common.util.Reflection.Instance;
import tekgenesis.metadata.form.model.WidgetDefModel;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.codegen.CodeGeneratorConstants.DEFINE_METHOD;
import static tekgenesis.codegen.common.MMCodeGenConstants.F_VAR;
import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.core.Strings.capitalizeFirst;
import static tekgenesis.common.util.Reflection.invokeDeclared;

/**
 * Wrapped Reflection.Instance to manipulate user widget definition classes.
 */
public class ReflectedWidgetDefInstance implements UiModelInstanceHandler {

    //~ Instance Fields ..............................................................................................................................

    private final Lazy<UiModelAccessorImpl> accessor;
    private final Instance                  instance;
    private final ReflectedFormInstance     root;

    //~ Constructors .................................................................................................................................

    /** Create new {@link ReflectedWidgetDefInstance reflected instance}. */
    public ReflectedWidgetDefInstance(@NotNull final ReflectedFormInstance root, @NotNull final WidgetInstance<?> object) {
        this.root = root;
        instance  = Instance.wrap(object);
        accessor  = instantiate();
        setInstanceFields();
    }

    //~ Methods ......................................................................................................................................

    @Override public <W extends WidgetInstance<?>> W invokeDefine(@NotNull String name) {
        return ensureNotNull(invokeDeclared(instance.getInstance(), DEFINE_METHOD + capitalizeFirst(name)));
    }

    @Override public ReflectedFormInstance root() {
        return root;
    }

    @NotNull @Override public Instance wrapped() {
        return instance;
    }

    @Override public void setCurrentWidget(Widget widget) {
        // todo pcolunga
    }

    @Override public FormFieldRef getEnumFieldRef(@NotNull Widget widget) {
        return getEnumField(widget);
    }

    @Override public FormInstance<?> getExtendedInstance() {
        throw unreachable();
    }

    /** Get underlying {@link WidgetDefModel model}. */
    public WidgetDefModel getModel() {
        return (WidgetDefModel) getUiModelAccessor().container();
    }

    @Override public void setSlot(@NotNull Widget field, @Nullable Object value) {
        getUiModelAccessor().set(field, value);
    }

    /** Returns underlying {@link WidgetInstance instance}. */
    WidgetInstance<?> getInstance() {
        return cast(instance.getInstance());
    }

    /** Set {@link WidgetDefModel model}. */
    ReflectedWidgetDefInstance setModel(@NotNull final WidgetDefModel model) {
        instance.setPrivateField(F_VAR, new UiModelAccessorImpl(root, this, model));
        return this;
    }

    private Lazy<UiModelAccessorImpl> instantiate() {
        return new Lazy<>(() -> instance.getPrivateField(F_VAR));
    }

    @SuppressWarnings("ConstantConditions")
    private void setInstanceFields() {
        final WidgetInstance<?> widgetInstance = getInstance();
        if (widgetInstance.actions == null) {
            widgetInstance.actions = ActionsImpl.getInstance();
            widgetInstance.context = new ApplicationContextImpl();
        }
    }

    @NotNull private UiModelAccessorImpl getUiModelAccessor() {
        return accessor.get();
    }
}  // end class ReflectedWidgetDefInstance
