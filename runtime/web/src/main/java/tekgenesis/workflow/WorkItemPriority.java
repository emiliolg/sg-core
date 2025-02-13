
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.workflow;

/**
 * WorkItemPriority.
 */
public enum WorkItemPriority {

    //~ Enum constants ...............................................................................................................................

    LOWEST(5), LOW(4), NORMAL(3), HIGH(3), HIGHEST(1);

    //~ Instance Fields ..............................................................................................................................

    private final int code;

    //~ Constructors .................................................................................................................................

    WorkItemPriority(int code) {
        this.code = code;
    }

    //~ Methods ......................................................................................................................................

    /** Get Priority Code number.* */
    public int getCode() {
        return code;
    }

    //~ Methods ......................................................................................................................................

    /** returns WorkItemPriority based on code. returns NORMAL if the code is invalid.* */
    public static WorkItemPriority find(int c) {
        for (final WorkItemPriority wip : values())
            if (wip.getCode() == c) return wip;
        return NORMAL;
    }
}
