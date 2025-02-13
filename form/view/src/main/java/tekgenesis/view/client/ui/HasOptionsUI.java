
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.*;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.model.KeyMap;
import tekgenesis.model.ValueCount;
import tekgenesis.view.client.formatter.InputHandler;
import tekgenesis.view.client.ui.base.HtmlDomUtils;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.core.Strings.coverText;
import static tekgenesis.view.client.ui.Widgets.checkArrayAccess;
import static tekgenesis.view.client.ui.Widgets.checkScalarAccess;

/**
 * Base UI class for widget that have a list of options.
 */
public abstract class HasOptionsUI extends FieldWidgetUI implements HasInputHandlerUI, HasScalarValueUI, HasArrayValueUI {

    //~ Instance Fields ..............................................................................................................................

    HasOptionsComponent          component    = null;
    private InputHandler<Object> inputHandler;

    private final Map<Object, String> options;    // Typed options to identifiers
    private final List<Object>        selection;  // Typed selection

    //~ Constructors .................................................................................................................................

    HasOptionsUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        inputHandler = InputHandler.none();
        selection    = new ArrayList<>();
        options      = new HashMap<>();
        component    = null;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean acceptsFocus() {
        return !isDisabled() && isVisible() && HtmlDomUtils.isVisible((com.google.gwt.user.client.ui.Widget) component);
    }

    @Override public void addChangeHandler(ValueChangeHandler<Object> changeHandler) {
        component.addChangeHandler(changeHandler);
    }

    public boolean hasNullOption() {
        return component.hasNullOption();
    }

    /** Clear selection. */
    public void uiSelectionClear() {
        selection.clear();
    }

    /** True if selection contains given id. */
    public boolean uiSelectionContains(@NotNull final String id) {
        for (final Map.Entry<Object, String> entry : options.entrySet()) {
            if (id.equals(entry.getValue())) return selection.contains(entry.getKey());
        }
        return false;
    }

    /** Update selected option id. Add or remove from selection. */
    public void uiSelectionUpdate(@NotNull final String id, boolean selected) {
        for (final Map.Entry<Object, String> entry : options.entrySet()) {
            if (id.equals(entry.getValue())) {
                if (selected) {
                    if (acceptsScalarAccess()) selection.clear();
                    selection.add(entry.getKey());
                }
                else selection.remove(entry.getKey());
                break;
            }
        }
    }

    @Override public void setFocus(boolean focused) {
        component.setFocus(focused);
    }

    public void setInputHandler(InputHandler<?> inputHandler) {
        this.inputHandler = cast(inputHandler);
    }

    /** FOR TESTING PURPOSE ONLY!!!! */
    public Map<Object, String> getOptions() {
        return options;
    }

    /** Set options from a Map with keys[String] and values[Object]. */
    public void setOptions(final KeyMap items) {
        options.clear();
        component.clearOptions();

        for (final Map.Entry<Object, String> entry : items) {
            final Object key   = entry.getKey();
            final String value = entry.getValue();
            if (key instanceof ValueCount) {
                final ValueCount valueCount = (ValueCount) key;
                final String     coverLabel = getModel().isEntity() ? coverText(key.toString()) : key.toString();
                if (!valueCount.isZero()) component.addOption(addOption(valueCount.getValue()), coverLabel);
            }
            else addFormattedOption(key, value);
        }
        // this is because new options may remove old value that is not valid anymore (and it shouldn't be removed, just marked as error)
        addInvalidValues();
    }

    @Override public void setPlaceholder(final String placeholder) {
        if (component instanceof NullableHasOptionsListBox) ((NullableHasOptionsListBox) component).setPlaceholder(placeholder);
        else super.setPlaceholder(placeholder);
    }

    @Nullable @Override public Object getValue() {
        checkScalarAccess(acceptsScalarAccess(), getModel().getName());
        return selection.isEmpty() ? null : selection.get(0);
    }

    @Override public void setValue(@Nullable final Object value) {
        setValue(value, false);
    }

    @Override public void setValue(@Nullable final Object value, boolean e) {
        checkScalarAccess(acceptsScalarAccess(), getModel().getName());
        selection.clear();

        if (value != null) selectOptions(Collections.singletonList(value), e);
        else component.clearSelection();
    }

    @NotNull @Override public Iterable<?> getValues() {
        checkArrayAccess(acceptsArrayAccess(), getModel().getName());
        return selection;
    }

    @Override public void setValues(@NotNull Iterable<Object> values) {
        setValues(values, false);
    }

    @Override public void setValues(@NotNull Iterable<Object> values, boolean e) {
        checkArrayAccess(acceptsArrayAccess(), getModel().getName());
        selection.clear();

        if (!isEmpty(values)) selectOptions(Colls.toList(values), e);
        else component.clearSelection();
    }

    boolean acceptsArrayAccess() {
        return getModel().isMultiple();
    }

    boolean acceptsScalarAccess() {
        return !getModel().isMultiple();
    }

    /** Clear user input data and options available. */
    @Override void clear() {
        super.clear();
        selection.clear();
        component.clearOptions();
    }

    void init(@NotNull final HasOptionsComponent c) {
        component = c;
        initWidget((com.google.gwt.user.client.ui.Widget) c);
    }

    /** True if selection is empty. */
    boolean uiSelectionIsEmpty() {
        return selection.isEmpty();
    }

    private void addFormattedOption(Object key, String value) {
        final String label      = isNotEmpty(value) ? value : inputHandler.format(key);
        final String coverLabel = getModel().isEntity() && !getModel().isFullText() ? coverText(label) : label;
        component.addOption(addOption(key), notNull(coverLabel));
    }

    /**
     * If the value of the combo isn't in the new options, then its set again as value and as
     * option.
     */
    private void addInvalidValues() {
        if (getModel().isMultiple()) {
            final Iterable<Object> oldValues = cast(getValues());
            if (!selection.isEmpty() && Colls.exists(oldValues, v -> options.get(v) == null)) setValues(oldValues);
        }
        else {
            final Object oldValue = getValue();
            if (oldValue != null && options.get(oldValue) == null) setValue(oldValue);
        }
    }

    private String addOption(@NotNull final Object option) {
        final String id = options.size() + "";
        options.put(option, id);
        return id;
    }

    private void selectOptions(@NotNull final List<Object> values, boolean e) {
        final List<String> ids = new ArrayList<>();

        for (final Object value : values) {
            final String id = options.get(value);
            if (id != null) ids.add(id);
            else {
                // if the value its not an option, then add it as an option
                addFormattedOption(value, value.toString());
                ids.add(options.get(value));
            }
            selection.add(value);
        }

        component.setSelectedOptions(ids, e);
    }  // end method selectOptions

    //~ Inner Interfaces .............................................................................................................................

    interface HasOptionsComponent extends IsWidget {
        /** Add change handler. */
        void addChangeHandler(final ValueChangeHandler<Object> changeHandler);

        /** Add option to component. */
        void addOption(@NotNull final String id, @NotNull final String label);

        /** Clear component options. */
        void clearOptions();

        /** Clear selected options. */
        void clearSelection();

        /** Returns true if it has null option. TESTING PURPOSES. */
        default boolean hasNullOption() {
            return false;
        }

        /** Explicitly add/remove widget's focus. */
        void setFocus(boolean focus);

        /** Set given option ids as selected and fire event if specified. */
        void setSelectedOptions(@NotNull final List<String> ids, boolean e);
    }

    //~ Inner Classes ................................................................................................................................

    protected class HasOptionsListBox extends ListBox implements HasOptionsComponent {
        boolean fireChangeEvent = true;

        public HasOptionsListBox(final boolean multipleSelect) {
            super();
            setMultipleSelect(multipleSelect);
            removeStyleName("gwt-ListBox");
        }

        public void addChangeHandler(final ValueChangeHandler<Object> changeHandler) {
            super.addChangeHandler(event -> {
                for (int i = 0; i < getItemCount(); i++) {
                    final String id = super.getValue(i);
                    uiSelectionUpdate(id, isItemSelected(i));
                }
                fireChange(changeHandler);
            });
        }

        @Override public void addOption(@NotNull String id, @NotNull String label) {
            super.addItem(label, id);
            setItemSelected(getItemCount() - 1, uiSelectionContains(id));
        }

        @Override public void clearOptions() {
            super.clear();
        }

        @Override public void clearSelection() {
            uiSelectionClear();
            setSelectedOptions(Colls.emptyList(), false);
        }

        @Override public void setSelectedOptions(@NotNull List<String> ids, boolean fireEvent) {
            for (int i = 0; i < getItemCount(); i++)
                setItemSelected(i, ids.contains(super.getValue(i)));

            if (fireEvent) DomEvent.fireNativeEvent(Document.get().createChangeEvent(), this);
        }

        void fireChange(ValueChangeHandler<Object> changeHandler) {
            if (fireChangeEvent) changeHandler.onValueChange(null);  // null event may be dangerous
        }
    }  // end class HasOptionsListBox

    protected class NullableHasOptionsListBox extends HasOptionsListBox {
        private boolean nullOption  = true;
        private String  placeHolder = "";

        public NullableHasOptionsListBox(final boolean multipleSelect) {
            super(multipleSelect);
            addEmptyNullOption();
        }

        @Override public void clearOptions() {
            super.clearOptions();
            if (nullOption) addEmptyNullOption();
        }

        @Override public void clearSelection() {
            super.clearSelection();
            if (!nullOption) addEmptyNullOption();
        }

        @Override public boolean hasNullOption() {
            return nullOption;
        }

        public void removeEmptyNullOption() {
            if (nullOption && getModel().isRequired()) {
                nullOption = false;
                removeItem(0);
            }
        }

        @Override public void setSelectedOptions(@NotNull List<String> ids, boolean fireEvent) {
            super.setSelectedOptions(ids, fireEvent);
            removeEmptyNullOption();
        }

        @Override void fireChange(ValueChangeHandler<Object> changeHandler) {
            super.fireChange(changeHandler);
            removeEmptyNullOption();
        }

        private void addEmptyNullOption() {
            nullOption      = true;
            fireChangeEvent = false;
            insertItem(placeHolder, "", 0);  // Add empty/null option
            if (uiSelectionIsEmpty()) setSelectedIndex(0);
            fireChangeEvent = true;
        }

        private void setPlaceholder(final String placeHolder) {
            this.placeHolder = placeHolder;
            if (nullOption) setItemText(0, placeHolder);
        }
    }  // end class NullableHasOptionsListBox
}  // end class HasOptionsUI
