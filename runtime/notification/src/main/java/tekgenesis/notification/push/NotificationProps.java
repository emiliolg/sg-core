
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.notification.push;

import javax.inject.Named;

import tekgenesis.common.env.Properties;

/**
 * NotificationProps.
 */
@Named("notification")
public class NotificationProps implements Properties {

    //~ Instance Fields ..............................................................................................................................

    public String googleAuthKey         = null;
    public String googleNotificationUrl = "https://android.googleapis.com/gcm/";  // Default value from Google Doc
    public String googleResource        = "send";
}
