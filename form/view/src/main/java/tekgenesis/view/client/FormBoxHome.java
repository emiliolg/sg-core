
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Tuple;
import tekgenesis.view.client.dialog.FavoriteDialog;
import tekgenesis.view.client.menu.RecentPopup;
import tekgenesis.view.client.menu.SuggestPopup;
import tekgenesis.view.client.suggest.ItemSuggestion;
import tekgenesis.view.client.suggest.KeySuggestOracle;
import tekgenesis.view.client.ui.base.HtmlList;
import tekgenesis.view.shared.response.FavoriteResponse;
import tekgenesis.view.shared.response.Response;
import tekgenesis.view.shared.service.FormService;
import tekgenesis.view.shared.utils.Favorite;

import static tekgenesis.metadata.form.model.FormConstants.MUTED;
import static tekgenesis.metadata.form.model.FormConstants.PAGE_HEADER;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.*;

/**
 * A home panel to redirect to a form faster using: search, favorites and recent forms.
 */
class FormBoxHome extends FlowPanel {

    //~ Instance Fields ..............................................................................................................................

    private final TekSuggestBox suggestedForms;

    //~ Constructors .................................................................................................................................

    private FormBoxHome() {
        setStyleName("formbox-home");

        final Label searchLabel = new HTML("<kbd>n</kbd> " + MSGS.search());
        searchLabel.setStyleName(PAGE_HEADER);
        add(searchLabel);

        final KeySuggestOracle suggestOracle = SuggestPopup.getSuggestPopup().getSuggestOracle();
        suggestedForms = new TekSuggestBox(suggestOracle, true);
        suggestedForms.addSelectionHandler(event -> {
            final ItemSuggestion selectedItem = (ItemSuggestion) event.getSelectedItem();
            if (selectedItem != null) {
                suggestedForms.setText("");
                FormHistory.getFormHistory().load(selectedItem.getKey());
            }
        });
        add(suggestedForms);

        final FlowPanel formLinks = new FlowPanel();
        formLinks.addStyleName("formbox-home-links");
        final FlowPanel favoritePanel = createStaticStructure("<kbd>F2</kbd> " + MSGS.favorites());
        final FlowPanel recentPanel   = createStaticStructure("<kbd>e</kbd> " + MSGS.recent());
        formLinks.add(favoritePanel);
        formLinks.add(recentPanel);
        populateFavorites(favoritePanel, MSGS.noFavorites());
        populateRecents(recentPanel, MSGS.noRecent());
        add(formLinks);
    }

    //~ Methods ......................................................................................................................................

    void setFocus(boolean focus) {
        suggestedForms.setFocus(focus);
    }

    private FlowPanel createStaticStructure(String label) {
        final FlowPanel panel       = new FlowPanel();
        final Label     headerLabel = new HTML(label);
        headerLabel.setStyleName(PAGE_HEADER);
        panel.add(headerLabel);
        return panel;
    }

    private void populateFavorites(final FlowPanel panel, String emptyLabel) {
        FavoriteDialog.sanitizeFavorites();
        FormService.App.getInstance().getFavorites(new AsyncCallback<Response<FavoriteResponse>>() {
                @Override public void onFailure(Throwable caught) {}
                @Override public void onSuccess(Response<FavoriteResponse> result) {
                    final List<Favorite> forms = result.getResponse().getFavorites();
                    if (forms.isEmpty()) {
                        final HTML empty = new HTML(emptyLabel);
                        empty.setStyleName(MUTED);
                        panel.add(empty);
                    }
                    else {
                        final HtmlList.Ordered ol = ol();
                        for (final Favorite fav : forms) {
                            final Anchor anchor = anchor(fav.getLabel());
                            anchor.setHref(fav.getFormLink());
                            final HtmlList.Item li = li();
                            li.add(anchor);
                            ol.add(li);
                        }
                        panel.add(ol);
                    }
                }
            });
    }

    private void populateRecents(FlowPanel panel, String emptyLabel) {
        RecentPopup.sanitizeRecents();
        final Seq<Tuple<String, String>> forms = RecentPopup.getRecentForms();
        if (forms.isEmpty()) {
            final HTML empty = new HTML(emptyLabel);
            empty.setStyleName(MUTED);
            panel.add(empty);
        }
        else {
            final HtmlList.Ordered ol = ol();
            for (final Tuple<String, String> idLabel : forms) {
                final Anchor anchor = anchor(idLabel.second());
                anchor.setHref(idLabel.first());
                final HtmlList.Item li = li();
                li.add(anchor);
                ol.add(li);
            }
            panel.add(ol);
        }
    }

    //~ Methods ......................................................................................................................................

    static FormBoxHome create() {
        return new FormBoxHome();
    }
}  // end class FormBoxHome
