
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

import tekgenesis.snippets.g.PersonTypeBase;

/**
 * User class for Model: PersonType
 */
public class PersonType extends PersonTypeBase implements Serializable {

    //~ Constructors .................................................................................................................................

    /** Constructor for PersonType. */
    @JsonCreator public PersonType(@JsonProperty("dni") int dni) {
        super(dni);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -3155990918216082733L;
}
