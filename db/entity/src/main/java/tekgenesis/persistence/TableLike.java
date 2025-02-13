
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.util.Collection;

import tekgenesis.persistence.expr.Expr;

/**
 * {@link DbTable} and {@link Select} implement this "interface": (We defined it as a class to avoid
 * making members public)
 */
public abstract class TableLike<T> {

    //~ Methods ......................................................................................................................................

    /** The SubQuery as an Sql. */
    abstract String asTableExpression();

    /** The Field list for a single table query. */
    abstract Collection<TableField<?>> fields();

    /** The Original 'from' DbTable. */
    abstract <I extends EntityInstance<I, K>, K> DbTable<I, K> getDbTable();

    /** The Is Single table? */
    abstract boolean isSingleTable();

    /** The List of expressions. */
    abstract Expr<?>[] getExpressions();

    /** The class of the result objects. */
    abstract Class<T> getType();
}
