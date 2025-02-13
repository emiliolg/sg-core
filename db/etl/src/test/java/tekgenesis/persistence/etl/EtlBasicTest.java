
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.etl;

import java.io.*;

import org.eclipse.jetty.io.WriterOutputStream;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.tools.test.DatabaseRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.persistence.InnerEntitySeqForUpdate;
import tekgenesis.persistence.nomm.model.AddressPhone;
import tekgenesis.persistence.nomm.model.PersonAddress;
import tekgenesis.persistence.nomm.model.PersonAddressForUpdate;
import tekgenesis.persistence.nomm.model.PersonForUpdate;
import tekgenesis.persistence.nomm.test.NoMmDbRule;

import static java.util.Arrays.asList;

import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.persistence.etl.EntityEtl.csv;
import static tekgenesis.persistence.etl.EntityEtl.xml;
import static tekgenesis.persistence.etl.EntityExporter.export;
import static tekgenesis.persistence.etl.EntityImporter.load;
import static tekgenesis.persistence.etl.EtlTests.*;
import static tekgenesis.persistence.nomm.model.AddressTable.ADDRESS;
import static tekgenesis.persistence.nomm.model.PersonTable.PERSON;
import static tekgenesis.transaction.Transaction.runInTransaction;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class EtlBasicTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;

    @Rule public DatabaseRule db = new NoMmDbRule() {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    //~ Methods ......................................................................................................................................

    @Test public void basicCSVImportExportTest()
        throws FileNotFoundException
    {
        final String fileName = "Addresses.csv";

        final long ts = System.currentTimeMillis();
        runInTransaction(() -> load(ADDRESS).using(csv()).from(new File(dataDir, fileName)));
        System.out.printf("Elapsed %d ms.%n", System.currentTimeMillis() - ts);

        final File outFile = new File(xmlDir, "Addresses.csv");
        runInTransaction(() -> export(ADDRESS).fields(ADDRESS.CODE, ADDRESS.STREET, ADDRESS.ROOM).using(csv()).into(outFile));
        checkFile(outFile);

        final File outFile2 = new File(xmlDir, "Addresses2.csv");
        runInTransaction(() -> export(ADDRESS).fields(ADDRESS.STREET).using(csv()).into(outFile2));
        checkFile(outFile2);
    }

    @Test public void basicXmlImportExportTest()
        throws FileNotFoundException
    {
        final String fileName = "Addresses.xml";

        runInTransaction(() -> load(ADDRESS).using(xml()).from(new File(dataDir, fileName)));

        final File outFile = new File(xmlDir, fileName);
        runInTransaction(() -> export(ADDRESS).fields(ADDRESS.CODE, ADDRESS.STREET, ADDRESS.ROOM).using(xml()).into(outFile));
        checkFile(outFile);

        final File outFile2 = new File(xmlDir, "Addresses2.xml");
        runInTransaction(() -> export(ADDRESS).fields(ADDRESS.STREET).using(xml()).into(outFile2));
        checkFile(outFile2);
    }

    @Test public void innerEntitiesXmlExportTest()
        throws IOException
    {
        runInTransaction(() -> {
            for (final String doc : asList("111", "112", "113", "121")) {
                final PersonForUpdate person = PersonForUpdate.create(1, doc)
                                                              .setFirstName("Juan ")
                                                              .setLastName("Perez" + doc)
                                                              .setBirthday(DateOnly.fromString("2012-12-20"));

                final InnerEntitySeqForUpdate<PersonAddressForUpdate, PersonAddress> addresses = person.getAddresses();
                for (final String street : asList("A", "B")) {
                    final PersonAddress address = addresses.add().setStreet(street);
                    address.getPhones().merge(Seq.fromTo(10, 11), AddressPhone::setPhone);
                }
                person.insert();
            }
        });

        final StringWriter w = new StringWriter();
        runInTransaction(() -> export(PERSON).using(xml()).into(new WriterOutputStream(w)));
        final BufferedReader reader = new BufferedReader(new StringReader(w.toString()));

        final File        xmlFile = new File(xmlDir, "Persons.xml");
        final PrintWriter f       = new PrintWriter(xmlFile);
        String            line;
        while ((line = reader.readLine()) != null) {
            if (!line.contains("<updateTime>")) f.println(line);
        }
        f.close();
        checkFile(xmlFile);
    }

    @Test public void tabCSVImportTest()
        throws FileNotFoundException
    {
        runInTransaction(() -> load(ADDRESS).using(csv()).from(new File(dataDir, "Addresses.tcsv")));

        final File outFile = new File(xmlDir, "Addresses.csv");
        runInTransaction(() -> export(ADDRESS).fields(ADDRESS.CODE, ADDRESS.STREET, ADDRESS.ROOM).using(csv()).into(outFile));
        checkFile(outFile);
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class EtlBasicTest
