
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

import tekgenesis.common.core.Constants;
import tekgenesis.expr.visitor.ExpressionVisitor;
import tekgenesis.type.Type;

import static tekgenesis.common.core.Constants.CONSTANT;

/**
 * Constant values.
 */
public class Value extends ExpressionAST {

    //~ Instance Fields ..............................................................................................................................

    private final Object        value;
    @NotNull private final Type valueType;

    //~ Constructors .................................................................................................................................

    Value(Object value, @NotNull Type type) {
        this.value = value;
        valueType  = type;
    }

    //~ Methods ......................................................................................................................................

    @Override public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public void acceptOperands(ExpressionVisitor<?> visitor) {  /* Do Nothing */
    }

    @Override public String toString() {
        return value == null ? Constants.NULL_TO_STRING : valueType.isString() ? "\"" + value + "\"" : value.toString();
    }

    @Override public String getName() {
        return CONSTANT;
    }

    /** @return  the value object of this constant */
    public Object getValue() {
        return value;
    }

    @NotNull @Override protected Type doSolveType(@NotNull RefTypeSolver r) {
        return valueType;
    }
}  // end class Value
