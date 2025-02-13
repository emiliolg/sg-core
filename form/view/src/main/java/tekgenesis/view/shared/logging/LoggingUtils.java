
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.logging;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.logging.Logger;
import tekgenesis.metadata.form.SourceWidget;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.collections.Colls.mkString;
import static tekgenesis.common.logging.Logger.Level.INFO;

/**
 * Logging Utils method that could be shared between client or server code.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")  // Log class
public class LoggingUtils {

    //~ Constructors .................................................................................................................................

    private LoggingUtils() {}

    //~ Methods ......................................................................................................................................

    /** Logs a Abstract events. */
    public static void logAbstractMethod(Logger logger, SourceWidget delegate, SourceWidget source, String fqn, boolean clientSide) {
        if (logger.isLoggable(INFO)) {
            final StringBuilder builder = new StringBuilder();
            builder.append("Processing... abstract invocation from widget")
                .append(source.toString())
                .append(" delegated on ")
                .append(delegate.toString())
                .append(" on form ")
                .append(fqn);
            logInfo(logger, clientSide, builder);
        }
    }

    /** Logs Cancel events. */
    public static void logCancel(Logger logger, String formId, boolean clientSide) {
        if (logger.isLoggable(INFO)) {
            final StringBuilder builder = new StringBuilder();
            builder.append("Canceling...").append(" ").append(formId);
            logInfo(logger, clientSide, builder);
        }
    }

    /** Logs Deleting events. */
    public static void logDelete(Logger logger, String formId, boolean clientSide) {
        if (logger.isLoggable(INFO)) {
            final StringBuilder builder = new StringBuilder();
            builder.append("Deleting...").append(" ").append(formId);
            logInfo(logger, clientSide, builder);
        }
    }

    /** Logs deprecation operations. */
    public static void logDeprecate(final Logger logger, boolean deprecate, final String fqn, boolean clientSide) {
        if (logger.isLoggable(INFO)) {
            final StringBuilder builder = new StringBuilder();
            builder.append(deprecate ? "Deprecate... " : "Undeprecate... ").append(fqn);
            logInfo(logger, clientSide, builder);
        }
    }

    /** Logs a Fetch events. */
    public static void logFetch(@NotNull final Logger logger, @NotNull final Iterable<Integer> fetchIndexes, @NotNull final String loaderClassName,
                                boolean clientSide) {
        if (logger.isLoggable(INFO)) {
            final StringBuilder builder = new StringBuilder();
            builder.append("Fetching... indexes: ").append(mkString(fetchIndexes, "[", ",", "]")).append(" with class: ").append(loaderClassName);
            logInfo(logger, clientSide, builder);
        }
    }

    /** Logs a Fetch menu. */
    public static void logGetMenu(@NotNull final Logger logger, @Nullable final String menuFqn, boolean clientSide) {
        if (logger.isLoggable(INFO)) {
            final StringBuilder builder = new StringBuilder();
            builder.append("Menu... ").append(notNull(menuFqn, "[ALL]"));
            logInfo(logger, clientSide, builder);
        }
    }

    public static void logLazyFetch(Logger logger, SourceWidget widget, String formFullName, boolean clientSide) {
        logMethod(logger, widget, formFullName, clientSide, "lazyFetch");
    }

    /** Logs a Loading events. */
    public static void logLoad(Logger logger, String formId, String pk, String parameters, boolean clientSide) {
        if (logger.isLoggable(INFO)) {
            final StringBuilder builder = new StringBuilder();
            builder.append("Loading... ").append(formId);
            if (isNotEmpty(pk)) builder.append(" with pk: ").append(pk);
            if (isNotEmpty(parameters)) builder.append(" with parameters: ").append(parameters);
            logInfo(logger, clientSide, builder);
            // todo why??? System.out.println("LoggingUtils.logLoad :: " + builder.toString());
        }
    }

    /** Logs link form events. */
    public static void logLoadSync(Logger logger, SourceWidget widget, String formFullName, @Nullable String pk, boolean clientSide) {
        if (logger.isLoggable(INFO)) {
            final StringBuilder builder = new StringBuilder();
            builder.append("Processing... onClick for form ").append(formFullName).append(" on widget: ").append(widget.getPath());
            if (isNotEmpty(pk)) builder.append(" with pk: ").append(pk);
            logInfo(logger, clientSide, builder);
        }
    }

    /** Logs onBlur events. */
    public static void logOnBlur(Logger logger, SourceWidget widget, String formFullName, boolean clientSide) {
        logMethod(logger, widget, formFullName, clientSide, "onBlur");
    }

    /** Logs onChange events. */
    public static void logOnChange(Logger logger, Iterable<SourceWidget> sources, String formFullName, boolean clientSide) {
        logMethod(logger, formFullName, "onChange", mkString(sources, ","), clientSide);
    }

    /** Logs onChange indexed events. */
    public static void logOnChangeIndexed(Logger logger, Iterable<String> sources, String formFullName, boolean clientSide) {
        logMethod(logger, formFullName, "onChange", mkString(sources, ","), clientSide);
    }

    /** Logs onClick events. */
    public static void logOnClick(Logger logger, SourceWidget widget, @Nullable String formFullName, boolean clientSide) {
        logMethod(logger, widget, formFullName, clientSide, "onClick");
    }

    /** Logs onDisplay events. */
    public static void logOnDisplay(Logger logger, SourceWidget widget, String formFullName, boolean clientSide) {
        logMethod(logger, widget, formFullName, clientSide, "onDisplay");
    }

    /** Logs method invocation operations. */
    public static void logOnMethodInvocation(final Logger logger, String method, final String fqn, boolean clientSide) {
        if (logger.isLoggable(INFO)) {
            final StringBuilder builder = new StringBuilder("Server side method invocation: ");
            builder.append('\'').append(method).append("\' (").append(fqn).append(')');
            logInfo(logger, clientSide, builder);
        }
    }

    /** Logs onNewLocation events. */
    public static void logOnNewLocation(Logger logger, SourceWidget widget, String formFullName, boolean clientSide) {
        logMethod(logger, widget, formFullName, clientSide, "onNewLocation");
    }

    /** Logs onRowChange events. */
    public static void logOnRowChange(Logger logger, Iterable<SourceWidget> widgetIds, String formFullName, boolean clientSide) {
        logMethod(logger, formFullName, "onRowChange", mkString(widgetIds, ","), clientSide);
    }

    /** Logs a Submit events. */
    public static void logSubmit(Logger logger, String formId, String action, boolean clientSide) {
        if (logger.isLoggable(INFO)) {
            final StringBuilder builder = new StringBuilder();
            builder.append("Submitting...").append(" ").append(formId);
            if (isNotEmpty(action)) builder.append(" with action: ").append(action);
            logInfo(logger, clientSide, builder);
        }
    }

    private static void appendClientOrServerSide(StringBuilder builder, boolean clientSide) {
        // Cannot use GwtReplaceable.isClient()... returns true on tests :(
        builder.append(clientSide ? " [Client Side]" : " [Server Side]");
    }

    private static void logInfo(Logger logger, boolean clientSide, StringBuilder builder) {
        appendClientOrServerSide(builder, clientSide);
        logger.info(builder.toString());
    }

    private static void logMethod(Logger logger, SourceWidget widget, String formFullName, boolean clientSide, String method) {
        if (logger.isLoggable(INFO)) {
            final StringBuilder builder = new StringBuilder();
            builder.append("Processing... ")
                .append(method)
                .append(" for form ")
                .append(formFullName)
                .append(" on widget: ")
                .append(widget.toString());
            logInfo(logger, clientSide, builder);
        }
    }

    private static void logMethod(Logger logger, String formFullName, String method, String widgets, boolean clientSide) {
        if (logger.isLoggable(INFO)) {
            final StringBuilder builder = new StringBuilder();
            builder.append("Processing... ").append(method).append(" for form ").append(formFullName).append(" on widgets: ").append(widgets);
            logInfo(logger, clientSide, builder);
        }
    }
}  // end class LoggingUtils
