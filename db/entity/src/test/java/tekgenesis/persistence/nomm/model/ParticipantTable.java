
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
public class ParticipantTable extends DbTable<Participant, Integer> {

    //~ Instance Fields ..............................................................................................................................

    public final TableField.Str                             ADDRESS_CODE;
    public final TableField.Str                             FIRST_NAME;
    public final TableField.Int                             ID;
    public final TableField.Str                             LAST_NAME;
    public final TableField.EnumerationSet<Country, String> NATIONALITIES;
    public final TableField.DTime                           UPDATE_TIME;

    //~ Constructors .................................................................................................................................

    ParticipantTable() {
        super(Participant.class, "MODEL", "PARTICIPANT", "", Modifier.NONE, CacheType.NONE);
        ID            = intField("id", "ID");
        FIRST_NAME    = strField("firstName", "FIRST_NAME", 30);
        LAST_NAME     = strField("lastName", "LAST_NAME", 30);
        ADDRESS_CODE  = strField("addressCode", "ADDRESS_CODE", 30);
        NATIONALITIES = enumSetField("nationalities", "NATIONALITIES", Country.class);
        UPDATE_TIME   = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ......................................................................................................................................

    @Override public ParticipantTable as(final String alias) {
        return createAlias(new ParticipantTable(), alias);
    }

    @Override protected EntityTable<Participant, Integer> createEntityTable() {
        return new EntityTable<>(PARTICIPANT);
    }

    @Override protected Integer strToKey(@NotNull String key) {
        return Integer.valueOf(key);
    }

    //~ Static Fields ................................................................................................................................

    public static final ParticipantTable PARTICIPANT = new ParticipantTable();
}  // end class ParticipantTable
