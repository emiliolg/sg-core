
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.nomm.model.g;

import java.util.EnumSet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.EntityRef;
import tekgenesis.persistence.EntityTable;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.persistence.nomm.model.Address;
import tekgenesis.persistence.nomm.model.Country;
import tekgenesis.persistence.nomm.model.Participant;

import static tekgenesis.persistence.nomm.model.AddressTable.ADDRESS;
import static tekgenesis.persistence.nomm.model.ParticipantTable.PARTICIPANT;

public class ParticipantBase implements PersistableInstance<Participant, Integer> {

    //~ Instance Fields ..............................................................................................................................

    private final EntityRef<Address, String> address = new EntityRef<>(ADDRESS);

    @Nullable private String  addressCode     = null;
    private String            firstName       = "";
    private int               id;
    private long              instanceVersion = 0;
    private String            lastName        = "";
    private EnumSet<Country>  nationalities   = EnumSet.noneOf(Country.class);
    @NotNull
    @SuppressWarnings("FieldMayBeFinal")
    private DateTime          updateTime = DateTime.EPOCH;

    //~ Methods ......................................................................................................................................

    @NotNull public EntityTable<Participant, Integer> et() {
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

    @Override public DbTable<Participant, Integer> table() {
        return PARTICIPANT;
    }

    @Nullable public Address getAddress() {
        return address.solve(addressCode);
    }

    public void setAddress(@Nullable Address address) {
        this.address.set(address);
        addressCode = address == null ? null : address.getCode();
    }

    // If the address is not optional generate this code -->
    //
    // public void setAddress(@NotNull Address address) {
    // this.address.set(address);
    // addressCode = address.getCode();
    // }

    public String getFirstName() {
        return firstName;
    }

    public Participant setFirstName(String fn) {
        firstName = fn;
        return (Participant) this;
    }

    public int getId() {
        return id;
    }

    @Override public long getInstanceVersion() {
        return instanceVersion;
    }

    public String getLastName() {
        return lastName;
    }

    public Participant setLastName(String s) {
        lastName = s;
        return (Participant) this;
    }

    public EnumSet<Country> getNationalities() {
        return nationalities;
    }

    public Participant setNationalities(EnumSet<Country> v) {
        nationalities = v;
        return (Participant) this;
    }

    @NotNull public DateTime getUpdateTime() {
        return updateTime;
    }

    //~ Methods ......................................................................................................................................

    public static Participant create(int id) {
        final Participant p = new Participant();
        ((ParticipantBase) p).id = id;
        return p;
    }

    @Nullable public static Participant find(int id) {
        return EntityTable.forTable(PARTICIPANT).find(id);
    }

    private static EntityTable<Participant, Integer> myEntityTable() {
        return EntityTable.forTable(PARTICIPANT);
    }
}  // end class ParticipantBase
