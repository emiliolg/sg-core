
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.ix.ventas.MCatalogo;
import tekgenesis.ix.ventas.OPERADOR;
import tekgenesis.persistence.ix.IxStoreHandleFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import static tekgenesis.common.tools.test.Tests.assertNotNull;
import static tekgenesis.ix.ventas.g.MCatalogoTable.MCATALOGO;
import static tekgenesis.ix.ventas.g.OPERADORTable.OPERADOR_;
import static tekgenesis.ix.ventas.g.StockTable.STOCK;
import static tekgenesis.persistence.Sql.selectFrom;

/**
 * IxMockStoreHandler Test.
 */
@RunWith(Parameterized.class)
@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc, MagicNumber" })
public class IxMockStoreHandlerTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameterized.Parameter public String dbName = null;
    @Rule public DbRule                    db     = new DbRule() {
            @Override
            @SuppressWarnings("DuplicateStringLiteralInspection")
            protected void before() {
                createDatabase(dbName);

                final IxProps mydomainProp = new IxProps();
                mydomainProp.url  = "mock:localhost:8989/myDomain/ixService";
                mydomainProp.mode = IxProps.MODE.WRITE;
                Context.getEnvironment().put("myDomain", mydomainProp);

                final IxProps otherdomainProp = new IxProps();
                otherdomainProp.url  = "mock:localhost:8990/myDomain/ixService";
                otherdomainProp.mode = IxProps.MODE.WRITE;
                Context.getEnvironment().put("otherDomain", otherdomainProp);

                IxService.setDomain("myDomain");
                TableFactory.setFactory(new TableFactory(new IxStoreHandleFactory()));
            }
        };

    int n;

    //~ Methods ......................................................................................................................................

    /** Insert/Find/Delete. */
    @Test public void basicOperations() {
        final OPERADOR operator = OPERADOR.create(12);
        operator.setActivo(true);
        operator.setAgentid(0);
        operator.setDescrip("Operador Dummy");
        operator.insert();

        final OPERADOR actual = OPERADOR.find(12);
        assertThat(actual).isNotNull();
        assertThat(actual.getDescrip()).isEqualTo("Operador Dummy");

        operator.delete();

        assertThat(OPERADOR.find(12345)).isNull();
    }

    /** Multiple domain operations. */
    @Test public void multipleDomainOperations() {
        final String domain1 = "myDomain";
        IxService.setDomain(domain1);

        final int      id       = 12;
        final OPERADOR operator = OPERADOR.create(id);
        operator.setActivo(true);
        operator.setAgentid(0);
        final String descrip1 = "desc 1";
        operator.setDescrip(descrip1);
        operator.insert();

        final String domain2 = "otherDomain";
        IxService.setDomain(domain2);

        final OPERADOR operator2 = OPERADOR.create(id);
        operator2.setActivo(true);
        operator2.setAgentid(0);
        final String descrip2 = "desc 2";
        operator2.setDescrip(descrip2);
        operator2.insert();

        IxService.setDomain(domain1);
        assertThat(assertNotNull(OPERADOR.find(id)).getDescrip()).isEqualTo(descrip1);
        IxService.setDomain(domain2);
        assertThat(assertNotNull(OPERADOR.find(id)).getDescrip()).isEqualTo(descrip2);
    }

    /** Using Batch processing. */
    @Test public void testBatchProcessing() {
        IxService.startBatch();
        final OPERADOR operator = OPERADOR.create(12);
        operator.setActivo(true);
        operator.setAgentid(0);
        operator.setDescrip("Operador Dummy");
        operator.insert();

        operator.delete();

        IxService.endBatch();
    }

    @Test public void testIncOperation() {
        final OPERADOR operator = OPERADOR.create(12);
        operator.setActivo(true);
        operator.setAgentid(0);
        operator.setDescrip("Operador Dummy");
        operator.insert();

        operator.incr(OPERADOR_.AGENTID, 14);

        final OPERADOR opFind = OPERADOR.find(12);
        assertThat(opFind).isNotNull();
        assertThat(opFind.getAgentid()).isEqualTo(14);

        OPERADOR.create(12).incr(OPERADOR_.AGENTID, 1);

        final OPERADOR opCheckIncr = OPERADOR.find(12);
        assertThat(opCheckIncr).isNotNull();
        assertThat(opCheckIncr.getAgentid()).isEqualTo(15);
    }

    @Test public void testQueryByUnOrderedFieldIndex() {
        // Test un-ordered field's index
        final ImmutableList<MCatalogo> list = selectFrom(MCATALOGO).where(MCATALOGO.CODADM.eq("09876").and(MCATALOGO.CODFLIA.eq("1234"))).list();
        assertThat(list).isNotNull();
    }
    /** Query - List. */
    @Test public void testQueryList() {
        populateOperatorTable();

        n = 10;
        OPERADOR.forEach(op -> {
            assertThat(op.getCodigo()).isEqualTo(n);
            assertThat(op.getAgentid()).isEqualTo(1000 + n);
            n += 1;
        });

        final Set<Integer> keys = newLinkedHashSet(11, 13, 15, 17, 19);

        int n1 = 11;
        for (final OPERADOR op : OPERADOR.list(keys)) {
            assertThat(op.getCodigo()).isEqualTo(n1);
            assertThat(op.isActivo()).isTrue();
            n1 += 2;
        }

        assertThat(OPERADOR.list(keys)).hasSize(5);
    }

    @Test public void testQueryWithNoIndexFields() {
        final IxProps prop = Context.getEnvironment().get("myDomain", IxProps.class);
        prop.validateQueryIndexes = true;
        Context.getEnvironment().put("myDomain", prop);

        try {
            selectFrom(STOCK).where(STOCK.CODADM.eq("1234").and(STOCK.STKACT.eq(12)).or(STOCK.CODADM.eq("5678"))).list();
            fail("Exception Exception because query's fields are not part of the index");
        }
        catch (final UnsupportedOperationException e) {
            // Ok
        }

        try {
            selectFrom(STOCK).where(STOCK.CODADM.eq("1234").or(STOCK.STKACT.eq(12))).list();
            fail("Exception Exception because OR query's fields are not part of the index");
        }
        catch (final UnsupportedOperationException e) {
            // Ok
        }

        try {
            // query with gap in the selected field's index
            selectFrom(STOCK).where(STOCK.CODADM.eq("1234").and(STOCK.DEPOSITO.eq(1))).list();
        }
        catch (final UnsupportedOperationException e) {
            // Ok
        }
    }

    @Test public void testSimpleQuery() {
        populateOperatorTable();
        final ImmutableList<OPERADOR> list = OPERADOR.list().where(OPERADOR_.CODIGO.eq(10)).list();
        assertThat(list).isNotNull();
        assertThat(list).isNotEmpty();
    }

    private void populateOperatorTable() {
        for (int i = 10; i <= 20; i++) {
            final OPERADOR op = OPERADOR.create(i);
            op.setAgentid(1000 + i);
            op.setActivo(i % 2 == 1);
            op.setDescrip("Operator " + i);
            op.insert();
        }
    }

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class IxMockStoreHandlerTest
