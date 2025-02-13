
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.TextArea;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;

/**
 * A TextArea UI widgets.
 */
public class TextAreaUI extends BaseHasScalarValueUI {

    //~ Instance Fields ..............................................................................................................................

    private final TextArea textArea = HtmlWidgetFactory.textArea();

    //~ Constructors .................................................................................................................................

    /** Creates a TextArea UI widgets. */
    public TextAreaUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        init(textArea, true);
    }

    //~ Methods ......................................................................................................................................

    /** Sets the number of visible lines. */
    public void setLength(int length) {
        if (length > 0) textArea.setCharacterWidth(length);
    }

    /** Sets the number of visible lines. */
    public void setVisibleLines(int lines) {
        if (lines > 0) textArea.setVisibleLines(lines);
    }

    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName("textArea");
    }

    // ** This widget doesn't support icon */
    @NotNull @Override Option<Element> createIcon() {
        return Option.empty();
    }
}
