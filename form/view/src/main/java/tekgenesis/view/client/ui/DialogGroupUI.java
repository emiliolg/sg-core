
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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.view.client.Application;
import tekgenesis.view.client.RootInputHandler;
import tekgenesis.view.client.ui.modal.ModalContent;
import tekgenesis.view.client.ui.modal.ModalListener;
import tekgenesis.view.client.ui.modal.ModalSubscription;

import static tekgenesis.common.core.Option.some;
import static tekgenesis.view.client.ui.base.HtmlDomUtils.clickElement;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.anchor;

/**
 * Dialog Group Widget.
 */
public class DialogGroupUI extends ContainerUI implements HasScalarValueUI, HasDisplayLinkUI, ClickHandler, ModalListener {

    //~ Instance Fields ..............................................................................................................................

    private ValueChangeHandler<Object> changeHandler = null;

    private final FlowPanel        contentDiv;
    @Nullable private final Anchor link;
    private final ModalContent     modalContent;
    private boolean                opened       = false;
    private ModalSubscription      subscription;

    //~ Constructors .................................................................................................................................

    /** DialogGroup constructor. */
    public DialogGroupUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        contentDiv   = new FlowPanel();
        subscription = null;

        modalContent = new ModalContent();
        modalContent.setBody(contentDiv);
        modalContent.setBody(contentDiv);
        modalContent.setTitle(model.getLabel());
        modalContent.setListener(this);
        modalContent.setCloseButton(true);

        // if the user adds the display option, the dialog can be opened by it's one
        // otherwise it will be opened by changing the model boolean value to true, probably for example from a server method
        if (model.getDisplayExpression().isEmpty()) link = null;
        else {
            link = anchor();
            link.addClickHandler(this);
            div.add(link);
        }
    }

    //~ Methods ......................................................................................................................................

    @Override public void addChangeHandler(ValueChangeHandler<Object> handler) {
        changeHandler = handler;
    }

    @Override public void click() {
        if (link != null) clickElement(link.getElement());
    }

    // open the dialog when clicking the link
    @Override public void onClick(ClickEvent event) {
        event.stopPropagation();
        setValue(!opened);
    }

    @Override public void onHide(ModalButtonType buttonClicked) {
        RootInputHandler.getInstance().setModalTrigger(null);
        opened = false;
        changeHandler.onValueChange(null);
    }

    @Override public void onShow() {
        RootInputHandler.getInstance().setModalTrigger(getElement());
        opened = true;
        changeHandler.onValueChange(null);
    }

    @Override public void setLabelFromExpression(String label) {
        modalContent.setTitle(label);
        if (subscription != null) subscription.updateLabel(label);
    }

    /** Set link text. */
    public void setLinkText(@Nullable String linkText) {
        // if null it's ok, the link will be hidden (this can be on purpose)
        if (link != null) link.setText(linkText);
    }

    // use custom 'opened' flag instead of dialog.isActive() because of expression evaluation
    @Override public Object getValue() {
        return opened;
    }

    @Override public void setValue(@Nullable Object modelValue) {
        if (modelValue != null && (Boolean) modelValue) {
            if (subscription == null || !Application.getInstance().isModalShowing()) subscription = Application.modal(modalContent);
        }
        else if (subscription != null) {
            subscription.hide();
            subscription = null;
        }
    }

    @Override public void setValue(@Nullable Object modelValue, boolean fireEvents) {
        setValue(modelValue);
        if (fireEvents) throw new UnsupportedOperationException(Constants.TO_BE_IMPLEMENTED);
    }

    @Override protected void addChildPanel(final WidgetUI w) {
        if (w.getModel().getWidgetType() == WidgetType.FOOTER) {
            // heads up! if inside a subform, this will be removed or replaced, but: a dialog inside a subform... mmmm
            w.removeStyleName(GroupUI.FORM_FOOTER_CLASS_NAME);
            modalContent.setFooter(w);
        }
        else contentDiv.add(w);
    }

    @NotNull @Override Option<Element> createIcon() {
        return link == null ? Option.empty() : some(link.getElement());
    }

    // the label is used as the dialog title
    @NotNull @Override Option<Element> createLabel() {
        return Option.empty();
    }
}
