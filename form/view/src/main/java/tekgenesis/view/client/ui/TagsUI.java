
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.formatter.InputHandler;
import tekgenesis.view.client.ui.base.ClickablePanel;
import tekgenesis.view.client.ui.base.DragDropHandler;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.metadata.form.model.FormConstants.FOCUSED;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.formatter.InputHandler.none;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.*;

/**
 * A Tags UI widget.
 */
@SuppressWarnings("GWTStyleCheck")
public class TagsUI extends FieldWidgetUI implements HasArrayValueUI, HasInputHandlerUI, HasWidthUI {

    //~ Instance Fields ..............................................................................................................................

    private ValueChangeHandler<Object> changeHandler = null;
    private Object                     dragSource;
    private InputHandler<?>            inputHandler;
    private final ClickablePanel       tagsContainer;

    private final TextFieldUI  tagsInput;
    private final List<Object> values;

    //~ Constructors .................................................................................................................................

    /** Creates a Tags UI widget. */
    public TagsUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);

        values       = new ArrayList<>();
        inputHandler = none();
        tagsInput    = new TextFieldUI(container, model);
        tagsInput.setStyleName("tagsTextBox");
        tagsInput.setPlaceholder(MSGS.addATag());

        tagsContainer = clickableDiv();  // container.
        tagsContainer.setStyleName(TAGS_INPUT);

        final TagsEventHandler handler = new TagsEventHandler();
        tagsInput.addKeyDownHandler(handler);
        tagsInput.addBlurHandler(handler);
        tagsInput.addDropHandler(DropEvent::preventDefault);
        tagsContainer.addClickHandler(handler);
        tagsContainer.add(tagsInput);

        initWidget(tagsContainer);
        setFocusTarget(Option.option(tagsInput));
        addTextFieldFocusHandler(tagsInput);

        dragSource = null;
    }

    //~ Methods ......................................................................................................................................

    @Override public void addChangeHandler(ValueChangeHandler<Object> handler) {
        changeHandler = handler;
    }

    /** Use content style to apply styles to inner invisible tags input. */
    @Override public void setContentStyleName(String styleName) {
        tagsInput.addStyleName(styleName);
    }

    @Override public void setDisabled(boolean disabled) {
        super.setDisabled(disabled);
        tagsInput.setVisible(!disabled);
        if (disabled) tagsContainer.addStyleName(TAGS_CONTAINER_DISABLED);
        else tagsContainer.removeStyleName(TAGS_CONTAINER_DISABLED);
    }

    @Override public void setInputHandler(InputHandler<?> inputHandler) {
        tagsInput.setInputHandler(inputHandler);
        this.inputHandler = inputHandler;
    }

    @Override public void setInputWidth(int col) {
        super.setInputWidth(col);
        tagsInput.setInputWidth(0);
    }

    /** Sets the length. */
    public void setLength(int length) {
        tagsInput.setLength(length, true);
    }

    @Override public void setPlaceholder(String placeholder) {
        tagsInput.setPlaceholder(placeholder);
    }

    @NotNull @Override public Iterable<?> getValues() {
        return values;
    }

    @Override public void setValues(@NotNull Iterable<Object> values) {
        this.values.clear();

        tagsContainer.clear();
        tagsContainer.add(tagsInput);

        for (final Object tag : values) {
            this.values.add(tag);
            addTagHtml(notNull(inputHandler.toString(tag)));
        }
    }

    @Override public void setValues(@NotNull Iterable<Object> v, boolean fireEvents) {
        setValues(v);
        if (fireEvents) throw new UnsupportedOperationException(Constants.TO_BE_IMPLEMENTED);
    }

    /** This widget doesn't support icons. */
    @NotNull @Override Option<Element> createIcon() {
        return Option.empty();
    }

    private void addTagHtml(@Nullable String tagText) {
        final FlowPanel tagDiv = div();
        tagDiv.setStyleName(TAG_DIV_STYLE);

        final InlineHTML tagTextSpan = span();
        tagTextSpan.setText(tagText + " ");

        final Anchor x = anchor(FormConstants.CLOSE_ICON);
        x.setStyleName(TAG_REMOVE_STYLE);
        x.getElement().setPropertyString(TAG_VALUE, tagText);
        x.addClickHandler(clickEvent -> {
            if (!isDisabled()) removeTag((Anchor) clickEvent.getSource());
        });

        final DragDropHandler handler = new TagDragDropHandler(tagDiv);

        tagDiv.addDomHandler(handler, DragStartEvent.getType());
        tagDiv.addDomHandler(handler, DragEnterEvent.getType());
        tagDiv.addDomHandler(handler, DragLeaveEvent.getType());
        tagDiv.addDomHandler(handler, DragOverEvent.getType());
        tagDiv.addDomHandler(handler, DragEndEvent.getType());
        tagDiv.addDomHandler(handler, DropEvent.getType());

        tagDiv.add(tagTextSpan);
        tagDiv.add(x);
        tagsContainer.insert(tagDiv, tagsContainer.getWidgetCount() - 1);
    }

    private void addTextFieldFocusHandler(final TextFieldUI ui) {
        ui.addFocusHandler(event -> addStyleName(FOCUSED));
        ui.addBlurHandler(event -> removeStyleName(FOCUSED));
    }

    private void changeOrder(int a, int b) {
        final Object aux = values.remove(a);
        values.add(b, aux);
        changeHandler.onValueChange(null);
    }

    private void removeTag(Anchor anchor) {
        values.remove(inputHandler.fromString(anchor.getElement().getPropertyString(TAG_VALUE)));
        anchor.getParent().removeFromParent();

        assert changeHandler != null;
        changeHandler.onValueChange(null);  // hack
    }

    //~ Static Fields ................................................................................................................................

    @NonNls static final String TAGS_INPUT = "tagsInput well";

    static final String TAGS_CONTAINER_DISABLED = "tagsContainerDisabled";

    static final String TAG_DIV_STYLE    = "tagDiv alert-info floating-modal";
    static final String TAG_REMOVE_STYLE = "tagRemove close";

    static final String TAG_VALUE = "TAG_VALUE";

    //~ Inner Classes ................................................................................................................................

    private class TagDragDropHandler extends DragDropHandler {
        /** Called after drop. */
        TagDragDropHandler(com.google.gwt.user.client.ui.Widget li) {
            super(li);
        }

        @Override public void onDragStart(DragStartEvent event) {
            dragSource = event.getSource();
            widget.addStyleName(DRAG_START);
            event.setData("text", String.valueOf(tagsContainer.getWidgetIndex(widget)));
        }

        @Override public void onDrop(DropEvent event) {
            if (dragSource != widget) {
                final Integer a = Integer.valueOf(event.getData("text"));

                final com.google.gwt.user.client.ui.Widget w = tagsContainer.getWidget(a);
                final int                                  b = tagsContainer.getWidgetIndex(widget);
                tagsContainer.remove(a);
                tagsContainer.insert(w, b);

                changeOrder(a, b);
            }
            widget.removeStyleName(DRAG_ENTER);
            dragSource = null;
            event.stopPropagation();
        }
    }

    private class TagsEventHandler implements BlurHandler, KeyDownHandler, ClickHandler {
        @Override public void onBlur(BlurEvent event) {
            if (isNotEmpty(inputHandler.toString(tagsInput.getValue()))) addTag();
        }

        @Override public void onClick(ClickEvent event) {
            tagsInput.setFocus(true);
        }

        @Override public void onKeyDown(KeyDownEvent event) {
            final String text = inputHandler.toString(tagsInput.getValue());

            // if ENTER add a tag.
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && isNotEmpty(text)) addTag();

            // if BACKSPACE, user input is empty and there is at least one tag, remove it.
            if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE && isEmpty(text) && tagsContainer.getWidgetCount() > 1) {
                final FlowPanel tagDiv = (FlowPanel) tagsContainer.getWidget(tagsContainer.getWidgetCount() - 2);
                final Anchor    anchor = (Anchor) tagDiv.getWidget(1);
                removeTag(anchor);
            }
        }

        private void addTag() {
            values.add(tagsInput.getValue());
            addTagHtml(inputHandler.toString(tagsInput.getValue()));

            tagsInput.clear();

            assert changeHandler != null;
            changeHandler.onValueChange(null);  // hack
        }
    }
}  // end class TagsUI
