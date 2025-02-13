
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.expr.exception;

import tekgenesis.code.Fun;
import tekgenesis.common.core.StrBuilder;
import tekgenesis.expr.BinaryExpression;
import tekgenesis.expr.ExpressionAST;
import tekgenesis.expr.ForbiddenExpression;
import tekgenesis.expr.FunctionCallNode;
import tekgenesis.expr.ListExpression;
import tekgenesis.expr.UnaryExpression;
import tekgenesis.type.Type;

import static tekgenesis.common.core.Constants.CANT_BE_APPLIED;
import static tekgenesis.common.core.Constants.FUNCTION;
import static tekgenesis.common.core.Constants.NOT_FOUND;
import static tekgenesis.common.core.Constants.OPERATOR;

/**
 * If the Operation is illegal between the types.
 */
public class IllegalOperationException extends RuntimeException {

    //~ Instance Fields ..............................................................................................................................

    private final ExpressionAST expr;

    //~ Constructors .................................................................................................................................

    /** For a binary operation. */
    public IllegalOperationException(final BinaryExpression expr) {
        super(new MsgBuilder(expr).types(expr.getLeft().getType(), expr.getRight().getType()).build());
        this.expr = expr;
    }

    /** For a unary operation. */
    public IllegalOperationException(final UnaryExpression expr) {
        super(new MsgBuilder(expr).types(expr.getOperand().getType()).build());
        this.expr = expr;
    }

    /** For a List Expression. */
    public IllegalOperationException(ListExpression expr) {
        super("List must contain at least one value");
        this.expr = expr;
    }

    /** For a forbidden operation. */
    public IllegalOperationException(final ForbiddenExpression expr, String msg) {
        super(msg);
        this.expr = expr;
    }

    /** For a unary operation. */
    public IllegalOperationException(final ExpressionAST expr, Type expected, Type found) {
        super("Incompatible types, required '" + expected + "' but found '" + found + "'");
        this.expr = expr;
    }

    /** For a Function call. */
    public IllegalOperationException(final FunctionCallNode expr, Fun<?> found, Type[] args) {
        super(new MsgBuilder(expr, found, args).build());
        this.expr = expr;
    }

    //~ Methods ......................................................................................................................................

    /** Returns only the error message, a shorter version that the default getMessage. */
    public String getErrorMessage() {
        return super.getMessage();
    }

    /** Returns the expression ast node. */
    public ExpressionAST getExpr() {
        return expr;
    }

    @Override public String getMessage() {
        return super.getMessage() + ", in expression " + expr;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -8646118348003236987L;

    //~ Inner Classes ................................................................................................................................

    private static class MsgBuilder {
        private final StrBuilder builder = new StrBuilder();

        private MsgBuilder(final ExpressionAST expr) {
            builder.append(OPERATOR).append(expr.getName()).append(CANT_BE_APPLIED);
        }

        private MsgBuilder(final FunctionCallNode expr, final Fun<?> found, Type[] args) {
            builder.append(FUNCTION).append(expr.getName()).append(found == null ? NOT_FOUND : CANT_BE_APPLIED);
            if (found != null) types(args);
        }

        public String build() {
            return builder.toString();
        }

        public MsgBuilder types(final Type... o) {
            builder.startCollection(",");
            for (final Type t : o)
                builder.appendElement("'").append(t).append("'");
            return this;
        }
    }  // end class MsgBuilder
}  // end class IllegalOperationException
