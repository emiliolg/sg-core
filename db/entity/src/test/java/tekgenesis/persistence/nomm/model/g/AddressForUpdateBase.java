
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.nomm.model.g;

import tekgenesis.common.core.DateTime;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.persistence.nomm.model.Address;
import tekgenesis.persistence.nomm.model.AddressForUpdate;

import static tekgenesis.common.Predefined.cast;

@SuppressWarnings({ "JavaDoc", "WeakerAccess", "DuplicateStringLiteralInspection", "MagicNumber", "ParameterHidesMemberVariable" })
public class AddressForUpdateBase extends Address implements PersistableInstance<Address, String> {

    //~ Methods ......................................................................................................................................

    public AddressForUpdate setRoom(String room) {
        ((AddressBase) this).room = room;
        return cast(this);
    }

    public AddressForUpdate setStreet(String street) {
        ((AddressBase) this).street = street;
        return cast(this);
    }

    public AddressForUpdate setUpdateTime(DateTime updateTime) {
        ((AddressBase) this).updateTime = updateTime;
        return cast(this);
    }

    //~ Methods ......................................................................................................................................

    public static AddressForUpdate addressForUpdate(String code) {
        final AddressForUpdate result = new AddressForUpdate();
        ((AddressBase) result).code = code;
        return result;
    }
    public static AddressForUpdate addressForUpdate(AddressBase address) {
        return address.copyTo(new AddressForUpdate());
    }
}
