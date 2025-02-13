
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.common.env;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.app.env.DatabaseEnvironment;
import tekgenesis.cluster.ClusterManager;
import tekgenesis.cluster.LocalClusterManager;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.impl.MemoryEnvironment;
import tekgenesis.common.env.impl.PropertiesEnvironment;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * PropertyEnvironment Test Suite;
 */
@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class PropertyEnvironmentTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameterized.Parameter public String dbName = null;
    @Rule public DbRule                    rule   = new DbRule() {
            @Override protected void before() {
                createDatabase(dbName);
                Context.getContext().setSingleton(ClusterManager.class, new LocalClusterManager());
            }
        };

    //~ Methods ......................................................................................................................................

    @Test public void propertiesBasicMemory() {
        final Person p = new Person("P.Sherman", "42 Wallaby Way, Sidney", "40");

        final MemoryEnvironment memEnv = new MemoryEnvironment();
        memEnv.put(p);

        final Person person1 = memEnv.get("x.y", Person.class);
        assertThat(person1.address).isEqualTo(p.address);
        assertThat(person1.age).isEqualTo(p.age);
        assertThat(person1.name).isEqualTo(p.name);

        final Person person2 = memEnv.get("x.y", Person.class);
        assertThat(person2.address).isEqualTo(p.address);
        assertThat(person2.age).isEqualTo(p.age);
        assertThat(person2.name).isEqualTo(p.name);
    }

    @Test public void propertiesDatabaseBasic() {
        basicPropertyTest(new DatabaseEnvironment());
    }

    @Test public void propertiesFile() {
        final String path = "server/core/src/test/resources";
        final File   file = new File(path, "test.properties");

        final PropertiesEnvironment propsEnv = new PropertiesEnvironment(file);

        assertThat(propsEnv.get("a", EnvironmentTest.Age.class).value).isEqualTo(10);
        assertThat(propsEnv.get("b", EnvironmentTest.Age.class).value).isEqualTo(30);

        final Integer[] lValue = { 0 };

        assertThat(propsEnv.get("c", EnvironmentTest.Age.class, value -> lValue[0] = value.orElseGet(() -> new EnvironmentTest.Age(0)).value).value)
            .isEqualTo(20);

        System.setProperty("c.age.value", "15");
        propsEnv.refresh();

        assertThat(lValue[0]).isGreaterThan(0);
        assertThat(propsEnv.get("c", EnvironmentTest.Age.class).value).isEqualTo(15);
    }

    @Test public void propertiesFileBasic() {
        basicPropertyTest(new PropertiesEnvironment());
    }

    private void basicPropertyTest(PropertiesEnvironment propsEnv) {
        final Person p = new Person("P.Sherman", "42 Wallaby Way, Sidney", "40");
        System.setProperty("test.str", "10");
        System.setProperty("person.name", p.name);
        System.setProperty("person.address", p.address);
        System.setProperty("person.age", p.age);

        propsEnv.refresh();

        final EnvironmentTest.StrListener testListener = new EnvironmentTest.StrListener();

        assertThat(propsEnv.get("", "test", EnvironmentTest.Str.class, testListener).str).isEqualTo("10");

        assertThat(propsEnv.get(Person.class).address).isEqualTo(p.address);
        assertThat(propsEnv.get(Person.class).age).isEqualTo(p.age);
        assertThat(propsEnv.get(Person.class).name).isEqualTo(p.name);

        final Person person1 = propsEnv.get("x.y", Person.class);
        assertThat(person1.address).isEqualTo(p.address);
        assertThat(person1.age).isEqualTo(p.age);
        assertThat(person1.name).isEqualTo(p.name);

        final Person person2 = propsEnv.get("x.y", Person.class);
        assertThat(person2.address).isEqualTo(p.address);
        assertThat(person2.age).isEqualTo(p.age);
        assertThat(person2.name).isEqualTo(p.name);

        System.setProperty("test.str", "12");
        propsEnv.refresh();
        assertThat(testListener.lastValue).isEqualTo("12");

        propsEnv.put("test.str", "13");
        assertThat(propsEnv.get("test.str", String.class)).isEqualTo("13");

        propsEnv.put("test.str", "15");
        assertThat(propsEnv.get("test.str", String.class)).isEqualTo("15");
    }

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "database : {0}")
    @SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection" })
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    //~ Inner Classes ................................................................................................................................

    static class Person {
        @JsonProperty String address;
        @JsonProperty String age;
        @JsonProperty String name;

        Person() {
            this("", "", "0");
        }

        Person(String n, String a, String ag) {
            name    = n;
            address = a;
            age     = ag;
        }
    }
}  // end class PropertyEnvironmentTest
