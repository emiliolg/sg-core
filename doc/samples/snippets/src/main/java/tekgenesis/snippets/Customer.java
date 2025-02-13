
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.snippets;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.jetbrains.annotations.NotNull;

import tekgenesis.snippets.g.CustomerBase;

/**
 * User class for Model: Customer
 */
public class Customer extends CustomerBase implements Serializable {

    //~ Constructors .................................................................................................................................

    /** Constructor for Customer. */
    @JsonCreator public Customer(@JsonProperty("dni") int dni,
                                 @JsonProperty("cuit")
                                 @NotNull String cuit) {
        super(dni, cuit);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -2391050819617724926L;
}
