package tekgenesis.sales.basic.g;

import tekgenesis.persistence.TableField.Bool;
import tekgenesis.cache.CacheType;
import tekgenesis.common.util.Conversions;
import tekgenesis.persistence.TableField.DTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.type.Modifier;
import org.jetbrains.annotations.NotNull;
import tekgenesis.sales.basic.ProductDataWorkItem;
import tekgenesis.persistence.TableField.Str;
import static tekgenesis.common.collections.Colls.listOf;

/** Metadata class for table associated to entity tekgenesis.sales.basic.ProductDataWorkItem */
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicNumber"})
public class ProductDataWorkItemTable
    extends DbTable<ProductDataWorkItem,Integer>
{

    //~ Fields ...................................................................................................................

    @NotNull public final Int ID;
    @NotNull public final Str TASK;
    @NotNull public final Int PARENT_CASE_ID;
    @NotNull public final DTime CREATION;
    @NotNull public final Str ASSIGNEE;
    @NotNull public final Str REPORTER;
    @NotNull public final Str OU_NAME;
    @NotNull public final Bool CLOSED;
    @NotNull public final Str DESCRIPTION;
    @NotNull public final Str TITLE;
    @NotNull public final Int PRIORITY_CODE;
    @NotNull public final Str BUSINESS_KEY;
    @NotNull public final DTime UPDATE_TIME;

    //~ Constructors .............................................................................................................

    private ProductDataWorkItemTable() {
        super(ProductDataWorkItem.class,"BASIC","PRODUCT_DATA_WORK_ITEM","PRODUCT_DATA_WORK_ITEM_SEQ",Modifier.NONE,CacheType.NONE);
        ID = intField("id", "ID", false, 9);
        TASK = strField("task", "TASK", 256);
        PARENT_CASE_ID = intField("parentCaseId", "PARENT_CASE_ID", false, 9);
        CREATION = dTimeField("creation", "CREATION");
        ASSIGNEE = strField("assignee", "ASSIGNEE", 256);
        REPORTER = strField("reporter", "REPORTER", 256);
        OU_NAME = strField("ouName", "OU_NAME", 256);
        CLOSED = boolField("closed", "CLOSED");
        DESCRIPTION = strField("description", "DESCRIPTION", 256);
        TITLE = strField("title", "TITLE", 256);
        PRIORITY_CODE = intField("priorityCode", "PRIORITY_CODE", false, 9);
        BUSINESS_KEY = strField("businessKey", "BUSINESS_KEY", 256);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ..................................................................................................................

    @NotNull protected Integer strToKey(@NotNull String key) { return Conversions.toInt(key); }

    @Override @NotNull public final ProductDataWorkItemTable as(@NotNull String alias) { return createAlias(new ProductDataWorkItemTable(), alias); }

    @Override @NotNull protected final EntityTable<ProductDataWorkItem,Integer> createEntityTable() { return new EntityTable<>(PRODUCT_DATA_WORK_ITEM); }

    //~ Fields ...................................................................................................................

    @NotNull public static final ProductDataWorkItemTable PRODUCT_DATA_WORK_ITEM = new ProductDataWorkItemTable();

}
