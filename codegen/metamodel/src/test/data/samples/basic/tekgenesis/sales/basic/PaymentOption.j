package tekgenesis.sales.basic;

import java.util.EnumSet;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.common.env.i18n.I18nBundle;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.core.Option;

/** 
 * Generated base class for enum: PaymentOption.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public enum PaymentOption
    implements Enumeration<PaymentOption,String>
{
    /** Cash */
	CASH("Cash", "CA"),
    /** Debit */
	DEBIT("Debit", "DE"),
    /** Credit */
	CREDIT("Credit", "CR"),
    /** Check */
	CHECK("Check", "CH");

    //~ Fields ...................................................................................................................

    @NotNull private final String label;
    @NotNull private final String symbol;

    //~ Constructors .............................................................................................................

    PaymentOption(@NotNull String label, @NotNull String symbol) {
        this.label = label;
        this.symbol = symbol;
    }

    //~ Getters ..................................................................................................................

    /** Returns the Payment Option Map. */
    @NotNull public static Map<String,PaymentOption> map() { return PAYMENT_OPTION_MAP; }

    /** Returns the Symbol. */
    @NotNull public String getSymbol() { return symbol; }

    //~ Methods ..................................................................................................................

    /** Returns the field label in the current locale */
    @NotNull public final String label() { return BUNDLE.getString(name(), label); }

    public int index() { return ordinal(); }

    /** Check that the enum is one of the specified values */
    public final boolean in(@NotNull PaymentOption first, @NotNull PaymentOption... rest) { return EnumSet.of(first, rest).contains(this); }

    /** Returns the field image path  */
    @NotNull public final String imagePath() { return BUNDLE.getString(name() + ".image", ""); }

    /** Returns the enum primary key */
    @NotNull public final String key() { return symbol; }

    /** Get a PaymentOption from the symbol */
    @Nullable public static final Option<PaymentOption> fromSymbol(@NotNull String symbol) { return Option.ofNullable(PAYMENT_OPTION_MAP.get(symbol)); }

    //~ Fields ...................................................................................................................

    @NotNull private static final Map<String,PaymentOption> PAYMENT_OPTION_MAP = Enumerations.buildMap(values());
    @NotNull private static final I18nBundle BUNDLE = I18nBundle.getBundle(PaymentOption.class);

}
