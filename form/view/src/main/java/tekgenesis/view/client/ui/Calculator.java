
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.TextBox;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.logging.Logger;

import static java.lang.Double.parseDouble;

import static com.google.gwt.event.dom.client.KeyCodes.KEY_BACKSPACE;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_ENTER;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_NINE;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_ZERO;

import static tekgenesis.view.client.ui.Calculator.CalcOperator.*;

public class Calculator {

    //~ Instance Fields ..............................................................................................................................

    private boolean      changed         = true;
    private CalcOperator currentOperator = null;

    private final TextBox display;
    private double        memoryNumber;
    private double        number;

    //~ Constructors .................................................................................................................................

    public Calculator(TextBox display) {
        this.display = display;
    }

    //~ Methods ......................................................................................................................................

    /*
     *  Method triggered when a number or dot is pressed
     */
    public void calculatorEvent(String label) {
        final String text = display.getText();
        if ((".".equals(label) && text.contains(".") && !changed) || (!changed && text.length() >= 15)) return;
        if (changed || "0".equals(text)) {
            if (!"0".equals(label)) {
                displayText(label);
                changed = false;
            }
        }
        else displayText(text + label);
    }

    public void keyPressed(int keyCode) {
        if (keyCode == DOT_KEY || (keyCode >= KEY_ZERO && keyCode <= KEY_NINE)) calculatorEvent(String.valueOf((char) keyCode));
        else {
            final CalcOperator operator = operatorMap.get(keyCode);
            if (operator != null) operatorEvent(operator);
        }
    }

    /*
     *  Method triggered when a operator is pressed
     */
    public void operatorEvent(CalcOperator operator) {
        switch (operator) {
        case C:
            resetEvent();
            break;
        case EQUALS:
            saveOperatorAndCalculateResult(null);
            break;
        case BACK:
            backEvent();
            break;
        case DOUBLE_ZERO:
            insertDoubleZero();
            break;
        case TRIPLE_ZERO:
            insertTripleZero();
            break;
        case MEMORY_RECOVER:
            recoverFromMemory();
            break;
        case MEMORY_ADD:
            addToMemory();
            break;
        case MEMORY_SUB:
            substractToMemory();
            break;
        case MEMORY_EXCHANGE:
            memoryExchange();
            break;
        case MEMORY_INSERT:
            insertMemory();
            break;
        case MEMORY_CLEAN:
            cleanMemory();
            break;
        case FACTORIAL:
            factorial();
            break;
        case CHANGE_SIGN:
            changeSign();
            break;
        default:
            saveOperatorAndCalculateResult(operator);
        }
    }  // end method operatorEvent

    public String getDisplayText() {
        return display.getText();
    }

    private void addToMemory() {
        if (!isJustADot()) memoryNumber += getDisplayNumber();
    }

    private void backEvent() {
        final String text = display.getText();
        if (text.length() <= 1 || changed) {
            displayText("0");
            changed = true;
        }
        else displayText(text.substring(0, text.length() - 1));
    }

    private void changeSign() {
        final String text = getDisplayText();
        if ("0".equals(text) || isJustADot()) return;
        if (text.charAt(0) != '-') displayText("-" + text);
        else displayText(text.substring(1));
        changed = false;
    }

    private void cleanMemory() {
        memoryNumber = 0;
    }

    private void displayText(String text) {
        display.setText(text);
    }

    private void factorial() {
        if (isJustADot()) return;
        int result = (int) getDisplayNumber();
        for (int i = result - 1; i > 0; i--)
            result *= i;
        displayText(String.valueOf(result));
        number = result;
    }

    private void insertDoubleZero() {
        if (!changed) displayText(display.getText() + "00");
    }

    private void insertMemory() {
        if (!isJustADot()) memoryNumber = getDisplayNumber();
    }

    private void insertTripleZero() {
        if (!changed) displayText(display.getText() + "000");
    }

    private void makeBasicOperationAndDisplay(double displayNumber) {
        switch (currentOperator) {
        case PLUS:
            number = number + displayNumber;
            break;
        case MUL:
            number = number * displayNumber;
            break;
        case DIV:
            number = number / displayNumber;
            break;
        case SUBS:
            number = number - displayNumber;
            break;
        case PERCENTAGE:
            number = number / 100 * displayNumber;
            break;
        default:
        }
        displayText(String.valueOf(number));
    }

    private void memoryExchange() {
        if (isJustADot()) return;
        final double displayNumber = getDisplayNumber();
        displayText(String.valueOf(memoryNumber));
        memoryNumber = displayNumber;
        changed      = false;
    }

    private void recoverFromMemory() {
        displayText(String.valueOf(memoryNumber));
        changed = false;
    }

    private void resetEvent() {
        displayText("0");
        changed         = true;
        currentOperator = null;
        number          = 0;
    }

    private void saveOperatorAndCalculateResult(@Nullable CalcOperator op) {
        if ((currentOperator != null && changed) || isJustADot()) return;
        if (currentOperator != null) makeBasicOperationAndDisplay(getDisplayNumber());
        changed         = true;
        currentOperator = op;
        number          = getDisplayNumber();
    }

    private void substractToMemory() {
        if (!isJustADot()) memoryNumber -= getDisplayNumber();
    }

    private double getDisplayNumber() {
        try {
            return parseDouble(getDisplayText());
        }
        catch (final NumberFormatException ignore) {
            Logger.getLogger(Calculator.class).warning("Somehow a number is not in the display:  \"" + getDisplayText() + "\"");
            return 0;
        }
    }

    private boolean isJustADot() {
        return ".".equals(getDisplayText());
    }

    //~ Methods ......................................................................................................................................

    private static Map<Integer, CalcOperator> getOperatorsMap() {
        final Map<Integer, CalcOperator> map = new HashMap<>();
        map.put(PLUS.getCharCode(), PLUS);
        map.put(MUL.getCharCode(), MUL);
        map.put(SUBS.getCharCode(), SUBS);
        map.put(DIV.getCharCode(), DIV);
        map.put(PERCENTAGE.getCharCode(), PERCENTAGE);
        map.put(EQUALS.getCharCode(), EQUALS);
        map.put(KEY_ENTER, EQUALS);
        map.put(KEY_BACKSPACE, BACK);
        map.put(C.getCharCode(), C);
        map.put(DOUBLE_ZERO.getCharCode(), DOUBLE_ZERO);
        map.put(TRIPLE_ZERO.getCharCode(), TRIPLE_ZERO);
        map.put(MEMORY_RECOVER.getCharCode(), MEMORY_RECOVER);
        map.put(MEMORY_ADD.getCharCode(), MEMORY_ADD);
        map.put(MEMORY_SUB.getCharCode(), MEMORY_SUB);
        map.put(MEMORY_EXCHANGE.getCharCode(), MEMORY_EXCHANGE);
        map.put(MEMORY_INSERT.getCharCode(), MEMORY_INSERT);
        map.put(MEMORY_CLEAN.getCharCode(), MEMORY_CLEAN);
        map.put(FACTORIAL.getCharCode(), FACTORIAL);
        map.put(CHANGE_SIGN.getCharCode(), CHANGE_SIGN);
        return map;
    }

    //~ Static Fields ................................................................................................................................

    private static final Map<Integer, CalcOperator> operatorMap = getOperatorsMap();
    private static final int                        DOT_KEY     = 46;

    //~ Enums ........................................................................................................................................

    public enum CalcOperator {
        PLUS("+"), MUL("x"), SUBS("-"), DIV("/"), PERCENTAGE("%"), EQUALS("="), BACK(""), CHANGE_SIGN("_"), DOUBLE_ZERO("Q"), TRIPLE_ZERO("W"),
        MEMORY_RECOVER("R"), MEMORY_ADD("A"), MEMORY_SUB("S"), MEMORY_EXCHANGE("X"), MEMORY_INSERT("M"), MEMORY_CLEAN("L"), FACTORIAL("!"), C("C");

        private final String symbol;

        CalcOperator(String symbol) {
            this.symbol = symbol;
        }

        String symbol() {
            return symbol;
        }

        int getCharCode() {
            return (int) symbol.charAt(0);
        }
    }
}  // end class Calculator
