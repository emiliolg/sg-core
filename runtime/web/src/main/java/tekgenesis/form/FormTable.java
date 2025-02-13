
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
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;

/**
 * Represents the model of a Table, to access and modify a table.
 */
public interface FormTable<T> extends Seq<T> {

    //~ Methods ......................................................................................................................................

    /** Return a new table field row, and adds it at the end of the table. */
    @NotNull T add();

    /** Return a new table field row, and inserts it at the specified index. */
    @NotNull T add(int index);

    /** Removes all the rows from the table. */
    void clear();

    /** Return the table field current row. */
    @NotNull Option<T> current();

    /** Return the specified table field associated row. */
    @NotNull T get(int index);

    /** Returns the index of a given row. */
    @NotNull Integer indexOf(T row);

    /** Merge table rows into given element list (sequentially). */
    default <V> void mergeInto(@NotNull final List<V> elements, @NotNull final BiConsumer<T, V> action, @NotNull final Supplier<V> create) {
        int       i    = 0;
        final int size = elements.size();
        for (int j = 0; j < size(); j++) {
            final T row = get(j);
            if (i >= size) elements.add(create.get());
            action.accept(row, elements.get(i++));
        }
        // Clear remaining
        for (int j = size - 1; j >= i; j--)
            elements.remove(j);
    }

    /** Populate new rows for each given element. */
    default <V> void populate(@NotNull final Iterable<V> elements, @NotNull final BiConsumer<T, V> populate) {
        elements.forEach(element -> populate.accept(add(), element));
    }

    /** Return an option of the table field previous row. */
    @NotNull Option<T> previous();

    /** Remove a table field specified row. */
    void remove(int index);

    /**
     * Removes a given row. Throws IllegalStateException if the row doesn't exist inside the table
     */
    void remove(T row);

    /** Remove the currently active row. */
    void removeCurrent();

    /** Returns the size of the table. */
    int size();

    /** Sorts the elements with the given Comparator. */
    void sort(Comparator<T> c);

    /** Swaps the elements at the specified positions. */
    void swap(int i, int j);

    /** Return the table field current row. */
    @NotNull T getCurrent();

    /** Return the table field current row index. */
    Option<Integer> getCurrentIndex();

    /** Returns the current page of the table. */
    int getCurrentPage();

    /** Sets the current page of the table. */
    void setCurrentPage(int page);

    /** Return the table field previous current row. */
    @NotNull T getPrevious();

    /** Return the table field previous row index. */
    @NotNull Option<Integer> getPreviousIndex();

    /** Returns true if the table is empty. */
    boolean isEmpty();
}  // end interface FormTable
