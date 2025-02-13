
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User interaction map. Exists during form life cycle, and support only serializable keys and
 * values.
 */
public interface InteractionMap {

    //~ Methods ......................................................................................................................................

    /** Returns true if this map contains a mapping for the specified key. */
    boolean containsKey(@NotNull final Object key);

    /** Returns a Set of the mappings contained in this map. */
    @Nullable Set<Map.Entry<Object, Object>> entrySet();

    /**
     * Returns the value to which the specified key is mapped, or null if this map contains no
     * mapping for the key.
     */
    @Nullable Object get(@NotNull final Object key);

    /** Returns a Collection of the values contained in this map. */
    @Nullable Set<Object> keySet();

    /**
     * Associates the specified value with the specified key in this map. If the map previously
     * contained a mapping for the key, the old value is replaced by the specified value.
     */
    @Nullable Object put(@NotNull final Object key, @NotNull final Object value);

    /** Removes the mapping for a key from this map if it is present. */
    @Nullable Object remove(@NotNull final Object key);

    /** Returns a Set of the mappings contained in this map. */
    @Nullable Collection<Object> values();
}
