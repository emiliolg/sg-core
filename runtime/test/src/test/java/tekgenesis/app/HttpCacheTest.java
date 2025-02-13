
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.app;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import tekgenesis.authorization.shiro.ShiroProps;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.invoker.CallResource;
import tekgenesis.common.invoker.HttpInvoker;
import tekgenesis.common.invoker.HttpInvokerResult;
import tekgenesis.common.tools.test.AppTests;
import tekgenesis.common.tools.test.ApplicationRule;
import tekgenesis.form.properties.XHtmlProps;

import static java.lang.Thread.sleep;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.common.invoker.HttpInvokers.invoker;
import static tekgenesis.sales.basic.service.SiteHandlerBase.Routes.cache;

/**
 * Test SuiGeneris cache.
 */
@Category(AppTests.class)
@SuppressWarnings({ "DuplicateStringLiteralInspection", "MagicNumber" })
public class HttpCacheTest {

    //~ Methods ......................................................................................................................................

    /** Test to check if cache is working. */
    @Test public void testDontGetFromCache()
        throws InterruptedException
    {
        final CallResource<?>           resource    = createInvoker().resource(cache());
        final HttpInvokerResult<byte[]> firstResult = resource.invoke(byte[].class).execute();
        sleep(4000);
        final HttpInvokerResult<byte[]> secondResult = resource.invoke(byte[].class).execute();
        assertThat(firstResult.get()).isNotEqualTo(secondResult.get());
    }

    /** Test to check if cache is working. */
    @Test public void testGetFromCache()
        throws InterruptedException
    {
        final CallResource<?>           resource    = createInvoker().resource(cache());
        final HttpInvokerResult<byte[]> firstResult = resource.invoke(byte[].class).execute();
        sleep(1000);
        final HttpInvokerResult<byte[]> secondResult = resource.invoke(byte[].class).execute();
        assertThat(firstResult.get()).isEqualTo(secondResult.get());
    }

    private HttpInvoker createInvoker() {
        return invoker(app.getServerUrl());
    }

    //~ Static Fields ................................................................................................................................

    @ClassRule public static ApplicationRule app = new ApplicationRule() {
            @Override protected void before()
                throws Exception
            {
                super.before();
                final XHtmlProps xHtmlProps = new XHtmlProps();
                xHtmlProps.cacheExpiration = 3;
                Context.getEnvironment().put(xHtmlProps);
                final ShiroProps shiroProps = new ShiroProps();
                shiroProps.autoLogin = "admin:password";
                Context.getEnvironment().put(shiroProps);
            }
        };
}  // end class HttpCacheTest
