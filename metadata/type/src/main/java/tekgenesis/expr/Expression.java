
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.expr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.code.*;
import tekgenesis.common.core.Option;
import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.type.Kind;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

/**
 * An Algebraic Expression.
 */
@SuppressWarnings("GwtInconsistentSerializableClass")  // using CustomFieldSerializer
public class Expression implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Implementation implementation;

    //~ Constructors .................................................................................................................................

    Expression(@NotNull Implementation implementation) {
        this.implementation = implementation;
    }

    //~ Methods ......................................................................................................................................

    /** Solve late bindings (Function and Variables references). */
    public Expression bind(Binder binder) {
        implementation.bind(binder);
        return this;
    }

    /** Compile the Expression (If needed). */
    public Expression compile(RefTypeSolver solver) {
        implementation.compile(solver);
        return this;
    }

    /** Compile, Bind and Evaluate the Expression.. */
    public Object compileBindAndEvaluate(RefTypeSolver solver, Binder binder, Evaluator evaluator) {
        return compile(solver).bind(binder).evaluate(evaluator, new Object());
    }

    /** Evaluate the Expression. */
    public Object evaluate(@NotNull Evaluator evaluator, @Nullable Object context) {
        return implementation.evaluate(evaluator, context);
    }

    /** Extract Strings from expression. */
    public List<String> extractStrings() {
        return implementation.extractStrings();
    }

    /** Re-Maps the references of this expression to the new context. */
    public Expression reMapReferences(final RefContextMapper mapper) {
        return new Expression(implementation.reMapReferences(mapper));
    }

    /** Return a new expression with the Strings replaced by the specified ones. */
    public Expression replaceStrings(List<String> strings) {
        final Implementation imp = implementation.replaceStrings(strings);
        return imp == implementation ? this : new Expression(imp);
    }

    /** Retrieve the name of all the references for this Expression. */
    public Collection<String> retrieveReferences() {
        return implementation.retrieveReferences();
    }

    /** Serialize the Expression to a Stream. */
    public void serialize(StreamWriter w) {
        int idx = singletonIndex();

        if (idx < 0) idx = isConstant() ? CONSTANT : COMPILED;
        w.writeInt(idx);

        if (idx < 0) implementation.serialize(w);
    }

    @Override public String toString() {
        return implementation.toString();
    }

    /** Returns the expression value if the expression is a constant if not return none(). */
    public Option<Object> getConstantValue() {
        return implementation.getConstantValue();
    }

    /** Tests if this expression is compiled. */
    public boolean isCompiled() {
        return implementation.isCompiled();
    }

    /** Returns true if the Expression is the NUll Expression. */
    public boolean isNull() {
        return implementation == ConstantExpression.NULL_IMPL;
    }

    /** Returns true if the Expression is Constant. */
    public boolean isConstant() {
        return implementation.isConstant();
    }

    /** Returns the expression type. */
    public Type getType() {
        return implementation.getType();
    }

    /** Returns true if the Expression isEmpty. */
    public boolean isEmpty() {
        return implementation == ConstantExpression.EMPTY_IMPL;
    }

    @NotNull Implementation getImplementation() {
        return implementation;
    }

    /** Utility method to find the index of the singleton (If this expression is a Singleton). */
    private int singletonIndex() {
        for (int i = 0; i < singletons.length; i++) {
            if (singletons[i] == this) return i;
        }
        return -1;
    }

    //~ Methods ......................................................................................................................................

    /** Create a constant expression of String type. */
    public static Expression createConstant(String value) {
        return new Expression(ConstantExpression.createConstantExpression(Types.stringType(), value));
    }

    /** Instantiate an Expression from the stream. */
    public static Expression instantiate(StreamReader r) {
        final int idx = r.readInt();
        return idx >= 0 ? singleton(idx) : new Expression(idx == CONSTANT ? readConstantExpression(r) : readCode(r));
    }

    private static Implementation readCode(StreamReader r) {
        final Type       type   = Kind.instantiateType(r);
        final int        length = r.readInt();
        final List<Code> result = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            final int         instructionOrdinal = r.readInt();
            final Instruction instruction        = Instruction.valueOf(instructionOrdinal);

            final Code code;
            switch (instruction) {
            case REF:
                code = new RefAccess(r.readString(), r.readBoolean());
                break;
            case FORBIDDEN:
                code = new ForbiddenAccess(r.readString());
                break;
            case IS_UPDATE:
                code = new UpdateAccess();
                break;
            case IS_READ_ONLY:
                code = new ReadOnlyAccess();
                break;
            case FUN:
                code = new FunctionCall(r.readString());
                break;
            case PUSH:
                final Kind k = Kind.instantiate(r);
                code = new Constant(k, k.readValue(r));
                break;
            case OR:
            case AND:
            case IF:
            case ELSE:
                code = new Jump(instruction, r.readInt());
                break;
            case LIST:
                code = new ListCode(r.readInt());
                break;
            default:
                code = instruction;
                break;
            }

            result.add(code);
        }
        return new CompiledExpression(type, result);
    }  // end method readCode

    private static ConstantExpression readConstantExpression(StreamReader r) {
        final Type type = Kind.instantiateType(r);
        return new ConstantExpression(type, r.readObjectConst());
    }

    /** Utility method to construct an Expression from a Singleton. */
    private static Expression singleton(int i) {
        return singletons[i];
    }

    //~ Static Fields ................................................................................................................................

    private static final int CONSTANT = -1;
    private static final int COMPILED = -2;

    private static final long serialVersionUID = 5546320099141454681L;

    public static final Expression    EMPTY      = new Expression(ConstantExpression.EMPTY_IMPL);
    public static final Expression    NULL       = new Expression(ConstantExpression.NULL_IMPL);
    public static final Expression    TRUE       = new Expression(ConstantExpression.TRUE_IMPL);
    public static final Expression    FALSE      = new Expression(ConstantExpression.FALSE_IMPL);
    private static final Expression[] singletons = { NULL, TRUE, FALSE };

    //~ Inner Interfaces .............................................................................................................................

    public interface Implementation {
        /** Solve late bindings (Function and Variables references). */
        void bind(Binder binder);

        /** Compile the Expression (If needed). */
        void compile(RefTypeSolver solver);

        /** Evaluate the Expression. */
        Object evaluate(@NotNull Evaluator evaluator, @Nullable Object context);

        /** Extract all constant Strings from Expression. */
        List<String> extractStrings();

        /** Re-Maps the references of this expression to the new context. */
        Implementation reMapReferences(RefContextMapper mapper);

        /** Return a new expression with the Strings replaced. */
        Implementation replaceStrings(List<String> strings);

        /** Retrieve the name of all the references for this Expression. */
        Collection<String> retrieveReferences();

        /** Serialize the Expression. */
        void serialize(StreamWriter w);

        /**
         * Returns the expression value if the expression is a not null constant. If it is an
         * expression or is null it will return none().
         */
        Option<Object> getConstantValue();

        /** Returns true if expression is compiled. */
        boolean isCompiled();

        /** Returns true if the Expression is Constant. */
        boolean isConstant();

        /** Returns the expression type. */
        Type getType();
    }  // end interface Implementation
}  // end class Expression
