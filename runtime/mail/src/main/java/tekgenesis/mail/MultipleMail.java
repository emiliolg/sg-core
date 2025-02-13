
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mail;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;

import static java.lang.String.format;
import static java.util.Collections.emptyMap;

import static tekgenesis.common.Predefined.ensureNotNull;

/**
 * Multiple mail sender.
 *
 * <p>The expected behaviour is : One email per destination (TO) will be sent. CC and BCC will be
 * maintain in each email.</p>
 */
@SuppressWarnings("WeakerAccess")
public class MultipleMail {

    //~ Instance Fields ..............................................................................................................................

    private final Mail                       mail;
    private Map<String, Map<String, Object>> multiArgs = emptyMap();

    //~ Constructors .................................................................................................................................

    /** .* */
    public MultipleMail(@NotNull Mail mail) {
        this.mail = mail;
    }

    //~ Methods ......................................................................................................................................

    /** Add multiple argument for TemplateMail.* */
    public MultipleMail addArguments(@NotNull final Map<String, Map<String, Object>> mArgs) {
        multiArgs = mArgs;
        return this;
    }

    /** Send multiple mails.* */
    public void send(@NotNull String config)
        throws MailException
    {
        if (!mail.isSimple() && mail.getTos().size() != multiArgs.size()) throw new IllegalStateException("MultimapArgument/TO size mismatch");

        for (final String t : mail.getTos().toList()) {
            if (!mail.isSimple())
                ((TemplateMail) mail).withArguments(ensureNotNull(multiArgs.get(t), format("Missed TO '%s' in multi map arguments", t)));

            MailSender.send(config, mail.to(Colls.listOf(t)));
        }
    }
}
