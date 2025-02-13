package tekgenesis.sales.basic.service;

import tekgenesis.common.core.EnumException;
import java.util.EnumSet;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.common.env.i18n.I18nBundle;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.common.core.Option;

/** 
 * Generated base class for enum: HandlerError.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public enum HandlerError
    implements Enumeration<HandlerError,String>, EnumException<HandlerErrorApplicationException>
{
    /** Error message */
	ERROR_MESSAGE("Error message"),
    /** %s */
	QUOTE_MESSAGE("%s");

    //~ Fields ...................................................................................................................

    @NotNull private final String label;

    //~ Constructors .............................................................................................................

    HandlerError(@NotNull String label) { this.label = label; }

    //~ Getters ..................................................................................................................

    /** Returns the Handler Error Map. */
    @NotNull public static Map<String,HandlerError> map() { return HANDLER_ERROR_MAP; }

    //~ Methods ..................................................................................................................

    /** Returns the field label in the current locale */
    @NotNull public final String label() { return BUNDLE.getString(name(), label); }

    public int index() { return ordinal(); }

    /** Check that the enum is one of the specified values */
    public final boolean in(@NotNull HandlerError first, @NotNull HandlerError... rest) { return EnumSet.of(first, rest).contains(this); }

    /** Returns the field image path  */
    @NotNull public final String imagePath() { return BUNDLE.getString(name() + ".image", ""); }

    /** Returns the enum primary key */
    @NotNull public final String key() { return name(); }

    /** Get a HandlerError from the name */
    @Nullable public static final Option<HandlerError> fromName(@NotNull String name) { return Option.ofNullable(HANDLER_ERROR_MAP.get(name)); }

    @Override @NotNull public final HandlerErrorApplicationException exception() { return new HandlerErrorApplicationException(this); }

    @Override @NotNull public final HandlerErrorApplicationException exception(@NotNull Object... args) { return new HandlerErrorApplicationException(this, args); }

    //~ Fields ...................................................................................................................

    @NotNull private static final Map<String,HandlerError> HANDLER_ERROR_MAP = Enumerations.buildMap(values());
    @NotNull private static final I18nBundle BUNDLE = I18nBundle.getBundle(HandlerError.class);

}
