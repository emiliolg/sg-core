
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.code;

import org.jetbrains.annotations.NotNull;

import tekgenesis.type.Kind;

/**
 * The code to execute a constant.
 */
public class Constant implements Code {

    //~ Instance Fields ..............................................................................................................................

    private final Kind   kind;
    private final Object value;

    //~ Constructors .................................................................................................................................

    /**
     * Creates a {@link Constant}.
     *
     * @param  kind   constant kind
     * @param  value  constant value
     */
    public Constant(Kind kind, Object value) {
        this.kind  = kind;
        this.value = value;
    }

    //~ Methods ......................................................................................................................................

    @Override public void bind(Binder binder) {}

    /** If the constant is an String replace the value. */
    public Constant replaceString(@NotNull String str) {
        return value instanceof String ? new Constant(kind, str) : this;
    }

    @Override public String toString() {
        return "Constant(" + value + ")";
    }

    /** Returns true if the value is an String. */
    public boolean isString() {
        return value instanceof String;
    }

    @Override public Instruction getInstruction() {
        return Instruction.PUSH;
    }

    /** Returns the constant Kind. */
    public Kind getKind() {
        return kind;
    }

    /** Returns the constant Value. */
    public Object getValue() {
        return value;
    }
}  // end class Constant
