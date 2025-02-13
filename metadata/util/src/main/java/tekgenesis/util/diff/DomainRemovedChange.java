
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util.diff;

import tekgenesis.common.core.Constants;

/**
 * A Change to indicate that a Domain has been removed from the @ModelRepository.
 */
public class DomainRemovedChange implements Change {

    //~ Instance Fields ..............................................................................................................................

    private final String domain;

    //~ Constructors .................................................................................................................................

    /** Constructor with the name of the Domain removed. */
    public DomainRemovedChange(String domain) {
        this.domain = domain;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getMessage() {
        return Constants.PACKAGE_SPC + domain + DiffConstants.HAS_BEEN_REMOVED;
    }
}
