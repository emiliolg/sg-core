
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.expr.visitor;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import tekgenesis.code.*;
import tekgenesis.expr.*;
import tekgenesis.expr.exception.IllegalOperationException;
import tekgenesis.type.Kind;

import static tekgenesis.code.Binder.EMPTY;
import static tekgenesis.expr.ConstantExpression.createConstantExpression;

/**
 * Code Generator. Visit the AST generating code. Returns <code>true</code> if the Expression is
 * constant
 */
public class CodeGeneratorVisitor extends ExpressionVisitor.Default<Boolean> {

    //~ Instance Fields ..............................................................................................................................

    private final List<Code>       code;
    private final RefContextMapper refMapper;

    //~ Constructors .................................................................................................................................

    private CodeGeneratorVisitor(final RefContextMapper refMapper) {
        this.refMapper = refMapper;
        code           = new ArrayList<>();
    }

    //~ Methods ......................................................................................................................................

    @Override public Boolean visit(Ref e) {
        if (e.isFilterRef()) code(new Constant(Kind.REFERENCE, e.getName()));
        else code(new RefAccess(refMapper.doMap(e.getName()), e.isCol()));
        return false;
    }

    @Override public Boolean visit(ForbiddenExpression e) {
        code(new ForbiddenAccess(e.getPermission()));
        return false;
    }

    @Override public Boolean visit(UpdateExpression e) {
        code(new UpdateAccess());
        return false;
    }

    @Override public Boolean visit(ReadOnlyExpression e) {
        code(new ReadOnlyAccess());
        return false;
    }

    @Override public Boolean visit(Value e) {
        code(new Constant(e.getType().getKind(), e.getValue()));
        return true;
    }

    @Override public Boolean visit(FunctionCallNode e) {
        boolean result = e.canBeConstant();
        for (final ExpressionAST expr : e.getArguments())
            if (!expr.accept(this)) result = false;
        code(new FunctionCall(e.getName()));
        return result;
    }

    @Override public Boolean visit(BinaryExpression e) {
        final boolean l = e.acceptLeft(this);
        final boolean r;
        if (e.isShortCircuit()) {
            final int instruction = reserve();
            r = e.acceptRight(this);
            code(instruction, new Jump(e.getCode(), currentAddress()));
        }
        else {
            r = e.acceptRight(this);
            code(e.getCode());
        }
        return l && r;
    }

    @Override public Boolean visit(IfExpression ifExpr) {
        // if
        final boolean c             = ifExpr.acceptCondition(this);
        final int     ifInstruction = reserve();
        // then
        final boolean t = ifExpr.acceptThen(this);
        // else
        final int elseInstruction = reserve();
        code(ifInstruction, new Jump(Instruction.IF, currentAddress()));
        final boolean e = ifExpr.acceptElse(this);
        code(elseInstruction, new Jump(Instruction.ELSE, currentAddress()));
        //
        return c && t && e;
    }

    @Nullable @Override public Boolean visit(ListExpression e) {
        boolean result = true;
        for (final ExpressionAST expr : e)
            result &= expr.accept(this);
        code(new ListCode(e.getSize()));
        return result;
    }

    @Nullable @Override public Boolean visit(AssignmentExpression e) {
        boolean result = e.getFieldExpr().accept(this) & e.getValueExpr().accept(this);

        if (e.hasWhen()) result &= e.getWhenExpr().get().accept(this);
        else code(new Constant(Kind.BOOLEAN, true));

        code(new Constant(Kind.BOOLEAN, e.isEquals()));
        code(e.getCode());
        return result;
    }

    @Override public Boolean visit(UnaryExpression e) {
        final boolean result = e.acceptOperand(this);
        code(e.getCode());
        return result;
    }

    @Override public Boolean visit(ConversionOp e) {
        final boolean result = e.acceptOperand(this);
        if (e.getScale().isPresent()) code(new Constant(Kind.INT, e.getScale().get()));

        code(e.getCode());
        return result;
    }

    private void code(Code op) {
        code.add(op);
    }

    private void code(int address, Code op) {
        code.set(address, op);
    }

    private int currentAddress() {
        return code.size();
    }

    private int reserve() {
        code.add(null);
        return code.size() - 1;
    }

    //~ Methods ......................................................................................................................................

    /** Compiles the expression and returns the instructions code. */
    public static Expression.Implementation compile(final ExpressionAST e, final RefTypeSolver refResolver) {
        return compile(e, RefContextMapper.none(refResolver));
    }

    /** Compiles the expression and returns the instructions code. */
    public static Expression.Implementation compile(final ExpressionAST e, final RefContextMapper refMapper) {
        final CodeGeneratorVisitor visitor    = new CodeGeneratorVisitor(refMapper);
        final ExpressionAST        expression = e.solveType(refMapper);  // skip root

        final boolean            constant = expression.accept(visitor);
        final CompiledExpression c        = new CompiledExpression(expression.getType(), visitor.code);
        return constant ? createConstant(e, c) : c;
    }

    private static Expression.Implementation createConstant(final ExpressionAST e, final CompiledExpression c) {
        c.bind(EMPTY);

        try {
            final Object result = c.evaluate(new Evaluator(), null);
            return createConstantExpression(c.getType(), result);
        }
        catch (final NumberFormatException constantConversionFailed) {
            final ConversionOp conversion = (ConversionOp) e;  // assume root conversion node
            throw new IllegalOperationException(conversion, conversion.getTargetType(), conversion.getOperand().getType());
        }
    }
}  // end class CodeGeneratorVisitor
