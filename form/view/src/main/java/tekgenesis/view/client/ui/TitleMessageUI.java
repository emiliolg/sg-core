
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.base.HElement;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.Strings.coverText;

/**
 * A Title UI widget.
 */
public class TitleMessageUI extends MessageUI {

    //~ Instance Fields ..............................................................................................................................

    private final HElement title = new HElement(1);

    //~ Constructors .................................................................................................................................

    /** Creates a TextArea UI widgets. */
    public TitleMessageUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        initWidget(title);
    }

    //~ Methods ......................................................................................................................................

    public TitleMessageUI withLabel(boolean sized) {
        return this;
    }

    @Override public Object getValue() {
        final Node textChild = title.getElement().getLastChild();
        return textChild != null ? textChild.getNodeValue() : "";
    }

    @Override public void setValue(@Nullable Object value) {
        final String text = notNull((String) value, "");
        removeLastNode(title.getElement());
        title.getElement().appendChild(Document.get().createTextNode(getModel().isFullText() ? text : coverText(text)));
    }

    @NotNull @Override Option<Element> createIcon() {
        return some(title.getElement());
    }

    /** Message title or entity doesn't support labels. */
    @NotNull @Override Option<Element> createLabel() {
        return Option.empty();
    }
}  // end class TitleMessageUI
