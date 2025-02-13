
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

import org.jboss.logging.MDC;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.MultiMap;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.command.exception.CommandStackCauseException;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.json.JsonMapping;
import tekgenesis.common.rest.exception.*;
import tekgenesis.common.service.Headers;
import tekgenesis.common.service.Status;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.MuteRule;
import tekgenesis.common.tools.test.server.HeadersAssertion;
import tekgenesis.common.tools.test.server.SgHttpServerRule;
import tekgenesis.common.util.Files;
import tekgenesis.ix.serv.CuentaCom;
import tekgenesis.ix.ventas.*;
import tekgenesis.ix.ventas.g.MCatalogoTable;
import tekgenesis.persistence.ix.IxSelect;
import tekgenesis.persistence.ix.IxStoreHandleFactory;
import tekgenesis.persistence.ix.IxStoreHandler;

import static java.util.Arrays.asList;

import static org.assertj.core.api.Assertions.*;

import static tekgenesis.common.collections.Maps.hashMap;
import static tekgenesis.common.core.Constants.LIFECYCLE_KEY;
import static tekgenesis.common.media.MediaType.APPLICATION_JSON;
import static tekgenesis.common.service.HeaderNames.*;
import static tekgenesis.common.service.Status.NOT_FOUND;
import static tekgenesis.common.service.Status.OK;
import static tekgenesis.ix.serv.g.CuentaComTable.CUENTA_COM;
import static tekgenesis.ix.ventas.g.MCatalogoTable.MCATALOGO;
import static tekgenesis.persistence.Criteria.*;
import static tekgenesis.persistence.IxStoreHandlerTest.TestCase.testCase;
import static tekgenesis.persistence.Sql.select;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.persistence.ix.IxSelect.newer;
import static tekgenesis.persistence.ix.IxSelect.pageSize;

/**
 * Ix Store handler Test.
 */
@RunWith(Parameterized.class)
@SuppressWarnings(
        { "DuplicateStringLiteralInspection", "MagicNumber", "JavaDoc", "SpellCheckingInspection", "AssignmentToStaticFieldFromInstanceMethod" }
                 )
public class IxStoreHandlerTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameterized.Parameter public String dbName = null;

    private final IxProps ixProp = new IxProps();
    @Rule public DbRule   db     = new DbRule() {
            @Override
            @SuppressWarnings("DuplicateStringLiteralInspection")
            protected void before() {
                createDatabase(dbName);
                TableFactory.setFactory(new TableFactory(new IxStoreHandleFactory()));
                setProps();
            }

            protected void after() {
                IxService.asImmutable(false);
                IxService.unsetDomain();
                if (IxService.isBatchEnabled()) IxService.abortBatch();
            }
        };

    @Rule public MuteRule mute = new MuteRule(IxStoreHandler.class);

    private final String rootPath = new File("ix/parser/src/test/resources/ix/rest/").getAbsolutePath();

    //~ Methods ......................................................................................................................................

    @After public void assertNoExpectationsLeft() {
        mock.assertNoLeftExpectations();
    }

    @Test public void testAvailable()
        throws InterruptedException
    {
        mock.expectHead().respondOk().withContentType(APPLICATION_JSON);

        assertThat(IxService.isAvailable()).isTrue();

        // Ping to an used port
        ixProp.url = "http://localhost:9999/ix";  // Unused port
        Context.getEnvironment().put(ixProp);

        assertThat(IxService.isAvailable()).isFalse();
    }

    @Test public void testCheckLock() {
        mock.expectGet("/ixService/ventas.CATALOGO/51857").respondRaw(OK, fileBytes("ventas.CATALOGO51857.json")).withContentType(APPLICATION_JSON);

        final MCatalogo cat = MCatalogo.find("51857");

        mock.expectGet("/ixService/ventas.CATALOGO/51857").withHeadersAssertion(new HeadersAssertion() {
                    @Override public void assertHeaders(@NotNull Headers expected, @NotNull Headers actual)
                        throws AssertionError
                    {
                        assertThat(actual.getOrEmpty(X_OPER_DEF)).startsWith("/ventas.CATALOGO/51857");
                    }
                }).respondOk().withContent(hashMap(Tuple.tuple("locked", Boolean.TRUE))).withContentType(APPLICATION_JSON);

        assertThat(cat).isNotNull();
        assertThat(cat.checkLock()).isTrue();
    }

    @Test public void testCheckMdcHeaderFromFormLifecycle() {
        final String uuid = UUID.randomUUID().toString();
        MDC.put(LIFECYCLE_KEY, uuid);
        assertMdcHeader(uuid);
    }

    @Test public void testCheckMdcHeaderFromTaskThread() {
        final String uuid = UUID.randomUUID().toString();
        MDC.put(LIFECYCLE_KEY, uuid);
        assertMdcHeader(uuid);
    }

    @Test public void testDelete() {
        mock.expectGet("/ixService/ventas.OPERADOR/12").respondRaw(OK, fileBytes("ventas.OPERADOR12.json")).withContentType(APPLICATION_JSON);
        final OPERADOR op = OPERADOR.find(12);

        mock.expectDelete("/ixService/ventas.OPERADOR/12").respondOk().withContentType(APPLICATION_JSON);
        assert op != null;
        assertThat(op).isNotNull();
        op.delete();
    }

    @Test public void testDeleteExceptions() {
        final List<TestCase> cases = asList(testCase(400, 1, BadRequestRestException.class),
                testCase(404, 2, NotFoundRestException.class),
                testCase(423, 3, LockedRestException.class),
                testCase(500, 4, InternalServerErrorRestException.class),
                testCase(502, 5, BadGatewayRestException.class),
                testCase(503, 6, ServiceUnavailableRestException.class),
                testCase(504, 7, GatewayTimeoutRestException.class));

        for (final TestCase c : cases) {
            try {
                mock.expectDelete("/ixService/ventas.OPERADOR/" + c.code).respond(Status.fromCode(c.errorCode)).withContentType(APPLICATION_JSON);

                OPERADOR.create(c.code).delete();
                failBecauseExceptionWasNotThrown(c.exception);
            }
            catch (final Exception exception) {
                assertThat(exception).isExactlyInstanceOf(c.exception);
                assertThat(exception).hasCauseExactlyInstanceOf(CommandStackCauseException.class);
            }
        }
    }

    @Test public void testFind() {
        mock.expectGet("/ixService/ventas.CATALOGO/51857").respondRaw(OK, fileBytes("ventas.CATALOGO51857.json")).withContentType(APPLICATION_JSON);

        final MCatalogo cat = MCatalogo.find("51857");

        assertThat(cat).isNotNull();

        assertThat(cat.getCodflia()).isEqualTo("0490021");

        assertThat(cat.getTgarfab()).isEqualTo(12);
        assertThat(cat.getCdate()).isEqualTo(DateOnly.fromString("2009-04-06"));
        assertThat(cat.getPrecio2()).isEqualTo(BigDecimal.valueOf(288.43000000000001));
    }

    @Test public void testFindExceptions() {
        final List<TestCase> cases = asList(testCase(400, 1, BadRequestRestException.class),
                testCase(423, 3, LockedRestException.class),
                testCase(500, 4, InternalServerErrorRestException.class),
                testCase(502, 5, BadGatewayRestException.class),
                testCase(503, 6, ServiceUnavailableRestException.class),
                testCase(504, 7, GatewayTimeoutRestException.class));

        for (final TestCase c : cases) {
            try {
                mock.expectGet("/ixService/ventas.CATALOGO/" + c.code).respond(Status.fromCode(c.errorCode)).withContentType(APPLICATION_JSON);
                MCatalogo.find(String.valueOf(c.code));
                failBecauseExceptionWasNotThrown(c.exception);
            }
            catch (final Exception exception) {
                assertThat(exception).isExactlyInstanceOf(c.exception);
                assertThat(exception).hasCauseExactlyInstanceOf(CommandStackCauseException.class);
            }
        }

        mock.expectGet("/ixService/ventas.CATALOGO/2").respond(NOT_FOUND).withContentType(APPLICATION_JSON);
        assertThat(MCatalogo.find("2")).isNull();  // 404
    }

    @Test public void testGetWithIntPks() {
        mock.expectGet("/ixService/serv.CTAMCOM/")
            .withParameter("codcta", "1575004")
            .respondRaw(OK, fileBytes("serv.CTAMCOM-1575004Query.json"))
            .withContentType(APPLICATION_JSON);

        final ImmutableList<CuentaCom> list = CuentaCom.list().where(CUENTA_COM.CODCTA.eq(1575004)).list();

        assertThat(list).isNotNull();

        assertThat(list.toList().size()).isEqualTo(1);

        final Option<CuentaCom> first1 = list.getFirst();

        assertThat(first1.isPresent()).isTrue();

        final CuentaCom CuentaCom = first1.get();

        assertThat(CuentaCom.getCoddir()).isEqualTo(Integer.MAX_VALUE);
    }

    public void testImmutableDomain() {
        assertThat(IxService.isImmutable()).isFalse();
        IxService.setDomain("G");
        IxService.setDomain("C");
    }

    @Test(expected = IllegalStateException.class)
    public void testImmutableDomainException() {
        assertThat(IxService.isImmutable()).isFalse();
        IxService.setDomain("G");
        IxService.asImmutable(true);
        assertThat(IxService.isImmutable()).isTrue();
        IxService.setDomain("C");
    }

    public void testImmutableSameDomain() {
        assertThat(IxService.isImmutable()).isFalse();
        IxService.setDomain("G");
        IxService.asImmutable(true);
        IxService.setDomain("G");
    }

    @Test public void testIncOperation() {
        mock.expectGet("/ixService/ventas.CATALOGO/").respondRaw(OK, fileBytes("ventas.CATALOGO.json")).withContentType(APPLICATION_JSON);

        final ImmutableList<MCatalogo> list = MCatalogo.list().list();

        final Option<MCatalogo> option = list.getFirst();

        assertThat(option.isPresent()).isTrue();

        final MCatalogo catalogo = option.get();

        mock.expectPost("/ixService/ventas.CATALOGO/").withHeadersAssertion(new HeadersAssertion() {
                    @Override public void assertHeaders(@NotNull Headers expected, @NotNull Headers actual)
                        throws AssertionError
                    {
                        assertThat(actual.getOrEmpty(X_OPER_DEF)).startsWith("precio1,-10.23;mdate");
                    }
                }).respondOk().withContent(EMPTY_JSON_ARRAY).withContentType(APPLICATION_JSON);

        catalogo.incr(MCATALOGO.PRECIO1, -10.23);

        mock.expectPost("/ixService/ventas.CATALOGO/").withHeadersAssertion(new HeadersAssertion() {
                    @Override public void assertHeaders(@NotNull Headers expected, @NotNull Headers actual)
                        throws AssertionError
                    {
                        assertThat(actual.getOrEmpty(X_OPER_DEF)).startsWith("precio1,10.0;mdate");
                    }
                }).respondOk().withContent(EMPTY_JSON_ARRAY).withContentType(APPLICATION_JSON);

        final MCatalogo newCatalogo = MCatalogo.create("123456");
        newCatalogo.incr(MCATALOGO.PRECIO1, 10);
    }  // end method testIncOperation

    @Test public void testInserInttWithKeys() {
        mock.expectPost("/ixService/serv.CTAMCOM/")
            .withContent(fileBytes("serv.CTAMCOM-1575004.json"))
            .respondOk()
            .withContent(EMPTY_JSON_ARRAY)
            .withContentType(APPLICATION_JSON)
            .withHeader(LOCATION, serverUrl + "/ixService/serv.CTAMCOM/1575004:null:0");

        final CuentaCom create = CuentaCom.create(1575004, Integer.MAX_VALUE, 0);
        create.setEstado(4);
        create.setTipmed(2);
        create.setValor("mariano@importadoradigital.com.ar");
        create.setFecmod(DateOnly.fromString("2011-03-04"));
        create.insert();
    }

    @Test public void testInsert() {
        mock.expectPost("/ixService/ventas.OPERADOR/")
            .withContent(fileBytes("ventas.OPERADOR12.json"))
            .respondOk()
            .withContentType(APPLICATION_JSON)
            .withHeader(LOCATION, serverUrl + "/ixService/ventas.OPERADOR/12");

        final OPERADOR operator = OPERADOR.create(12);
        operator.setActivo(true);
        operator.setDescrip("Operador Dummy");
        operator.insert();
    }

    @Test public void testInsertExceptions() {
        final List<TestCase> cases = asList(testCase(400, 1, BadRequestRestException.class),
                testCase(404, 2, NotFoundRestException.class),
                testCase(423, 3, LockedRestException.class),
                testCase(500, 4, InternalServerErrorRestException.class),
                testCase(502, 5, BadGatewayRestException.class),
                testCase(503, 6, ServiceUnavailableRestException.class),
                testCase(504, 7, GatewayTimeoutRestException.class));

        for (final TestCase c : cases) {
            try {
                mock.expectPost("/ixService/ventas.OPERADOR/").respond(c.getStatus()).withContentType(APPLICATION_JSON);
                OPERADOR.create(c.code).insert();
                failBecauseExceptionWasNotThrown(c.exception);
            }
            catch (final Exception exception) {
                assertThat(exception).isExactlyInstanceOf(c.exception);
                assertThat(exception).hasCauseExactlyInstanceOf(CommandStackCauseException.class);
            }
        }
    }

    @Test public void testInsertFull() {
        mock.expectPost("/ixService/ventas.BAJAS/")
            .withContent(fileBytes("ventas.BAJAS12.json"))
            .respondOk()
            .withContentType(APPLICATION_JSON)
            .withHeader(LOCATION, serverUrl + "/ixService/ventas.BAJAS/12");

        mock.expectDelete("/ixService/ventas.BAJAS/1%3A2%3A3%3A51857%3Anull").respondOk().withContentType(APPLICATION_JSON);

        final BAJAS bajas = BAJAS.create(1, 2, 3, "51857", 2);
        bajas.setFecultmod(DateOnly.date(1981, 5, 12));
        bajas.setMdate(DateOnly.date(2013, 1, 28));
        bajas.setMtime(1000);
        bajas.insert();

        final BAJAS bajasNull = BAJAS.create(1, 2, 3, "51857", Integer.MAX_VALUE);
        bajasNull.delete();
    }

    @Test public void testNewerDefaultFields() {
        mock.expectGet("/ixService/ventas.CATALOGO/")
            .withHeader(X_NEWER, "mdate=2014-01-12,mtime=45611")
            .respondRaw(OK, fileBytes("ventas.CATALOGO-page1.json"))
            .withContentType(APPLICATION_JSON);

        final ImmutableList<MCatalogo> list = newer(MCatalogo.list(), DateTime.dateTime(2014, 1, 12, 12, 40, 11)).list();
        assertThat(list.toList().size()).isEqualTo(3);
    }

    @Test public void testNewerNoDefaultFields() {
        mock.expectGet("/ixService/ventas.STOCK/")
            .withHeader(X_NEWER, "fecult=2014-01-12,mtime=45611")
            .respondOk()
            .withContent(EMPTY_JSON_ARRAY)
            .withContentType(APPLICATION_JSON);

        final IxSelect<Stock> list = newer(Stock.list(), DateTime.dateTime(2014, 1, 12, 12, 40, 11));

        assertThat(list.count()).isEqualTo(0);
    }

    @Test public void testNewerOnlyDate() {
        mock.expectGet("/ixService/ventas.CODIFICA/")
            .withHeader(X_NEWER, "mtime=2014-01-12")
            .respondOk()
            .withContent(EMPTY_JSON_ARRAY)
            .withContentType(APPLICATION_JSON);

        final IxSelect<CODIFICA> list = newer(CODIFICA.list(), DateTime.dateTime(2014, 1, 12, 12, 40, 11));

        assertThat(list.count()).isEqualTo(0);
    }

    @Test public void testQueryByUnOrderedFieldIndex() {
        final MultiMap<String, String> codFliaQueryParam = MultiMap.createMultiMap();
        codFliaQueryParam.put("codadm", "09876");
        codFliaQueryParam.put("codflia", "1234");

        mock.expectGet("/ixService/ventas.CATALOGO/")
            .withParameters(codFliaQueryParam)
            .respondRaw(OK, fileBytes("ventas.CATALOGO-where.json"))
            .withContentType(APPLICATION_JSON);
        // Test un-ordered field's index
        selectFrom(MCATALOGO).where(MCATALOGO.CODADM.eq("09876").and(MCATALOGO.CODFLIA.eq("1234"))).list();
    }

    @Test public void testQueryCriteria() {
        mock.expectGet("/ixService/ventas.CATALOGO/").repeated(3).respondRaw(OK, fileBytes("ventas.CATALOGO.json")).withContentType(APPLICATION_JSON);
        mock.expectGet("/ixService/ventas.CATALOGO/")
            .withParameter("codadm", "2345")
            .repeated(3)
            .respondRaw(OK, fileBytes("ventas.CATALOGO.json"))
            .withContentType(APPLICATION_JSON);

        mock.expectGet("/ixService/ventas.CATALOGO/").repeated(2).respondRaw(OK, fileBytes("ventas.CATALOGO.json")).withContentType(APPLICATION_JSON);

        selectFrom(MCATALOGO).where(EMPTY).list();
        selectFrom(MCATALOGO).where(FALSE).list();
        selectFrom(MCATALOGO).where(TRUE).list();

        selectFrom(MCATALOGO).where(FALSE.or(MCATALOGO.CODADM.eq("2345"))).list();
        selectFrom(MCATALOGO).where(TRUE.and(MCATALOGO.CODADM.eq("2345"))).list();
        selectFrom(MCATALOGO).where(EMPTY.or(MCATALOGO.CODADM.eq("2345"))).list();

        // This expression does not evaluate the right part because
        // - true or something is always true
        // - false and somethinh is always false
        selectFrom(MCATALOGO).where(TRUE.or(MCATALOGO.CODADM.eq("2345"))).list();
        selectFrom(MCATALOGO).where(FALSE.and(MCATALOGO.CODADM.eq("2345"))).list();
    }

    @Test public void testQueryFields() {
        mock.expectGet("/ixService/ventas.CATALOGO/")
            .withHeader(X_FIELDS, "codadm,des1adm")
            .respondRaw(OK, fileBytes("ventas.CATALOGO.json"))
            .withContentType(APPLICATION_JSON);

        assertThat(select(MCATALOGO.CODADM, MCATALOGO.DES1ADM).from(MCATALOGO).list().toStrings())  //
        .containsExactly("codadm=50000,des1adm=MOD COMP. INSP 530S CPU",
            "codadm=50001,des1adm=LCD 22\" DELL",
            "codadm=50002,des1adm=MOD LCD 20\" DELL",
            "codadm=50003,des1adm=COMP XPS 420 CPU",
            "codadm=50004,des1adm=MOVI NOKIA 5310 RED CATER",
            "codadm=50005,des1adm=MONTACARGAS F VARELA");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testQueryLimit() {
        assertThat(MCatalogo.list().limit(3).count()).isEqualTo(3);
    }

    @Test public void testQueryList() {
        mock.expectGet("/ixService/ventas.CATALOGO/").repeated(2).respondRaw(OK, fileBytes("ventas.CATALOGO.json")).withContentType(APPLICATION_JSON);

        final ImmutableList<MCatalogo> list = MCatalogo.list().list();

        for (final MCatalogo MCatalogo : list) {
            assertThat(MCatalogo.getCodadm()).isNotNull();
            assertThat(MCatalogo.getCodflia()).isNotNull();
            assertThat(MCatalogo.getCdate()).isNotNull();
            assertThat(MCatalogo.getPrecio2()).isNotNull();
        }

        assertThat(MCatalogo.list().count()).isEqualTo(6);
    }

    @Test public void testQueryPageSize() {
        mock.expectGet("/ixService/ventas.CATALOGO/").repeated(3).respondOk().withRawContent(req -> {
                final Option<String> offsetOpt = req.getHeaders().getFirst(X_OFFSET);
                if (offsetOpt.isEmpty()) {
                    req.getHeaders().put(X_OFFSET, "3");
                    return fileBytes("ventas.CATALOGO-page1.json");
                }
                final String offset = offsetOpt.get();
                if (offset.contains("3")) {
                    req.getHeaders().set(X_OFFSET, "6");
                    return fileBytes("ventas.CATALOGO-page2.json");
                }
                return EMPTY_JSON_ARRAY_BYTES;
            }).withContentType(APPLICATION_JSON);

        final Select<MCatalogo> list = pageSize(MCatalogo.list(), 3);
        assertThat(list.count()).isEqualTo(6);
    }

    @Test public void testQueryWhere() {
        final MultiMap<String, String> parameters = MultiMap.createMultiMap();
        parameters.put("codflia", "0490021");
        parameters.put("provunico", "12||codadm=12345");

        mock.expectGet("/ixService/ventas.CATALOGO/")
            .withParameters(parameters)
            .respondRaw(OK, fileBytes("ventas.CATALOGO-where.json"))
            .withContentType(APPLICATION_JSON);

        final MCatalogoTable M = MCATALOGO;

        final ImmutableList<MCatalogo> list = pageSize(
                    MCatalogo.list()                     //
                    .where(M.CODFLIA.eq("0490021")       //
                                 .and(M.PROVUNICO.eq(12))  // //
                                 .or(M.CODADM.eq("12345"))),  //
                    1).list();

        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(1);

        final MCatalogo item = list.iterator().next();

        assertThat(item.getCodflia()).isEqualTo("0490021");
        assertThat(item.getTgarfab()).isEqualTo(12);
        assertThat(item.getProvunico()).isEqualTo(961);
        assertThat(item.getFcostorep()).isEqualTo(DateOnly.date(2010, 1, 15));
    }

    @Test public void testQueryWithNoIndexFields() {
        final IxProps prop = Context.getEnvironment().get(IxProps.class);
        prop.validateQueryIndexes = true;
        Context.getEnvironment().put(prop);

        try {
            selectFrom(MCATALOGO).where(MCATALOGO.CATEG.eq(1).and(MCATALOGO.ESTACT.eq(false)).or(MCATALOGO.ASOCIA.eq(true))).list();
            fail("Exception Exception because query's fields are not part of the index");
        }
        catch (final UnsupportedOperationException e) {
            // Ok
        }

        try {
            selectFrom(MCATALOGO).where(MCATALOGO.CODADM.eq("1234").or(MCATALOGO.ASOCIA.eq(true))).list();
            fail("Exception Exception because OR query's fields are not part of the index");
        }
        catch (final UnsupportedOperationException e) {
            // Ok
        }

        try {
            // query with gap in the selected field's index
            selectFrom(MCATALOGO).where(MCATALOGO.CODFLIA.eq("1234").and(MCATALOGO.DES1ADM.eq("abcd"))).list();
        }
        catch (final UnsupportedOperationException e) {
            // Ok
        }
    }

    @Test public void testQueryWithoutResults() {
        mock.expectGet("/ixService/ventas.CATALOGO/").repeated(2).respondOk().withContent(EMPTY_JSON_ARRAY).withContentType(APPLICATION_JSON);
        MCatalogo.list().list();
        assertThat(MCatalogo.list().count()).isEqualTo(0);
    }

    @Test public void testSubdomainService() {
        mock.expectGet("/myDomain/ixService/ventas.CATALOGO/51857")
            .respondRaw(OK, fileBytes("ventas.CATALOGO51857.json"))
            .withContentType(APPLICATION_JSON);

        IxService.setDomain("myDomain");

        final MCatalogo cat = MCatalogo.find("51857");

        assertThat(cat).isNotNull();

        assertThat(cat.getCodflia()).isEqualTo("0490021");
        assertThat(cat.getTgarfab()).isEqualTo(12);
        assertThat(cat.getCdate()).isEqualTo(DateOnly.fromString("2009-04-06"));
        assertThat(cat.getPrecio2()).isEqualTo(BigDecimal.valueOf(288.43000000000001));

        final BAJAS bajas = BAJAS.create(1, 2, 3, "51857", 2);
        bajas.setFecultmod(DateOnly.date(1981, 5, 12));
        bajas.setMdate(DateOnly.date(2013, 1, 28));
        bajas.setMtime(1000);

        // try {
        // bajas.insert();
        // failBecauseExceptionWasNotThrown(OperationNotAllowedException.class);
        // }
        // catch (final OperationNotAllowedException ignore) {}
    }  // end method testSubdomainService

    private void assertMdcHeader(String uuid) {
        mock.expectGet("/ixService/ventas.OPERADOR/12")
            .withHeader(X_MDC_UUID, uuid)
            .respondRaw(OK, fileBytes("ventas.OPERADOR12.json"))
            .withContentType(APPLICATION_JSON);
        final OPERADOR op = OPERADOR.find(12);

        assert op != null;
        assertThat(op).isNotNull();

        mock.expectPost("/ixService/ventas.OPERADOR/").withHeader(X_MDC_UUID, uuid).respond(OK).withContentType(APPLICATION_JSON);
        op.insert();

        mock.expectDelete("/ixService/ventas.OPERADOR/12").withHeader(X_MDC_UUID, uuid).respondOk().withContentType(APPLICATION_JSON);
        op.delete();

        mock.expectGet("/ixService/serv.CTAMCOM/")
            .withHeader(X_MDC_UUID, uuid)
            .withParameter("codcta", "1575004")
            .respondRaw(OK, fileBytes("serv.CTAMCOM-1575004Query.json"))
            .withContentType(APPLICATION_JSON);

        CuentaCom.list().where(CUENTA_COM.CODCTA.eq(1575004)).list();

        mock.expectPost("/ixService/ventas.CATALOGO/").withHeader(X_MDC_UUID, uuid).withHeadersAssertion(new HeadersAssertion() {
                    @Override public void assertHeaders(@NotNull Headers expected, @NotNull Headers actual)
                        throws AssertionError
                    {
                        assertThat(actual.getOrEmpty(X_OPER_DEF)).startsWith("precio1,10.0;mdate");
                    }
                }).respondOk().withContent(EMPTY_JSON_ARRAY).withContentType(APPLICATION_JSON);

        final MCatalogo newCatalogo = MCatalogo.create("123456");
        newCatalogo.incr(MCATALOGO.PRECIO1, 10);
    }

    /* Raw bytes read from file. */
    @NotNull private byte[] fileBytes(final String fileName) {
        try {
            return Files.toByteArray(new FileInputStream(new File(rootPath, fileName)));
        }
        catch (final FileNotFoundException e) {
            fail("FileNotFoundException", e);
        }
        return EMPTY_BYTES;
    }

    private void setProps() {
        ixProp.url          = "http://localhost:" + mock.getServerPort() + "/ixService";
        ixProp.pingTimeout  = 500;
        ixProp.pingInterval = 20000;
        ixProp.mode         = IxProps.MODE.WRITE;
        Context.getEnvironment().put(ixProp);
        Context.getEnvironment().get("gw", IxProps.class);

        final IxProps mydomainProp = new IxProps();
        mydomainProp.url  = "http://localhost:" + mock.getServerPort() + "/myDomain/ixService";
        mydomainProp.mode = IxProps.MODE.READ;
        Context.getEnvironment().put("myDomain", mydomainProp);
    }

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    //~ Static Fields ................................................................................................................................

    private static final byte[] EMPTY_BYTES = new byte[0];

    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");
    private static String       serverUrl    = null;

    @ClassRule public static SgHttpServerRule mock = SgHttpServerRule.httpServerRule().onStart(sgHttpServerRule -> {
                if (sgHttpServerRule != null) serverUrl = "http://localhost:" + sgHttpServerRule.getServerPort() + "/ixService";
            }).build();

    private static final JsonNode EMPTY_JSON_ARRAY       = JsonMapping.shared().createArrayNode();
    private static final byte[]   EMPTY_JSON_ARRAY_BYTES = JsonMapping.shared().createArrayNode().toString().getBytes();

    //~ Inner Classes ................................................................................................................................

    static class TestCase {
        int                        code;
        int                        errorCode;
        Class<? extends Throwable> exception;

        private TestCase(final int errorCode, final int code, final Class<? extends Throwable> exception) {
            this.errorCode = errorCode;
            this.code      = code;
            this.exception = exception;
        }

        private Status getStatus() {
            return Status.fromCode(errorCode);
        }

        static TestCase testCase(int err, int c, Class<? extends Throwable> ex) {
            return new TestCase(err, c, ex);
        }
    }
}  // end class IxStoreHandlerTest
