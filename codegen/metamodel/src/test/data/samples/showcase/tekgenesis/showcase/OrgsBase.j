package tekgenesis.showcase;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.model.KeyMap;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: Orgs.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class OrgsBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.showcase.Orgs");
    }

    /** Associates a new message to the specified field and returns it for any further configuration. */
    @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

    /** Returns true if the field value is not null. */
    public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

    /** Resets the given fields. */
    protected void reset(@NotNull Field... fields) { f.reset(fields); }

    /** Focuses given field. */
    protected void focus(@NotNull Field field) { f.focus(field); }

    /** Returns the label of the given field. */
    @NotNull protected String label(@NotNull Field field) { return f.label(field); }

    /** Returns a typed configuration for a given field. */
    @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    /** Returns the value of the combo_box(allOrg). */
    @NotNull public Org getAllOrg() { return Org.valueOf(f.get(Field.ALL_ORG, String.class)); }

    /** Sets the value of the combo_box(allOrg). */
    @NotNull public Orgs setAllOrg(@NotNull Org allOrg) {
        f.set(Field.ALL_ORG, allOrg);
        return (Orgs) this;
    }

    /** Sets the options of the combo_box(allOrg). */
    public void setAllOrgOptions(@NotNull Iterable<Org> items) { f.opts(Field.ALL_ORG, items); }

    /** Sets the options of the combo_box(allOrg) with the given KeyMap. */
    public void setAllOrgOptions(@NotNull KeyMap items) { f.opts(Field.ALL_ORG, items); }

    /** Returns the value of the combo_box(filteredOrgsByType). */
    @NotNull public Org getFilteredOrgsByType() {
        return Org.valueOf(f.get(Field.FILTERED_ORGS_BY_TYPE, String.class));
    }

    /** Sets the value of the combo_box(filteredOrgsByType). */
    @NotNull public Orgs setFilteredOrgsByType(@NotNull Org filteredOrgsByType) {
        f.set(Field.FILTERED_ORGS_BY_TYPE, filteredOrgsByType);
        return (Orgs) this;
    }

    /** Sets the options of the combo_box(filteredOrgsByType). */
    public void setFilteredOrgsByTypeOptions(@NotNull Iterable<Org> items) { f.opts(Field.FILTERED_ORGS_BY_TYPE, items); }

    /** Sets the options of the combo_box(filteredOrgsByType) with the given KeyMap. */
    public void setFilteredOrgsByTypeOptions(@NotNull KeyMap items) { f.opts(Field.FILTERED_ORGS_BY_TYPE, items); }

    /** Returns the value of the combo_box(filteredOrgsByShow). */
    @NotNull public Org getFilteredOrgsByShow() {
        return Org.valueOf(f.get(Field.FILTERED_ORGS_BY_SHOW, String.class));
    }

    /** Sets the value of the combo_box(filteredOrgsByShow). */
    @NotNull public Orgs setFilteredOrgsByShow(@NotNull Org filteredOrgsByShow) {
        f.set(Field.FILTERED_ORGS_BY_SHOW, filteredOrgsByShow);
        return (Orgs) this;
    }

    /** Sets the options of the combo_box(filteredOrgsByShow). */
    public void setFilteredOrgsByShowOptions(@NotNull Iterable<Org> items) { f.opts(Field.FILTERED_ORGS_BY_SHOW, items); }

    /** Sets the options of the combo_box(filteredOrgsByShow) with the given KeyMap. */
    public void setFilteredOrgsByShowOptions(@NotNull KeyMap items) { f.opts(Field.FILTERED_ORGS_BY_SHOW, items); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<Orgs> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<Orgs> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<Orgs> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(Orgs.class);

    //~ Inner Classes ............................................................................................................

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        $H0("$H0"),
        $M1("$M1"),
        ALL_ORG("allOrg"),
        FILTERED_ORGS_BY_TYPE("filteredOrgsByType"),
        FILTERED_ORGS_BY_SHOW("filteredOrgsByShow");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
