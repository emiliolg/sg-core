
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
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.TextBox;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.type.Kind;
import tekgenesis.view.client.RootInputHandler;
import tekgenesis.view.client.formatter.InputHandler;
import tekgenesis.view.client.ui.base.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

import static com.google.gwt.event.dom.client.KeyCodes.KEY_BACKSPACE;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_DELETE;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_ENTER;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_TAB;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.view.client.formatter.InputHandler.none;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.textField;

/**
 * A TextField UI widget.
 */
public class TextFieldUI extends BaseHasScalarValueUI implements HasInputHandlerUI, HasMaskUI, HasWidthUI {

    //~ Instance Fields ..............................................................................................................................

    private int changeDelay     = 0;
    private int changeThreshold = 0;

    private InputHandler<?> inputHandler;
    private Object          modelValue = null;

    private final TextBox textBox;

    //~ Constructors .................................................................................................................................

    /** Creates a TextField UI widget. */
    public TextFieldUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        this(container, model, model.isSecret());
    }

    /** Creates a TextField UI widget with secret option. */
    public TextFieldUI(@NotNull final ModelUI container, @NotNull final Widget model, boolean secret) {
        super(container, model);
        updateModelValue(null);
        inputHandler = none();
        textBox      = secret ? new PasswordTextField() : textField();
        textBox.setMaxLength(CHROME_DEFAULT_MAX_LENGTH);

        registerHandler();
        init(textBox, true);
        setDelayAndThreshold();
    }

    //~ Methods ......................................................................................................................................

    @Override public void setFocus(boolean focused) {
        setFocus(focused, true);
    }

    public void setFocus(boolean focused, boolean withSelection) {
        super.setFocus(focused);
        if (withSelection) textBox.selectAll();
    }

    @Override public void setInputHandler(InputHandler<?> inputHandler) {
        this.inputHandler = inputHandler;
    }

    /** Sets the max length and visible length of the text field. */
    public void setLength(final int length, boolean expand) {
        setLength(textBox, length);
    }

    /** Enters this text field in 'no paste' mode. */
    public void setNoPaste(boolean noPaste) {
        if (textBox instanceof DisablePasteBox) ((DisablePasteBox) textBox).setDisablePaste(noPaste);
    }

    @Nullable public Object getValue() {
        return modelValue;
    }

    public void setValue(@Nullable final Object v, boolean fireEvents) {
        updateModelValue(v);
        if (fireEvents) {
            // force blur to emulate user input on test
            textBox.setFocus(true);
            textBox.setValue(inputHandler.toString(v), true);
            textBox.setFocus(false);
        }
        else if (HtmlDomUtils.isFocused(textBox.getElement())) textBox.setValue(inputHandler.toString(modelValue));
        else {
            final InputHandler<Object> objectInputHandler = cast(inputHandler);
            textBox.setValue(objectInputHandler.format(modelValue));
        }
    }

    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName("textBox");
        if (getModel().getType() == Kind.DECIMAL) addStyleName("decimal");
    }

    /** Registers a drop handler. */
    void addDropHandler(DropHandler dropHandler) {
        textBox.addDropHandler(dropHandler);
    }

    /** Expose focus handler registration for Range delegation. */
    void addFocusHandler(@NotNull final FocusHandler focusHandler) {
        textBox.addFocusHandler(focusHandler);
    }

    /** Registers a key down handler. */
    void addKeyDownHandler(KeyDownHandler keyDownHandler) {
        textBox.addKeyDownHandler(keyDownHandler);
    }

    @NotNull @Override Option<Element> createIcon() {
        return some(textBox.getElement());
    }

    @Override Option<Icon> iconInWidget(Element icon, String iconStyle) {
        return Icon.inTextBox(textBox, iconStyle, getModel().isExpand());
    }

    private void registerHandler() {
        final Handler handler = new Handler();
        textBox.addKeyDownHandler(handler);
        textBox.addKeyPressHandler(handler);
        textBox.addKeyUpHandler(handler);
        textBox.addFocusHandler(handler);
        textBox.addBlurHandler(handler);
    }

    /** Changes the internal typed value of the widget. */
    private void updateModelValue(@Nullable final Object newVal) {
        modelValue = newVal;
    }

    private void setDelayAndThreshold() {
        final Widget widgetModel = getModel();
        // Delay
        changeDelay = widgetModel.hasChangeDelay() ? widgetModel.getChangeDelay() : -1;

        // Threshold
        final int userSuggestThreshold = widgetModel.getChangeThreshold();
        changeThreshold = userSuggestThreshold > 0 ? userSuggestThreshold : 0;
    }

    //~ Methods ......................................................................................................................................

    static void setLength(TextBox textBox, int length) {
        if (length > 0) {
            textBox.setMaxLength(length);
            textBox.setVisibleLength((int) Math.round(length * CAPS_SIZE_FIX));
        }
    }

    //~ Static Fields ................................................................................................................................

    static final double CAPS_SIZE_FIX = 1.5;  // Input size fix

    // This to set the max length equally in Chrome and Firefox. :-)
    private static final int CHROME_DEFAULT_MAX_LENGTH = 524288;

    private static final int KEY_UP_DELAY_MILLIS = 1000;
    private static final int KEY_C_MINUS         = 99;

    //~ Inner Classes ................................................................................................................................

    private class Handler implements KeyDownHandler, KeyPressHandler, KeyUpHandler, FocusHandler, BlurHandler {
        private final Timer valueChangeTimer;

        private Handler() {
            valueChangeTimer = new Timer() {
                    @Override public void run() {
                        ValueChangeEvent.fire(textBox, textBox.getText());
                    }
                };
        }

        /** leave 'edition mode' on blur. Display formatted value. */
        @Override public void onBlur(BlurEvent event) {
            final String filteredInput = inputHandler.filter(textBox.getText());  // filter not redundant when copy paste
            setValue(inputHandler.fromString(filteredInput));

            triggerChange();
        }

        /** enter 'edition mode' on focus. */
        @Override public void onFocus(FocusEvent event) {
            final String editValue = inputHandler.toString(modelValue);
            textBox.setValue(editValue);
            textBox.selectAll();
        }

        @Override public void onKeyDown(KeyDownEvent event) {
            final int keyCode = event.getNativeKeyCode();
            if (keyCode == KEY_TAB || keyCode == KEY_ENTER) triggerChange();
        }

        @Override public void onKeyPress(KeyPressEvent e) {
            final char charCode = e.getCharCode();

            if (charCode > 0 && charCode != KEY_ENTER && !e.isControlKeyDown() && !e.isMetaKeyDown()) {
                // Key enter should be ignored it inputs '/r'

                final String  text         = textBox.getText();
                final boolean mustOpenCalc = getModel().getType().isNumber() && charCode == KEY_C_MINUS;
                if (mustOpenCalc) textBox.setSelectionRange(0, 0);
                if (text.length() < textBox.getMaxLength()) {
                    // override default behaviour to filter invalid chars
                    e.preventDefault();
                    final int cursor = textBox.getCursorPos();

                    final String prefix    = text.substring(0, cursor);
                    final String suffix    = text.substring(cursor + textBox.getSelectionLength());
                    final String userInput = prefix + charCode + suffix;

                    final String filteredInput = inputHandler.filter(userInput);
                    if (!text.equals(filteredInput))
                    // char made it throw filter => valid char
                    // forward cursor for new strokes (not when replacing selected text)
                    updateEditValue(filteredInput, cursor + 1);
                }
                if (mustOpenCalc) RootInputHandler.getInstance().showCalculator(TextFieldUI.this);
            }
        }

        @Override public void onKeyUp(KeyUpEvent event) {
            final int keyCode = event.getNativeKeyCode();
            if (keyCode == KEY_BACKSPACE || keyCode == KEY_DELETE) {
                final String filteredInput = inputHandler.filter(textBox.getText());
                updateEditValue(filteredInput, textBox.getCursorPos());
            }
        }

        private void triggerChange() {
            valueChangeTimer.cancel();
            valueChangeTimer.run();
        }

        private void updateEditValue(@Nullable final String val, final int cursor) {
            textBox.setValue(val);
            if (isNotEmpty(val)) textBox.setCursorPos(max(0, min(cursor, val.length())));

            // update 'modelValue' with user input
            updateModelValue(inputHandler.fromString(val));

            if (textBox.getText().length() >= changeThreshold) {
                // delay the call to wait for the user to finish typing, to avoid calling the on change method
                if (isEmpty(getModel().getOnChangeMethodName()) && isEmpty(getModel().getOnUiChangeMethodName())) valueChangeTimer.run();
                else if (changeDelay >= 0) valueChangeTimer.schedule(changeDelay);
                else valueChangeTimer.schedule(KEY_UP_DELAY_MILLIS);
            }
        }
    }  // end class Handler
}  // end class TextFieldUI
