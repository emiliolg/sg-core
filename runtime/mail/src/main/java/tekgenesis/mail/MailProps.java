
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mail;

import javax.inject.Named;

import tekgenesis.common.core.Constants;
import tekgenesis.common.env.Properties;

/**
 * Mail Configuration.
 */
@Named("mail")
@SuppressWarnings("WeakerAccess")
public class MailProps implements Properties {

    //~ Instance Fields ..............................................................................................................................

    public boolean auth;

    public String defaultPropScope = "";

    public String  domain      = Constants.TEKGENESIS_DOMAIN;
    public String  hostname    = null;
    public String  password    = null;
    public int     port;
    public String  protocol    = "smtp";
    public boolean requiredTls;
    public int     retryNro    = 3;
    public int     statsCache  = 100;
    public String  timeout     = null;
    public boolean tls;
    public String  username    = null;
    public boolean useSSl;
}
