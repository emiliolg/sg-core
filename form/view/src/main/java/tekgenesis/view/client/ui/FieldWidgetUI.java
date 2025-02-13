
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
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.UIObject;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.check.CheckMsg;
import tekgenesis.check.CheckType;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.Alignment;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.base.HtmlDomUtils;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;
import tekgenesis.view.client.ui.base.Tooltip;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Option.option;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.metadata.form.model.FormConstants.*;

/**
 * Base class for InputWidgets with control-group functionality.
 */
public abstract class FieldWidgetUI extends WidgetUI implements HasPlaceholderUI, HasTooltipUI {

    //~ Instance Fields ..............................................................................................................................

    Tooltip tooltipDiv;

    private final FlowPanel controlsDiv;
    private CheckMsg        currentMsg;
    private final FlowPanel fieldContainer;
    private InlineHTML      hintSpan;
    private InlineHTML      inlineMsg;

    private WidgetMessages messages;

    private final Option<FlowPanel> panel;

    //~ Constructors .................................................................................................................................

    FieldWidgetUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        this(container, model, false);
    }

    FieldWidgetUI(@NotNull final ModelUI container, @NotNull final Widget model, boolean noLabel) {
        super(container, model);
        inlineMsg      = null;
        messages       = null;
        hintSpan       = null;
        tooltipDiv     = null;
        currentMsg     = null;
        controlsDiv    = HtmlWidgetFactory.div();
        fieldContainer = HtmlWidgetFactory.div();
        panel          = model.getOffsetCol() > 0 ? some(HtmlWidgetFactory.div()) : Option.empty();
        if (noLabel) controlsDiv.setStyleName(FormConstants.MARGIN_0);
    }

    //~ Methods ......................................................................................................................................

    /** Adds a blur handler. */
    public void addBlurHandler(@NotNull final BlurHandler handler) {
        if (content instanceof FocusWidget) ((FocusWidget) content).addBlurHandler(handler);
    }

    /** Add a check msg to the widget. */
    public void addMessage(@NotNull final CheckMsg msg) {
        setCurrentMsg(msg);

        if (msg.isInline()) setInlineMessage(msg.getType(), msg.getText());
        else {
            if (messages == null) messages = WidgetMessages.create(controlsDiv, false);
            messages.msg(msg.getType(), msg.getText());
            messages.show();
        }
    }  // end method addMessage

    /** Clear the messages of the widget. */
    public void clearMessages() {
        currentMsg = null;
        if (messages != null) messages.clear();
        if (inlineMsg != null) inlineMsg.setText(null);
    }

    /** Returns if the widget has validation errors. */
    public boolean hasErrorMessage() {
        return currentMsg != null && currentMsg.getType() == CheckType.ERROR;
    }

    /** Gets current Message. */
    public CheckMsg getCurrentMessage() {
        return currentMsg;
    }

    /** Returns if the widget has validation errors ans they are visible. */
    public boolean isErrorMessageVisible() {
        return hasErrorMessage() && HtmlDomUtils.isVisible(this);
    }

    /** Sets hint for input. */
    public void setHint(@Nullable String hint) {
        if (hintSpan == null) {
            hintSpan = HtmlWidgetFactory.span();
            hintSpan.setStyleName("help-block");
        }
        hintSpan.setText(hint);
        controlsDiv.add(hintSpan);
    }

    /** Sets Placeholder property to the DOM element. */
    public void setInputWidth(int col) {
        if (col > 0) controlsDiv.setStyleName("col-sm-" + col);
        else if (col == 0) controlsDiv.setStyleName("");
        setAlignmentStyle(getModel().getAlignment());
    }

    @Override public Element getLabelContainer() {
        return panel.isPresent() ? panel.get().getElement() : getElement();
    }

    @Override public void setMaxWidth() {
        if (tooltipDiv == null) super.setMaxWidth();
        else if (!getModel().isExpand() && !getContext().getMultiple().isPresent())
            HtmlDomUtils.setMaxWidth(fieldContainer.getElement(), getModel().getMaxWidth());
    }

    /** Sets Placeholder property to the DOM element. */
    public void setPlaceholder(String placeholder) {
        for (final Focusable focusable : getFocusTarget())
            HtmlDomUtils.setPlaceholder((UIObject) focusable, placeholder);
    }

    /** Sets tooltip for input. */
    public void setTooltip(String tip) {
        if (isEmpty(tip)) {
            if (tooltipDiv != null) fieldContainer.remove(tooltipDiv);
            tooltipDiv = null;
        }
        else {
            if (tooltipDiv == null) tooltipDiv = new Tooltip();
            tooltipDiv.setMsg(tip);
            fieldContainer.addStyleName("with-tooltip");
            fieldContainer.insert(tooltipDiv, 1);
        }
    }

    @Override protected void initWidget(com.google.gwt.user.client.ui.Widget widget) {
        fieldContainer.add(widget);
        controlsDiv.add(fieldContainer);
        if (panel.isPresent()) {
            final FlowPanel p = panel.get();
            p.addStyleName("offset-container");
            p.add(controlsDiv);
            div.add(p);
        }
        else div.add(controlsDiv);
        initComposite();
        content = widget;
        if (widget instanceof Focusable) setFocusTarget(option((Focusable) widget));
        final Widget model    = getModel();
        final int    labelCol = model.getLabelCol() > 0 ? model.getLabelCol() : model.getNoLabel() ? 0 : 2;
        setInputWidth(GRID - labelCol);
    }

    void addControlStyleName(String style) {
        controlsDiv.addStyleName(style);
    }

    @Override void addStyleNames() {
        addStyleName(WIDGET);
        addStyleName(FORM_GROUP);
        final int                                  col    = getModel().getCol();
        final int                                  offset = getModel().getOffsetCol();
        final com.google.gwt.user.client.ui.Widget target = panel.isPresent() ? panel.get() : this;
        if (col > 0) target.addStyleName("col-sm-" + col);
        if (offset > 0) {
            target.addStyleName(OFFSET_PREFIX + offset);
            target.addStyleName(WITH_OFFSET);
        }
        if (getModel().getType().isNumber()) addStyleName(NUMBER_STYLE);
        if (getModel().isTopLabel()) addStyleName(VERTICAL_LABEL_FIELD);
    }

    /** Clear user input data. */
    void clear() {
        super.clear();
        clearMessages();
    }

    void setCurrentMsg(@NotNull final CheckMsg msg) {
        currentMsg = msg;
    }

    /** Sets help for input. */
    void setInlineMessage(final CheckType type, final String msg) {
        if (inlineMsg == null) {
            inlineMsg = HtmlWidgetFactory.span();
            controlsDiv.add(inlineMsg);
        }

        inlineMsg.setText(msg);
        inlineMsg.setStyleName("help-inline " + type.getDecorationClass(true));
    }

    private void setAlignmentStyle(final Alignment alignment) {
        switch (alignment) {
        case CENTER:
            controlsDiv.addStyleName("center-align");
            break;
        case RIGHT:
            controlsDiv.addStyleName("right-align");
            break;
        default:
            break;
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final double MARGIN = 1.5;

    @NonNls static final String FORM_GROUP = "form-group";
}  // end class FieldWidgetUI
