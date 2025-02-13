
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.dialog;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.metadata.link.Links;
import tekgenesis.view.client.FormStorage;
import tekgenesis.view.client.menu.RecentPopup;
import tekgenesis.view.client.ui.base.DraggableList;
import tekgenesis.view.client.ui.base.ExtButton;
import tekgenesis.view.client.ui.base.HtmlList;
import tekgenesis.view.client.ui.base.Icon;
import tekgenesis.view.client.ui.base.Tooltip;
import tekgenesis.view.client.ui.modal.ModalContent;
import tekgenesis.view.shared.response.FavoriteResponse;
import tekgenesis.view.shared.response.Response;
import tekgenesis.view.shared.service.FormService;
import tekgenesis.view.shared.utils.Favorite;

import static com.google.gwt.event.dom.client.KeyCodes.KEY_ENTER;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_NINE;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_ONE;

import static tekgenesis.metadata.form.model.FormConstants.CLOSE_BUTTON;
import static tekgenesis.metadata.form.model.FormConstants.PULL_RIGHT;
import static tekgenesis.metadata.form.widget.IconType.STAR;
import static tekgenesis.metadata.form.widget.IconType.STAR_O;
import static tekgenesis.view.client.FormHistory.getFormHistory;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.anchor;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.div;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.li;

/**
 * Favorites modal.
 */
public class FavoriteDialog extends ModalContent {

    //~ Instance Fields ..............................................................................................................................

    private final DraggableList draggableList;

    private List<Favorite> favorites;
    // private EntitySeq<Favorite> favoriteList;

    private String          fqn         = null;
    private final FlowPanel headerPanel;
    private String          label       = null;
    private String          parameters  = null;
    private String          pk          = null;

    //~ Constructors .................................................................................................................................

    private FavoriteDialog() {
        favorites = new ArrayList<>();
        populateFavorites();
        headerPanel = div();
        headerPanel.addStyleName(PULL_RIGHT);
        draggableList = new DraggableList();
        draggableList.addStyleName("favorites-list");
        draggableList.addListener(createListener());
        // draggableList.lettered();
        setTitle(MSGS.favorites());
        setBody(draggableList);
        setHeader(headerPanel);
        addKeyHandlers();
    }

    //~ Methods ......................................................................................................................................

    private void addFavorite() {
        if (favorites.size() < 10) FormService.App.getInstance().addFavorite(getFormLink(), favorites.size(), new AsyncCallback<Void>() {
                @Override public void onSuccess(Void v) {
                    favorites.add(new Favorite(label, getFormLink()));updateView();}
                @Override public void onFailure(Throwable caught) {}
            });
    }

    private void addKeyHandlers() {
        headerPanel.sinkEvents(KeyCodes.KEY_DOWN);
        headerPanel.addDomHandler(event -> {
                final int code = event.getNativeKeyCode();
                if (code >= KEY_ONE && code <= KEY_NINE) {
                    final Favorite f = favorites.get(code - KEY_ONE);
                    if (f != null) loadForm(f.getFormLink());
                }
                if (code == KEY_ENTER && fqn != null) {
                    if (isFormFavorite()) removeFavorite(getIdxFromName());
                    else addFavorite();
                    event.preventDefault();
                }
            },
            KeyDownEvent.getType());
    }

    private DraggableList.OrderChangeListener createListener() {
        return this::swapFavorites;
    }

    private void loadForm(String formLink) {
        getFormHistory().load(formLink);
    }

    private void populateFavorites() {
        FormService.App.getInstance().getFavorites(new AsyncCallback<Response<FavoriteResponse>>() {
                @Override public void onFailure(Throwable caught) {}

                @Override public void onSuccess(Response<FavoriteResponse> result) {
                    favorites = result.getResponse().getFavorites();
                    updateView();
                }
            });
    }

    private void removeFavorite(int idx) {
        if (idx < 0) return;
        FormService.App.getInstance().removeFavorite(idx, new AsyncCallback<Void>() {
                @Override public void onSuccess(Void v) {
                    favorites.remove(idx);
                    updateView();
                }
                @Override public void onFailure(Throwable caught) {}
            });
    }

    private void swapFavorites(int a, int b) {
        FormService.App.getInstance().swapFavorite(a, b, new AsyncCallback<Void>() {
                @Override public void onSuccess(Void v) {
                    final Favorite aux = favorites.remove(a);
                    favorites.add(b, aux);
                    updateView();
                }
                @Override public void onFailure(Throwable caught) {}
            });
    }

    private void updateHeader() {
        headerPanel.clear();
        final Tooltip tooltip = new Tooltip();
        tooltip.setMsg(
            "<ul> <li> " + MSGS.favoritesInfo1() + " </li> <li> " + MSGS.favoritesInfo2() + "</li> <li> " + MSGS.favoritesInfo3() + " </li> <li> " +
            MSGS.favoritesInfo4() + "</li> </ul>");
        headerPanel.add(tooltip);

        final IconType               type;
        @Nullable final ClickHandler handler;
        if (fqn == null) {
            handler = null;
            type    = STAR_O;
        }
        else if (isFormFavorite()) {
            type    = STAR;
            handler = event -> removeFavorite(getIdxFromName());
        }
        else {
            type    = STAR_O;
            handler = event -> addFavorite();
        }

        final ExtButton addBtn = new ExtButton(new Icon(type));
        addBtn.addStyleName("btn-favorite");
        if (handler != null) addBtn.addClickHandler(handler);
        else addBtn.setEnabled(false);
        headerPanel.add(addBtn);
        addBtn.setFocus(true);
    }

    private void updateList() {
        draggableList.clear();
        for (int i = 0; i < favorites.size(); i++) {
            final Favorite favorite = favorites.get(i);
            final Anchor   anchor   = anchor(favorite.getLabel());
            anchor.addClickHandler(event -> loadForm(favorite.getFormLink()));

            final Anchor remove = anchor();
            final int    finalI = i;
            remove.addClickHandler(event -> removeFavorite(finalI));
            remove.setHTML(FormConstants.TIMES);
            remove.addStyleName(CLOSE_BUTTON);

            final HtmlList.Item li = li();
            li.add(anchor);
            li.add(remove);
            draggableList.add(li);
        }
    }

    private void updateView() {
        updateList();
        updateHeader();
    }

    private boolean isFormFavorite() {
        for (final Favorite favorite : favorites) {
            if (favorite.getFormLink().equals(getFormLink())) return true;
        }
        return false;
    }

    private String getFormLink() {
        return pk != null || parameters != null ? Links.formLink(fqn, pk, parameters) : fqn;
    }

    private int getIdxFromName() {
        final String link = getFormLink();
        for (int i = 0; i < favorites.size(); i++) {
            if (favorites.get(i).getFormLink().equals(link)) return i;
        }
        return -1;
    }

    //~ Methods ......................................................................................................................................

    /** TO BE REMOVED! Sanitizes local storage. */
    public static void sanitizeFavorites() {
        for (final FormStorage storage : FormStorage.getInstance())
            storage.remove(FAVORITE_KEY);
    }

    /** Return helpDialog instance. */
    @SuppressWarnings("NonThreadSafeLazyInitialization")  // running on client side
    public static ModalContent show(@NotNull final String fqn, @Nullable final String pk, @Nullable final String parameters) {
        if (instance == null) instance = new FavoriteDialog();
        instance.fqn        = fqn;
        instance.pk         = pk;
        instance.parameters = parameters;
        instance.label      = RecentPopup.getRecentPopup().getForms().get(instance.getFormLink());
        instance.updateView();
        return instance;
    }

    /** Returns the favorite forms. */
    public static void getFavoriteForms(final AsyncCallback<Response<FavoriteResponse>> async) {
        FormService.App.getInstance().getFavorites(async);
    }

    //~ Static Fields ................................................................................................................................

    private static final String   FAVORITE_KEY     = "favorite";
    private static final String   TUPLE_SEPARATOR  = ">";
    private static final char     VALUES_SEPARATOR = ';';
    private static FavoriteDialog instance         = null;
}  // end class FavoriteDialog
