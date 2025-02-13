
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mail;

import java.util.Arrays;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.authorization.User;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.form.ApplicationContext;
import tekgenesis.form.ApplicationContextImpl;
import tekgenesis.form.feedback.FeedbackEvent;
import tekgenesis.form.feedback.FeedbackEventImpl;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.common.env.context.Context.bind;
import static tekgenesis.mail.g.MailQueueTable.MAIL_QUEUE;
import static tekgenesis.persistence.Sql.deleteFrom;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.transaction.Transaction.runInTransaction;

@RunWith(Parameterized.class)
@SuppressWarnings("DuplicateStringLiteralInspection")
public class MailFeedbackReporterTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameterized.Parameter public String dbName = null;

    @Rule public DbRule db = new DbRule(DbRule.SG, DbRule.MAIL, DbRule.AUTHORIZATION) {
            @Override protected void before() {
                createDatabase(dbName);
                bind(ApplicationContext.class, ApplicationContextImpl.class);
            }
        };

    private MailFeedbackReporterProps props    = null;
    private MailFeedbackReporter      reporter = null;

    //~ Constructors .................................................................................................................................

    public MailFeedbackReporterTest() {}

    //~ Methods ......................................................................................................................................

    @Test public void testNoConfig() {
        props.from = null;
        props.to   = null;

        final FeedbackEvent event = createEvent();
        assertThat(reporter.isEnabled()).isFalse();
        reporter.reportFeedback(event);
        assertThat(MailQueue.list().list()).isEmpty();

        props.from = "test";
        props.to   = null;
        assertThat(reporter.isEnabled()).isFalse();
        reporter.reportFeedback(event);
        assertThat(MailQueue.list().list()).isEmpty();

        props.from = null;
        props.to   = "test2";
        assertThat(reporter.isEnabled()).isFalse();
        reporter.reportFeedback(event);
        assertThat(MailQueue.list().list()).isEmpty();

        props.from = "test-to@example.com";
        props.to   = "invalid-email";
        assertThat(reporter.isEnabled()).isTrue();
        reporter.reportFeedback(event);
        assertThat(MailQueue.list().list()).isEmpty();

        props.from = "invalid-email";
        props.to   = "test-to@example.com";
        assertThat(reporter.isEnabled()).isTrue();
        reporter.reportFeedback(event);
        assertThat(MailQueue.list().list()).isNotEmpty();  // it is valid to send an invalid "from" address
    }

    @Test public void testSimple() {
        final FeedbackEvent event = createEvent();
        assertThat(reporter.isEnabled()).isTrue();
        reporter.reportFeedback(event);
        final ImmutableList<MailQueue> list = MailQueue.list().list();
        assertThat(list).isNotEmpty();
        final MailQueue mail = list.getFirst().get();
        assertEvent(event, mail);
    }

    @Before public void setUp() {  //
        runInTransaction(() -> {
            props      = Context.getEnvironment().get(MailFeedbackReporterProps.class);
            props.from = "test-from@example.com";
            props.to   = "test-to@example.com";
            reporter   = new MailFeedbackReporter();
            deleteFrom(MAIL_QUEUE).execute();
            assertThat(selectFrom(MAIL_QUEUE).exists()).isFalse();
        });
    }

    private void assertEvent(@NotNull FeedbackEvent event, @NotNull MailQueue mail) {
        assertThat(mail.getTo()).isEqualTo(props.to);
        assertThat(mail.getFrom()).isEqualTo(props.from);
        assertThat(mail.getSubject()).contains(event.getApplication());
        assertThat(mail.getSubject()).contains(event.getType().name());
    }
    @NotNull private FeedbackEvent createEvent() {
        return new FeedbackEventImpl("test Summary",
            "Test descrption",
            FeedbackEvent.FeedbackType.EXCEPTION,
            "http://some.url",
            "Genesis",
            User.findOrCreate("test"),
            DateTime.current(),
            Arrays.asList("some history", "some other"),
            "my error");
    }

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class MailFeedbackReporterTest
