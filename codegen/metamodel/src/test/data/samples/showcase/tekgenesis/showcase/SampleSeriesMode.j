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
 * Generated base class for enum: SampleSeriesMode.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public enum SampleSeriesMode
    implements Enumeration<SampleSeriesMode,String>
{
    /** Stacked */
	STACKED("Stacked"),
    /** Overlapped */
	OVERLAPPED("Overlapped"),
    /** Side By Side */
	SIDE_BY_SIDE("Side By Side");

    //~ Fields ...................................................................................................................

    @NotNull private final String label;

    //~ Constructors .............................................................................................................

    SampleSeriesMode(@NotNull String label) { this.label = label; }

    //~ Getters ..................................................................................................................

    /** Returns the Sample Series Mode Map. */
    @NotNull public static Map<String,SampleSeriesMode> map() { return SAMPLE_SERIES_MODE_MAP; }

    //~ Methods ..................................................................................................................

    /** Returns the field label in the current locale */
    @NotNull public final String label() { return BUNDLE.getString(name(), label); }

    public int index() { return ordinal(); }

    /** Check that the enum is one of the specified values */
    public final boolean in(@NotNull SampleSeriesMode first, @NotNull SampleSeriesMode... rest) { return EnumSet.of(first, rest).contains(this); }

    /** Returns the field image path  */
    @NotNull public final String imagePath() { return BUNDLE.getString(name() + ".image", ""); }

    /** Returns the enum primary key */
    @NotNull public final String key() { return name(); }

    /** Get a SampleSeriesMode from the name */
    @Nullable public static final Option<SampleSeriesMode> fromName(@NotNull String name) { return Option.ofNullable(SAMPLE_SERIES_MODE_MAP.get(name)); }

    //~ Fields ...................................................................................................................

    @NotNull private static final Map<String,SampleSeriesMode> SAMPLE_SERIES_MODE_MAP = Enumerations.buildMap(values());
    @NotNull private static final I18nBundle BUNDLE = I18nBundle.getBundle(SampleSeriesMode.class);

}
