
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
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.code.Code;
import tekgenesis.code.FunctionCall;
import tekgenesis.code.Instruction;
import tekgenesis.code.PredefinedFunctions;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Suppliers;
import tekgenesis.expr.exception.IllegalOperationException;
import tekgenesis.expr.visitor.ExpressionVisitor;
import tekgenesis.type.DecimalType;
import tekgenesis.type.EnumType;
import tekgenesis.type.IntType;
import tekgenesis.type.Kind;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.collections.Maps.enumMap;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.type.Kind.*;

/**
 * Converts the sourceType passed expression to the target sourceType.
 */
public class ConversionOp extends UnaryExpression {

    //~ Instance Fields ..............................................................................................................................

    private Option<Integer> scale = Option.empty();

    //~ Constructors .................................................................................................................................

    protected ConversionOp(ExpressionAST e, Supplier<Type> type) {
        super(Operator.CAST, e, type);
    }

    ConversionOp(Type type, ExpressionAST e) {
        this(e, Suppliers.fromObject(type));
    }

    //~ Methods ......................................................................................................................................

    @Override public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @NotNull @Override public ExpressionAST solveType(@NotNull RefTypeSolver refResolver) {
        final Type target = getTargetType();
        if (target.isEnum()) operand = operand.solveEnumRef((EnumType) target);
        else if (target.isArray() && operand instanceof Ref) ((Ref) operand).setCol(true);

        final Type opType = operand.solveType(refResolver).getType();
        return conversionNeeded(opType) ? super.solveType(refResolver) : operand;
    }

    @Override public String toString() {
        return conversionNeeded(operand.getType()) ? super.toString() : operand.toString();
    }

    @Override public String getName() {
        return getTargetKind().getText();
    }
    /** Return optional scale for Decimal conversion. */
    public Option<Integer> getScale() {
        return scale;
    }

    /** Returns true if the conversion is an implicit one. */
    public boolean isImplicit() {
        final Type sourceType = getOperand().getType();

        final Kind kind = sourceType.getKind();
        switch (getTargetKind()) {
        case STRING:
            return true;
        case REAL:
            return kind == INT || kind == DECIMAL;
        case DECIMAL:
            return kind == INT || kind == REAL;
        default:
            return false;
        }
    }

    @NotNull @Override public Type getTargetType() {
        return notNull(super.getTargetType(), Types.anyType()).getFinalType();
    }

    protected boolean conversionNeeded(Type opType) {
        return getTargetKind() != ANY && !opType.equivalent(getTargetType());
    }

    @NotNull @Override protected Type doSolveType(@NotNull RefTypeSolver refResolver) {
        final Type opType = getOperand().solveType(refResolver).getType();
        final Type t      = getTargetType();
        instruction = findInstruction(opType, t);
        if (instruction == null) throw new IllegalOperationException(this, t, getOperand().getType());
        return t;
    }

    protected Kind getTargetKind() {
        return getTargetType().getKind();
    }

    @Nullable
    @SuppressWarnings("MethodWithMultipleReturnPoints")
    private Code findInstruction(Type sourceType, Type target) {
        if (sourceType.isString()) return fromString.get(target.getKind());

        final Kind sourceKind = sourceType.getKind();

        switch (target.getKind()) {
        case STRING:
            return sourceKind == DATE ? Instruction.TO_DATE_STR : sourceKind == DATE_TIME ? Instruction.TO_TIME_STR : Instruction.TO_STR;
        case REAL:
            return sourceKind == INT ? Instruction.INT_TO_REAL : sourceKind == DECIMAL ? Instruction.DEC_TO_REAL : null;
        case INT:
            return ((IntType) target).isLong() ? toLong(sourceKind) : toInt(sourceKind);
        case DECIMAL:
            if (sourceKind == DECIMAL) {
                scale = some(((DecimalType) target).getDecimals());
                return new FunctionCall(PredefinedFunctions.SCALE_FN);
            }
            else return sourceKind == INT ? Instruction.INT_TO_DEC : sourceKind == REAL ? Instruction.REAL_TO_DEC : null;
        case DATE_TIME:
            return sourceKind == DATE ? Instruction.DATE_TO_DATE_TIME : null;
        case DATE:
            return sourceKind == DATE_TIME ? Instruction.DATE_TIME_TO_DATE : null;
        case TYPE:
            return findInstruction(sourceType, target.getFinalType());
        default:
            return null;
        }
    }

    @Nullable private Instruction toInt(final Kind sourceKind) {
        return sourceKind == REAL ? Instruction.REAL_TO_INT : sourceKind == DECIMAL ? Instruction.DEC_TO_INT : null;
    }

    @Nullable private Code toLong(final Kind sourceKind) {
        return sourceKind == REAL ? Instruction.REAL_TO_LONG
                                  : sourceKind == DECIMAL ? Instruction.DEC_TO_LONG : sourceKind == INT ? Instruction.INT_TO_LONG : null;
    }

    //~ Methods ......................................................................................................................................

    /** Returns an Expression based on a \n String or <code>null</code> if it doesn't exists. */
    @Nullable public static ConversionOp fromTypeName(String name, @NotNull ExpressionAST operand) {
        final Type type = Types.fromString(name);
        return type == Types.nullType() ? null : new ConversionOp(type, operand);
    }

    //~ Static Fields ................................................................................................................................

    private static final EnumMap<Kind, Instruction> fromString = enumMap(tuple(BOOLEAN, Instruction.STR_TO_BOOL),
            tuple(DATE_TIME, Instruction.STR_TO_TIME),
            tuple(DATE, Instruction.STR_TO_DATE),
            tuple(REAL, Instruction.STR_TO_REAL),
            tuple(INT, Instruction.STR_TO_INT),
            tuple(DECIMAL, Instruction.STR_TO_DEC));
}  // end class ConversionOp
