
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Modifiers for Types.
 */
public enum Modifier {

    //~ Enum constants ...............................................................................................................................

    ABSTRACT(true), REMOTE(true), PROTECTED(true), LOCAL(true), INTERFACE(true), INTERNAL(true), EXTERNAL(true), LISTING(true), PROCESSOR(true),
    IMPORTER(true), LIFECYCLE(true), RUNNABLE(true), FINAL(true), MONITOR(true), EXCEPTION(true), DEPRECABLE, AUDITABLE, REMOTABLE,
    OPTIMISTIC_LOCKING, DEFAULT_SEARCHABLE, DATABASE_SEARCHABLE;

    //~ Instance Fields ..............................................................................................................................

    private boolean prefix;

    //~ Constructors .................................................................................................................................

    Modifier() {}

    Modifier(boolean b) {
        prefix = b;
    }

    //~ Methods ......................................................................................................................................

    /** The Id of the modifier. */
    @NotNull public String getId() {
        return name().toLowerCase();
    }

    /** Returns true if modifiers is a prefix one. */
    public boolean isPrefix() {
        return prefix;
    }

    //~ Methods ......................................................................................................................................

    /** The empty Set of modifiers. */
    public static EnumSet<Modifier> emptySet() {
        return EnumSet.noneOf(Modifier.class);
    }

    /** Return the Modifier for a given id or <code>null</code>. */
    @Nullable public static Modifier fromId(String id) {
        return idToEnum.get(id);
    }

    //~ Static Fields ................................................................................................................................

    public static final EnumSet<Modifier> NONE = EnumSet.noneOf(Modifier.class);

    private static final Map<String, Modifier> idToEnum;

    static {
        idToEnum = new HashMap<>();
        for (final Modifier type : values())
            idToEnum.put(type.getId(), type);
    }
}
