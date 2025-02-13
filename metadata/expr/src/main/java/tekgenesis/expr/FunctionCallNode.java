
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.expr;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.code.Fun;
import tekgenesis.code.FunctionRegistry;
import tekgenesis.expr.exception.IllegalOperationException;
import tekgenesis.expr.visitor.ExpressionVisitor;
import tekgenesis.type.Type;

import static java.util.Arrays.asList;

/**
 * Custom functions.
 */
public class FunctionCallNode extends ExpressionAST {

    //~ Instance Fields ..............................................................................................................................

    private final List<ExpressionAST> arguments;

    private Fun<?>       function;
    private final String name;

    //~ Constructors .................................................................................................................................

    /** Creates an ExpressionAST denoting a FunctionCallNode. */

    public FunctionCallNode(final String name, final List<ExpressionAST> arguments) {
        this.name      = name;
        this.arguments = arguments;
        function       = null;
    }

    FunctionCallNode(final String name, final ExpressionAST... expressions) {
        this(name, asList(expressions));
    }

    //~ Methods ......................................................................................................................................

    @Override public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override public void acceptOperands(ExpressionVisitor<?> visitor) {
        for (final ExpressionAST expr : arguments)
            expr.accept(visitor);
    }

    /** Returns false is the function is not constant. */
    public boolean canBeConstant() {
        return function.canBeConstant();
    }

    /** Invoke the function. */
    public Object execute(Object... args) {
        return function.invoke(args);
    }

    /** Return the Arguments of this function. */
    public List<ExpressionAST> getArguments() {
        return arguments;
    }

    @Override public String getName() {
        return name;
    }

    @NotNull @Override protected Type doSolveType(@NotNull RefTypeSolver refResolver) {
        final Type[] argTypes = solveArgumentTypes(refResolver);

        Fun<?> found = null;
        for (final Fun<?> fun : FunctionRegistry.getInstance().functions()) {
            if (fun.getName().equals(name)) {
                found = fun;
                if (argumentsMatch(fun, argTypes)) {
                    function = fun;
                    return function.getReturnType();
                }
            }
        }
        throw new IllegalOperationException(this, found, argTypes);
    }

    private boolean argumentsMatch(Fun<?> fun, Type[] argTypes) {
        final Type[] types = fun.getArgTypes();
        // check args length
        if (types.length != argTypes.length) return false;
        // check args types
        for (int i = 0; i < types.length; i++)
            if (!argTypes[i].equivalent(types[i])) return false;
        return true;
    }

    private Type[] solveArgumentTypes(RefTypeSolver refResolver) {
        final Type[] argTypes = new Type[arguments.size()];
        int          i        = 0;
        for (final ExpressionAST a : arguments)
            argTypes[i++] = a.solveType(refResolver).getType();
        return argTypes;
    }
}  // end class FunctionCallNode
