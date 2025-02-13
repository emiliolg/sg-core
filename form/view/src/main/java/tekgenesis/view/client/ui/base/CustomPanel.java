
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.base;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import static tekgenesis.common.core.Constants.TO_BE_IMPLEMENTED;

/**
 * Creates empty custom panel.
 */
public class CustomPanel extends ComplexPanel implements InsertPanel.ForIsWidget {

    //~ Constructors .................................................................................................................................

    /** Creates an empty flow panel. */
    CustomPanel(final Element elem) {
        setElement(elem);
    }

    //~ Methods ......................................................................................................................................

    /**
     * Adds a new child widget to the panel.
     *
     * @param  w  the widget to be added
     */
    @Override public void add(Widget w) {
        final Element element = getElement();
        add(w, element);
    }

    @Override public void clear() {
        throw new UnsupportedOperationException(TO_BE_IMPLEMENTED);
        /*try {
         *  doLogicalClear();
         * } finally {
         *  // Remove all existing child nodes.
         *  Node child = getElement().getFirstChild();
         *  while (child != null) {
         *      getElement().removeChild(child);
         *      child = getElement().getFirstChild();
         *  }
         *}*/
    }

    public void insert(IsWidget w, int beforeIndex) {
        insert(asWidgetOrNull(w), beforeIndex);
    }

    /**
     * Inserts a widget before the specified index.
     *
     * @param   w            the widget to be inserted
     * @param   beforeIndex  the index before which it will be inserted
     *
     * @throws  IndexOutOfBoundsException  if <code>beforeIndex</code> is out of range
     */
    public void insert(Widget w, int beforeIndex) {
        final Element element = getElement();
        insert(w, element, beforeIndex, true);
    }
}  // end class CustomPanel
