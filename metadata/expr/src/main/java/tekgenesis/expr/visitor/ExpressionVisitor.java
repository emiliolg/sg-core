
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.expr.visitor;

import org.jetbrains.annotations.Nullable;

import tekgenesis.expr.*;

/**
 * An visitor for an expression.
 *
 * @param  <T>  return type of the visit operation
 */
public interface ExpressionVisitor<T> {

    //~ Methods ......................................................................................................................................

    /** Visit {@link Ref}. */
    T visit(Ref e);

    /** Visit {@link Value}. */
    T visit(Value e);

    /** Visit {@link BinaryExpression }. */
    T visit(BinaryExpression e);

    /** Visit {@link ForbiddenExpression }. */
    T visit(ForbiddenExpression e);

    /** Visit {@link UpdateExpression }. */
    T visit(UpdateExpression e);

    /** Visit {@link ReadOnlyExpression }. */
    T visit(ReadOnlyExpression e);

    /** Visit an If Expression. */
    T visit(IfExpression e);

    /** Visit {@link UnaryExpression}. */
    T visit(UnaryExpression e);

    /** Visit {@link ConversionOp }. */
    T visit(ConversionOp e);

    /** Visit {@link FunctionCallNode}. */
    T visit(FunctionCallNode e);

    /** Visit {@link ListExpression}. */
    T visit(ListExpression e);

    /** Visit {@link AssignmentExpression}. */
    T visit(AssignmentExpression e);

    //~ Inner Classes ................................................................................................................................

    /**
     * Default handy implementation of the visitor.
     *
     * @param  <T>  return type of the visit operation
     */
    class Default<T> implements ExpressionVisitor<T> {
        @Nullable @Override public T visit(Ref e) {
            return visitExpression(e);
        }
        @Nullable @Override public T visit(Value e) {
            return visitExpression(e);
        }
        @Nullable @Override public T visit(BinaryExpression e) {
            return visitExpression(e);
        }
        @Nullable @Override public T visit(IfExpression e) {
            return visitExpression(e);
        }
        @Nullable @Override public T visit(FunctionCallNode e) {
            return visitExpression(e);
        }
        @Nullable @Override public T visit(UnaryExpression e) {
            return visitExpression(e);
        }
        @Nullable @Override public T visit(ForbiddenExpression e) {
            return visitExpression(e);
        }
        @Nullable @Override public T visit(UpdateExpression e) {
            return visitExpression(e);
        }
        @Nullable @Override public T visit(ReadOnlyExpression e) {
            return visitExpression(e);
        }

        @Nullable @Override public T visit(ConversionOp e) {
            return visitExpression(e);
        }
        @Nullable @Override public T visit(ListExpression e) {
            return visitExpression(e);
        }
        @Nullable @Override public T visit(AssignmentExpression e) {
            return visitExpression(e);
        }

        @Nullable T visitExpression(ExpressionAST e) {
            e.acceptOperands(this);
            return getVisitReturnValue();
        }

        @Nullable T getVisitReturnValue() {
            return null;
        }
    }  // end class Default
}  // end interface ExpressionVisitor
