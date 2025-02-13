
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.model;

import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.annotation.GwtIncompatible;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;

import static tekgenesis.common.core.enumeration.Enumerations.getValuesFor;

/**
 * Metadata model map. Wraps a simple [Object,String] map. Allows object typed map serialization.
 */
public class KeyMap implements Iterable<Map.Entry<Object, String>> {

    //~ Instance Fields ..............................................................................................................................

    private final Map<Object, String> options;

    //~ Constructors .................................................................................................................................

    /** Creates a new FieldOptions. */
    private KeyMap() {
        this(new LinkedHashMap<>());
    }

    /** Creates a new FieldOptions based on a given map. */
    private KeyMap(@NotNull final Map<Object, String> options) {
        this.options = options;
    }

    //~ Methods ......................................................................................................................................

    /** Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(final Object key) {
        return options.containsKey(key);
    }

    /** Returns true if this map contains a mapping for the specified value. */
    public boolean containsValue(final String value) {
        return options.containsValue(value);
    }

    /** Returns a set view of the options contained in this map. */
    public Set<Map.Entry<Object, String>> entrySet() {
        return options.entrySet();
    }

    /** Get key associated value (or null). */
    public String get(final Object key) {
        return options.get(key);
    }

    @Override public Iterator<Map.Entry<Object, String>> iterator() {
        return entrySet().iterator();
    }

    /** Returns a set view of the option keys contained in this map. */
    public Set<Object> keySet() {
        return options.keySet();
    }

    /** Put given key,value pair option. */
    @SuppressWarnings("UnusedReturnValue")
    public Object put(@NotNull final Object key, @Nullable final String value) {
        return options.put(key, value);
    }

    /** Put all key,value pairs on given map. */
    @SuppressWarnings("NonJREEmulationClassesInClientCode")
    public void putAll(@NotNull final KeyMap other) {
        options.putAll(other.options);
    }

    /** Serialize map to writer. */
    public void serialize(StreamWriter w) {
        w.writeObject(options);
    }

    /** Returns the options size. */
    public int size() {
        return options.size();
    }

    /** Returns a collection view of the option values contained in this map. */
    public Collection<String> values() {
        return options.values();
    }

    /** Returns true if options are empty. */
    public boolean isEmpty() {
        return options.isEmpty();
    }

    //~ Methods ......................................................................................................................................

    /** Creates an empty FieldOptions. */
    public static KeyMap create() {
        return new KeyMap();
    }

    /** Create a FieldOptions based on a given map. */
    public static KeyMap create(@NotNull Map<Object, String> options) {
        return new KeyMap(options);
    }

    /** Create a KeyMap from an Enum. */
    @GwtIncompatible public static KeyMap fromEnum(final String enumClassName) {
        final KeyMap options = new KeyMap();
        for (final Enumeration<?, ?> enumeration : getValuesFor(enumClassName))
            options.put(enumeration.name(), enumeration.label());
        return options;
    }

    /** Create a KeyMap for an Iterable. */
    public static KeyMap fromValues(final Iterable<String> values) {
        final KeyMap map = new KeyMap();
        for (final Object value : values)
            map.put(value, value.toString());
        return map;
    }

    /** Instantiate map from reader. */
    @SuppressWarnings("unchecked")
    public static KeyMap instantiate(StreamReader r) {
        return create((Map<Object, String>) r.readObject());
    }

    /** Create a FieldOptions based on the given single option. */
    public static KeyMap singleton(@NotNull Tuple<?, String> option) {
        final KeyMap result = new KeyMap();
        result.put(option.first(), option.second());
        return result;
    }

    //~ Static Fields ................................................................................................................................

    public static final KeyMap EMPTY = new KeyMap(Collections.emptyMap());
}  // end class KeyMap
