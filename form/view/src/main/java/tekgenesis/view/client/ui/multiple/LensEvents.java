
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.multiple;

/**
 * Lens Event utility class.
 */
public class LensEvents {

    //~ Constructors .................................................................................................................................

    private LensEvents() {}

    //~ Static Fields ................................................................................................................................

    public static final LensEvent REFRESH = new LensEvent() {
            @Override public boolean refresh() {
                return true;
            }
        };

    public static final LensEvent INITIALIZE = new LensEvent() {};
}
