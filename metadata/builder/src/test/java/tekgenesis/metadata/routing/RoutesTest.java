
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.routing;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.exception.CollidingPathException;
import tekgenesis.metadata.exception.InvalidPathException;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.FormBuilder;
import tekgenesis.metadata.form.widget.FormBuilderPredefined;
import tekgenesis.repository.ModelRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;

/**
 * Test routing mechanism.
 */
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class RoutesTest {

    //~ Methods ......................................................................................................................................

    @Test public void testCollidingPaths() {
        final RoutesHelper routes = new RoutesHelper();

        // Define paths;
        routes.route("a/b", "FormB");
        routes.route("a/c", "FormC");
        routes.route("x/y/z", "FormZ");

        expectInvalidPath(routes, "a", CollidingPathException.class);
        expectInvalidPath(routes, "x", CollidingPathException.class);
        expectInvalidPath(routes, "x/y", CollidingPathException.class);
        expectInvalidPath(routes, "x/y/z/o/p/q", CollidingPathException.class);
    }

    @Test public void testInvalidPaths() {
        final RoutesHelper routes = new RoutesHelper();

        expectInvalidPath(routes, "/", InvalidPathException.class);
        expectInvalidPath(routes, "/  /a", InvalidPathException.class);
        expectInvalidPath(routes, "a/b//c", InvalidPathException.class);
    }

    @Test public void testPathNormalization() {
        final RoutesHelper routes = new RoutesHelper();

        // Define paths;
        routes.route("a", "FormA");
        routes.route("/b", "FormB");
        routes.route("c/", "FormC");
        routes.route("/d/", "FormD");

        final RoutesAssert routing = routes.routing();

        // Test routing;

        routing.assertRoute("/a", "FormA");
        routing.assertRoute("/b", "FormB");
        routing.assertRoute("/c", "FormC");
        routing.assertRoute("/d", "FormD");

        // Test routing with primary key;
        routing.assertRoute("/a/some:pk", "FormA");
        routing.assertRoute("/b/other:pk", "FormB");
        routing.assertRoute("/c/pk", "FormC");
        routing.assertRoute("/d/1", "FormD");

        // Test routing with slashed primary key;
        routing.assertRoute("/a/some/slashed/pk", "FormA");
        routing.assertRoute("/b/other/slashed/pk", "FormB");
        routing.assertRoute("/c/with/path", "FormC");
        routing.assertRoute("/d/1/2/3/4", "FormD");

        routing.assertRoute("a", "FormA");
        routing.assertRoute("a/", "FormA");
        routing.assertRoute("/a/", "FormA");
        routing.assertRoute("", Nowhere);
        routing.assertRoute("/", Nowhere);
        routing.assertRoute("/ /", Nowhere);
        routing.assertRoute("/a//", Nowhere);
        routing.assertRoute("/a////", Nowhere);
        routing.assertRoute("/ /a/", Nowhere);
    }  // end method testPathNormalization

    private void expectInvalidPath(@NotNull RoutesHelper routes, @NotNull String path, final Class<? extends BuilderException> e) {
        try {
            routes.attempt(path, "Unreachable");
            failBecauseExceptionWasNotThrown(e);
        }
        catch (final BuilderException ignored) {}
    }

    //~ Static Fields ................................................................................................................................

    private static final String Nowhere = "Nowhere";

    //~ Inner Classes ................................................................................................................................

    private static class RoutesAssert {
        private final Routing<Form> routing;

        private RoutesAssert(Routing<Form> routing) {
            this.routing = routing;
        }

        private void assertRoute(@NotNull final String path, @NotNull final String form) {
            final Route<Form> route = routing.route(path);
            if (Nowhere.equals(form)) {
                assertThat(route.isDefined()).isFalse();
                assertThat(route.isNowhere()).isTrue();
            }
            else {
                assertThat(route.isDefined()).isTrue();
                assertThat(route.isNowhere()).isFalse();
                assertThat(route.getTarget().getName()).isEqualTo(form);
            }
        }
    }

    private static class RoutesHelper {
        private final ModelRepository repository;

        private RoutesHelper() {
            repository = new ModelRepository();
        }

        private void attempt(@NotNull final String path, @NotNull final String form)
            throws BuilderException
        {
            final FormBuilder builder = FormBuilderPredefined.form("", "tekgenesis.test", form);
            builder.onRoute(path);
            builder.withRepository(repository);
            repository.add(builder.build());
        }

        private void route(@NotNull final String path, @NotNull final String form) {
            try {
                attempt(path, form);
            }
            catch (final BuilderException e) {
                fail(e.getMessage());
            }
        }

        private RoutesAssert routing() {
            return new RoutesAssert(Routes.routing(repository));
        }
    }
}  // end class RoutesTest
