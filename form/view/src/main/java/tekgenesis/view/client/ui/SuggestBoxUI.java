
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.model.KeyMap;
import tekgenesis.type.EntityReference;
import tekgenesis.type.Type;
import tekgenesis.type.assignment.AssignmentType;
import tekgenesis.view.client.formatter.InputHandler;
import tekgenesis.view.client.suggest.ItemSuggestOracle;
import tekgenesis.view.client.suggest.ItemSuggestion;
import tekgenesis.view.client.suggest.KeySuggestOracle;
import tekgenesis.view.client.suggest.TekSuggestOracle;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;
import tekgenesis.view.client.ui.base.Icon;

import static com.google.gwt.user.client.ui.TekSuggestBox.HIGH_KEY_UP_DELAY;
import static com.google.gwt.user.client.ui.TekSuggestBox.LOW_KEY_UP_DELAY;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.core.Option.*;
import static tekgenesis.metadata.form.model.FormConstants.INPUT_APPEND;
import static tekgenesis.metadata.form.widget.WidgetType.SEARCH_BOX;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.formatter.InputHandler.none;
import static tekgenesis.view.client.ui.HasLinkUI.FormLinkUtil.*;
import static tekgenesis.view.client.ui.base.HtmlDomUtils.clickElement;

/**
 * A SuggestBox UI widget.
 */
@SuppressWarnings({ "NonJREEmulationClassesInClientCode", "ClassWithTooManyMethods" })  // GWT, also not true.
public class SuggestBoxUI extends FieldWidgetUI implements HasInputHandlerUI, HasScalarValueUI, SuggestUI, HasLinkUI, HasWidthUI {

    //~ Instance Fields ..............................................................................................................................

    private Iterable<AssignmentType> filter;

    private InputHandler<?>  inputHandler;
    private Option<Anchor>   link;
    @Nullable private String linkPk;
    private Object           modelValue = null;

    private Option<FlowPanel> panel;

    private TekSuggestBox suggestBox;

    //~ Constructors .................................................................................................................................

    /** Creates a SuggestBox UI widget. */
    public SuggestBoxUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        inputHandler = none();
        link         = empty();
        linkPk       = null;
        filter       = null;

        initSuggest(model);
        initWidget(suggestBox);

        setFocusTarget(of(suggestBox.getTextBox()));
    }

    //~ Methods ......................................................................................................................................

    /** Just like WidgetUI, but if it is a SearchBox it won't focus. */
    @Override public boolean acceptsFocus() {
        return !isSearchBox() && super.acceptsFocus();
    }

    @Override public void addChangeHandler(ValueChangeHandler<Object> changeHandler) {
        // ignore: handled in the selectionHandler
    }

    @Override public void addClickHandler(ClickHandler handler) {
        for (final Anchor a : link)
            a.addClickHandler(handler);
    }

    /** Adds a create new SelectionEvent handler. */
    public void addCreateNewHandler(final ValueChangeHandler<String> handler) {
        suggestBox.addCreateNewHandler(handler);
    }

    /** Adds a SelectionEvent handler. */
    public void addSelectionHandler(final SelectionHandler<SuggestOracle.Suggestion> handler) {
        suggestBox.addSelectionHandler(handler);
    }

    @Override public boolean canSearchDeprecable() {
        return isSearchBox() && getModel().isBoundedToDeprecable();
    }

    @Override public void click() {
        for (final Anchor a : link)
            clickElement(a.getElement());
    }

    /** Returns true if model has an on_new method defined. */
    public boolean hasOnNewMethod() {
        return !isEmpty(getModel().getOnNewMethodName()) || getModel().getOnNewForm().isNotEmpty();
    }

    @Override public boolean hasOnSuggest() {
        return !getModel().getOnSuggestMethodName().isEmpty();
    }

    @Override public boolean hasOnSuggestSync() {
        return !getModel().getOnSuggestSyncMethodName().isEmpty();
    }

    /**
     * To be used in testing only! Runs query and attempts to execute the click on the selected
     * option
     */
    public void suggest(String text, int option, @Nullable String expectedText) {
        suggestBox.showAndSelectSuggestion(text, option, expectedText);
    }

    /** Sets bound model to suggest box. */
    public void setBoundModel(QName qn) {
        if (!isDatabaseObject()) {
            final SuggestOracle oracle = suggestBox.getSuggestOracle();
            if (oracle instanceof ItemSuggestOracle && !qn.isEmpty()) ((ItemSuggestOracle) oracle).setEntityToSearchFqn(qn.getFullName());
        }
    }

    /** Overridden to apply content style to suggest box's text field. */
    @Override public void setContentStyleName(String styleName) {
        suggestBox.getTextBox().addStyleName(styleName);
    }

    @Override public boolean isBoundedToDeprecable() {
        return getModel().isBoundedToDeprecable();
    }

    /** Get suggest box filter expression. */
    @NotNull public Iterable<AssignmentType> getFilterExpression() {
        return notNull(filter, Colls.emptyIterable());
    }

    /** Set suggest box filter expression. */
    public void setFilterExpression(@Nullable final Iterable<AssignmentType> expression) {
        filter = expression;
    }

    /** When bound to a primitive type used to set the text to display. */
    public void setFormattedLabel(final Object value) {
        final InputHandler<Object> objectInputHandler = cast(inputHandler);
        setLabel(value == null ? "" : objectInputHandler.format(value));
    }

    /** Sets if the logged user can use this widget to search for deprecated instances. */
    public void setHandleDeprecated(boolean handleDeprecated) {
        if (getSuggestOracle() instanceof ItemSuggestOracle)
            ((ItemSuggestOracle) suggestBox.getSuggestOracle()).setHandleDeprecated(handleDeprecated);

        for (final FlowPanel p : panel) {
            p.setVisible(handleDeprecated);
            if (!handleDeprecated) p.getParent().removeStyleName(INPUT_APPEND);
        }
    }

    public InputHandler<?> getInputHandler() {
        return inputHandler;
    }

    public void setInputHandler(InputHandler<?> inputHandler) {
        this.inputHandler = inputHandler;
    }

    /** Set label and value. */
    public void setItem(ItemSuggestion item) {
        // value
        final InputHandler<Object> objectInputHandler = cast(inputHandler);
        setValue(objectInputHandler.fromString(item.getKey()));

        // label
        setLabel(item.getReplacementString());
    }

    /** Get label for testing purposes. */
    @NotNull public String getLabel() {
        return notNull(suggestBox.getText());
    }

    /** Set label for value. */
    public void setLabel(@Nullable final String label) {
        suggestBox.setText(notNull(label));
    }

    /** Sets the max length and visible length of the suggest box's text field. */
    public void setLength(int length) {
        if (!(isDatabaseObject() || isEnum())) TextFieldUI.setLength((TextBox) suggestBox.getTextBox(), length);
    }

    @Override public void setLink(@Nullable String url) {
        for (final Anchor a : link)
            a.setHref(url);
    }

    @Nullable @Override public String getLinkPk() {
        return linkPk;
    }

    @Override public void setLinkPk(@Nullable String pk) {
        for (final Anchor a : link)
            updateLinkPk(a, getModel(), linkPk = pk);
    }

    /** Sets on suggest expression result that will be passed to the user when searching. */
    public void setOnSuggestExpression(@Nullable final Object result) {
        ((TekSuggestOracle) getSuggestOracle()).setOnSuggestArg(result);
    }

    /** Set Options for enums. */
    public void setOptions(@NotNull final KeyMap options) {
        if (isEnum()) {
            final KeySuggestOracle oracle = cast(getSuggestOracle());
            oracle.clear();
            for (final Map.Entry<Object, String> entry : options)
                oracle.addSuggestion(new ItemSuggestion((String) entry.getKey(), entry.getValue()));
        }
    }

    @Override public void setPlaceholder(String placeholder) {
        suggestBox.setPlaceholder(placeholder);
    }

    /** Search boxes are always available. */
    public void setReadOnly(boolean readOnly) {
        if (isSearchBox()) return;
        super.setReadOnly(readOnly);
    }

    /** Returns the Suggest Oracle associated with the suggest box. */
    public SuggestOracle getSuggestOracle() {
        return suggestBox.getSuggestOracle();
    }

    @Override public Object getValue() {
        return modelValue;
    }

    @Override public void setValue(@Nullable final Object value) {
        setValue(value, false);
    }

    @Override public void setValue(@Nullable final Object value, boolean fireEvents) {
        updateModelValue(value);
        if (getModel().getType().isString()) setLabel((String) value);
    }

    protected void addStyleNames() {
        super.addStyleNames();
        // noinspection GWTStyleCheck
        addStyleName("suggestbox");
    }

    @NotNull @Override Option<Element> createIcon() {
        return of(suggestBox.getElement());
    }

    @Override Option<Icon> iconInWidget(Element icon, String iconStyle) {
        return suggestBox.changeIcon(iconStyle).map(Icon::new);
    }

    private void initSuggest(Widget model) {
        final SuggestOracle suggestOracle = isEnum() ? new KeySuggestOracle(this, hasOnNewMethod())
                                                     : new ItemSuggestOracle(this, hasOnNewMethod(), getSuggestionFqn(model));

        if (hasLink(model)) {
            final Anchor anchor = link(model);
            Icon.inWidget(anchor, IconType.EXTERNAL_LINK.getClassName());
            final FlowPanel addOn = HtmlWidgetFactory.div();
            addOn.add(anchor);
            link  = some(anchor);
            panel = some(addOn);
        }
        else if (isSearchBox() && model.isBoundedToDeprecable()) {
            final CheckBox checkBox = HtmlWidgetFactory.checkBox();
            checkBox.setName("includeDeprecated");
            checkBox.setTitle(MSGS.includeDeprecated());
            checkBox.addValueChangeHandler(event -> {
                if (suggestOracle instanceof ItemSuggestOracle) ((ItemSuggestOracle) suggestOracle).setAddDeprecated(event.getValue());

                suggestBox.setText("");
                suggestBox.getSuggestionDisplay().hideSuggestions();
                suggestBox.setFocus(true);
            });

            final FlowPanel addOn = HtmlWidgetFactory.div();
            addOn.add(checkBox);
            panel = some(addOn);
        }
        else panel = empty();

        final String icon = notEmpty(model.getIconStyle(), isSearchBox() ? IconType.SEARCH.getClassName() : "");
        suggestBox = new TekSuggestBox(suggestOracle, true, icon, panel, !getModel().isFullText(), true);
        setDelayAndThreshold(suggestBox, getModel());

        suggestBox.setPlaceholder(MSGS.searchPlaceHolder());

        suggestBox.setChooseOne(MSGS.chooseOne());
    }  // end method initSuggest

    /** Changes the internal typed value of the widget. */
    private void updateModelValue(@Nullable final Object value) {
        modelValue = value;

        if (hasAutoLinkPk(getModel())) setLinkPk(modelValue != null ? modelValue.toString() : null);
    }

    private boolean isEnum() {
        return getModel().getType().isEnum();
    }

    @NotNull private String getSuggestionFqn(Widget model) {
        if (isDatabaseObject()) {
            final EntityReference entityReference = cast(model.getType());
            return entityReference.getFullName();
        }

        final Type type = model.getType().getFinalType();
        return type.getImplementationClassName();
    }  // end method getSuggestionFqn

    private boolean isDatabaseObject() {
        return getModel().getType().isDatabaseObject();
    }

    /** Returns if this SuggestBoxUI represents a Search Box. */
    private boolean isSearchBox() {
        return getModel().getWidgetType() == SEARCH_BOX;
    }

    //~ Methods ......................................................................................................................................

    static void setDelayAndThreshold(@NotNull final TekSuggestBox suggestBox, @NotNull final Widget model) {
        // Delay
        final boolean hasOnSuggest = isNotEmpty(model.getOnSuggestSyncMethodName()) || isNotEmpty(model.getOnSuggestMethodName());
        final int     delay        = model.hasChangeDelay() ? model.getChangeDelay() : hasOnSuggest ? HIGH_KEY_UP_DELAY : LOW_KEY_UP_DELAY;
        suggestBox.setSuggestDelay(delay);

        // Threshold
        final int userSuggestThreshold = model.getChangeThreshold();
        final int threshold            = userSuggestThreshold > 0 ? userSuggestThreshold : 2;
        suggestBox.setSuggestThreshold(threshold);
    }
}  // end class SuggestBoxUI
