
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.code.Code;
import tekgenesis.code.Instruction;
import tekgenesis.expr.exception.IllegalOperationException;
import tekgenesis.expr.visitor.ExpressionVisitor;
import tekgenesis.type.*;

import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.collections.Maps.enumMap;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.expr.BinaryExpression.Operator.*;
import static tekgenesis.expr.ExpressionFactory.conversion;
import static tekgenesis.type.Types.*;

/**
 * A Binary (Two Operands) ExpressionAST.
 */
public class BinaryExpression extends ExpressionAST {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private ExpressionAST  left;
    @NotNull private final Operator op;
    @NotNull private ExpressionAST  right;

    //~ Constructors .................................................................................................................................

    /** Creates a Binary expression. */
    public BinaryExpression(@NotNull Operator op, @NotNull ExpressionAST left, @NotNull ExpressionAST right) {
        this.op    = op;
        this.left  = left;
        this.right = right;
    }

    //~ Methods ......................................................................................................................................

    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    /** Accepts the visitor fot the left side of the expression. */
    public final <T> T acceptLeft(ExpressionVisitor<T> visitor) {
        return left.accept(visitor);
    }

    @Override public void acceptOperands(ExpressionVisitor<?> visitor) {
        acceptLeft(visitor);
        acceptRight(visitor);
    }

    /** Accepts the visitor fot the right side of the expression. */
    public final <T> T acceptRight(ExpressionVisitor<T> visitor) {
        return right.accept(visitor);
    }

    /** Returns the left side of the expression. */
    @NotNull public ExpressionAST getLeft() {
        return left;
    }

    /** Returns a friendly name of the operation. */
    public String getName() {
        return op.toString().toLowerCase();
    }

    /** Returns the Logic of the expression. */
    @NotNull
    @SuppressWarnings("WeakerAccess")
    public Operator getOp() {
        return op;
    }

    /** Returns the right side of the expression. */
    @NotNull public ExpressionAST getRight() {
        return right;
    }

    /** Returns true if the operand is a short circuit one (and & OR). */
    public boolean isShortCircuit() {
        return op == OR || op == AND;
    }

    @NotNull @Override protected Type doSolveType(RefTypeSolver refResolver) {
        applyConversions(refResolver);

        final Type returnType = op.returnType(left.getType(), right.getType());
        if (returnType == null) throw new IllegalOperationException(this);

        // Find the instruction this will check that the operation can operate with this type
        instruction = op.code(left.getType(), right.getType());
        if (instruction == null) throw new IllegalOperationException(this);

        return returnType;
    }

    private void applyConversions(RefTypeSolver refResolver) {
        final Type l = left.solveType(refResolver).getType();

        if (l.isEnum())  // Special case try to solve right as an Enum reference
            right = right.solveEnumRef((EnumType) l);

        final Type r = right.solveType(refResolver).getType();
        //
        // Convert sides to the correct super type
        final Type t = getOp().commonType(l, r);

        if (t != Types.anyType()) {
            left  = conversion(left, t).solveType(refResolver);
            right = conversion(right, t).solveType(refResolver);
        }
    }

    //~ Methods ......................................................................................................................................

    private static boolean isLong(final Type l) {
        return l instanceof IntType && ((IntType) l).isLong();
    }

    //~ Enums ........................................................................................................................................

    public enum Operator {
        ADD("+") {
            @Override Type commonType(final Type l, final Type r) {
                // String concatenation
                return l.isString() || r.isString() ? Types.stringType() : SUB.commonType(l, r);
            }

            @Nullable @Override public Code code(Type l, Type r) {
                if (l.isTime() && !intType().equals(r)) return null;
                return add.get(l.getKind());
            }},

        SUB("-") {
            @Override Type commonType(final Type l, final Type r) {
                // If any is Long promote to decimal
                return isLong(l) || isLong(r) ? Types.decimalType() : super.commonType(l, r);
            }

            @Nullable @Override public Code code(Type l, Type r) {
                final Kind lk = l.getKind();
                return lk == Kind.DATE ? subDate.get(r.getKind()) : lk == Kind.DATE_TIME ? subTime.get(r.getKind()) : sub.get(lk);
            }},

        DIV("/") {
            @Override Type commonType(final Type l, final Type r) { return SUB.commonType(l, r); }

            @Nullable @Override public Code code(Type l, Type r) { return div.get(l.getKind()); }},
        MOD("%") {
            @Nullable @Override public Code code(Type l, Type r) { return mod.get(l.getKind()); }@Override Type commonType(final Type l,
                                                                                                                           final Type r) {
                return SUB.commonType(l, r);
            }},
        MUL("*") {
            @Nullable @Override public Code code(Type l, Type r) { return mul.get(l.getKind()); }@Override Type commonType(final Type l,
                                                                                                                           final Type r) {
                return SUB.commonType(l, r);
            }},

        AND("&&", Instruction.AND) {
            @Override public Type returnType(Type l, Type r) { return l == booleanType() && r == booleanType() ? booleanType() : null; }},

        OR("||", Instruction.OR) { @Override public Type returnType(Type l, Type r) { return AND.returnType(l, r); } },
        EQ("==", Instruction.EQ) {
            @Nullable @Override public Code code(Type l, Type r) { return l.isNumber() && r.isNumber() ? Instruction.EQ_NUM : super.code(l, r); }

            @Override public Type returnType(Type l, Type r) { return booleanType(); }},

        LE("<=", Instruction.LE) { @Override public Type returnType(Type l, Type r) { return booleanType(); } },
        GE(">=", Instruction.GE) { @Override public Type returnType(Type l, Type r) { return booleanType(); } },
        NE("!=", Instruction.NE) {
            @Nullable @Override public Code code(Type l, Type r) { return l.isNumber() && r.isNumber() ? Instruction.NE_NUM : super.code(l, r); }@Override
            public Type returnType(Type l, Type r) { return booleanType(); }},

        GT(">", Instruction.GT) { @Override public Type returnType(Type l, Type r) { return booleanType(); } },
        LT("<", Instruction.LT) { @Override public Type returnType(Type l, Type r) { return booleanType(); } },;

        private final Instruction instruction;
        private final String      text;

        Operator(@NotNull final String s) {
            this(s, null);
        }

        Operator(@NotNull final String s, @Nullable Instruction instruction) {
            text             = s;
            this.instruction = instruction;
        }

        @Override public String toString() {
            return text;
        }

        Code code(Type l, Type r) {
            return ensureNotNull(instruction);
        }

        Type commonType(final Type l, final Type r) {
            return l.equivalent(r) ? l : l.commonSuperType(r);
        }

        Type returnType(Type leftOp, Type rightOp) {
            // Special cases TIME differences
            return leftOp.isTime() && rightOp.isTime() ? intType() : leftOp;
        }
        boolean isArithmetic() {
            return this == ADD || this == SUB || this == DIV || this == MOD || this == MUL;
        }

        private static final EnumMap<Kind, Instruction> add = enumMap(tuple(Kind.INT, Instruction.ADD_INT),
                tuple(Kind.REAL, Instruction.ADD_REAL),
                tuple(Kind.DECIMAL, Instruction.ADD_DEC),
                tuple(Kind.STRING, Instruction.CAT),
                tuple(Kind.DATE, Instruction.ADD_DATE_INT),
                tuple(Kind.DATE_TIME, Instruction.ADD_TIME_INT));

        private static final EnumMap<Kind, Instruction> sub = enumMap(tuple(Kind.INT, Instruction.SUB_INT),
                tuple(Kind.REAL, Instruction.SUB_REAL),
                tuple(Kind.DECIMAL, Instruction.SUB_DEC),
                tuple(Kind.DATE, Instruction.SUB_DATE_INT));

        private static final EnumMap<Kind, Instruction> subDate = enumMap(tuple(Kind.INT, Instruction.SUB_DATE_INT),
                tuple(Kind.DATE, Instruction.SUB_DATE));

        private static final EnumMap<Kind, Instruction> subTime = enumMap(tuple(Kind.INT, Instruction.SUB_TIME_INT),
                tuple(Kind.DATE_TIME, Instruction.SUB_TIME));

        private static final EnumMap<Kind, Instruction> mul = enumMap(tuple(Kind.INT, Instruction.MULT_INT),
                tuple(Kind.REAL, Instruction.MULT_REAL),
                tuple(Kind.DECIMAL, Instruction.MULT_DEC));

        private static final EnumMap<Kind, Instruction> div = enumMap(tuple(Kind.INT, Instruction.DIV_INT),
                tuple(Kind.REAL, Instruction.DIV_REAL),
                tuple(Kind.DECIMAL, Instruction.DIV_DEC));

        private static final EnumMap<Kind, Instruction> mod = enumMap(tuple(Kind.INT, Instruction.MOD_INT));
    }
}  // end class BinaryExpression
