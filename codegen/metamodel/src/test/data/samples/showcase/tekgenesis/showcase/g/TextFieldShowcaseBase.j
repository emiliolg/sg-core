package tekgenesis.showcase.g;

import java.math.BigDecimal;
import java.util.function.Consumer;
import tekgenesis.persistence.Criteria;
import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.common.core.Decimals;
import tekgenesis.persistence.EntityInstanceImpl;
import tekgenesis.persistence.EntityListener;
import tekgenesis.persistence.EntityListenerType;
import tekgenesis.persistence.EntityTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Integers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.database.RowMapper;
import tekgenesis.persistence.Select;
import tekgenesis.common.collections.Seq;
import java.util.Set;
import tekgenesis.common.core.Strings;
import tekgenesis.showcase.TextFieldShowcase;
import static tekgenesis.showcase.g.TextFieldShowcaseTable.TEXT_FIELD_SHOWCASE;
import static tekgenesis.common.util.Conversions.formatList;
import static tekgenesis.persistence.Sql.selectFrom;

/** 
 * Generated base class for entity: TextFieldShowcase.
 * Don't modify this as this is an auto generated class that's gets generated
 * every time the meta model file is modified. Use subclass instead.
 */
@SuppressWarnings({"DuplicateStringLiteralInspection", "WeakerAccess", "FieldMayBeFinal", "FieldCanBeLocal", "ConstantConditions", "ClassWithTooManyMethods", "ClassWithTooManyFields", "MagicNumber", "MethodOverridesStaticMethodOfSuperclass", "ParameterHidesMemberVariable", "FieldNameHidesFieldInSuperclass", "OverlyComplexClass", "RedundantCast", "UnusedReturnValue"})
public abstract class TextFieldShowcaseBase
    extends EntityInstanceImpl<TextFieldShowcase,Integer>
    implements PersistableInstance<TextFieldShowcase,Integer>
{

    //~ Fields ...................................................................................................................

    int idKey = 0;
    @Nullable String f1 = null;
    @Nullable String f2 = null;
    @Nullable String f3 = null;
    @Nullable String f4 = null;
    @Nullable String patente = null;
    @Nullable BigDecimal money = null;
    @Nullable BigDecimal t1 = null;
    @Nullable BigDecimal t2 = null;
    @Nullable BigDecimal t3 = null;
    @Nullable BigDecimal t4 = null;
    @Nullable Integer a1 = null;
    @Nullable Integer a2 = null;
    @Nullable Integer a3 = null;
    @Nullable Integer a4 = null;
    @Nullable String html = null;
    @NotNull DateTime updateTime = DateTime.EPOCH;

    //~ Methods ..................................................................................................................

    /** Returns the Id Key. */
    public int getIdKey() { return this.idKey; }

    /** Returns the F1. */
    @Nullable public String getF1() { return this.f1; }

    /** Sets the value of the F1. */
    @NotNull public TextFieldShowcase setF1(@Nullable String f1) {
        markAsModified();
        this.f1 = Strings.truncate(f1, 50);
        return (TextFieldShowcase) this;
    }

    /** Returns the F2. */
    @Nullable public String getF2() { return this.f2; }

    /** Sets the value of the F2. */
    @NotNull public TextFieldShowcase setF2(@Nullable String f2) {
        markAsModified();
        this.f2 = Strings.truncate(f2, 50);
        return (TextFieldShowcase) this;
    }

    /** Returns the F3. */
    @Nullable public String getF3() { return this.f3; }

    /** Sets the value of the F3. */
    @NotNull public TextFieldShowcase setF3(@Nullable String f3) {
        markAsModified();
        this.f3 = Strings.truncate(f3, 50);
        return (TextFieldShowcase) this;
    }

    /** Returns the F4. */
    @Nullable public String getF4() { return this.f4; }

    /** Sets the value of the F4. */
    @NotNull public TextFieldShowcase setF4(@Nullable String f4) {
        markAsModified();
        this.f4 = Strings.truncate(f4, 50);
        return (TextFieldShowcase) this;
    }

    /** Returns the Patente. */
    @Nullable public String getPatente() { return this.patente; }

    /** Sets the value of the Patente. */
    @NotNull public TextFieldShowcase setPatente(@Nullable String patente) {
        markAsModified();
        this.patente = Strings.truncate(patente, 6);
        return (TextFieldShowcase) this;
    }

    /** Returns the Money. */
    @Nullable public BigDecimal getMoney() { return this.money; }

    /** Sets the value of the Money. */
    @NotNull public TextFieldShowcase setMoney(@Nullable BigDecimal money) {
        markAsModified();
        this.money = Decimals.scaleAndCheck("money", money, false, 10, 2);
        return (TextFieldShowcase) this;
    }

    /** Returns the T1. */
    @Nullable public BigDecimal getT1() { return this.t1; }

    /** Sets the value of the T1. */
    @NotNull public TextFieldShowcase setT1(@Nullable BigDecimal t1) {
        markAsModified();
        this.t1 = Decimals.scaleAndCheck("t1", t1, false, 4, 2);
        return (TextFieldShowcase) this;
    }

    /** Returns the T2. */
    @Nullable public BigDecimal getT2() { return this.t2; }

    /** Sets the value of the T2. */
    @NotNull public TextFieldShowcase setT2(@Nullable BigDecimal t2) {
        markAsModified();
        this.t2 = Decimals.scaleAndCheck("t2", t2, false, 4, 2);
        return (TextFieldShowcase) this;
    }

    /** Returns the T3. */
    @Nullable public BigDecimal getT3() { return this.t3; }

    /** Sets the value of the T3. */
    @NotNull public TextFieldShowcase setT3(@Nullable BigDecimal t3) {
        markAsModified();
        this.t3 = Decimals.scaleAndCheck("t3", t3, false, 4, 2);
        return (TextFieldShowcase) this;
    }

    /** Returns the T4. */
    @Nullable public BigDecimal getT4() { return this.t4; }

    /** Sets the value of the T4. */
    @NotNull public TextFieldShowcase setT4(@Nullable BigDecimal t4) {
        markAsModified();
        this.t4 = Decimals.scaleAndCheck("t4", t4, false, 4, 2);
        return (TextFieldShowcase) this;
    }

    /** Returns the A1. */
    @Nullable public Integer getA1() { return this.a1; }

    /** Sets the value of the A1. */
    @NotNull public TextFieldShowcase setA1(@Nullable Integer a1) {
        markAsModified();
        this.a1 = Integers.checkSignedLength("a1", a1, false, 9);
        return (TextFieldShowcase) this;
    }

    /** Returns the A2. */
    @Nullable public Integer getA2() { return this.a2; }

    /** Sets the value of the A2. */
    @NotNull public TextFieldShowcase setA2(@Nullable Integer a2) {
        markAsModified();
        this.a2 = Integers.checkSignedLength("a2", a2, false, 9);
        return (TextFieldShowcase) this;
    }

    /** Returns the A3. */
    @Nullable public Integer getA3() { return this.a3; }

    /** Sets the value of the A3. */
    @NotNull public TextFieldShowcase setA3(@Nullable Integer a3) {
        markAsModified();
        this.a3 = Integers.checkSignedLength("a3", a3, false, 9);
        return (TextFieldShowcase) this;
    }

    /** Returns the A4. */
    @Nullable public Integer getA4() { return this.a4; }

    /** Sets the value of the A4. */
    @NotNull public TextFieldShowcase setA4(@Nullable Integer a4) {
        markAsModified();
        this.a4 = Integers.checkSignedLength("a4", a4, false, 9);
        return (TextFieldShowcase) this;
    }

    /** Returns the Html. */
    @Nullable public String getHtml() { return this.html; }

    /** Sets the value of the Html. */
    @NotNull public TextFieldShowcase setHtml(@Nullable String html) {
        markAsModified();
        this.html = Strings.truncate(html, 255);
        return (TextFieldShowcase) this;
    }

    /** Returns the Update Time. */
    @Override @NotNull public DateTime getUpdateTime() { return this.updateTime; }

    /** Creates a new {@link TextFieldShowcase} instance. */
    @NotNull public static TextFieldShowcase create(int idKey) {
        final TextFieldShowcase result = new TextFieldShowcase();
        ((TextFieldShowcaseBase) result).idKey = Integers.checkSignedLength("idKey", idKey, false, 9);
        return result;
    }

    /** 
     * Find (or create if not present) a 'TextFieldShowcase' in the database.
     * Identified by the primary key.
     */
    @NotNull public static TextFieldShowcase findOrCreate(int idKey) { return myEntityTable().findOrCreate(idKey); }

    /** 
     * Find (or create if not present) a 'TextFieldShowcase' in the database.
     * Identified by the primary key.
     */
    @NotNull public static TextFieldShowcase findOrCreate(@NotNull String key) { return myEntityTable().findOrCreateByString(key); }

    @NotNull private static EntityTable<TextFieldShowcase,Integer> myEntityTable() { return EntityTable.forTable(TEXT_FIELD_SHOWCASE); }

    @NotNull public EntityTable<TextFieldShowcase,Integer> et() { return myEntityTable(); }

    @Override @NotNull public DbTable<TextFieldShowcase,Integer> table() { return TEXT_FIELD_SHOWCASE; }

    /** 
     * Try to finds an Object of type 'TextFieldShowcase' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TextFieldShowcase find(int idKey) { return myEntityTable().find(idKey); }

    /** 
     * Try to finds an Object of type 'TextFieldShowcase' in the database.
     * Identified by the primary key.
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static TextFieldShowcase findOrFail(int idKey) { return myEntityTable().findOrFail(idKey); }

    /** 
     * Try to finds an Object of type 'TextFieldShowcase' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TextFieldShowcase findPersisted(int idKey) { return myEntityTable().findPersisted(idKey); }

    /** 
     * Try to finds an Object of type 'TextFieldShowcase' in the database.
     * Identified by the primary key.
     * Ignoring caches and accessing the database
     * Throws EntityNotFoundException if is not present
     */
    @NotNull public static TextFieldShowcase findPersistedOrFail(int idKey) { return myEntityTable().findPersistedOrFail(idKey); }

    /** 
     * Try to finds an Object of type 'TextFieldShowcase' in the database.
     * Identified by the primary key.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TextFieldShowcase find(@NotNull String key) { return myEntityTable().findByString(key); }

    /** 
     * Try to finds an Object of type 'TextFieldShowcase' in the database.
     * That verifies the specified condition.
     * Returns <code>null</code> if is not present
     */
    @Nullable public static TextFieldShowcase findWhere(@NotNull Criteria... condition) {
        return selectFrom(TEXT_FIELD_SHOWCASE).where(condition).get();
    }

    /** Create a selectFrom(TEXT_FIELD_SHOWCASE). */
    @NotNull public static Select<TextFieldShowcase> list() { return selectFrom(TEXT_FIELD_SHOWCASE); }

    /** Performs the given action for each TextFieldShowcase */
    public static void forEach(@Nullable Consumer<TextFieldShowcase> consumer) { selectFrom(TEXT_FIELD_SHOWCASE).forEach(consumer); }

    /** List instances of 'TextFieldShowcase' with the specified keys. */
    @NotNull public static ImmutableList<TextFieldShowcase> list(@Nullable Set<Integer> keys) { return myEntityTable().list(keys); }

    /** List instances of 'TextFieldShowcase' with the specified keys. */
    @NotNull public static ImmutableList<TextFieldShowcase> list(@Nullable Iterable<String> keys) { return myEntityTable().listFromStringKeys(keys); }

    /** List the instances of 'TextFieldShowcase' that verify the specified condition. */
    @NotNull public static Select<TextFieldShowcase> listWhere(@NotNull Criteria condition) { return selectFrom(TEXT_FIELD_SHOWCASE).where(condition); }

    @Override @NotNull public final TextFieldShowcase update() { return PersistableInstance.super.update(); }

    @Override @NotNull public final TextFieldShowcase insert() { return PersistableInstance.super.insert(); }

    /** Register a Listener */
    public static void addListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<TextFieldShowcase> listener) { myEntityTable().addListener(listenerType, listener); }

    /** Remove a Listener */
    public static void removeListener(@NotNull EntityListenerType listenerType, @NotNull EntityListener<TextFieldShowcase> listener) { myEntityTable().removeListener(listenerType, listener); }

    @NotNull public String keyAsString() { return String.valueOf(idKey); }

    @NotNull public Integer keyObject() { return idKey; }

    @Override @NotNull public final Seq<String> describe() { return formatList(getIdKey()); }

    @Override @NotNull public String toString() { return "" + getIdKey(); }

    /** Gets mapper for SQL statements */
    @NotNull public static RowMapper<TextFieldShowcase> rowMapper() { return TEXT_FIELD_SHOWCASE.metadata().getRowMapper(); }

}
