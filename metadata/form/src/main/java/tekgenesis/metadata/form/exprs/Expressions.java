
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.exprs;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.code.Evaluator;
import tekgenesis.common.logging.Logger;
import tekgenesis.expr.Expression;
import tekgenesis.metadata.form.UiModelRetriever;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.type.assignment.AssignmentType;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.collections.Colls.*;

/**
 * Common utility methods to deal with {@link Expression expressions}.
 */
public class Expressions {

    //~ Constructors .................................................................................................................................

    private Expressions() {}

    //~ Methods ......................................................................................................................................

    /** Bind ui model expressions. */
    public static void bind(@NotNull final UiModelRetriever retriever, @NotNull final UiModel model) {
        new UiModelBinder(retriever).bind(model);
    }

    /** Evaluates a given {@link Expression}. */
    public static Object evaluate(@NotNull Evaluator evaluator, @NotNull Model context, @NotNull Expression e) {
        return e.evaluate(evaluator, context);
    }

    /** Evaluates a List of Assignments {@link Expression}. */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static Iterable<AssignmentType> evaluateAssignmentList(@NotNull Evaluator evaluator, @NotNull Model context, @NotNull Expression e) {
        final Object v = evaluate(evaluator, context, e);
        if (v instanceof Iterable) return cast(v);
        logger.warning("An Iterable was expected from Expression '" + e + "': evaluation returned: '" + v + "'.");
        return emptyIterable();
    }

    /** Evaluates a Boolean {@link Expression}. */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static boolean evaluateBoolean(@NotNull Evaluator evaluator, @NotNull Model context, @NotNull Expression e) {
        final Object v = evaluate(evaluator, context, e);
        if (v instanceof Boolean) return (Boolean) v;
        logger.warning("A Boolean was expected from Expression '" + e + "': evaluation returned: '" + v + "'.");
        return false;
    }

    /** Evaluates a Double {@link Expression}. */
    @Nullable
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static Double evaluateDouble(@NotNull Evaluator evaluator, @NotNull Model context, @NotNull Expression e) {
        final Object v = evaluate(evaluator, context, e);
        if (v == null || v instanceof Double) return (Double) v;
        logger.warning("A Double was expected from Expression '" + e + "': evaluation returned: '" + v + "'.");
        return null;
    }

    /** Evaluates a number {@link Expression}. */
    @Nullable
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static Number evaluateNumber(@NotNull Evaluator evaluator, @NotNull Model context, @NotNull Expression e) {
        final Object v = evaluate(evaluator, context, e);
        if (v == null || v instanceof Number) return (Number) v;
        logger.warning("A Number was expected from Expression '" + e + "': evaluation returned: '" + v + "'.");
        return null;
    }

    /** Evaluates a String {@link Expression}. */
    @Nullable public static String evaluateString(@NotNull Evaluator evaluator, @NotNull Model context, @NotNull Expression e) {
        final Object result = evaluate(evaluator, context, e);
        return result == null ? null : result.toString();
    }

    /** Evaluates a String list {@link Expression}. */
    @Nullable
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static Iterable<String> evaluateStringList(@NotNull Evaluator evaluator, @NotNull Model context, @NotNull Expression e) {
        final Object v = evaluate(evaluator, context, e);
        if (v == null || v instanceof Iterable) return cast(v);
        if (isEmpty(v.toString())) return emptyList();
        if (v instanceof String) return listOf(v.toString());
        logger.warning("An Iterable was expected from Expression '" + e + "': evaluation returned: '" + v + "'.");
        return null;
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(Expressions.class);
}  // end class Expressions
