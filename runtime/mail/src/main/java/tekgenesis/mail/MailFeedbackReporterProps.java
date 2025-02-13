
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

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.env.Properties;

/**
 * The Mail Reporter properties.
 */
@Named("mail-feedback-reporter")
@SuppressWarnings("WeakerAccess")
public class MailFeedbackReporterProps implements Properties {

    //~ Instance Fields ..............................................................................................................................

    @Nullable public String from = null;
    @Nullable public String to   = null;
}
