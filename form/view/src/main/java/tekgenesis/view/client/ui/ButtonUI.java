
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.EnumMap;

import com.google.gwt.user.client.ui.Button;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.widget.ButtonType;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Maps.enumMap;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.metadata.form.model.FormConstants.BTN_PRIMARY;
import static tekgenesis.metadata.form.widget.ButtonType.*;
import static tekgenesis.view.client.FormViewMessages.MSGS;

/**
 * Button UI widget.
 */
public class ButtonUI extends BaseButtonUI {

    //~ Constructors .................................................................................................................................

    /** Creates Button UI widget. */
    public ButtonUI(@NotNull final ModelUI container, @NotNull final Widget model, boolean update) {
        super(container, model);
        setText(resolveLabel(update));
    }

    //~ Methods ......................................................................................................................................

    /** Cancel buttons are always available. */
    public void setReadOnly(boolean readOnly) {
        if (getModel().getButtonType() == CANCEL) return;
        super.setReadOnly(readOnly);
    }

    @Override protected void addButtonStyle() {
        final String style = defaultStyles.get(getModel().getButtonType());
        if (isNotEmpty(style)) button.addStyleName(style);
    }

    private String resolveLabel(boolean update) {
        String text = getModel().getLabel();
        if (isEmpty(text)) {
            switch (getModel().getButtonType()) {
            case SAVE:
                text = getModel().isSavesEntity() ? (update ? MSGS.update() : MSGS.create()) : MSGS.save();
                break;
            case DELETE:
                text = MSGS.delete();
                break;
            case CANCEL:
                text = MSGS.cancel();
                break;
            case CUSTOM:
                text = "";
                break;
            case ADD_ROW:
                text = getModel().getIconStyle().isEmpty() ? MSGS.add() : "";
                break;
            case REMOVE_ROW:
                text = getModel().getIconStyle().isEmpty() ? MSGS.remove() : "";
                break;
            case EXPORT:
                text = getModel().getIconStyle().isEmpty() ? MSGS.export() : "";
                break;
            case VALIDATE:
                text = MSGS.validate();
                break;
            }
        }
        return text;
    }

    //~ Methods ......................................................................................................................................

    /** Package method to be used by FormView to create action buttons. */
    static Button createActionButton(@NotNull String action, @NotNull String label) {
        final Button b = HtmlWidgetFactory.button();
        b.getElement().setId(action);
        b.setText(label);
        b.addStyleName(BTN_PRIMARY);
        return b;
    }

    //~ Static Fields ................................................................................................................................

    private static final EnumMap<ButtonType, String> defaultStyles = enumMap(tuple(SAVE, BTN_PRIMARY), tuple(DELETE, "btn-danger"));
}  // end class ButtonUI
