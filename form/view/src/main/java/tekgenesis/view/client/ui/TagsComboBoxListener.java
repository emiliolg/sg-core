
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.event.dom.client.FocusEvent;

/**
 * TagsComboBox component listener.
 */
public interface TagsComboBoxListener {

    //~ Methods ......................................................................................................................................

    /** On focus event... */
    void focus(FocusEvent event);

    /** Clear UI selection. */
    void uiSelectionClear();

    /** UI selection contains this id? */
    boolean uiSelectionContains(final String id);

    /** Update UI selection for given id. */
    void uiSelectionUpdate(final String id, final boolean selected);

    /** Is disable? */
    boolean isDisabled();
}
