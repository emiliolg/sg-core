
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.doc;

/**
 * Sample Form class.
 */
public class SampleForm extends SampleFormBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the form is loaded. */
    @Override public void load() {
        setEmail(context.getUser().getEmail());  // Use current logged User from context to specified a value
    }
}
