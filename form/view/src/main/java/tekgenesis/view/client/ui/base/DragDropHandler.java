
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
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.Widget;

import org.jetbrains.annotations.NonNls;

/**
 * DragDropHandler.
 */
public abstract class DragDropHandler implements DragStartHandler, DragEnterHandler, DragLeaveHandler, DragOverHandler, DragEndHandler, DropHandler {

    //~ Instance Fields ..............................................................................................................................

    protected final Widget widget;

    //~ Constructors .................................................................................................................................

    /** Called after drop. */
    protected DragDropHandler(Widget li) {
        widget = li;
        li.getElement().setDraggable(Element.DRAGGABLE_TRUE);
        li.addStyleName("draggableItem");
    }

    //~ Methods ......................................................................................................................................

    @Override public void onDragEnd(DragEndEvent event) {
        widget.removeStyleName(DRAG_START);
        widget.removeStyleName(DRAG_ENTER);
    }
    @Override public void onDragEnter(DragEnterEvent event) {
        widget.addStyleName(DRAG_ENTER);
    }

    @Override public void onDragLeave(DragLeaveEvent event) {
        widget.removeStyleName(DRAG_ENTER);
    }

    @Override public void onDragOver(DragOverEvent event) {
        event.preventDefault();
    }

    @Override public abstract void onDragStart(DragStartEvent event);
    @Override public abstract void onDrop(DropEvent event);

    //~ Static Fields ................................................................................................................................

    @NonNls protected static final String DRAG_START = "dragStart";
    @NonNls protected static final String DRAG_ENTER = "dragEnter";
}  // end class DragDropHandler
