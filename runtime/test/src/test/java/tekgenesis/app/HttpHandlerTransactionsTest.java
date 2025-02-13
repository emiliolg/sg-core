
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
import tekgenesis.common.collections.Seq;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.invoker.CallResource;
import tekgenesis.common.invoker.GenericType;
import tekgenesis.common.invoker.HttpInvoker;
import tekgenesis.common.invoker.HttpInvokerResult;
import tekgenesis.common.service.Call;
import tekgenesis.common.service.Status;
import tekgenesis.common.tools.test.AppTests;
import tekgenesis.common.tools.test.ApplicationRule;
import tekgenesis.sales.basic.service.Country;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.common.invoker.HttpInvokers.invoker;
import static tekgenesis.common.service.Status.NOT_FOUND;
import static tekgenesis.common.service.Status.OK;
import static tekgenesis.sales.basic.service.CountryHandlerBase.Routes.*;

/**
 * Test Handler transactional behavior.
 */
@Category(AppTests.class)
@SuppressWarnings({ "MagicNumber", "JavaDoc", "DuplicateStringLiteralInspection" })
public class HttpHandlerTransactionsTest {

    //~ Methods ......................................................................................................................................

    @Test public void testDeleteCommitOnSuccess() {
        final Country country = createCountry("xx", "XX Nation");
        postCountry(country);

        // Delete recently created country
        final HttpInvokerResult<?> deleteResult = createResource(delete(country.getIso2())).invoke().execute();
        assertThat(deleteResult.getStatus().isSuccessful()).isTrue();

        // List all after successful delete
        final Seq<Country> last = listAll();
        assertThat(last).extracting("iso2").doesNotContain(country.getIso2());
    }

    @Test public void testDeleteRollbackWithoutSuccess() {
        final Country country = createCountry("ww", "WW Nation");
        postCountry(country);

        // Delete recently created country
        final CallResource<?> delete = createResource(delete(country.getIso2()));
        delete.param("success", "false");
        final Status deleteStatus = delete.invoke().getStatus();
        assertThat(deleteStatus.isClientError()).isTrue();

        // List all after unsuccessful delete
        final Seq<Country> last = listAll();
        assertThat(last).extracting("iso2").contains(country.getIso2());
    }

    @Test public void testGetCommitOnSuccess() {
        // Invoke get method with insert
        final HttpInvokerResult<Country> rollbackResult = createResource(getAndInsert()).invoke(Country.class).execute();
        assertThat(rollbackResult.getStatus().isSuccessful()).isTrue();
        final Country country = rollbackResult.get();
        assertThat(country.getIso2()).isEqualTo("aa");

        // Assert successful insert (due to get method commit)
        final Status statusResult = createResource(get(country.getIso2())).invoke(Country.class).getStatus();
        assertThat(statusResult).isEqualTo(OK);
    }

    @Test public void testGetRollbackWithoutSuccess() {
        // Invoke get method with insert
        final Status status = createResource(getAndInsert()).param("success", "false").invoke(Country.class).getStatus();
        assertThat(status.isClientError()).isTrue();

        // Assert unsuccessful insert (due to get method rollback)
        final Status statusResult = createResource(get("aa")).invoke(Country.class).getStatus();
        assertThat(statusResult).isEqualTo(NOT_FOUND);
    }

    @Test public void testPostCommitOnSuccess() {
        postCountry(createCountry("zz", "ZZ Nation"));
    }

    @Test public void testPostRollbackAfterException() {
        // List all before post
        final Seq<Country> before = listAll();

        // Post new country without success
        final CallResource<?> post       = createResource(postWithException());
        final Status          postStatus = post.invoke(createCountry("yy", "YY Nation")).getStatus();
        assertThat(postStatus.isServerError()).isTrue();

        // List all after post
        final Seq<Country> after = listAll();

        // Assert country post transaction was not committed
        assertThat(before).extracting("iso2").doesNotContain("yy");
        assertThat(after).extracting("iso2").doesNotContain("yy");
    }

    @Test public void testPostRollbackWithoutSuccess() {
        // List all before post
        final Seq<Country> before = listAll();

        // Post new country without success
        final CallResource<?> post = createResource(post());
        post.param("success", "false");
        final Status postStatus = post.invoke(createCountry("yy", "YY Nation")).getStatus();
        assertThat(postStatus.isClientError()).isTrue();

        // List all after post
        final Seq<Country> after = listAll();

        // Assert country post transaction was not committed
        assertThat(before).extracting("iso2").doesNotContain("yy");
        assertThat(after).extracting("iso2").doesNotContain("yy");
    }

    @Test public void testPutCommitOnSuccess() {
        final Country country = createCountry("uu", "UU Nation");
        postCountry(country);

        // Update recently created country
        country.setName("Patria Nostra");
        final HttpInvokerResult<?> putResult = createResource(put(country.getIso2())).invoke(country).execute();
        assertThat(putResult.getStatus().isSuccessful()).isTrue();

        // Assert successful update
        final HttpInvokerResult<Country> getResult = createResource(get(country.getIso2())).invoke(Country.class).execute();
        assertThat(getResult.getStatus().isSuccessful()).isTrue();
        assertThat(getResult.get().getName()).isEqualTo("Patria Nostra");
    }

    @Test public void testPutRollbackWithoutSuccess() {
        final Country country = createCountry("vv", "VV Nation");
        postCountry(country);

        // Update recently created country
        final CallResource<?> put = createResource(put(country.getIso2()));
        put.param("success", "false");
        country.setName("Patria Nostra");
        final Status putStatus = put.invoke(country).getStatus();
        assertThat(putStatus.isClientError()).isTrue();

        // Assert unsuccessful update
        final HttpInvokerResult<Country> getResult = createResource(get(country.getIso2())).invoke(Country.class).execute();
        assertThat(getResult.getStatus().isSuccessful()).isTrue();
        assertThat(getResult.get().getName()).isEqualTo("VV Nation");
    }

    private Country createCountry(String iso2, String name) {
        final Country result = new Country();
        result.setIso2(iso2);
        result.setName(name);
        return result;
    }

    private HttpInvoker createInvoker() {
        return invoker(app.getServerUrl());
    }

    private CallResource<?> createResource(Call call) {
        return createInvoker().resource(call);
    }

    private Seq<Country> listAll() {
        final CallResource<?>                 list   = createResource(list());
        final HttpInvokerResult<Seq<Country>> result = list.invoke(new GenericType<Seq<Country>>() {}).execute();
        assertThat(result.getStatus().isSuccessful()).isTrue();
        return result.get();
    }

    private void postCountry(Country country) {
        // List all before post
        final Seq<Country> before = listAll();

        // Post new country
        final CallResource<?>      post       = createResource(post());
        final HttpInvokerResult<?> postResult = post.invoke(country).execute();
        assertThat(postResult.getStatus().isSuccessful()).isTrue();

        // List all after post
        final Seq<Country> after = listAll();

        // Assert country post transaction was committed
        assertThat(before).extracting("iso2").doesNotContain(country.getIso2());
        assertThat(after).extracting("iso2").contains(country.getIso2());
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
}  // end class HttpHandlerTransactionsTest
