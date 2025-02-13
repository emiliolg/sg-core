
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
import tekgenesis.expr.exception.IllegalOperationException;
import tekgenesis.expr.visitor.ExpressionVisitor;
import tekgenesis.type.EnumType;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static tekgenesis.expr.ExpressionFactory.conversion;

/**
 * A Binary (Two Operands) ExpressionAST.
 */
public class IfExpression extends ExpressionAST {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final ExpressionAST cond;
    @NotNull private ExpressionAST       falseExpr;
    @NotNull private ExpressionAST       trueExpr;

    //~ Constructors .................................................................................................................................

    /** Creates a Binary expression. */
    public IfExpression(@NotNull ExpressionAST cond, @NotNull ExpressionAST trueExpr, @NotNull ExpressionAST falseExpr) {
        super(Instruction.IF, Suppliers.empty());
        this.cond      = cond;
        this.trueExpr  = trueExpr;
        this.falseExpr = falseExpr;
    }

    //~ Methods ......................................................................................................................................

    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    /** Accepts the condition Expression. */
    public <T> T acceptCondition(ExpressionVisitor<T> visitor) {
        return cond.accept(visitor);
    }

    /** Accepts the else Expression. */
    public <T> T acceptElse(ExpressionVisitor<T> visitor) {
        return falseExpr.accept(visitor);
    }

    @Override public void acceptOperands(ExpressionVisitor<?> visitor) {
        acceptCondition(visitor);
        acceptThen(visitor);
        acceptElse(visitor);
    }

    /** Accepts the then Expression. */
    public <T> T acceptThen(ExpressionVisitor<T> visitor) {
        return trueExpr.accept(visitor);
    }

    /** Returns a friendly name of the operation. */
    public String getName() {
        return "if";
    }

    @NotNull @Override protected Type doSolveType(RefTypeSolver refResolver) {
        cond.solve(refResolver, Types.booleanType());

        final Type target = getTargetType();
        if (target instanceof EnumType) {
            final EnumType et = (EnumType) target;
            trueExpr  = trueExpr.solveEnumRef(et);
            falseExpr = falseExpr.solveEnumRef(et);
        }
        final Type a = trueExpr.solveType(refResolver).getType();
        final Type b = falseExpr.solveType(refResolver).getType();

        if (!a.equivalent(b)) {
            // Convert sides to the correct super type
            final Type t = a.commonSuperType(b);
            if (t == Types.anyType()) throw new IllegalOperationException(falseExpr, a, b);
            trueExpr  = conversion(trueExpr, t).solveType(refResolver);
            falseExpr = conversion(falseExpr, t).solveType(refResolver);
        }
        return trueExpr.getType();
    }
}  // end class IfExpression
