
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;
import tekgenesis.form.configuration.SubformConfiguration;

import static tekgenesis.common.Predefined.ensureNotNull;

/**
 * Subform Showcase form class.
 */
@SuppressWarnings("WeakerAccess")
public class SubformsShowcase extends SubformsShowcaseBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action changeSubForm() {
        final double random = Math.random();
        setLast("Last" + random);
        final AddressForm address = isDefined(Field.ADDRESS) ? ensureNotNull(getAddress(), "Adress not found") : createAddress();
        address.setStreet("Patricias Argentinas " + random);
        address.setCountry("SYNC: " + random);
        setHideSubform(!isHideSubform());
        return actions.getDefault();
    }

    @Override public void load() {
        Address.forEach(address -> {
            final TableRow row = getTable().add();
            row.setDescription(address.getStreet());
            final String pk = address.keyAsString();
            row.createAddressInSubform(pk);
            row.createAddressInSubform2(pk);
        });
    }

    //~ Inner Classes ................................................................................................................................

    public class TableRow extends TableRowBase {
        @NotNull @Override public Action hideSubform() {
            final SubformConfiguration configuration = configuration(Field.ADDRESS_IN_SUBFORM2);
            configuration.setVisible(false);
            return actions.getDefault();
        }
    }
}
