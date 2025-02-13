
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo.components;

/**
 * Leader Builder.
 */
public class LeaderBuilder extends FoBuilder<LeaderBuilder, Leader> {

    //~ Methods ......................................................................................................................................

    public Leader build() {
        return new Leader().withProperties(getProperties());
    }

    //~ Methods ......................................................................................................................................

    /** Returns new leader. */
    public static LeaderBuilder leader() {
        return new LeaderBuilder();
    }
}
