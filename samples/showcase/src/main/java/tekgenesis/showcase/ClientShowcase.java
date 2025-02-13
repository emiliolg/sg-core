
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

/**
 * User class for Form: ClientShowcase
 */
public class ClientShowcase extends ClientShowcaseBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        getIndicators().add().setIndicatorIcon("clock-o");
        getIndicators().add().setIndicatorIcon("arrows-h");
        getIndicators().add().setIndicatorIcon("dollar");
        getIndicators().add().setIndicatorIcon("credit-card");
        getIndicators().add().setIndicatorIcon("money");
        getIndicators().add().setIndicatorIcon("file-text-o");
        getIndicators().add().setIndicatorIcon("calendar-o");

        populate(getAddresses().add());
        populate(getAddresses().add());
        populate(getAddresses().add());
        populate(getAddresses().add());
        populate(getAddresses().add());
    }

    public void populate(AddressesRow add) {
        add.setAddressName("Pablo Celentano");
        add.setAddressLine("Tronador 3430");
        add.setAddressCity("Buenos Aires");
        add.setAddressStateCountry("Argentina");
    }

    //~ Inner Classes ................................................................................................................................

    public class AddressesRow extends AddressesRowBase {}

    public class IndicatorsRow extends IndicatorsRowBase {}
}
