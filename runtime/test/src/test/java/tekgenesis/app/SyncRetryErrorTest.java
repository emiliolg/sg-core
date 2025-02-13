
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.app;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
import tekgenesis.common.tools.test.AppTests;
import tekgenesis.common.tools.test.ApplicationRule;
import tekgenesis.properties.SchemaProps;
import tekgenesis.sales.basic.City;
import tekgenesis.sales.basic.CityView;
import tekgenesis.sales.basic.Country;
import tekgenesis.sales.basic.StateProvince;
import tekgenesis.service.ServiceManager;
import tekgenesis.task.ScheduledTask;
import tekgenesis.task.TaskService;
import tekgenesis.task.TaskServiceProps;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.app.SyncTest.cleanRecords;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.sales.basic.g.CityViewTable.CITY_VIEW;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 */
@Category(AppTests.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber", "ConstantConditions" })
public class SyncRetryErrorTest {

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
            StateProvince.create("AR", "B").setName("Buenos Aires").insert();
            City.create().setName("Olivos").setStateProvince(StateProvince.create("AR", "B")).insert();
        });

        final Option<TaskService> service = Context.getSingleton(ServiceManager.class).getService(TaskService.class);
        if (service.isPresent()) service.get().submit(scheduledTask).get(1, TimeUnit.MINUTES);

        runInTransaction(() -> {
            final ImmutableList<CityView> cityViews = selectFrom(CITY_VIEW).list();
            assertThat(cityViews).isNotEmpty();

            final CityView cityView = cityViews.getFirst().get();
            assertThat(cityView.getName()).isEqualTo("Olivos");
            assertThat(cityView.getStateProvinceCode()).isEqualTo("B");
            assertThat(cityView.getStateProvinceCountryIso()).isEqualTo("AR");
        });
    }

    //~ Static Fields ................................................................................................................................

    @ClassRule public static ApplicationRule app = new ApplicationRule() {
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
                schemaProps.remoteUrl = "http://localhost:8228;invalid url;http://localhost:1;http://localhost:8080";
                Context.getEnvironment().put(schemaProps);
            }
        };
}  // end class SyncRetryErrorTest
