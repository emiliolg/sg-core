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
 * Generated base class for enum: Transmission.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public enum Transmission
    implements Enumeration<Transmission,String>
{
    /** Manual */
	MANUAL("Manual"),
    /** Automatic */
	AUTOMATIC("Automatic"),
    /** Semiautomatic */
	SEMIAUTOMATIC("Semiautomatic"),
    /** Manumatic */
	MANUMATIC("Manumatic");

    //~ Fields ...................................................................................................................

    @NotNull private final String label;

    //~ Constructors .............................................................................................................

    Transmission(@NotNull String label) { this.label = label; }

    //~ Getters ..................................................................................................................

    /** Returns the Transmission Map. */
    @NotNull public static Map<String,Transmission> map() { return TRANSMISSION_MAP; }

    //~ Methods ..................................................................................................................

    /** Returns the field label in the current locale */
    @NotNull public final String label() { return BUNDLE.getString(name(), label); }

    public int index() { return ordinal(); }

    /** Check that the enum is one of the specified values */
    public final boolean in(@NotNull Transmission first, @NotNull Transmission... rest) { return EnumSet.of(first, rest).contains(this); }

    /** Returns the field image path  */
    @NotNull public final String imagePath() { return BUNDLE.getString(name() + ".image", ""); }

    /** Returns the enum primary key */
    @NotNull public final String key() { return name(); }

    /** Get a Transmission from the name */
    @Nullable public static final Option<Transmission> fromName(@NotNull String name) { return Option.ofNullable(TRANSMISSION_MAP.get(name)); }

    //~ Fields ...................................................................................................................

    @NotNull private static final Map<String,Transmission> TRANSMISSION_MAP = Enumerations.buildMap(values());
    @NotNull private static final I18nBundle BUNDLE = I18nBundle.getBundle(Transmission.class);

}
