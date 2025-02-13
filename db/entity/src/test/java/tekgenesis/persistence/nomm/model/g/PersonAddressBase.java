
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
import tekgenesis.common.core.Tuple;
import tekgenesis.common.core.Tuple3;
import tekgenesis.persistence.*;
import tekgenesis.persistence.nomm.model.AddressPhone;
import tekgenesis.persistence.nomm.model.Person;
import tekgenesis.persistence.nomm.model.PersonAddress;

import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.persistence.EntitySeq.createInnerEntitySeq;
import static tekgenesis.persistence.nomm.model.AddressPhoneTable.ADDRESS_PHONE;
import static tekgenesis.persistence.nomm.model.PersonAddressTable.PERSON_ADDRESS;
import static tekgenesis.persistence.nomm.model.PersonTable.PERSON;

public class PersonAddressBase implements InnerInstance<PersonAddress, Tuple3<Integer, String, Integer>, Person, Tuple<Integer, String>> {

    //~ Methods ......................................................................................................................................

    //J-
    String docCode         = "";
    int    docType         = 0;
    int    seqId           = 0;

    String            street          = "";
    @NotNull DateTime updateTime      = DateTime.EPOCH;
    long              instanceVersion = 0;

    EntityRef<Person, Tuple<Integer, String>> person = new EntityRef<>(PERSON);
    InnerEntitySeq<AddressPhone>              phones = createInnerEntitySeq(ADDRESS_PHONE, (PersonAddress) this, ap -> ((AddressPhoneBase) ap).personAddress);
    //J+

    @NotNull public EntityTable<PersonAddress, Tuple3<Integer, String, Integer>> et() {
        return myEntityTable();
    }

    @NotNull @Override public String keyAsString() {
        return docType + ":" + docCode + ":" + seqId;
    }
    @NotNull @Override public Tuple3<Integer, String, Integer> keyObject() {
        return tuple(docType, docCode, seqId);
    }

    @NotNull @Override public EntityRef<Person, Tuple<Integer, String>> parent() {
        return person;
    }

    @Override public int seqId() {
        return getSeqId();
    }

    @NotNull @Override public EntitySeq<PersonAddress> siblings() {
        return getPerson().getAddresses();
    }

    @Override public DbTable<PersonAddress, Tuple3<Integer, String, Integer>> table() {
        return PERSON_ADDRESS;
    }

    public String getDocCode() {
        return docCode;
    }

    public int getDocType() {
        return docType;
    }

    @Override public long getInstanceVersion() {
        return instanceVersion;
    }

    @NotNull public Person getPerson() {
        return person.solveOrFail(tuple(docType, docCode));
    }

    public InnerEntitySeq<AddressPhone> getPhones() {
        return phones;
    }

    public int getSeqId() {
        return seqId;
    }

    public String getStreet() {
        return street;
    }

    @NotNull public DateTime getUpdateTime() {
        return updateTime;
    }

    <T extends PersonAddressBase> T copyTo(T to) {
        to.docCode         = docCode;
        to.docType         = docType;
        to.seqId           = seqId;
        to.street          = street;
        to.updateTime      = updateTime;
        to.instanceVersion = instanceVersion;
        return to;
    }

    //~ Methods ......................................................................................................................................

    @Nullable public static PersonAddress find(int docType, String docCode, int seqId) {
        return myEntityTable().find(tuple(docType, docCode, seqId));
    }

    private static EntityTable<PersonAddress, Tuple3<Integer, String, Integer>> myEntityTable() {
        return EntityTable.forTable(PERSON_ADDRESS);
    }
}  // end class PersonAddressBase
