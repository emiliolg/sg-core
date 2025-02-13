
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.es;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runners.model.Statement;

import tekgenesis.common.core.DateTime;

/**
 * Test ElasticSearch base wrapper.
 */
public class ElasticSearchTest {

    //~ Instance Fields ..............................................................................................................................

    private ElasticSearch es = null;

    @Rule public TestRule r = (base, description) ->
                              new Statement() {
                @Override public void evaluate()
                    throws Throwable
                {
                    final File tempdir;
                    try {
                        tempdir = File.createTempFile("es-temp", Long.toString(System.nanoTime()));
                        tempdir.delete();
                        tempdir.mkdir();
                    }
                    catch (final IOException e) {
                        throw new RuntimeException(e);
                    }

                    try(final ElasticSearch elasticSearch = ElasticSearch.createEmbeddedServer(tempdir, 9200)) {
                        es = elasticSearch;
                        base.evaluate();
                    }
                    finally {
                        if (es != null) es.close();
                        es = null;
                    }
                }
            };

    //~ Static Fields ................................................................................................................................

    /*@Test public void basicOperations() {
     *  final DateTime aTime = DateTime.current();
     * es.indexDocument(MESSAGE_INDEX, "1", new Message("pepe", DateTime.EPOCH, "World Origin"));
     * es.indexDocument(MESSAGE_INDEX, "3", new Message("pepe", DateTime.EPOCH, "The World Origin"));
     * es.indexDocument(MESSAGE_INDEX, "2", new Message("pepe", aTime, "Hello World"));
     * es.indexDocument(MESSAGE_INDEX, "4", new Message("pepe", aTime, "World Hello"));
     *
     * final Message document = assertNotNull(es.getDocument(MESSAGE_INDEX, Message.class, "2"));
     * assertThat(document.dateTime).isEqualTo(aTime);
     * assertThat(document.user).isEqualTo("pepe");
     * assertThat(document.message).isEqualTo("Hello World");
     * es.refresh();
     * assertThat(es.search(MESSAGE_INDEX, Message.class, prefixQuery("user", "pep"))).hasSize(4);
     *assertThat(es.search(MESSAGE_INDEX, Message.class, matchPhrasePrefixQuery("message", "Hello
     * World").fuzziness("AUTO").slop(2))).hasSize(2);
     *}*/

    /*@Test public void indexDocument()
     *  throws InterruptedException
     * {
     *  es.createIndex("some-index-name", "SomeIndexType", "", "some-index-alias");
     *
     * es.refresh();
     *
     * assertThat(es.aliasExists("some-index-alias")).isTrue();
     *
     * es.deleteIndex("some-index-name");
     *
     *assertThat(es.aliasExists("some-index-alias")).isFalse();
     *}*/

    public static final String MESSAGE_INDEX = "my_doc";

    //~ Inner Classes ................................................................................................................................

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    static class Message {
        private final DateTime dateTime;

        private final String message;
        private final String user;

        @JsonCreator public Message(@JsonProperty("user") String user,
                                    @JsonProperty("dateTime") DateTime dateTime,
                                    @JsonProperty("message") String message) {
            this.user     = user;
            this.dateTime = dateTime;
            this.message  = message;
        }

        @Override public String toString() {
            return user + ":" + message + ":" + dateTime;
        }
    }
}  // end class ElasticSearchTest
