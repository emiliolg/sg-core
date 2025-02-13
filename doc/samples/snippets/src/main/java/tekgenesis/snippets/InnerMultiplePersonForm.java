
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.snippets;

/**
 * User class for Form: InnerMultiplePersonForm
 */
public class InnerMultiplePersonForm extends InnerMultiplePersonFormBase {

    //~ Methods ......................................................................................................................................

    /**
     * Merges the edting person's addresses with the ui set values.
     *
     * @param  editingPerson  person to update
     */
    public void doMergeData(InnerMultiplePerson editingPerson) {
        editingPerson.getAddresses()
            .mergeMatching(getAddresses(),                          //
                (ci, v) -> ci.getAddressId().equals(v.getAddressId()),  //
                (ci, v) ->
                    ci.setStreet(v.getStreet())                     //
                    .setAddressId(v.getAddressId())                 //
                    .setZipCode(v.getZipCode())                     //
                    );
    }
    /**
     * Merges the edting person's addresses with the ui set values, failing if new items are found.
     *
     * @param  editingPerson  person to update
     */
    public void doMergeDataNoAdditions(InnerMultiplePerson editingPerson) {
        editingPerson.getAddresses()
            .mergeMatching(getAddresses(),                          //
                (ci, v) -> ci.getAddressId().equals(v.getAddressId()),  //
                (ci, v) -> {
                    if (ci.hasEmptyKey()) throw new RuntimeException();
                    else ci.setStreet(v.getStreet())                //
                    .setAddressId(v.getAddressId())                 //
                    .setZipCode(v.getZipCode());
                }
                //
                );
    }

    //~ Inner Classes ................................................................................................................................

    public class AddressesRow extends AddressesRowBase {}
}  // end class InnerMultiplePersonForm
