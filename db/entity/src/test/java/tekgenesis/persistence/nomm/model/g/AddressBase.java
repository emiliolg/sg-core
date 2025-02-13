
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
import tekgenesis.persistence.*;
import tekgenesis.persistence.nomm.model.Address;

import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.persistence.nomm.model.AddressTable.ADDRESS;

public class AddressBase implements EntityInstance<Address, String> {

    //~ Instance Fields ..............................................................................................................................

    String   code       = "";
    String   room       = "";
    String   street     = "";
    DateTime updateTime = DateTime.EPOCH;

    //~ Methods ......................................................................................................................................

    @NotNull public EntityTable<Address, String> et() {
        return myEntityTable();
    }

    @Override public boolean hasEmptyKey() {
        return code == null;
    }

    @NotNull @Override public String keyAsString() {
        return code;
    }

    @NotNull @Override public String keyObject() {
        return code;
    }

    @Override public DbTable<Address, String> table() {
        return ADDRESS;
    }

    @Override public String toString() {
        return code + " " + street;
    }

    public String getCode() {
        return code;
    }

    public String getRoom() {
        return room;
    }
    public String getStreet() {
        return street;
    }
    @NotNull public DateTime getUpdateTime() {
        return updateTime;
    }

    <T extends AddressBase> T copyTo(T to) {
        to.room       = room;
        to.code       = code;
        to.street     = street;
        to.updateTime = updateTime;
        return to;
    }

    //~ Methods ......................................................................................................................................

    @Nullable public static Address find(String code) {
        return myEntityTable().find(code);
    }

    @Nullable public static Address findWhere(Criteria p) {
        return myEntityTable().findWhere(p);
    }

    public static Select<Address> list() {
        return selectFrom(ADDRESS);
    }

    private static EntityTable<Address, String> myEntityTable() {
        return EntityTable.forTable(ADDRESS);
    }
}  // end class AddressBase
