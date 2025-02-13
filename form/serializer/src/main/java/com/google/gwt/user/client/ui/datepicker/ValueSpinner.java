
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package com.google.gwt.user.client.ui.datepicker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.resources.client.ClientBundleWithLookup;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import org.jetbrains.annotations.Nullable;

/**
 * A {@link ValueSpinner} is a combination of a {@link TextBox} and a {@link Spinner} to allow
 * spinning.
 *
 * <h3>CSS Style Rules</h3>
 *
 * <ul class='css'>
 *   <li>.gwt-ValueSpinner { primary style }</li>
 *   <li>.gwt-ValueSpinner .textBox { the text-box }</li>
 *   <li>.gwt-ValueSpinner .arrows { the spinner arrows }</li>
 * </ul>
 */
@SuppressWarnings({ "MagicNumber", "AssignmentToStaticFieldFromInstanceMethod", "NonThreadSafeLazyInitialization" })
public class ValueSpinner extends HorizontalPanel {

    //~ Instance Fields ..............................................................................................................................

    private final Spinner spinner;
    private final TextBox valueBox = new TextBox();

    private final SpinnerListener spinnerListener = value -> {
                                                        if (getSpinner() != null) getSpinner().setValue(value, false);
                                                        valueBox.setText(formatValue(value));
                                                    };

    //~ Constructors .................................................................................................................................

    /** @param  value  initial value */
    @SuppressWarnings("WeakerAccess")  // component
    public ValueSpinner(long value) {
        this(value, 0, 0, 1, 99, false);
    }

    /**
     * @param  value   initial value
     * @param  styles  the styles and images used by this widget
     * @param  images  the images used by the spinner
     */
    @SuppressWarnings("WeakerAccess")  // component
    public ValueSpinner(long value, @Nullable ValueSpinnerResources styles, @Nullable Spinner.SpinnerResources images) {
        this(value, 0, 0, 1, 99, false, styles, images);
    }

    /**
     * @param  value  initial value
     * @param  min    min value
     * @param  max    max value
     */
    @SuppressWarnings("WeakerAccess")  // component
    public ValueSpinner(long value, int min, int max) {
        this(value, min, max, 1, 99, true);
    }

    /**
     * @param  value    initial value
     * @param  min      min value
     * @param  max      max value
     * @param  minStep  min value for stepping
     * @param  maxStep  max value for stepping
     */
    @SuppressWarnings("WeakerAccess")  // component
    public ValueSpinner(long value, int min, int max, int minStep, int maxStep) {
        this(value, min, max, minStep, maxStep, true);
    }

    /**
     * @param  value        initial value
     * @param  min          min value
     * @param  max          max value
     * @param  minStep      min value for stepping
     * @param  maxStep      max value for stepping
     * @param  constrained  if set to false min and max value will not have any effect
     */
    @SuppressWarnings("WeakerAccess")  // component
    public ValueSpinner(long value, int min, int max, int minStep, int maxStep, boolean constrained) {
        this(value, min, max, minStep, maxStep, constrained, null);
    }

    /**
     * @param  value        initial value
     * @param  min          min value
     * @param  max          max value
     * @param  minStep      min value for stepping
     * @param  maxStep      max value for stepping
     * @param  constrained  if set to false min and max value will not have any effect
     * @param  resources    the styles and images used by this widget
     */
    @SuppressWarnings({ "WeakerAccess", "ConstructorWithTooManyParameters" })
    public ValueSpinner(long value, int min, int max, int minStep, int maxStep, boolean constrained, @Nullable ValueSpinnerResources resources) {
        this(value, min, max, minStep, maxStep, constrained, resources, null);
    }

    /**
     * @param  value        initial value
     * @param  min          min value
     * @param  max          max value
     * @param  minStep      min value for stepping
     * @param  maxStep      max value for stepping
     * @param  constrained  if set to false min and max value will not have any effect
     * @param  resources    the styles and images used by this widget
     * @param  images       the images used by the spinner
     */
    @SuppressWarnings({ "WeakerAccess", "ConstructorWithTooManyParameters" })
    public ValueSpinner(long value, int min, int max, int minStep, int maxStep, boolean constrained, @Nullable ValueSpinnerResources resources,
                        @Nullable Spinner.SpinnerResources images) {
        super();
        if (resources != null) defaultResources = resources;
        else if (defaultResources == null) defaultResources = GWT.create(ValueSpinnerResources.class);

        final Style style = defaultResources.valueSpinnerStyle();
        style.ensureInjected();

        setStylePrimaryName(Style.GWT_VALUE_SPINNER);
        if (images == null) spinner = new Spinner(spinnerListener, value, min, max, minStep, maxStep, constrained);
        else spinner = new Spinner(spinnerListener, value, min, max, minStep, maxStep, constrained, images);
        valueBox.setStyleName(style.textBox());
        final KeyPressHandler keyPressHandler = event -> {
                                                    final int    index        = valueBox.getCursorPos();
                                                    final String previousText = valueBox.getText();
                                                    final String newText;
                                                    if (valueBox.getSelectionLength() > 0)
                                                        newText = previousText.substring(0, valueBox.getCursorPos()) + event.getCharCode() +
                                                                  previousText.substring(valueBox.getCursorPos() + valueBox.getSelectionLength(),
                                                                previousText.length());
                                                    else
                                                        newText = previousText.substring(0, index) + event.getCharCode() +
                                                                  previousText.substring(index, previousText.length());
                                                    valueBox.cancelKey();
                                                    try {
                                                        final long newValue = parseValue(newText);
                                                        if (spinner.isConstrained() && (newValue > spinner.getMax() || newValue < spinner.getMin()))
                                                            return;
                                                        spinner.setValue(newValue, true);
                                                    }
                                                    catch (final Exception e) {
                                                        // valueBox.cancelKey();
                                                    }
                                                };
        valueBox.addKeyPressHandler(keyPressHandler);
        // valueBox.getElement().setPropertyString("type", "number");
        setVerticalAlignment(ALIGN_MIDDLE);
        add(valueBox);
        final VerticalPanel arrowsPanel = new VerticalPanel();
        arrowsPanel.setStylePrimaryName(style.arrows());
        arrowsPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
        arrowsPanel.add(spinner.getIncrementArrow());
        arrowsPanel.add(spinner.getDecrementArrow());
        add(arrowsPanel);
    }  // end ctor ValueSpinner

    //~ Methods ......................................................................................................................................

    /** @return  whether this widget is enabled. */
    public boolean isEnabled() {
        return spinner.isEnabled();
    }

    /**
     * Sets whether this widget is enabled.
     *
     * @param  enabled  true to enable the widget, false to disable it
     */
    public void setEnabled(boolean enabled) {
        spinner.setEnabled(enabled);
        valueBox.setEnabled(enabled);
    }

    /** @return  the Spinner used by this widget */
    public Spinner getSpinner() {
        return spinner;
    }

    /** @return  the SpinnerListener used to listen to the {@link Spinner} events */
    public SpinnerListener getSpinnerListener() {
        return spinnerListener;
    }

    /** @return  the TextBox used by this widget */
    public TextBox getTextBox() {
        return valueBox;
    }

    /**
     * @param   value  the value to format
     *
     * @return  the formatted value
     */
    String formatValue(long value) {
        return String.valueOf(value);
    }

    /**
     * @param   value  the value to parse
     *
     * @return  the parsed value
     */
    long parseValue(String value) {
        return Long.valueOf(value);
    }

    //~ Static Fields ................................................................................................................................

    private static ValueSpinnerResources defaultResources = null;

    //~ Inner Interfaces .............................................................................................................................

    @CssResource.ImportedWithPrefix(Style.GWT_VALUE_SPINNER)
    @SuppressWarnings({ "JavaDoc", "SpellCheckingInspection" })
    public interface Style extends CssResource {
        String DEFAULT_CSS       = "com/google/gwt/user/client/ui/datepicker/ValueSpinner.css";
        String GWT_VALUE_SPINNER = "gwt-ValueSpinner";

        String arrows();
        String textBox();
    }

    /**
     * Resources used.
     */
    public interface ValueSpinnerResources extends ClientBundleWithLookup {
        /** The styles used in this widget. */
        @Source(Style.DEFAULT_CSS)
        Style valueSpinnerStyle();
    }
}  // end class ValueSpinner
