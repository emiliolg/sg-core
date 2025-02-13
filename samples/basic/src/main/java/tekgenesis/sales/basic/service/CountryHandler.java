
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic.service;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;

import static tekgenesis.persistence.Sql.select;
import static tekgenesis.sales.basic.g.CountryTable.COUNTRY;

/**
 * User class for Handler: TransactionHandler
 */
public class CountryHandler extends CountryHandlerBase {

    //~ Constructors .................................................................................................................................

    CountryHandler(@NotNull Factory factory) {
        super(factory);
    }

    //~ Methods ......................................................................................................................................

    /** Invoked for route "/country/$id". */
    @NotNull @Override public Result<Void> delete(@NotNull String iso2, boolean success) {
        final tekgenesis.sales.basic.Country country = tekgenesis.sales.basic.Country.find(iso2);
        if (country == null) return notFound();
        country.delete();
        return result(success);
    }

    /** Invoked for route "/country/$id". */
    @NotNull @Override public Result<Country> get(@NotNull String iso2) {
        final tekgenesis.sales.basic.Country country = tekgenesis.sales.basic.Country.find(iso2);
        if (country == null) return notFound();
        return ok(countryFor(country));
    }

    /** Invoked for route "/country". */
    @NotNull @Override public Result<Seq<Country>> list(boolean success) {
        final Seq<Country> result = select(COUNTRY.ISO2, COUNTRY.NAME).as(Country.class).from(COUNTRY).list();
        if (!success) return badRequest();
        return ok(result);
    }

    /** Invoked for route "/country". */
    @NotNull @Override public Result<Void> post(@NotNull Country body, boolean success) {
        final tekgenesis.sales.basic.Country country = tekgenesis.sales.basic.Country.create(body.getIso2());
        country.setName(body.getName());
        country.insert();
        return result(success);
    }

    @NotNull @Override public Result<Country> postWithException(@NotNull Country body) {
        final tekgenesis.sales.basic.Country country = tekgenesis.sales.basic.Country.create(body.getIso2());
        country.setName(body.getName());
        country.insert();
        throw new RuntimeException("Exception after insert");
    }

    /** Invoked for route "/country/$id". */
    @NotNull @Override public Result<Void> put(@NotNull String iso2, @NotNull Country body, boolean success) {
        final tekgenesis.sales.basic.Country country = tekgenesis.sales.basic.Country.find(iso2);
        if (country == null) return notFound();
        country.setName(body.getName());
        country.update();
        return result(success);
    }

    /** Invoked for route "/country/getAndInsert". */
    @NotNull @Override public Result<Country> getAndInsert(boolean success) {
        final tekgenesis.sales.basic.Country country = tekgenesis.sales.basic.Country.create("aa");
        country.setName("AA Nation");
        country.insert();  // Get method transactions should rollback if result is error
        return success ? ok(countryFor(country)) : badRequest();
    }

    /** Method to allow testing fail to commit transaction if result is not success. */
    private <T> Result<T> result(boolean success) {
        return success ? ok() : badRequest();
    }

    //~ Methods ......................................................................................................................................

    @NotNull private static Country countryFor(final tekgenesis.sales.basic.Country country) {
        final Country result = new Country();
        result.setIso2(country.getIso2());
        result.setName(country.getName());
        return result;
    }
}  // end class CountryHandler
