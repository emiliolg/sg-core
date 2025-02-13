
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.util.Files.readLines;

class Curl {

    //~ Instance Fields ..............................................................................................................................

    private final String              method;
    private final Map<String, String> requestProps;
    private String                    result;
    private final String              textBody;

    private final String url;

    //~ Constructors .................................................................................................................................

    private Curl(String url, String method, Map<String, String> requestProps, String textBody) {
        this.url          = url;
        this.method       = method;
        this.requestProps = requestProps;
        this.textBody     = textBody;
        result            = "";
    }

    //~ Methods ......................................................................................................................................

    @Override public String toString() {
        return result;
    }

    @SuppressWarnings("EmptyCatchBlock")
    String invoke() {
        try {
            final HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
            con.setRequestMethod(method);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            for (final String key : requestProps.keySet())
                con.setRequestProperty(key, requestProps.get(key));

            if (isNotEmpty(textBody)) writeRequest(con, textBody);

            final InputStream stream = con.getInputStream();

            if (stream != null) {
                // noinspection ObjectToString
                result = toString(readLines(new InputStreamReader(stream)));
                stream.close();
            }
        }
        catch (final IOException e) {
            result = "";
        }

        return result;
    }

    @SuppressWarnings("EmptyCatchBlock")
    Reader reader() {
        try {
            final HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod(method);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            for (final String key : requestProps.keySet())
                con.setRequestProperty(key, requestProps.get(key));

            final InputStream stream = con.getInputStream();

            if (stream != null)
            // noinspection ObjectToString
            return new InputStreamReader(stream);
        }
        catch (final IOException e) {}

        return null;
    }

    private String toString(final List<String> lines) {
        final StringBuilder builder = new StringBuilder();
        for (final String string : lines)
            builder.append(string).append(' ');
        return builder.toString();
    }

    //~ Methods ......................................................................................................................................

    static Curl get(String url, Map<String, String> requestProps) {
        return new Curl(url, "GET", requestProps, "");
    }

    static Curl post(String url, Map<String, String> requestProps, String textBody) {
        return new Curl(url, "POST", requestProps, textBody);
    }

    // Writes a request to a connection
    @SuppressWarnings("EmptyCatchBlock")
    private static void writeRequest(HttpsURLConnection connection, String textBody) {
        try {
            final BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            wr.write(textBody);
            wr.flush();
            wr.close();
        }
        catch (final IOException e) {}
    }
}  // end class Curl
