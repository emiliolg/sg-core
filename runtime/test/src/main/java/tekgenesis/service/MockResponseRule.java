
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.jetbrains.annotations.NotNull;
import org.junit.rules.TemporaryFolder;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.service.Headers;
import tekgenesis.common.service.Status;

import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.common.json.JsonMapping.json;
import static tekgenesis.common.util.Files.readLines;

/**
 * Simple mock response rule.
 */
public class MockResponseRule extends TemporaryFolder {

    //~ Instance Fields ..............................................................................................................................

    private MockResultHandler handler = null;

    private File output = null;

    //~ Methods ......................................................................................................................................

    /** Handle given result. */
    void handle(@NotNull Result<?> result) {
        prepare();
        handler.handle(result, new MockForwarder());
    }

    /** Return response body as json. */
    <T> T getBodyAsJson(@NotNull final Class<T> c) {
        try {
            return json().readValue(output, c);
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** Return response body as json seq. */
    <T> Seq<T> getBodyAsJsonSeq(@NotNull final Class<T> c) {
        try {
            final CollectionType type = TypeFactory.defaultInstance().constructCollectionType(List.class, c);
            final List<T>        list = json().readValue(output, type);
            return seq(list);
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** Return response body as string. */
    String getBodyAsString() {
        return readLines(output).mkString("\n");
    }

    /** Return response headers. */
    Headers getHeaders() {
        return handler.getHeaders();
    }

    /** Return response status. */
    Status getStatus() {
        return handler.getStatus();
    }

    private void prepare() {
        try {
            output  = newFile();
            handler = new MockResultHandler(new MockServerResponse(new FileOutputStream(output)));
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    //~ Inner Classes ................................................................................................................................

    private static class MockForwarder implements Forwarder {
        @Override public void forward(@NotNull String uri, @NotNull ResultHandler resultHandler, boolean routing) {}
        @Override public String url(@NotNull String uri) {
            return "";
        }
    }
}  // end class MockResponseRule
