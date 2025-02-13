
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mail;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Resource;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.ToStringBuilder;
import tekgenesis.service.html.Html;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.collections.Colls.emptyIterable;
import static tekgenesis.common.collections.Colls.mkString;

/**
 * Simple HTML Mail.
 */
@SuppressWarnings("WeakerAccess")
public class Mail {

    //~ Instance Fields ..............................................................................................................................

    private Seq<String>          bcc       = emptyIterable();
    private String               body      = null;
    private Seq<String>          cc        = emptyIterable();
    private String               from      = null;
    private Seq<String>          replyTo   = emptyIterable();
    private final List<Resource> resources = new ArrayList<>();
    private DateTime             schedule  = null;

    private String      subject = null;
    private Seq<String> tos     = emptyIterable();

    //~ Methods ......................................................................................................................................

    /** Add Attachment. */
    public Mail addAttachment(@NotNull Resource resource) {
        resources.add(resource);
        return this;
    }

    /** BCC destinations. */
    public Mail bcc(@NotNull Seq<String> bs) {
        bcc = bs;
        return this;
    }

    /** CC destinations. */
    public Mail cc(@NotNull Seq<String> cs) {
        cc = cs;
        return this;
    }

    /** FROM origin. */
    public Mail from(@NotNull String f) {
        from = f;
        return this;
    }

    /** CC destinations. */
    public Mail replyTo(@NotNull Seq<String> r) {
        replyTo = r;
        return this;
    }

    /** TO destinations. */
    public Mail schedule(@NotNull DateTime s) {
        schedule = s;
        return this;
    }

    /** TO destinations. */
    public Mail to(@NotNull Seq<String> ts) {
        tos = ts;
        return this;
    }

    @Override public String toString() {
        final ToStringBuilder str = new ToStringBuilder("Mail");

        str.add("Subject", subject)
            .add("FROM", from)
            .add("REPLY TO", mkString(replyTo))
            .add("TO", mkString(tos))
            .add("CC", mkString(cc))
            .add("BCC", mkString(bcc))
            .add("Body", body)
            .add("Attachments", mkString(resources));

        return str.build();
    }

    /** Body Content. */
    public Mail withBody(@NotNull String b) {
        body = b;
        return this;
    }

    /** Body Content as Html. */
    public Mail withBody(@NotNull Html b)
        throws MailException
    {
        try {
            final StringWriter htmlWriter = new StringWriter();
            b.render(htmlWriter);
            body = htmlWriter.toString();
            return this;
        }
        catch (final IOException e) {
            throw new MailException(e);
        }
    }

    /** Set mail subject. */
    public Mail withSubject(@NotNull String s) {
        subject = s;
        return this;
    }

    /** BCCs. */
    public Seq<String> getBcc() {
        return bcc;
    }

    /** Mail body. */
    public String getBody() {
        return body;
    }

    /** CCs. */
    public Seq<String> getCc() {
        return cc;
    }

    /** Return send date. */
    public DateTime getDate() {
        return notNull(schedule, DateTime.current());
    }

    /** Check if it is a simple mail. */
    public boolean isSimple() {
        return true;
    }

    /** From. */
    public String getFrom() {
        return from;
    }

    /** reply To. */
    public Seq<String> getReplyTo() {
        return replyTo;
    }

    /** Mail attachments. */
    public List<Resource> getResources() {
        return resources;
    }

    /** The subject. */
    public String getSubject() {
        return subject;
    }

    /** TOs. */
    public Seq<String> getTos() {
        return tos;
    }

    /** Generate mail content. */
    void generate()
        throws MailException {}

    //~ Methods ......................................................................................................................................

    /** Check if the email is syntax right. */
    public static boolean checkEmailAddress(@NotNull String email) {
        return checkEmailAddresses(Colls.listOf(email));
    }

    /** Check if the email is syntax right. */
    public static boolean checkEmailAddresses(@NotNull String emails) {
        return checkEmailAddresses(Strings.split(emails, ';'));  // @ToDo se deberian des-escapear los ; en las partes
    }

    /** Check if the email is syntax right. */
    public static boolean checkEmailAddresses(@NotNull Seq<String> emails) {
        try {
            MailSender.checkEmailAddresses(emails);
            return true;
        }
        catch (final MailException e) {
            return false;
        }
    }
}  // end class SimpleMail
