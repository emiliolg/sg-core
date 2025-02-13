
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.expr;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.code.Binder;
import tekgenesis.code.Code;
import tekgenesis.code.Evaluator;
import tekgenesis.common.Predefined;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Suppliers;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.expr.exception.IllegalOperationException;
import tekgenesis.expr.visitor.CodeGeneratorVisitor;
import tekgenesis.expr.visitor.ExpressionVisitor;
import tekgenesis.expr.visitor.ToStringVisitor;
import tekgenesis.type.EnumType;
import tekgenesis.type.Type;

/**
 * Abstract expression class.
 */
public abstract class ExpressionAST {

    //~ Instance Fields ..............................................................................................................................

    protected Supplier<Type> targetType;

    @Nullable Code         instruction;
    @Nullable private Type type;

    //~ Constructors .................................................................................................................................

    ExpressionAST() {
        type        = null;
        instruction = null;
        targetType  = Suppliers.empty();
    }

    ExpressionAST(@Nullable Code i, Supplier<Type> targetType) {
        type            = null;
        instruction     = i;
        this.targetType = targetType;
    }

    //~ Methods ......................................................................................................................................

    /** Accepts the visitor. */
    public abstract <T> T accept(ExpressionVisitor<T> visitor);

    /** Accept all operands of the ExpressionAST. */
    public abstract void acceptOperands(ExpressionVisitor<?> visitor);

    /** Creates an Expression based in this ExpressionAST. */
    public Expression createExpression() {
        return new Expression(new Implementation(this));
    }

    /** Solve the type of the expression using the specified {@link RefTypeSolver }. */
    @NotNull public ExpressionAST solveType(@NotNull final RefTypeSolver refResolver) {
        // Check if type is not solved yet and invoke the doSolveType Function
        if (type == null) type = doSolveType(refResolver);
        return this;
    }

    @Override public String toString() {
        return ToStringVisitor.generate(this);
    }

    /** Returns the Instruction to execute this expression. */
    @NotNull public final Code getCode() {
        return Predefined.ensureNotNull(instruction, ILLEGAL_NULL_VALUE);
    }

    /** Returns the name of the expression. */
    public abstract String getName();

    /** Returns the target type of the conversion. */
    @Nullable public Type getTargetType() {
        return targetType.get();
    }

    /**
     * Returns the {@link Type} of the expression.
     *
     * @throws  IllegalStateException  if the type is null
     */
    @NotNull public Type getType() {
        return Predefined.ensureNotNull(type, ILLEGAL_NULL_VALUE);
    }

    /**
     * The real implementation of the {@link #solveType(RefTypeSolver) } function in each subclass.
     */
    @NotNull protected abstract Type doSolveType(final RefTypeSolver refResolver);

    /** Solve and verifies return type. */
    void solve(RefTypeSolver refResolver, Type expectedType) {
        final Type c = solveType(refResolver).getType();
        if (!c.equals(expectedType)) throw new IllegalOperationException(this, expectedType, c);
    }

    @NotNull ExpressionAST solveEnumRef(EnumType enumType) {
        setTargetType(enumType);
        return this;
    }

    void setTargetType(@Nullable Type t) {
        if (t != null && targetType.get() == null) targetType = Suppliers.fromObject(t);
    }

    //~ Methods ......................................................................................................................................

    /** Accepts the visitor for the specified Expression. */
    public static <T> T accept(Expression expression, ExpressionVisitor<T> visitor) {
        final Expression.Implementation impl = expression.getImplementation();
        if (!(impl instanceof Implementation)) throw new IllegalStateException("AST not present. Cannot be visited");
        return ((Implementation) impl).ast.accept(visitor);
    }

    //~ Static Fields ................................................................................................................................

    @NonNls private static final String ILLEGAL_NULL_VALUE = "Null value. Do you forget to call solveType?";

    //~ Inner Classes ................................................................................................................................

    private static class Implementation implements Expression.Implementation {
        @NotNull private final ExpressionAST        ast;
        @Nullable private Expression.Implementation compiledExpression;

        public Implementation(@NotNull ExpressionAST expressionAST) {
            ast                = expressionAST;
            compiledExpression = null;
        }

        @Override public void bind(Binder binder) {
            if (compiledExpression == null) throw new IllegalStateException("Attempting to bind a not compiled Expression: " + ast);
            getCompiled().bind(binder);
        }

        @Override public void compile(RefTypeSolver solver) {
            if (compiledExpression == null) compiledExpression = CodeGeneratorVisitor.compile(ast, solver);
        }

        @Override public Object evaluate(@NotNull Evaluator evaluator, Object context) {
            return getCompiled().evaluate(evaluator, context);
        }

        @Override public List<String> extractStrings() {
            return getCompiled().extractStrings();
        }

        @Override public Expression.Implementation reMapReferences(RefContextMapper mapper) {
            compiledExpression = CodeGeneratorVisitor.compile(ast, mapper);
            return this;
        }

        @Override public Expression.Implementation replaceStrings(List<String> strings) {
            return getCompiled().replaceStrings(strings);
        }

        @Override public Collection<String> retrieveReferences() {
            return getCompiled().retrieveReferences();
        }

        @Override public void serialize(StreamWriter w) {
            getCompiled().serialize(w);
        }

        @Override public String toString() {
            return ast.toString();
        }

        @Override public Option<Object> getConstantValue() {
            return getCompiled().getConstantValue();
        }

        @Override public boolean isCompiled() {
            return compiledExpression != null;
        }

        @Override public boolean isConstant() {
            return isCompiled() && getCompiled().isConstant();
        }

        @Override public Type getType() {
            return getCompiled().getType();
        }

        @NotNull private Expression.Implementation getCompiled() {
            if (compiledExpression == null) throw new IllegalStateException("Expression was not compiled: " + ast);
            return compiledExpression;
        }
    }  // end class Implementation
}  // end class ExpressionAST
