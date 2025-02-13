
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import static tekgenesis.common.core.Predicates.alwaysFalse;
import static tekgenesis.common.core.Predicates.alwaysTrue;

/**
 * An {@link EntitySeq} with additional methods to deal with inner entities.
 */
public interface InnerEntitySeqForUpdate<UE extends E, E extends InnerInstance<E, ?, ?, ?>> extends EntitySeq.Inner<E> {

    //~ Methods ......................................................................................................................................

    /** Create a new Element and add it to the sequence. */
    @NotNull UE add();

    /** Merge the Instances in the Inner with the new ones. Matching sequentially */
    <S> InnerEntitySeqForUpdate<UE, E> merge(Iterable<S> newValues, BiConsumer<UE, S> action);

    /**
     * Merge the Instances in the Inner with the new ones, matching using the specified Predicate.
     */
    default <S> InnerEntitySeqForUpdate<UE, E> mergeMatching(Iterable<S> newValues, BiPredicate<E, S> matchPredicate, BiConsumer<UE, S> action) {
        return mergeMatching(newValues, matchPredicate, action, alwaysTrue(), (eSupplier, s) -> action.accept(eSupplier.get(), s));
    }

    /**
     * 3-way Merge the Instances in the Inner with the new ones, matching using the specified
     * Predicate.
     *
     * @param  newValues       The values we will match against
     * @param  matchPredicate  How to match current values against new ones. This will be tried in
     *                         sequence
     * @param  matchAction     What to do when a match is detected
     * @param  deleteAction    A predicate that will receive a non matching item. Returning true
     *                         will remove the item
     * @param  createAction    This will be called for new items that are not present in the current
     *                         set. A Supplier to create new instances is passed as a parameter.
     */
    <S> InnerEntitySeqForUpdate<UE, E> mergeMatching(Iterable<S> newValues, BiPredicate<E, S> matchPredicate, BiConsumer<UE, S> matchAction,
                                                     Predicate<UE> deleteAction, BiConsumer<Supplier<UE>, S> createAction);

    /**
     * Update the Instances in the Inner with the new ones, matching using the specified Predicate.
     */
    default <S> InnerEntitySeqForUpdate<UE, E> updateMatching(Iterable<S> newValues, BiPredicate<E, S> matchPredicate, BiConsumer<UE, S> action) {
        return mergeMatching(newValues, matchPredicate, action, alwaysFalse(), (eSupplier, s) -> {});
    }
}  // end interface InnerEntitySeqForUpdate
