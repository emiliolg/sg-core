
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
import java.util.List;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableIterator;
import tekgenesis.common.collections.ImmutableList;

import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.core.Tuple.asList;
import static tekgenesis.database.DbConstants.UNDEFINED;
import static tekgenesis.persistence.Criteria.allOf;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.persistence.expr.Expr.constant;

/**
 * Implementation of an InnerEntitySeq.
 */
public final class EntitySeqImpl<C extends EntityInstance<C, K>, K, P extends EntityInstance<P, PK>, PK> implements EntitySeq<C> {

    //~ Instance Fields ..............................................................................................................................

    private final List<C>                       current;
    private final Function<C, EntityRef<P, PK>> getParentRef;
    private final ImmutableList<TableField<?>>  keyFields;

    private final P                                owner;
    @NotNull private final transient DbTable<C, K> table;
    private boolean                                unDefined = true;

    //~ Constructors .................................................................................................................................

    EntitySeqImpl(@NotNull DbTable<C, K> table, P owner, @NotNull Function<C, EntityRef<P, PK>> getParentRef,
                  ImmutableList<TableField<?>> foreignColumns) {
        current           = new ArrayList<>();
        unDefined         = true;
        this.table        = table;
        this.owner        = owner;
        keyFields         = foreignColumns;
        this.getParentRef = getParentRef;
    }

    //~ Methods ......................................................................................................................................

    @Override public C get(final int index) {
        return getCurrent().get(index);
    }

    public void invalidate() {
        unDefined = true;
    }

    @NotNull @Override public ImmutableIterator<C> iterator() {
        return immutable(getCurrent().iterator());
    }

    @Override public int size() {
        return getCurrent().size();
    }

    @Override public String toString() {
        return unDefined ? UNDEFINED : Colls.mkString(getCurrent());
    }

    @Override public boolean isUndefined() {
        return unDefined;
    }

    @Override public boolean isEmpty() {
        return getCurrent().isEmpty();
    }

    private synchronized void solve() {
        if (unDefined) {
            current.clear();
            final Criteria p = allOf(keyFields.zipWith((a, b) -> a.eq(constant(b)), asList(owner.keyObject())));
            selectFrom(table).where(p).forEach(c -> {
                getParentRef.apply(c).initialize(owner);
                current.add(c);
            });
        }
        unDefined = false;
    }

    private List<C> getCurrent() {
        if (unDefined) solve();
        return current;
    }
}  // end class EntitySeqImpl
