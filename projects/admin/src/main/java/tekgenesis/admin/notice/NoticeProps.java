
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.admin.notice;

import javax.inject.Named;

import tekgenesis.common.env.Properties;

/**
 * Notice properties.
 */
@Named("notice")
public class NoticeProps implements Properties {

    //~ Instance Fields ..............................................................................................................................

    public String mailFrom     = "admin@tekgenesis.com";
    public String mailSchedule = "9:00";
    public String mailSubject  = "Sui Generis Notice founds!";

    public String notificationMail = null;
}
