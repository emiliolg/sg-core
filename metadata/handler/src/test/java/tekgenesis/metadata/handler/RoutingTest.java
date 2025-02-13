
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.handler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.StrBuilder;
import tekgenesis.common.service.Method;
import tekgenesis.field.FieldOption;
import tekgenesis.field.FieldOptions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import static tekgenesis.common.service.Method.GET;
import static tekgenesis.common.service.Method.POST;
import static tekgenesis.metadata.handler.Parts.retrieveParts;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "SpellCheckingInspection" })
public class RoutingTest {

    //~ Instance Fields ..............................................................................................................................

    @Rule public RoutingRule routing = new RoutingRule();

    //~ Methods ......................................................................................................................................

    @Test public void testPrint() {
        routing.addRoute(GET, "/$any*", "fallbackGet");
        routing.addRoute(POST, "/$any*", "fallbackPost");
        routing.addRoute(GET, "/ipad", "showIpad");
        routing.addRoute(GET, "/ipad/buy", "buyIpad");
        routing.addRoute(GET, "/$product", "show");
        routing.addRoute(GET, "/$product/buy", "buy");
        routing.addRoute(GET, "/$product/$action", "action");
        routing.addRoute(GET, "/", "list");
        routing.addInternalRoute(GET, "/private", "private");
        routing.addInternalRoute(POST, "/private", "private");

        final String expected = "GET:\n" +
                                "/\t->list\n" +
                                "/ipad\t->showIpad\n" +
                                "/ipad/buy\t->buyIpad\n" +
                                "/private\t[internal]\t->private\n" +
                                "/$product\t->show\n" +
                                "/$product/buy\t->buy\n" +
                                "/$product/$action\t->action\n" +
                                "/$any*\t->fallbackGet\n" +
                                "POST:\n" +
                                "/private\t[internal]\t->private\n" +
                                "/$any*\t->fallbackPost\n" +
                                "PUT:\n" +
                                "DELETE:\n" +
                                "HEAD:\n" +
                                "OPTIONS:\n" +
                                "PATCH:\n";

        routing.assertPrint(expected);
    }

    @Test public void testRoutesAliases() {
        routing.addRoute(GET, "/");
        routing.addRoute(GET, "/product/$id");

        assertThat(routing.addRoute(GET, "/")).isNotNull();
        assertThat(routing.addRoute(GET, "/product/$id")).isNotNull();
        assertThat(routing.addRoute(GET, "/product/$pid")).isNotNull();
        assertThat(routing.addRoute(GET, "/product/$var")).isNotNull();
        assertThat(routing.addRoute(GET, "/product/$some")).isNotNull();
        assertThat(routing.addRoute(GET, "/product/id")).isNull();
        assertThat(routing.addRoute(GET, "/product/$id*")).isNull();
        assertThat(routing.addRoute(GET, "/product")).isNull();

        routing.addInternalRoute(GET, "/product/$path*");
        assertThat(routing.addRoute(GET, "/product/any")).isNull();
        assertThat(routing.addRoute(GET, "/product/$any*")).isNotNull();
        assertThat(routing.addInternalRoute(GET, "/product")).isNotNull();
    }

    @Test public void testRoutesBase() {
        routing.addRoute(GET, "/");

        routing.assertGet("/");

        routing.failPost("/");
        routing.failGet("/any");
    }

    @Test public void testRoutesComplex() {
        routing.addRoute(GET, "/products");
        routing.addRoute(GET, "/products/featured");

        routing.assertGet("/products");
        routing.assertGet("/products/featured");

        routing.failGet("/");
        routing.failPost("/products");
        routing.failGet("/products/other");
    }

    @Test public void testRoutesDynamic() {
        routing.addRoute(POST, "/products/$pid");

        routing.assertPost("/products/ipad");

        routing.failPost("/products");
        routing.failGet("/products/ipad");
        routing.failPost("/products/ipad/features");
    }

    @Test public void testRoutesEmpty() {
        routing.addRoute(GET, "/$any", "dynamic");
        routing.failGet("/");  // Dynamic parts cannot be empty

        routing.addRoute(GET, "/$any*", "wildcard");
        routing.assertGetIs("/", "wildcard");  // Wildcard parts can be empty (Grrrrr... only on root!)

        routing.addRoute(GET, "/", "static");
        routing.assertGetIs("/", "static");  // Static empty parts are prioritized

        routing.assertGetIs("/some", "dynamic");
        routing.assertGetIs("/some/other", "wildcard");
    }

    @Test public void testRoutesExternal() {
        routing.addRoute(GET, "/products/featured");
        routing.addRoute(GET, "/products/new");
        routing.addInternalRoute(GET, "/products/private");

        routing.assertGetForExternal("/products/featured");
        routing.assertGetForExternal("/products/new");

        routing.failGetForExternal("/product/private");
    }

    @Test public void testRoutesMatching() {
        routing.addRoute(GET, "/", "list");
        routing.addRoute(GET, "/$pid", "find");
        routing.addRoute(POST, "/$pid", "create");
        routing.addRoute(GET, "/$pid/buy", "buy");

        routing.assertGetIs("/", "list");
        routing.assertGetIs("/ipad", "find");
        routing.assertPostIs("/ipad", "create");
        routing.assertGetIs("/ipad/buy", "buy");

        routing.failPost("/");
        routing.failGet("/ipad/features");
        routing.failPost("/ipad/buy");
    }

    @Test public void testRoutesPrefix() {
        routing.addRoute(GET, "/products");
        routing.addRoute(GET, "/products/featured");

        routing.assertGet("/products");
        routing.assertGet("/products/featured");

        routing.failGet("/");
        routing.failPost("/products");
        routing.failGet("/products/other");
    }

    @Test public void testRoutesPriority() {
        routing.addRoute(GET, "/$any*", "fallbackGet");
        routing.addRoute(POST, "/$any*", "fallbackPost");
        routing.addRoute(GET, "/ipad", "showIpad");
        routing.addRoute(GET, "/ipad/buy", "buyIpad");
        routing.addRoute(GET, "/$product", "show");
        routing.addRoute(GET, "/$product/buy", "buy");
        routing.addRoute(GET, "/$product/$action", "action");
        routing.addRoute(GET, "/", "list");

        routing.assertGetIs("/", "list");
        routing.assertGetIs("/ipad", "showIpad");
        routing.assertGetIs("/ipad/buy", "buyIpad");
        routing.assertGetIs("/iphone", "show");
        routing.assertGetIs("/iphone/buy", "buy");
        routing.assertGetIs("/iphone/stock", "action");
        routing.assertGetIs("/ipad/stock", "action");
        routing.assertGetIs("/iphone/features/camera", "fallbackGet");

        routing.assertPostIs("/", "fallbackPost");
        routing.assertPostIs("/ipad", "fallbackPost");
        routing.assertPostIs("/ipad/buy", "fallbackPost");
        routing.assertPostIs("/iphone", "fallbackPost");
        routing.assertPostIs("/iphone/buy", "fallbackPost");
        routing.assertPostIs("/iphone/stock", "fallbackPost");
        routing.assertPostIs("/ipad/stock", "fallbackPost");
        routing.assertPostIs("/iphone/features/camera", "fallbackPost");
    }

    @Test public void testRoutesSimple() {
        routing.addRoute(GET, "/products/featured");
        routing.addRoute(GET, "/products/new");

        routing.assertGet("/products/featured");
        routing.assertGet("/products/new");

        routing.failGet("/product");
        routing.failGet("/products");
        routing.failGet("/products/featured/new");
    }

    @Test public void testRoutesTypedAliases() {
        routing.addRoute(GET, "/products/$id:Int");

        assertThat(routing.addRoute(GET, "/products/$id:Int")).isNotNull();
        assertIllegalStateException("/products/$id:String", "String");
        assertIllegalStateException("/products/$id:Real", "Real");
        assertIllegalStateException("/products/$id:Decimal", "Decimal");
        assertIllegalStateException("/products/$id:Boolean", "Boolean");
        assertIllegalStateException("/products/$id:Date", "Date");
        assertIllegalStateException("/products/$id:DateTime", "DateTime");

        assertThat(routing.addRoute(GET, "/products/$id:Int/other")).isNull();
        routing.addRoute(GET, "/products/$id:Int/other");

        assertThat(routing.addRoute(GET, "/products/$id:Int/other")).isNotNull();
        assertIllegalStateException("/products/$id:String/other", "String");
        assertIllegalStateException("/products/$id:Real/other", "Real");
        assertIllegalStateException("/products/$id:Decimal/other", "Decimal");
        assertIllegalStateException("/products/$id:Boolean/other", "Boolean");
        assertIllegalStateException("/products/$id:Date/other", "Date");
        assertIllegalStateException("/products/$id:DateTime/other", "DateTime");

        assertThat(routing.addRoute(GET, "/products/678/other")).isNull();
        routing.addRoute(GET, "/products/678/other");

        assertThat(routing.addRoute(GET, "/products/678/other")).isNotNull();

        assertThat(routing.addRoute(GET, "/products/ipad/other")).isNull();
        routing.addRoute(GET, "/products/ipad/other");

        assertThat(routing.addRoute(GET, "/products/ipad/other")).isNotNull();
    }

    /** Not yet public. */
    @Test public void testRoutesWildcard() {
        routing.addRoute(GET, "/resources/$file*");

        routing.assertGet("/resources/index.html");
        routing.assertGet("/resources/css/style.css");
        routing.assertGet("/resources/js/provided/suigeneris.js");

        routing.failGet("/resources");  // Grrrr... Should be allowed!
    }

    private void assertIllegalStateException(@NotNull String path, @NotNull String type) {
        try {
            routing.addRoute(GET, path);
            failBecauseExceptionWasNotThrown(IllegalStateException.class);
        }
        catch (final IllegalStateException e) {
            assertThat(e).hasMessage("Variable 'id' of type " + type + " found for already existing 'id' of type Int.");
        }
    }

    //~ Inner Classes ................................................................................................................................

    public static class RoutingRule implements TestRule {
        private Routing routing = null;

        @Override public Statement apply(final Statement base, Description description) {
            return new Statement() {
                @Override public void evaluate()
                    throws Throwable
                {
                    routing = Routing.create();
                    try {
                        base.evaluate();
                    }
                    finally {
                        routing = null;
                    }
                }
            };
        }

        void assertPrint(@NotNull final String expected) {
            final StrBuilder builder = new StrBuilder();
            routing.print(builder);
            assertThat(builder.toString()).isEqualTo(expected);
        }

        @Nullable private Route addInternalRoute(@NotNull final Method method, @NotNull final String path) {
            return addInternalRoute(method, path, "");
        }

        @Nullable private Route addInternalRoute(@NotNull final Method method, @NotNull final String path, final String result) {
            return routing.addRoute(createRoute(method, path, result, true));
        }

        @Nullable private Route addRoute(@NotNull final Method method, @NotNull final String path) {
            return addRoute(method, path, "");
        }

        @Nullable private Route addRoute(@NotNull final Method method, @NotNull final String path, final String result) {
            return routing.addRoute(createRoute(method, path, result, false));
        }

        private void assertGet(@NotNull final String path) {
            assertIsDefined(path, GET, true, false);
        }

        private void assertGetForExternal(@NotNull final String path) {
            assertIsDefined(path, GET, true, true);
        }

        private void assertGetIs(@NotNull String path, @NotNull String result) {
            assertResult(path, result, GET);
        }

        private void assertIsDefined(String path, Method method, boolean expected, boolean external) {
            assertThat(routing.getRoute(method, path, external).isPresent()).isEqualTo(expected);
        }

        private void assertPost(@NotNull final String path) {
            assertIsDefined(path, POST, true, false);
        }

        private void assertPostIs(@NotNull String path, @NotNull String result) {
            assertResult(path, result, POST);
        }

        private void assertResult(String path, String result, Method httpMethod) {
            final Option<RouteMatch> match = routing.getRoute(httpMethod, path, false);
            assertThat(match.isPresent()).isTrue();
            assertThat(match.get().getRoute().getMethodName()).isEqualTo(result);
        }

        private Route createRoute(@NotNull Method method, @NotNull String path, @NotNull String result, boolean internal) {
            final FieldOptions options = new FieldOptions();
            options.put(FieldOption.METHOD, method);
            options.put(FieldOption.CLASS_METHOD, result);
            if (internal) options.put(FieldOption.INTERNAL, Boolean.TRUE);
            return new Route(path, retrieveParts(path), options);
        }

        private void failGet(@NotNull final String path) {
            assertIsDefined(path, GET, false, false);
        }

        private void failGetForExternal(@NotNull final String path) {
            assertIsDefined(path, GET, false, true);
        }

        private void failPost(@NotNull final String path) {
            assertIsDefined(path, POST, false, false);
        }
    }  // end class RoutingRule
}  // end class RoutingTest
