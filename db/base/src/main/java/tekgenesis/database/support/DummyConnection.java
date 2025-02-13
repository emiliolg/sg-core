
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.support;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * A Dummy connection with nearly everything not implemented.
 */
@SuppressWarnings("ClassWithTooManyMethods")
public abstract class DummyConnection implements Connection {

    //~ Methods ......................................................................................................................................

    @Override public void abort(Executor executor)
        throws SQLException {}

    @Override public void clearWarnings() {}
    @Override public abstract void close();
    @Override public void commit() {
        close();
    }
    @Override public Array createArrayOf(String typeName, Object[] elements) {
        throw ni();
    }
    @Override public Blob createBlob() {
        throw ni();
    }
    @Override public Clob createClob() {
        throw ni();
    }
    @Override public NClob createNClob() {
        throw ni();
    }
    @Override public SQLXML createSQLXML() {
        throw ni();
    }
    @Override public Statement createStatement() {
        throw ni();
    }
    @Override public Statement createStatement(int rsType, int rsConcurrency) {
        throw ni();
    }
    @Override public Statement createStatement(int rsType, int rsConcurrency, int rsHoldability) {
        throw ni();
    }
    @Override public Struct createStruct(String typeName, Object[] attributes) {
        throw ni();
    }
    @Override public String nativeSQL(String sql) {
        throw ni();
    }
    @Override public CallableStatement prepareCall(String sql) {
        throw ni();
    }
    @Override public CallableStatement prepareCall(String sql, int rsType, int rsc) {
        throw ni();
    }
    @Override public CallableStatement prepareCall(String sql, int rsType, int rsc, int rsh) {
        throw ni();
    }
    @Override public PreparedStatement prepareStatement(String sql) {
        throw ni();
    }
    @Override public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) {
        throw ni();
    }
    @Override public PreparedStatement prepareStatement(String sql, int[] columnIndexes) {
        throw ni();
    }
    @Override public PreparedStatement prepareStatement(String sql, String[] columnNames) {
        throw ni();
    }
    @Override public PreparedStatement prepareStatement(String sql, int rsType, int rsConcurrency) {
        throw ni();
    }
    @Override public PreparedStatement prepareStatement(String sql, int rsType, int rsc, int rsh) {
        throw ni();
    }
    @Override public void releaseSavepoint(Savepoint savepoint) {
        throw ni();
    }
    @Override public void rollback() {
        close();
    }
    @Override public void rollback(Savepoint savepoint) {
        throw ni();
    }
    @Override public <T> T unwrap(Class<T> iface) {
        throw ni();
    }
    @Override public boolean getAutoCommit() {
        return false;
    }
    @Override public void setAutoCommit(boolean autoCommit) {
        throw ni();
    }

    @Override public String getCatalog() {
        throw ni();
    }

    @Override public void setCatalog(String catalog) {
        throw ni();
    }

    @Override public Properties getClientInfo() {
        throw ni();
    }

    @Override public String getClientInfo(String name) {
        throw ni();
    }

    @Override public void setClientInfo(Properties properties) {
        throw ni();
    }

    @Override public void setClientInfo(String name, String value) {
        throw ni();
    }

    @Override public boolean isClosed() {
        return false;
    }

    @Override public boolean isValid(int timeout) {
        return !isClosed();
    }

    @Override public int getHoldability() {
        return 0;
    }

    @Override public void setHoldability(int holdability) {
        throw ni();
    }

    @Override public DatabaseMetaData getMetaData() {
        throw ni();
    }

    @Override public int getNetworkTimeout()
        throws SQLException
    {
        throw ni();
    }

    @Override public void setNetworkTimeout(Executor executor, int milliseconds)
        throws SQLException {}

    @Override public boolean isWrapperFor(Class<?> iface) {
        return false;
    }

    @Override public void setReadOnly(boolean readOnly) {
        throw ni();
    }

    @Override public Savepoint setSavepoint() {
        throw ni();
    }

    @Override public Savepoint setSavepoint(String name) {
        throw ni();
    }

    @Override public String getSchema()
        throws SQLException
    {
        throw ni();
    }

    @Override public void setSchema(String schema)
        throws SQLException {}

    @Override public int getTransactionIsolation() {
        return TRANSACTION_READ_COMMITTED;
    }

    @Override public void setTransactionIsolation(int level) {
        throw ni();
    }

    @Override public Map<String, Class<?>> getTypeMap() {
        throw ni();
    }

    @Override public void setTypeMap(Map<String, Class<?>> map) {
        throw ni();
    }

    @Override public SQLWarning getWarnings() {
        throw ni();
    }

    @Override public boolean isReadOnly() {
        return false;
    }

    private RuntimeException ni() {
        return new UnsupportedOperationException(getClass().getName());
    }
}  // end class DummyConnection
