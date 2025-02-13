
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.response;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

import tekgenesis.model.KeyMap;

/**
 * Org Units response. Contains current Org Unit and all available Org Units for the current user.
 */
@SuppressWarnings("FieldMayBeFinal")
public class OrgUnitsResponse implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private String currentOrgUnitKey;

    @SuppressWarnings("GwtInconsistentSerializableClass")
    private KeyMap orgUnits;

    //~ Constructors .................................................................................................................................

    /** Gwt constructor. */
    public OrgUnitsResponse() {
        currentOrgUnitKey = null;
        orgUnits          = null;
    }

    /** Create a OrgUnitsResponse with the current Org Unit key. */
    public OrgUnitsResponse(String orgUnitKey) {
        currentOrgUnitKey = orgUnitKey;
        orgUnits          = KeyMap.create();
    }

    //~ Methods ......................................................................................................................................

    /** Add an Org Unit to the response. */
    public void addOrgUnit(@NotNull final String ouName, @NotNull final String ouDescription) {
        orgUnits.put(ouName, ouDescription);
    }

    /** Returns the current Org Unit's key. */
    public String getCurrentOrgUnitKey() {
        return currentOrgUnitKey;
    }

    /** Returns the Org Units. */
    public KeyMap getOrgUnits() {
        return orgUnits;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 3331192698108230484L;
}  // end class OrgUnitsResponse
