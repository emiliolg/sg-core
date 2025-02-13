
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import com.google.gwt.user.client.ui.PopupPanel;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.view.shared.response.FormModelResponse;
import tekgenesis.view.shared.response.RedirectFormResponse;

import static tekgenesis.view.client.DynamicFormBox.needsConfirmation;

/**
 * Fixed box for Swiper.
 */
public class SwipeFormBox extends FormBox {

    //~ Instance Fields ..............................................................................................................................

    private final FormBox    parent;
    private final PopupPanel popupPanel;

    //~ Constructors .................................................................................................................................

    SwipeFormBox(@NotNull FormModel model, BoxConfiguration configuration, FormBox formBox, PopupPanel swipePopup) {
        configure(configuration);
        setCurrent(model, "");  // should the pk be in the model? instead of just an 'update' flag?
        parent     = formBox;
        popupPanel = swipePopup;
    }

    //~ Methods ......................................................................................................................................

    @Override protected void onSync(FormModelResponse response) {
        invoke(response.getInvoke());
        if (response.hasDownload()) download(response.getDownload());

        getCurrent().modelUpdated(response.getSync());
        // navigation
        final RedirectFormResponse redirect = response.getRedirect();
        if (redirect != null) {
            // Hide all messages.
            Application.messages().hide(this);
            // Note that navigation in swipe boxes will always 'leave' current form.
            redirect.withCallback(null);
            if (needsConfirmation(response, redirect, this::onSync)) return;
            Application.getInstance().hideActiveModal(true);
            redirect(redirect);
            asynchronousInvoke(response);
        }
    }

    @Override protected void onTermination(FormModelResponse response) {
        // Static Boxes doesn't support redirection.
    }

    @Override protected void redirect(@NotNull RedirectFormResponse redirect) {
        // Navigation in swipe boxes will close the swipe and navigate the parent form.
        popupPanel.hide();
        parent.redirect(redirect);
    }
}  // end class SwipeFormBox
