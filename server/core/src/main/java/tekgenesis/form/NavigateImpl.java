
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.metadata.form.InstanceReference;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.FormAction;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.form.FormsImpl.initializeInstance;
import static tekgenesis.form.ReflectedFormInstance.checkPublicStatic;

/**
 * Form navigate action.
 */
public class NavigateImpl<T extends FormInstance<?>> extends ActionImpl implements Navigate<T> {

    //~ Instance Fields ..............................................................................................................................

    private final List<FormAction> actions = new ArrayList<>();

    private Class<MappingCallback<T, FormInstance<?>>> classCallback  = null;
    private boolean                                    confirmation;
    private boolean                                    dialog;
    private T                                          form           = null;
    private Class<T>                                   formClass      = null;
    private FormParameters<T>                          parameters     = null;
    private String                                     pk;
    private InstanceReference                          reference      = null;
    private String                                     targetFormBox  = null;
    private boolean                                    terminate;
    private Integer                                    widgetCallback = null;

    //~ Constructors .................................................................................................................................

    private NavigateImpl(@NotNull T form, String pk) {
        super(ActionType.NAVIGATE);
        this.form = form;
        this.pk   = pk;
    }

    private NavigateImpl(@NotNull Class<T> formClass, String pk, InstanceReference reference) {
        super(ActionType.NAVIGATE);
        this.pk        = pk;
        this.reference = reference;
        this.formClass = formClass;
    }

    //~ Methods ......................................................................................................................................

    /** Add form navigation callback with a MappingCallback class. */
    @NotNull @Override public <V extends FormInstance<?>> Navigate<T> callback(final Class<? extends MappingCallback<T, V>> clazz) {
        checkPublicStatic(clazz);
        classCallback = cast(clazz);
        return this;
    }

    /** Add form navigation callback bind to a Widget. */
    public void callback(final int ordinal) {
        widgetCallback = ordinal;
    }

    /** Add form navigation callback bind to a Widget. */
    @NotNull @Override public Navigate<T> callback(@NotNull Enum<?> field) {
        widgetCallback = field.ordinal();
        return this;
    }

    @NotNull @Override public Navigate<T> dialog() {
        dialog = true;
        return this;
    }

    /** Return true if any kind of callback is defined. */
    public boolean hasCallback() {
        return widgetCallback != null || classCallback != null;
    }

    /** Specify callback flow leave. */
    @NotNull @Override public Navigate<T> leave() {
        terminate = true;
        return this;
    }

    @NotNull @Override public Navigate<T> leaveWithConfirmation() {
        confirmation = true;
        return this;
    }

    /** Indicates whether this navigation should ask for confirmation or not. */
    public boolean needsConfirmation() {
        return confirmation;
    }

    /** Return form instance. */
    public T resolveForm() {
        if (form == null) setFormAndPk();
        return form;
    }

    /** Specify form box id to navigate in that target. */
    @NotNull @Override public Navigate<T> targetFormBox(@NotNull String formBoxId) {
        targetFormBox = formBoxId;
        return this;
    }

    /** Add internationalized actions to the navigation. */
    @NotNull public NavigateImpl<T> withActions(@NotNull Seq<FormAction> a) {
        a.into(actions);
        return this;
    }

    @NotNull @Override public Navigate<T> withParameters(final FormParameters<T> ps) {
        if (form != null)
            throw new IllegalStateException(
                "Form already initialized and parametrized. Parametrize on initialization or navigate with form class and set parameters.");
        parameters = ps;
        return this;
    }

    /** Return navigation actions. */
    public List<FormAction> getActions() {
        return actions;
    }

    /** Get form navigation callback. Ensure {@link #hasCallback()} first! */
    public String getCallback(@NotNull final Form context) {
        return widgetCallback != null ? context.getWidgetByOrdinal(widgetCallback).getName() : classCallback.getName();
    }

    /** Indicates whether this navigation should force callback flow termination. */
    public boolean isTerminate() {
        return terminate;
    }

    /** Return if navigate is dialog. */
    public boolean isDialog() {
        return dialog;
    }

    /** Return navigation parameters. */
    public FormParameters<T> getParameters() {
        return parameters;
    }

    /** Returns the pk of the loaded entity. */
    public String getPK() {
        return pk;
    }

    /** Return the target form box id. */
    public String getTargetFormBox() {
        return targetFormBox;
    }

    private void setFormAndPk() {
        final BaseReflectedFormInstance instance = initializeInstance(formClass, pk, reference, parameters != null ? parameters.asMap() : null);
        form = cast(instance.getInstance());
        pk   = instance.getModel().isUpdate() ? instance.keyAsString() : null;
    }

    //~ Methods ......................................................................................................................................

    /** Invoked from interface. User initialized the instance. */
    static <T extends FormInstance<?>> NavigateImpl<T> navigate(@NotNull final T form) {
        return navigate(BaseReflectedFormInstance.wrapInstanceHandler(form));
    }

    /** If model is update, navigate with primary key. */
    static <T extends FormInstance<?>> NavigateImpl<T> navigate(@NotNull final BaseReflectedFormInstance instance) {
        final T clazz = cast(instance.getInstance());
        return new NavigateImpl<>(clazz, instance.getModel().isUpdate() ? instance.keyAsString() : null);
    }

    @NotNull static <T extends FormInstance<?>> NavigateImpl<T> navigate(Class<T> formClass) {
        return navigate(formClass, null, null);
    }

    @NotNull static <T extends FormInstance<?>> NavigateImpl<T> navigate(Class<T> formClass, String pk) {
        return navigate(formClass, pk, null);
    }

    @NotNull static <T extends FormInstance<?>> NavigateImpl<T> navigate(Class<T> formClass, String pk, InstanceReference reference) {
        return new NavigateImpl<>(formClass, pk, reference);
    }
}  // end class NavigateImpl
