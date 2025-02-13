
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import tekgenesis.form.configuration.MapConfiguration;

/**
 * User class for Form: AddressShowcaseForm
 */
public class AddressShowcaseForm extends AddressShowcaseFormBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the form is loaded. */
    @Override public void load() {
        final MapConfiguration conf = configuration(Field.MAP);
        conf.dimension(0, 320);
    }

    //~ Inner Classes ................................................................................................................................

    public class MapRow extends MapRowBase {}
}
