
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.console;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.env.Version;

import static tekgenesis.admin.Message.FOOTER_TEXT;
import static tekgenesis.admin.Message.NO_COMPONENTS_INFO;
import static tekgenesis.common.collections.Colls.mkString;

/**
 * User class for Form: ApplicationFooterForm
 */
public class ApplicationFooterForm extends ApplicationFooterFormBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the form is loaded. */
    @Override public void onLoad() {
        try {
            final Seq<String> components = Version.getInstance().getComponents().map(Version.ComponentInfo::toString);
            setFooterText(FOOTER_TEXT.label(mkString(components, " / ")));
        }
        catch (final Exception ignore) {
            setFooterText(NO_COMPONENTS_INFO.label());
        }
    }
}
