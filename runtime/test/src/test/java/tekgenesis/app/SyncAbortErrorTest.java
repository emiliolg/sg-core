
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.app;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jetbrains.annotations.NotNull;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TestRule;
import org.junit.runners.model.Statement;

import tekgenesis.app.properties.ApplicationProps;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Option;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.env.logging.LogConfig;
import tekgenesis.common.service.Status;
import tekgenesis.common.tools.test.AppTests;
import tekgenesis.common.tools.test.ApplicationRule;
import tekgenesis.common.tools.test.server.SgHttpServerRule;
import tekgenesis.properties.SchemaProps;
import tekgenesis.sales.basic.Country;
import tekgenesis.sales.basic.CountryView;
import tekgenesis.service.ServiceManager;
import tekgenesis.task.ScheduledTask;
import tekgenesis.task.TaskService;
import tekgenesis.task.TaskServiceProps;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.app.SyncTest.cleanRecords;
import static tekgenesis.common.tools.test.server.SgHttpServerRule.httpServerRule;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.sales.basic.g.CountryViewTable.COUNTRY_VIEW;
import static tekgenesis.task.ViewRefresher.FIELD_SEPARATOR;
import static tekgenesis.task.ViewRefresher.MORE;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 */
@Category(AppTests.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber", "ConstantConditions" })
public class SyncAbortErrorTest {

    //~ Instance Fields ..............................................................................................................................

    @Rule public TestRule clean = (statement, description) ->
                                  new Statement() {
                @Override public void evaluate()
                    throws Throwable
                {
                    cleanRecords();
                    statement.evaluate();
                }
            };

    //~ Methods ......................................................................................................................................

    @Test public void syncCountries()
        throws ExecutionException, InterruptedException, TimeoutException
    {
        final ScheduledTask scheduledTask = ScheduledTask.createFromFqn("tekgenesis.sales.basic.TekgenesisSalesBasicRefreshRemoteViewTask").get();

        runInTransaction(() -> {
            Country.create("AR").setName("Argentina").insert();
            Country.create("UY").setName("Uruguay").insert();
            Country.create("BR").setName("Brasil").insert();
        });

        final HashMap<String, Object> emptyContent = emptyContent();
        final HashMap<String, Object> content      = invokeInTransaction(() -> countriesContent(false, Country.list().toArray(Country[]::new)));
        server.expectGet().keep().respond(Status.OK, emptyContent);
        server.expectGet("/sg/sync/tekgenesis.sales.basic.Country").respond(Status.OK, content);
        final Option<TaskService> service = Context.getSingleton(ServiceManager.class).getService(TaskService.class);
        if (service.isPresent()) service.get().submit(scheduledTask).get(1, TimeUnit.MINUTES);
        final ImmutableList<CountryView> countries = invokeInTransaction(() -> selectFrom(COUNTRY_VIEW).list());
        assertThat(countries.size()).isEqualTo(3);

        server.expectGet("/sg/sync/tekgenesis.sales.basic.Country").respond(Status.OK, countriesContent(true, invokeInTransaction(() ->
                        Country.find("AR"))));
        server.expectGet("/sg/sync/tekgenesis.sales.basic.Country").respond(Status.OK, countriesContent(true, invokeInTransaction(() ->
                        Country.find("AR"))));
        server.expectGet("/sg/sync/tekgenesis.sales.basic.Country").respond(Status.GATEWAY_TIMEOUT);
        if (service.isPresent()) service.get().submit(scheduledTask).get(1, TimeUnit.MINUTES);

        runInTransaction(() -> {
            final ImmutableList<CountryView> countries2 = selectFrom(COUNTRY_VIEW).list();
            assertThat(countries2.size()).isEqualTo(3);
        });
    }

    @Test public void syncCountriesPartial()
        throws ExecutionException, InterruptedException, TimeoutException
    {
        final ScheduledTask scheduledTask = ScheduledTask.createFromFqn("tekgenesis.sales.basic.TekgenesisSalesBasicRefreshRemoteViewTask").get();

        runInTransaction(() -> {
            Country.create("AR").setName("Argentina").insert();
            Country.create("UY").setName("Uruguay").insert();
            Country.create("BR").setName("Brasil").insert();
        });

        final HashMap<String, Object> emptyContent = emptyContent();
        final HashMap<String, Object> content      = invokeInTransaction(() ->
                    countriesContent(false, Country.list().toList().toArray(new Country[3])));
        server.expectGet().keep().respond(Status.OK, emptyContent);
        server.expectGet("/sg/sync/tekgenesis.sales.basic.Country").respond(Status.OK, content);
        final Option<TaskService> service = Context.getSingleton(ServiceManager.class).getService(TaskService.class);
        if (service.isPresent()) service.get().submit(scheduledTask).get(1, TimeUnit.MINUTES);
        final ImmutableList<CountryView> countries = invokeInTransaction(() -> selectFrom(COUNTRY_VIEW).list());
        assertThat(countries.size()).isEqualTo(3);

        runInTransaction(() -> {
            Country.create("CL").setName("Chile").insert();
            Country.create("CO").setName("Colombia").insert();
        });

        runInTransaction(() -> {
            server.expectGet("/sg/sync/tekgenesis.sales.basic.Country").respond(Status.OK, countriesContent(true, Country.find("CL")));
            server.expectGet("/sg/sync/tekgenesis.sales.basic.Country").respond(Status.GATEWAY_TIMEOUT);
        });
        if (service.isPresent()) service.get().submit(scheduledTask).get(1, TimeUnit.MINUTES);

        runInTransaction(() -> {
            final ImmutableList<CountryView> countries2 = selectFrom(COUNTRY_VIEW).list();
            assertThat(countries2.size()).isEqualTo(4);
        });
    }

    @NotNull private HashMap<String, Object> countriesContent(boolean more, Country... coutries) {
        final HashMap<String, Object> content = new HashMap<>();
        content.put(MORE, more);
        for (final Country country : coutries)

            content.put(country.getIso2(),
                "iso2=" + country.getIso2() + FIELD_SEPARATOR + "name=" + country.getName() + FIELD_SEPARATOR + "updateTime=" +
                country.getUpdateTime());
        return content;
    }

    @NotNull private HashMap<String, Object> emptyContent() {
        final HashMap<String, Object> emptyContent = new HashMap<>();
        emptyContent.put(MORE, false);
        return emptyContent;
    }

    //~ Static Fields ................................................................................................................................

    @ClassRule public static final SgHttpServerRule server = httpServerRule().unordered().onStart(s -> LogConfig.start()).build();
    @ClassRule public static ApplicationRule        app    = new ApplicationRule() {
            @Override protected void configureTaskProperties(TaskServiceProps props) {
                props.enabled = true;
            }

            @Override protected void configureAppProperties(final ApplicationProps prop) {
                super.configureAppProperties(prop);
                prop.syncEnabled = true;
            }

            @Override protected void before()
                throws Exception
            {
                super.before();
                final SchemaProps schemaProps = new SchemaProps();
                schemaProps.remoteTimeout = 1;
                schemaProps.remoteUrl     = server.getServerAddress();
                Context.getEnvironment().put(schemaProps);
            }
        };
}  // end class SyncAbortErrorTest
