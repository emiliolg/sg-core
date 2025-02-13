
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.expr;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.Predefined;
import tekgenesis.expr.visitor.ExpressionVisitor;
import tekgenesis.type.EnumType;
import tekgenesis.type.EnumValue;
import tekgenesis.type.Type;

import static tekgenesis.common.Predefined.equal;

/**
 * References an operand.
 */
public class Ref extends ExpressionAST implements Comparable<Ref> {

    //~ Instance Fields ..............................................................................................................................

    private boolean col;

    private final String ref;

    //~ Constructors .................................................................................................................................

    protected Ref(String ref) {
        this.ref = ref;
    }

    //~ Methods ......................................................................................................................................

    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public void acceptOperands(ExpressionVisitor<?> visitor) {  /* Do Nothing */
    }

    @Override public int compareTo(@NotNull Ref r) {
        return getName().compareTo(r.getName());
    }

    @Override public boolean equals(Object o) {
        return o instanceof Ref && equal(((Ref) o).ref, ref);
    }

    @Override public int hashCode() {
        return Predefined.hashCodeAll(ref);
    }

    /** Set's if it's referencing a column. */
    public void setCol(boolean col) {
        this.col = col;
    }

    /**  */
    public boolean isFilterRef() {
        return false;
    }

    /** @return  true if it's referencing a column */
    public boolean isCol() {
        return col;
    }

    /** @return  the String representation of the reference */
    @Override public String getName() {
        return ref;
    }

    @NotNull @Override protected Type doSolveType(@NotNull RefTypeSolver refResolver) {
        return refResolver.doResolve(getName(), col);
    }

    @NotNull @Override ExpressionAST solveEnumRef(EnumType enumType) {
        final EnumValue v = enumType.getValue(getName());
        return v == null ? this : new Value(v.getName(), enumType);
    }
}  // end class Ref
