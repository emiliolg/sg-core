
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

import tekgenesis.common.core.StrBuilder;
import tekgenesis.expr.*;

import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.common.core.Strings.join;

/**
 * ExpressionAST Visitor to generate a readable string of the expression.
 */
public class ToStringVisitor extends ExpressionVisitor.Default<String> {

    //~ Methods ......................................................................................................................................

    @Override public String visit(Ref e) {
        return e.getName();
    }

    @Override public String visit(Value e) {
        return e.toString();
    }

    @Override public String visit(UnaryExpression e) {
        return e.getName() + "(" + e.acceptOperand(this) + ")";
    }

    @Override public String visit(ForbiddenExpression e) {
        return e.getName() + "(" + e.getPermission() + ")";
    }

    @Override public String visit(UpdateExpression e) {
        return e.getName();
    }

    @Override public String visit(ConversionOp e) {
        return e.isImplicit() ? e.acceptOperand(this) : e.getName() + "(" + e.acceptOperand(this) + ")";
    }

    @Override public String visit(FunctionCallNode e) {
        return asMethodCall(e, e.getArguments());
    }

    @Override public String visit(BinaryExpression e) {
        return "(" + e.acceptLeft(this) + " " + e.getName() + " " + e.acceptRight(this) + ")";
    }

    @Override public String visit(IfExpression e) {
        return e.acceptCondition(this) + " ? " + e.acceptThen(this) + " : " + e.acceptElse(this);
    }

    @Nullable @Override public String visit(ListExpression e) {
        final String list = join(map(e, expressionAST -> expressionAST.accept(this)), ',');
        return "(" + list + ")";
    }

    @Nullable @Override public String visit(AssignmentExpression e) {
        return e.getFieldExpr().accept(this) + (e.isEquals() ? " = " : " != ") + e.getValueExpr().accept(this) +
               (e.hasWhen() ? " when " + e.getWhenExpr().get().accept(this) : "");
    }

    private String asMethodCall(ExpressionAST e, Iterable<ExpressionAST> children) {
        final StrBuilder r = new StrBuilder();
        for (final ExpressionAST expression : children)
            r.appendElement(expression.accept(this), ", ");
        return asMethodName(e) + "(" + r.toString() + ")";
    }

    private String asMethodName(final ExpressionAST e) {
        return e.getName();
    }

    //~ Methods ......................................................................................................................................

    /** Generate a readable string of the expression. */
    public static String generate(final ExpressionAST expr) {
        final String result = expr.accept(new ToStringVisitor());
        // remove the first unwanted parenthesis for a binary expression
        return expr instanceof BinaryExpression ? result.substring(1, result.length() - 1) : result;
    }
}  // end class ToStringVisitor
