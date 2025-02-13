
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.base;

import com.google.gwt.user.client.ui.HasWidgets;

import org.jetbrains.annotations.Nullable;

import tekgenesis.type.Kind;
import tekgenesis.view.client.RootInputHandler;
import tekgenesis.view.client.ui.CalculatorUI;
import tekgenesis.view.client.ui.TextFieldUI;

import static java.lang.Double.parseDouble;
import static java.lang.String.valueOf;

import static tekgenesis.metadata.form.model.FormConstants.DISPLAY_WIDGET;
import static tekgenesis.metadata.form.widget.PopoverType.RIGHT;

public class CalculatorPopover extends Popover {

    //~ Instance Fields ..............................................................................................................................

    private final CalculatorUI calculatorUI;
    private final TextFieldUI  textFieldUI;

    //~ Constructors .................................................................................................................................

    public CalculatorPopover(@Nullable TextFieldUI textFieldUI) {
        super(RIGHT);
        addStyleName("calculator-popover");
        addStyleName(DISPLAY_WIDGET);
        calculatorUI = new CalculatorUI();
        addContent(calculatorUI);
        this.textFieldUI = textFieldUI;
        if (textFieldUI != null) this.textFieldUI.setFocus(false);
        showContent();
    }

    //~ Methods ......................................................................................................................................

    @Override public void hideContent() {
        super.hideContent();
        calculatorUI.hided();
        if (textFieldUI != null) writeResult();
        RootInputHandler.getInstance().closedCalculator();
        ((HasWidgets) getParent()).remove(this);
    }

    private void writeResult() {
        final String  displayText = calculatorUI.getDisplayText();
        final boolean intType     = textFieldUI.getModel().getType().getKind() == Kind.INT;
        final Object  value       = textFieldUI.getValue();
        if (!"0".equals(displayText)) textFieldUI.setValue(intType ? valueOf((int) parseDouble(displayText)) : displayText, true);
        textFieldUI.setFocus(true, false);
        textFieldUI.initPrev(value != null ? value.toString() : "");
    }
}
