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
 * Generated base class for enum: Sex.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public enum Sex
    implements Enumeration<Sex,String>
{
    /** Female */
	F("Female", 0),
    /** Male */
	M("Male", 1);

    //~ Fields ...................................................................................................................

    @NotNull private final String label;
    private final int number;

    //~ Constructors .............................................................................................................

    Sex(@NotNull String label, int number) {
        this.label = label;
        this.number = number;
    }

    //~ Getters ..................................................................................................................

    /** Returns the Sex Map. */
    @NotNull public static Map<String,Sex> map() { return SEX_MAP; }

    /** Returns the Number. */
    public int getNumber() { return number; }

    //~ Methods ..................................................................................................................

    /** Returns the field label in the current locale */
    @NotNull public final String label() { return BUNDLE.getString(name(), label); }

    public int index() { return getNumber(); }

    /** Check that the enum is one of the specified values */
    public final boolean in(@NotNull Sex first, @NotNull Sex... rest) { return EnumSet.of(first, rest).contains(this); }

    /** Returns the field image path  */
    @NotNull public final String imagePath() { return BUNDLE.getString(name() + ".image", ""); }

    /** Returns the enum primary key */
    @NotNull public final String key() { return name(); }

    /** Get a Sex from the name */
    @Nullable public static final Option<Sex> fromName(@NotNull String name) { return Option.ofNullable(SEX_MAP.get(name)); }

    //~ Fields ...................................................................................................................

    @NotNull private static final Map<String,Sex> SEX_MAP = Enumerations.buildMap(values());
    @NotNull private static final I18nBundle BUNDLE = I18nBundle.getBundle(Sex.class);

}
