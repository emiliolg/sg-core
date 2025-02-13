
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.base;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

/**
 * HTML list with inner draggable.
 *
 * <ul>
 *   <li><widget></widget></li>
 * </ul>
 */
public class DraggableList extends ComplexPanel implements HasWidgets {

    //~ Instance Fields ..............................................................................................................................

    private Object                          dragSource;
    private final List<OrderChangeListener> listeners;

    //~ Constructors .................................................................................................................................

    /** Draggable list constructor. */
    public DraggableList() {
        final Element ol = DOM.createElement("ol");
        setElement(ol);
        dragSource = null;
        listeners  = new ArrayList<>();
    }

    //~ Methods ......................................................................................................................................

    @Override public void add(final Widget li) {
        final DragDropHandler handler = new ListDragDropHandler(li);

        li.addDomHandler(handler, DragStartEvent.getType());
        li.addDomHandler(handler, DragEnterEvent.getType());
        li.addDomHandler(handler, DragLeaveEvent.getType());
        li.addDomHandler(handler, DragOverEvent.getType());
        li.addDomHandler(handler, DragEndEvent.getType());
        li.addDomHandler(handler, DropEvent.getType());

        final Element element = getElement();
        super.add(li, element);
    }

    /** Adds an order change listener. */
    public void addListener(OrderChangeListener listener) {
        listeners.add(listener);
    }

    //~ Inner Interfaces .............................................................................................................................

    // /** Adds an order change listener. */
    // public void lettered() { ol.setAttribute("type", "A"); }

    /**
     * Drop / Order Change Listener.
     */
    public interface OrderChangeListener {
        /** Called after drop. */
        void onChange(int a, int b);
    }

    //~ Inner Classes ................................................................................................................................

    private class ListDragDropHandler extends DragDropHandler {
        public ListDragDropHandler(Widget li) {
            super(li);
        }

        @Override public void onDragStart(DragStartEvent event) {
            dragSource = event.getSource();
            widget.addStyleName(DRAG_START);
            event.setData("text", String.valueOf(getWidgetIndex(widget)));
        }

        @Override public void onDrop(DropEvent event) {
            if (dragSource != widget) {
                final Integer a = Integer.valueOf(event.getData("text"));

                final Widget w = getWidget(a);
                final int    b = getWidgetIndex(widget);
                remove(a);
                final Element element = getElement();
                insert(w, element, b, true);

                for (final DraggableList.OrderChangeListener listener : listeners)
                    listener.onChange(a, b);
            }
            widget.removeStyleName(DRAG_ENTER);
            dragSource = null;
            event.stopPropagation();
        }
    }
}  // end class DraggableList
