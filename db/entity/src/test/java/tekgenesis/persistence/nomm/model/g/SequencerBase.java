
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.nomm.model.g;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.persistence.nomm.model.Sequencer;

import static tekgenesis.persistence.nomm.model.SequencerTable.SEQUENCER;

/**
 * Test Sequence class.
 */
public class SequencerBase implements PersistableInstance<Sequencer, Integer> {

    //~ Methods ......................................................................................................................................

    //J-
    @SuppressWarnings("FieldMayBeFinal") private Integer  id              = EntityTable.DEFAULT_EMPTY_KEY;
    @SuppressWarnings("FieldMayBeFinal") private DateTime updateTime      = DateTime.EPOCH;

    public long      instanceVersion = 0;
    private String   firstName       = "";
    private String   lastName        = "";
    //J+

    @NotNull public EntityTable<Sequencer, Integer> et() {
        return myEntityTable();
    }

    public long incrementVersion() {
        return instanceVersion++;
    }

    @NotNull @Override public String keyAsString() {
        return String.valueOf(id);
    }
    @NotNull @Override public Integer keyObject() {
        return id;
    }

    @Override public DbTable<Sequencer, Integer> table() {
        return SEQUENCER;
    }

    /** Returns first name. */
    public String getFirstName() {
        return firstName;
    }

    /** Sets first name. */
    public Sequencer setFirstName(String v) {
        firstName = v;
        return (Sequencer) this;
    }

    /** Returns key. */
    public Integer getId() {
        return id;
    }

    @Override public long getInstanceVersion() {
        return instanceVersion;
    }

    /** Returns last name. */
    public String getLastName() {
        return lastName;
    }

    /** Sets last name. */
    public Sequencer setLastName(String v) {
        lastName = v;
        return (Sequencer) this;
    }

    @NotNull public DateTime getUpdateTime() {
        return updateTime;
    }

    //~ Methods ......................................................................................................................................

    /** Find the Sequencer defined by the given key. */
    @Nullable public static Sequencer find(Integer key) {
        return myEntityTable().find(key);
    }

    private static EntityTable<Sequencer, Integer> myEntityTable() {
        return EntityTable.forTable(SEQUENCER);
    }
}  // end class SequencerBase
