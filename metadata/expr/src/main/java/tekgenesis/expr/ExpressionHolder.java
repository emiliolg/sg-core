
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;

/**
 * Holds a reference to ExpressionAST. This is a hack to use the GWT CustomFieldSerializer
 */
public class ExpressionHolder implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private final ExpressionAST expression;

    //~ Constructors .................................................................................................................................

    /** Creates the holder to the ref. */
    private ExpressionHolder(@Nullable final ExpressionAST expression) {
        this.expression = expression;
    }

    //~ Methods ......................................................................................................................................

    /** Returns the expression. */
    public Option<ExpressionAST> getExpression() {
        return Option.option(expression);
    }

    //~ Methods ......................................................................................................................................

    /** Create an empty holder. */
    public static ExpressionHolder none() {
        return new ExpressionHolder(null);
    }

    /** Creates a holder for the ref. */
    public static ExpressionHolder some(@NotNull final ExpressionAST expression) {
        return new ExpressionHolder(expression);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -2602390359019872618L;
}
