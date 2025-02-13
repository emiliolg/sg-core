package tekgenesis.showcase;

import java.util.EnumSet;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.common.env.i18n.I18nBundle;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.core.Option;

/** 
 * Generated base class for enum: PropertyType.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public enum PropertyType
    implements Enumeration<PropertyType,String>
{
    /** String */
	STRING("String"),
    /** Int */
	INT("Int"),
    /** Real */
	REAL("Real"),
    /** Boolean */
	BOOLEAN("Boolean"),
    /** Date */
	DATE("Date");

    //~ Fields ...................................................................................................................

    @NotNull private final String label;

    //~ Constructors .............................................................................................................

    PropertyType(@NotNull String label) { this.label = label; }

    //~ Getters ..................................................................................................................

    /** Returns the Property Type Map. */
    @NotNull public static Map<String,PropertyType> map() { return PROPERTY_TYPE_MAP; }

    //~ Methods ..................................................................................................................

    /** Returns the field label in the current locale */
    @NotNull public final String label() { return BUNDLE.getString(name(), label); }

    public int index() { return ordinal(); }

    /** Check that the enum is one of the specified values */
    public final boolean in(@NotNull PropertyType first, @NotNull PropertyType... rest) { return EnumSet.of(first, rest).contains(this); }

    /** Returns the field image path  */
    @NotNull public final String imagePath() { return BUNDLE.getString(name() + ".image", ""); }

    /** Returns the enum primary key */
    @NotNull public final String key() { return name(); }

    /** Get a PropertyType from the name */
    @Nullable public static final Option<PropertyType> fromName(@NotNull String name) { return Option.ofNullable(PROPERTY_TYPE_MAP.get(name)); }

    //~ Fields ...................................................................................................................

    @NotNull private static final Map<String,PropertyType> PROPERTY_TYPE_MAP = Enumerations.buildMap(values());
    @NotNull private static final I18nBundle BUNDLE = I18nBundle.getBundle(PropertyType.class);

}
