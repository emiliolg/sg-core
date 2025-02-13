
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.extension;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.util.Reflection;
import tekgenesis.form.Action;
import tekgenesis.form.FormInstance;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.metadata.form.widget.WidgetBuilder;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.ensureNotNull;

/**
 * Form extension service class.
 *
 * <p>A form extension is a concrete subclass of this class, that is actually an implementation of a
 * FormInstance. The constructor should specify the form class to extend, and the extend method can
 * be overriden to the metadata of the form. The class will be loaded using the
 * {@link java.util.ServiceLoader} facility.</p>
 */
public abstract class FormExtension<F extends FormInstance<E>, E> extends FormInstance<E> implements Cloneable {

    //~ Instance Fields ..............................................................................................................................

    protected UiModelAccessor f = null;
    protected F               i = null;

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action cancel() {
        return i.cancel();
    }

    @NotNull @Override public Action create() {
        return i.create();
    }

    @NotNull @Override public Action delete() {
        return i.delete();
    }

    @NotNull @Override public Action deprecate(final boolean status) {
        return i.deprecate(status);
    }

    /** Method invoked on Form initialization. */
    @SuppressWarnings({ "EmptyMethod", "RedundantThrows" })
    public void extend(Extender<F, E> extender)
        throws BuilderException {}

    /**
     * Method invoked on Form initialization. Use Parameter.queryStringToMap() to parse parameters.
     */
    public void extend(Extender<F, E> extender, @Nullable String pk, @Nullable final String parameters)
        throws BuilderException
    {
        extend(extender.cached(true));
    }

    @NotNull @Override public String keyAsString() {
        return i.keyAsString();
    }

    @Override public void load() {
        i.load();
    }

    @NotNull @Override public E populate() {
        return i.populate();
    }

    @NotNull @Override public Action update() {
        return i.update();
    }

    /** Specify the Form. */
    public abstract Class<F> getFormType();

    @Override public void setPrimaryKey(@NotNull final String pk) {
        i.setPrimaryKey(pk);
    }

    protected FormExtension<F, E> clone(@Nullable UiModelAccessor implementation, @Nullable F instance)
        throws CloneNotSupportedException
    {
        final FormExtension<F, E> result = cast(clone());
        result.f = implementation;
        result.i = instance;
        return result;
    }

    /** Returns the FormImplementation for a row. */
    @NotNull protected UiModelAccessor rowF(FormRowInstance<?> row) {
        return ensureNotNull(Reflection.getPrivateField(row, "f"));
    }

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Form extender interface.
     */
    public interface Extender<F extends FormInstance<E>, E> {
        /** Add new widget at the end of the form. */
        FormFieldRef add(WidgetBuilder widgetBuilder)
            throws BuilderException;

        /** Add new widget after the other. */
        FormFieldRef addAfter(FormFieldRef ref, WidgetBuilder widgetBuilder)
            throws BuilderException;

        /** Add new widget before the other. */
        FormFieldRef addBefore(FormFieldRef ref, WidgetBuilder widgetBuilder)
            throws BuilderException;

        /** Add new widget inside the other. */
        FormFieldRef addInside(FormFieldRef ref, WidgetBuilder widgetBuilder)
            throws BuilderException;

        /** Mark the form as cached. */
        Extender<F, E> cached(boolean cached);

        /** Find a child widget by the form. */
        WidgetBuilder findWidget(FormFieldRef ref)
            throws BuilderException;

        /** Add onChange listener function to a field. */
        WidgetBuilder onChange(FormFieldRef ref, Function<? extends FormExtension<F, E>, Action> fn)
            throws BuilderException;

        /** Add onClick listener function to a field. */
        WidgetBuilder onClick(FormFieldRef ref, Function<? extends FormExtension<F, E>, Action> fn)
            throws BuilderException;

        /** Remove a children widget. */
        void remove(WidgetBuilder widgetBuilder)
            throws BuilderException;
    }
}  // end class FormExtension
