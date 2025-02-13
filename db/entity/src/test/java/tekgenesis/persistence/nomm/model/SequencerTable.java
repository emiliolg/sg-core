
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.nomm.model;

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
public class SequencerTable extends DbTable<Sequencer, Integer> {

    //~ Instance Fields ..............................................................................................................................

    public final TableField.Str   FIRST_NAME;
    public final TableField.Int   ID;
    public final TableField.Str   LAST_NAME;
    public final TableField.DTime UPDATE_TIME;

    //~ Constructors .................................................................................................................................

    SequencerTable() {
        super(Sequencer.class, "MODEL", "SEQUENCER", "SEQUENCER_SEQ", Modifier.NONE, CacheType.NONE);
        ID          = intField("id", "ID");
        FIRST_NAME  = strField("firstName", "FIRST_NAME", 30);
        LAST_NAME   = strField("lastName", "LAST_NAME", 30);
        UPDATE_TIME = dTimeField("updateTime", "UPDATE_TIME");
        primaryKey(listOf(ID));
    }

    //~ Methods ......................................................................................................................................

    @Override public SequencerTable as(final String alias) {
        return createAlias(new SequencerTable(), alias);
    }

    @Override protected EntityTable<Sequencer, Integer> createEntityTable() {
        return new EntityTable<>(SEQUENCER);
    }

    @Override protected Integer strToKey(String key) {
        return Integer.valueOf(key);
    }

    //~ Static Fields ................................................................................................................................

    public static final SequencerTable SEQUENCER = new SequencerTable();
}
