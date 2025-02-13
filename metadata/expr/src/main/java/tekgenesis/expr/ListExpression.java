
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.expr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.expr.exception.IllegalOperationException;
import tekgenesis.expr.visitor.ExpressionVisitor;
import tekgenesis.type.EnumType;
import tekgenesis.type.Kind;
import tekgenesis.type.Type;

import static tekgenesis.expr.AssignmentExpression.hasValidType;
import static tekgenesis.type.Types.anyType;
import static tekgenesis.type.Types.arrayType;

/**
 * An expression containing other AST Expressions. Solving it will solve inner expressions.
 */
public class ListExpression extends ExpressionAST implements Iterable<ExpressionAST> {

    //~ Instance Fields ..............................................................................................................................

    private final List<ExpressionAST> expressions;
    private Type                      listType = null;

    //~ Constructors .................................................................................................................................

    /** List expression constructor. */
    public ListExpression(@Nullable Type listType) {
        expressions   = new ArrayList<>();
        this.listType = listType;
    }

    //~ Methods ......................................................................................................................................

    @Override public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public void acceptOperands(ExpressionVisitor<?> visitor) {
        for (final ExpressionAST expr : expressions)
            expr.acceptOperands(visitor);
    }

    /** Add an expression to the list. */
    public void add(ExpressionAST expressionAST) {
        expressions.add(expressionAST);
    }

    @Override public Iterator<ExpressionAST> iterator() {
        return expressions.iterator();
    }

    @NotNull @Override public ExpressionAST solveType(@NotNull RefTypeSolver refResolver) {
        for (final ExpressionAST expr : expressions)
            expr.solveType(refResolver);
        return super.solveType(refResolver);
    }

    @Override public String getName() {
        return "list";
    }

    /** Get list size. */
    public int getSize() {
        return expressions.size();
    }

    @NotNull @Override protected Type doSolveType(RefTypeSolver refResolver) {
        Type type = listType;
        for (final ExpressionAST expr : expressions) {
            final Type nextType = expr.doSolveType(refResolver);
            if (type == null) type = nextType;
            else if (!hasValidType(nextType, type) && type.getKind() != Kind.ANY) throw new IllegalOperationException(expr, type, nextType);
        }
        if (type == null) throw new IllegalOperationException(this);
        return arrayType(type);
    }

    void replace(ExpressionAST expr, ExpressionAST solvedExpr) {
        expressions.set(expressions.indexOf(expr), solvedExpr);
    }

    Type getListType() {
        return listType;
    }

    //~ Inner Classes ................................................................................................................................

    public static class AssignmentListExpression extends ListExpression {
        private final Type    assignmentType;
        private final boolean checkSameType;

        /** List expression constructor. */
        public AssignmentListExpression(@Nullable Type listType, @Nullable Type assignmentType, boolean sameType) {
            super(listType);
            this.assignmentType = assignmentType;
            checkSameType       = sameType;
        }

        @NotNull @Override public ExpressionAST solveType(@NotNull RefTypeSolver refResolver) {
            for (final ExpressionAST expr : this) {
                if (expr instanceof AssignmentExpression && assignmentType != null) expr.setTargetType(assignmentType);
                expr.solveType(refResolver);
            }
            return super.solveType(refResolver);
        }

        @NotNull @Override protected Type doSolveType(RefTypeSolver refResolver) {
            if (checkSameType) return super.doSolveType(refResolver);
            for (final ExpressionAST expr : this)
                expr.doSolveType(refResolver);
            return arrayType(anyType());
        }

        @NotNull @Override ExpressionAST solveEnumRef(EnumType enumType) {
            for (final ExpressionAST expr : this)
                replace(expr, expr.solveEnumRef(enumType));
            return this;
        }
    }
}  // end class ListExpression
