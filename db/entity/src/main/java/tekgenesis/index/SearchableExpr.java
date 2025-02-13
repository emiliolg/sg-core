
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.index;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.annotation.Pure;

import static tekgenesis.common.collections.ImmutableList.fromArray;
import static tekgenesis.index.IndexSearcher.Container.fromIterable;

/**
 * Represents a filter field. SearchableExpr
 */
public interface SearchableExpr {

    //~ Instance Fields ..............................................................................................................................

    SearchableExpr EMPTY_EXPR = new SearchableExpr() {
            @NotNull @Override public SearchableExpr also(@NotNull final SearchableExpr other) {
                return other;
            }
            @NotNull @Override public SearchableExpr and(@NotNull final SearchableExpr other) {
                return other.must();
            }
            @NotNull @Override public SearchableExpr boost(float boost) {
                return this;
            }
            @NotNull @Override public SearchableExpr must() {
                return this;
            }
            @NotNull @Override public SearchableExpr not() {
                return this;
            }
        };

    //~ Methods ......................................................................................................................................

    /**
     * Makes a container with the left side as a SHOULD and the right side as a SHOULD, unless any
     * of the side has the occurrence overriden.
     *
     * <p>Field.also(Field) -> returns a container with both fields. Field.also(Container) ->
     * returns a container with both expressions in the same level (Field, Container).
     * Container.also(Container) -> returns a container with both containers (Container, Container).
     * Container.also(Field) -> returns the container adding the same field as a MUST clause in the
     * same level as the container expressions (ContainerField1, ContainerField2, ..., Field).</p>
     */
    @NotNull @Pure SearchableExpr also(@NotNull final SearchableExpr other);

    /**
     * Makes a container with the left side as a MUST and the right side as a MUST, unless any of
     * the side has the occurrence overriden.
     *
     * <p>Field.and(Field) -> returns a container with both fields. Field.and(Container) -> returns
     * a container with both expressions in the same level (Field, Container).
     * Container.and(Container) -> returns a container with both containers (Container, Container).
     * Container.and(Field) -> returns the container adding the same field as a MUST clause in the
     * same level as the container expressions (ContainerField1, ContainerField2, ..., Field).</p>
     */
    @NotNull @Pure SearchableExpr and(@NotNull final SearchableExpr other);

    /** Sets a default boost of 2 over the expression. */
    @NotNull default SearchableExpr boost() {
        return boost(2);
    }

    /**
     * Sets a boost over the expression. This means that the score given by this clause will be
     * multiplied times the boost factor.
     */
    @NotNull SearchableExpr boost(final float boost);

    /** Sets searchable expression as a MUST clause. */
    @NotNull SearchableExpr must();

    /** Sets searchable expression as a MUST_NOT clause. */
    @NotNull SearchableExpr not();

    //~ Methods ......................................................................................................................................

    /** Not empty and container. Sets SHOULD occur in every not overriden occur expression. */
    @NotNull static SearchableExpr also(@NotNull SearchableExpr first, @NotNull final SearchableExpr... exprs) {
        return fromIterable(fromArray(exprs), first);
    }

    /** Not empty and container. Sets MUST occur in every not overriden occur expression. */
    @NotNull static SearchableExpr and(@NotNull final SearchableExpr first, @NotNull final SearchableExpr... exprs) {
        return fromIterable(fromArray(exprs).map(SearchableExpr::must), first.must());
    }

    //~ Inner Interfaces .............................................................................................................................

    interface FieldExpr extends SearchableExpr {
        @NotNull @Override default FieldExpr boost() {
            return boost(2);
        }
        @NotNull @Override FieldExpr boost(final float boost);

        /** Set query as fuzzy with default max edits = 1. */
        @NotNull default FieldExpr fuzzy() {
            return fuzzy(1);
        }

        /**
         * Sets this query as fuzzy. This means that it generates all possible matching terms that
         * are within the maximum edit distance (int between 0 and 2).
         */
        @NotNull FieldExpr fuzzy(int fuzziness);
        @NotNull @Override FieldExpr must();
        @NotNull @Override FieldExpr not();
    }
}  // end interface SearchableExpr
