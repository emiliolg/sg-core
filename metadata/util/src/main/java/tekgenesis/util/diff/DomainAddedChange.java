
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util.diff;

import tekgenesis.repository.ModelRepository;

import static tekgenesis.common.core.Constants.PACKAGE_SPC;
import static tekgenesis.common.core.Strings.quoted;
import static tekgenesis.util.diff.DiffConstants.HAS_BEEN_ADDED;

/**
 * A {@link Change} that indicates that a new domain has been added to the {@link ModelRepository}.
 */
public class DomainAddedChange implements Change {

    //~ Instance Fields ..............................................................................................................................

    private final String domain;

    //~ Constructors .................................................................................................................................

    /** Constructor with the new domain name. */
    public DomainAddedChange(String domain) {
        this.domain = domain;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getMessage() {
        return PACKAGE_SPC + quoted(domain) + HAS_BEEN_ADDED;
    }
}
