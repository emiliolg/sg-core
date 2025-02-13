
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.check;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;

/**
 * CheckMsg class containing validation String and type.
 */
@SuppressWarnings("FieldMayBeFinal")
public class CheckMsg implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private boolean   inline;
    private String    text;
    private CheckType type;

    //~ Constructors .................................................................................................................................

    /** Default contructor. */
    public CheckMsg() {
        inline = false;
        text   = null;
        type   = null;
    }

    /** Default constructor. */
    public CheckMsg(@NotNull final String text) {
        this(false, CheckType.ERROR, text);
    }

    /** Specialized constructor. */
    public CheckMsg(final boolean inline, @NotNull final CheckType type, @NotNull final String text) {
        this.inline = inline;
        this.type   = type;
        this.text   = text;
    }

    //~ Methods ......................................................................................................................................

    /** Serialize to a Stream. */
    public void serialize(StreamWriter w) {
        w.writeBoolean(inline);
        type.serialize(w);
        w.writeString(text);
    }

    @Override public String toString() {
        return (inline ? "inline " : "") + (isError() ? "" : type.toString().toLowerCase() + " ") + "\"" + text + "\"";
    }

    /** Return a new CheckMsg changing the text of the message. */
    public CheckMsg withText(String message) {
        return new CheckMsg(inline, type, message);
    }

    /** Returns true if Inline. */
    public boolean isInline() {
        return inline;
    }

    /** Returns true if error. */
    public boolean isError() {
        return type == CheckType.ERROR;
    }

    /** Returns the message. */
    public String getText() {
        return text;
    }

    /** Returns the Message Type. */
    public CheckType getType() {
        return type;
    }

    //~ Methods ......................................................................................................................................

    /** Instantiate from an Stream. */
    public static CheckMsg instantiate(StreamReader r) {
        return new CheckMsg(r.readBoolean(), CheckType.instantiate(r), r.readString());
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -8606738121915554212L;
}  // end class CheckMsg
