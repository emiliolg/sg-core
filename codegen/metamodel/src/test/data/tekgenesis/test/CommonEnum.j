package tekgenesis.test;

import java.util.EnumSet;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.common.env.i18n.I18nBundle;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.core.Option;

/** 
 * Generated base class for enum: CommonEnum.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public enum CommonEnum
    implements Enumeration<CommonEnum,String>
{
    /** One */
	ONE("One"),
    /** Two */
	TWO("Two");

    //~ Fields ...................................................................................................................

    @NotNull private final String label;

    //~ Constructors .............................................................................................................

    CommonEnum(@NotNull String label) { this.label = label; }

    //~ Getters ..................................................................................................................

    /** Returns the Common Enum Map. */
    @NotNull public static Map<String,CommonEnum> map() { return COMMON_ENUM_MAP; }

    //~ Methods ..................................................................................................................

    /** Returns the field label in the current locale */
    @NotNull public final String label() { return BUNDLE.getString(name(), label); }

    public int index() { return ordinal(); }

    /** Check that the enum is one of the specified values */
    public final boolean in(@NotNull CommonEnum first, @NotNull CommonEnum... rest) { return EnumSet.of(first, rest).contains(this); }

    /** Returns the field image path  */
    @NotNull public final String imagePath() { return BUNDLE.getString(name() + ".image", ""); }

    /** Returns the enum primary key */
    @NotNull public final String key() { return name(); }

    /** Get a CommonEnum from the name */
    @Nullable public static final Option<CommonEnum> fromName(@NotNull String name) { return Option.ofNullable(COMMON_ENUM_MAP.get(name)); }

    //~ Fields ...................................................................................................................

    @NotNull private static final Map<String,CommonEnum> COMMON_ENUM_MAP = Enumerations.buildMap(values());
    @NotNull private static final I18nBundle BUNDLE = I18nBundle.getBundle(CommonEnum.class);

}
