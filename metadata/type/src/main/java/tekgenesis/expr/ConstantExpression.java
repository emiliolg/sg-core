
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.expr;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.code.Binder;
import tekgenesis.code.Evaluator;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.Option;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.type.Kind;
import tekgenesis.type.Type;

import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.core.Option.option;
import static tekgenesis.type.Types.*;

/**
 * An Optimized Implementation for Constant Expressions.
 */
public class ConstantExpression implements Expression.Implementation {

    //~ Instance Fields ..............................................................................................................................

    private final Type type;

    private final Object value;

    //~ Constructors .................................................................................................................................

    ConstantExpression(Type type, Object value) {
        this.type  = type;
        this.value = value;
    }

    //~ Methods ......................................................................................................................................

    @Override public void bind(Binder binder) {}

    @Override public void compile(RefTypeSolver solver) {}

    @Override public Object evaluate(@NotNull Evaluator evaluator, Object context) {
        return value;
    }

    @Override public List<String> extractStrings() {
        return value instanceof String ? Collections.singletonList((String) value) : Collections.emptyList();
    }

    @Override public Expression.Implementation reMapReferences(final RefContextMapper mapper) {
        return this;
    }

    @Override public Expression.Implementation replaceStrings(List<String> strings) {
        return value instanceof String && !strings.isEmpty() ? new ConstantExpression(type, strings.get(0)) : this;
    }

    @Override public Collection<String> retrieveReferences() {
        return Colls.emptyList();
    }

    @Override public void serialize(StreamWriter w) {
        Kind.serializeType(w, type.getFinalType());
        w.writeObjectConst(value);
    }

    @Override public String toString() {
        return String.valueOf(value);
    }

    @Override public Option<Object> getConstantValue() {
        return option(value);
    }

    @Override public boolean isCompiled() {
        return true;
    }
    @Override public boolean isConstant() {
        return true;
    }

    @Override public Type getType() {
        return type;
    }

    //~ Methods ......................................................................................................................................

    /** Create a Constant Expression. */
    public static Expression.Implementation createConstantExpression(Type type, @Nullable final Object value) {
        for (final ConstantExpression i : singletonImplementations) {
            if (equal(i.value, value)) return i;
        }
        return new ConstantExpression(type, value);
    }

    //~ Static Fields ................................................................................................................................

    public static final ConstantExpression NULL_IMPL  = new ConstantExpression(nullType(), null);
    public static final ConstantExpression TRUE_IMPL  = new ConstantExpression(booleanType(), true);
    public static final ConstantExpression FALSE_IMPL = new ConstantExpression(booleanType(), false);
    public static final ConstantExpression EMPTY_IMPL = new ConstantExpression(stringType(), "");

    private static final ConstantExpression[] singletonImplementations = {
        NULL_IMPL,
        TRUE_IMPL,
        FALSE_IMPL,
        EMPTY_IMPL,
    };
}  // end class ConstantExpression
