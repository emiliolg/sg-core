
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.menu;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.ListBox;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Tuple;
import tekgenesis.metadata.form.model.FormConstants;
import tekgenesis.metadata.link.Links;
import tekgenesis.view.client.FormStorage;
import tekgenesis.view.client.ui.modal.ModalContent;
import tekgenesis.view.client.ui.modal.ModalListener;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.common.core.Functions.stringToTuple;
import static tekgenesis.common.core.Strings.escapeCharOn;
import static tekgenesis.common.core.Strings.splitNotEscaped;
import static tekgenesis.metadata.form.model.FormConstants.BLOCK_INPUT;
import static tekgenesis.view.client.FormHistory.getFormHistory;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.ui.base.HtmlWidgetFactory.list;

/**
 * Recents popup.
 */
public class RecentPopup extends ModalContent {

    //~ Instance Fields ..............................................................................................................................

    private final Map<String, String> forms;
    private final ListBox             recentForms;
    private int                       recentSelectedIndex;

    //~ Constructors .................................................................................................................................

    private RecentPopup() {
        setModalClassName(FormConstants.MODAL_MENU);
        setTransparent(true);

        forms       = new HashMap<>();
        recentForms = list(false, 10);
        recentForms.addStyleName(BLOCK_INPUT);
        recentForms.addKeyDownHandler(event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) getFormHistory().load(recentForms.getValue(recentForms.getSelectedIndex()));
        });
        recentForms.addClickHandler(event -> getFormHistory().load(recentForms.getValue(recentForms.getSelectedIndex()), null));

        recentSelectedIndex = 0;

        setBody(recentForms);
        setTitle(MSGS.recent());
        setListener(new ModalListener() {
                @Override public void onHide(ModalButtonType buttonClicked) {}

                @Override public void onShow() {
                    populateRecent();
                    recentForms.setSelectedIndex(recentSelectedIndex);
                }
            });
    }

    //~ Methods ......................................................................................................................................

    /** Shares maps between Recent and Favorite to get the proper label of the form. */
    public Map<String, String> getForms() {
        return forms;
    }

    void add(String link, String label) {
        forms.put(link, label);
    }

    void appendRecentForm(@NotNull String fqn, @Nullable String pk, @Nullable String parameters) {
        final String link = pk != null || parameters != null ? Links.formLink(fqn, pk, parameters) : fqn;

        final String label = forms.get(link);

        if (isNotEmpty(label)) {
            String result = createTuple(link, label);

            int i = 0;
            for (final Tuple<String, String> tuple : getRecentForms()) {
                if (i++ >= RECENT_LIMIT) break;
                if (!tuple.first().equals(link)) result += VALUES_SEPARATOR + createTuple(tuple.first(), tuple.second());
            }

            for (final FormStorage storage : FormStorage.getInstance())
                storage.set(RECENT_KEY, result);
        }
    }

    void setRecentSelectedIndex(int index) {
        recentSelectedIndex = index;
    }

    private String createTuple(@NotNull String link, @NotNull String label) {
        return link + TUPLE_SEPARATOR + escapeCharOn(notNull(label, ""), VALUES_SEPARATOR);
    }

    private void populateRecent() {
        recentForms.clear();
        for (final Tuple<String, String> tuple : getRecentForms())
            recentForms.addItem(tuple.second(), tuple.first());
    }

    //~ Methods ......................................................................................................................................

    /** TO BE REMOVED! Sanitizes local storage. */
    public static void sanitizeRecents() {
        for (final FormStorage storage : FormStorage.getInstance()) {
            final String recents = storage.get(RECENT_KEY);
            if (recents != null && !recents.contains(TUPLE_SEPARATOR)) storage.remove(RECENT_KEY);
        }
    }

    /** Returns the recently opened forms. */
    public static Seq<Tuple<String, String>> getRecentForms() {
        for (final FormStorage storage : FormStorage.getInstance())
            return seq(splitNotEscaped(storage.get(RECENT_KEY), VALUES_SEPARATOR)).map(stringToTuple(TUPLE_SEPARATOR));
        return Colls.emptyIterable();
    }

    /** Return helpDialog instance. */
    @SuppressWarnings("NonThreadSafeLazyInitialization")  // running on client side
    public static RecentPopup getRecentPopup() {
        if (instance == null) instance = new RecentPopup();
        return instance;
    }

    //~ Static Fields ................................................................................................................................

    private static RecentPopup  instance         = null;
    private static final String TUPLE_SEPARATOR  = ">";
    private static final char   VALUES_SEPARATOR = '~';
    private static final String RECENT_KEY       = "recent";
    private static final int    RECENT_LIMIT     = 15;
}  // end class RecentPopup
