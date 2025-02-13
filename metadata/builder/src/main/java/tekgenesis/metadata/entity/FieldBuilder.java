
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.entity;

import java.util.List;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import tekgenesis.aggregate.AggregateFn;
import tekgenesis.check.CheckMsg;
import tekgenesis.check.CheckType;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.expr.Expression;
import tekgenesis.expr.ExpressionAST;
import tekgenesis.field.FieldOption;
import tekgenesis.field.FieldOptions;
import tekgenesis.field.HasFieldOption;
import tekgenesis.field.MetaModelReference;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.exception.InvalidOptionValueException;
import tekgenesis.type.Type;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.field.FieldOption.*;

/**
 * Base class of Attribute and WidgetBuilder.
 */
@SuppressWarnings({ "UnusedReturnValue", "WeakerAccess" })
public abstract class FieldBuilder<This extends FieldBuilder<This>> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final FieldOptions options;

    //~ Constructors .................................................................................................................................

    protected FieldBuilder() {
        this(new FieldOptions());
    }

    protected FieldBuilder(@NotNull final FieldOptions options) {
        this.options = options;
    }

    //~ Methods ......................................................................................................................................

    /** Add a check expression. */
    public This check(@NotNull Expression e, @NotNull String message)
        throws BuilderException
    {
        return check(e, new CheckMsg(message));
    }

    /** Add a check expression. */
    public This check(@NotNull ExpressionAST e, @NotNull String message)
        throws BuilderException
    {
        return check(e.createExpression(), new CheckMsg(message));
    }

    /** Add a check to the field. */
    public This check(@NotNull final Expression checkExpr, final boolean inline, @NotNull final CheckType checkType, @NotNull final String msg)
        throws BuilderException
    {
        return check(checkExpr, new CheckMsg(inline, checkType, msg));
    }

    /** Set Field identification. */
    public This id(@NotNull final String id) {
        try {
            return with(ID, id);
        }
        catch (final BuilderException e) {
            // Id is supported by all Widgets
            throw new IllegalStateException(e);
        }
    }

    /** Set Field label. */
    public This label(@NotNull final String label) {
        try {
            return with(LABEL, label);
        }
        catch (final BuilderException e) {
            // Label is supported by all Widgets
            throw new IllegalStateException(e);
        }
    }

    /** Sets a Boolean option to true. */
    public This with(FieldOption option)
        throws BuilderException
    {
        return withOption(option, Boolean.TRUE);
    }

    /** Sets a String Option. */
    public This with(FieldOption option, String str)
        throws BuilderException
    {
        return withOption(option, str);
    }

    /** Sets a String Option. */
    public This with(FieldOption option, Function<?, ?> supplierRef)
        throws BuilderException
    {
        return withOption(option, supplierRef);
    }

    /** Sets an Enum Option. */
    public This with(FieldOption option, Enum<?> e)
        throws BuilderException
    {
        return withOption(option, e);
    }

    /** Sets an Integer Option. */
    public This with(FieldOption option, int n)
        throws BuilderException
    {
        return withOption(option, n);
    }

    /** Set an Expression Option. */
    public This with(FieldOption option, Expression expr)
        throws BuilderException
    {
        return withOption(option, expr);
    }

    /** Set an MetaModel Reference Option. */
    public This with(FieldOption option, MetaModelReference reference)
        throws BuilderException
    {
        return withOption(option, reference);
    }

    /** Set an Type Option. */
    public This with(FieldOption option, Type type)
        throws BuilderException
    {
        return withOption(option, type);
    }

    /** Should only be used from the AST Builder . The argument must be previously checked */
    public void withArgument(Tuple<FieldOption, Object> arg) {
        options.put(arg.first(), arg.second());
    }

    /** Sets an Enum Option of the specified class. */
    public <T extends Enum<T>> This withEnum(FieldOption option, Class<T> c, String txt)
        throws BuilderException
    {
        try {
            return with(option, Enumerations.valueOf(c, txt.toUpperCase()));
        }
        catch (final RuntimeException e) {
            throw new InvalidOptionValueException(getName(), option.getId(), txt);
        }
    }

    /**
     * Add the specified documentation of this field. For now, it will be used only Entity Fields.
     */
    public This withFieldDocumentation(@NotNull String content) {
        options.put(FIELD_DOCUMENTATION, content);
        return cast(this);
    }

    /** Add the specified field option. */
    public This withOption(FieldOption option, Object obj)
        throws BuilderException
    {
        checkOptionSupport(option);
        options.put(option, obj);
        return cast(this);
    }

    /** Returns true of the attribute is optional. */
    public boolean isOptional() {
        return options.getExpression(OPTIONAL) == Expression.TRUE;
    }

    /** Returns the Field Options. */
    @NotNull public FieldOptions getOptions() {
        return options;
    }

    protected void addDefaultArguments(List<HasFieldOption> arguments) {
        for (final HasFieldOption arg : arguments) {
            final FieldOption opt = arg.getFieldOption();
            if (!hasOption(opt)) {
                final Object defaultValue = arg.getDefaultValue();
                if (defaultValue != null) options.put(opt, defaultValue);
            }
        }
    }

    protected This aggregate(@NotNull final Expression expr, @NotNull final AggregateFn fn, @NotNull final String ref)
        throws BuilderException
    {
        withOption(AGGREGATE, options.getAggregate(AGGREGATE).add(expr, fn, ref));
        return cast(this);
    }

    protected This check(Expression checkExpr, CheckMsg msg)
        throws BuilderException
    {
        withOption(CHECK, options.getCheck(CHECK).addCheck(checkExpr, msg));

        return cast(this);
    }

    /** Check if the option is supported. */
    protected abstract void checkOptionSupport(FieldOption option)
        throws BuilderException;

    protected boolean hasOption(FieldOption opt) {
        return options.hasOption(opt);
    }

    protected String getName() {
        return options.getString(ID);
    }

    boolean getBooleanOption(FieldOption option) {
        return options.getBoolean(option);
    }
}  // end class FieldBuilder
