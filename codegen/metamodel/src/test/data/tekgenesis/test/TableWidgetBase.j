package tekgenesis.test;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.test.TableWidget.VariantsRow;
import tekgenesis.form.configuration.WidgetConfiguration;
import tekgenesis.form.WidgetInstance;

/** 
 * Generated base class for widget: TableWidget.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class TableWidgetBase
    extends WidgetInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Returns the value of the text_field(name). */
    @NotNull public String getName() { return f.get(Field.NAME, String.class); }

    /** Sets the value of the text_field(name). */
    @NotNull public TableWidget setName(@NotNull String name) {
        f.set(Field.NAME, name);
        return (TableWidget) this;
    }

    /** Returns a {@link FormTable<VariantsRow>} instance to handle Variants manipulation */
    @NotNull public final FormTable<VariantsRow> getVariants() { return table(Field.VARIANTS, VariantsRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

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

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static final Logger logger = Logger.getLogger(TableWidget.class);

    //~ Inner Classes ............................................................................................................

    public abstract class VariantsRowBase
        implements FormRowInstance<VariantsRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(variant). */
        @NotNull public String getVariant() { return f.get(Field.VARIANT, String.class); }

        /** Sets the value of the text_field(variant). */
        @NotNull public VariantsRow setVariant(@NotNull String variant) {
            f.set(Field.VARIANT, variant);
            return (VariantsRow) this;
        }

        /** Returns the value of the text_field(price). */
        @NotNull public String getPrice() { return f.get(Field.PRICE, String.class); }

        /** Sets the value of the text_field(price). */
        @NotNull public VariantsRow setPrice(@NotNull String price) {
            f.set(Field.PRICE, price);
            return (VariantsRow) this;
        }

        /** Returns the value of the check_box(primary). */
        public boolean isPrimary() { return f.get(Field.PRIMARY, Boolean.class); }

        /** Sets the value of the check_box(primary). */
        @NotNull public VariantsRow setPrimary(boolean primary) {
            f.set(Field.PRIMARY, primary);
            return (VariantsRow) this;
        }

        /** Associates a new message to the specified field and returns it for any further configuration. */
        @NotNull public Message message(@NotNull Field field, @NotNull String msg) { return f.msg(field, msg); }

        /** Returns true if the field value is not null. */
        public boolean isDefined(@NotNull Field... fields) { return f.defined(fields); }

        /** Resets the given fields. */
        protected void reset(@NotNull Field... fields) { f.reset(fields); }

        /** Focuses given field. */
        protected void focus(@NotNull Field field) { f.focus(field); }

        /** Return associated table. */
        @NotNull public final FormTable<VariantsRow> table() { return getVariants(); }

        /** Remove row from table. */
        @NotNull public Action remove() {
            table().remove((VariantsRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        NAME("name"),
        VARIANTS("variants"),
        VARIANT("variant"),
        PRICE("price"),
        PRIMARY("primary");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
