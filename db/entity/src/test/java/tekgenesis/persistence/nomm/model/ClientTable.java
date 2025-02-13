
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
 * Test class.
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "MagicNumber" })
public class ClientTable extends DbTable<Client, Integer> {

    //~ Instance Fields ..............................................................................................................................

    public final TableField.Int   ID;
    public final TableField.Str   MAIL;
    public final TableField.Str   NAME;
    public final TableField.DTime UPDATE_TIME;

    //~ Constructors .................................................................................................................................

    private ClientTable() {
        super(Client.class, "MODEL", "ADDRESS", "", Modifier.NONE, CacheType.NONE);
        ID          = intField("id", "ID");
        NAME        = strField("name", "NAME", 30);
        MAIL        = strField("mail", "MAIL", 30);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ......................................................................................................................................

    @Override public ClientTable as(final String alias) {
        return createAlias(new ClientTable(), alias);
    }

    @Override protected EntityTable<Client, Integer> createEntityTable() {
        return new EntityTable<>(CLIENT);
    }

    @Override protected Integer strToKey(@NotNull String key) {
        return Integer.valueOf(key);
    }

    //~ Static Fields ................................................................................................................................

    public static final ClientTable CLIENT = new ClientTable();
}
