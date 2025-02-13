
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.widget;

import java.util.EnumSet;

import static java.util.EnumSet.of;

/**
 * Mail Validation Type.
 */
@SuppressWarnings("NonJREEmulationClassesInClientCode")
public enum MailValidationType {

    //~ Enum constants ...............................................................................................................................

    ALL, SYNTAX, DOMAIN, ADDRESS, SYNTAX_DOMAIN, DOMAIN_ADDRESS;

    //~ Methods ......................................................................................................................................

    public boolean mustCheckAddress() {
        return address.contains(this);
    }
    public boolean mustCheckDomain() {
        return domain.contains(this);
    }

    public boolean mustCheckSyntax() {
        return syntax.contains(this);
    }

    //~ Static Fields ................................................................................................................................

    private static final EnumSet<MailValidationType> syntax  = of(ALL, SYNTAX, SYNTAX_DOMAIN);
    private static final EnumSet<MailValidationType> domain  = of(ALL, DOMAIN, SYNTAX_DOMAIN, DOMAIN_ADDRESS, ADDRESS);
    private static final EnumSet<MailValidationType> address = of(ALL, ADDRESS, DOMAIN_ADDRESS);
}
