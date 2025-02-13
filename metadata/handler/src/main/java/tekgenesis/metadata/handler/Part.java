
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.handler;

import org.jetbrains.annotations.NotNull;

import tekgenesis.field.TypeField;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

/**
 * Section which, when combined with others, makes up the whole of a route.
 */
public class Part extends TypeField {

    //~ Instance Fields ..............................................................................................................................

    private final boolean dynamic;
    private final boolean wildcard;

    //~ Constructors .................................................................................................................................

    private Part(@NotNull String text, @NotNull Type type, boolean dynamic, boolean wildcard) {
        super(text, type);
        this.dynamic  = dynamic;
        this.wildcard = wildcard;
    }

    //~ Methods ......................................................................................................................................

    @Override public String toString() {
        return (!isStatic() ? "$" : "") + getName() + (isWildcard() ? "*" : "");
    }

    /** Return true if part is dynamic. */
    public boolean isDynamic() {
        return dynamic;
    }

    /** Return true if part is static. */
    public boolean isStatic() {
        return !dynamic && !wildcard;
    }

    /**
     * All dynamic parts must be signed, because there's no way to specified signed option from mm
     * definition.
     */
    @Override public boolean isSigned() {
        return true;
    }

    /** Return true if part is wildcard. */
    public boolean isWildcard() {
        return wildcard;
    }

    //~ Methods ......................................................................................................................................

    /** Create dynamic part. */
    static Part createDynamic(@NotNull String text, @NotNull Type type) {
        return new Part(text, type, true, false);
    }

    /** Create static part. */
    static Part createStatic(@NotNull String text) {
        return new Part(text, Types.nullType(), false, false);
    }

    /** Create wildcard part. */
    static Part createWildcard(@NotNull String text) {
        return new Part(text, Types.stringType(), false, true);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -7286090883229062015L;
}  // end class Part
