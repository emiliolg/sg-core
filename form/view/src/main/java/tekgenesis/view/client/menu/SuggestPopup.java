
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.menu;

import com.google.gwt.user.client.ui.TekSuggestBox;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.view.client.suggest.ItemSuggestion;
import tekgenesis.view.client.suggest.KeySuggestOracle;
import tekgenesis.view.client.ui.modal.ModalContent;
import tekgenesis.view.client.ui.modal.ModalListener;

import static tekgenesis.metadata.form.model.FormConstants.BLOCK_INPUT;
import static tekgenesis.view.client.FormHistory.getFormHistory;
import static tekgenesis.view.client.FormViewMessages.MSGS;

/**
 * Modal dialog for menu shortcuts: recent forms and a forms suggest box to search for forms *
 */
public class SuggestPopup extends ModalContent {

    //~ Instance Fields ..............................................................................................................................

    private final TekSuggestBox    suggestedForms;
    private final KeySuggestOracle suggestOracle;

    //~ Constructors .................................................................................................................................

    private SuggestPopup() {
        setModalClassName(FormConstants.MODAL_MENU);
        setTransparent(true);

        suggestOracle  = new KeySuggestOracle();
        suggestedForms = new TekSuggestBox(suggestOracle, true);
        suggestedForms.addStyleName(BLOCK_INPUT);
        suggestedForms.addSelectionHandler(event -> {
            final ItemSuggestion selectedItem = (ItemSuggestion) event.getSelectedItem();
            if (selectedItem != null) {
                getFormHistory().load(selectedItem.getKey());
                suggestedForms.setText("");
            }
        });

        setBody(suggestedForms);
        setTitle(MSGS.jumpToForm());

        setListener(new ModalListener() {
                @Override public void onHide(ModalButtonType buttonClicked) {
                    suggestedForms.setText("");
                }
                @Override public void onShow() {}
            });
    }

    //~ Methods ......................................................................................................................................

    /** Returns the form suggestOracle to search for forms that are in menus. */
    public KeySuggestOracle getSuggestOracle() {
        return suggestOracle;
    }

    void addSuggestion(@NotNull final String link, @NotNull final String label) {
        suggestOracle.addSuggestion(new ItemSuggestion(link, label));
    }

    //~ Methods ......................................................................................................................................

    /** Return helpDialog instance. */
    @SuppressWarnings("NonThreadSafeLazyInitialization")  // running on client side
    public static SuggestPopup getSuggestPopup() {
        if (instance == null) instance = new SuggestPopup();
        return instance;
    }

    //~ Static Fields ................................................................................................................................

    private static SuggestPopup instance = null;
}  // end class SuggestPopup
