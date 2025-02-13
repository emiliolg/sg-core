
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.nomm.test;

import java.util.EnumSet;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.core.Tuple3;
import tekgenesis.common.tools.test.DatabaseRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.TransactionalRule;
import tekgenesis.database.exception.UniqueViolationException;
import tekgenesis.persistence.Criteria;
import tekgenesis.persistence.DbTable;
import tekgenesis.persistence.TableMetadata;
import tekgenesis.persistence.nomm.model.*;
import tekgenesis.transaction.Transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.DateOnly.fromString;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.common.tools.test.Tests.assertNotNull;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.persistence.nomm.model.Country.*;
import static tekgenesis.persistence.nomm.model.ParticipantTable.PARTICIPANT;
import static tekgenesis.persistence.nomm.model.PersonAddressTable.PERSON_ADDRESS;
import static tekgenesis.persistence.nomm.model.g.AddressForUpdateBase.addressForUpdate;
import static tekgenesis.persistence.nomm.model.g.PersonForUpdateBase.personForUpdate;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class BasicPersistentTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    private final DatabaseRule db = new NoMmDbRule() {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    @Rule public TestRule t = TransactionalRule.into(db);

    //~ Methods ......................................................................................................................................

    @Test public void compositeKeyTest() {
        createPerson(1, "87654321", "John", "Petersen", "2012-12-15");
        createPerson(2, "12345678", "Juan", "Perez", "2012-12-20");
        createPerson(3, "12341234", "Ivan", "Petrovich", "2012-1-1");

        checkPerson(assertNotNull(Person.find(2, "12345678")), "Juan", "Perez", "2012-12-20");
        checkPerson(assertNotNull(Person.find(1, "87654321")), "John", "Petersen", "2012-12-15");
        checkPerson(assertNotNull(Person.findByCode("12345678", 2)), "Juan", "Perez", "2012-12-20");

        final Set<Tuple<Integer, String>> persons = Colls.set(tuple(1, "87654321"), tuple(2, "12345678"), tuple(3, "12341234"));

        assertThat(Person.list(persons).toList().toStrings()).containsExactly("John Petersen", "Juan Perez", "Ivan Petrovich");
        assertThat(Person.list(listOf("1:87654321", "2:12345678", "3:12341234")).toList().toStrings()).containsExactly("John Petersen",
            "Juan Perez",
            "Ivan Petrovich");

        personForUpdate(assertNotNull(Person.find(2, "12345678"))).setFirstName("Jose").update();
        personForUpdate(assertNotNull(Person.find(1, "87654321"))).delete();

        final Person p3 = assertNotNull(Person.find(2, "12345678"));
        assertThat(p3.getFirstName()).isEqualTo("Jose");
        assertThat(p3.getLastName()).isEqualTo("Perez");

        assertThat(Person.find(1, "87654321")).isNull();

        final PersonForUpdate p1New = PersonForUpdate.create(1, "87654321").setFirstName("Johny").setLastName("Petersen").setBirthday(DateOnly.EPOCH);
        final PersonForUpdate p2New = personForUpdate(assertNotNull(Person.find(2, "12345678"))).setFirstName("Johny");
        final PersonForUpdate p3New = personForUpdate(assertNotNull(Person.find(3, "12341234"))).setFirstName("Johny");

        Transaction.getCurrent().ifPresent(t -> {
            t.beginBatch();
            p1New.merge();
            p2New.merge();
            p3New.merge();
            t.endBatch();
        });
        assertThat(Person.list(persons).toList()).extracting("firstName").containsExactly("Johny", "Johny", "Johny");
    }  // end method compositeKeyTest

    @Test public void enumSetTest() {
        Participant.create(1).setFirstName("Juan").setLastName("Pérez").setNationalities(EnumSet.of(ARGENTINA, SPAIN)).insert();
        Participant.create(2).setFirstName("Giovanni").setLastName("D'Pietro").setNationalities(EnumSet.of(ARGENTINA, ITALY)).insert();
        Participant.create(3).setFirstName("Hans").setLastName("Peters").setNationalities(EnumSet.of(ARGENTINA, GERMANY)).insert();
        Participant.create(4).setFirstName("Omni").setLastName("Present").setNationalities(EnumSet.allOf(Country.class)).insert();

        assertThat(selectFrom(PARTICIPANT).where(PARTICIPANT.NATIONALITIES.contains(ARGENTINA)).count()).isEqualTo(4);
        assertThat(selectFrom(PARTICIPANT).where(PARTICIPANT.NATIONALITIES.contains(UNITED_STATES)).count()).isEqualTo(1);
        assertThat(selectFrom(PARTICIPANT).where(PARTICIPANT.NATIONALITIES.doesNotContain(GERMANY)).count()).isEqualTo(2);
        assertThat(selectFrom(PARTICIPANT).where(PARTICIPANT.NATIONALITIES.eq(EnumSet.of(ARGENTINA, SPAIN))).count()).isEqualTo(1);

        assertThat(selectFrom(PARTICIPANT).where(PARTICIPANT.NATIONALITIES.containsAnyOf(EnumSet.of(ITALY, SPAIN))).count()).isEqualTo(3);
        assertThat(selectFrom(PARTICIPANT).where(PARTICIPANT.NATIONALITIES.containsAnyOf(ITALY, SPAIN)).count()).isEqualTo(3);

        assertThat(selectFrom(PARTICIPANT).where(PARTICIPANT.NATIONALITIES.containsAllOf(EnumSet.of(ARGENTINA, ITALY))).count()).isEqualTo(2);
        assertThat(selectFrom(PARTICIPANT).where(PARTICIPANT.NATIONALITIES.containsAllOf(ARGENTINA, ITALY)).count()).isEqualTo(2);
    }

    @Test public void simplePersistenceTest() {
        final Address address = addressForUpdate("1234").setRoom("123").setStreet("123").insert();

        final Participant participant = Participant.create(1234);
        participant.setFirstName("123");
        participant.setLastName("123");
        participant.setAddress(address);

        participant.insert();

        final Address storedAddress = assertNotNull(Address.find("1234"));
        assertThat(storedAddress.getCode()).isEqualTo("1234");
        assertThat(storedAddress.getRoom()).isEqualTo("123");
        assertThat(storedAddress.getStreet()).isEqualTo("123");

        final Participant storedParticipant = assertNotNull(Participant.find(1234));
        assertThat(storedParticipant.getId()).isEqualTo(1234);
        assertThat(storedParticipant.getFirstName()).isEqualTo("123");
        assertThat(storedParticipant.getLastName()).isEqualTo("123");

        final Address participantAddress = assertNotNull(storedParticipant.getAddress());
        assertThat(participantAddress.getCode()).isEqualTo("1234");
        assertThat(participantAddress.getRoom()).isEqualTo("123");
        assertThat(participantAddress.getStreet()).isEqualTo("123");

        assertThat(storedParticipant.getAddress()).isSameAs(participantAddress);

        addressForUpdate(storedAddress).setRoom("321").update();

        assertThat(assertNotNull(Address.find("1234")).getRoom()).isEqualTo("321");
    }  // end method simplePersistenceTest

    @Test public void testBatch() {
        Transaction.getCurrent().ifPresent(t -> {
            try {
                t.beginBatch();
                Participant.create(1).setFirstName("Juan").setLastName("Pérez").setNationalities(EnumSet.of(ARGENTINA, SPAIN)).insert();
                Participant.create(1).setFirstName("Giovanni").setLastName("D'Pietro").setNationalities(EnumSet.of(ARGENTINA, ITALY)).insert();
                Participant.create(3).setFirstName("Hans").setLastName("Peters").setNationalities(EnumSet.of(ARGENTINA, GERMANY)).insert();
                Participant.create(4).setFirstName("Omni").setLastName("Present").setNationalities(EnumSet.allOf(Country.class)).insert();
                t.endBatch();

                assertThat(selectFrom(PARTICIPANT).where(PARTICIPANT.NATIONALITIES.contains(ARGENTINA)).count()).isEqualTo(4);
                assertThat(selectFrom(PARTICIPANT).where(PARTICIPANT.NATIONALITIES.contains(UNITED_STATES)).count()).isEqualTo(1);
                assertThat(selectFrom(PARTICIPANT).where(PARTICIPANT.NATIONALITIES.doesNotContain(GERMANY)).count()).isEqualTo(2);
                assertThat(selectFrom(PARTICIPANT).where(PARTICIPANT.NATIONALITIES.eq(EnumSet.of(ARGENTINA, SPAIN))).count()).isEqualTo(1);

                assertThat(selectFrom(PARTICIPANT).where(PARTICIPANT.NATIONALITIES.containsAnyOf(EnumSet.of(ITALY, SPAIN))).count()).isEqualTo(3);
                assertThat(selectFrom(PARTICIPANT).where(PARTICIPANT.NATIONALITIES.containsAnyOf(ITALY, SPAIN)).count()).isEqualTo(3);

                assertThat(selectFrom(PARTICIPANT).where(PARTICIPANT.NATIONALITIES.containsAllOf(EnumSet.of(ARGENTINA, ITALY))).count()).isEqualTo(2);
                assertThat(selectFrom(PARTICIPANT).where(PARTICIPANT.NATIONALITIES.containsAllOf(ARGENTINA, ITALY)).count()).isEqualTo(2);
            }
            catch (final Exception e) {
                assertThat(e).isInstanceOf(UniqueViolationException.class);
            }
        });
    }

    @Test public void testBatchRollback() {
        try {
            Transaction.getCurrent().ifPresent(t -> {
                t.beginBatch();
                Participant.create(1).setFirstName("Juan").setLastName("Pérez").setNationalities(EnumSet.of(ARGENTINA, SPAIN)).insert();
                Participant.create(1).setFirstName("Giovanni").setLastName("D'Pietro").setNationalities(EnumSet.of(ARGENTINA, ITALY)).insert();
            });
        }
        catch (final Exception e) {
            assertThat(e).isInstanceOf(UniqueViolationException.class);
        }
    }

    @Test public void testMetadata() {
        final TableMetadata<PersonAddress, Tuple3<Integer, String, Integer>> metadata = PERSON_ADDRESS.metadata();
        assertThat(metadata.keyFromString("1:a:2")).isEqualTo(Tuple.tuple(1, "a", 2));

        final Criteria eq = metadata.buildKeyCriteria(Tuple.tuple(1, "a", 2));
        assertThat(eq.toString()).isEqualTo("PERSON_ADDRESS.DOC_TYPE = 1 and PERSON_ADDRESS.DOC_CODE = n'a' and PERSON_ADDRESS.SEQ_ID = 2");

        final Criteria gt = metadata.buildKeyCriteriaGT(Tuple.tuple(1, "a", 2));
        assertThat(gt.toString()).isEqualTo(
            "(PERSON_ADDRESS.DOC_TYPE > 1 or PERSON_ADDRESS.DOC_CODE > n'a' and PERSON_ADDRESS.DOC_TYPE = 1 or PERSON_ADDRESS.SEQ_ID > 2 and PERSON_ADDRESS.DOC_TYPE = 1 and PERSON_ADDRESS.DOC_CODE = n'a')");

        final PersonAddress personAddress = new PersonAddress();
        assertThat(personAddress.table()).isSameAs(PERSON_ADDRESS);
        assertThat(DbTable.forName(PersonAddress.class.getName())).isSameAs(PERSON_ADDRESS);

        assertThat(TableMetadata.forName(PersonAddress.class.getName())).isSameAs(metadata);
        assertThat(TableMetadata.entities()).contains("tekgenesis.test.basic.Author", "tekgenesis.test.basic.Book");
        assertThat(TableMetadata.tablesByEntity().get("tekgenesis.test.basic.Author")).isEqualTo("BASIC_TEST.AUTHOR");
        TableMetadata.localEntities(db.getEnv());
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    private static void checkPerson(final Person p1, final String name, final String lastName, final String birthday) {
        assertThat(p1.getFirstName()).isEqualTo(name);
        assertThat(p1.getLastName()).isEqualTo(lastName);
        assertThat(p1.getBirthday()).isEqualTo(fromString(birthday));
    }

    private static Person createPerson(int dt, String dc, String fn, String ln, String birthday) {
        return PersonForUpdate.create(dt, dc).setFirstName(fn).setLastName(ln).setBirthday(fromString(birthday)).insert();
    }
}  // end class BasicPersistentTest
