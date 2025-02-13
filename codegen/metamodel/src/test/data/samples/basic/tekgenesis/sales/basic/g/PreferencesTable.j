package tekgenesis.sales.basic.g;

import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.TableField.Decimal;
import tekgenesis.sales.basic.DocType;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Enum;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.sales.basic.MailDigest;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.sales.basic.Preferences;
import tekgenesis.sales.basic.Sex;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.Preferences */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class PreferencesTable
    extends DbTable<Preferences,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Enum<DocType,Integer> CUSTOMER_DOCUMENT_TYPE;
    @NotNull public final Decimal CUSTOMER_DOCUMENT_ID;
    @NotNull public final Enum<Sex,String> CUSTOMER_SEX;
    @NotNull public final Str MAIL;
    @NotNull public final Str TWITTER;
    @NotNull public final Enum<MailDigest,String> DIGEST;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private PreferencesTable() {
        super(Preferences.class,"BASIC","PREFERENCES","PREFERENCES_SEQ",Modifier.NONE,CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        CUSTOMER_DOCUMENT_TYPE = enumField("customerDocumentType", "CUSTOMER_DOCUMENT_TYPE", DocType.class);
        CUSTOMER_DOCUMENT_ID = decimalField("customerDocumentId", "CUSTOMER_DOCUMENT_ID", false, 10, 0);
        CUSTOMER_SEX = enumField("customerSex", "CUSTOMER_SEX", Sex.class);
        MAIL = strField("mail", "MAIL", 60);
        TWITTER = strField("twitter", "TWITTER", 60);
        DIGEST = enumField("digest", "DIGEST", MailDigest.class);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
        secondaryKeys(listOf(listOf(CUSTOMER_DOCUMENT_TYPE, CUSTOMER_DOCUMENT_ID, CUSTOMER_SEX)));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull public final PreferencesTable as(@NotNull String alias) { return createAlias(new PreferencesTable(), alias); }

    @Override @NotNull protected final EntityTable<Preferences,Integer> createEntityTable() { return new EntityTable<>(PREFERENCES); }

    //~ Fields ...................................................................................................................

    @NotNull public static final PreferencesTable PREFERENCES = new PreferencesTable();

}
