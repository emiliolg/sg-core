
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Lazy;
import tekgenesis.common.core.Option;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.type.ModelType;

import static java.util.Comparator.comparingInt;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.core.Constants.SEQ_ID;
import static tekgenesis.common.core.Option.ofNullable;
import static tekgenesis.common.core.Strings.getterName;

/**
 * Scope bindings.
 */
class Bindings {

    //~ Instance Fields ..............................................................................................................................

    private final ImmutableList<Binding>      all;       // All scope bindings
    private final Option<? extends ModelType> binding;   // Scope binding model
    private final Lazy<Boolean>               fullKeys;  // Indicates if all primary keys are bound
    private final String                      implicit;  // An implicit field
    private final ImmutableList<Binding>      keys;      // Scope primary key bindings

    //~ Constructors .................................................................................................................................

    Bindings(Option<? extends ModelType> binding, Iterable<Widget> widgets) {
        this(binding, widgets, "");
    }

    Bindings(Option<? extends ModelType> binding, Iterable<Widget> widgets, String implicit) {
        this.binding  = binding;
        this.implicit = implicit;
        all           = calculateAllBindings(widgets);
        keys          = calculateKeyBindings();
        fullKeys      = calculateFullKeysBound();
    }

    //~ Methods ......................................................................................................................................

    boolean exists(@NotNull final Predicate<Binding> predicate) {
        return all.exists(predicate);
    }

    Seq<Binding> filter(@NotNull final Predicate<Binding> predicate) {
        return all.filter(predicate);
    }

    boolean hasDefaultPrimaryKey() {
        return binding.castTo(DbObject.class).map(DbObject::hasDefaultPrimaryKey).orElse(false);
    }

    ImmutableList<Binding> keys() {
        return keys;
    }

    boolean isPrimaryKeyBound() {
        return fullKeys.get();
    }

    boolean isDbObject() {
        return binding.castTo(DbObject.class).isPresent();
    }

    @NotNull private ImmutableList<Binding> calculateAllBindings(Iterable<Widget> widgets) {
        return binding.map(model -> {
                final Map<String, Widget> map      = generateBindingsMap(widgets);
                final List<Binding>       bindings = model.getChildren().flatMap(createBinding(model, map)).into(new ArrayList<>());
                Colls.filter(widgets, Widget::isPlaceholderBinding).map(PlaceholderBinding::new).forEach(bindings::add);
                return immutable(bindings);
            }).orElseGet(Colls::emptyList);
    }

    private Lazy<Boolean> calculateFullKeysBound() {
        return new Lazy<>(() -> binding.castTo(DbObject.class).map(db -> db.getPrimaryKey().forAll(this::isPrimaryKey)).orElse(false));
    }

    @NotNull private ImmutableList<Binding> calculateKeyBindings() {
        return binding.castTo(DbObject.class).map(db -> {
                final ImmutableList<? extends TypeField> pks = db.getPrimaryKey();
                return all.filter(Binding::isPrimaryKey).sorted(comparingInt(l -> pks.indexOf(l.field()))).toList();
            }).orElseGet(Colls::emptyList);
    }

    @NotNull private Function<TypeField, Iterable<Binding>> createBinding(ModelType model, Map<String, Widget> map) {
        final Predicate<TypeField> pk = f -> model instanceof DbObject && f instanceof Attribute && ((DbObject) model).isPrimaryKey((Attribute) f);

        return field -> {
                   final Option<Widget> w = ofNullable(map.get(field.getFullName()));
                   return w.map(widget -> new TypeFieldBinding(field, widget, pk.test(field)));
               };
    }

    private Map<String, Widget> generateBindingsMap(Iterable<Widget> widgets) {
        final Map<String, Widget> result = new HashMap<>();
        for (final Widget w : widgets) {
            final String b = w.getBinding();
            if (isNotEmpty(b)) result.put(b, w);
        }
        return result;
    }

    private boolean isPrimaryKey(Attribute pk) {
        return SEQ_ID.equals(pk.getName()) || implicit.equals(pk.getName()) || keys.exists(b -> b.field().equals(pk));
    }

    //~ Inner Classes ................................................................................................................................

    static class PlaceholderBinding implements Binding {
        private final Widget widget;

        PlaceholderBinding(Widget widget) {
            this.widget = widget;
        }

        @Override public TypeField field() {
            throw new IllegalStateException("Placeholder binding does not have an associated field");
        }

        @Override public String invokeFieldGetter(WidgetContainerClassGenerator generator, String instance) {
            return instance;
        }

        @Override public String invokeWidgetGetter(WidgetContainerClassGenerator generator, String instance) {
            final String getterName = getterName(widget().getName(), widget().getType().getImplementationClassName());
            return generator.invoke(instance, getterName);
        }

        @Override public Widget widget() {
            return widget;
        }

        @Override public boolean isProtected() {
            return false;
        }

        @Override public boolean isFieldMultiple() {
            return false;
        }

        @Override public boolean isArray() {
            return false;
        }

        @Override public boolean isPrimaryKey() {
            return false;
        }

        @Override public boolean isReadOnly() {
            return false;
        }
    }  // end class PlaceholderBinding

    static class TypeFieldBinding implements Binding {
        private final TypeField field;
        private final boolean   pk;
        private final Widget    widget;

        TypeFieldBinding(@NotNull final TypeField field, @NotNull final Widget widget, boolean pk) {
            this.field  = field;
            this.widget = widget;
            this.pk     = pk;
        }

        @Override public TypeField field() {
            return field;
        }

        @Override public String invokeFieldGetter(WidgetContainerClassGenerator generator, String instance) {
            final String getterName = getterName(field().getName(), field().getImplementationClassName());
            return generator.invoke(instance, getterName);
        }

        @Override public String invokeWidgetGetter(WidgetContainerClassGenerator generator, String instance) {
            final String getterName = getterName(widget().getName(), field().getImplementationClassName());
            return generator.invoke(instance, getterName);
        }

        @Override public Widget widget() {
            return widget;
        }

        @Override public boolean isPrimaryKey() {
            return pk;
        }
    }  // end class TypeFieldBinding
}  // end class Bindings
