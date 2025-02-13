
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.formatter;

import org.jetbrains.annotations.Nullable;

/**
 * Handles input from the user. Involves formatting and filtering
 */
public class InputHandler<T> implements LocalizedConverter<T>, Formatter<T>, InputFilter {

    //~ Instance Fields ..............................................................................................................................

    private final LocalizedConverter<T> converter;
    private final InputFilter           filter;
    private Formatter<T>                formatter;

    //~ Constructors .................................................................................................................................

    /** Default Constructor. */
    private InputHandler(LocalizedConverter<T> converter, Formatter<T> formatter, InputFilter filter) {
        this.converter = converter;
        this.formatter = formatter;
        this.filter    = filter;
    }

    //~ Methods ......................................................................................................................................

    @Override public String filter(String value) {
        return filter.filter(value);
    }

    @Override public String format(@Nullable T value) {
        return formatter.format(value);
    }

    @Override public T fromString(@Nullable String s) {
        return converter.fromString(s);
    }

    @Override public String toString(@Nullable Object t) {
        return converter.toString(t);
    }

    void setFormatter(Formatter<T> formatter) {
        this.formatter = formatter;
    }

    //~ Methods ......................................................................................................................................

    /** Creates an {@link InputHandler} with a converter and formatter. */
    public static <T> InputHandler<T> create(LocalizedConverter<T> converter, Formatter<T> formatter) {
        return new InputHandler<>(converter, formatter, NONE_FILTER);
    }

    /** Creates an {@link InputHandler} with a converter, a formatter and a inputFilter. */
    public static <T> InputHandler<T> create(LocalizedConverter<T> converter, Formatter<T> formatter, InputFilter filter) {
        return new InputHandler<>(converter, formatter, filter);
    }

    /** Creates an identifier {@link InputHandler}. */
    public static InputHandler<String> id() {
        return IDENTIFIER_INPUT_HANDLER;
    }

    /** Creates a lowercase {@link InputHandler}. */
    public static InputHandler<String> lowercase() {
        return LOWERCASE_INPUT_HANDLER;
    }

    /** Creates an empty/null {@link InputHandler}. */
    public static InputHandler<Object> none() {
        return OBJECT_INPUT_HANDLER;
    }

    /** Creates a string {@link InputHandler}. */
    public static InputHandler<String> string() {
        return STRING_INPUT_HANDLER;
    }

    /** Creates an uppercase {@link InputHandler}. */
    public static InputHandler<String> uppercase() {
        return UPPERCASE_INPUT_HANDLER;
    }

    //~ Static Fields ................................................................................................................................

    private static final InputHandler<String> STRING_INPUT_HANDLER = create(STRING_CONVERTER, STRING_FORMATTER, NONE_FILTER);

    private static final InputHandler<String> UPPERCASE_INPUT_HANDLER = create(STRING_CONVERTER, UPPERCASE_FORMATTER, UPPERCASE_FILTER);

    private static final InputHandler<String> LOWERCASE_INPUT_HANDLER = create(STRING_CONVERTER, LOWERCASE_FORMATTER, LOWERCASE_FILTER);

    private static final InputHandler<String> IDENTIFIER_INPUT_HANDLER = create(STRING_CONVERTER, NONE_FORMATTER, IDENTIFIER_FILTER);

    private static final InputHandler<Object> OBJECT_INPUT_HANDLER = create(OBJECT_CONVERTER, OBJECT_FORMATTER, NONE_FILTER);
}  // end class InputHandler
