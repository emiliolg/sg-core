
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.entity;

import org.jetbrains.annotations.NotNull;

import tekgenesis.expr.Expression;
import tekgenesis.field.FieldOption;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.exception.IncompatibleOptionsException;
import tekgenesis.type.MetaModel;
import tekgenesis.type.Type;

import static tekgenesis.common.Predefined.cast;

/**
 * A Builder for Fields in a Composite (Struct or Entity).
 */
@SuppressWarnings("UnusedReturnValue")
public class CompositeFieldBuilder<This extends CompositeFieldBuilder<This>> extends FieldBuilder<This> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final String name;
    @NotNull private final Type   type;

    //~ Constructors .................................................................................................................................

    protected CompositeFieldBuilder(@NotNull String name, @NotNull Type type) {
        this.name = name;
        this.type = type;
    }

    //~ Methods ......................................................................................................................................

    /** Build a Field. */
    public TypeField build(MetaModel model) {
        return new TypeField(getName(), getType(), getOptions());
    }

    /** Sets the default value expression. */
    public This defaultValue(Expression expr)
        throws BuilderException
    {
        return with(FieldOption.DEFAULT, expr);
    }

    /** Sets the attribute as optional. */
    @NotNull public This optional()
        throws BuilderException
    {
        return with(FieldOption.OPTIONAL, Expression.TRUE);
    }

    /** Sets the attribute as read only. */
    @NotNull public This readOnly()
        throws BuilderException
    {
        return with(FieldOption.READ_ONLY);
    }

    /**
     * Set this Attribute as 'Serial' with the specified Sequence Name. Its value will define the
     * entity identity.
     */
    public This serial(final String text) {
        return cast(this);
    }

    @NotNull public String getName() {
        return name;
    }

    /** Get the Field Type. */
    @NotNull public Type getType() {
        return type;
    }

    @Override protected void checkOptionSupport(FieldOption option)
        throws BuilderException
    {
        if (option == FieldOption.SIGNED && !type.getFinalType().isNumber())
            throw new IncompatibleOptionsException(FieldOption.SIGNED, type.getFinalType(), name);

        if (option == FieldOption.CUSTOM_MASK && !type.getFinalType().isString())
            throw new IncompatibleOptionsException(FieldOption.CUSTOM_MASK, type.getFinalType(), name);
    }
}  // end class CompositeFieldBuilder
