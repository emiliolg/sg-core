
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.expr;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.Suppliers;
import tekgenesis.expr.exception.IllegalOperationException;
import tekgenesis.expr.visitor.ExpressionVisitor;
import tekgenesis.type.ArrayType;
import tekgenesis.type.EnumType;
import tekgenesis.type.Type;

import static tekgenesis.code.Instruction.ASSIGNMENT;
import static tekgenesis.md.MdConstants.DEPRECATED_FIELD;

/**
 * Expression representing an assignment with equals or non-equals (a = b, a != b, a = b when bool,
 * a = (c, d) when bool).
 */
public class AssignmentExpression extends ExpressionAST {

    //~ Instance Fields ..............................................................................................................................

    private final boolean               eq;
    private final ExpressionAST         field;
    private ExpressionAST               value;
    private final Option<ExpressionAST> when;

    //~ Constructors .................................................................................................................................

    /** Construct assignment with optional when expression and. */
    public AssignmentExpression(ExpressionAST field, ExpressionAST value, Option<ExpressionAST> when, boolean eq) {
        super(ASSIGNMENT, Suppliers.empty());
        this.field = field;
        this.value = value;
        this.when  = when;
        this.eq    = eq;
    }

    //~ Methods ......................................................................................................................................

    @Override public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public void acceptOperands(ExpressionVisitor<?> visitor) {
        field.acceptOperands(visitor);
        value.acceptOperands(visitor);
        when.ifPresent(expr -> expr.acceptOperands(visitor));
    }

    /** Check if assignment has when expression. */
    public boolean hasWhen() {
        return when.isPresent();
    }

    @NotNull @Override public ExpressionAST solveType(@NotNull RefTypeSolver refResolver) {
        field.solveType(refResolver);

        Type fieldType = field.getType().getFinalType();
        if (fieldType.isArray()) fieldType = ((ArrayType) fieldType).getElementType();

        if (fieldType.isEnum()) value = value.solveEnumRef((EnumType) fieldType);
        value.solveType(refResolver);

        final Type valueType = value.getType().getFinalType();
        final Type t         = valueType.isArray() ? ((ArrayType) valueType).getElementType() : valueType;
        if (!DEPRECATED_FIELD.equals(field.getName()) && !hasValidType(t, fieldType))
            throw new IllegalOperationException(value, fieldType, valueType);

        when.ifPresent(expr -> expr.solveType(refResolver));
        return super.solveType(refResolver);
    }

    /** Field reference expression. */
    public ExpressionAST getFieldExpr() {
        return field;
    }

    @Override public String getName() {
        return "assignment expression";
    }

    /** Check if expression belongs to an equal or a non-equal assignment. */
    public boolean isEquals() {
        return eq;
    }

    /** Assignation value expression. */
    public ExpressionAST getValueExpr() {
        return value;
    }

    /** When expression if present. */
    public Option<ExpressionAST> getWhenExpr() {
        return when;
    }

    @NotNull @Override protected Type doSolveType(RefTypeSolver refResolver) {
        final Type type = value.doSolveType(refResolver);
        return type.isArray() ? ((ArrayType) type).getElementType() : type;
    }

    @Override void setTargetType(@Nullable Type t) {
        super.setTargetType(t);
        field.setTargetType(t);
    }

    //~ Methods ......................................................................................................................................

    @SuppressWarnings("SimplifiableIfStatement")
    static boolean hasValidType(Type t, Type fieldType) {
        if (fieldType.isDatabaseObject() && t.isString()) return true;
        if (!fieldType.equivalent(t)) return false;

        return !fieldType.isDatabaseObject() || fieldType.getImplementationClassName().equals(t.getImplementationClassName());
    }
}  // end class AssignmentExpression
