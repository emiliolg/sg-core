
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableIterator;
import tekgenesis.common.core.Tuple;

import static java.util.Comparator.comparingInt;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.collections.Colls.indexOf;
import static tekgenesis.common.core.Tuple.createAppending;
import static tekgenesis.database.DbConstants.UNDEFINED;
import static tekgenesis.persistence.Sql.selectFrom;

/**
 * Implementation of an InnerEntitySeq.
 */
public class InnerEntitySeqImpl<UC extends C, C extends InnerInstance<C, CK, P, PK>, CK, P extends EntityInstance<P, PK>, PK>
    implements EntitySeq.Inner<C>
{

    //~ Instance Fields ..............................................................................................................................

    private final List<UC> current;

    private final DbTable<C, CK> dbTable;

    @NotNull private final InnerEntityTable<C, CK, P, PK> et;
    private final Function<C, UC>                         forUpdate;
    private final Function<C, EntityRef<P, PK>>           getParentRef;

    @NotNull private List<UC> original;

    @NotNull private final P parent;
    private boolean          unDefined = true;

    //~ Constructors .................................................................................................................................

    InnerEntitySeqImpl(Function<C, UC> forUpdate, DbTable<C, CK> dbTable, @NotNull P parent, @NotNull Function<C, EntityRef<P, PK>> getParentRef) {
        current           = new ArrayList<>();
        unDefined         = true;
        this.getParentRef = getParentRef;
        et                = cast(dbTable.entityTable());
        this.dbTable      = dbTable;
        this.parent       = parent;
        original          = current;
        this.forUpdate    = forUpdate;
    }

    //~ Methods ......................................................................................................................................

    @Override public synchronized void deleteAll() {
        if (unDefined) solve();
        for (final C e : original)
            et.deleteInternal(e, true);
        current.clear();
        attachCurrent();
    }

    /** Returns the element at the specified position in this list. */
    public C get(final int index) {
        return getCurrent().get(index);
    }

    @Override public void invalidate() {
        unDefined = true;
    }

    @NotNull @Override public ImmutableIterator<C> iterator() {
        return cast(immutable(getCurrent().iterator()));
    }

    /** Inserts or Updates updated elements and deletes the elements which were not updated. */
    public synchronized void persist() {
        if (unDefined) return;
        if (getCurrent() == original) {
            updateModified();
            return;
        }
        // Delete removed records
        for (final UC orig : original) {
            if (!current.contains(orig)) et.deleteInternal(orig, true);
        }
        // Persist in database
        for (final UC t : current) {
            if (!original.contains(t)) {
                updateKey(t, t.seqId());
                et.doInsert(t, false);
            }
            else et.update(t);
        }
        attachCurrent();
    }  // end method persist

    public int size() {
        return getCurrent().size();
    }

    //
    public String toString() {
        return unDefined ? UNDEFINED : Colls.mkString(getCurrent());
    }

    public boolean isUndefined() {
        return unDefined;
    }

    @Override public boolean isEmpty() {
        return getCurrent().isEmpty();
    }

    @NotNull synchronized UC add() {
        solve();
        detachCurrent();
        return createNew();
    }
    /** Implementation of delete when inner seq is dirty. */
    C delete(C instance) {
        et.deleteInternal(instance, true);
        final int seqId = instance.seqId();
        original.removeIf(c -> c.seqId() == seqId);
        if (original != current) current.removeIf(c -> c.seqId() == seqId);

        return instance;
    }
    <S> void merge3(Iterable<S> newValues, BiPredicate<C, S> matchPredicate, BiConsumer<UC, S> matchAction, Predicate<UC> deleteAction,
                    BiConsumer<Supplier<UC>, S> createAction) {
        solve();
        // Create copy of newValues
        final ArrayList<S> values = Colls.into(newValues, new ArrayList<>());

        // Backup original ones
        detachCurrent();

        for (final Iterator<UC> iterator = current.iterator(); iterator.hasNext();) {
            final UC element = iterator.next();
            final S  s       = findMatchingElement(values, element, matchPredicate);
            if (s != null) matchAction.accept(element, s);
            else if (deleteAction.test(element)) iterator.remove();
        }
        // Insert remaining ones
        for (final S v : values)
            createAction.accept(this::createNew, v);
    }
    synchronized <S> void mergeSequentially(Iterable<S> newValues, BiConsumer<UC, S> action) {
        solve();
        // Backup original ones
        detachCurrent();

        int       i    = 0;
        final int size = current.size();
        for (final S v : newValues) {
            if (i >= size) createNew();
            action.accept(current.get(i++), v);
        }
        // Clean remaining
        for (int j = size - 1; j >= i; j--)
            current.remove(j);
    }

    /** Implementation of persist when inner seq is dirty. */
    UC persist(UC instance, boolean failIfExists) {
        final int index = indexOf(original, c -> c.seqId() == instance.seqId());

        if (index != -1) {
            if (failIfExists) throw new IllegalStateException("Inserting existing record");
            et.update(instance);
            updateCurrent(instance);
        }
        else {
            et.doInsert(instance, false);
            if (current == original) invalidate();
            else {
                updateCurrent(instance);
                original.add(instance);
            }
        }
        return instance;
    }  // end method persist

    synchronized void solve() {
        if (unDefined) {
            current.clear();
            // noinspection Convert2MethodRef  (If not we have problems with javac)
            for (final UC element :
                 selectFrom(dbTable)                                                           //
                 .where(dbTable.metadata().buildKeyCriteria(Tuple.asList(parent.keyObject())))  //
                 .sorted(comparingInt(c -> c.seqId())).map(forUpdate))
            {
                getParentRef.apply(element).initialize(parent);
                current.add(element);
            }
            attachCurrent();
            unDefined = false;
        }
    }                                                                                          // end method solve

    private void attachCurrent() {
        original = current;
    }

    private UC createNew() {
        final UC element = forUpdate.apply(dbTable.metadata().createInstance());

        final int last = current.size() - 1;
        updateKey(element, last == -1 ? 1 : current.get(last).seqId() + 1);
        getParentRef.apply(element).initialize(parent);
        current.add(element);
        return element;
    }

    private List<UC> detachCurrent() {
        if (current == original) original = new ArrayList<>(original);
        return current;
    }

    @Nullable private <S> S findMatchingElement(ArrayList<S> values, C element, BiPredicate<C, S> matchPredicate) {
        for (final Iterator<S> iterator = values.iterator(); iterator.hasNext();) {
            final S value = iterator.next();
            if (matchPredicate.test(element, value)) {
                iterator.remove();
                return value;
            }
        }
        return null;
    }

    private void updateCurrent(UC instance) {
        final int currentIndex = indexOf(current, c -> c.seqId() == instance.seqId());
        if (currentIndex != -1) current.set(currentIndex, instance);
        else current.add(instance);
    }

    private void updateKey(C element, int seqId) {
        et.getMetadata().setKey(element, createAppending(parent.keyObject(), seqId));
    }

    private void updateModified() {
        for (final C t : current) {
            if (t.modified()) et.update(t);
        }
    }

    private List<UC> getCurrent() {
        if (unDefined) solve();
        return current;
    }

    //~ Inner Classes ................................................................................................................................

    static class Base<C extends InnerInstance<C, CK, P, PK>, CK, P extends EntityInstance<P, PK>, PK> extends InnerEntitySeqImpl<C, C, CK, P, PK>
        implements InnerEntitySeq<C>
    {
        Base(DbTable<C, CK> dbTable, @NotNull P parent, @NotNull Function<C, EntityRef<P, PK>> getParentRef) {
            super(Function.identity(), dbTable, parent, getParentRef);
        }

        @NotNull @Override public C add() {
            return super.add();
        }

        @Override public <S> InnerEntitySeq<C> merge(Iterable<S> newValues, BiConsumer<C, S> action) {
            mergeSequentially(newValues, action);
            return this;
        }

        @Override public <S> InnerEntitySeq<C> mergeMatching(Iterable<S> newValues, BiPredicate<C, S> matchPredicate, BiConsumer<C, S> matchAction,
                                                             Predicate<C> deleteAction, BiConsumer<Supplier<C>, S> createAction) {
            merge3(newValues, matchPredicate, matchAction, deleteAction, createAction);
            return this;
        }
    }

    static class ForUpdate<UC extends C, C extends InnerInstance<C, CK, P, PK>, CK, P extends EntityInstance<P, PK>, PK>
        extends InnerEntitySeqImpl<UC, C, CK, P, PK> implements InnerEntitySeqForUpdate<UC, C>
    {
        ForUpdate(Function<C, UC> forUpdate, DbTable<C, CK> dbTable, @NotNull P parent, @NotNull Function<C, EntityRef<P, PK>> getParentRef) {
            super(forUpdate, dbTable, parent, getParentRef);
        }

        @NotNull @Override public UC add() {
            return super.add();
        }

        @Override public <S> InnerEntitySeqForUpdate<UC, C> merge(Iterable<S> newValues, BiConsumer<UC, S> action) {
            mergeSequentially(newValues, action);
            return this;
        }

        @Override public <S> InnerEntitySeqForUpdate<UC, C> mergeMatching(Iterable<S> newValues, BiPredicate<C, S> matchPredicate,
                                                                          BiConsumer<UC, S> matchAction, Predicate<UC> deleteAction,
                                                                          BiConsumer<Supplier<UC>, S> createAction) {
            merge3(newValues, matchPredicate, matchAction, deleteAction, createAction);
            return this;
        }
    }
}  // end class InnerEntitySeqImpl
