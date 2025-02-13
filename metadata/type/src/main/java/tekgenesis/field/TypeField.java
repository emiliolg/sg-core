
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.field;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.check.Check;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.StrBuilder;
import tekgenesis.common.core.Strings;
import tekgenesis.expr.Expression;
import tekgenesis.type.CompositeType;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.Strings.capitalizeFirst;
import static tekgenesis.common.core.Strings.fromCamelCase;
import static tekgenesis.field.FieldOption.*;

/**
 * A Basic Implementation of a Model Field.
 */
public class TypeField implements ModelField, Serializable, Signed {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final String name;

    @NotNull private final FieldOptions options;

    @NotNull private Type type;

    //~ Constructors .................................................................................................................................

    /** default constructor.* */
    protected TypeField() {
        name    = "";
        options = FieldOptions.EMPTY;
        type    = Types.nullType();
    }

    /** Copy constructor. */
    private TypeField(TypeField other) {
        this(other.name, other.type, other.options);
    }

    /** Create a basic Type Field. */
    public TypeField(String name, Type type) {
        this(name, type, FieldOptions.EMPTY);
    }

    /** Create a basic Type Field. */
    public TypeField(@NotNull String name, @NotNull Type type, @NotNull FieldOptions options) {
        this.name    = name;
        this.type    = type;
        this.options = options;
    }

    //~ Methods ......................................................................................................................................

    /** The Getter name for a field of this type. */
    @NotNull public final String getterName() {
        return Strings.getterName(getName(), getImplementationClassName());
    }

    @Override public boolean hasChildren() {
        return false;
    }

    /** Returns <code>true</code> if the typeField has the specified option. */
    public boolean hasOption(FieldOption option) {
        return options.getBoolean(option);
    }

    /**
     * Returns the set of simple (Non composite) fields for the present type. Only the PrimaryKey
     * Fields are included for Entities
     */
    @NotNull
    @SuppressWarnings("WeakerAccess")
    public ImmutableList<TypeField> retrieveSimpleFields() {
        if (!isComposite()) return listOf(this);

        final ImmutableList.Builder<TypeField> result = ImmutableList.builder();

        int                i       = 0;
        final List<String> columns = options.getStrings(FieldOption.COLUMN);
        for (final TypeField a : ((CompositeType) getFinalType()).retrieveSimpleFields()) {
            final String columnName = i < columns.size() ? columns.get(i) : "";
            result.add(Chained.createChained(this, a, columnName));
            i++;
        }

        return result.build();
    }

    @Override public String toString() {
        return getName();
    }

    /** Returns the expressions to check the field. */
    @NotNull public Check.List getCheck() {
        return options.getCheck(CHECK);
    }

    @NotNull @Override public ImmutableList<? extends ModelField> getChildren() {
        return Colls.emptyList();
    }

    /** The name of the database column without qualification. */
    @NotNull public String getColumnName() {
        final List<String> columnName = options.getStrings(COLUMN);
        return columnName.isEmpty() ? Types.getConstrainedName(fromCamelCase(getSimpleName())) : columnName.get(0);
    }

    /** Returns true if expression is always required. */
    public boolean isRequired() {
        return getOptional() == Expression.FALSE;
    }

    /** Returns if it's signed or not. */
    @Override public boolean isSigned() {
        return options.hasOption(SIGNED);
    }

    public boolean isSynthesized() {
        return false;
    }

    /** Returns the expression to calculate the field default value. */
    @NotNull public Expression getDefaultValue() {
        return options.getExpression(DEFAULT);
    }

    /** Returns true if the type is a composite one (Struct or Entity). */
    public boolean isComposite() {
        return getType().isComposite();
    }

    /** Returns <code>true</code> if the field is 'multiple' (a Collection of references). */
    public boolean isMultiple() {
        return false;
    }

    /** returns true if the Type is a Type. */
    public final boolean isType() {
        return getFinalType().isType();
    }

    /** Returns the documentation of the field. */
    @NotNull @Override public String getFieldDocumentation() {
        return options.getString(FIELD_DOCUMENTATION);
    }

    /** Returns the field full name. */
    public String getFullName() {
        return getName();
    }

    /** returns the Class that implements the field type. */
    @NotNull public String getImplementationClassName() {
        return getType().getImplementationClassName();
    }

    /** Returns true if expression is always optional. */
    public boolean isOptional() {
        return !isRequired();
    }

    @NotNull @Override public String getLabel() {
        return options.getString(LABEL);
    }

    /** returns true if the Type is a Enum. */
    public boolean isEnum() {
        return getType().isEnum();
    }

    /** Returns the field mask. */
    @NotNull public Expression getMask() {
        return options.getExpression(CUSTOM_MASK);
    }

    @NotNull @Override public String getName() {
        return getSimpleName();
    }

    /** Returns <code>true</code> if the type field is optional. */
    public Expression getOptional() {
        return options.getExpression(OPTIONAL);
    }

    @NotNull @Override public final FieldOptions getOptions() {
        return options;
    }

    /**
     * For common fields the name of the field, for qualified ones, the name without qualification.
     */
    @NotNull
    @SuppressWarnings("WeakerAccess")
    public final String getSimpleName() {
        return name;
    }

    /** The target column Name when the field refers to another entity field. */
    public String getTargetColumnName() {
        return "";
    }
    /** The target attribute Name when the field refers to another entity field. */
    public String getTargetName() {
        return "";
    }

    @NotNull @Override public final Type getType() {
        return type;
    }

    /** Only when solving undefined references. */
    public void setType(@NotNull Type type) {
        // if (!(this.type instanceof UnresolvedTypeReference)) throw new IllegalStateException();
        this.type = type;

        if (type instanceof ModelField) options.putAll(((ModelField) type).getOptions());
    }

    /** Returns the attribute type as an String. */
    @NotNull public final String getTypeAsString() {
        return getType().toString();
    }

    /** returns true if the Type is a View. */
    public final boolean isView() {
        return getFinalType().isView();
    }

    /** returns true if the Type is an Entity. */
    public final boolean isEntity() {
        return getFinalType().isEntity();
    }

    /** Returns true if the attribute is read only. */
    public boolean isReadOnly() {
        return hasOption(READ_ONLY);
    }

    //~ Methods ......................................................................................................................................

    /**
     * Returns the set of simple (Non composite, eventually Qualified) fields for the specified
     * Fields. Only the PrimaryKey Fields are included for Entities
     */
    public static ImmutableList<TypeField> retrieveSimpleFields(Iterable<? extends TypeField> fields) {
        return ImmutableList.build(builder -> fields.forEach(f -> builder.addAll(f.retrieveSimpleFields())));
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 6871525126762063057L;

    //~ Inner Classes ................................................................................................................................

    @SuppressWarnings("FieldMayBeFinal")
    public static class Chained extends TypeField {
        private final String columnName;
        private TypeField    field;
        private TypeField    parent;

        /** constructor.* */
        Chained() {
            field      = null;
            parent     = null;
            columnName = "";
        }

        private Chained(@NotNull TypeField parent, @NotNull TypeField field, final String columnName) {
            super(field);
            this.field      = field;
            this.parent     = parent;
            this.columnName = columnName;
        }

        @NotNull @Override public String getColumnName() {
            if (!columnName.isEmpty()) return columnName;
            final List<TypeField> parts  = getParts();
            final StrBuilder      result = new StrBuilder().startCollection("_");
            for (final TypeField part : parts)
                result.appendElement(fromCamelCase(part.getSimpleName()));
            return Types.getConstrainedName(result.toString());
        }

        @NotNull @Override public String getName() {
            return parent.getName() + capitalizeFirst(field.getSimpleName());
        }

        @Override public Expression getOptional() {
            return parent.getOptional();
        }

        /** Return the parts as a List. */
        @NotNull public List<TypeField> getParts() {
            final LinkedList<TypeField> result = new LinkedList<>();

            TypeField f = this;
            while (f instanceof Chained) {
                final Chained q = (Chained) f;
                result.addFirst(q.field);
                f = q.parent;
            }
            result.addFirst(f);
            return result;
        }

        @Override public String getTargetColumnName() {
            final List<TypeField> parts = getParts();
            parts.remove(0);
            final StrBuilder result = new StrBuilder().startCollection("_");
            for (final TypeField part : parts)
                result.appendElement(part.getColumnName());
            return Types.getConstrainedName(result.toString());
        }

        private static Chained createChained(@NotNull TypeField parent, @NotNull TypeField field, final String columnName) {
            if (field instanceof Chained) {
                final Chained c = (Chained) field;
                return createChained(createChained(parent, c.parent, c.columnName), c.field, columnName);
            }

            return new Chained(parent, field, columnName);
        }

        private static final long serialVersionUID = 1737525653508054522L;
    }  // end class Chained
}  // end class TypeField
