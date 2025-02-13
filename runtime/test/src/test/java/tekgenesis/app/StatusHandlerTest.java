
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.app;

import java.io.IOException;
import java.util.Map;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import tekgenesis.admin.HealtchCheckHandler;
import tekgenesis.admin.StatusHandler;
import tekgenesis.authorization.shiro.ShiroProps;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.invoker.CallResource;
import tekgenesis.common.invoker.HttpInvokerResult;
import tekgenesis.common.invoker.HttpInvokers;
import tekgenesis.common.invoker.InvokerCommand;
import tekgenesis.common.service.Status;
import tekgenesis.common.tools.test.AppTests;
import tekgenesis.common.tools.test.ApplicationRule;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.common.Predefined.cast;

/**
 * Test Health Check.
 */
@Category(AppTests.class)
@SuppressWarnings("DuplicateStringLiteralInspection")
public class StatusHandlerTest {

    //~ Methods ......................................................................................................................................

    @Test public void testHealth() {
        TestStatusService.setOk(false);
        final CallResource<?> resource = HttpInvokers.invoker(app.getServerUrl()).resource(HealtchCheckHandler.Routes.healthCheck());
        assertThat(resource.invoke().getStatus()).isEqualTo(Status.SERVICE_UNAVAILABLE);
        TestStatusService.setOk(true);
        assertThat(resource.invoke().getStatus()).isEqualTo(Status.OK);
    }

    @Test public void testStatus()
        throws IOException
    {
        final CallResource<?>           resource = HttpInvokers.invoker(app.getServerUrl()).resource(StatusHandler.Routes.statusJson());
        final InvokerCommand<Object>    invoke   = resource.invoke(Object.class);
        final HttpInvokerResult<Object> result   = invoke.execute();
        assertThat(result.getStatus()).isEqualTo(Status.OK);

        final Map<String, ?> map = cast(result.get());
        assertThat(map).containsKey(TestStatusService.TEST_STATUS);
        final Map<String, String> testStatus = cast(map.get(TestStatusService.TEST_STATUS));
        assertThat(testStatus).containsEntry("version", "1.0");
    }

    //~ Static Fields ................................................................................................................................

    @ClassRule public static ApplicationRule app = new ApplicationRule() {
            @Override protected void before()
                throws Exception
            {
                super.before();
                final ShiroProps shiroProps = new ShiroProps();
                shiroProps.autoLogin = "admin:password";
                Context.getEnvironment().put(shiroProps);
            }
        };
}  // end class StatusHandlerTest
