
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
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.TableField;
import tekgenesis.type.Modifier;

import static tekgenesis.common.collections.Colls.listOf;

/**
 * TEST CLASS.
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "MagicNumber" })
public class AddressTable extends DbTable<Address, String> {

    //~ Instance Fields ..............................................................................................................................

    public final TableField.Str   CODE;
    public final TableField.Str   ROOM;
    public final TableField.Str   STREET;
    public final TableField.DTime UPDATE_TIME;

    //~ Constructors .................................................................................................................................

    AddressTable() {
        super(Address.class, "MODEL", "ADDRESS", "", Modifier.NONE, CacheType.NONE);
        CODE        = strField("code", "CODE", 30);
        ROOM        = strField("room", "ROOM", 30);
        STREET      = strField("street", "STREET", 30);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(CODE));
    }

    //~ Methods ......................................................................................................................................

    @Override public AddressTable as(final String alias) {
        return createAlias(new AddressTable(), alias);
    }

    @Override protected EntityTable<Address, String> createEntityTable() {
        return new EntityTable<>(ADDRESS);
    }

    @Override protected String strToKey(@NotNull String key) {
        return key;
    }

    //~ Static Fields ................................................................................................................................

    public static final AddressTable ADDRESS = new AddressTable();
}
