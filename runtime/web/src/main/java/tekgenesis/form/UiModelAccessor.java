
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.util.Comparator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.form.configuration.WidgetConfiguration;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.model.KeyMap;
import tekgenesis.persistence.HasChildren;

/**
 * Ui model accessor to be used privately on ui base classes.
 */
public interface UiModelAccessor {

    //~ Methods ......................................................................................................................................

    /**
     * Associates a configuration to the specified field and returns it for further manipulation.
     */
    @NotNull <T extends WidgetConfiguration> T config(@NotNull FormFieldRef field);

    /** Return true if the field has a value. */
    boolean defined(@NotNull FormFieldRef... fields);

    /** Focus given field. */
    void focus(@NotNull FormFieldRef field);

    /** Return the field associated value. */
    <T> T get(@NotNull FormFieldRef field, Class<T> c);

    /** Initialize and return the field associated sub-form. */
    @NotNull <T extends FormInstance<?>> T init(@NotNull FormFieldRef field, @NotNull Class<T> clazz);

    /** Initialize, populates and return the field associated sub-form. */
    @NotNull <T extends FormInstance<?>> T init(@NotNull FormFieldRef field, @NotNull Class<T> clazz, @NotNull String pk);

    /** Returns the label of the given field. */
    String label(@NotNull FormFieldRef field);

    /**
     * Associates a new message to the specified field and returns it for any further configuration.
     */
    @NotNull Message msg(@NotNull FormFieldRef field, @NotNull String msg);

    /** Returns optional field associated widget. */
    @NotNull <W extends WidgetInstance<?>> OptionalWidget<W> optionalWidget(@NotNull final FormFieldRef field, @NotNull final Class<W> clazz);

    /** Set the field associated options. */
    void opts(@NotNull FormFieldRef field, @NotNull Iterable<?> items);

    /** Set the field associated options. */
    void opts(@NotNull FormFieldRef field, @NotNull KeyMap items);

    /** Set the field associated tree model options. */
    <T extends HasChildren<T>> void optsTree(@NotNull FormFieldRef field, @NotNull Iterable<? extends T> items);

    /** Set the field associated tree model options. Use comparator to sort items and children. */
    <T extends HasChildren<T>> void optsTree(@NotNull FormFieldRef field, @NotNull Iterable<? extends T> items,
                                             @NotNull Comparator<? super T> comparator);

    /** Resets the given fields. */
    void reset(@NotNull FormFieldRef... fields);

    /** Return {@link FormInstance root model instance}. */
    @NotNull FormInstance<?> root();

    /** Set the field associated value. */
    void set(@NotNull FormFieldRef field, @Nullable Object v);

    /** Return the field associated sub-form. */
    @Nullable <T extends FormInstance<?>> T subform(@NotNull final FormFieldRef field, @NotNull final Class<T> clazz);

    /** Return a FormTable Object to access and modify a table. */
    @NotNull <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> row, @NotNull UiModelInstance enclosing);

    /** Returns form title. */
    String title();

    /** Returns field associated widget. */
    @NotNull <W extends WidgetInstance<?>> W widget(@NotNull final FormFieldRef field, @NotNull final Class<W> clazz);

    /** Return the field associated array value. */
    @NotNull <T> Seq<T> getArray(@NotNull FormFieldRef field, final Class<T> c);

    /** Set the array associated value. */
    void setArray(@NotNull FormFieldRef field, @Nullable Iterable<?> v);
}  // end interface UiModelAccessor
