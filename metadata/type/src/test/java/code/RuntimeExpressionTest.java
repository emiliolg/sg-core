
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package code;

import org.junit.Test;

import tekgenesis.code.Evaluator;
import tekgenesis.expr.ConstantExpression;
import tekgenesis.expr.Expression;
import tekgenesis.expr.SimpleReferenceSolver;
import tekgenesis.type.Types;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class RuntimeExpressionTest {

    //~ Methods ......................................................................................................................................

    @Test public void constantExpressions() {
        final Expression.Implementation expression = ConstantExpression.createConstantExpression(Types.stringType(), "1");

        assertThat(expression.isConstant()).isTrue();
        assertThat(executeExpr(expression)).isEqualTo("1");
    }

    private Object executeExpr(Expression.Implementation e) {
        final SimpleReferenceSolver env = new SimpleReferenceSolver();
        e.compile(env);
        e.bind(env);
        return e.evaluate(new Evaluator(), null);
    }
}
