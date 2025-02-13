
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
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;

import static tekgenesis.common.core.Option.option;

/**
 * Represents a route to a target. If defined, holds target, path, and optional key.
 */
public abstract class Route<T> {

    //~ Constructors .................................................................................................................................

    private Route() {}

    //~ Methods ......................................................................................................................................

    /** Get route full path. */
    public abstract String toString();

    /** True if route has a defined target. */
    public abstract boolean isDefined();

    /** True if route targets nowhere. */
    public abstract boolean isNowhere();

    /** Get route optional key. */
    public abstract Option<String> getKey();

    /** Get route normalized path. */
    public abstract String getPath();

    /** Get route target element. */
    public abstract T getTarget();

    //~ Methods ......................................................................................................................................

    /** Return defined route without key. */
    @NotNull public static <T> Route<T> defined(@NotNull final T target, @NotNull final String path) {
        return new Defined<>(target, path);
    }

    /** Return defined route with specified key. */
    @NotNull
    @SuppressWarnings("WeakerAccess")
    public static <T> Route<T> defined(@NotNull final T target, @NotNull final String path, @Nullable final String key) {
        return new Defined<>(target, path, key);
    }

    /** Return optional route. */
    @NotNull public static <T> Route<T> maybe(@Nullable final T target, @NotNull final String path, @Nullable final String key) {
        return target == null ? nowhere() : defined(target, path, key);
    }

    /** Return nowhere route. */
    @NotNull
    @SuppressWarnings("unchecked")
    public static <T> Route<T> nowhere() {
        return (Route<T>) NOWHERE;
    }

    //~ Static Fields ................................................................................................................................

    private static final Route<Object> NOWHERE = new Route<Object>() {
            @Override public Object getTarget() {
                throw illegal();
            }

            @Override public String getPath() {
                throw illegal();
            }

            @Override public Option<String> getKey() {
                throw illegal();
            }

            @Override public boolean isDefined() {
                return false;
            }

            @Override public boolean isNowhere() {
                return true;
            }

            @Override
            @SuppressWarnings("DuplicateStringLiteralInspection")
            public String toString() {
                return "nowhere";
            }

            @SuppressWarnings("DuplicateStringLiteralInspection")
            private IllegalStateException illegal() {
                return new IllegalStateException("Nowhere!");
            }
        };

    //~ Inner Classes ................................................................................................................................

    private static class Defined<T> extends Route<T> {
        private final Option<String> key;
        private final String         path;

        private final T target;

        public Defined(@NotNull final T target, @NotNull final String path) {
            this(target, path, null);
        }

        public Defined(@NotNull final T target, @NotNull final String path, @Nullable final String key) {
            this.target = target;
            this.path   = path;
            this.key    = option(key);
        }

        @Override public String toString() {
            return path + key.map(k -> Routing.ROUTING_PATH_CHAR + k).orElse("");
        }

        @Override public boolean isDefined() {
            return true;
        }

        @Override public boolean isNowhere() {
            return false;
        }

        public Option<String> getKey() {
            return key;
        }

        public String getPath() {
            return path;
        }

        public T getTarget() {
            return target;
        }
    }  // end class Defined
}  // end class Route
