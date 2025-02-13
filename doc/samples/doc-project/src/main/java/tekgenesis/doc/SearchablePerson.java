
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.doc;

import org.jetbrains.annotations.NotNull;

import tekgenesis.doc.g.SearchablePersonBase;

/**
 * User class for Model: SearchablePerson
 */
public class SearchablePerson extends SearchablePersonBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public String getSomeFilterAndExtra() {
        return "someExtraValue";
    }
}
