
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.logging.Logger;
import tekgenesis.database.exception.LimitExceededException;

import static java.lang.String.format;

/**
 * Handler of a ResultSet.
 *
 * @param  <T>  Type of expected result
 */
public interface ResultHandler<T> {

    //~ Methods ......................................................................................................................................

    /** Extract the result set process it and return some value. */
    @Nullable T handle(@NotNull ResultSetSupplier stmt)
        throws SQLException;

    //~ Methods ......................................................................................................................................

    /** A Handler for list of rows of a given row mapper. */
    static <T> ResultHandler<ImmutableList<T>> listRowHandler(RowMapper<T> mapper) {
        return new ListResultHandler<>(mapper);
    }

    /** A Handler for a single row result of an arbitrary type. */
    static <T> ResultHandler<T> reflectiveHandler(Class<T> expectedType) {
        return singleRowHandler(RowMapper.reflectiveMapper(expectedType));
    }

    /** A Handler for single row of a given row mapper. */
    static <T> ResultHandler<T> singleRowHandler(RowMapper<T> mapper) {
        return stmt -> {
                   try(final ResultSet rs = stmt.getResultSet()) {
                       return rs.next() ? mapper.mapRow(rs) : null;
                   }
               };
    }

    //~ Inner Interfaces .............................................................................................................................

    interface ResultSetSupplier {
        /** Supply a ResultSet. */
        @NotNull ResultSet getResultSet()
            throws SQLException;
    }

    //~ Inner Classes ................................................................................................................................

    class ListResultHandler<T> implements ResultHandler<ImmutableList<T>> {
        private final RowMapper<T> mapper;

        private ListResultHandler(RowMapper<T> mapper) {
            this.mapper = mapper;
        }

        @Nullable @Override public ImmutableList<T> handle(@NotNull ResultSetSupplier stmt)
            throws SQLException
        {
            try(final ResultSet rs = stmt.getResultSet()) {
                final ImmutableList.Builder<T> result = ImmutableList.builder();
                while (rs.next()) {
                    checkSize(result.size());
                    result.add(mapper.mapRow(rs));
                }
                return result.build();
            }
        }
        private static void checkSize(int size) {
            switch (size) {
            case WARNING_VALUE:
                logger.warning(SIZE_MESSAGE, WARNING_VALUE);
                break;
            case ERROR_VALUE:
                logger.error(SIZE_MESSAGE, ERROR_VALUE);
                break;
            case FATAL_VALUE:
                throw new LimitExceededException(format(SIZE_MESSAGE, FATAL_VALUE));
            }
        }

        private static final int    WARNING_VALUE = 100_000;
        private static final int    ERROR_VALUE   = 500_000;
        private static final int    FATAL_VALUE   = 1_000_000;
        private static final String SIZE_MESSAGE  = "Size of Result List exceeds %d";
        private static final Logger logger        = Logger.getLogger(ListResultHandler.class);
    }
}
