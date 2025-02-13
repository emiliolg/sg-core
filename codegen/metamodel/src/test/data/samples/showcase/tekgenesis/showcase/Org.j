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
 * Generated base class for enum: Org.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public enum Org
    implements Enumeration<Org,String>
{
    /** Garbarino */
	GARBARINO("Garbarino", 0, OrgType.RETAIL, true),
    /** Compumundo */
	COMPUMUNDO("Compumundo", 1, OrgType.RETAIL, true),
    /** Logistics */
	LOGISTICS("Logistics", 2, OrgType.LOGISTIC, false);

    //~ Fields ...................................................................................................................

    @NotNull private final String label;
    private final int id;
    @NotNull private final OrgType type;
    private final boolean show;

    //~ Constructors .............................................................................................................

    Org(@NotNull String label, int id, @NotNull OrgType type, boolean show) {
        this.label = label;
        this.id = id;
        this.type = type;
        this.show = show;
    }

    //~ Getters ..................................................................................................................

    /** Returns the Org Map. */
    @NotNull public static Map<String,Org> map() { return ORG_MAP; }

    /** Returns the Id. */
    public int getId() { return id; }

    /** Returns the Type. */
    @NotNull public OrgType getType() { return type; }

    /** Returns true if it is Show. */
    public boolean isShow() { return show; }

    //~ Methods ..................................................................................................................

    /** Returns the field label in the current locale */
    @NotNull public final String label() { return BUNDLE.getString(name(), label); }

    public int index() { return ordinal(); }

    /** Check that the enum is one of the specified values */
    public final boolean in(@NotNull Org first, @NotNull Org... rest) { return EnumSet.of(first, rest).contains(this); }

    /** Returns the field image path  */
    @NotNull public final String imagePath() { return BUNDLE.getString(name() + ".image", ""); }

    /** Returns the enum primary key */
    @NotNull public final String key() { return name(); }

    /** Get a Org from the name */
    @Nullable public static final Option<Org> fromName(@NotNull String name) { return Option.ofNullable(ORG_MAP.get(name)); }

    //~ Fields ...................................................................................................................

    @NotNull private static final Map<String,Org> ORG_MAP = Enumerations.buildMap(values());
    @NotNull private static final I18nBundle BUNDLE = I18nBundle.getBundle(Org.class);

}
