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
 * Generated base class for enum: DocType.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public enum DocType
    implements Enumeration<DocType,Integer>
{
    /** DNI */
	DNI("DNI", true, 0, Sex.F),
    /** Passport */
	PASS("Passport", false, 1, Sex.M);

    //~ Fields ...................................................................................................................

    @NotNull private final String label;
    private final boolean local;
    private final int id;
    @NotNull private final Sex sex;

    //~ Constructors .............................................................................................................

    DocType(@NotNull String label, boolean local, int id, @NotNull Sex sex) {
        this.label = label;
        this.local = local;
        this.id = id;
        this.sex = sex;
    }

    //~ Getters ..................................................................................................................

    /** Returns the Doc Type Map. */
    @NotNull public static Map<Integer,DocType> map() { return DOC_TYPE_MAP; }

    /** Returns true if it is Local. */
    public boolean isLocal() { return local; }

    /** Returns the Id. */
    public int getId() { return id; }

    /** Returns the Sex. */
    @NotNull public Sex getSex() { return sex; }

    //~ Methods ..................................................................................................................

    /** Returns the field label in the current locale */
    @NotNull public final String label() { return BUNDLE.getString(name(), label); }

    public int index() { return key(); }

    /** Check that the enum is one of the specified values */
    public final boolean in(@NotNull DocType first, @NotNull DocType... rest) { return EnumSet.of(first, rest).contains(this); }

    /** Returns the field image path  */
    @NotNull public final String imagePath() { return BUNDLE.getString(name() + ".image", ""); }

    /** Returns the enum primary key */
    @NotNull public final Integer key() { return id; }

    /** Get a DocType from the id */
    @Nullable public static final Option<DocType> fromId(int id) { return Option.ofNullable(DOC_TYPE_MAP.get(id)); }

    //~ Fields ...................................................................................................................

    @NotNull private static final Map<Integer,DocType> DOC_TYPE_MAP = Enumerations.buildMap(values());
    @NotNull private static final I18nBundle BUNDLE = I18nBundle.getBundle(DocType.class);

}
