
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.user.client.ui.RootPanel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.metadata.form.widget.ButtonType;
import tekgenesis.view.client.controller.FormController;
import tekgenesis.view.shared.response.RedirectFormResponse;

/**
 * Box handler.
 */
class BoxHandler {

    //~ Constructors .................................................................................................................................

    private BoxHandler() {}

    //~ Methods ......................................................................................................................................

    /** Attach a FormBox instance. If box already exists nothing is done. */
    static FormBox attach(@NotNull String boxId, @NotNull BoxConfiguration configuration) {
        FormBox result = boxes.get(boxId);
        if (result == null) {
            final RootPanel holder = RootPanel.get(boxId);
            holder.clear();                        // gwt clear
            holder.getElement().setInnerHTML("");  // dom clear

            result = configuration.isBoundedByHistory() ? new HistoryFormBox() : new DynamicFormBox();
            result.configure(configuration);
            result.attach(holder);
            boxes.put(boxId, result);
        }
        return result;
    }

    /** Cancel current form. */
    static void cancel(@NotNull String boxId) {
        final FormBox box = boxes.get(boxId);
        if (box != null) box.cancel();
    }

    /** Detach an existing FormBox instance. */
    static void detach(@NotNull String boxId) {
        final FormBox box = boxes.get(boxId);
        if (box != null) {
            box.unloadCurrent();
            box.detach();
            boxes.remove(boxId);
        }
    }

    /** Request box focus. */
    static void focus(@NotNull String boxId) {
        final FormBox box = boxes.get(boxId);
        if (box != null) box.getCurrent().getView().focusFirst();
    }

    /** Load specified form. */
    static void load(@NotNull String boxId, @NotNull String fqn, @Nullable String pk) {
        final FormBox box = boxes.get(boxId);
        if (box != null) box.load(fqn, pk);
    }

    /** Redirect specified form. */
    static void redirect(@NotNull String boxId, @NotNull final RedirectFormResponse redirect) {
        final FormBox box = boxes.get(boxId);
        if (box != null) box.redirect(redirect);
    }

    /** Add listener to box. */
    static void startListening(@NotNull String boxId, @NotNull FormBoxListener listener) {
        final FormBox box = boxes.get(boxId);
        if (box != null) box.addListener(listener);
    }

    /** Remove listener from box. */
    static void stopListening(@NotNull String boxId, @NotNull FormBoxListener listener) {
        final FormBox box = boxes.get(boxId);
        if (box != null) box.removeListener(listener);
    }

    static boolean isSomeoneDirty() {
        for (final FormBox formBox : boxes.values()) {
            final FormController current = formBox.getCurrent();
            if (current != null && current.getModel().isDirtyByUser() && isBoundedToEntity(formBox) &&
                current.getView().finder().byButtonType(ButtonType.SAVE).isPresent()) return true;
        }
        return false;
    }

    private static boolean isBoundedToEntity(FormBox formBox)
    {
        return !formBox.getCurrent().getModel().metadata().getBinding().getName().isEmpty();
    }

    //~ Static Fields ................................................................................................................................

    private static final Map<String, FormBox> boxes = new TreeMap<>();
}  // end class BoxHandler
