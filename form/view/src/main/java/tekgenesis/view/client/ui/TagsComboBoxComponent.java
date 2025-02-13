
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.List;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.*;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.view.client.RootInputHandler;
import tekgenesis.view.client.ui.base.Icon;

import static com.google.gwt.event.dom.client.KeyCodes.*;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.metadata.form.widget.IconType.CHEVRON_DOWN;
import static tekgenesis.metadata.form.widget.IconType.CHEVRON_UP;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.*;

/**
 * TagsComboBox GWT widget.
 */
class TagsComboBoxComponent extends FocusPanel implements HasOptionsUI.HasOptionsComponent {

    //~ Instance Fields ..............................................................................................................................

    private final FlowPanel      bigDiv;
    private final Icon           icon;
    private TagsComboBoxListener listener        = null;
    private final FlowPanel      popupInnerPanel;

    private ValueChangeHandler<String> valueChangeHandler = null;

    //~ Constructors .................................................................................................................................

    TagsComboBoxComponent(String id) {
        setStyleName("tagsComboBox input-xlarge well");

        final FlowPanel innerDiv = div();
        bigDiv = div();  // this is to support tag 'cutting' when overflow.
        bigDiv.setStyleName("tagsComboBoxBigDiv");
        final FlowPanel tagsDiv = div();
        tagsDiv.setStyleName("tagsComboBoxDiv");
        tagsDiv.add(bigDiv);
        innerDiv.add(tagsDiv);

        icon = new Icon(CHEVRON_DOWN);
        icon.addStyleName("tagsComboBoxDropdown");

        final PopupPanel popup = new PopupPanel(true, false);
        popup.getElement().setId(id + "TagsPopup");
        popup.setStyleName(TekSuggestBox.SUGGEST_BOX_POPUP_STYLE_NAME_DEFAULT);
        popup.setPreviewingAllNativeEvents(true);
        popupInnerPanel = div();
        popupInnerPanel.setStyleName("tagsComboBoxPopup");
        popup.setWidget(popupInnerPanel);

        popup.addCloseHandler(popupPanelCloseEvent -> icon.setType(CHEVRON_DOWN));

        final TagsComboBoxHandler handler = new TagsComboBoxHandler(popup, this);
        popup.addDomHandler(event -> {
                final int code = event.getNativeKeyCode();
                if (code == KEY_ESCAPE || code == KEY_DOWN) {
                    popup.hide();
                    icon.setType(CHEVRON_DOWN);
                    setFocus(true);
                }
            },
            KeyDownEvent.getType());

        addClickHandler(handler);
        addKeyDownHandler(handler);
        addFocusHandler(handler);

        innerDiv.add(icon);
        add(innerDiv);
    }  // end ctor TagsComboBoxComponent

    //~ Methods ......................................................................................................................................

    @Override public void addChangeHandler(ValueChangeHandler<Object> changeHandler) {
        valueChangeHandler = cast(changeHandler);
    }

    @Override public void addOption(@NotNull final String id, @NotNull final String label) {
        final CheckBox checkBox = checkBox();
        checkBox.setStyleName(RootInputHandler.DONT_DESELECT_TABLE);
        checkBox.setStyleName(FormConstants.FLOATING_MODAL);
        checkBox.setText(label);
        final boolean contains = listener.uiSelectionContains(id);
        checkBox.setValue(contains);
        checkBox.getElement().setAttribute("data-key", id);
        checkBox.addValueChangeHandler(event -> {
            final Boolean selected = event.getValue();
            listener.uiSelectionUpdate(id, selected);
            notifyValueChange();
            focusFirstElement();
        });

        if (contains) addTagUi(id, label);
        popupInnerPanel.add(checkBox);
    }

    @Override public void clearOptions() {
        // clearing all checkboxes and tags.
        popupInnerPanel.clear();
        bigDiv.clear();
    }

    @Override public void clearSelection() {
        listener.uiSelectionClear();
        setSelectedOptions(Colls.emptyList(), false);
    }

    public void setDisabled(boolean disabled) {
        if (disabled) addStyleName(TAGS_DISABLED);
        else removeStyleName(TAGS_DISABLED);

        for (final Widget widget : bigDiv) {
            if (disabled) widget.addStyleName(TAG_DISABLED);
            else widget.removeStyleName(TAG_DISABLED);
        }
    }

    public void setListener(TagsComboBoxListener l) {
        listener = l;
    }

    @Override public void setSelectedOptions(@NotNull List<String> ids, boolean e) {
        bigDiv.clear();

        for (final Widget widget : popupInnerPanel) {
            final boolean  contains = ids.contains(getKey(widget));
            final CheckBox checkBox = (CheckBox) widget;
            checkBox.setValue(contains);
            if (contains) addTagUi(getKey(widget), checkBox.getText());
        }
    }

    private void addTagUi(final String id, final String label) {
        final FlowPanel tagDiv = div();
        tagDiv.getElement().setPropertyString(TagsUI.TAG_VALUE, label);
        tagDiv.setStyleName(TagsUI.TAG_DIV_STYLE);

        final InlineHTML tagTextSpan = span();
        tagTextSpan.setText(label + " ");

        final Anchor x = anchor(FormConstants.CLOSE_ICON);
        x.setStyleName(TagsUI.TAG_REMOVE_STYLE);
        x.addClickHandler(event -> {
            if (!listener.isDisabled()) {
                listener.uiSelectionUpdate(id, false);
                notifyValueChange();
            }
        });
        x.addKeyDownHandler(event -> {
            if (!listener.isDisabled() && event.getNativeKeyCode() == KEY_SPACE) {
                listener.uiSelectionUpdate(id, false);
                notifyValueChange();
                event.stopPropagation();
                setFocus(true);
            }
        });

        tagDiv.add(tagTextSpan);
        tagDiv.add(x);
        bigDiv.add(tagDiv);
    }

    private void focusFirstElement() {
        final Widget widget = popupInnerPanel.getWidget(0);
        if (widget instanceof Focusable) ((Focusable) widget).setFocus(true);
    }

    private void notifyValueChange() {
        valueChangeHandler.onValueChange(null);
    }

    private String getKey(Widget widget) {
        return widget.getElement().getAttribute("data-key");
    }

    //~ Static Fields ................................................................................................................................

    private static final String TAGS_DISABLED = "tagsDisabled";
    private static final String TAG_DISABLED  = "tagDisabled";

    //~ Inner Classes ................................................................................................................................

    private class TagsComboBoxHandler implements ClickHandler, KeyDownHandler, BlurHandler, FocusHandler {
        private final PopupPanel popup;
        private final UIObject   relativeTo;

        TagsComboBoxHandler(PopupPanel popup, UIObject relativeTo) {
            this.popup      = popup;
            this.relativeTo = relativeTo;
        }

        @Override public void onBlur(BlurEvent event) {
            hidePopup();
        }

        @Override public void onClick(ClickEvent event) {
            if (!listener.isDisabled()) {
                if (popup.isShowing()) hidePopup();
                else showPopup();
            }
            event.preventDefault();
        }

        @Override public void onFocus(FocusEvent event) {
            if (listener.isDisabled()) setFocus(false);
            listener.focus(event);
        }

        @Override public void onKeyDown(KeyDownEvent event) {
            if (event.isDownArrow() || event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
                if (!popup.isShowing()) showPopup();
                event.preventDefault();
            }
            if (event.isUpArrow() || event.getNativeKeyCode() == KEY_ESCAPE) {
                if (popup.isShowing()) hidePopup();
                event.preventDefault();
            }
        }

        private void hidePopup() {
            popup.hide();
            icon.setType(CHEVRON_DOWN);
            setFocus(true);
        }

        private void showPopup() {
            popup.showRelativeTo(relativeTo);
            icon.setType(CHEVRON_UP);
            popup.getElement().getFirstChildElement().focus();
            focusFirstElement();
        }
    }  // end class TagsComboBoxHandler
}  // end class TagsComboBoxComponent
