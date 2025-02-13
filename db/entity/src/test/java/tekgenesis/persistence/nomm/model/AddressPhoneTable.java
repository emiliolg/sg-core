
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.nomm.model;

import org.jetbrains.annotations.NotNull;

import tekgenesis.cache.CacheType;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.core.Tuple4;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.InnerEntityTable;
import tekgenesis.persistence.TableField;
import tekgenesis.type.Modifier;

import static tekgenesis.common.collections.Colls.listOf;

/**
 * TEST CLASS.
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "MagicNumber" })
public class AddressPhoneTable extends DbTable<AddressPhone, Tuple4<Integer, String, Integer, Integer>> {

    //~ Instance Fields ..............................................................................................................................

    public final TableField.Int ADDRESS_SEQ_ID;
    public final TableField.Str DOC_CODE;
    public final TableField.Int DOC_TYPE;

    public final TableField.Int PHONE;

    public final TableField.Int   SEQ_ID;
    public final TableField.DTime UPDATE_TIME;

    //~ Constructors .................................................................................................................................

    AddressPhoneTable() {
        super(AddressPhone.class, "MODEL", "ADDRESS_PHONE", "", Modifier.NONE, CacheType.NONE);
        DOC_TYPE       = intField("docType", "DOC_TYPE");
        DOC_CODE       = strField("docCode", "DOC_CODE", 30);
        ADDRESS_SEQ_ID = intField("addressSeqId", "ADDRESS_SEQ_ID");
        SEQ_ID         = intField("seqId", "SEQ_ID");
        PHONE          = intField("phone", "PHONE");
        UPDATE_TIME    = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(DOC_TYPE, DOC_CODE, ADDRESS_SEQ_ID, SEQ_ID));
    }

    //~ Methods ......................................................................................................................................

    @Override public AddressPhoneTable as(final String alias) {
        return createAlias(new AddressPhoneTable(), alias);
    }

    @Override protected EntityTable<AddressPhone, Tuple4<Integer, String, Integer, Integer>> createEntityTable() {
        return new InnerEntityTable<>(ADDRESS_PHONE);
    }

    @Override protected Tuple4<Integer, String, Integer, Integer> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 4);
        return Tuple.tuple(Integer.valueOf(parts[0]), parts[1], Integer.valueOf(parts[2]), Integer.valueOf(parts[3]));
    }

    //~ Static Fields ................................................................................................................................

    public static final AddressPhoneTable ADDRESS_PHONE = new AddressPhoneTable();
}  // end class AddressPhoneTable
