
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

import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.core.Tuple3;
import tekgenesis.persistence.InnerInstance;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.persistence.nomm.model.Person;
import tekgenesis.persistence.nomm.model.PersonAddress;
import tekgenesis.persistence.nomm.model.PersonAddressForUpdate;

public class PersonAddressForUpdateBase extends PersonAddress
    implements PersistableInstance<PersonAddress, Tuple3<Integer, String, Integer>>,
               InnerInstance<PersonAddress, Tuple3<Integer, String, Integer>, Person, Tuple<Integer, String>>
{

    //~ Methods ......................................................................................................................................

    public long incrementVersion() {
        return ((PersonAddressBase) this).instanceVersion++;
    }
    public PersonAddressForUpdate setStreet(String v) {
        ((PersonAddressBase) this).street = v;
        return (PersonAddressForUpdate) this;
    }
    public PersonAddressForUpdate setUpdateTime(@NotNull DateTime v) {
        ((PersonAddressBase) this).updateTime = v;
        return (PersonAddressForUpdate) this;
    }

    //~ Methods ......................................................................................................................................

    public static PersonAddressForUpdate personAddressForUpdate(PersonAddressBase personAddress) {
        return personAddress.copyTo(new PersonAddressForUpdate());
    }
}
