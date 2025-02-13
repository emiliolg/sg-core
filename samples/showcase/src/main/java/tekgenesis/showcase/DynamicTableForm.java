
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
 * User class for Form: DynamicTableForm
 */
public class DynamicTableForm extends DynamicTableFormBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        // fake some data for tests
        final FormTable<ClientAddressesRow> table = getClientAddresses();

        final ClientAddressesRow row = table.add();
        row.setCountry("Argentina");
        row.setState("Buenos Aires");
        row.setCity("Pilar");
        row.setStreet("Av. Cayetano Veliera 3025");
        row.setZip("1929");
    }

    //~ Inner Classes ................................................................................................................................

    public class ClientAddressesRow extends ClientAddressesRowBase {}
}
