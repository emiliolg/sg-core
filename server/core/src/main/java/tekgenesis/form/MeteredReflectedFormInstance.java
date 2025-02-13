
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.util.function.Function;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metric.core.CallMetric;
import tekgenesis.metric.core.MetricSources;
import tekgenesis.metric.core.MetricsFactory;

import static tekgenesis.common.core.Suppliers.fromRunnable;

/**
 * Metered ReflectedFormInstanceHandler.
 */
public class MeteredReflectedFormInstance extends ReflectedFormInstance {

    //~ Constructors .................................................................................................................................

    MeteredReflectedFormInstance(@NotNull final FormInstance<?> object) {
        super(object);
    }

    /** Create new FormInstance. */
    MeteredReflectedFormInstance(@NotNull Class<? extends FormInstance<?>> clazz, @NotNull Form form) {
        super(clazz, form);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public Action cancel() {
        return withMetric("cancel", super::cancel, this::isSuccessAction);
    }

    @NotNull @Override
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public Action create() {
        return withMetric("create", super::create, this::isSuccessAction);
    }

    @NotNull @Override
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public Action delete() {
        return withMetric("delete", super::delete, this::isSuccessAction);
    }

    @NotNull
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public Action deprecate(boolean deprecate) {
        return withMetric("deprecate", () -> super.deprecate(deprecate), this::isSuccessAction);
    }

    @Override public Object find() {
        return withMetric("find", super::find);
    }

    @Override public <T> T invokeUserMethod(@NotNull String method, @NotNull Object... args) {
        return withMetric(method, () -> super.invokeUserMethod(method, args));
    }

    @NotNull @Override
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public Object populate() {
        return withMetric("populate", super::populate);
    }

    @NotNull @Override
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public Action update() {
        return withMetric("update", super::update, this::isSuccessAction);
    }

    @Override void onLoad() {
        withMetric("onLoad", fromRunnable(super::onLoad));
    }

    private <T> T withMetric(String method, Supplier<T> supplier) {
        return withMetric(method, supplier, t -> true);
    }

    private <T> T withMetric(String method, Supplier<T> supplier, Function<T, Boolean> checkSuccess) {
        final CallMetric m = MetricsFactory.call(MetricSources.FORM, getInstance().getClass(), method).start();
        try {
            final T t = supplier.get();
            m.mark(checkSuccess.apply(t));
            return t;
        }
        catch (final Throwable e) {
            m.mark(false);
            throw e;
        }
        finally {
            m.stop();
        }
    }

    private boolean isSuccessAction(@NotNull final Action action) {
        return !action.isError();
    }

    //~ Methods ......................................................................................................................................

    /** @return  a MeteredFormInstanceHandler */
    public static MeteredReflectedFormInstance create(ReflectedFormInstance reflectedFormInstance) {
        return new MeteredReflectedFormInstance(reflectedFormInstance);
    }
}  // end class MeteredReflectedFormInstance
