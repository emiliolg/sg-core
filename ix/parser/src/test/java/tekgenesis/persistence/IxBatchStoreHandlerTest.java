
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Consumer;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.Times;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.json.JsonMapping;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.MuteRule;
import tekgenesis.common.tools.test.server.SgHttpServerRule;
import tekgenesis.ix.serv.CuentaCom;
import tekgenesis.ix.ventas.OPERADOR;
import tekgenesis.persistence.ix.IxBatchHandler;
import tekgenesis.persistence.ix.IxStoreHandleFactory;
import tekgenesis.persistence.ix.IxStoreHandler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import static tekgenesis.ix.serv.g.CuentaComTable.CUENTA_COM;

/**
 * Ix Batch Store handler Test.
 */
@RunWith(Parameterized.class)
@SuppressWarnings({ "DuplicateStringLiteralInspection", "MagicNumber", "JavaDoc" })
public class IxBatchStoreHandlerTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameterized.Parameter public String dbName = null;

    private final IxProps ixProp = new IxProps();
    @Rule public DbRule   db     = new DbRule() {
            @Override protected void before() {
                createDatabase(dbName);
                TableFactory.setFactory(new TableFactory(new IxStoreHandleFactory()));
                setProps();
            }
        };

    @Rule public MuteRule mute = new MuteRule(IxStoreHandler.class);

    private final String rootPath = new File("ix/parser/src/test/resources/ix/rest/").getAbsolutePath();

    //~ Methods ......................................................................................................................................

    @Test public void parallelBatch() {
        IxService.startBatch();
        assertThat(IxBatchHandler.current().size()).isEqualTo(0);

        final Thread threadA = executeInThread(p -> createSimpleBatch());
        final Thread threadB = executeInThread(p -> createSimpleBatch());

        assertThat(IxBatchHandler.current().size()).isEqualTo(0);
        IxService.abortBatch();
        assertThat(IxBatchHandler.isBatchEnabled()).isFalse();

        try {
            threadA.join(Times.MILLIS_SECOND);
            threadB.join(Times.MILLIS_SECOND);
        }
        catch (final InterruptedException e) {
            assertThat(IxBatchHandler.isBatchEnabled()).isFalse();
            fail(e.getMessage());
        }
        assertThat(IxBatchHandler.isBatchEnabled()).isFalse();
    }  // end method parallelBatch

    @Test public void testMultiSchemaPost() {
        mock.expectPost("/ixService/").withContent(fileToJson("batch.multi.json"));

        IxService.startBatch();

        final OPERADOR operator = OPERADOR.create(12);
        operator.setActivo(true);
        operator.setDescrip("Operador Dummy");
        operator.insert();

        final CuentaCom createB = CuentaCom.create(2686115, Integer.MAX_VALUE, 0);
        createB.setEstado(4);
        createB.setTipmed(2);
        createB.setValor("mariano@importadoradigital.com.ar");
        createB.setFecmod(DateOnly.fromString("2011-03-04"));
        createB.insert();

        createB.delete();

        final CuentaCom createC = CuentaCom.create(2686115, Integer.MAX_VALUE, 0);
        createC.setEstado(4);
        createC.setTipmed(2);
        createC.setValor("mariano@importadoradigital.com.ar");
        createC.setFecmod(DateOnly.fromString("2011-03-04"));

        createC.incr(CUENTA_COM.RENGLON, 10);

        IxService.endBatch();
    }

    @Test public void testPostAbort() {
        IxService.startBatch();

        final CuentaCom createA = CuentaCom.create(1575004, Integer.MAX_VALUE, 0);
        createA.setEstado(4);
        createA.setTipmed(2);
        createA.setValor("mariano@importadoradigital.com.ar");
        createA.setFecmod(DateOnly.fromString("2011-03-04"));
        createA.insert();

        IxService.abortBatch();
    }

    @SuppressWarnings("JavaDoc")
    @Test public void testSimplePost() {
        mock.expectPost("/ixService/").withContent(fileToJson("batch.serv.json"));

        IxService.startBatch();

        final CuentaCom createA = CuentaCom.create(1575004, Integer.MAX_VALUE, 0);
        createA.setEstado(4);
        createA.setTipmed(2);
        createA.setValor("mariano@importadoradigital.com.ar");
        createA.setFecmod(DateOnly.fromString("2011-03-04"));
        createA.insert();

        final CuentaCom createB = CuentaCom.create(2686115, Integer.MAX_VALUE, 0);
        createB.setEstado(4);
        createB.setTipmed(2);
        createB.setValor("mariano@importadoradigital.com.ar");
        createB.setFecmod(DateOnly.fromString("2011-03-04"));
        createB.insert();

        createB.delete();

        final CuentaCom create = CuentaCom.create(1575004, Integer.MAX_VALUE, 0);
        create.setEstado(4);
        create.setTipmed(2);
        create.setValor("mariano@importadoradigital.com.ar");
        create.setFecmod(DateOnly.fromString("2011-03-04"));
        create.insert();

        IxService.endBatch();
    }

    @SuppressWarnings("JavaDoc")
    @Test public void testSimpleWithAliasNamePost() {
        mock.expectPost("/ixService/").withContent(fileToJson("batch.serv.json"));

        IxService.startBatch();

        final CuentaCom createA = CuentaCom.create(1575004, Integer.MAX_VALUE, 0);
        createA.setEstado(4);
        createA.setTipmed(2);
        createA.setValor("mariano@importadoradigital.com.ar");
        createA.setFecmod(DateOnly.fromString("2011-03-04"));
        createA.insert();

        final CuentaCom createB = CuentaCom.create(2686115, Integer.MAX_VALUE, 0);
        createB.setEstado(4);
        createB.setTipmed(2);
        createB.setValor("mariano@importadoradigital.com.ar");
        createB.setFecmod(DateOnly.fromString("2011-03-04"));
        createB.insert();

        createB.delete();

        final CuentaCom create = CuentaCom.create(1575004, Integer.MAX_VALUE, 0);
        create.setEstado(4);
        create.setTipmed(2);
        create.setValor("mariano@importadoradigital.com.ar");
        create.setFecmod(DateOnly.fromString("2011-03-04"));
        create.insert();

        IxService.endBatch();
    }

    @Test(expected = OperationNotAllowedException.class)
    public void testTooMuchOperationBatchMode() {
        try {
            IxService.startBatch();

            for (int i = 1; i <= 10000; i++) {
                final OPERADOR operator = OPERADOR.create(12);
                operator.setActivo(true);
                operator.setDescrip("Operador Dummy");
                operator.insert();
            }
            IxService.abortBatch();
        }
        finally {
            if (IxService.isBatchEnabled()) IxService.abortBatch();
        }
    }

    private void createSimpleBatch() {
        IxService.startBatch();
        assertThat(IxBatchHandler.current().size()).isEqualTo(0);
        final OPERADOR operator = OPERADOR.create(12);
        operator.setActivo(true);
        operator.setDescrip("Operador Dummy");
        operator.insert();
        assertThat(IxBatchHandler.current().size()).isEqualTo(1);

        try {
            Thread.sleep(800);
            assertThat(IxBatchHandler.current().size()).isEqualTo(1);
        }
        catch (final InterruptedException e) {
            fail(e.getMessage());
        }
    }

    private Thread executeInThread(Consumer<Void> consumer) {
        final Thread thread = new Thread(() -> consumer.accept(null));
        thread.start();
        return thread;
    }

    /* Raw bytes read from file. */
    private Object fileToJson(final String fileName) {
        try {
            return JsonMapping.shared().readValue(new File(rootPath, fileName), Object.class);
        }
        catch (final IOException e) {
            fail("Exception", e);
            throw new UncheckedIOException(e);
        }
    }

    private void setProps() {
        ixProp.url          = "http://localhost:" + mock.getServerPort() + "/ixService";
        ixProp.pingTimeout  = 500;
        ixProp.pingInterval = 200;
        ixProp.mode         = IxProps.MODE.WRITE;
        Context.getEnvironment().put(ixProp);
        Context.getEnvironment().get("gw", IxProps.class);
    }

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    //~ Static Fields ................................................................................................................................

    @ClassRule public static SgHttpServerRule mock = SgHttpServerRule.httpServerRule().build();
}  // end class IxBatchStoreHandlerTest
