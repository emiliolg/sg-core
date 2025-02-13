
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.handler;

import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.StrBuilder;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.service.Method;
import tekgenesis.type.Type;

import static java.util.Map.Entry;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.metadata.handler.Routes.normalize;
import static tekgenesis.metadata.handler.Routes.split;

/**
 * Manages /path/to -> route.
 */
public class Routing {

    //~ Instance Fields ..............................................................................................................................

    private List<String> lastTriedRoutes = null;

    private final Map<Method, RoutingNode> roots;

    //~ Constructors .................................................................................................................................

    private Routing() {
        roots = new EnumMap<>(Method.class);
        for (final Method method : Method.values())
            roots.put(method, new RoutingNode());
    }

    //~ Methods ......................................................................................................................................

    /** Add given route to routing. Return previous route on matching path (if any). */
    @Nullable public Route addRoute(@NotNull final Route route) {
        return addRoute(route, route.getHttpMethod());
    }

    /**
     * Add given route to routing for the specified http method. Return previous route on matching
     * path (if any).
     */
    @Nullable public Route addRoute(@NotNull final Route route, final Method httpMethod) {
        return getRoot(httpMethod).put(route);
    }

    /** Print routing. */
    public void print(@NotNull final StrBuilder builder) {
        for (final Entry<Method, RoutingNode> entry : roots.entrySet()) {
            builder.append(entry.getKey()).append(":\n");
            entry.getValue().print(builder);
        }
    }

    @Override public String toString() {
        final StrBuilder builder = new StrBuilder();
        print(builder);
        return builder.toString();
    }

    /** Get last tried routes. */
    public List<String> getLastTriedRoutes() {
        final List<String> aux = lastTriedRoutes;
        lastTriedRoutes = null;
        return aux != null ? aux : Colls.emptyList();
    }

    /** Get optional route match for method and path. */
    public Option<RouteMatch> getRoute(@NotNull Method method, @NotNull String path, final boolean external) {
        final Tuple<Option<RouteMatch>, List<String>> tupleResult = getRoot(method).match(path);
        final Option<RouteMatch>                      result      = tupleResult.first();
        lastTriedRoutes = tupleResult.second();
        if (!external || result.isPresent() && !result.get().getRoute().isInternal()) return result;
        return Option.empty();
    }

    Option<Route> exists(@NotNull final Seq<Part> parts, @NotNull final Method method) {
        final List<Part> ps = parts.into(new ArrayList<>());
        return getRoot(method).find(ps);
    }

    /** Get root node for corresponding method. */
    private RoutingNode getRoot(@NotNull final Method method) {
        return roots.get(method);
    }

    //~ Methods ......................................................................................................................................

    /** Create empty routing. */
    public static Routing create() {
        return new Routing();
    }

    //~ Inner Classes ................................................................................................................................

    private static class RoutingNode {
        private RoutingNode                    dynamic  = null;
        private final int                      level;
        private final Part                     part;
        private Route                          route    = null;
        private final Map<String, RoutingNode> statics  = new TreeMap<>();
        private RoutingNode                    wildcard = null;

        private RoutingNode() {
            this(0, null);
        }

        private RoutingNode(int level, @Nullable Part part) {
            this.level = level;
            this.part  = part;
        }

        /** Get routes that were tried from this one on. */
        private void addTriedRoutes(@NotNull final List<String> triedRoutes) {
            if (wildcard != null) {
                if (wildcard.route != null) triedRoutes.add(wildcard.route.getPath());
                wildcard.addTriedRoutes(triedRoutes);
            }
            if (dynamic != null) {
                if (dynamic.route != null) triedRoutes.add(dynamic.route.getPath());
                dynamic.addTriedRoutes(triedRoutes);
            }
            for (final RoutingNode routingNode : statics.values()) {
                if (routingNode.route != null) triedRoutes.add(routingNode.route.getPath());
                routingNode.addTriedRoutes(triedRoutes);
            }
        }

        private RoutingNode createChild(@NotNull final Part p) {
            return new RoutingNode(level + 1, p);
        }

        private Option<Route> find(List<Part> parts) {
            if (parts.isEmpty()) return option(route);

            final Part        next   = parts.remove(0);
            final RoutingNode target = getRoutingNode(next, false);
            if (target != null) {
                if (next.isWildcard()) return option(target.route);
                else return target.find(parts);
            }
            return Option.empty();
        }

        private IllegalStateException incompatibleRoutes(Part p) {
            final Part existing = dynamic.part;
            return new IllegalStateException(
                String.format("Variable '%s' of type %s found for already existing '%s' of type %s.",
                    p.getName(),
                    p.getType(),
                    existing == null ? "" : existing.getName(),
                    existing == null ? "" : existing.getType()));
        }

        private Tuple<Option<RouteMatch>, List<String>> match(@NotNull final String path) {
            final List<String> parts          = split(normalize(path)).into(new ArrayList<>());
            final List<String> triedRoutesAux = new ArrayList<>();
            return tuple(match(parts, triedRoutesAux).map(value -> new RouteMatch(value, parts)), triedRoutesAux);
        }

        @SuppressWarnings("Duplicates")
        private Option<Route> match(@NotNull final List<String> parts, List<String> triedRoutes) {
            if (isWildcard() || parts.size() == level) return option(route);
            boolean noResult = true;
            if (parts.size() > level) {
                final RoutingNode node = statics.get(parts.get(level));
                if (node != null) {
                    final Option<Route> match = node.match(parts, triedRoutes);
                    noResult = false;
                    if (match.isPresent()) return match;
                }
                if (isNotEmpty(parts.get(level)) && dynamic != null && dynamic.part != null &&
                    matchesType(dynamic.part.getType(), parts.get(level)))
                {
                    final Option<Route> match = dynamic.match(parts, triedRoutes);
                    noResult = false;
                    if (match.isPresent()) return match;
                }
                if (wildcard != null) {
                    final Option<Route> match = wildcard.match(parts, triedRoutes);
                    noResult = false;
                    if (match.isPresent()) return match;
                }
            }
            if (noResult) addTriedRoutes(triedRoutes);
            return Option.empty();
        }

        private boolean matchesType(Type type, String text) {
            try {
                type.valueOf(text);  // Switch to more performing check (instead of exception)
                return true;
            }
            catch (final Exception ignored) {
                return false;
            }
        }

        private void print(@NotNull StrBuilder builder) {
            if (route != null) {
                builder.append(route.getPath()).append("\t");
                if (route.isInternal()) builder.append("[internal]\t");
                builder.append("->").append(route.getMethodName()).append("\n");
            }
            for (final RoutingNode s : statics.values())
                s.print(builder);
            if (dynamic != null) dynamic.print(builder);
            if (wildcard != null) wildcard.print(builder);
        }

        /** Put new route. Return previous route on matching path (if any). */
        @Nullable private Route put(@NotNull final Route r) {
            return put(r.getParts().into(new ArrayList<>()), r);
        }

        @Nullable private Route put(@NotNull final List<Part> parts, @NotNull final Route r) {
            if (parts.isEmpty()) return replaceRoute(r);

            final Part        next   = parts.remove(0);
            final RoutingNode target = ensureNotNull(getRoutingNode(next, true));
            if (next.isWildcard()) return target.replaceRoute(r);
            return target.put(parts, r);
        }

        private Route replaceRoute(Route r) {
            final Route previous = route;
            route = r;
            return previous;
        }

        private boolean isWildcard() {
            return part != null && part.isWildcard();
        }

        @Nullable private RoutingNode getDynamic(Part p, boolean create) {
            if (dynamic == null && create) dynamic = createChild(p);
            if (dynamic != null && dynamic.part != null && !dynamic.part.getType().equivalent(p.getType())) throw incompatibleRoutes(p);
            return dynamic;
        }

        @Nullable private RoutingNode getRoutingNode(Part p, boolean create) {
            if (p.isStatic()) return getStatic(p, create);
            if (p.isDynamic()) return getDynamic(p, create);
            return getWildcard(p, create);
        }

        @Nullable private RoutingNode getStatic(Part p, boolean create) {
            RoutingNode result = statics.get(p.getName());
            if (result == null && create) {
                result = createChild(p);
                statics.put(p.getName(), result);
            }
            return result;
        }

        @Nullable private RoutingNode getWildcard(Part p, boolean create) {
            if (wildcard == null && create) wildcard = createChild(p);
            return wildcard;
        }
    }  // end class RoutingNode
}  // end class Routing
