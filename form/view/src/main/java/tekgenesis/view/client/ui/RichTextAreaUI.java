
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
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RichTextArea;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;
import tekgenesis.view.client.ui.richtextarea.Toolbar;

import static tekgenesis.common.Predefined.isEmpty;

/**
 * A TextArea UI widgets.
 */
public class RichTextAreaUI extends FieldWidgetUI implements HasScalarValueUI {

    //~ Instance Fields ..............................................................................................................................

    private final RichTextArea textArea;

    private final Toolbar toolbar;

    //~ Constructors .................................................................................................................................

    /** Creates a RichTextArea UI widgets. */
    @SuppressWarnings("GWTStyleCheck")
    public RichTextAreaUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        final FlowPanel richTextArea = HtmlWidgetFactory.div();

        textArea = HtmlWidgetFactory.richTextArea();
        textArea.addStyleName("form-control");
        textArea.addStyleName("richTextArea");
        toolbar = new Toolbar(textArea);
        toolbar.addStyleName("textAreaToolbar");

        textArea.addFocusHandler(event -> toolbar.closeAllButtons());

        richTextArea.add(toolbar);
        richTextArea.add(textArea);

        setFocusTarget(Option.option(textArea));

        initWidget(richTextArea);
    }

    //~ Methods ......................................................................................................................................

    @Override public void addChangeHandler(final ValueChangeHandler<Object> changeHandler) {
        textArea.addBlurHandler(event -> changeHandler.onValueChange(null));
    }

    @Override public void setDisabled(final boolean disabled) {
        super.setDisabled(disabled);
        toolbar.setVisible(!disabled);

        final Style style = textArea.getElement().getStyle();
        if (disabled) style.setBorderStyle(Style.BorderStyle.NONE);
        else style.clearBorderStyle();
    }

    @Override public Object getValue() {
        final String t = toolbar.isHTMLMode() ? textArea.getText() : textArea.getHTML();
        return isEmpty(t) ? null : t;
    }

    @Override public void setValue(@Nullable Object modelValue) {
        textArea.setHTML((String) modelValue);
    }

    @Override public void setValue(@Nullable Object modelValue, boolean fireEvents) {
        setValue(modelValue);
        if (fireEvents) throw new UnsupportedOperationException(Constants.TO_BE_IMPLEMENTED);
    }

    @Override void addStyleNames() {
        super.addStyleNames();
        addStyleName("richText");
    }

    // ** This widget doesn't support icon */
    @NotNull @Override Option<Element> createIcon() {
        return Option.empty();
    }
}  // end class RichTextAreaUI
