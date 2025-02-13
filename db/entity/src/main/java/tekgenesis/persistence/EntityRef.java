
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.function.Function;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;

import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.core.Option.option;
import static tekgenesis.common.util.Reflection.findFieldOrFail;
import static tekgenesis.common.util.Reflection.getFieldValue;
import static tekgenesis.database.DbConstants.CANNOT_GET_REVERSE_FIELD_VALUE;

/**
 * A reference to Another Entity.
 */
public class EntityRef<T extends EntityInstance<T, K>, K> {

    //~ Instance Fields ..............................................................................................................................

    private final Function<T, EntitySeq<?>> getReverseRef;

    private SoftReference<Option<T>>          referenceHolder;
    @NotNull private final EntityTable<T, K>  table;

    //~ Constructors .................................................................................................................................

    /** Create a reference. */
    public EntityRef(final DbTable<T, K> dbTable) {
        this(dbTable, null);
    }

    /** Create a reference. */
    public EntityRef(@NotNull DbTable<T, K> table, @Nullable Function<T, EntitySeq<?>> getReverseRef) {
        this.table         = table.entityTable();
        this.getReverseRef = getReverseRef;
        referenceHolder    = new SoftReference<>(null);
    }

    //~ Methods ......................................................................................................................................

    /**
     * Returns a reflective based function to Initialize a Parent Reference based on the child class
     * and Field Name.
     */

    //J-
    public static <
            C extends EntityInstance<C, CK>, CK,
            P extends EntityInstance<P, PK>, PK
            >
    Function<C, EntityRef<P,PK>> getRefFunction(Class<C> childClass, String reverseFieldName) {
        final Field refField = findFieldOrFail(childClass, reverseFieldName);
        return c -> ensureNotNull(getFieldValue(c, refField), CANNOT_GET_REVERSE_FIELD_VALUE);
    }
    //J+

    /** Set the reference to the specified EntityInstance. */
    @Contract("!null -> !null; null -> null")
    @Nullable public T initialize(@Nullable T ref) {
        referenceHolder = new SoftReference<>(option(ref));
        return ref;
    }

    /** Invalidate the reference. */
    public synchronized void invalidate() {
        referenceHolder.clear();
    }

    /** Set the reference to the specified EntityInstance. */
    public void set(@Nullable T newRef) {
        final Option<T> r = referenceHolder.get();
        if (r != null && r.getOrNull() == newRef) return;
        synchronized (this) {
            final Option<T> ref = referenceHolder.get();
            if (ref != null && ref.getOrNull() == newRef) return;

            if (getReverseRef != null) {
                if (ref != null && ref.isPresent()) getReverseRef.apply(ref.get()).invalidate();
                if (newRef != null) getReverseRef.apply(newRef).invalidate();
            }
            initialize(newRef);
        }
    }

    /** Solve the reference. */
    @Nullable public T solve(@Nullable K key) {
        final Option<T> r = referenceHolder.get();
        if (r != null) return r.getOrNull();
        synchronized (this) {
            final Option<T> ref = referenceHolder.get();
            return ref != null ? ref.getOrNull() : initialize(key == null ? null : table.find(key));
        }
    }

    /** Solve the reference. Fail if cannot solve it */
    public T solveOrFail(@NotNull K key) {
        final Option<T> r = referenceHolder.get();
        if (r != null && r.isPresent()) return r.get();

        synchronized (this) {
            final Option<T> ref = referenceHolder.get();
            return ref != null && ref.isPresent() ? ref.get() : initialize(table.findOrFail(key));
        }
    }

    /** Returns true if the Reference is undefined. */
    public boolean isUndefined() {
        return referenceHolder.get() == null;
    }
}  // end class EntityRef
