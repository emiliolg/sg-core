
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.expr;

/**
 * Base Top level Implementations.
 */
public abstract class ExprImpl<T> implements Expr<T> {

    //~ Instance Fields ..............................................................................................................................

    private final String name;

    //~ Constructors .................................................................................................................................

    protected ExprImpl() {
        this("");
    }

    protected ExprImpl(final String name) {
        this.name = name;
    }

    //~ Methods ......................................................................................................................................

    @Override public String asSql(boolean qualify) {
        return accept(new ExpressionBuilder(qualify));
    }

    @Override public String toString() {
        return asSql(true);
    }

    @Override public final String getName() {
        return name;
    }
}
