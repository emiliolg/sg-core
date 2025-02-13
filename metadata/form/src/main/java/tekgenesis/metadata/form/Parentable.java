
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.annotation.Pure;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.metadata.form.IndexedWidget.createIndexed;
import static tekgenesis.metadata.form.IndexedWidget.qualify;

/**
 * Defines the ability to traverse {@link Parent parents}.
 */
public interface Parentable<T extends Parentable<T>> {

    //~ Methods ......................................................................................................................................

    /** Return {@link IndexedWidget } {@link UiModel metamodel}. */
    @NotNull default IndexedWidget indexed(@NotNull final Widget widget, @NotNull final Option<Integer> item) {
        return qualified(this, createIndexed(widget, item));
    }

    /** Returns optional {@link Parent<T> parent}. */
    @NotNull Option<? extends Parent<T>> parent();

    /** Return associated {@link UiModel metamodel}. */
    @NotNull UiModel getUiModel();

    //~ Methods ......................................................................................................................................

    /**
     * Qualify {@link IndexedWidget indexed widget} within {@link Parentable<?> container hierarchy}.
     */
    @Pure static IndexedWidget qualified(@NotNull final Parentable<?> c, @NotNull final IndexedWidget w) {
        return c.parent().map(p -> {
                final Widget anchor = p.anchor();
                return qualified(p.value(), qualify(anchor, p.item(), w));
            }).orElse(w);
    }
}
