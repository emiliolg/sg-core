
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.doc.populated;

import org.jetbrains.annotations.NotNull;

/**
 * Sample Form.
 */
public class SampleForm extends SampleFormBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when populating a form instance. */
    @NotNull @Override public Object populate() {
        final String email = User.findEmailForUsername(getName());
        setEmail(email);
        return email;
    }
}
