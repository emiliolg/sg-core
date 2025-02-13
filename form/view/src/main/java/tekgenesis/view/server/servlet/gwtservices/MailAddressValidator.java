
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.servlet.gwtservices;

// ...............................................................................................................................
//
// (C) Copyright  2011/2016 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.*;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.logging.Logger;

/**
 * Service to make validations on an email address. Checks if the syntax is ok, if the host exists
 * and if the address exists too.
 */
public class MailAddressValidator {

    //~ Constructors .................................................................................................................................

    private MailAddressValidator() {}

    //~ Methods ......................................................................................................................................

    /** Check if domain exists. If there is no internet it will return true. */
    public static boolean doesDomainExist(String email) {
        final int idx = email.indexOf("@");
        if (idx < 0) return false;
        final String host = email.substring(idx + 1);
        try {
            return getMX(host);
        }
        catch (final Exception ignore) {
            return true;
        }
    }

    /**
     * @param   address  : email address to ve validated
     *
     * @return  true if it possibly exist.
     */
    @SuppressWarnings("OverlyLongMethod")
    public static boolean mailAddressExist(String address)
        throws ConnectException
    {
        // If the address does not contain an '@', it's not valid
        if (!address.contains("@")) return false;

        // Isolate the domain/machine name and get a list of mail exchangers
        final String       domain = address.substring(address.indexOf('@') + 1);
        final List<String> mxList;
        try {
            mxList = getMXList(domain);
        }
        catch (final CommunicationException ce) {
            logger().info(MAIL_VALIDATION + "got dns problems email=" + address + ". Exception caught: " + ce + " with message: " + ce.getMessage());
            return true;
        }
        catch (final NamingException ex) {
            logger().info(MAIL_VALIDATION + "got host naming exception for email=" + address + " - " + ex);
            return !(ex instanceof NameNotFoundException) && !(ex instanceof ServiceUnavailableException);
        }

        // if we do not find an mx, we assume the address doesn't exist
        if (mxList.isEmpty()) return false;

        // Only use the first mx for performance issues
        try {
            final Socket         skt = new Socket(mxList.get(0), 25);
            final BufferedReader rdr = new BufferedReader(new InputStreamReader(skt.getInputStream()));
            final BufferedWriter wtr = new BufferedWriter(new OutputStreamWriter(skt.getOutputStream()));

            final int res = hear(rdr);
            if (res != 220) throw new MailValidationException("Invalid header");
            say(wtr, "EHLO " + domain);

            final int res2 = hear(rdr);
            if (res2 != 250) throw new MailValidationException("Not ESMTP");

            // validate the sender address
            say(wtr, "MAIL FROM: <" + address + ">");
            final int res3 = hear(rdr);
            if (res3 != 250) throw new MailValidationException("Sender rejected. Code is: " + res3);

            say(wtr, "RCPT TO: <" + address + ">");
            final int res4 = hear(rdr);

            say(wtr, "RSET");
            hear(rdr);
            say(wtr, "QUIT");
            hear(rdr);

            rdr.close();
            wtr.close();
            skt.close();

            if (res4 == 550) {
                logger().info(MAIL_VALIDATION + "Received code: " + 550 + ". Didn't find any email address = '" + address + "'");
                return false;
            }
            if (res4 == 501) {
                logger().info(MAIL_VALIDATION + "Received code: " + 501 + ". Invalid email address= '" + address + "'");
                return false;
            }
            logger().debug(MAIL_VALIDATION + "Received code: " + res4 + ". Accepting email address = '" + address + "' as valid.");
        }
        catch (final ConnectException e) {
            logger().info(
                MAIL_VALIDATION + "remote mail validation error. Accepting email address anyway: email=" + address + " - Exception caught:  " +
                e.getClass() + ", message : " + e.getMessage());
            throw e;
        }
        catch (final Exception e) {
            logger().info(
                MAIL_VALIDATION + "remote mail validation error. Not accepting this email address: email=" + address + " - Exception caught:  " +
                e.getClass() + ", message : " + e.getMessage());
            return false;
        }
        return true;
    }  // end method mailAddressExist

    /** Check an email address syntax. True if it is correct. */
    public static boolean isEmailSyntaxValid(@Nullable String email) {
        return email != null && email.matches(MAIL_REGEX);
    }

    private static int hear(BufferedReader in)
        throws IOException
    {
        String line;
        int    res = 0;

        while ((line = in.readLine()) != null) {
            final String pfx = line.substring(0, 3);
            try {
                res = Integer.parseInt(pfx);
            }
            catch (final Exception ignore) {
                res = -1;
            }
            if (line.charAt(3) != '-') break;
        }
        return res;
    }

    private static Logger logger() {
        return Logger.getLogger(MailAddressValidator.class);
    }

    private static void say(BufferedWriter wr, String text)
        throws IOException
    {
        wr.write(text + "\r\n");
        wr.flush();
    }

    @SuppressWarnings("UseOfObsoleteCollectionType")
    private static boolean getMX(String hostName)
        throws NamingException
    {
        // Perform a DNS lookup for MX records in the domain
        final Hashtable<String, String> env = new Hashtable<>();
        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        final DirContext ictx = new InitialDirContext(env);
        try {
            final Attributes attrs = ictx.getAttributes(hostName, new String[] { "MX" });
            final Attribute  attr  = attrs.get("MX");

            // if we don't have an MX record, try the machine itself
            if ((attr == null) || (attr.size() == 0)) {
                final Attributes ictxAttributes = ictx.getAttributes(hostName, new String[] { "A" });
                final Attribute  a              = ictxAttributes.get("A");
                if (a == null || a.size() == 0) return false;
            }
            return true;
        }
        catch (final NameNotFoundException | ServiceUnavailableException ignore) {
            return false;
        }
    }

    private static List<String> getMXList(String hostName)
        throws NamingException
    {
        // Perform a DNS lookup for MX records in the domain
        // noinspection UseOfObsoleteCollectionType
        final Hashtable<String, String> env = new Hashtable<>();
        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        final DirContext ictx = new InitialDirContext(env);
        Attribute        attr = ictx.getAttributes(hostName, new String[] { "MX" }).get("MX");

        // if we don't have an MX record, try the machine itself
        if ((attr == null) || (attr.size() == 0)) {
            attr = ictx.getAttributes(hostName, new String[] { "A" }).get("A");
            if (attr == null) throw new NamingException("No match for name '" + hostName + "'");
        }

        final List<String>         res = new ArrayList<>();
        final NamingEnumeration<?> en  = attr.getAll();

        while (en.hasMore()) {
            final String   mailhost;
            final String   x = (String) en.next();
            final String[] f = x.split(" ");
            // THE fix *************
            if (f.length == 1) mailhost = f[0];
            else if (f[1].endsWith(".")) mailhost = f[1].substring(0, (f[1].length() - 1));
            else mailhost = f[1];
            // THE fix *************
            res.add(mailhost);
        }
        return res;
    }

    //~ Static Fields ................................................................................................................................

    private static final String MAIL_VALIDATION = "[MAIL VALIDATION] ";
    private static final String MAIL_REGEX      = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$";

    //~ Inner Classes ................................................................................................................................

    private static class MailValidationException extends Exception {
        private MailValidationException() {}
        public MailValidationException(String message) {
            super(message);
        }

        private static final long serialVersionUID = -4320412555909296357L;
    }
}  // end class MailAddressValidator
