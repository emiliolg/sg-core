
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

import tekgenesis.code.Instruction;
import tekgenesis.common.core.Suppliers;
import tekgenesis.expr.exception.IllegalOperationException;
import tekgenesis.expr.visitor.ExpressionVisitor;
import tekgenesis.type.Type;

import static tekgenesis.type.Types.booleanType;

/**
 * A forbidden ExpressionAST that holds a permission.
 */
public class ForbiddenExpression extends ExpressionAST {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final String permission;

    //~ Constructors .................................................................................................................................

    /** Creates a ExpressionAST with only one permission. */
    public ForbiddenExpression(@NotNull String permission) {
        super(Instruction.FORBIDDEN, Suppliers.fromObject(booleanType()));
        this.permission = permission;
    }

    //~ Methods ......................................................................................................................................

    @Override public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public final void acceptOperands(ExpressionVisitor<?> visitor) {
        visitor.visit(this);
    }

    @Override public String getName() {
        return Instruction.FORBIDDEN.name().toLowerCase();
    }

    /** Returns permission name of the forbidden expression. */
    @NotNull public String getPermission() {
        return permission;
    }

    @NotNull @Override protected Type doSolveType(RefTypeSolver refResolver) {
        doSolvePermissionRef(refResolver);
        return booleanType();
    }

    private void doSolvePermissionRef(RefTypeSolver refResolver) {
        if (!(refResolver instanceof RefPermissionSolver))
            throw new IllegalOperationException(this, "The 'forbidden' operation can only be used inside Forms.");

        if (!((RefPermissionSolver) refResolver).hasPermission(permission))
            throw new IllegalOperationException(this, "Permission '" + permission + "' not declared on form.");
    }
}  // end class ForbiddenExpression
