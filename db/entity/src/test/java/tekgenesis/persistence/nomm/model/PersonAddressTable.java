
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
import tekgenesis.common.core.Tuple3;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.InnerEntityTable;
import tekgenesis.persistence.TableField;
import tekgenesis.type.Modifier;

import static tekgenesis.common.collections.Colls.listOf;

/**
 * Test class.
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "MagicNumber" })
public class PersonAddressTable extends DbTable<PersonAddress, Tuple3<Integer, String, Integer>> {

    //~ Instance Fields ..............................................................................................................................

    public final TableField.Str   DOC_CODE;
    public final TableField.Int   DOC_TYPE;
    public final TableField.Int   SEQ_ID;
    public final TableField.Str   STREET;
    public final TableField.DTime UPDATE_TIME;

    //~ Constructors .................................................................................................................................

    PersonAddressTable() {
        super(PersonAddress.class, "MODEL", "PERSON_ADDRESS", "", Modifier.NONE, CacheType.NONE);
        DOC_TYPE    = intField("docType", "DOC_TYPE");
        DOC_CODE    = strField("docCode", "DOC_CODE", 30);
        SEQ_ID      = intField("seqId", "SEQ_ID");
        STREET      = strField("street", "STREET", 30);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(DOC_TYPE, DOC_CODE, SEQ_ID));
    }

    //~ Methods ......................................................................................................................................

    @Override public PersonAddressTable as(final String alias) {
        return createAlias(new PersonAddressTable(), alias);
    }

    @Override protected EntityTable<PersonAddress, Tuple3<Integer, String, Integer>> createEntityTable() {
        return new InnerEntityTable<>(PERSON_ADDRESS);
    }

    @Override protected Tuple3<Integer, String, Integer> strToKey(@NotNull String key) {
        final String[] parts = Strings.splitToArray(key, 3);
        return Tuple.tuple(Integer.valueOf(parts[0]), parts[1], Integer.valueOf(parts[2]));
    }

    //~ Static Fields ................................................................................................................................

    public static final PersonAddressTable PERSON_ADDRESS = new PersonAddressTable();
}  // end class PersonAddressTable
