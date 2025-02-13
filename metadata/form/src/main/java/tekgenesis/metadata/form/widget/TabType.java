
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.widget;

/**
 * Tab Type.
 */
public enum TabType {

    //~ Enum constants ...............................................................................................................................

    PILL, TOP, LEFT, BOTTOM, RIGHT, VERTICAL_LEFT, VERTICAL_RIGHT;

    //~ Methods ......................................................................................................................................

    public boolean isPill() {
        return this == PILL || this == VERTICAL_LEFT || this == VERTICAL_RIGHT;
    }

    public boolean isVertical() {
        return this == VERTICAL_LEFT || this == VERTICAL_RIGHT;
    }
}
