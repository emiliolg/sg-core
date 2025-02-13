
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
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TekSuggestBox;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.model.KeyMap;
import tekgenesis.type.ArrayType;
import tekgenesis.type.EntityReference;
import tekgenesis.type.Type;
import tekgenesis.type.assignment.AssignmentType;
import tekgenesis.view.client.Application;
import tekgenesis.view.client.controller.FormController;
import tekgenesis.view.client.formatter.InputHandler;
import tekgenesis.view.client.suggest.ItemSuggestOracle;
import tekgenesis.view.client.suggest.ItemSuggestion;
import tekgenesis.view.client.suggest.KeySuggestOracle;
import tekgenesis.view.client.suggest.TekSuggestOracle;
import tekgenesis.view.client.ui.base.ClickablePanel;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.common.core.Strings.coverText;
import static tekgenesis.metadata.form.model.FormConstants.FOCUSED;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.formatter.InputHandler.none;
import static tekgenesis.view.client.ui.SuggestBoxUI.setDelayAndThreshold;
import static tekgenesis.view.client.ui.TagsUI.*;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.anchor;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.clickableDiv;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.div;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.span;

/**
 * A TagsSuggestBox UI widget.
 */
public class TagsSuggestBoxUI extends FieldWidgetUI implements SuggestUI, HasArrayValueUI, HasInputHandlerUI, HasWidthUI {

    //~ Instance Fields ..............................................................................................................................

    private ValueChangeHandler<Object> changeHandler = null;

    private Iterable<AssignmentType> filter;
    private InputHandler<?>          inputHandler;

    private ClickablePanel tagsContainer;

    private TekSuggestBox      tagsSuggest;
    private final List<Object> values;

    //~ Constructors .................................................................................................................................

    /** Creates a TagsSuggestBox UI widget. */
    public TagsSuggestBoxUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);

        values       = new ArrayList<>();
        filter       = null;
        inputHandler = none();

        final TagsSuggestEventHandler handler = new TagsSuggestEventHandler();
        initTagsContainer(handler);
        initTagsSuggest(handler);

        tagsContainer.add(tagsSuggest);

        initWidget(tagsContainer);
        setFocusTarget(Option.option(tagsSuggest));
        addTextFieldFocusHandler();
    }

    //~ Methods ......................................................................................................................................

    @Override public void addChangeHandler(ValueChangeHandler<Object> handler) {
        changeHandler = handler;
    }

    @Override public void addCreateNewHandler(ValueChangeHandler<String> handler) {
        tagsSuggest.addCreateNewHandler(handler);
    }

    @Override public boolean canSearchDeprecable() {
        return false;
    }

    @Override public boolean hasOnNewMethod() {
        return !isEmpty(getModel().getOnNewMethodName());
    }

    @Override public boolean hasOnSuggest() {
        return !getModel().getOnSuggestMethodName().isEmpty();
    }

    @Override public boolean hasOnSuggestSync() {
        return !getModel().getOnSuggestSyncMethodName().isEmpty();
    }

    @Override public void setDisabled(boolean disabled) {
        super.setDisabled(disabled);
        if (disabled) tagsContainer.addStyleName(TAGS_CONTAINER_DISABLED);
        else tagsContainer.removeStyleName(TAGS_CONTAINER_DISABLED);
    }

    @Override public boolean isBoundedToDeprecable() {
        return false;
    }

    /** Get suggest box filter expression. */
    @NotNull public Iterable<AssignmentType> getFilterExpression() {
        return notNull(filter, Colls.emptyIterable());
    }

    /** Set suggest box filter expression. */
    public void setFilterExpression(@Nullable final Iterable<AssignmentType> expression) {
        filter = expression;
    }

    @Override public void setHandleDeprecated(boolean handleDeprecated) {
        if (getSuggestOracle() instanceof ItemSuggestOracle)
            ((ItemSuggestOracle) tagsSuggest.getSuggestOracle()).setHandleDeprecated(handleDeprecated);
    }

    public InputHandler<?> getInputHandler() {
        return inputHandler;
    }

    public void setInputHandler(InputHandler<?> inputHandler) {
        this.inputHandler = inputHandler;
    }

    /** Sets on suggest expression result that will be passed to the user when searching. */
    public void setOnSuggestExpression(@Nullable final Object result) {
        ((TekSuggestOracle) getSuggestOracle()).setOnSuggestArg(result);
    }

    /** Set Options for enums. */
    public void setOptions(KeyMap options) {
        if (getElementType().isEnum()) {
            final KeySuggestOracle oracle = cast(tagsSuggest.getSuggestOracle());
            oracle.clear();
            for (final Map.Entry<Object, String> entry : options.entrySet())
                oracle.addSuggestion(new ItemSuggestion((String) entry.getKey(), entry.getValue()));
        }

        for (final Object value : values) {
            if (!existsTag(value)) {
                if (isEnumOrEntity() && options.get(value) != null) addTag(value, options.get(value));
                else if ((!isEnumOrEntity())) {
                    final InputHandler<Object> objectInputHandler = cast(inputHandler);
                    addTag(value, notNull(objectInputHandler.format(value)));
                }
            }
        }
    }  // end method setOptions

    @Override public void setPlaceholder(String placeholder) {
        tagsSuggest.setPlaceholder(placeholder);
    }

    /** Returns the Suggest Oracle associated with the suggest box. */
    public SuggestOracle getSuggestOracle() {
        return tagsSuggest.getSuggestOracle();
    }

    /** Only for tests. */
    public List<String> getTagsText() {
        final List<String> result = new ArrayList<>();
        tagsContainer.forEach(t -> {
            final String innerText = t.getElement().getInnerText();
            if (innerText.endsWith("×")) result.add(innerText.substring(0, innerText.lastIndexOf(" ×")));
        });
        return result;
    }

    @NotNull @Override public Iterable<?> getValues() {
        return values;
    }

    @Override public void setValues(@NotNull Iterable<Object> newValues) {
        // Remove old tags.
        final Seq<Object> aux = seq(newValues);
        for (final Object v : values) {
            if (!aux.contains(v)) removeTagUi(v);
        }

        // Clear values.
        values.clear();

        // Add new ones, tags will be added later in setOptions method where we have proper context.
        for (final Object v : aux)
            values.add(v);
    }

    @Override public void setValues(@NotNull Iterable<Object> v, boolean fireEvents) {
        setValues(v);
        if (fireEvents) throw new UnsupportedOperationException(Constants.TO_BE_IMPLEMENTED);
    }

    @NotNull @Override Option<Element> createIcon() {
        return Option.empty();
    }

    private void addTag(final Object tagValue, final String tagText) {
        final FlowPanel tagDiv = div();
        tagDiv.setStyleName(TAG_DIV_STYLE);
        tagDiv.getElement().setPropertyObject(TAG_VALUE, tagValue);

        final InlineHTML tagTextSpan = span();
        if (getModel().isEntity() && !getModel().isFullText()) {
            final String coverText = coverText(tagText);
            tagTextSpan.setText(coverText + " ");
            tagTextSpan.setTitle(tagText);
        }
        else tagTextSpan.setText(tagText + " ");

        final Anchor x = anchor(FormConstants.CLOSE_ICON);
        x.setStyleName(TAG_REMOVE_STYLE);
        x.addClickHandler(clickEvent -> {
            if (!isDisabled()) {
                removeTag(tagValue);

                // As this widget will eliminate itself if it is inisde a table it should be handled first.
                for (final MultipleUI ui : getContext().getMultiple())
                    handleClickOnTables(clickEvent, ui);

                tagDiv.removeFromParent();
            }
        });

        tagDiv.add(tagTextSpan);
        tagDiv.add(x);
        tagsContainer.insert(tagDiv, tagsContainer.getWidgetCount() - 1);
        if (tagsContainer.getWidgetCount() > 1 && !tagsSuggest.getElement().hasClassName(WITH_DIVISION)) tagsSuggest.addStyleName(WITH_DIVISION);
    }

    private void addTextFieldFocusHandler() {
        tagsSuggest.getTextBox().addFocusHandler(event -> addStyleName(FOCUSED));
        tagsSuggest.getTextBox().addBlurHandler(event -> removeStyleName(FOCUSED));
    }

    private boolean existsTag(Object value) {
        for (final com.google.gwt.user.client.ui.Widget widget : tagsContainer) {
            if (widget instanceof FlowPanel && equal(widget.getElement().getPropertyObject(TAG_VALUE), value)) return true;
        }
        return false;
    }

    private void initTagsContainer(TagsSuggestEventHandler handler) {
        tagsContainer = clickableDiv();
        tagsContainer.setStyleName(TAGS_INPUT);
        tagsContainer.addClickHandler(handler);
    }

    private void initTagsSuggest(TagsSuggestEventHandler handler) {
        boolean valueFromSuggest = true;

        final Widget        model         = getModel();
        final SuggestOracle suggestOracle;

        if (getElementType().isEnum()) suggestOracle = new KeySuggestOracle(this, hasOnNewMethod());
        else {
            suggestOracle = new ItemSuggestOracle(this, hasOnNewMethod(), getSuggestionFqn());
            if (model.getType().isString()) valueFromSuggest = false;
        }

        tagsSuggest = new TekSuggestBox(suggestOracle, valueFromSuggest, true);
        setDelayAndThreshold(tagsSuggest, getModel());

        tagsSuggest.setPlaceholder(MSGS.addATag());
        tagsSuggest.setChooseOne(MSGS.chooseOne());

        tagsSuggest.addSelectionHandler(handler);
        tagsSuggest.getTextBox().addKeyDownHandler(handler);
        tagsSuggest.getTextBox().addBlurHandler(handler);

        ((TekSuggestBox.DefaultSuggestionDisplay) tagsSuggest.getSuggestionDisplay()).setPositionRelativeTo(tagsContainer);
    }  // end method initTagsSuggest

    private void notifyValueChange() {
        assert changeHandler != null;
        changeHandler.onValueChange(null);
    }

    private void removeTag(Object tagValue) {
        values.remove(tagValue);
        notifyValueChange();
        if (values.isEmpty()) tagsSuggest.removeStyleName(WITH_DIVISION);
    }

    private void removeTagUi(Object value) {
        for (final com.google.gwt.user.client.ui.Widget widget : tagsContainer) {
            if (widget instanceof FlowPanel && equal(widget.getElement().getPropertyObject(TAG_VALUE), value)) widget.removeFromParent();
        }
    }

    private Type getElementType() {
        return ((ArrayType) getModel().getType()).getElementType();
    }

    private String getSuggestionFqn() {
        final Type type = getElementType().getFinalType();
        if (type.isDatabaseObject()) return ((EntityReference) type).getFullName();

        return type.getImplementationClassName();
    }

    private boolean isEnumOrEntity() {
        return getElementType().isEnum() || getElementType().isEntity();
    }

    //~ Methods ......................................................................................................................................

    private static void handleClickOnTables(ClickEvent clickEvent, MultipleUI multipleUI) {
        if (multipleUI instanceof TableUI) ((TableUI) multipleUI).handleSelectionEvent((Event) clickEvent.getNativeEvent());
    }

    //~ Static Fields ................................................................................................................................

    private static final String WITH_DIVISION = "with-division";

    //~ Inner Classes ................................................................................................................................

    private class TagsSuggestEventHandler implements BlurHandler, KeyDownHandler, ClickHandler, SelectionHandler<SuggestOracle.Suggestion> {
        @Override public void onBlur(BlurEvent event) {
            tagsSuggest.setText("");
        }

        @Override public void onClick(ClickEvent event) {
            tagsSuggest.setFocus(true);
        }

        @Override public void onKeyDown(KeyDownEvent event) {
            // if BACKSPACE, user input is empty and there is at least one tag, remove it.
            if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE && isEmpty(tagsSuggest.getText()) && tagsContainer.getWidgetCount() > 1) {
                final FlowPanel tagDiv = (FlowPanel) tagsContainer.getWidget(tagsContainer.getWidgetCount() - 2);
                removeTag(tagDiv.getElement().getPropertyObject(TAG_VALUE));
                tagDiv.removeFromParent();
                event.stopPropagation();
            }
        }

        @Override public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event) {
            final ItemSuggestion suggestion = (ItemSuggestion) event.getSelectedItem();
            if (suggestion != null) {
                tagsSuggest.setText("");
                final InputHandler<Object> objectInputHandler = cast(inputHandler);
                final Object               value              = objectInputHandler.fromString(suggestion.getKey());

                final String label;
                if (getElementType().isEntity() || getElementType().isEnum() || getElementType().isString())
                    label = suggestion.getReplacementString();
                else label = notNull(objectInputHandler.format(value));

                if (value != null && !values.contains(value)) {
                    values.add(value);
                    addTag(value, label);

                    final Widget         widget  = getModel();
                    final FormController current = Application.getInstance().getActiveOrMain().getCurrent();
                    if (!widget.getType().isEnum() && suggestion.getKey() != null && current != null) {
                        final FormModel controllerModel = current.getModel();
                        if (controllerModel != null) {
                            final Model  model   = widget.getMultiple()                                    //
                                                   .map(controllerModel::getMultiple)                      //
                                                   .flatMap(mm -> getContext().getItem().map(row -> (Model) mm.getRow(row)))  //
                                                   .orElse(controllerModel);
                            final KeyMap options = KeyMap.create();
                            options.putAll(model.getOptions(widget));
                            options.put(suggestion.getKey(), suggestion.getReplacementString());
                            model.setOptions(widget, options);
                        }
                    }

                    notifyValueChange();
                }
            }
        }
    }  // end class TagsSuggestEventHandler
}
