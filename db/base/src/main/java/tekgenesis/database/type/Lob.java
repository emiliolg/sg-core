
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.type;

import java.io.CharArrayReader;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Clob;
import java.sql.SQLException;

import org.jetbrains.annotations.Nullable;

/**
 * A container for Lob Data with an specified Size.
 */
public class Lob {

    //~ Instance Fields ..............................................................................................................................

    private final boolean     clob;
    private final InputStream inputStream;
    private final Reader      reader;

    private final int size;

    //~ Constructors .................................................................................................................................

    private Lob(boolean clob, Reader reader, InputStream inputStream, int size) {
        this.clob        = clob;
        this.reader      = reader;
        this.inputStream = inputStream;
        this.size        = size;
    }

    //~ Methods ......................................................................................................................................

    /** Returns true if the Lob is a Clob. */
    public boolean isClob() {
        return clob;
    }

    /** If it is a Blob it will return the associated InputStream. */
    public InputStream getInputStream() {
        return inputStream;
    }

    /** If it is a Clob it will return the associated Reader. */
    public Reader getReader() {
        return reader;
    }

    /** Returns the Lob size. */
    public int getSize() {
        return size;
    }

    //~ Methods ......................................................................................................................................

    /** Create a Lob of Blob type. */
    public static Lob createBlob(InputStream stream, int size) {
        return new Lob(false, null, stream, size);
    }

    /** Create a Lob of Clob type. */
    public static Lob createClob(char[] chars) {
        return new Lob(true, new CharArrayReader(chars), null, chars.length);
    }

    /** Create a Lob of Clob type. */
    @Nullable public static Lob createClob(final Clob clob)
        throws SQLException
    {
        return clob == null ? null : createClob(clob.getCharacterStream(), (int) clob.length());
    }

    /** Create a Lob of Clob type. */
    public static Lob createClob(String s) {
        return s == null ? null : new Lob(true, new StringReader(s), null, s.length());
    }

    /** Create a Lob of Clob type. */
    public static Lob createClob(Reader reader, int size) {
        return new Lob(true, reader, null, size);
    }
}  // end class Lob
