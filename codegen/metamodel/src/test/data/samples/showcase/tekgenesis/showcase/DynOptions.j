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
 * Generated base class for enum: DynOptions.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public enum DynOptions
    implements Enumeration<DynOptions,String>
{
    /** String */
	STRING("String"),
    /** String5 */
	STRING5("String5"),
    /** Int */
	INT("Int"),
    /** Int5 */
	INT5("Int5"),
    /** Real */
	REAL("Real"),
    /** Decimal */
	DECIMAL("Decimal"),
    /** Decimal(4,2) */
	DECIMAL42("Decimal(4,2)"),
    /** DateOnly */
	DATE("DateOnly"),
    /** String Secret */
	STRING_SECRET("String Secret");

    //~ Fields ...................................................................................................................

    @NotNull private final String label;

    //~ Constructors .............................................................................................................

    DynOptions(@NotNull String label) { this.label = label; }

    //~ Getters ..................................................................................................................

    /** Returns the Dyn Options Map. */
    @NotNull public static Map<String,DynOptions> map() { return DYN_OPTIONS_MAP; }

    //~ Methods ..................................................................................................................

    /** Returns the field label in the current locale */
    @NotNull public final String label() { return BUNDLE.getString(name(), label); }

    public int index() { return ordinal(); }

    /** Check that the enum is one of the specified values */
    public final boolean in(@NotNull DynOptions first, @NotNull DynOptions... rest) { return EnumSet.of(first, rest).contains(this); }

    /** Returns the field image path  */
    @NotNull public final String imagePath() { return BUNDLE.getString(name() + ".image", ""); }

    /** Returns the enum primary key */
    @NotNull public final String key() { return name(); }

    /** Get a DynOptions from the name */
    @Nullable public static final Option<DynOptions> fromName(@NotNull String name) { return Option.ofNullable(DYN_OPTIONS_MAP.get(name)); }

    //~ Fields ...................................................................................................................

    @NotNull private static final Map<String,DynOptions> DYN_OPTIONS_MAP = Enumerations.buildMap(values());
    @NotNull private static final I18nBundle BUNDLE = I18nBundle.getBundle(DynOptions.class);

}
