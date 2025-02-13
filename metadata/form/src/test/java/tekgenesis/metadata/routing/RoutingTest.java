
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.routing;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import static tekgenesis.metadata.routing.Routing.*;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection" })
public class RoutingTest {

    //~ Methods ......................................................................................................................................

    @Test public void testCheckCollisions() {
        final Routing<String> routingA = new Routing<>();
        routingA.addRoute("a", "A");
        assertThat(routingA.checkCollisions("a")).isFalse();
        assertThat(routingA.checkCollisions("a/b")).isFalse();

        final Routing<String> routingB = new Routing<>();
        routingB.addRoute("a/b", "A");
        assertThat(routingB.checkCollisions("a")).isFalse();
        assertThat(routingB.checkCollisions("a/b")).isFalse();

        final Routing<String> routingC = new Routing<>();
        routingC.addRoute("a/c", "A");
        assertThat(routingC.checkCollisions("a/b")).isTrue();

        final Routing<String> routingD = new Routing<>();
        routingD.addRoute("a/b/c/d", "A");
        assertThat(routingD.checkCollisions("d")).isTrue();
        assertThat(routingD.checkCollisions("c/d")).isTrue();
        assertThat(routingD.checkCollisions("b/c/d")).isTrue();
        assertThat(routingD.checkCollisions("a")).isFalse();
        assertThat(routingD.checkCollisions("a/b")).isFalse();
        assertThat(routingD.checkCollisions("a/b/c")).isFalse();
    }

    @Test public void testIsValid() {
        assertThat(isValid("a")).isTrue();
        assertThat(isValid("/a")).isTrue();
        assertThat(isValid("/a/b")).isTrue();
        assertThat(isValid("/a/b/")).isTrue();

        assertThat(isValid("/a//")).isFalse();
        assertThat(isValid("/a/b//")).isFalse();
        assertThat(isValid("")).isFalse();
        assertThat(isValid("/")).isFalse();
        assertThat(isValid("//a")).isFalse();
        assertThat(isValid("///")).isFalse();
    }

    @Test public void testNormalize() {
        assertThat(normalize("a")).isEqualTo("/a");
        assertThat(normalize("/b")).isEqualTo("/b");
        assertThat(normalize("c/")).isEqualTo("/c");
        assertThat(normalize("/d/")).isEqualTo("/d");

        assertThat(normalize("/")).isEqualTo("/");
        assertThat(normalize("")).isEqualTo("/");
    }

    @Test public void testRoute() {
        final Routing<String> routing = new Routing<>();

        assertThat(routing.addRoute("a/b", "B")).isTrue();

        assertThat(routing.route("a/b").getKey().isEmpty()).isTrue();

        final Route<?> route = routing.route("a/b/some:pk");
        assertThat(route.getKey().isPresent()).isTrue();
        assertThat(route.getKey().get()).isEqualTo("some:pk");
        assertThat(route.getPath()).isEqualTo("/a/b");
        assertThat(route.getTarget()).isEqualTo("B");
        assertThat(route.isDefined()).isTrue();
        assertThat(route.isNowhere()).isFalse();

        assertThat(route.toString()).isEqualTo("/a/b/some:pk");

        final Route<?> nowhere = routing.route("/dev/null");
        assertThat(nowhere.toString()).isEqualTo("nowhere");
        assertThat(nowhere.isNowhere()).isTrue();
        assertThat(nowhere.isDefined()).isFalse();

        try {
            nowhere.getTarget();
            failBecauseExceptionWasNotThrown(IllegalStateException.class);
        }
        catch (final IllegalStateException e) {
            assertThat(e).hasMessage("Nowhere!");
        }

        try {
            nowhere.getPath();
            failBecauseExceptionWasNotThrown(IllegalStateException.class);
        }
        catch (final IllegalStateException e) {
            assertThat(e).hasMessage("Nowhere!");
        }

        try {
            nowhere.getKey();
            failBecauseExceptionWasNotThrown(IllegalStateException.class);
        }
        catch (final IllegalStateException e) {
            assertThat(e).hasMessage("Nowhere!");
        }
    }  // end method testRoute

    @Test public void testRouting() {
        final Routing<String> routing = new Routing<>();

        assertThat(routing.addRoute("a/b", "B")).isTrue();
        assertThat(routing.addRoute("a/c", "C")).isTrue();
        assertThat(routing.addRoute("x/y/z", "Z")).isTrue();

        assertThat(routing.route("a/b").isDefined()).isTrue();
        assertThat(routing.route("a/c").isDefined()).isTrue();
        assertThat(routing.route("x/y/z").isDefined()).isTrue();

        assertThat(routing.route("a/b/pk").isDefined()).isTrue();
        assertThat(routing.route("a/c/pk").isDefined()).isTrue();
        assertThat(routing.route("x/y/z/pk").isDefined()).isTrue();

        assertThat(routing.route("a/b/pk/with/slash").isDefined()).isTrue();
        assertThat(routing.route("a/c/pk/with/slash").isDefined()).isTrue();
        assertThat(routing.route("x/y/z/pk/with/slash").isDefined()).isTrue();

        assertThat(routing.addRoute("", "Invalid")).isFalse();
        assertThat(routing.addRoute("/", "Invalid")).isFalse();
        assertThat(routing.addRoute("p//", "Invalid")).isFalse();
        assertThat(routing.addRoute("p/q//r", "Invalid")).isFalse();
        assertThat(routing.addRoute("//p/q/r", "Invalid")).isFalse();

        assertThat(routing.route("").isDefined()).isFalse();
        assertThat(routing.route("/").isDefined()).isFalse();
        assertThat(routing.route("p//").isDefined()).isFalse();
        assertThat(routing.route("p/q//r").isDefined()).isFalse();
        assertThat(routing.route("//p/q/r").isDefined()).isFalse();

        assertThat(routing.addRoute("a/d", "D")).isTrue();
        assertThat(routing.addRoute("a", "Collision")).isFalse();
        assertThat(routing.addRoute("x/y", "Collision")).isFalse();
        assertThat(routing.addRoute("a/b/p", "Collision")).isFalse();

        assertThat(routing.route("a/d").isDefined()).isTrue();
        assertThat(routing.route("a").isDefined()).isFalse();
        assertThat(routing.route("x/y").isDefined()).isFalse();
    }  // end method testRouting

    @Test public void testSplitSections() {
        assertThat(splitSections("/a").toString()).isEqualTo("(a)");
        assertThat(splitSections("/a/").toString()).isEqualTo("(a)");
        assertThat(splitSections("/a/b").toString()).isEqualTo("(a, b)");
        assertThat(splitSections("/a/b/").toString()).isEqualTo("(a, b)");
        assertThat(splitSections("/a/b/c").toString()).isEqualTo("(a, b, c)");
        assertThat(splitSections("/a/b/c/").toString()).isEqualTo("(a, b, c)");
        assertThat(splitSections("/").toString()).isEqualTo("()");
        assertThat(splitSections("").toString()).isEqualTo("()");
    }

    @Test public void testSplitSuccessions() {
        assertThat(splitSuccessions("/a").toString()).isEqualTo("(/a)");
        assertThat(splitSuccessions("/a/").toString()).isEqualTo("(/a)");
        assertThat(splitSuccessions("/a/b").toString()).isEqualTo("(/a, /a/b)");
        assertThat(splitSuccessions("/a/b/").toString()).isEqualTo("(/a, /a/b)");
        assertThat(splitSuccessions("/a/b/c").toString()).isEqualTo("(/a, /a/b, /a/b/c)");
        assertThat(splitSuccessions("/a/b/c/").toString()).isEqualTo("(/a, /a/b, /a/b/c)");
        assertThat(splitSuccessions("/").toString()).isEqualTo("()");
        assertThat(splitSuccessions("").toString()).isEqualTo("()");
    }
}  // end class RoutingTest
