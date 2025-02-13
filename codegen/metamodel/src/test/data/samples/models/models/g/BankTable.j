package models.g;

import models.Bank;
import java.math.BigDecimal;
import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.TableField.Decimal;
import models.DocType;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Enum;
import java.util.EnumSet;
import tekgenesis.persistence.InnerEntityTable;
import tekgenesis.persistence.TableField.Int;
import org.jetbrains.annotations.NotNull;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.core.Tuple3;
import static tekgenesis.type.Modifier.AUDITABLE;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity models.Bank */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class BankTable
    extends DbTable<Bank,Tuple3<DocType,BigDecimal,Integer>>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Enum<DocType,String> CUSTOMER_DOCUMENT_TYPE;
    @NotNull public final Decimal CUSTOMER_DOCUMENT_ID;
    @NotNull public final Int SEQ_ID;
    @NotNull public final Str NAME;
    @NotNull public final DTime UPDATE_TIME;
    @NotNull public final DTime CREATION_TIME;
    @NotNull public final Str CREATION_USER;
    @NotNull public final Str UPDATE_USER;

    //~ Constructors .............................................................................................................

    private BankTable() {
        super(Bank.class,"MODELS","BANK","",EnumSet.of(AUDITABLE),CacheType.NONE);
        CUSTOMER_DOCUMENT_TYPE = enumField("customerDocumentType", "CUSTOMER_DOCUMENT_TYPE", DocType.class);
        CUSTOMER_DOCUMENT_ID = decimalField("customerDocumentId", "CUSTOMER_DOCUMENT_ID", false, 10, 0);
        SEQ_ID = intField("seqId", "SEQ_ID", false, 9);
        NAME = strField("name", "NAME", 255);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        CREATION_TIME = dTimeField("creationTime", "CREATION_TIME");
        CREATION_USER = strInternField("creationUser", "CREATION_USER", 100);
        UPDATE_USER = strInternField("updateUser", "UPDATE_USER", 100);
        primaryKey(listOf(CUSTOMER_DOCUMENT_TYPE, CUSTOMER_DOCUMENT_ID, SEQ_ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Tuple3<DocType,BigDecimal,Integer> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 3);
        return Tuple.tuple(DocType.valueOf(parts[0]), Conversions.toDecimal(parts[1]), Conversions.toInt(parts[2]));
    }

    @Override @NotNull public final BankTable as(@NotNull String alias) { return createAlias(new BankTable(), alias); }

    @Override @NotNull protected final EntityTable<Bank,Tuple3<DocType,BigDecimal,Integer>> createEntityTable() { return new InnerEntityTable<>(BANK); }

    //~ Fields ...................................................................................................................

    @NotNull public static final BankTable BANK = new BankTable();

}
