
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.invites;

import java.io.*;
import java.text.DateFormat;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Resource;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.media.Mime;
import tekgenesis.form.Action;
import tekgenesis.mail.Mail;
import tekgenesis.mail.MailException;
import tekgenesis.mail.MailProps;
import tekgenesis.mail.MailSender;
import tekgenesis.persistence.ResourceHandler;
import tekgenesis.report.fo.Fo;
import tekgenesis.report.fo.FopBuildException;
import tekgenesis.report.fo.FopBuilder;
import tekgenesis.report.fo.components.FoB;
import tekgenesis.report.fo.components.TableB;
import tekgenesis.report.fo.components.TableCellB;
import tekgenesis.report.fo.components.TableRowB;
import tekgenesis.report.fo.document.PageSequenceBuilder;
import tekgenesis.report.fo.document.SimpleDocument;
import tekgenesis.report.fo.document.SimplePageMasterBuilder;

import static tekgenesis.common.env.context.Context.getEnvironment;
import static tekgenesis.report.fo.components.BlockB.block;
import static tekgenesis.report.fo.components.ExternalGraphicB.externalGraphic;
import static tekgenesis.report.fo.document.PageSequenceBuilder.pageSequence;
import static tekgenesis.report.fo.document.SimpleDocumentBuilder.simpleDocument;
import static tekgenesis.report.fo.document.SimplePageMasterBuilder.simplePageMaster;

/**
 * User class for Form: InviteGenerator
 */
public class InviteGenerator extends InviteGeneratorBase {

    //~ Instance Fields ..............................................................................................................................

    private final MailProps mailProps;

    //~ Constructors .................................................................................................................................

    /**  */
    public InviteGenerator() {
        mailProps          = new MailProps();
        mailProps.hostname = "smtp.gmail.com";
        // mailProps.port     = 587;
        mailProps.tls      = true;
        mailProps.auth     = true;
        mailProps.username = "";
        mailProps.password = "";
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action generateInvite() {
        final ByteArrayOutputStream attachment = createInvitePDF();
        sendInviteMail(new ByteArrayInputStream(attachment.toByteArray()));
        return actions.getDefault();
    }

    @NotNull private ByteArrayOutputStream createInvitePDF() {
        // #createInvitePdf
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        final FoB header = block().children(
                TableB.table()
                      .rows(
                          TableRowB.tableRow()
                                   .children(
                                       TableCellB.tableCell()
                                                       .child(block().content("You are invited to " + getEvent()).family(FONT_FACE).size("10")),
                                             TableCellB.tableCell()
                                                             .child(
                                                           block().content("On " + getTime().format(DateFormat.SHORT, DateFormat.SHORT))
                                                              .family(FONT_FACE)
                                                              .size("10")))));

        final FoB                 content      = block().children(
                externalGraphic().src(LOGO_URL)
                                 .width("45mm")
                                 .height("45mm")
                                 .alignRight()
                                 .contentHeight(Fo.SCALE_TO_FIT)
                                 .contentWidth(Fo.SCALE_TO_FIT));
        final FoB                 footer       = block();
        final PageSequenceBuilder pageSequence = pageSequence().withName(MASTER_NAME)
                                                 .addChildren(block().children(header).marginBottom("5px"),
                block().children(content).marginBottom("3px"),
                block().children(footer));

        final SimplePageMasterBuilder pageMaster = simplePageMaster().masterName(MASTER_NAME)
                                                   .marginBottom("10mm")
                                                   .marginLeft("10mm")
                                                   .marginRight("10mm")
                                                   .marginTop("10mm");
        final SimpleDocument          document   = simpleDocument().withPageSequence(pageSequence).withSimplePageMaster(pageMaster).build();

        try {
            FopBuilder.build(document, out);
        }
        catch (final FopBuildException e) {
            throw new RuntimeException("PDF could not be build.");
        }
        finally {
            try {
                out.close();
            }
            catch (final IOException ignore) {}
        }
        // #createInvitePdf
        return out;
    }  // end method createInvitePDF

    private void initMailConfiguration() {
        getEnvironment().put(CONF_NAME, mailProps);
    }

    // #sendInviteMail
    private void sendInviteMail(@NotNull InputStream invite) {
        final ResourceHandler resourceHandler = Context.getContext().getSingleton(ResourceHandler.class);
        final Resource        attachment      = resourceHandler.create().upload("Invitation.pdf", Mime.APPLICATION_PDF.getMime(), invite);

        initMailConfiguration();

        final Seq<String> recipients = Colls.map(getGuests(), GuestsRowBase::getMail);

        final Mail simpleMail = new Mail().from(getHostMail())
                                .to(recipients)
                                .withSubject("Invite")
                                .withBody(
                    "<html>" +
                    "<head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' />" +
                    "<title>See Attached Invitation!</title>" +
                    "</head>" +
                    "<body><p>See Attached Invitation!</p></body>" +
                    "</html>").addAttachment(attachment);

        try {
            MailSender.send(CONF_NAME, simpleMail);
        }
        catch (final MailException e) {
            // do something
        }
    }
    // #sendInviteMail

    //~ Static Fields ................................................................................................................................

    private static final String CONF_NAME   = "testConf";
    private static final String FONT_FACE   = "Garamond";
    private static final String LOGO_URL    = "http://vignette1.wikia.nocookie.net/lego/images/6/69/Superman_CGI.png";
    private static final String MASTER_NAME = "pdfContent";

    //~ Inner Classes ................................................................................................................................

    public class GuestsRow extends GuestsRowBase {}
}  // end class InviteGenerator
