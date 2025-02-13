
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package com.google.gwt.user.client.ui.datepicker;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * The {@link Spinner} provide two arrows for in- and decreasing values. A linked
 * {@link SpinnerListener}
 */
@SuppressWarnings("MagicNumber")
public class Spinner {

    //~ Instance Fields ..............................................................................................................................

    private final boolean constrained;
    private final Image   decrementArrow;
    private boolean       enabled = true;

    private final SpinnerResources images;
    private boolean                increment;
    private final Image            incrementArrow;
    private int                    initialSpeed = 7;
    private long                   max;
    private int                    maxStep;
    private long                   min;
    private int                    minStep;

    private final List<SpinnerListener> spinnerListeners = new ArrayList<>();
    private int                         step;
    private long                        value;

    private final Timer timer = new Timer() {
            private int counter = 0;
            private int speed   = 7;

            @Override public void cancel() {
                super.cancel();
                speed   = initialSpeed;
                counter = 0;
            }

            @Override public void run() {
                counter++;
                if (speed <= 0 || counter % speed == 0) {
                    speed--;
                    counter = 0;
                    if (increment) increase();
                    else decrease();
                }
                if (speed < 0 && step < maxStep) step += 1;
            }
        };

    //~ Constructors .................................................................................................................................

    /**
     * @param  spinner  the widget listening to the arrows
     * @param  value    initial value
     */
    public Spinner(SpinnerListener spinner, long value) {
        this(spinner, value, 0, 0, 1, 99, false);
    }

    /**
     * @param  spinner  the widget listening to the arrows
     * @param  value    initial value
     * @param  min      min value
     * @param  max      max value
     */
    public Spinner(SpinnerListener spinner, long value, long min, long max) {
        this(spinner, value, min, max, 1, 99, true);
    }

    /**
     * @param  spinner  the widget listening to the arrows
     * @param  value    initial value
     * @param  min      min value
     * @param  max      max value
     * @param  minStep  min value for stepping
     * @param  maxStep  max value for stepping
     */
    public Spinner(SpinnerListener spinner, long value, long min, long max, int minStep, int maxStep) {
        this(spinner, value, min, max, minStep, maxStep, true);
    }

    /**
     * @param  spinner      the widget listening to the arrows
     * @param  value        initial value
     * @param  min          min value
     * @param  max          max value
     * @param  minStep      min value for stepping
     * @param  maxStep      max value for stepping
     * @param  constrained  determines if min and max value will take effect
     */
    @SuppressWarnings("ConstructorWithTooManyParameters")
    public Spinner(SpinnerListener spinner, long value, long min, long max, int minStep, int maxStep, boolean constrained) {
        this(spinner, value, min, max, minStep, maxStep, constrained, GWT.create(SpinnerResources.class));
    }

    /**
     * @param  spinner      the widget listening to the arrows
     * @param  value        initial value
     * @param  min          min value
     * @param  max          max value
     * @param  minStep      min value for stepping
     * @param  maxStep      max value for stepping
     * @param  constrained  determines if min and max value will take effect
     * @param  images       the arrows images
     */
    @SuppressWarnings("ConstructorWithTooManyParameters")
    public Spinner(SpinnerListener spinner, long value, long min, long max, int minStep, int maxStep, boolean constrained,
                   final SpinnerResources images) {
        super();
        spinnerListeners.add(spinner);
        this.images      = images;
        this.value       = value;
        this.constrained = constrained;
        step             = minStep;
        this.minStep     = minStep;
        this.maxStep     = maxStep;
        this.min         = min;
        this.max         = max;
        initialSpeed     = INITIAL_SPEED;
        incrementArrow   = new Image(images.arrowUp());
        final MouseUpHandler mouseUpHandler = event -> {
                                                  if (enabled) cancelTimer((Widget) event.getSource());
                                              };
        incrementArrow.addMouseUpHandler(mouseUpHandler);
        final MouseDownHandler mouseDownHandler = event -> {
                                                      if (enabled) {
                                                          final Image sender = (Image) event.getSource();
                                                          if (sender == incrementArrow) {
                                                              sender.setResource(images.arrowUpPressed());
                                                              increment = true;
                                                              increase();
                                                          }
                                                          else {
                                                              sender.setResource(images.arrowDownPressed());
                                                              increment = false;
                                                              decrease();
                                                          }
                                                          timer.scheduleRepeating(30);
                                                      }
                                                  };
        incrementArrow.addMouseDownHandler(mouseDownHandler);
        final MouseOverHandler mouseOverHandler = event -> {
                                                      if (enabled) {
                                                          final Image sender = (Image) event.getSource();
                                                          if (sender == incrementArrow) sender.setResource(images.arrowUpHover());
                                                          else sender.setResource(images.arrowDownHover());
                                                      }
                                                  };
        incrementArrow.addMouseOverHandler(mouseOverHandler);
        final MouseOutHandler mouseOutHandler = event -> {
                                                    if (enabled) cancelTimer((Widget) event.getSource());
                                                };
        incrementArrow.addMouseOutHandler(mouseOutHandler);

        decrementArrow = new Image(images.arrowDown());
        decrementArrow.addMouseUpHandler(mouseUpHandler);
        decrementArrow.addMouseDownHandler(mouseDownHandler);
        decrementArrow.addMouseOverHandler(mouseOverHandler);
        decrementArrow.addMouseOutHandler(mouseOutHandler);
        fireOnValueChanged();
    }  // end ctor Spinner

    //~ Methods ......................................................................................................................................

    /** @param  listener  the listener to add */
    public void addSpinnerListener(SpinnerListener listener) {
        spinnerListeners.add(listener);
    }

    /** @param  listener  the listener to remove */
    public void removeSpinnerListener(SpinnerListener listener) {
        spinnerListeners.remove(listener);
    }

    /** @return  true is min and max values are active, false if not */
    public boolean isConstrained() {
        return constrained;
    }

    /** @return  Gets whether this widget is enabled */
    public boolean isEnabled() {
        return enabled;
    }

    /** @return  the image representing the decreating arrow */
    public Image getDecrementArrow() {
        return decrementArrow;
    }

    /**
     * Sets whether this widget is enabled.
     *
     * @param  enabled  true to enable the widget, false to disable it
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            incrementArrow.setResource(images.arrowUp());
            decrementArrow.setResource(images.arrowDown());
        }
        else {
            incrementArrow.setResource(images.arrowUpDisabled());
            decrementArrow.setResource(images.arrowDownDisabled());
        }
        if (!enabled) timer.cancel();
    }

    /** @return  the image representing the increasing arrow */
    public Image getIncrementArrow() {
        return incrementArrow;
    }

    /**
     * @param  initialSpeed  the initial speed of the spinner. Higher values mean lower speed,
     *                       default value is 7
     */
    public void setInitialSpeed(int initialSpeed) {
        this.initialSpeed = initialSpeed;
    }

    /** @return  the maximum value */
    public long getMax() {
        return max;
    }

    /** @param  max  the maximum value. Will not have any effect if constrained is set to false */
    public void setMax(long max) {
        this.max = max;
    }

    /** @return  the maximum spinner step */
    public int getMaxStep() {
        return maxStep;
    }

    /** @param  maxStep  the maximum step for this spinner */
    public void setMaxStep(int maxStep) {
        this.maxStep = maxStep;
    }

    /** @return  the minimum value */
    public long getMin() {
        return min;
    }

    /** @param  min  the minimum value. Will not have any effect if constrained is set to false */
    public void setMin(long min) {
        this.min = min;
    }

    /** @return  the minimum spinner step */
    public int getMinStep() {
        return minStep;
    }

    /** @param  minStep  the minimum step for this spinner */
    public void setMinStep(int minStep) {
        this.minStep = minStep;
    }

    /** @return  the current value */
    public long getValue() {
        return value;
    }

    /**
     * @param  value      sets the current value of this spinner
     * @param  fireEvent  fires value changed event if set to true
     */
    public void setValue(long value, boolean fireEvent) {
        this.value = value;
        if (fireEvent) fireOnValueChanged();
    }

    private void cancelTimer(Widget sender) {
        step = minStep;
        if (sender == incrementArrow) ((Image) sender).setResource(images.arrowUp());
        else ((Image) sender).setResource(images.arrowDown());
        timer.cancel();
    }

    /** Decreases the current value of the spinner by subtracting current step. */
    private void decrease() {
        value -= step;
        if (constrained && value < min) {
            value = min;
            timer.cancel();
        }
        fireOnValueChanged();
    }

    private void fireOnValueChanged() {
        for (final SpinnerListener listener : spinnerListeners)
            listener.onSpinning(value);
    }

    /** Increases the current value of the spinner by adding current step. */
    private void increase() {
        value += step;
        if (constrained && value > max) {
            value = max;
            timer.cancel();
        }
        fireOnValueChanged();
    }

    //~ Static Fields ................................................................................................................................

    private static final int INITIAL_SPEED = 7;

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Default resources for spinning arrows.
     */
    @SuppressWarnings("JavaDoc")
    public interface SpinnerResources extends ClientBundle {
        @Source("arrowDown.png")
        ImageResource arrowDown();
        @Source("arrowDownDisabled.png")
        ImageResource arrowDownDisabled();
        @Source("arrowDownHover.png")
        ImageResource arrowDownHover();
        @Source("arrowDownPressed.png")
        ImageResource arrowDownPressed();
        @Source("arrowUp.png")
        ImageResource arrowUp();
        @Source("arrowUpDisabled.png")
        ImageResource arrowUpDisabled();
        @Source("arrowUpHover.png")
        ImageResource arrowUpHover();
        @Source("arrowUpPressed.png")
        ImageResource arrowUpPressed();
    }
}  // end class Spinner
