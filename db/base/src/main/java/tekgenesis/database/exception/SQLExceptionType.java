
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.exception;

import java.sql.SQLException;

import org.jetbrains.annotations.NotNull;

/**
 * SQL Exception Types.
 */
public enum SQLExceptionType {

    //~ Enum constants ...............................................................................................................................

    BAD_GRAMMAR { @Override public DatabaseException buildException(@NotNull SQLException exception) { return new BadGrammarException(exception); } },
    INVALID_NAME {
        @Override public DatabaseException buildException(@NotNull SQLException exception) { return new InvalidNameException(exception); }},
    DUPLICATE_OBJECT {
        @Override public DatabaseException buildException(@NotNull SQLException exception) { return new DuplicateObjectException(exception); }},
    INSUFFICIENT_PRIVILEGES {
        @Override public DatabaseException buildException(@NotNull SQLException exception) { return new InsufficientPriviledgesException(exception); }},

    INTEGRITY_VIOLATION {
        @Override public DatabaseException buildException(@NotNull SQLException exception) {
            return new DatabaseIntegrityViolationException(exception);
        }},
    RESTRICT_VIOLATION {
        @Override public DatabaseException buildException(@NotNull SQLException exception) { return new RestrictViolationException(exception); }},
    NOT_NULL_VIOLATION {
        @Override public DatabaseException buildException(@NotNull SQLException exception) { return new NotNullViolationException(exception); }},
    FOREIGN_KEY_VIOLATION {
        @Override public DatabaseException buildException(@NotNull SQLException exception) { return new ForeignKeyViolationException(exception); }},
    UNIQUE_VIOLATION {
        @Override public DatabaseException buildException(@NotNull SQLException exception) { return new UniqueViolationException(exception); }},
    CHECK_VIOLATION {
        @Override public DatabaseException buildException(@NotNull SQLException exception) { return new CheckViolationException(exception); }},
    EXCLUSION_VIOLATION {
        @Override public DatabaseException buildException(@NotNull SQLException exception) { return new ExclusionViolationException(exception); }},

    ACCESS_ERROR {
        @Override public DatabaseException buildException(@NotNull SQLException exception) { return new DatabaseAccessException(exception); }},

    CONCURRENCY_ERROR {
        @Override public DatabaseException buildException(@NotNull SQLException exception) { return new DatabaseConcurrencyException(exception); }},
    DEADLOCK {
        @Override public DatabaseException buildException(@NotNull SQLException exception) { return new DatabaseDeadlockException(exception); }},
    SCHEMA_DOES_NOT_EXISTS_ERROR {
        @Override public DatabaseException buildException(@NotNull SQLException exception) {
            final String msg = exception.getMessage();
            final int    p   = msg.lastIndexOf(' ');
            return new DatabaseSchemaDoesNotExistsException(exception, p == -1 ? msg : msg.substring(p + 1));
        }},
    LIMIT_EXCEEDED {
        @Override public DatabaseException buildException(@NotNull SQLException exception) { return new LimitExceededException(exception); }},

    EXECUTION_CANCELED {
        @Override public DatabaseException buildException(@NotNull SQLException exception) {
            return new DatabaseExecutionCanceledException(exception);
        }},
    UNSPECIFIED;

    //~ Instance Fields ..............................................................................................................................

    private boolean loggable = true;

    //~ Constructors .................................................................................................................................

    @SuppressWarnings("WeakerAccess")
    SQLExceptionType() {}

    @SuppressWarnings("WeakerAccess")
    SQLExceptionType(boolean loggable) {
        this.loggable = loggable;
    }

    //~ Methods ......................................................................................................................................

    /** Constructs a Database exception representing this type. */
    public DatabaseException buildException(@NotNull SQLException exception) {
        return new DatabaseUnspecifiedException(exception);
    }

    /**
     * Indicates whether this SQL Exception type will be logged as error.
     *
     * @return  True if the exception will be logged, false otherwise
     */
    public boolean isLoggable() {
        return loggable;
    }
}
