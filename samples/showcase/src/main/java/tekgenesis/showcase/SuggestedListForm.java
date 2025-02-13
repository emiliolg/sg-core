
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import tekgenesis.form.FormTable;

/**
 * User class for Form: SuggestedListForm
 */
@SuppressWarnings("JavaDoc")
public class SuggestedListForm extends SuggestedListFormBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        final FormTable<SuggestedPersonsRow> table = getSuggestedPersons();
        SuggestedPerson.forEach(p -> table.add().populate(p));
    }

    //~ Inner Classes ................................................................................................................................

    public class SuggestedPersonsRow extends SuggestedPersonsRowBase {
        public void populate(SuggestedPerson p) {
            setPerson(p);
            setSuggest(p);
        }
    }
}
