
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.expr;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.code.Instruction;
import tekgenesis.common.core.Suppliers;
import tekgenesis.expr.exception.IllegalOperationException;
import tekgenesis.expr.visitor.ExpressionVisitor;
import tekgenesis.type.Kind;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static tekgenesis.common.collections.Maps.enumMap;
import static tekgenesis.common.core.Tuple.tuple;

/**
 * An ExpressionAST with only one operand.
 */
public class UnaryExpression extends ExpressionAST {

    //~ Instance Fields ..............................................................................................................................

    @NotNull ExpressionAST          operand;
    @NotNull private final Operator operator;

    //~ Constructors .................................................................................................................................

    /** Creates a ExpressionAST with only one operand. */
    public UnaryExpression(@NotNull Operator operator, @NotNull ExpressionAST operand) {
        this(operator, operand, Suppliers.empty());
    }
    /** Creates a ExpressionAST with only one operand, and targetType. */
    UnaryExpression(@NotNull Operator operator, @NotNull ExpressionAST operand, @NotNull Supplier<Type> targetType) {
        super(null, targetType);
        this.operand  = operand;
        this.operator = operator;
    }

    //~ Methods ......................................................................................................................................

    @Override public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    /** Accepts the visitor for the operand expression. */
    public final <T> T acceptOperand(ExpressionVisitor<T> visitor) {
        return operand.accept(visitor);
    }

    @Override public final void acceptOperands(ExpressionVisitor<?> visitor) {
        acceptOperand(visitor);
    }

    @Override public String getName() {
        return operator.toString();
    }

    /** Returns the operand of the Unary expression. */
    @NotNull public ExpressionAST getOperand() {
        return operand;
    }

    @NotNull @Override protected Type doSolveType(RefTypeSolver refResolver) {
        if (operator.isAggregate() && operand instanceof Ref) ((Ref) operand).setCol(true);
        Type t = operand.solveType(refResolver).getType();
        if (operator.isAggregate()) t = Types.elementType(t);

        final Type retType = operator.returnType(t);

        if (retType == null) throw new IllegalOperationException(this);

        instruction = operator.code(t);
        if (instruction == null) throw new IllegalOperationException(this);

        return retType;
    }

    //~ Methods ......................................................................................................................................

    /** Returns an Expression based on a \n String or <code>null</code> if it doesn't exists. */
    @Nullable public static UnaryExpression fromId(String name, @NotNull ExpressionAST operand) {
        final Operator op = idToEnum.get(name);
        return op == null ? ConversionOp.fromTypeName(name, operand) : new UnaryExpression(op, operand);
    }

    //~ Static Fields ................................................................................................................................

    private static final Map<String, Operator> idToEnum;

    static {
        idToEnum = new HashMap<>();
        for (final Operator op : Operator.values())
            idToEnum.put(op.getId(), op);
    }

    //~ Enums ........................................................................................................................................

    public enum Operator {
        //J-
        MINUS("-", false) {
            @Override Instruction code(Type t) { return minus.get(t.getKind()); }
        },
        PLUS("+", false) {
            @Override Instruction code(Type t) { return Instruction.NOP; }
        },
        NOT("!", false) {
            @Override Instruction code(Type t) { return t == Types.booleanType() ? Instruction.NOT : null; }
        },
        SIZE("size", false) {
            @Override Instruction code(Type t) { return Instruction.SIZE; }
            @Override Type returnType(Type t)  { return Types.intType(); }
        },
        SUM("sum", true) {
            @Override Instruction code(Type t) { return sum.get(t.getKind()); }
            @Override Type returnType(Type t)  { return t.isNumber() ? super.returnType(t) : Types.intType(); }
        },
        AVG("avg", true) {
            @Override Instruction code(Type t) { return avg.get(t.getKind()); }
        },
        @SuppressWarnings("DuplicateStringLiteralInspection")COUNT("count", true) {
            @Override Instruction code(Type t) { return Instruction.COUNT; }
            @Override Type returnType(Type t)  { return Types.intType(); }
        },
        ROWS("rows", true) {
            @Override Instruction code(Type t) { return Instruction.ROWS; }
            @Override Type returnType(Type t)  { return Types.intType(); }
        },
        MIN("min", true) {
            @Override Instruction code(Type t) { return Instruction.MIN; }
        },
        MAX("max", true) {
            @Override Instruction code(Type t) { return Instruction.MAX; }
        },
        CAST("cast", false) {
            @Override Instruction code(Type t) { return Instruction.NOP; }
        };
        //J+
        private final boolean aggregate;

        private final String text;

        Operator(String s, boolean b) {
            text      = s;
            aggregate = b;
        }

        @Override public String toString() {
            return text;
        }

        /** Return the external Id of the Operator. */
        public String getId() {
            return text;
        }

        abstract Instruction code(Type t);
        Type returnType(Type t) {
            return t;
        }
        boolean isAggregate() {
            return aggregate;
        }

        private static final EnumMap<Kind, Instruction> sum = enumMap(tuple(Kind.BOOLEAN, Instruction.SUM_BOOL),
                tuple(Kind.INT, Instruction.SUM_INT),
                tuple(Kind.REAL, Instruction.SUM_REAL),
                tuple(Kind.DECIMAL, Instruction.SUM_DEC));

        private static final EnumMap<Kind, Instruction> avg = enumMap(tuple(Kind.INT, Instruction.AVG_INT),
                tuple(Kind.REAL, Instruction.AVG_REAL),
                tuple(Kind.DECIMAL, Instruction.AVG_DEC));

        private static final EnumMap<Kind, Instruction> minus = enumMap(tuple(Kind.INT, Instruction.MINUS_INT),
                tuple(Kind.REAL, Instruction.MINUS_REAL),
                tuple(Kind.DECIMAL, Instruction.MINUS_DEC));
    }
}  // end class UnaryExpression
