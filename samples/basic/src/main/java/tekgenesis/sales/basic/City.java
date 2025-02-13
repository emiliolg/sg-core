
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.sales.basic.g.CityBase;

import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.collections.Colls.emptyIterable;
import static tekgenesis.common.collections.Colls.emptyList;

/**
 * User class for Model: City
 */
public class City extends CityBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public ImmutableList<Country> getCountries() {
        return emptyList();
    }

    @NotNull @Override public Country getCountry() {
        return ensureNotNull(Country.find(getStateProvinceCountryIso2()));
    }

    @NotNull @Override public String getCountryName() {
        return getCountry().getName();
    }

    @NotNull @Override public Seq<Integer> getPolygon() {
        return emptyIterable();
    }
}
