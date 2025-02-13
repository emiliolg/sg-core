
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.admin.test;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import tekgenesis.admin.AdminDevHandler;
import tekgenesis.admin.AdminHandlerBase;
import tekgenesis.admin.SwaggerHandlerBase;
import tekgenesis.authorization.shiro.ShiroProps;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.invoker.HttpInvoker;
import tekgenesis.common.invoker.InvokerCommand;
import tekgenesis.common.invoker.exception.InvokerInvocationException;
import tekgenesis.common.tools.test.AppTests;
import tekgenesis.common.tools.test.ApplicationRule;

import static tekgenesis.common.invoker.HttpInvokers.invoker;

/**
 * Test All Handlers in admin module.
 */
@Category(AppTests.class)
public class HandlerTest {

    //~ Methods ......................................................................................................................................

    @Test public void testAdminClearCache() {
        final HttpInvoker httpInvoker = invoker(app.getServerUrl());

        final InvokerCommand<?> resource = httpInvoker.resource(AdminHandlerBase.Routes.clearCache())
                                           .param("fqn", "tekgenesis.authorization.Application")
                                           .invoke();

        resource.execute();
    }

    @Test public void testAdminClearUserCache() {
        final HttpInvoker httpInvoker = invoker(app.getServerUrl());

        final InvokerCommand<?> resource = httpInvoker.resource(AdminHandlerBase.Routes.clearUserCache()).param("name", "NodeConfig").invoke();
        resource.execute();
    }

    @Test public void testAdminRebuildIndex() {
        final HttpInvoker httpInvoker = invoker(app.getServerUrl());

        final InvokerCommand<?> resource = httpInvoker.resource(AdminHandlerBase.Routes.rebuildIndex())
                                           .param("fqn", "tekgenesis.authorization.Application")
                                           .invoke();

        resource.execute();
    }

    /** InvokerInvocationException BAD REQUEST is expected because tests are run in dev mode. */
    @Test(expected = InvokerInvocationException.class)
    public void testAdminRefresh() {
        final HttpInvoker httpInvoker = invoker(app.getServerUrl());

        final InvokerCommand<?> resource = httpInvoker.resource(AdminDevHandler.Routes.refresh()).invoke();

        resource.execute();
    }

    @Test public void testSwagger() {
        final HttpInvoker httpInvoker = invoker(app.getServerUrl());

        final InvokerCommand<?> resource = httpInvoker.resource(SwaggerHandlerBase.Routes.swagger()).invoke();

        resource.execute();
    }

    @Test public void testSwaggerRespApi() {
        final HttpInvoker httpInvoker = invoker(app.getServerUrl());

        final InvokerCommand<?> resource = httpInvoker.resource(SwaggerHandlerBase.Routes.restApi()).invoke();

        resource.execute();
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
}  // end class HandlerTest
