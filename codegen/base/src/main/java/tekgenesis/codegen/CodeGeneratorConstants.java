
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import tekgenesis.common.Predefined;
import tekgenesis.common.core.QName;

import static tekgenesis.common.util.JavaReservedWords.NULL;

/**
 * Constants for the Code Generation.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public interface CodeGeneratorConstants {
    //J-


    @NonNls String TABLE = "Table";

    @NonNls String SPLIT_TO_ARRAY = "splitToArray";
    @NonNls String ESCAPE_CHAR_ON = "escapeCharOn";
    @NonNls String PARTS          = "parts";
    @NonNls String JOIN           = "join";

    String    OFFSET        = "offset";
    String    LIMIT        = "limit";
    String    RESULT        = "result";
    String    CURRENT       = "current";
    String FIRST = "first";
    String[] TUPLE_METHODS = {
            FIRST, "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth"
    };

    /** Annotations. */
    String VALUE     = "value";
    String OVERRIDE  = "Override";

    String DEPRECATED_ANNOTATION  = "Deprecated";

    String STRING_ARRAY = "String[]";

    String THAT        = "that";
    String EQ          = " == ";
    String NOT_EQ      = " != ";
    String AND         = " && ";
    String OR          = " || ";
    String CAT         = " + ";
    String EMPTY       = "\"\"";
    String SPACE       = "\" \"";
    String EQ_NULL     = EQ + NULL;
    String NOT_EQ_NULL = NOT_EQ + NULL;


    @NonNls String JAVA_LANG = "java.lang";

    @NonNls String DEFAULT_NOT_NULL_ANNOTATION = "org.jetbrains.annotations.NotNull";
    @NonNls String DEFAULT_NULLABLE_ANNOTATION = "org.jetbrains.annotations.Nullable";
    @NonNls String SUPPRESS_WARNINGS_ANNOTATION = "SuppressWarnings";

    @NonNls String PREDEFINED_CLASS = Predefined.class.getName();
    @NonNls String ENSURE_NOT_NULL  = "ensureNotNull";
    @NonNls String FORMAT  = "format";

    @NonNls String EQUALS       = "equals";
    @NonNls String HASH_CODE    = "hashCode";
    @NonNls String TO_STRING    = "toString";
    @NonNls String DESCRIBE    = "describe";
    @NonNls QName EQUAL_METHOD = QName.createQName(PREDEFINED_CLASS, "equal");

    @NonNls String HASH_CODE_ALL= "hashCodeAll";

    @NonNls String GET_SINGLETON_METHOD = "getSingleton";
    @NonNls String CREATE_METHOD = "create";
    @NonNls String DEFINE_METHOD = "define";
    @NonNls String GETTER_COMMENT = "Returns the %s.";
    @NonNls String SETTER_COMMENT = "Sets the value of the %s.";
    @NonNls String CREATE_COMMENT = "Creates a %s.";
    @NonNls String BOOLEAN_GETTER_COMMENT = "Returns true if it is %s.";
    @NonNls String NULL_IF_NOT_PRESENT    = "Returns <code>null</code> if is not present";
    @NonNls String FAIL_IF_NOT_PRESENT    = "Throws EntityNotFoundException if is not present";

    @NonNls String FIELDS_COMMENT =
            "//~ Fields ...................................................................................................................";
    @NonNls String CONSTRUCTORS_COMMENT =
            "//~ Constructors .............................................................................................................";
    @NonNls String METHODS_COMMENT =
            "//~ Methods ..................................................................................................................";
    @NonNls String INNER_CLASSES_COMMENT =
            "//~ Inner Classes ............................................................................................................";
    @NonNls String GETTERS_COMMENT =
            "//~ Getters ..................................................................................................................";
    @NonNls String SETTERS_COMMENT =
            "//~ Setters ..................................................................................................................";
    @NonNls
    String TRUNCATE = "truncate";
    @NonNls
    String SCALE_AND_CHECK = "scaleAndCheck";
    @NonNls
    String CHECK_SIGNED_LENGTH = "checkSignedLength";
    @NonNls
    String CHECK_SIGNED = "checkSigned";

    @NonNls String JACKSON_PACKAGE = "com.fasterxml.jackson.annotation.";
    @NonNls String JSON_PROPERTY_ORDER = JACKSON_PACKAGE + "JsonPropertyOrder";
    @NonNls String JSON_PROPERTY = JACKSON_PACKAGE + "JsonProperty";
    @NonNls String JSON_CREATOR = JACKSON_PACKAGE + "JsonCreator";
    @NonNls String JSON_AUTODETECT = JACKSON_PACKAGE +   "JsonAutoDetect";

    @NonNls String LOGGER_FIELD = "logger";
    @NonNls
    String LAMBDA_OP = " -> ";

    /** returns an object tuple method suitable for the specified numer of arguments */
    @NotNull
    static String tupleMethod(int n) {
        return n == 2 ? "tuple2" : "tuple";
    }


    //J+
}  // end class CodeGeneratorConstants
