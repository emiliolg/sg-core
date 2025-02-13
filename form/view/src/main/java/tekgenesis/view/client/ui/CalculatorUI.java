
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.view.client.ui.Calculator.CalcOperator;
import tekgenesis.view.client.ui.base.Icon;

import static tekgenesis.metadata.form.model.FormConstants.BTN_DANGER;
import static tekgenesis.metadata.form.model.FormConstants.BTN_INFO;
import static tekgenesis.metadata.form.model.FormConstants.BTN_SUCCESS;
import static tekgenesis.metadata.form.model.FormConstants.BTN_XS;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.div;

public class CalculatorUI extends FocusPanel {

    //~ Instance Fields ..............................................................................................................................

    private final Calculator          calculator;
    private final TextBox             display;
    private final HandlerRegistration keyDownHandler;
    private final HandlerRegistration keyPressHandler;
    private final FocusPanel          panel;

    //~ Constructors .................................................................................................................................

    public CalculatorUI() {
        super();

        panel = this;
        final FlowPanel calculatorBox = div();
        calculatorBox.addStyleName(CALCULATOR);

        final FlowPanel calcDisplay = div();
        calcDisplay.addStyleName(CALC_DISPLAY);

        display = new TextBox();
        display.setEnabled(false);
        display.setText("0");
        calcDisplay.add(display);

        final Button     one        = new CalcButton("1");
        final Button     two        = new CalcButton("2");
        final Button     three      = new CalcButton("3");
        final Button     four       = new CalcButton("4");
        final Button     five       = new CalcButton("5");
        final Button     six        = new CalcButton("6");
        final Button     seven      = new CalcButton("7");
        final Button     eight      = new CalcButton("8");
        final Button     nine       = new CalcButton("9");
        final Button     zero       = new CalcButton("0");
        final Button     plus       = new OperatorButton(CalcOperator.PLUS);
        final Button     minus      = new OperatorButton(CalcOperator.SUBS);
        final Button     divide     = new OperatorButton(CalcOperator.DIV);
        final Button     mul        = new OperatorButton(CalcOperator.MUL);
        final Button     c          = new CButton(CalcOperator.C);
        final Button     equals     = new OperatorButton(CalcOperator.EQUALS);
        final CalcButton percentage = new OperatorButton(CalcOperator.PERCENTAGE);
        final CalcButton dot        = new CalcButton(".");
        final Button     back       = new BackButton(CalcOperator.BACK);

        final FlowPanel keysRow1 = new KeysDiv(seven, eight, nine, c, back);
        final FlowPanel keysRow2 = new KeysDiv(four, five, six, plus, mul);
        final FlowPanel keysRow3 = new KeysDiv(one, two, three, minus, divide);
        final FlowPanel keysRow4 = new KeysDiv(zero, dot, equals, percentage);

        final FlowPanel keypad = new KeypadDiv(keysRow1, keysRow2, keysRow3, keysRow4);

        calculatorBox.add(calcDisplay);
        calculatorBox.add(keypad);
        add(calculatorBox);

        final KeyHandler handler = new KeyHandler();
        keyPressHandler = RootPanel.get().addDomHandler(handler, KeyPressEvent.getType());
        keyDownHandler  = RootPanel.get().addDomHandler(handler, KeyDownEvent.getType());

        calculator = new Calculator(display);
    }  // end ctor CalculatorUI

    //~ Methods ......................................................................................................................................

    public void hided() {
        keyPressHandler.removeHandler();
        keyDownHandler.removeHandler();
    }

    public String getDisplayText() {
        return display.getText();
    }

    private void keyPressed(int keyCode) {
        calculator.keyPressed(keyCode);
        panel.setFocus(true);
    }

    private void updateDisplay() {
        display.setText(calculator.getDisplayText());
    }

    //~ Static Fields ................................................................................................................................

    private static final String CALCULATOR   = "calculator";
    private static final String CALC_DISPLAY = "calc-display";
    private static final String KEYPAD       = "keypad";
    private static final String KEYS         = "keys";

    //~ Inner Classes ................................................................................................................................

    private class BackButton extends OperatorButton {
        public BackButton(CalcOperator operator) {
            super(operator);
        }

        @Override void addStyleColor() {
            addStyleName(BTN_DANGER);
            Icon.inWidget(this, IconType.ARROW_LEFT);
        }
    }

    private class CalcButton extends Button {
        public CalcButton(String label) {
            super(label);
            addStyleName(BTN_XS);
            addStyleColor();
            addEvent(label);
        }

        void addEvent(String label) {
            addClickHandler(clickEvent -> {
                calculator.calculatorEvent(label);
                panel.setFocus(true);
            });
        }

        void addStyleColor() {
            addStyleName(BTN_INFO);
        }
    }

    private class CButton extends OperatorButton {
        public CButton(CalcOperator operator) {
            super(operator);
        }

        @Override void addStyleColor() {
            addStyleName(BTN_DANGER);
        }
    }

    private class KeyHandler implements KeyPressHandler, KeyDownHandler {
        @Override public void onKeyDown(KeyDownEvent event) {
            final int keyCode = event.getNativeKeyCode();
            if (keyCode == KeyCodes.KEY_BACKSPACE) {
                event.preventDefault();
                keyPressed(KeyCodes.KEY_BACKSPACE);
            }
        }

        @Override public void onKeyPress(KeyPressEvent event) {
            final int keyCode = event.getCharCode();
            keyPressed(keyCode);
        }
    }

    private class KeypadDiv extends FlowPanel {
        public KeypadDiv(FlowPanel... rows) {
            super();
            addStyleName(KEYPAD);
            for (final FlowPanel row : rows)
                add(row);
        }
    }

    private class KeysDiv extends FlowPanel {
        public KeysDiv(Button... buttons) {
            super();
            addStyleName(KEYS);
            for (final Button button : buttons)
                add(button);
        }
    }

    private class OperatorButton extends CalcButton {
        private final CalcOperator operator;

        public OperatorButton(CalcOperator operator) {
            super(operator.symbol());
            this.operator = operator;
        }

        @Override void addEvent(String label) {
            addClickHandler(clickEvent -> {
                calculator.operatorEvent(operator);
                panel.setFocus(true);
            });
        }

        @Override void addStyleColor() {
            addStyleName(BTN_SUCCESS);
        }
    }
}  // end class CalculatorUI
