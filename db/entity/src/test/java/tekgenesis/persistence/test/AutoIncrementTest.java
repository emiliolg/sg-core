
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.test;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.TransactionalRule;
import tekgenesis.persistence.InnerEntitySeq;
import tekgenesis.test.basic.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.common.tools.test.Tests.assertNotNull;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber", "OverlyLongMethod" })
public class AutoIncrementTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;
    private final DbRule     db     = new DbRule() {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    @Rule public TestRule t = TransactionalRule.into(db);

    //~ Methods ......................................................................................................................................

    @Test public void innerAutoIncrement() {
        final GrandFather grandFather = GrandFather.create(1234);
        grandFather.setName("Grand Father");
        grandFather.insert();

        grandFather.getChildren().merge(listOf("Father"), (f, fv) -> {
                f.setName(fv);
                f.getGrandChildren().merge(listOf("Son"), Son::setName);
            });

        grandFather.update();

        final GrandFather storedGrandFather = assertNotNull(GrandFather.find(1234));
        assertThat(storedGrandFather.getName()).isEqualTo("Grand Father");

        final ImmutableList<Father> storedChildren = storedGrandFather.getChildren().toList();
        assertThat(storedChildren).hasSize(1).extracting("name").containsExactly("Father");

        final ImmutableList<Son> storedGrandChildren = storedChildren.get(0).getGrandChildren().toList();
        assertThat(storedGrandChildren).hasSize(1).extracting("name").containsExactly("Son");
    }

    @Test public void innerDeletionReordering() {
        final GrandFather grandFather = GrandFather.create(1);
        grandFather.setName("Grand Father");
        grandFather.insert();

        //J-
        final ImmutableList<Tuple<String, ImmutableList<String>>> values = listOf(
                tuple("Father A", listOf("Son One", "Son Two")),
                tuple("Father B", listOf("Son Three", "Son Four"))
        );
        //J+

        grandFather.getChildren().merge(values, (c, v) -> {
                c.setName(v._1());
                c.getGrandChildren().merge(v._2(), Son::setName);
            });

        grandFather.update();

        final GrandFather storedGrandFather = assertNotNull(GrandFather.find(1));
        assertThat(storedGrandFather.getName()).isEqualTo("Grand Father");

        final ImmutableList<Father> storedChildren = storedGrandFather.getChildren().toList();
        assertThat(storedChildren).hasSize(2).extracting("name").containsExactly("Father A", "Father B");

        final ImmutableList<Son> storedGrandChildrenForA = storedChildren.get(0).getGrandChildren().toList();
        assertThat(storedGrandChildrenForA).hasSize(2).extracting("name").containsExactly("Son One", "Son Two");

        final ImmutableList<Son> storedGrandChildrenForB = storedChildren.get(1).getGrandChildren().toList();
        assertThat(storedGrandChildrenForB).hasSize(2).extracting("name").containsExactly("Son Three", "Son Four");

        storedChildren.get(0).delete();

        storedGrandFather.update();

        final GrandFather finalGrandFather = assertNotNull(GrandFather.find(1));
        assertThat(finalGrandFather.getName()).isEqualTo("Grand Father");

        final ImmutableList<Father> finalStoredChildren = finalGrandFather.getChildren().toList();
        assertThat(finalStoredChildren).hasSize(1).extracting("name").containsExactly("Father B");

        final ImmutableList<Son> finalGrandChildrenForB = finalStoredChildren.get(0).getGrandChildren().toList();
        assertThat(finalGrandChildrenForB).hasSize(2).extracting("name").containsExactly("Son Three", "Son Four");
    }  // end method innerDeletionReordering

    @Test public void innerSeqAutoIncrement() {
        final Sequencer sequencer = Sequencer.create();
        sequencer.setName("Sequencer Name");

        sequencer.getRows().merge(listOf("Row Name"), Row::setName);

        sequencer.insert();

        final Sequencer storedSequencer = assertNotNull(Sequencer.find(sequencer.getId()));
        assertThat(storedSequencer.getName()).isEqualTo("Sequencer Name");

        final InnerEntitySeq<Row> storedRows = storedSequencer.getRows();
        assertThat(storedRows).hasSize(1).extracting("name").containsExactly("Row Name");
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class AutoIncrementTest
