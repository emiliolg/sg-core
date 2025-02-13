
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

import tekgenesis.code.Instruction;
import tekgenesis.common.core.Suppliers;
import tekgenesis.expr.visitor.ExpressionVisitor;
import tekgenesis.type.Type;

import static tekgenesis.type.Types.booleanType;

public class ReadOnlyExpression extends ExpressionAST {

    //~ Constructors .................................................................................................................................

    public ReadOnlyExpression() {
        super(Instruction.IS_READ_ONLY, Suppliers.fromObject(booleanType()));
    }

    //~ Methods ......................................................................................................................................

    @Override public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public void acceptOperands(ExpressionVisitor<?> visitor) {
        // nothing
    }

    @Override
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public String getName() {
        return "isReadOnly()";
    }

    @NotNull @Override protected Type doSolveType(RefTypeSolver refResolver) {
        return booleanType();
    }
}
