
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
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.PasswordTextBox;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.base.HtmlDomUtils;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;
import tekgenesis.view.client.ui.base.Icon;
import tekgenesis.view.client.ui.zxcvbn.Zxcvbn;
import tekgenesis.view.client.ui.zxcvbn.ZxcvbnResult;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.metadata.form.model.FormConstants.FORM_CONTROL;
import static tekgenesis.metadata.form.widget.IconType.STOP;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.div;
import static tekgenesis.view.client.ui.base.Icon.inWidget;

/**
 * Password Field Widget.
 */
public class PasswordTextFieldUI extends FieldWidgetUI implements HasKeyPressHandlers, HasScalarValueUI, HasWidthUI {

    //~ Instance Fields ..............................................................................................................................

    private int lastScore;

    private final PasswordTextBox textBox;
    private final Zxcvbn          zxcvbn = Zxcvbn.getInstance();

    //~ Constructors .................................................................................................................................

    /** PasswordTextFieldUI constructor. */
    public PasswordTextFieldUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);

        textBox = HtmlWidgetFactory.passwordTextBox();
        textBox.addStyleName(FORM_CONTROL);

        zxcvbn.ensureLoaded();

        initWidget(buildPasswordWidget(model));

        setFocusTarget(Option.option(textBox));
    }

    //~ Methods ......................................................................................................................................

    @Override public void addChangeHandler(ValueChangeHandler<Object> handler) {
        final HasValue<Object> box = cast(textBox);
        box.addValueChangeHandler(handler);
    }

    @Override public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
        return textBox.addKeyPressHandler(handler);
    }

    /** Gets last score. */
    public int getLastScore() {
        return lastScore;
    }

    /** Sets the max length and visible length of the text field. */
    public void setLength(final int length, boolean expand) {
        if (!expand) HtmlDomUtils.setMaxWidth(textBox.getElement(), length == 0 ? FormConstants.DEFAULT_LENGTH : length);
    }

    @Nullable @Override public Object getValue() {
        final String val = textBox.getValue();
        return isEmpty(val) ? null : val;
    }

    @Override public void setValue(@Nullable Object modelValue) {
        setValue(modelValue, false);
    }

    @Override public void setValue(@Nullable Object modelValue, boolean fireEvents) {
        textBox.setValue((String) modelValue, fireEvents);
    }

    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName("passwordTextBox");
    }

    @NotNull @Override Option<Element> createIcon() {
        return some(textBox.getElement());
    }

    @Override Option<Icon> iconInWidget(Element icon, String iconStyle) {
        return Icon.inTextBox(textBox, iconStyle, getModel().isExpand());
    }

    @NotNull private FlowPanel buildPasswordWidget(Widget model) {
        final FlowPanel container = div();

        if (model.hasMetering()) {
            final FlowPanel dots = div();
            dots.setStyleName(PASSWORD_STRENGTH_INDICATOR);

            inWidget(dots, STOP);
            inWidget(dots, STOP);
            inWidget(dots, STOP);
            inWidget(dots, STOP);

            textBox.addKeyUpHandler(event -> {
                dots.setStyleName(PASSWORD_STRENGTH_INDICATOR);
                dots.setTitle("");

                final String text = textBox.getText();
                if (text.isEmpty()) return;

                final ZxcvbnResult result = zxcvbn.testPassword(text);
                lastScore = result.getScore();

                switch (lastScore) {
                case 0:
                    dots.addStyleName("weak");
                    dots.setTitle(MSGS.weak());
                    break;
                case 1:
                    dots.addStyleName("so-so");
                    dots.setTitle(MSGS.soSo());
                    break;
                case 2:
                    dots.addStyleName("good");
                    dots.setTitle(MSGS.good());
                    break;
                case 3:
                case 4:
                    dots.addStyleName("great");
                    dots.setTitle(MSGS.great());
                    break;
                }
            });
            container.add(textBox);
            container.add(dots);
        }
        else container.add(textBox);

        return container;
    }  // end method buildPasswordWidget

    //~ Static Fields ................................................................................................................................

    private static final String PASSWORD_STRENGTH_INDICATOR = "password-strength-indicator";
}  // end class PasswordTextFieldUI
