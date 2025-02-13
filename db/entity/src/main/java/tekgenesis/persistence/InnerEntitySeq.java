
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
public interface InnerEntitySeq<E extends InnerInstance<E, ?, ?, ?>> extends EntitySeq.Inner<E> {

    //~ Methods ......................................................................................................................................

    /** Create a new Element and add it to the sequence. */
    @NotNull E add();

    /** Merge the Instances in the Inner with the new ones. Matching sequentially */
    <S> InnerEntitySeq<E> merge(Iterable<S> newValues, BiConsumer<E, S> action);

    /**
     * Merge the Instances in the Inner with the new ones, matching using the specified Predicate.
     * Non matching values will be removed. And new values will be added for each item in newValues
     * with no match; after creation, action consumer will be invoked.
     *
     * @param   newValues       The values we will match against
     * @param   matchPredicate  How to match current values against new ones. This will be tried in
     *                          sequence
     * @param   action          What to do when a match is detected
     *
     * @return  the merged sequence
     */
    // #simpleMerge
    default <S> InnerEntitySeq<E> mergeMatching(Iterable<S> newValues, BiPredicate<E, S> matchPredicate, BiConsumer<E, S> action) {
        return mergeMatching(newValues, matchPredicate, action, alwaysTrue(), (eSupplier, s) -> action.accept(eSupplier.get(), s));
    }
    // #simpleMerge
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
    // #fullMerge
    <S> InnerEntitySeq<E> mergeMatching(Iterable<S> newValues, BiPredicate<E, S> matchPredicate, BiConsumer<E, S> matchAction,
                                        Predicate<E> deleteAction, BiConsumer<Supplier<E>, S> createAction);
    // #fullMerge

    /**
     * Update the Instances in the Inner with the new ones, matching using the specified Predicate.
     */
    default <S> InnerEntitySeq<E> updateMatching(Iterable<S> newValues, BiPredicate<E, S> matchPredicate, BiConsumer<E, S> action) {
        return mergeMatching(newValues, matchPredicate, action, alwaysFalse(), (eSupplier, s) -> {});
    }
}  // end class InnerEntitySeq
