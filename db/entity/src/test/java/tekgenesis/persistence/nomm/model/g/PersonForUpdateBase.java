
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.nomm.model.g;

import java.math.BigDecimal;

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Decimals;
import tekgenesis.common.core.Tuple;
import tekgenesis.persistence.InnerEntitySeqForUpdate;
import tekgenesis.persistence.PersistableInstance;
import tekgenesis.persistence.nomm.model.*;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.persistence.EntitySeq.createInnerEntitySeq;
import static tekgenesis.persistence.nomm.model.PersonAddressTable.PERSON_ADDRESS;

public class PersonForUpdateBase extends Person implements PersistableInstance<Person, Tuple<Integer, String>> {

    //~ Methods ......................................................................................................................................

    public long incrementVersion() {
        return ((PersonBase) this).instanceVersion++;
    }

    @Override public InnerEntitySeqForUpdate<PersonAddressForUpdate, PersonAddress> getAddresses() {
        return cast(super.getAddresses());
    }

    public PersonForUpdate setBirthday(DateOnly v) {
        ((PersonBase) this).birthday = v;
        return (PersonForUpdate) this;
    }
    public PersonForUpdate setFirstName(String v) {
        ((PersonBase) this).firstName = v;
        return (PersonForUpdate) this;
    }
    public PersonForUpdate setLastName(String v) {
        ((PersonBase) this).lastName = v;
        return (PersonForUpdate) this;
    }
    public PersonForUpdate setSalary(BigDecimal v) {
        ((PersonBase) this).salary = Decimals.scaleAndCheck("salary", v, true, 10, 2);
        return (PersonForUpdate) this;
    }
    public PersonForUpdate setSex(Sex v) {
        ((PersonBase) this).sex = v;
        return (PersonForUpdate) this;
    }
    public PersonForUpdate setUpdateTime(DateTime v) {
        ((PersonBase) this).updateTime = v;
        return (PersonForUpdate) this;
    }

    //~ Methods ......................................................................................................................................

    /** Javadoc. */
    public static PersonForUpdate create(int docType, String docCode) {
        final PersonForUpdate p = new PersonForUpdate();
        ((PersonBase) p).docType = docType;
        ((PersonBase) p).docCode = docCode;
        init(p);
        return p;
    }

    public static PersonForUpdate personForUpdate(PersonBase person) {
        final PersonForUpdate p = person.copyTo(new PersonForUpdate());
        init(p);
        return p;
    }

    private static void init(PersonForUpdate p) {
        ((PersonBase) p).addresses = createInnerEntitySeq(PersonAddressForUpdate::personAddressForUpdate,
                PERSON_ADDRESS,
                p,
                a -> ((PersonAddressBase) a).person);
    }
}  // end class PersonForUpdateBase
