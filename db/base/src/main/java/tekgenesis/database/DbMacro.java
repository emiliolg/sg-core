
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Tuple3;
import tekgenesis.common.util.Preprocessor;

import static java.lang.String.format;

import static tekgenesis.common.collections.Colls.mkString;
import static tekgenesis.common.core.Constants.*;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.database.SqlConstants.CURRENT_DATE;
import static tekgenesis.database.SqlConstants.CURRENT_TIMESTAMP;
import static tekgenesis.database.SqlConstants.asSqlConstant;

/**
 * Macros to abstract differences between databases.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public enum DbMacro {

    //~ Enum constants ...............................................................................................................................

    False("false"), True("true"), CheckBoolConstraint(""), CheckBool(""),  // Deprecated
    Identity(INT),                                                         // Deprecated
    Serial(INT), BigSerial(BIGINT), EmptyString("''"),                     //
    SequenceStartValue("$1"),                                              //
    SequenceCache("Cache 10"),                                             //
    CurrentDate(DbMacro::currentDate),                                     //
    CurrentTime(DbMacro::currentTime),                                     //
    DbCurrentDate(CURRENT_DATE),                                           //
    DbCurrentTime(CURRENT_TIMESTAMP),                                      //
    Utc("(($1 at local) at time zone interval '0' minute)"),               // Convert to UTC
    SeqNextVal,                                                            // The expression to take the next value
                                                                           // for sequences
    ForUpdate("for update"),                                               // for update clause
    NoWait("nowait"),                                                      // nowait used in select for update
    SkipLocked("skip locked"),                                             // skip locked in select for update
    Distinct("distinct"),                                                  // distinct for select
    SelectCurrentTime("select CurrentTime"),                               // Returns current timestamp
    MinDateTime("timestamp '-4712-01-01 00:00:00 GMT'"),                   // Minimum DateTime Value
    MaxDateTime("timestamp '9999-31-12 23:59:59 GMT'"),                    // Maximum DateTime Value
    Values(DbMacro::valuesAsSql),                                          // Special for merge
    UpdateIf("and $1 then update set $2"),                                 // Conditional update on merge
    NlsSort("$1"),                                                         // Specify NLS sort order

    // -- Features

    NeedsCreateSequence,          //
    NeedsGrantReference("true"),  //
    NeedsSerialComment,           //

    // -- DML Macros
    RenameColumn("rename column $1 to $2"),        // All but Hsql
    SetNotNull("alter column $1 set not null"),    // Hsql, Postgres, Mysql
    DropNotNull("alter column $1 set null"),       // Hsql, Mysql
    SetDefault("alter column $1 set default $2"),  // Hsql, Postgres, Mysql
    AlterColumnType("alter column $1 $2"),         // Hsql, Postgres, Mysql, MsSql
    AddColumn("add column $1"),                    // Hsql, Postgres, Mysql, MsSql
    CommentOnView("comment on table"),             // Hsql, Postgres, Mysql, MsSql
    AlterView("alter view"),                       // Hsql, Mysql, MsSql

    // Qualified names where schema is prefixed
    Schema("$1"),                                           // <-- This will be overridden when the preprocessor is defined
    SchemaOrUser("$CURRENT_USER"), QName("Schema($1).$2"),  //
    TableName("QName($1,$2)"),                              //
    IndexName("QName($1,$2)"),                              //

    // Functions
    bitand("bitand($1,$2)"),

    // --- Types that allow redefinition --
    bigint("bigint"), nvarchar("nvarchar($1)"),  //
    datetime("datetime($1)"), clob("clob"),      //
    blob("blob"),                                //
    double_("double precision"),                 //
    boolean_(BOOLEAN);

    //~ Instance Fields ..............................................................................................................................

    private final Preprocessor.Macro defaultValue;

    private final String id;

    //~ Constructors .................................................................................................................................

    DbMacro() {
        this((Preprocessor.Macro) null);
    }

    DbMacro(@NotNull final String value) {
        this(new Preprocessor.Macro(value));
    }

    DbMacro(@NotNull final Function<List<String>, String> value) {
        this(new Preprocessor.Macro(value));
    }

    DbMacro(@Nullable final Preprocessor.Macro defaultValue) {
        this.defaultValue = defaultValue;
        id                = name().replaceAll("_", "");
    }

    //~ Methods ......................................................................................................................................

    /** Returns the id of the macro. */
    public String id() {
        return id;
    }

    @Override public String toString() {
        return id;
    }

    /** Returns the Macro default value. */
    @Nullable public Preprocessor.Macro getDefaultValue() {
        return defaultValue;
    }
    /** Returns the default value as an String. */
    public String getStringValue() {
        return defaultValue == null ? "" : defaultValue.asString();
    }

    //~ Methods ......................................................................................................................................

    /** Return all default values as a map. */
    public static EnumMap<DbMacro, Preprocessor.Macro> defaultValues() {
        final EnumMap<DbMacro, Preprocessor.Macro> map = new EnumMap<>(DbMacro.class);
        for (final DbMacro macro : values())
            if (macro.defaultValue != null) map.put(macro, macro.defaultValue);
        return map;
    }

    static Tuple3<String, ArrayList<String>, ArrayList<String>> parseValues(final List<String> args) {
        final String            alias     = args.get(0);
        final ArrayList<String> argValues = new ArrayList<>();
        final ArrayList<String> argNames  = new ArrayList<>();
        final Pattern           pattern   = Pattern.compile("(.*\\S) +(\\w+)");
        for (int i = 1; i < args.size(); i++) {
            final String  arg     = args.get(i);
            final Matcher matcher = pattern.matcher(arg);
            if (matcher.matches()) {
                argValues.add(matcher.group(1));
                argNames.add(matcher.group(2));
            }
            else {
                argValues.add(arg);
                argNames.add(arg);
            }
        }
        return tuple(alias, argValues, argNames);
    }
    private static String currentDate(final List<String> args) {
        return Database.useClientTime() ? asSqlConstant(DateOnly.current()) : DbCurrentDate.name();
    }
    private static String currentTime(final List<String> args) {
        return Database.useClientTime() ? asSqlConstant(DateTime.current()) : DbCurrentTime.name();
    }

    private static String valuesAsSql(final List<String> args) {
        final Tuple3<String, ArrayList<String>, ArrayList<String>> t = parseValues(args);
        return format("values(%s) as %s (%s)", mkString(t._2(), ","), t._1(), mkString(t._3(), ","));
    }
}
