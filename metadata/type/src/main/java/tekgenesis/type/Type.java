
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;

/**
 * This Interface describes a Type to be used in the definition of expressions, entities, forms,
 * etc.
 */
public interface Type extends Serializable {

    //~ Methods ......................................................................................................................................

    /** Return the type but with the specified parameters applied. */
    Type applyParameters(Seq<String> strings);

    /** Returns the value of the type represented by the given String or null. */
    @Nullable String asString(@Nullable Object obj);

    /**
     * Returns the common (most general) super type between this type and the one passed as a
     * parameter or { @link Types.any() } if there si no super-type but Object.
     */
    @NotNull Type commonSuperType(@NotNull Type that);

    /**
     * A 'relaxed' equals comparison between types. For example all StringType objects are
     * equivalent regardless there length
     */
    boolean equivalent(Type t);

    /** Returns the value of the type represented by the given String. */
    Object valueOf(String str);

    /** Returns true if the type is Undefined. */
    boolean isUndefined();

    /** Returns true if type is Void. */
    boolean isVoid();

    /** Returns the default value of the type. */
    @Nullable Object getDefaultValue();

    /** Returns true if the type is a composite one (Struct or Entity). */
    boolean isComposite();

    /** Returns true if the type is a Resource. */
    boolean isResource();
    /**
     * Returns true if the type is considered a time. Both {@link DateOnlyType} and
     * {@link DateTimeType}, are considered times.
     */
    boolean isTime();

    /** Returns true if the type is of kind {@link Kind#TYPE}. */
    boolean isType();

    /** Get the final (Effective) type of the type. */
    Type getFinalType();

    /** Returns true if the type is a {@link StringType}. */
    boolean isString();

    /**
     * Returns an {@link Option} to the {@link Class} used to represent this Kind or
     * {@link Option#empty()} if its not defined.
     */

    @Nullable Class<?> getImplementationClass();

    /**
     * Returns the name of the {@link Class} used to represent this Kind or the empty String if its
     * not defined.
     */
    String getImplementationClassName();

    /** returns the Type's kind. */
    @NotNull
    @SuppressWarnings("NullableProblems")
    Kind getKind();

    /** Returns true if the type is an Html. */
    boolean isHtml();

    /** Returns true if the type is Null. */
    boolean isNull();

    /** Returns an {@link Option} to the length or {@link Option#empty()} if its not defined. */
    Option<Integer> getLength();

    /** Returns true if the type is any kind of Enum. */
    boolean isEnum();

    /** Returns true if the type is a {@link BooleanType}. */
    boolean isBoolean();

    /** Return the number of allowed parameters. */
    int getParametersCount();

    /** Returns true if the type is a Inner type. */
    boolean isInner();

    /**
     * Returns true if the type is considered a number. All of {@link IntType}, {@link RealType},
     * {@link DecimalType} are considered numbers.
     */
    boolean isNumber();

    /**
     * The SQL Implementation type as an String Returns the empty string if not suitable
     * implementation exists.
     *
     * @param  multiple
     */
    @NotNull String getSqlImplementationType(boolean multiple);

    /** The SQL Implementation type as a int defined in {@link java.sql.Types}. */
    int getSqlType();

    /** Returns true if the type is a DatabaseObject. */
    boolean isDatabaseObject();

    /** Returns true if type is a View. */
    boolean isView();

    /** Returns true if the type is a {@link AnyType}. */
    boolean isAny();

    /** Returns true if the type is any kind of Array. */
    boolean isArray();

    /** Returns true if the type is an Entity. */
    boolean isEntity();
}  // end interface Type
