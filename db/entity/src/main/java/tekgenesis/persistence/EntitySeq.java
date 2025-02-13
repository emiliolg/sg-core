
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;

/**
 * A {@link Seq<EntityInstance>} with some additional methods.
 */
// todo Phantom references
public interface EntitySeq<E extends EntityInstance<E, ?>> extends Seq<E> {

    //~ Methods ......................................................................................................................................

    /** Returns the element at the specified position in this list. */
    E get(int index);

    /** Invalidate the Sequence. */
    void invalidate();

    /** Returns true if the List is undefined. */
    boolean isUndefined();

    //~ Inner Interfaces .............................................................................................................................

    //J-
    /** Creates a new EntitySeq. */
    @NotNull
    static <C extends EntityInstance<C, CK>, CK,
            P extends EntityInstance<P, PK>, PK
            >
    EntitySeq<C> createEntitySeq(@NotNull DbTable<C, CK> table, @NotNull P owner, @NotNull Function<C, EntityRef<P,PK>> getParentRef, ImmutableList<TableField<?>> foreignColumns) {
        return new EntitySeqImpl<>(table, owner, getParentRef, foreignColumns);
    }

    /** Creates a new InnerEntitySeq */
    @NotNull
    static <C extends InnerInstance<C, CK, P, PK>, CK,
            P extends EntityInstance<P, PK>, PK
            >
    InnerEntitySeq<C> createInnerEntitySeq(@NotNull DbTable<C, CK> table, @NotNull P parent, @NotNull Function<C, EntityRef<P,PK>> getParentRef) {
        return new InnerEntitySeqImpl.Base<>(table, parent, getParentRef);
    }
    /** Creates a new InnerEntitySeqForUpdate */
    @NotNull
    static <UC extends C,
            C extends InnerInstance<C, CK, P, PK>, CK,
            P extends EntityInstance<P, PK>, PK
            >
    InnerEntitySeqForUpdate<UC, C> createInnerEntitySeq(Function<C, UC> forUpdate, @NotNull DbTable<C, CK> table, @NotNull P parent, @NotNull Function<C, EntityRef<P,PK>> getParentRef) {
        return new InnerEntitySeqImpl.ForUpdate<>(forUpdate, table, parent, getParentRef);
    }


    //J+

    // Class to identify Inner
    interface Inner<E extends EntityInstance<E, ?>> extends EntitySeq<E> {
        /** Deletes all elements. */
        void deleteAll();
        /** Persist the elements of the sequence. */
        void persist();
    }
}  // end class EntitySeq
