package tekgenesis.sales.basic;

import tekgenesis.form.Action;
import tekgenesis.form.Actions;
import tekgenesis.sales.basic.CategoriesService.CategoriesRow;
import tekgenesis.form.exception.FormCannotBePopulatedException;
import tekgenesis.metadata.form.widget.FormFieldRef;
import tekgenesis.form.FormInstance;
import tekgenesis.form.extension.FormListener;
import tekgenesis.form.extension.FormListenerMap;
import tekgenesis.form.extension.FormListenerType;
import tekgenesis.form.FormParameters;
import tekgenesis.form.FormRowInstance;
import tekgenesis.form.FormTable;
import tekgenesis.common.logging.LoggableInstance;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.transaction.Transaction;
import tekgenesis.form.UiModelAccessor;
import tekgenesis.form.configuration.WidgetConfiguration;

/** 
 * Generated base class for form: CategoriesService.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue", "LocalVariableHidesMemberVariable", "UnusedParameters", "EmptyMethod"})
public abstract class CategoriesServiceBase
    extends FormInstance<Void>
    implements LoggableInstance
{

    //~ Fields ...................................................................................................................

    @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

    //~ Methods ..................................................................................................................

    /** Invoked when populating a form instance */
    @NotNull public final Void populate() {
        throw new FormCannotBePopulatedException("tekgenesis.sales.basic.CategoriesService");
    }

    /** Invoked when the form is loaded */
    public abstract void load();

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

    /** Returns the value of the text_field(from). */
    @Nullable public Long getFrom() { return f.get(Field.FROM, Long.class); }

    /** Sets the value of the text_field(from). */
    @NotNull public CategoriesService setFrom(@Nullable Long from) {
        f.set(Field.FROM, from);
        return (CategoriesService) this;
    }

    /** Returns the value of the text_field(limit). */
    public int getLimit() { return f.get(Field.LIMIT, Integer.class); }

    /** Sets the value of the text_field(limit). */
    @NotNull public CategoriesService setLimit(int limit) {
        f.set(Field.LIMIT, limit);
        return (CategoriesService) this;
    }

    /** Returns a {@link FormTable<CategoriesRow>} instance to handle Categories manipulation */
    @NotNull public final FormTable<CategoriesRow> getCategories() { return table(Field.CATEGORIES, CategoriesRow.class); }

    /** Method to allow table row class override */
    @NotNull final <T> FormTable<T> table(@NotNull FormFieldRef field, @NotNull Class<T> rowClass) { return f.table(field, rowClass, this); }

    /** Returns the value of the text_field(next). */
    @Nullable public Long getNext() { return f.get(Field.NEXT, Long.class); }

    /** Sets the value of the text_field(next). */
    @NotNull public CategoriesService setNext(@Nullable Long next) {
        f.set(Field.NEXT, next);
        return (CategoriesService) this;
    }

    @NotNull public static CategoriesServiceParameters parameters() { return new CategoriesServiceParameters(); }

    /** Register a form Listener */
    public static void addListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CategoriesService> listener) { listeners.addListener(listenerType, listener); }

    /** Remove a form Listener */
    public static void removeListener(@NotNull FormListenerType listenerType, @NotNull FormListener<CategoriesService> listener) { listeners.removeListener(listenerType, listener); }

    @Override @NotNull public Logger logger() { return logger; }

    /** Returns utility methods to deal with {@link Action actions}. */
    @NotNull protected final Actions actions() { return actions; }

    //~ Fields ...................................................................................................................

    @NotNull private static FormListenerMap<CategoriesService> listeners = new FormListenerMap<>();
    @NotNull private static final Logger logger = Logger.getLogger(CategoriesService.class);

    //~ Inner Classes ............................................................................................................

    public abstract class CategoriesRowBase
        implements FormRowInstance<CategoriesRow>
    {

        //~ Fields ...................................................................................................................

        @SuppressWarnings({"InstanceVariableMayNotBeInitialized", "UnusedDeclaration", "NullableProblems"}) @NotNull private UiModelAccessor f;

        //~ Methods ..................................................................................................................

        /** Returns the value of the text_field(id). */
        public long getId() { return f.get(Field.ID, Long.class); }

        /** Sets the value of the text_field(id). */
        @NotNull public CategoriesRow setId(long id) {
            f.set(Field.ID, id);
            return (CategoriesRow) this;
        }

        /** Returns the value of the text_field(name). */
        @NotNull public String getName() { return f.get(Field.NAME, String.class); }

        /** Sets the value of the text_field(name). */
        @NotNull public CategoriesRow setName(@NotNull String name) {
            f.set(Field.NAME, name);
            return (CategoriesRow) this;
        }

        /** Returns the value of the text_field(descr). */
        @NotNull public String getDescr() { return f.get(Field.DESCR, String.class); }

        /** Sets the value of the text_field(descr). */
        @NotNull public CategoriesRow setDescr(@NotNull String descr) {
            f.set(Field.DESCR, descr);
            return (CategoriesRow) this;
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
        @NotNull public final FormTable<CategoriesRow> table() { return getCategories(); }

        /** Remove row from table and delete associated Category instance. */
        @NotNull public Action remove() {
            Transaction.runInTransaction( () -> {
            	final Category instance = isDefined(Field.ID) ? Category.find(keyAsString()) : null;
            	if (instance != null) instance.delete();
            });
            table().remove((CategoriesRow) this);
            return actions().getDefault();
        }

        /** Returns a typed configuration for a given field. */
        @NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Field field) { return f.config(field); }

        /** Populate field values with given category instance. */
        public void populate(@NotNull Category category) {
            setId(category.getIdKey())
            	.setName(category.getName())
            	.setDescr(category.getDescr());
        }

        /** Copies field values to given category instance. */
        public void copyTo(@NotNull Category category) {
            category.setName(getName())
            	.setDescr(getDescr());
        }

        /** Return primary key of bound {@link Category} */
        @NotNull public String keyAsString() { return "" + getId(); }

    }

    public static final class CategoriesServiceParameters
        extends FormParameters<CategoriesService>
    {

        //~ Methods ..................................................................................................................

        @NotNull public CategoriesServiceParameters withFrom(@Nullable Long from) { return put(Field.FROM, from); }

        @NotNull public CategoriesServiceParameters withLimit(int limit) { return put(Field.LIMIT, limit); }

    }

    @SuppressWarnings("UnnecessarySemicolon")
    public enum Field
        implements tekgenesis.metadata.form.widget.FormFieldRef
    {
        FROM("from"),
        LIMIT("limit"),
        CATEGORIES("categories"),
        ID("id"),
        NAME("name"),
        DESCR("descr"),
        NEXT("next");

        //~ Fields ...................................................................................................................

        @Nullable private final String id;

        //~ Constructors .............................................................................................................

        Field(@Nullable final String id) { this.id = id; }

        //~ Methods ..................................................................................................................

        @NotNull public String id() { return id; }

    }
}
