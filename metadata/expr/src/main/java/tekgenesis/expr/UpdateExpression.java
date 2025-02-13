
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

public class UpdateExpression extends ExpressionAST {

    //~ Constructors .................................................................................................................................

    public UpdateExpression() {
        super(Instruction.IS_UPDATE, Suppliers.fromObject(booleanType()));
    }

    //~ Methods ......................................................................................................................................

    @Override public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public void acceptOperands(ExpressionVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override public String getName() {
        return "isUpdate()";
    }

    @NotNull @Override protected Type doSolveType(RefTypeSolver refResolver) {
        return booleanType();
    }
}
