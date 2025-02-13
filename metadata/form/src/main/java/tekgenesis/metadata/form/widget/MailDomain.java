
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.widget;

import tekgenesis.model.KeyMap;

import static tekgenesis.common.collections.ImmutableList.fromArray;

public enum MailDomain {

    //~ Enum constants ...............................................................................................................................

    HOTMAIL_COM("hotmail.com"), GMAIL_COM("gmail.com"), YAHOO_COM_AR("yahoo.com.ar"), MAIL_COM("mail.com"), HOTMAIL_COM_AR("hotmail.com.ar"),
    LIVE_COM_AR("live.com.ar"), YAHOO_COM("yahoo.com"), FIBERTEL_COM_AR("fibertel.com.ar"), LIVE_COM("live.com"), HOTMAIL_ES("hotmail.es"),
    ARNET_COM_AR("arnet.com.ar"), SPEEDY_COM_AR("speedy.com.ar"), CIUDAD_COM_AR("ciudad.com.ar"), OUTLOOK_COM("outlook.com");

    //~ Instance Fields ..............................................................................................................................

    private final String domain;

    //~ Constructors .................................................................................................................................

    MailDomain(String domain) {
        this.domain = domain;
    }

    //~ Methods ......................................................................................................................................

    @Override public String toString() {
        return getDomain();
    }

    public String getDomain() {
        return domain;
    }

    //~ Methods ......................................................................................................................................

    public static KeyMap domainsKeyMap() {
        return KeyMap.fromValues(fromArray(values()).toStrings());
    }
}
