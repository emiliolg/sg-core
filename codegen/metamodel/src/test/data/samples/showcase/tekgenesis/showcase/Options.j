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
 * Generated base class for enum: Options.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public enum Options
    implements Enumeration<Options,String>
{
    /** Option1 */
	OPTION1("Option1"),
    /** Option2 */
	OPTION2("Option2"),
    /** Option3 */
	OPTION3("Option3"),
    /** Option4 */
	OPTION4("Option4"),
    /** Option5 */
	OPTION5("Option5"),
    /** Option6 */
	OPTION6("Option6");

    //~ Fields ...................................................................................................................

    @NotNull private final String label;

    //~ Constructors .............................................................................................................

    Options(@NotNull String label) { this.label = label; }

    //~ Getters ..................................................................................................................

    /** Returns the Options Map. */
    @NotNull public static Map<String,Options> map() { return OPTIONS_MAP; }

    //~ Methods ..................................................................................................................

    /** Returns the field label in the current locale */
    @NotNull public final String label() { return BUNDLE.getString(name(), label); }

    public int index() { return ordinal(); }

    /** Check that the enum is one of the specified values */
    public final boolean in(@NotNull Options first, @NotNull Options... rest) { return EnumSet.of(first, rest).contains(this); }

    /** Returns the field image path  */
    @NotNull public final String imagePath() { return BUNDLE.getString(name() + ".image", ""); }

    /** Returns the enum primary key */
    @NotNull public final String key() { return name(); }

    /** Get a Options from the name */
    @Nullable public static final Option<Options> fromName(@NotNull String name) { return Option.ofNullable(OPTIONS_MAP.get(name)); }

    //~ Fields ...................................................................................................................

    @NotNull private static final Map<String,Options> OPTIONS_MAP = Enumerations.buildMap(values());
    @NotNull private static final I18nBundle BUNDLE = I18nBundle.getBundle(Options.class);

}
