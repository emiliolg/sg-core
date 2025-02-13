
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form;

import java.io.Serializable;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.metadata.form.widget.ButtonType;

import static tekgenesis.metadata.form.widget.ButtonType.*;

/**
 * Create an object to represent the qualified source of an event.
 */
@SuppressWarnings("FieldMayBeFinal")  // gwt serialization
public class SourceWidget implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private String path;

    //~ Constructors .................................................................................................................................

    private SourceWidget() {
        path = null;
    }

    /** Create qualified source of an event. */
    public SourceWidget(final String path) {
        this.path = path;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SourceWidget)) return false;
        final SourceWidget that = (SourceWidget) o;
        return path != null ? path.equals(that.path) : that.path == null;
    }

    @Override public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }

    @Override public String toString() {
        return path != null ? path : "<none>";
    }

    /** Returns the source widget path. */
    public String getPath() {
        return path;
    }

    //~ Methods ......................................................................................................................................

    /** Button types that support table discovery. */
    public static boolean buttonTypeSupportsTableDiscovery(ButtonType buttonType) {
        return buttonType == ADD_ROW || buttonType == REMOVE_ROW || buttonType == EXPORT;
    }

    /** SourceWidget de serialization. */
    @Nullable public static SourceWidget deserialize(StreamReader r) {
        if (!r.readBoolean()) return null;

        final String path = r.readString();
        return new SourceWidget(path);
    }

    /** SourceWidget serialization. */
    public static void serialize(StreamWriter w, SourceWidget s) {
        if (s == null) w.writeBoolean(false);
        else {
            w.writeBoolean(true);
            w.writeString(s.getPath());
        }
    }  // end method serialize

    //~ Static Fields ................................................................................................................................

    public static SourceWidget NONE = new SourceWidget();

    private static final long serialVersionUID = -3624910240381738469L;
}  // end class SourceWidget
