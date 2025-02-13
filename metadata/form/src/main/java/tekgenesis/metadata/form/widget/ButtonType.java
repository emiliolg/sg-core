
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
 * Button Types.
 */
public enum ButtonType {

    //~ Enum constants ...............................................................................................................................

    CUSTOM, SAVE, DELETE, CANCEL, ADD_ROW, REMOVE_ROW, VALIDATE, EXPORT;

    //~ Methods ......................................................................................................................................

    /** Id of the button. */
    public String getId() {
        return name().toLowerCase();
    }
}
