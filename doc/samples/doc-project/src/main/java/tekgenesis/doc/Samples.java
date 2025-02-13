
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.doc;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Resource;
import tekgenesis.common.env.context.Context;
import tekgenesis.form.Action;
import tekgenesis.index.SearchResult;
import tekgenesis.mail.Mail;
import tekgenesis.mail.MailException;
import tekgenesis.mail.MailSender;
import tekgenesis.mail.TemplateMail;
import tekgenesis.persistence.ResourceHandler;

import static tekgenesis.common.media.MediaType.APPLICATION_OCTET_STREAM;
import static tekgenesis.doc.g.CarSearcherBase.CAR_SEARCHER;

/**
 * User class for Form: Samples
 */
public class Samples extends SamplesBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action changed() {
        return actions.getDefault();
    }

    @NotNull @Override public Action click(@NotNull Field field) {
        return actions.getDefault();
    }

    @NotNull @Override public Action create() {
        return actions.getDefault();
    }

    @NotNull @Override public Action doPrint() {
        return actions.getDefault();
    }

    @NotNull @Override public Action doValidate() {
        return actions.getDefault();
    }

    @NotNull @Override public Car find() {
        return new Car();
    }

    @NotNull @Override public Action navigateThere() {
        return actions.getDefault();
    }

    /**
     * send Mail method.
     *
     * @throws  MailException  if mail fails
     */
    public void sendMail()
        throws MailException
    {
        // #simpleEmail
        // Simple Email
        final Mail simpleMail = new Mail().from("tekg@tekgenesis.com")
                                .to(Colls.listOf("test1@testmail.com"))
                                .withSubject("Test Mail")
                                .withBody(
                "<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' />" +
                "<title>El pedido ha sido procesado!</title>" +
                "</head>" +
                "<body><p>El pedido fue exitoso!!</p></body>" +
                "</html>");

        MailSender.send(simpleMail);
        // #simpleEmail
    }

    /**
     * Send Template Mail.
     *
     * @throws  MailException  if mail fails
     */
    public void sendTemplatMail()
        throws MailException
    {
        final ResourceHandler rh = Context.getSingleton(ResourceHandler.class);
        // #templateEmail

        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream("mail.mustache");

        final Resource.Factory factory = rh.create();
        final Resource         mail    = factory.upload("templateMailTest", APPLICATION_OCTET_STREAM.getMime().getMime(), inputStream);

        final Map<String, Object> arguments = new HashMap<>();
        arguments.put("user", "Username");
        arguments.put("nro", "1234");

        final TemplateMail templateMail = TemplateMail.mustache(mail);

        templateMail.from("tek1@tekgenesis.com").to(Colls.listOf("t1@testmail.com")).withSubject("Test Mail Template");

        templateMail.withArguments(arguments);

        MailSender.send(templateMail);
        // #templateEmail
    }

    //~ Methods ......................................................................................................................................

    /**
     * suggest method.
     *
     * @param   query  queryString to search
     *
     * @return  the matching items
     */
    @NotNull public static Iterable<Car> suggest(String query) {
        final ImmutableList<String> validValues = ImmutableList.fromArray(new String[] { "corolla", "pryus" });
        // #complexFilter
        final List<SearchResult> result = CAR_SEARCHER.search(CAR_SEARCHER.YEAR.ne(ANY_YEAR).and(CAR_SEARCHER.MODEL.in(validValues).must()));
        // #complexFilter
        return new ArrayList<>(result.size());
    }

    //~ Static Fields ................................................................................................................................

    private static final int ANY_YEAR = 1999;

    //~ Inner Classes ................................................................................................................................

    public class ArgentinaRow extends ArgentinaRowBase {}

    public class ChartColumnRow extends ChartColumnRowBase {}

    public class SomeRow extends SomeRowBase {}

    public class TableIdRow extends TableIdRowBase {}
}  // end class Samples
