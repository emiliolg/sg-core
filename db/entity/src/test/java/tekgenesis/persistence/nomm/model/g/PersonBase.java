
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
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.persistence.*;
import tekgenesis.persistence.nomm.model.Person;
import tekgenesis.persistence.nomm.model.PersonAddress;
import tekgenesis.persistence.nomm.model.Sex;

import static tekgenesis.persistence.EntitySeq.createInnerEntitySeq;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.persistence.nomm.model.PersonAddressTable.PERSON_ADDRESS;
import static tekgenesis.persistence.nomm.model.PersonTable.PERSON;

public class PersonBase implements EntityInstance<Person, Tuple<Integer, String>> {

    //~ Instance Fields ..............................................................................................................................

    EntitySeq.Inner<PersonAddress> addresses = createInnerEntitySeq(PERSON_ADDRESS, (Person) this, a -> ((PersonAddressBase) a).person);

    DateOnly birthday = null;

    String docCode = "";

    int    docType;
    String firstName       = "";
    long   instanceVersion = 0;

    String lastName = "";

    BigDecimal salary = BigDecimal.ZERO;
    Sex        sex    = Sex.F;

    DateTime updateTime = DateTime.EPOCH;

    //~ Methods ......................................................................................................................................

    @NotNull public EntityTable<Person, Tuple<Integer, String>> et() {
        return myEntityTable();
    }

    @NotNull @Override public String keyAsString() {
        return docType + ":" + Strings.escapeCharOn(docCode, ':');
    }

    @NotNull @Override public Tuple<Integer, String> keyObject() {
        return Tuple.tuple(docType, docCode);
    }

    @Override public DbTable<Person, Tuple<Integer, String>> table() {
        return PERSON;
    }

    @Override public String toString() {
        return firstName + " " + lastName;
    }

    public EntitySeq<PersonAddress> getAddresses() {
        return addresses;
    }

    public DateOnly getBirthday() {
        return birthday;
    }

    public String getDocCode() {
        return docCode;
    }

    public int getDocType() {
        return docType;
    }

    public String getFirstName() {
        return firstName;
    }

    @Override public long getInstanceVersion() {
        return instanceVersion;
    }

    public String getLastName() {
        return lastName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public Sex getSex() {
        return sex;
    }

    @NotNull public DateTime getUpdateTime() {
        return updateTime;
    }
    <T extends PersonBase> T copyTo(T to) {
        to.addresses  = addresses;
        to.birthday   = birthday;
        to.docCode    = docCode;
        to.docType    = docType;
        to.firstName  = firstName;
        to.lastName   = lastName;
        to.salary     = salary;
        to.sex        = sex;
        to.updateTime = updateTime;
        return to;
    }

    //~ Methods ......................................................................................................................................

    @Nullable public static Person find(String key) {
        return myEntityTable().findByString(key);
    }

    @Nullable public static Person find(int docType, String docCode) {
        return myEntityTable().find(Tuple.tuple(docType, docCode));
    }

    @Nullable public static Person findByCode(String docCode, int docType) {
        return myEntityTable().findByKey(0, Tuple.tuple(docCode, docType));
    }

    @Nullable public static Person findByDocCode(String c) {
        return myEntityTable().findWhere(PERSON.DOC_CODE.eq(c));
    }
    public static Select<Person> list() {
        return selectFrom(PERSON);
    }

    public static ImmutableList<Person> list(Set<Tuple<Integer, String>> keys) {
        return myEntityTable().list(keys);
    }

    public static ImmutableList<Person> list(Iterable<String> keys) {
        return myEntityTable().listFromStringKeys(keys);
    }
    private static EntityTable<Person, Tuple<Integer, String>> myEntityTable() {
        return EntityTable.forTable(PERSON);
    }
}  // end class PersonBase
