
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form;

import java.util.function.BiFunction;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.annotation.Pure;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.ImmutableList.Builder;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.StrBuilder;
import tekgenesis.common.core.Tuple;
import tekgenesis.metadata.form.exprs.ItemContext;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetType;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.collections.ImmutableList.builder;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.of;
import static tekgenesis.common.core.Option.ofNullable;
import static tekgenesis.common.core.Strings.split;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.metadata.form.QualifiedWidget.nextScope;
import static tekgenesis.metadata.form.exprs.ItemContext.createItemContext;
import static tekgenesis.metadata.form.model.Model.resolveModel;
import static tekgenesis.metadata.form.widget.WidgetType.MAP;
import static tekgenesis.metadata.form.widget.WidgetType.TABLE;

/**
 * Qualified widget with optional section (per qualification).
 */
public class IndexedWidget extends BaseQualifiedWidget<IndexedWidget> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Option<Integer> item;

    //~ Constructors .................................................................................................................................

    private IndexedWidget(@NotNull final Widget widget) {
        super(widget);
        item = empty();
    }

    private IndexedWidget(@NotNull final Widget widget, @NotNull final Option<Integer> item) {
        super(widget);
        this.item = item;
    }

    private IndexedWidget(@NotNull final IndexedWidget qualification, @NotNull final Widget widget, @NotNull final Option<Integer> item) {
        super(qualification, widget);
        this.item = item;
    }

    //~ Methods ......................................................................................................................................

    public boolean belongsTo(@NotNull final MultipleWidget multiple) {
        return item.isPresent() && widget.getMultiple().map(m -> m.equals(multiple)).orElse(false);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IndexedWidget)) return false;

        final IndexedWidget that = (IndexedWidget) o;
        return super.equals(that) && item.equals(that.item);
    }

    /**
     * Applies a binary operator to a start value and all {@link ItemWidget parts} of this indexed
     * widget, going left to right. op(...op(initialValue, (flight, #2)), (departure, #?), ...,
     * (airport, #?)
     */
    @Contract("!null,_ -> !null")
    @Nullable @Pure public <T> T foldLeft(@Nullable T initialValue, BiFunction<T, ItemWidget, T> op) {
        return flattened().foldLeft(initialValue, op);
    }

    @Override public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + item.hashCode();
        return result;
    }

    /** Return optional item index. */
    @NotNull public Option<Integer> item() {
        return item;
    }

    /** Return optional {@link ItemContext item context}. */
    @NotNull public Option<ItemContext> itemContext() {
        return item.map(s -> createItemContext(widget.getMultiple().get(), s));
    }

    @NotNull @Override public Option<Model> mapping(@NotNull Model m) {
        return foldLeft(of(m), (previous, part) -> {
                final Widget w = part.widget();
                //J-
                if (w.isWidgetDef()) return previous.flatMap(model -> {
                    final Model next = resolveModel(model, w, part.item()).getWidgetDef(w);
                    return ofNullable(next);
                });
                //J+
                return previous;
            });
    }

    /**
     * Creates a new {@link IndexedWidget indexed widget} nesting given
     * {@link Widget widget and item} (e.g.: invoice.nest(product,#2) -> invoice.product#2)
     */
    @NotNull @Pure public IndexedWidget nested(@NotNull Widget w, @NotNull Option<Integer> s) {
        return new IndexedWidget(this, w, s);
    }

    /** Return optional qualification. */
    @NotNull public Option<IndexedWidget> qualification() {
        return qualification;
    }

    /** Return detached {@link IndexedWidget root indexed widget}. */
    @NotNull public IndexedWidget root() {
        return qualification.map(IndexedWidget::root).orElseGet(() -> createIndexed(widget, item));
    }

    @Override void name(@NotNull StrBuilder builder) {
        builder.appendElement(widget.getName() + item.map(i -> "#" + i).orElse(""));
    }

    /**
     * Flatten {@link IndexedWidget widget} as list (e.g.: flight#2.departure.airport -> [(flight,
     * #2), (departure,#?), (airport,#?)])
     */
    private Builder<ItemWidget> flatten(@NotNull final Builder<ItemWidget> builder) {
        qualification.ifPresent(q -> q.flatten(builder));
        return builder.add(new ItemWidget(widget, item));
    }

    /**
     * Return flattened list of {@link ItemWidget item widgets} (e.g.: flight#2.departure.airport ->
     * [(flight, #2), (departure,#?), (airport,#?)])
     */
    private ImmutableList<ItemWidget> flattened() {
        return flatten(builder()).build();
    }

    /**
     * Creates a new {@link IndexedWidget indexed widget} nesting given {@link ItemWidget widget}
     * (e.g.: invoice.nest((product,#2)) -> invoice.product#2)
     */
    @NotNull @Pure private IndexedWidget nested(@NotNull ItemWidget w) {
        return nested(w.widget, w.item);
    }

    //~ Methods ......................................................................................................................................

    /**
     * Create an {@link IndexedWidget indexed widget} from a given reference (e.g.:
     * flight#2.airport#3.code)
     */
    @NotNull public static Option<IndexedWidget> createFromReference(@NotNull final UiModelRetriever retriever, @NotNull final UiModel model,
                                                                     @NotNull final String reference) {
        final ImmutableList<String> items = split(reference, '.');

        final Builder<ItemWidget> builder = builder();
        UiModel                   scope   = model;
        for (final String item : items) {
            final Tuple<String, Option<Integer>> tuple = getItemWidgetTuple(item);
            final String                         name  = tuple.first();
            if (scope == null || !scope.containsElement(name)) return empty();
            final Widget widget = scope.getElement(name);
            builder.add(new ItemWidget(widget, tuple.second()));
            scope = nextScope(retriever, scope, widget);
        }

        final ImmutableList<ItemWidget> parts = builder.build();

        return parts.getFirst().map(first -> {
            final IndexedWidget base = createIndexed(first.widget(), first.item());
            return parts.drop(1).foldLeft(base, IndexedWidget::nested);
        });
    }

    /** Create {@link IndexedWidget indexed widget} for a given widget and item. */
    public static IndexedWidget createIndexed(@NotNull final Widget widget, @NotNull final Option<Integer> item) {
        assertIndexed(widget, item);
        return new IndexedWidget(widget, item);
    }

    /**
     * Qualify {@link IndexedWidget inner indexed widget} with given {@link Widget outer widget} and
     * item (e.g.: qualify(flight, 2, departure.airport) -> flight#2.departure.airport)
     */
    @NotNull @Pure public static IndexedWidget qualify(@NotNull final Widget outer, @NotNull final Option<Integer> item,
                                                       @NotNull final IndexedWidget inner) {
        return inner.flattened().foldLeft(createIndexed(outer, item), IndexedWidget::nested);
    }

    private static void assertIndexed(@NotNull Widget widget, @NotNull Option<Integer> item) {
        final WidgetType widgetType = widget.getWidgetType();
        if (item.isPresent() && widget.getMultiple().isEmpty() && isEmpty(widget.getButtonBoundId()) && widgetType != TABLE && widgetType != MAP)
            throw new IllegalStateException("Attempting to index a widget within no multiple");
    }

    /** Return widget name from part (e.g.: sale#2 -> {sale,some(2)}) */
    private static Tuple<String, Option<Integer>> getItemWidgetTuple(String part) {
        final int hash = part.indexOf('#');
        return hash != -1 ? tuple(part.substring(0, hash), of(Integer.valueOf(part.substring(hash + 1)))) : tuple(part, empty());
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * Tuple of widget and optional item.
     */
    @SuppressWarnings("WeakerAccess")
    public static class ItemWidget {
        @NotNull private final Option<Integer> item;
        @NotNull private final Widget          widget;

        private ItemWidget(@NotNull Widget widget, @NotNull Option<Integer> item) {
            this.item   = item;
            this.widget = widget;
            assertIndexed(widget, item);
        }

        @NotNull public Option<Integer> item() {
            return item;
        }

        @NotNull public LocalWidget toLocalWidget() {
            return new LocalWidget(widget.getName(), item.getOrNull());
        }

        @NotNull public Widget widget() {
            return widget;
        }
    }
}  // end class IndexedWidget
