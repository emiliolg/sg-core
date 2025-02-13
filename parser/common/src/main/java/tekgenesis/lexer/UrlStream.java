
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lexer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.util.CharArraySequence;
import tekgenesis.common.util.Files;

/**
 * This class implements the {@link CharStream} class based on a {@link File}.
 */
public class UrlStream extends CharSequenceStream {

    //~ Constructors .................................................................................................................................

    /**
     * Creates an instance based on a URL.
     *
     * @throws  IOException  if the files does not exists or an IO Error is raised while reading
     */
    public UrlStream(@NotNull URL url)
        throws IOException
    {
        this(url, Charset.defaultCharset().name());
    }

    /**
     * Creates an instance based on a File with a given encoding.
     *
     * @throws  IOException  if the files does not exists or an IO Error is raised while reading
     */
    @SuppressWarnings("WeakerAccess")
    public UrlStream(@NotNull URL url, @NotNull String encoding)
        throws IOException
    {
        super(url.getPath(), load(url, encoding));
    }

    //~ Methods ......................................................................................................................................

    private static CharSequence load(@NotNull URL url, @NotNull String encoding)
        throws IOException
    {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        Files.copy(url.openStream(), out);
        return new CharArraySequence(out.toString(encoding));
    }
}  // end class UrlStream
