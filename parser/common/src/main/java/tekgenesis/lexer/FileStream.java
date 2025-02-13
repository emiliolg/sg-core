
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lexer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.util.CharArraySequence;

/**
 * This class implements the {@link CharStream} class based on a {@link File}.
 */
public class FileStream extends CharSequenceStream {

    //~ Constructors .................................................................................................................................

    /**
     * Creates an instance based on a File.
     *
     * @throws  IOException  if the files does not exists or an IO Error is raised while reading
     */
    public FileStream(@NotNull File file)
        throws IOException
    {
        super(file.getPath(), load(file, null));
    }
    /**
     * Creates an instance based on a File.
     *
     * @throws  IOException  if the files does not exists or an IO Error is raised while reading
     */
    public FileStream(@NotNull String file)
        throws IOException
    {
        super(file, load(new File(file), null));
    }

    /**
     * Creates an instance based on a File with a given encoding.
     *
     * @throws  IOException  if the files does not exists or an IO Error is raised while reading
     */
    public FileStream(@NotNull File file, @NotNull String encoding)
        throws IOException
    {
        super(file.getPath(), load(file, encoding));
    }

    //~ Methods ......................................................................................................................................

    private static CharSequence load(@NotNull File file, @Nullable String encoding)
        throws IOException
    {
        final FileInputStream fis = new FileInputStream(file);
        try(InputStreamReader isr = encoding == null ? new InputStreamReader(fis) : new InputStreamReader(fis, encoding)) {
            final char[] data   = new char[(int) file.length()];
            final int    length = isr.read(data);
            return new CharArraySequence(data, 0, length);
        }
    }
}  // end class FileStream
