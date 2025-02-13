
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.ix;

import java.io.*;

import tekgenesis.common.core.Constants;

/**
 * User: emilio Date: 4/12/11 Time: 20:09
 */
class IxDecode {

    //~ Constructors .................................................................................................................................

    private IxDecode() {}

    //~ Methods ......................................................................................................................................

    /** Decode a File from Ideafix format to UTF-8. */
    public static void main(String[] args)
        throws IOException
    {
        for (final String arg : args)
            decode(arg);
    }

    private static void decode(String name)
        throws IOException
    {
        final InputStreamReader in = new InputStreamReader(new FileInputStream(name), IxCharset.IDEAFIX_CHARSET);
        final BufferedReader    is = new BufferedReader(in);
        final PrintWriter       pw = new PrintWriter(new OutputStreamWriter(System.out, Constants.UTF8));

        String line;
        while ((line = is.readLine()) != null)
            pw.println(line);
        pw.close();
    }
}
