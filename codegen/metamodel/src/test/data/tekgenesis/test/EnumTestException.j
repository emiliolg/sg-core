package tekgenesis.test;

import tekgenesis.common.core.EnumException;
import java.util.EnumSet;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.common.env.i18n.I18nBundle;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.core.Option;

/** 
 * Generated base class for enum: EnumTestException.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public enum EnumTestException
    implements Enumeration<EnumTestException,String>, EnumException<EnumTestApplicationException>
{
    /** One */
	ONE("One"),
    /** Two */
	TWO("Two");

    //~ Fields ...................................................................................................................

    @NotNull private final String label;

    //~ Constructors .............................................................................................................

    EnumTestException(@NotNull String label) { this.label = label; }

    //~ Getters ..................................................................................................................

    /** Returns the Enum Test Exception Map. */
    @NotNull public static Map<String,EnumTestException> map() { return ENUM_TEST_EXCEPTION_MAP; }

    //~ Methods ..................................................................................................................

    /** Returns the field label in the current locale */
    @NotNull public final String label() { return BUNDLE.getString(name(), label); }

    public int index() { return ordinal(); }

    /** Check that the enum is one of the specified values */
    public final boolean in(@NotNull EnumTestException first, @NotNull EnumTestException... rest) { return EnumSet.of(first, rest).contains(this); }

    /** Returns the field image path  */
    @NotNull public final String imagePath() { return BUNDLE.getString(name() + ".image", ""); }

    /** Returns the enum primary key */
    @NotNull public final String key() { return name(); }

    /** Get a EnumTestException from the name */
    @Nullable public static final Option<EnumTestException> fromName(@NotNull String name) { return Option.ofNullable(ENUM_TEST_EXCEPTION_MAP.get(name)); }

    @Override @NotNull public final EnumTestApplicationException exception() { return new EnumTestApplicationException(this); }

    @Override @NotNull public final EnumTestApplicationException exception(@NotNull Object... args) { return new EnumTestApplicationException(this, args); }

    //~ Fields ...................................................................................................................

    @NotNull private static final Map<String,EnumTestException> ENUM_TEST_EXCEPTION_MAP = Enumerations.buildMap(values());
    @NotNull private static final I18nBundle BUNDLE = I18nBundle.getBundle(EnumTestException.class);

}
