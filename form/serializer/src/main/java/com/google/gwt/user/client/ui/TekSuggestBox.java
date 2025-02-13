
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

/*
 * Copyright 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.user.client.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.adapters.TakesValueEditor;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.PopupPanel.AnimationType;
import com.google.gwt.user.client.ui.SuggestOracle.Callback;
import com.google.gwt.user.client.ui.SuggestOracle.Request;
import com.google.gwt.user.client.ui.SuggestOracle.Response;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.model.FormConstants;

import static com.google.gwt.event.dom.client.KeyCodes.*;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.first;
import static tekgenesis.common.core.Constants.PLACEHOLDER;
import static tekgenesis.common.core.Option.*;
import static tekgenesis.common.core.Strings.coverText;
import static tekgenesis.common.core.Strings.isNumeric;
import static tekgenesis.metadata.form.model.FormConstants.ACTIVE_STYLE;
import static tekgenesis.metadata.form.model.FormConstants.FORM_CONTROL;
import static tekgenesis.metadata.form.model.FormConstants.INPUT_GROUP;
import static tekgenesis.metadata.form.model.FormConstants.NUMBER_COLUMN_CLASS;

/**
 * A {@link TekSuggestBox} is a text box or text area which displays a pre-configured set of
 * selections that match the user's input.
 *
 * <p>Each {@link TekSuggestBox} is associated with a single {@link SuggestOracle}. The
 * {@link SuggestOracle} is used to provide a set of selections given a specific query string.</p>
 *
 * <p>By default, the {@link TekSuggestBox} uses a {@link MultiWordSuggestOracle} as its oracle.
 * Below we show how a {@link MultiWordSuggestOracle} can be configured:</p>
 *
 * <pre>
     MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
     oracle.add("Cat");
     oracle.add("Dog");
     oracle.add("Horse");
     oracle.add("Canary");

     SuggestBox box = new SuggestBox(oracle);
 * </pre>
 *
 * <p>Using the example above, if the user types "C" into the text widget, the oracle will configure
 * the suggestions with the "Cat" and "Canary" suggestions. Specifically, whenever the user types a
 * key into the text widget, the value is submitted to the <code>MultiWordSuggestOracle</code>.</p>
 *
 * <p>Note that there is no method to retrieve the "currently selected suggestion" in a SuggestBox,
 * because there are points in time where the currently selected suggestion is not defined. For
 * example, if the user types in some text that does not match any of the SuggestBox's suggestions,
 * then the SuggestBox will not have a currently selected suggestion. It is more useful to know when
 * a suggestion has been chosen from the SuggestBox's list of suggestions. A SuggestBox fires
 * {@link SuggestionEvent SuggestionEvents} whenever a suggestion is chosen, and handlers for these
 * events can be added using the {@link #addValueChangeHandler(ValueChangeHandler)} method.</p>
 *
 * <p><img class='gallery' src='doc-files/SuggestBox.png'/></p>
 *
 * <h3>CSS Style Rules</h3>
 *
 * <dl>
 *   <dt>.gwt-SuggestBox</dt>
 *   <dd>the suggest box itself</dd>
 * </dl>
 *
 * @see  SuggestOracle
 * @see  MultiWordSuggestOracle
 * @see  TextBoxBase
 */
@SuppressWarnings("WeakerAccess")
public class TekSuggestBox extends Composite
    implements HasText, Focusable, HasAllKeyHandlers, HasValue<String>, HasSelectionHandlers<Suggestion>, IsEditor<LeafValueEditor<String>>,
               HasEnabled
{

    //~ Instance Fields ..............................................................................................................................

    protected final TextBoxBase box;

    protected String                  currentText = null;
    protected final SuggestionDisplay display;

    protected boolean               invalidateSelection;
    protected final Timer           refreshSuggestionsTimer;
    private LeafValueEditor<String> editor        = null;

    private final boolean    mustCover;
    private SuggestOracle    oracle;
    private boolean          selectsFirstItem = true;
    protected final Callback callback         = new Callback() {
            public void onSuggestionsReady(Request request, Response response) {
                display.setMoreSuggestions(response.hasMoreSuggestions(), response.getMoreSuggestionsCount());
                display.showSuggestions(TekSuggestBox.this,
                    response.getSuggestions(),
                    oracle.isDisplayStringHTML(),
                    isAutoSelectEnabled(),
                    TekSuggestBox.this::setNewSelection);
            }
        };

    protected int suggestDelay = 0;

    protected int   suggestThreshold = 0;
    private Element chevronIcon      = null;

    private String             chooseOne       = "";
    private Option<InlineHTML> iconContainer   = empty();
    private String             lastPlaceholder = "";

    private final boolean valueFromSuggest;

    //~ Constructors .................................................................................................................................

    /**
     * Constructor for {@link TekSuggestBox}. Creates a {@link TextBox} to use with this
     * {@link TekSuggestBox}.
     *
     * @param  oracle            the oracle for this <code>SuggestBox</code>
     * @param  valueFromSuggest  based on this suggest will clean its value if it is not coming from
     *                           a valid suggestion.
     */
    public TekSuggestBox(SuggestOracle oracle, boolean valueFromSuggest) {
        this(oracle, valueFromSuggest, null, empty(), true, false);
    }

    /**
     * Constructor for {@link TekSuggestBox}. Creates a {@link TextBox} to use with this
     * {@link TekSuggestBox}.
     *
     * @param  oracle            the oracle for this <code>SuggestBox</code>
     * @param  valueFromSuggest  based on this suggest will clean its value if it is not coming from
     *                           a valid suggestion.
     * @param  withChevron       has chevron or not.
     */
    public TekSuggestBox(SuggestOracle oracle, boolean valueFromSuggest, boolean withChevron) {
        this(oracle, valueFromSuggest, null, empty(), true, withChevron);
    }

    /**
     * Constructor for {@link TekSuggestBox}. The text box will be removed from it's current
     * location and wrapped by the {@link TekSuggestBox}.
     *
     * @param  oracle            supplies suggestions based upon the current contents of the text
     *                           widget
     * @param  valueFromSuggest  based on this suggest will clean its value if it is not coming from
     *                           a valid suggestion.
     * @param  iconStyle         icon of suggest box
     * @param  appendPanel       additional append panel.
     * @param  mustCover         cover or not?
     */

    public TekSuggestBox(SuggestOracle oracle, boolean valueFromSuggest, @Nullable String iconStyle, Option<FlowPanel> appendPanel, boolean mustCover,
                         boolean withChevron) {
        box = new TextBox();
        box.setStyleName(FORM_CONTROL);
        // noinspection GWTStyleCheck
        box.addStyleName("suggest-icon-place");
        display = new DefaultSuggestionDisplay(this);
        display.addScrollHandler(this);
        this.valueFromSuggest = valueFromSuggest;
        this.mustCover        = mustCover;

        initWidget(prepareBox(iconStyle, appendPanel, withChevron));
        if (withChevron)  // noinspection GWTStyleCheck
            addStyleName("with-chevron");

        refreshSuggestionsTimer = new Timer()
            {
                @Override public void run() {
                    refreshSuggestions();
                }
            };

        addEventsToTextBox();

        setOracle(oracle);
        box.getElement().setAttribute("autocomplete", "off");
    }

    //~ Methods ......................................................................................................................................

    /** Adds a ValueChangeHandler as CreateNewHandler. */
    @SuppressWarnings("UnusedReturnValue")
    public HandlerRegistration addCreateNewHandler(ValueChangeHandler<String> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
        return addDomHandler(handler, KeyDownEvent.getType());
    }

    public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
        return addDomHandler(handler, KeyPressEvent.getType());
    }

    public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
        return addDomHandler(handler, KeyUpEvent.getType());
    }

    public HandlerRegistration addSelectionHandler(SelectionHandler<Suggestion> handler) {
        return addHandler(handler, SelectionEvent.getType());
    }

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        // return addHandler(handler, ValueChangeEvent.getType());
        throw new UnsupportedOperationException(
            "We are using the value change for the Create New :), should actually create a custom event fot that");
    }

    /** Returns a {@link TakesValueEditor} backed by the DateBox. */
    public LeafValueEditor<String> asEditor() {
        if (editor == null) editor = TakesValueEditor.of(this);
        return editor;
    }

    public Option<Element> changeIcon(String iconStyle) {
        if (!iconContainer.isPresent()) createIconContainer(iconStyle, cast(getWidget()));
        return iconContainer.map(iconSpan -> {
            final Element icon    = createIcon(iconStyle);
            final Element element = iconSpan.getElement();
            element.getFirstChildElement().removeFromParent();
            element.appendChild(icon);
            return icon;
        });
    }

    public FlowPanel prepareBox(@Nullable String iconStyle, Option<FlowPanel> appendPanel, boolean withChevron) {
        final FlowPanel container = new FlowPanel();

        container.add(box);

        if (withChevron) {
            final Anchor anchor = new Anchor();
            // noinspection GWTStyleCheck
            anchor.setStyleName("suggest-icon");
            anchor.addClickHandler(event -> {
                if (isEnabled() && !display.isSuggestionsShowing()) showSuggestions("", callback);
            });
            chevronIcon = createIcon("chevron-down");
            anchor.getElement().appendChild(chevronIcon);
            container.add(anchor);
        }

        for (final FlowPanel panel : appendPanel) {
            container.addStyleName(INPUT_GROUP);
            // noinspection GWTStyleCheck
            container.addStyleName("fixDeprecateSearchBox");
            panel.addStyleName(FormConstants.INPUT_GROUP_ADDON);
            container.add(panel);
        }

        return container;
    }

    /** Show the current list of suggestions and select the index. */
    public void showAndSelectSuggestion(String query, final int index, @Nullable final String expectedSelection) {
        if (isAttached()) {
            setText(query);
            showSuggestions(query,
                (request, response) -> {
                    callback.onSuggestionsReady(request, response);
                    getSuggestionDisplay().select(index, expectedSelection);
                });
        }
    }

    public void setAccessKey(char key) {
        box.setAccessKey(key);
    }

    /**
     * Turns on or off the behavior that automatically selects the first suggested item. This
     * behavior is on by default.
     *
     * @param  selectsFirstItem  Whether or not to automatically select the first suggestion
     */
    public void setAutoSelectEnabled(boolean selectsFirstItem) {
        this.selectsFirstItem = selectsFirstItem;
    }

    /** Message for when a list of suggestions covers all the universe of suggestions. */
    public void setChooseOne(String chooseOne) {
        this.chooseOne = chooseOne;
    }

    /**
     * Returns whether or not the first suggestion will be automatically selected. This behavior is
     * on by default.
     *
     * @return  true if the first suggestion will be automatically selected
     */
    public boolean isAutoSelectEnabled() {
        return selectsFirstItem;
    }

    @Override public boolean isEnabled() {
        return box.isEnabled();
    }

    @Override public void setEnabled(boolean b) {
        box.setEnabled(b);
    }

    public void setFocus(boolean focused) {
        box.setFocus(focused);
    }

    /**
     * Set the new suggestion in the text box.
     *
     * @param  suggestion  the new suggestion
     */
    public void setNewSelection(@Nullable final Suggestion suggestion) {
        if (isExtra(suggestion)) {
            ValueChangeEvent.fire(this, getText());
            setText("");
        }
        else {
            final String fullText = suggestion == null ? "" : suggestion.getReplacementString();
            box.setTitle(fullText);
            setText(fullText);
            invalidateSelection = false;
            fireSuggestionEvent(suggestion);
        }

        box.getElement().setPropertyString(PLACEHOLDER, lastPlaceholder);

        display.hideSuggestions();
    }  // end method setNewSelection

    /** Sets this suggest box placeholder. */
    public void setPlaceholder(String placeholder) {
        lastPlaceholder = placeholder;
        box.getElement().setPropertyString(PLACEHOLDER, placeholder);
    }

    /** Set a delay to hit the server after user input. */
    public void setSuggestDelay(int suggestDelay) {
        this.suggestDelay = suggestDelay;
    }

    /**
     * Get the {@link SuggestionDisplay} used to display suggestions.
     *
     * @return  the {@link SuggestionDisplay}
     */
    public SuggestionDisplay getSuggestionDisplay() {
        return display;
    }

    /**
     * Gets the suggest box's {@link SuggestOracle}.
     *
     * @return  the {@link SuggestOracle}
     */
    public SuggestOracle getSuggestOracle() {
        return oracle;
    }

    /** Set a threshold to hit the server. */
    public void setSuggestThreshold(int suggestThreshold) {
        this.suggestThreshold = suggestThreshold;
    }

    public int getTabIndex() {
        return box.getTabIndex();
    }

    public void setTabIndex(int index) {
        box.setTabIndex(index);
    }

    public String getText() {
        return box.getText();
    }

    public void setText(String text) {
        final String coverText    = mustCover ? coverText(text) : text;
        final String tabsReplaced = coverText.replaceAll("\\t", " ");
        box.setText(tabsReplaced);

        if (mustCover) box.setTitle(text);

        currentText = tabsReplaced;
    }

    /**
     * Get the text box associated with this suggest box.
     *
     * @return  this suggest box's text box
     */
    public TextBoxBase getTextBox() {
        return box;
    }

    public String getValue() {
        return box.getValue();
    }

    public void setValue(@Nullable String newValue) {
        box.setValue(newValue);
    }

    public void setValue(@Nullable String value, boolean fireEvents) {
        box.setValue(value, fireEvents);
    }

    protected void addEventsToTextBox() {
        final TextBoxEvents events = new TextBoxEvents();
        events.addKeyHandlersTo(box);
        // SEE CREATE NEW EVENT! box.addValueChangeHandler(events);
        box.addBlurHandler(events);
        display.addCloseHandler(events);
    }  // end method addEventsToTextBox

    protected void addSuggestions(String query, int top, Callback call) {
        if (query.isEmpty()) oracle.requestDefaultSuggestions(new Request(query, top), call);
        else oracle.requestSuggestions(new Request(query, top), call);
    }

    protected void fireSuggestionEvent(@Nullable Suggestion selectedSuggestion) {
        SelectionEvent.fire(this, selectedSuggestion);
    }

    @Override protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        display.onEnsureDebugId(baseID);
    }

    protected void refreshSuggestions() {
        // Get the raw text.
        final String text = getText();
        if (text.equals(currentText)) return;

        currentText         = text;
        invalidateSelection = true;
        showSuggestions(text, callback);
    }

    protected void showSuggestions(String query, Callback call) {
        addSuggestions(query, PAGE_SIZE, call);
    }

    @NotNull private Element createIcon(String iconStyle) {
        final Element icon = DOM.createElement("icon-i");
        icon.setClassName("fa fa-" + iconStyle);
        return icon;
    }

    private void createIconContainer(@NotNull String iconStyle, FlowPanel container) {
        container.addStyleName(INPUT_GROUP);
        final InlineHTML span = new InlineHTML();
        span.setStyleName(FormConstants.INPUT_GROUP_ADDON);
        span.getElement().appendChild(createIcon(iconStyle));
        iconContainer = Option.of(span);
        container.getElement().insertFirst(span.getElement());
    }

    private void loadingSuggestions(boolean loadingDone) {
        if (chevronIcon == null) return;
        if (!loadingDone) chevronIcon.setClassName("fa fa-refresh fa-spin loading-suggestions");
        else chevronIcon.setClassName("fa fa-chevron-down");
    }

    private String getChooseOne() {
        return chooseOne;
    }

    /**
     * Sets the suggestion oracle used to create suggestions.
     *
     * @param  oracle  the oracle
     */
    private void setOracle(SuggestOracle oracle) {
        this.oracle = oracle;
    }

    //~ Methods ......................................................................................................................................

    /**
     * Creates and returns an extra-suggestion option. Behaviour of this option upon selection will
     * differ.
     */
    public static Suggestion createExtraSuggestion(@NotNull final String text) {
        return new ExtraSuggestion(text);
    }

    /** Returns true if suggestion is an extra one. */
    public static boolean isExtra(@Nullable final Suggestion suggestion) {
        return suggestion instanceof ExtraSuggestion;
    }

    //~ Static Fields ................................................................................................................................

    public static final String MAX_SUGGESTIONS_HEIGHT = "216px";

    private static final double MARGIN = 0.5;

    public static final int HIGH_KEY_UP_DELAY = 500;
    public static final int LOW_KEY_UP_DELAY  = 50;

    public static final int PAGE_SIZE = 10;

    /** Used to display suggestions to the user. */
    @SuppressWarnings("SpellCheckingInspection")
    public static final String SUGGEST_BOX_POPUP_STYLE_NAME_DEFAULT = "typeahead dropdown-menu floating-modal";

    //~ Inner Interfaces .............................................................................................................................

    /**
     * The callback used when a user selects a {@link Suggestion}.
     */
    public interface SuggestionCallback {
        /** The callback used when a user selects a {@link Suggestion}. */
        void onSuggestionSelected(Suggestion suggestion);
    }

    //~ Inner Classes ................................................................................................................................

    /**
     * <p>The default implementation of {@link SuggestionDisplay} displays suggestions in a
     * {@link PopupPanel} beneath the {@link TekSuggestBox}.</p>
     *
     * <h3>CSS Style Rules</h3>
     *
     * <dl>
     *   <dt>.gwt-SuggestBoxPopup</dt>
     *   <dd>the suggestion popup</dd>
     *
     *   <dt>.gwt-SuggestBoxPopup .item</dt>
     *   <dd>an unselected suggestion</dd>
     *
     *   <dt>.gwt-SuggestBoxPopup .item-selected</dt>
     *   <dd>a selected suggestion</dd>
     *
     *   <dt>.gwt-SuggestBoxPopup .suggestPopupTopLeft</dt>
     *   <dd>the top left cell</dd>
     *
     *   <dt>.gwt-SuggestBoxPopup .suggestPopupTopLeftInner</dt>
     *   <dd>the inner element of the cell</dd>
     *
     *   <dt>.gwt-SuggestBoxPopup .suggestPopupTopCenter</dt>
     *   <dd>the top center cell</dd>
     *
     *   <dt>.gwt-SuggestBoxPopup .suggestPopupTopCenterInner</dt>
     *   <dd>the inner element of the cell</dd>
     *
     *   <dt>.gwt-SuggestBoxPopup .suggestPopupTopRight</dt>
     *   <dd>the top right cell</dd>
     *
     *   <dt>.gwt-SuggestBoxPopup .suggestPopupTopRightInner</dt>
     *   <dd>the inner element of the cell</dd>
     *
     *   <dt>.gwt-SuggestBoxPopup .suggestPopupMiddleLeft</dt>
     *   <dd>the middle left cell</dd>
     *
     *   <dt>.gwt-SuggestBoxPopup .suggestPopupMiddleLeftInner</dt>
     *   <dd>the inner element of the cell</dd>
     *
     *   <dt>.gwt-SuggestBoxPopup .suggestPopupMiddleCenter</dt>
     *   <dd>the middle center cell</dd>
     *
     *   <dt>.gwt-SuggestBoxPopup .suggestPopupMiddleCenterInner</dt>
     *   <dd>the inner element of the cell</dd>
     *
     *   <dt>.gwt-SuggestBoxPopup .suggestPopupMiddleRight</dt>
     *   <dd>the middle right cell</dd>
     *
     *   <dt>.gwt-SuggestBoxPopup .suggestPopupMiddleRightInner</dt>
     *   <dd>the inner element of the cell</dd>
     *
     *   <dt>.gwt-SuggestBoxPopup .suggestPopupBottomLeft</dt>
     *   <dd>the bottom left cell</dd>
     *
     *   <dt>.gwt-SuggestBoxPopup .suggestPopupBottomLeftInner</dt>
     *   <dd>the inner element of the cell</dd>
     *
     *   <dt>.gwt-SuggestBoxPopup .suggestPopupBottomCenter</dt>
     *   <dd>the bottom center cell</dd>
     *
     *   <dt>.gwt-SuggestBoxPopup .suggestPopupBottomCenterInner</dt>
     *   <dd>the inner element of the cell</dd>
     *
     *   <dt>.gwt-SuggestBoxPopup .suggestPopupBottomRight</dt>
     *   <dd>the bottom right cell</dd>
     *
     *   <dt>.gwt-SuggestBoxPopup .suggestPopupBottomRightInner</dt>
     *   <dd>the inner element of the cell</dd>
     * </dl>
     */
    public static class DefaultSuggestionDisplay extends SuggestionDisplay implements HasAnimation {
        private int     currentSuggestionsOffset = PAGE_SIZE;
        private boolean hasMoreSuggestions       = false;
        /**
         * Sub-classes making use of decorateSuggestionList to add elements to the suggestion popup
         * _may_ want those elements to show even when there are 0 suggestions. An example would be
         * showing a "No matches" message.
         */
        private boolean hideWhenEmpty = true;

        /**
         * We need to keep track of the last {@link TekSuggestBox} because it acts as an autoHide
         * partner for the {@link PopupPanel}. If we use the same display for multiple
         * {@link TekSuggestBox}, we need to switch the autoHide partner.
         */
        private TekSuggestBox lastSuggestBox = null;
        private final Element message;

        /**
         * Object to position the suggestion display next to, instead of the associated suggest box.
         */
        private UIObject             positionRelativeTo = null;
        private final ScrollPanel    scrollPanel;
        private final SuggestionMenu suggestionMenu;
        private final PopupPanel     suggestionPopup;

        /** Construct a new {@link DefaultSuggestionDisplay}. */
        public DefaultSuggestionDisplay() {
            suggestionMenu  = new SuggestionMenu();
            suggestionPopup = createPopup();
            scrollPanel     = new ScrollPanel();
            scrollPanel.setWidget(suggestionMenu);

            final FlowPanel main = new FlowPanel();
            message = DOM.createSpan();
            message.addClassName("suggest-title");
            main.getElement().appendChild(message);
            main.add(scrollPanel);
            final Widget list = decorateSuggestionList(main);

            suggestionPopup.setWidget(list);
        }

        DefaultSuggestionDisplay(TekSuggestBox suggestBox) {
            this();
            lastSuggestBox = suggestBox;
        }

        @Override
        @SuppressWarnings("UnusedReturnValue")
        public HandlerRegistration addCloseHandler(final CloseHandler<PopupPanel> handler) {
            return suggestionPopup.addCloseHandler(handler);
        }

        public void addScrollHandler(TekSuggestBox tekSuggestBox) {
            scrollPanel.addScrollHandler(event -> {
                if (scrollPanel.getVerticalScrollPosition() == scrollPanel.getMaximumVerticalScrollPosition() && hasMoreSuggestions) {
                    tekSuggestBox.loadingSuggestions(false);
                    hasMoreSuggestions = false;

                    // We have more suggestions to show, let's go get them.
                    tekSuggestBox.addSuggestions(tekSuggestBox.getText(),
                        currentSuggestionsOffset + PAGE_SIZE,
                        (request, response) -> {
                            if (!isSuggestionsShowing()) return;

                            showSuggestions(response.getSuggestions(), tekSuggestBox);
                            setMoreSuggestions(response.hasMoreSuggestions(), response.getMoreSuggestionsCount());
                            if (!response.hasMoreSuggestions()) currentSuggestionsOffset = PAGE_SIZE;

                            tekSuggestBox.loadingSuggestions(true);
                        });
                }
            });
        }

        @Override public void hideSuggestions() {
            suggestionPopup.hide();
        }

        @Override public void moveSelectionDown() {
            // Make sure that the menu is actually showing. These keystrokes
            // are only relevant when choosing a suggestion.
            if (isSuggestionListShowing()) {
                // If nothing is selected, getSelectedItemIndex will return -1 and we
                // will select index 0 (the first item) by default.
                suggestionMenu.selectItem(suggestionMenu.getSelectedItemIndex() + 1);
                checkDownScroll();
            }
        }

        @Override public void moveSelectionUp() {
            // Make sure that the menu is actually showing. These keystrokes
            // are only relevant when choosing a suggestion.
            if (isSuggestionListShowing()) {
                // if nothing is selected, then we should select the last suggestion by
                // default. This is because, in some cases, the suggestions menu will
                // appear above the text box rather than below it (for example, if the
                // text box is at the bottom of the window and the suggestions will not
                // fit below the text box). In this case, users would expect to be able
                // to use the up arrow to navigate to the suggestions.
                if (suggestionMenu.getSelectedItemIndex() == -1) suggestionMenu.selectItem(suggestionMenu.getNumItems() - 1);
                else suggestionMenu.selectItem(suggestionMenu.getSelectedItemIndex() - 1);

                checkUpScroll();
            }
        }

        @Override public void select(int index, @Nullable String expectedSelection) {
            if (suggestionMenu.getNumItems() <= index)
                throw new IndexOutOfBoundsException(
                    "Attempting to select an out of range index for suggest box. Select '" + index + "' with '" + suggestionMenu.getNumItems() +
                    "' items.");

            suggestionMenu.selectItem(index);
            if (expectedSelection != null && !expectedSelection.equals(suggestionMenu.getSelectedItem().getText()))
                throw new IllegalStateException(
                    "Wrong suggestbox value. Expecting: " + expectedSelection + " Found: " + suggestionMenu.getSelectedItem().getText());
            suggestionMenu.doSelectedItemAction();
        }

        public void setAnimationEnabled(boolean enable) {
            suggestionPopup.setAnimationEnabled(enable);
        }

        public boolean isAnimationEnabled() {
            return suggestionPopup.isAnimationEnabled();
        }

        /**
         * Check whether or not the list of suggestions is being shown.
         *
         * @return  true if the suggestions are visible, false if not
         */
        public boolean isSuggestionListShowing() {
            return suggestionPopup.isShowing();
        }
        @Override public boolean isSuggestionsShowing() {
            return suggestionPopup.isShowing();
        }

        /**
         * Sets the style name of the suggestion popup.
         *
         * @param  style  the new primary style name
         *
         * @see    UIObject#setStyleName(String)
         */
        public void setPopupStyleName(String style) {
            suggestionPopup.setStyleName(style);
        }

        /**
         * Sets the UI object where the suggestion display should appear next to.
         *
         * @param  uiObject  the uiObject used for positioning, or null to position relative to the
         *                   suggest box
         */
        public void setPositionRelativeTo(UIObject uiObject) {
            positionRelativeTo = uiObject;
        }

        /**
         * Set whether or not the suggestion list should be hidden when there are no suggestions to
         * display. Defaults to true.
         *
         * @param  hideWhenEmpty  true to hide when empty, false not to
         */
        public void setSuggestionListHiddenWhenEmpty(boolean hideWhenEmpty) {
            this.hideWhenEmpty = hideWhenEmpty;
        }

        /**
         * Check whether or not the suggestion list is hidden when there are no suggestions to
         * display.
         *
         * @return  true if hidden when empty, false if not
         */
        public boolean isSuggestionListHiddenWhenEmpty() {
            return hideWhenEmpty;
        }

        /**
         * Create the PopupPanel that will hold the list of suggestions.
         *
         * @return  the popup panel
         */
        protected PopupPanel createPopup() {
            // PopupPanel p = new DecoratedPopupPanel(true, false, "suggestPopup");

            final PopupPanel p = new PopupPanel(true, false);
            // p.setAnimationEnabled(true);
            p.setStyleName(SUGGEST_BOX_POPUP_STYLE_NAME_DEFAULT);

            // p.setStyleName("gwt-SuggestBoxPopup");
            p.setPreviewingAllNativeEvents(true);
            p.setAnimationType(AnimationType.ROLL_DOWN);
            return p;
        }

        /**
         * Wrap the list of suggestions before adding it to the popup. You can override this method
         * if you want to wrap the suggestion list in a decorator.
         *
         * @param   suggestionList  the widget that contains the list of suggestions
         *
         * @return  the suggestList, optionally inside of a wrapper
         */
        protected Widget decorateSuggestionList(Widget suggestionList) {
            return suggestionList;
        }

        /**
         * <b>Affected Elements:</b>
         *
         * <ul>
         *   <li>-popup = The popup that appears with suggestions.</li>
         *   <li>-item# = The suggested item at the specified index.</li>
         * </ul>
         *
         * @see  UIObject#onEnsureDebugId(String)
         */
        @Override protected void onEnsureDebugId(String baseID) {
            suggestionPopup.ensureDebugId(baseID + "-popup");
            suggestionMenu.setMenuItemDebugIds(baseID);
        }

        protected void showSuggestions(Collection<? extends Suggestion> suggestions, final TekSuggestBox box) {
            addSuggestions(suggestions, box::setNewSelection, true);

            currentSuggestionsOffset += PAGE_SIZE;
            suggestionPopup.showRelativeTo(positionRelativeTo != null ? positionRelativeTo : box);
        }

        @Override protected void showSuggestions(final TekSuggestBox suggestBox, Collection<? extends Suggestion> suggestions,
                                                 boolean isDisplayStringHTML, boolean isAutoSelectEnabled, final SuggestionCallback sgtCallback) {
            // Hide the popup if there are no suggestions to display.
            final boolean anySuggestions = (suggestions != null && !suggestions.isEmpty());
            if (!anySuggestions && hideWhenEmpty) {
                hideSuggestions();
                return;
            }

            // Hide the popup before we manipulate the menu within it. If we do not
            // do this, some browsers will redraw the popup as items are removed
            // and added to the menu.
            if (suggestionPopup.isAttached()) suggestionPopup.hide(true);

            suggestionMenu.clearItems();

            assert suggestions != null;

            message.removeClassName(DISPLAY_NONE);
            if (isNotEmpty(lastSuggestBox.getChooseOne())) message.setInnerText(lastSuggestBox.getChooseOne());
            else message.addClassName(DISPLAY_NONE);

            // set to size of 9 items so we can scroll
            if (suggestions.size() == 10 && hasMoreSuggestions) scrollPanel.setHeight(MAX_SUGGESTIONS_HEIGHT);

            addSuggestions(suggestions, sgtCallback, isDisplayStringHTML);

            if (isAutoSelectEnabled && anySuggestions)
            // Select the first item in the suggestion menu.
            suggestionMenu.selectItem(0);

            // Link the popup autoHide to the TextBox.
            if (lastSuggestBox != suggestBox) {
                // If the suggest box has changed, free the old one first.
                if (lastSuggestBox != null) suggestionPopup.removeAutoHidePartner(lastSuggestBox.getElement());
                lastSuggestBox = suggestBox;
                suggestionPopup.addAutoHidePartner(suggestBox.getElement());
            }

            // Show the popup under the TextBox.
            suggestionPopup.showRelativeTo(positionRelativeTo != null ? positionRelativeTo : suggestBox);
        }  // end method showSuggestions

        @Nullable @Override protected Suggestion getCurrentSelection() {
            if (!isSuggestionListShowing()) return null;
            final MenuItem item = suggestionMenu.getSelectedItem();
            return item == null ? null : ((SuggestionMenuItem) item).getSuggestion();
        }

        @Override protected void setMoreSuggestions(boolean moreSuggestions, int numMoreSuggestions) {
            hasMoreSuggestions = moreSuggestions;
        }

        /**
         * Get the {@link PopupPanel} used to display suggestions.
         *
         * @return  the popup panel
         */
        protected PopupPanel getPopupPanel() {
            return suggestionPopup;
        }

        private void addSuggestions(Collection<? extends Suggestion> suggestions, SuggestionCallback sgtCallback, boolean isDisplayStringHTML) {
            final boolean hasParts = hasParts(suggestions);

            for (final Suggestion suggestion : suggestions) {
                final SuggestionMenuItem item = new SuggestionMenuItem(suggestion, isDisplayStringHTML, hasParts);
                item.setScheduledCommand(() -> sgtCallback.onSuggestionSelected(suggestion));

                item.addStyleName(isExtra(suggestion) ? "suggest-extra" : "suggest-item");

                suggestionMenu.addItem(item);
            }
        }

        private void checkDownScroll() {
            final int menuHeight     = suggestionMenu.getSelectedItem().getOffsetHeight();
            final int selectedBottom = (suggestionMenu.getSelectedItemIndex() + 1) * menuHeight;
            final int maxShowing     = 9 * menuHeight;

            final int mustBeScroll = selectedBottom - maxShowing;

            if (selectedBottom > maxShowing && scrollPanel.getVerticalScrollPosition() < mustBeScroll)
                scrollPanel.setVerticalScrollPosition(mustBeScroll);
        }

        private void checkUpScroll() {
            final int selectedTop = suggestionMenu.getSelectedItemIndex() * suggestionMenu.getSelectedItem().getOffsetHeight();

            if (selectedTop < scrollPanel.getVerticalScrollPosition()) scrollPanel.setVerticalScrollPosition(selectedTop);
        }

        private boolean hasParts(Collection<? extends Suggestion> suggestions) {
            return first(suggestions).filter(s -> s.getDisplayString().split("\\t").length > 1).isPresent();
        }

        private static final String DISPLAY_NONE = "display-none";
    }  // end class DefaultSuggestionDisplay

    private static class ExtraSuggestion implements Suggestion {
        private final String display;

        private ExtraSuggestion(String display) {
            this.display = display;
        }

        @Override public String getDisplayString() {
            return display;
        }

        @Override public String getReplacementString() {
            return display;
        }
    }

    /**
     * Used to display suggestions to the user.
     */
    public abstract static class SuggestionDisplay {
        /** Adds a CloseHandler. */
        @SuppressWarnings("UnusedReturnValue")
        public abstract HandlerRegistration addCloseHandler(CloseHandler<PopupPanel> handler);

        /** Add a scroll handler. */
        public abstract void addScrollHandler(TekSuggestBox tekSuggestBox);

        /** Hide the list of suggestions from view. */
        public abstract void hideSuggestions();

        /** Highlight the suggestion directly below the current selection in the list. */
        public abstract void moveSelectionDown();

        /** Highlight the suggestion directly above the current selection in the list. */
        public abstract void moveSelectionUp();

        /** Select the 'index' suggestion. */
        public abstract void select(int index, @Nullable String expectedSelection);

        /** Returns true if suggestions are displayed. */
        public abstract boolean isSuggestionsShowing();

        /**
         * Set the debug id of widgets used in the SuggestionDisplay.
         *
         * @param  suggestBoxBaseID  the baseID of the {@link TekSuggestBox}
         *
         * @see    UIObject#onEnsureDebugId(String)
         */
        protected void onEnsureDebugId(String suggestBoxBaseID) {}

        /**
         * Update the list of visible suggestions.
         *
         * <p>Use care when using isDisplayStringHtml; it is an easy way to expose script-based
         * security problems.</p>
         *
         * @param  suggestBox           the suggest box where the suggestions originated
         * @param  suggestions          the suggestions to show
         * @param  isDisplayStringHTML  should the suggestions be displayed as HTML
         * @param  isAutoSelectEnabled  if true, the first item should be selected automatically
         * @param  callback             the callback used when the user makes a suggestion
         */
        protected abstract void showSuggestions(TekSuggestBox suggestBox, Collection<? extends Suggestion> suggestions, boolean isDisplayStringHTML,
                                                boolean isAutoSelectEnabled, SuggestionCallback callback);

        /**
         * Get the currently selected {@link Suggestion} in the display.
         *
         * @return  the current suggestion, or null if none selected
         */
        protected abstract Suggestion getCurrentSelection();

        /**
         * Accepts information about whether there were more suggestions matching than were provided
         * to {@link #showSuggestions}.
         *
         * @param  hasMoreSuggestions  true if more matches were available
         * @param  numMoreSuggestions  number of more matches available. If the specific number is
         *                             unknown, 0 will be passed.
         */
        @SuppressWarnings({ "UnusedParameters", "EmptyMethod" })
        protected void setMoreSuggestions(boolean hasMoreSuggestions, int numMoreSuggestions) {
            // Subclasses may optionally implement.
        }
    }  // end class SuggestionDisplay

    /**
     * The SuggestionMenu class is used for the display and selection of suggestions in the
     * SuggestBox widget. SuggestionMenu differs from MenuBar in that it always has a vertical
     * orientation, and it has no sub-menus. It also allows for programmatic selection of items in
     * the menu, and programmatic performing the action associated with the selected item. In the
     * MenuBar class, items cannot be programmatic selected - they can only be selected when the
     * user places the mouse over a particular item. Additional methods in SuggestionMenu provide
     * information about the number of items in the menu, and the index of the currently selected
     * item.
     */
    private static class SuggestionMenu extends MenuBar {
        private final List<Element> trs;

        public SuggestionMenu() {
            super(true);
            // Make sure that CSS styles specified for the default Menu classes
            // do not affect this menu
            setStyleName("");
            setFocusOnHoverEnabled(false);
            trs = new ArrayList<>();
        }

        public void doSelectedItemAction() {
            // In order to perform the action of the item that is currently
            // selected, the menu must be showing.
            final MenuItem selectedItem = getSelectedItem();
            if (selectedItem != null) doItemAction(selectedItem, true, false);
        }

        @Override public MenuItem insertItem(MenuItem item, int beforeIndex)
            throws IndexOutOfBoundsException
        {
            // Check the bounds
            if (beforeIndex < 0 || beforeIndex > getItems().size()) throw new IndexOutOfBoundsException();

            // Add to the list of items.
            getItems().add(item);

            // Get the t-body to append the new row.
            final Element outer = getElement();
            final Element table = DOM.getChild(outer, 1);
            if (table != null) {  // wierd case where table might be null
                final Element tBody = table.getFirstChildElement();

                // Setup the menu item.
                final Element tr = DOM.createTR();
                DOM.appendChild(tBody, tr);

                trs.add(tr);

                DOM.sinkEvents(tr, Event.ONMOUSEOVER);
                Event.setEventListener(tr,
                    event -> {
                        if (Event.ONMOUSEOVER == event.getTypeInt()) {
                            for (final Element t : trs)
                                t.removeClassName(ACTIVE_STYLE);
                            tr.addClassName(ACTIVE_STYLE);
                        }
                    });

                final SuggestionMenuItem suggestMenuItem = (SuggestionMenuItem) item;
                suggestMenuItem.appendTo(tr);
            }

            item.setParentMenu(this);
            item.setSelectionStyle(false);
            updateSubmenuIcon(item);
            return item;
        }

        /**
         * Selects the item at the specified index in the menu. Selecting the item does not perform
         * the item's associated action; it only changes the style of the item and updates the value
         * of SuggestionMenu.selectedItem.
         *
         * @param  index  index
         */
        @SuppressWarnings("MethodOverloadsMethodOfSuperclass")
        public void selectItem(int index) {
            final List<MenuItem> items = getItems();
            if (index > -1 && index < items.size()) itemOver(items.get(index), false);
        }

        public int getNumItems() {
            return getItems().size();
        }

        /**
         * Returns the index of the menu item that is currently selected.
         *
         * @return  returns the selected item
         */
        public int getSelectedItemIndex() {
            // The index of the currently selected item can only be
            // obtained if the menu is showing.
            final MenuItem selectedItem = getSelectedItem();
            if (selectedItem != null) return getItems().indexOf(selectedItem);
            return -1;
        }

        @Override void itemOver(MenuItem item, boolean focus) {
            super.itemOver(item, focus);

            if (item != null) {
                final SuggestionMenuItem suggestMenuItem = (SuggestionMenuItem) item;
                for (final Element t : trs)
                    t.removeClassName(ACTIVE_STYLE);
                final Element tr = suggestMenuItem.getTr();
                if (tr != null) tr.addClassName(ACTIVE_STYLE);
            }
        }
    }  // end class SuggestionMenu

    /**
     * Class for menu items in a SuggestionMenu. A SuggestionMenuItem differs from a MenuItem in
     * that each item is backed by a Suggestion object. The text of each menu item is derived from
     * the display string of a Suggestion object, and each item stores a reference to its Suggestion
     * object.
     */
    public static class SuggestionMenuItem extends MenuItem {
        private final boolean hasParts;
        private Suggestion    suggestion = null;
        private Element       tr         = null;

        private SuggestionMenuItem(Suggestion suggestion, boolean asHTML, boolean hasParts) {
            super(suggestion.getDisplayString(), asHTML);
            // Each suggestion should be placed in a single row in the suggestion
            // menu. If the window is re-sized and the suggestion cannot fit on a
            // single row, it should be clipped (instead of wrapping around and
            // taking up a second row).
            // DOM.setStyleAttribute(getElement(), "whiteSpace", "nowrap");
            // setStyleName(ITEM_STYLE_NAME_DEFAULT);
            setStyleName("");
            setSuggestion(suggestion);
            this.hasParts = hasParts;
        }

        void appendTo(Element t) {
            tr = t;

            final String   suggestionDisplayString = suggestion.getDisplayString();
            final String   displayString           = isExtra(suggestion) && hasParts ? suggestionDisplayString + "\t " : suggestionDisplayString;
            final String[] parts                   = splitDisplayString(displayString);

            final Element tdElem = getElement();
            if (parts.length > 1) {
                tdElem.setInnerText("");
                tdElem.getStyle().setPadding(0, Style.Unit.PX);
            }
            DOM.appendChild(t, tdElem);

            if (parts.length > 1) {
                for (final String part : parts) {
                    final Element td = DOM.createTD();
                    td.setInnerHTML(part);
                    td.setAttribute("role", "menuitem");

                    if (isNumeric(part)) td.addClassName(NUMBER_COLUMN_CLASS);

                    DOM.appendChild(t, td);
                    DOM.sinkEvents(td, Event.ONCLICK);
                    Event.setEventListener(td, event -> {
                            if (Event.ONCLICK == event.getTypeInt()) getScheduledCommand().execute();
                        });
                }
            }
        }

        Element getTr() {
            return tr;
        }

        @NotNull private String[] splitDisplayString(String displayString) {
            final String toBeSplitted = displayString.endsWith("\t") ? displayString + " " : displayString;
            return toBeSplitted.split("\\t");
        }

        private Suggestion getSuggestion() {
            return suggestion;
        }

        private void setSuggestion(Suggestion suggestion) {
            this.suggestion = suggestion;
        }
    }  // end class SuggestionMenuItem

    protected class TextBoxEvents extends HandlesAllKeyEvents implements ValueChangeHandler<String>, BlurHandler, CloseHandler<PopupPanel> {
        public void onBlur(BlurEvent event) {
            refreshSuggestionsTimer.cancel();
            if (invalidateSelection && !display.isSuggestionsShowing() && valueFromSuggest)
                setNewSelection(null);  // select null when a blur occurs and the popup is closed (no possible option)
        }

        @Override public void onClose(CloseEvent<PopupPanel> event) {
            // when the popup-menu is closed, check that the value has changed and that the element is not focused
            // the auto closed is set on true when the popup is hidden before building it {@link
            // TekSuggestBox.DefaultSuggestionDisplay.showSuggestions}
            if (invalidateSelection && !event.isAutoClosed() && !equal(getActiveElement(), box.getElement()) && valueFromSuggest)
                setNewSelection(null);  // select null when the popup is closed and no option was selected
            box.getElement().setPropertyString(PLACEHOLDER, lastPlaceholder);

            loadingSuggestions(true);
        }
        public void onKeyDown(KeyDownEvent event) {
            switch (event.getNativeKeyCode()) {
            case KEY_ESCAPE:
                display.hideSuggestions();
                break;
            case KEY_DOWN:
                if (display.isSuggestionsShowing()) display.moveSelectionDown();
                else showSuggestions("", callback);
                break;
            case KEY_UP:
                display.moveSelectionUp();
                break;
            case KEY_ENTER:
                if (display.isSuggestionsShowing()) {
                    event.stopPropagation();
                    changeSelection();
                }
                break;
            case KEY_TAB:
                if (display.isSuggestionsShowing()) changeSelection();
                break;
            }
            delegateEvent(TekSuggestBox.this, event);
        }

        public void onKeyPress(KeyPressEvent event) {
            if (suggestDelay > 0) refreshSuggestionsTimer.cancel();
            delegateEvent(TekSuggestBox.this, event);
        }

        public void onKeyUp(KeyUpEvent event) {
            // After every user key input, refresh the popup suggestions.
            if (box.getText().length() >= suggestThreshold) {
                if (suggestDelay > 0) refreshSuggestionsTimer.schedule(suggestDelay);
                else refreshSuggestions();
            }
            else invalidateSelection = true;

            delegateEvent(TekSuggestBox.this, event);
        }

        public void onValueChange(ValueChangeEvent<String> event) {
            // SEE CREATE NEW! delegateEvent(TekSuggestBox.this, event);
        }

        protected void changeSelection() {
            final Suggestion suggestion = display.getCurrentSelection();
            if (valueFromSuggest || suggestion != null) setNewSelection(suggestion);
        }

        /** Return the currently active/focused element. */
        private native Element getActiveElement()  /*-{ return $wnd.document.activeElement; }-*/;
    }  // end class TextBoxEvents
}  // end class TekSuggestBox
