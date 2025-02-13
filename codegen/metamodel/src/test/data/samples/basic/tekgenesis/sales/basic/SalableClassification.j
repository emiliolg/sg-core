package tekgenesis.sales.basic;

import java.math.BigDecimal;
import java.util.EnumSet;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.common.env.i18n.I18nBundle;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.core.Option;

/** 
 * Enum Salable Classification documentation
 * <b> Default comission amount </b>
 * 
 * Generated base class for enum: SalableClassification.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public enum SalableClassification
    implements Enumeration<SalableClassification,String>
{
    /** A documentation */
	A("A", new BigDecimal("1.80")),
    /** B documentation */
	B("B", new BigDecimal("1.30")),
    /** C documentation */
	C("C", new BigDecimal("1.20")),
    /** D documentation */
	D("D", new BigDecimal("0.90")),
    /** E documentation */
	E("E", new BigDecimal("0.50")),
    /** Z */
	Z("Z", new BigDecimal("0.50")),
    /** This is invalid */
	INVALID("Invalid", new BigDecimal("0"));

    //~ Fields ...................................................................................................................

    @NotNull private final String label;
    @NotNull private final BigDecimal defaultCommision;

    //~ Constructors .............................................................................................................

    SalableClassification(@NotNull String label, @NotNull BigDecimal defaultCommision) {
        this.label = label;
        this.defaultCommision = defaultCommision;
    }

    //~ Getters ..................................................................................................................

    /** Returns the Salable Classification Map. */
    @NotNull public static Map<String,SalableClassification> map() { return SALABLE_CLASSIFICATION_MAP; }

    /** Returns the Default Commision. */
    @NotNull public BigDecimal getDefaultCommision() { return defaultCommision; }

    //~ Methods ..................................................................................................................

    /** Returns the field label in the current locale */
    @NotNull public final String label() { return BUNDLE.getString(name(), label); }

    public int index() { return ordinal(); }

    /** Check that the enum is one of the specified values */
    public final boolean in(@NotNull SalableClassification first, @NotNull SalableClassification... rest) { return EnumSet.of(first, rest).contains(this); }

    /** Returns the field image path  */
    @NotNull public final String imagePath() { return BUNDLE.getString(name() + ".image", ""); }

    /** Returns the enum primary key */
    @NotNull public final String key() { return name(); }

    /** Get a SalableClassification from the name */
    @Nullable public static final Option<SalableClassification> fromName(@NotNull String name) {
        return Option.ofNullable(SALABLE_CLASSIFICATION_MAP.get(name));
    }

    //~ Fields ...................................................................................................................

    @NotNull private static final Map<String,SalableClassification> SALABLE_CLASSIFICATION_MAP = Enumerations.buildMap(values());
    @NotNull private static final I18nBundle BUNDLE = I18nBundle.getBundle(SalableClassification.class);

}
