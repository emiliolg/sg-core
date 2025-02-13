
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.StrBuilder;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import static tekgenesis.common.collections.Colls.seq;

/**
 * Response Error for sending exceptions to client side.
 */
@SuppressWarnings({ "ThrowableResultOfMethodCallIgnored", "InstanceVariableMayNotBeInitialized" })
public class ResponseError implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private String     className;
    private List<Line> clazz;
    private String     devMessage;
    private boolean    devMode;
    private String     fileName;
    private int        line;
    private String     message;
    private String     methodName;
    private List<Line> stack = emptyList();

    //~ Constructors .................................................................................................................................

    /** GWT Empty Constructor. */
    @SuppressWarnings("WeakerAccess")
    protected ResponseError() {}

    /** Response Error. */
    public ResponseError(@NotNull Throwable cause, boolean devMode) {
        this.devMode = devMode;

        // In dev mode, we build message and stack trace to show it in client.
        if (devMode) build(cause);
        // else we only build message, since we don't need the stack trace (less traffic & processing).
        else buildOnlyCause(cause);
    }

    //~ Methods ......................................................................................................................................

    /** Returns a map with all the values of the error to use as XHTML parameters. */
    public Map<String, Object> asMapArguments() {
        final HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("fileName", fileName);
        arguments.put("errorMessage", devMessage + "! " + message);
        arguments.put("whereErrorMessage", "In " + className + "." + methodName + " at line " + line);
        if (devMode) stackToArguments(arguments);

        return arguments;
    }

    @Override public String toString() {
        final StrBuilder builder = new StrBuilder().append("Exception: ").append(devMessage).append("\nMessage: ").append(message);

        if (devMode)
            builder.append("\nClassname: ")
                .append(className)
                .append("\nFilename: ")
                .append(fileName)
                .append("\nMethod: ")
                .append(methodName)
                .append("\nLine: ")
                .append(line)
                .append("\nStack:\n")
                .startCollection("\n")
                .append(stack);

        return builder.toString();
    }

    /** Set response error source preview. */
    public ResponseError withClass(Option<List<Line>> preview) {
        clazz = preview.getOrNull();
        return this;
    }

    /** Returns the fqn of the affected class. */
    public String getClassName() {
        return className;
    }

    /** Returns the source preview. */
    public List<Line> getClazz() {
        return clazz;
    }

    /** Returns the exception message to show to the developer. */
    public String getDevMessage() {
        return devMessage;
    }

    /**
     * Returns true if the error happened in devMode mode. With this we will later show Homer at the
     * client.
     */
    public boolean isDevMode() {
        return devMode;
    }

    /** Returns the file name of the affected class. */
    public String getFileName() {
        return fileName;
    }

    /** Returns the line number of the exception. */
    public int getLine() {
        return line;
    }

    /** Returns the exception message. */
    public String getMessage() {
        return message;
    }

    /** Returns the method where the exception occurred. */
    public String getMethodName() {
        return methodName;
    }

    /** Returns the exception stack-trace. */
    public List<Line> getStack() {
        return stack;
    }

    private void build(Throwable cause) {
        buildOnlyCause(cause);

        // in dev mode, show exception detail
        final List<StackTraceElement> stackTrace = asList(cause.getStackTrace());
        if (!stackTrace.isEmpty()) {
            error(stackTrace.get(0));
            stack = new ArrayList<>(stackTrace.size());
            for (final StackTraceElement element : stackTrace)
                stack.add(new Line(element.getLineNumber(), element.getFileName(), element.toString()));
        }
    }

    private void buildOnlyCause(Throwable cause) {
        message    = deepest(cause).getMessage();
        devMessage = cause.toString();
    }

    private void error(StackTraceElement error) {
        className  = error.getClassName();
        fileName   = error.getFileName();
        methodName = error.getMethodName();
        line       = error.getLineNumber();
    }

    private void stackToArguments(HashMap<String, Object> arguments) {
        final List<String> lines    = new ArrayList<>();
        final List<String> contents = new ArrayList<>();
        final List<String> classes  = new ArrayList<>();

        for (final Line stackLine : stack) {
            lines.add(String.valueOf(stackLine.getLine()));
            contents.add(stackLine.getContent());
            classes.add(stackLine.getClazz());
        }

        arguments.put("errorLines", seq(lines));
        arguments.put("errorContents", seq(contents));
        arguments.put("errorClasses", seq(classes));
    }

    //~ Methods ......................................................................................................................................

    /** Finds deepest exception. */
    public static Throwable deepest(Throwable cause) {
        return cause.getCause() != null ? deepest(cause.getCause()) : cause;
    }

    //~ Static Fields ................................................................................................................................

    public static final ResponseError EMPTY = new ResponseError();

    private static final long serialVersionUID = 8005633027600766839L;

    //~ Inner Classes ................................................................................................................................

    /**
     * Represents a Line on a file or a list.
     */
    public static class Line implements Serializable {
        private String clazz;
        private String content;
        private int    line;

        /** GWT Empty Constructor. */
        protected Line() {}

        /** Line constructor. */
        public Line(int line, String clazz, String content) {
            this.line    = line;
            this.clazz   = clazz;
            this.content = content;
        }

        @Override public String toString() {
            return content;
        }

        /** Class name of the line. */
        public String getClazz() {
            return clazz;
        }

        /** Content of the line. */
        public String getContent() {
            return content;
        }

        /** Line number. */
        public int getLine() {
            return line;
        }

        private static final long serialVersionUID = 8005633027600733439L;
    }
}  // end class ResponseError
