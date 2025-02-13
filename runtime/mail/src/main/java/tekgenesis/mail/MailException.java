
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mail;

/**
 * Mail Exception.
 */
public class MailException extends RuntimeException {

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public MailException(Throwable e) {
        super(e);
    }

    /** Default constructor. */
    public MailException(String msg) {
        super(msg);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -7912468847512000610L;
}
