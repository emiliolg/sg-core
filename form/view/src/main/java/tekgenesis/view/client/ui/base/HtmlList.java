
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
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

/**
 * Class to create html lists.
 */
public class HtmlList {

    //~ Constructors .................................................................................................................................

    private HtmlList() {}

    //~ Inner Classes ................................................................................................................................

    public static class Item extends ComplexPanel implements HasText {
        private final Element li;

        Item() {
            li = DOM.createElement("li");
            setElement(li);
        }

        Item(final String text) {
            this();
            setText(text);
        }

        @Override public void add(Widget w) {
            super.add(w, li);
        }

        @Override public String getText() {
            return li.getInnerText();
        }

        @Override public void setText(String text) {
            li.setInnerText(text == null ? "" : text);
        }
    }

    public static class Label extends ComplexPanel implements HasText {
        private final LabelElement label;

        /** Returns a label for an input. */
        public Label(final String text, final String idFor) {
            label = DOM.createLabel().cast();
            label.setHtmlFor(idFor);
            setElement(label);
            setText(text);
        }

        @Override public void add(Widget w) {
            super.add(w, label);
        }

        @Override public String getText() {
            return label.getInnerText();
        }

        @Override public void setText(String text) {
            label.setInnerText(text == null ? "" : text);
        }
    }

    public static class Ordered extends ComplexPanel {
        private final Element ol;

        Ordered() {
            ol = DOM.createElement("ol");
            setElement(ol);
        }

        @Override public void add(Widget w) {
            super.add(w, ol);
        }
    }

    public static class Unordered extends ComplexPanel {
        private final Element ul;

        protected Unordered() {
            ul = DOM.createElement("ul");
            setElement(ul);
        }

        @Override public void add(Widget w) {
            super.add(w, ul);
        }
    }
}  // end class HtmlList
