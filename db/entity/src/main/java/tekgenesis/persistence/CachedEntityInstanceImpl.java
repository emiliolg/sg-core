
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
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.unreachable;

/**
 * Cached EntityInstance Implementation.
 */
public abstract class CachedEntityInstanceImpl<This extends EntityInstance<This, K>, K> extends EntityInstanceBaseImpl<This, K> {

    //~ Instance Fields ..............................................................................................................................

    private final Function<CachedEntityInstanceImpl<This, K>, AbstractData<This, K>> getData;
    private final BiConsumer<CachedEntityInstanceImpl<This, K>, AbstractData<This, K>> setData;

    //~ Constructors .................................................................................................................................

    protected CachedEntityInstanceImpl(Function<CachedEntityInstanceImpl<This, K>, AbstractData<This, K>>   getData,
                                       BiConsumer<CachedEntityInstanceImpl<This, K>, AbstractData<This, K>> setData) {
        this.getData = getData;
        this.setData = setData;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public String keyAsString() {
        return _data().keyAsString();
    }

    @NotNull @Override public K keyObject() {
        return _data().keyObject();
    }

    @Override public boolean modified() {
        return _data().modified;
    }

    protected AbstractData<This, K> data() {
        final AbstractData<This, K> d = _data();
        if (d.cached) return d;
        if (d.hasEmptyKey() || d.modified) {
            d.onLoad(cast(this));
            return d;
        }
        return cacheAndLoad();
    }

    protected void markAsModified(AbstractData<This, K> d) {
        if (!d.modified) {
            synchronized (this) {
                _data().markAsModified(this);
            }
        }
    }

    @NotNull AbstractData<This, K> _data() {
        return getData.apply(this);
    }

    @Override void resetModified() {
        _data().resetModified();
    }
    private synchronized AbstractData<This, K> cacheAndLoad() {
        final AbstractData<This, K> d = _data();
        if (d.cached) return d;

        final This cached = table().entityTable().getStoreHandler().cache(cast(this), true);
        setData.accept(this, dataField(cached));
        return _data();
    }

    //~ Methods ......................................................................................................................................

    /** Package private method to expose data field. */
    static <T extends EntityInstance<T, K>, K> AbstractData<T, K> dataField(@NotNull T instance) {
        final CachedEntityInstanceImpl<T, K> base = cast(instance);
        return base._data();
    }

    //~ Inner Classes ................................................................................................................................

    public abstract static class AbstractData<I extends EntityInstance<I, K>, K> implements Cloneable {
        boolean         modified = false;
        private boolean cached   = false;

        @Override public boolean equals(final Object o) {
            if (!getClass().isInstance(o)) return false;
            final AbstractData<I, K> that = cast(o);
            return keyObject().equals(that.keyObject());
        }
        @Override public int hashCode() {
            return keyObject().hashCode();
        }

        /** Initialize data with instance. */
        @SuppressWarnings("EmptyMethod")
        public void onLoad(final I instance) {}

        protected boolean hasEmptyKey() {
            return false;
        }
        protected abstract String keyAsString();
        protected abstract K keyObject();

        void markAsModified(CachedEntityInstanceImpl<I, K> instance) {
            if (!cached || modified) {
                modified = true;
                return;
            }

            try {
                final AbstractData<I, K> dirtyData = cast(clone());
                dirtyData.modified = true;
                dirtyData.cached   = false;
                final EntityTable<I, K> et   = instance.table().entityTable();
                final I                 self = cast(instance);
                et.getStoreHandler().cache(self, false);
                instance.setData.accept(instance, dirtyData);
            }
            catch (final CloneNotSupportedException e) {
                throw unreachable();
            }
        }
        void resetModified() {
            modified = false;
        }

        void setCached(final boolean b) {
            if (b == cached) return;
            cached = b;
        }
    }  // end class AbstractData
}  // end class CachedEntityInstanceImpl
