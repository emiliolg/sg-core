
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.check;

import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;

/**
 * Validation Types.
 */
@SuppressWarnings("ALL")
public enum CheckType {

    //~ Enum constants ...............................................................................................................................

    ERROR("error"), WARNING("warning"), INFO("info"), SUCCESS("success"), PLAIN("plain"), TITLE("title"), ENTITY("entity");

    //~ Instance Fields ..............................................................................................................................

    private final String decoration;

    //~ Constructors .................................................................................................................................

    CheckType(String decoration) {
        this.decoration = decoration;
    }

    //~ Methods ......................................................................................................................................

    /** Serialize to a Stream. */
    public void serialize(StreamWriter w) {
        w.writeInt(ordinal());
    }

    /**
     * Creates Style class name.
     *
     * @param  supportsError
     */
    public String getDecorationClass(boolean supportsError) {
        return supportsError ? decoration : decoration.equals("error") ? "danger" : decoration;
    }

    //~ Methods ......................................................................................................................................

    /** Instantiate from an Stream. */
    public static CheckType instantiate(StreamReader r) {
        return VALUES[r.readInt()];
    }

    //~ Static Fields ................................................................................................................................

    private static final CheckType[] VALUES = values();
}
