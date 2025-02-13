
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import java.util.ArrayList;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.metadata.link.Links;
import tekgenesis.view.client.ui.base.ModalOkCancelAlert;

import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.metadata.link.Links.QUESTION_MARK;
import static tekgenesis.metadata.link.Links.SLASH;
import static tekgenesis.metadata.link.Links.formUrl;

/**
 * Utility class to manage Form history.
 */
@SuppressWarnings("NonJREEmulationClassesInClientCode")
public class FormHistory {

    //~ Instance Fields ..............................................................................................................................

    private String                          currentHistoryToken = "";
    private final HistoryValueChangeHandler handler;

    //~ Constructors .................................................................................................................................

    private FormHistory() {
        handler = new HistoryValueChangeHandler();
        History.addValueChangeHandler(handler);
    }

    //~ Methods ......................................................................................................................................

    /** Add history listener. */
    public HistoryChangeHandlerRegistration addHistoryListener(final HistoryListener listener) {
        handler.add(listener);
        return new HistoryChangeHandlerRegistration(handler, listener);
    }

    /** Returns true if history matches current url. */
    public boolean historyMatchesUrl() {
        return Window.Location.getHref().contains(currentHistoryToken);
    }

    /** Load a form link to the url. */
    public void load(@NotNull final String link) {
        History.newItem(link.startsWith("#") ? link.substring(1) : link);
    }

    /** Load a form to the url. */
    public void load(@NotNull final String fqn, @Nullable final String pk) {
        load(fqn, pk, null);
    }

    /** Load a form to the url. */
    public void load(@NotNull final String fqn, @Nullable final String pk, @Nullable final String parameters) {
        History.newItem(formUrl(fqn, pk, parameters));
    }

    /** Performs a reload of the current history state. */
    public void reload() {
        History.fireCurrentHistoryState();
    }

    /** Performs a reload of the current history state. */
    public void reloadWithoutConfirmChanges() {
        handler.load();
    }

    /** Unload form. */
    public void unload() {
        History.newItem(SLASH);
    }

    HistoryFormEvent current() {
        return HistoryFormEvent.create();
    }

    void silentLoad(@NotNull final String url) {
        History.newItem(url, false);
    }

    void silentLoad(@NotNull final String fqn, @Nullable final String pk, @Nullable final String parameters) {
        History.newItem(formUrl(fqn, pk, parameters), false);
    }

    /** Unload form. */
    void silentUnload() {
        History.newItem(SLASH, false);
    }

    //~ Methods ......................................................................................................................................

    /** Gets the FormHistory instance. */
    public static FormHistory getFormHistory() {
        return FormHistoryHolder.instance;
    }

    //~ Inner Interfaces .............................................................................................................................

    public interface HistoryListener {
        /** Form history changed. */
        void formHistoryChanged(@NotNull final String url);
        /** Form history changed. */
        void formHistoryChanged(@NotNull final String fqn, @Nullable final String pk, @Nullable final String parameters);
        /** Form history cleared. */
        void formHistoryCleared();
    }

    //~ Inner Classes ................................................................................................................................

    private static class FormHistoryHolder {
        private FormHistoryHolder() {}

        private static final FormHistory instance = new FormHistory();
    }

    /**
     * Class to handle listener unregistration from handler.
     */
    public class HistoryChangeHandlerRegistration {
        private final HistoryValueChangeHandler changeHandler;
        private final HistoryListener           listener;

        HistoryChangeHandlerRegistration(@NotNull final HistoryValueChangeHandler changeHandler, @NotNull final HistoryListener listener) {
            this.listener      = listener;
            this.changeHandler = changeHandler;
        }

        void removeHandler() {
            changeHandler.remove(listener);
        }
    }

    /**
     * Class to parse the url for a load form operation.
     */
    static class HistoryFormEvent {
        private final String fqn;
        private final String parameters;
        private final String pk;
        private final String url;

        private HistoryFormEvent() {
            url        = "";
            fqn        = "";
            pk         = "";
            parameters = "";
        }

        private HistoryFormEvent(String url) {
            this.url   = url;
            fqn        = "";
            pk         = "";
            parameters = "";
        }

        private HistoryFormEvent(String fqn, String pk, String parameters) {
            url             = "";
            this.fqn        = fqn;
            this.pk         = pk;
            this.parameters = parameters;
        }

        @Override public String toString() {
            return this != NONE ? isNotEmpty(url) ? url : fqn + " [" + notNull(pk) + "]" : "";
        }

        /** Gets the form id from the url. */
        public String getFqn() {
            return fqn;
        }

        /** Gets the parameters of the url as a query string. */
        public String getParameters() {
            return parameters;
        }

        /** Gets the entity pk from the url. */
        public String getPk() {
            return pk;
        }

        /** Gets the url. */
        public String getUrl() {
            return url;
        }

        private boolean isRootOrNull() {
            return (isEmpty(fqn) && isEmpty(url)) || SLASH.equals(fqn);
        }

        private String getToken() {
            return isRootOrNull() ? SLASH : isNotEmpty(url) ? url : formUrl(getFqn(), getPk(), getParameters());
        }

        private static HistoryFormEvent create() {
            final String token = History.getToken();

            final int    firstSlash = token.indexOf(SLASH);
            final String prefix     = firstSlash > 0 ? token.substring(0, firstSlash) : null;

            if (isNotEmpty(prefix)) {
                if (equal(Links.LOAD_FORM_URL_PREFIX, prefix)) {
                    final String pk;
                    final String fqn;
                    final int    secondSlash = token.indexOf(SLASH, firstSlash + 1);
                    final int    endOfFqn    = token.indexOf(QUESTION_MARK);

                    if (secondSlash > 0) {
                        fqn = token.substring(firstSlash + 1, secondSlash);
                        if (endOfFqn > 0) pk = token.substring(secondSlash + 1, endOfFqn);
                        else pk = token.substring(secondSlash + 1, token.length());
                    }
                    else {
                        if (endOfFqn > 0) fqn = token.substring(firstSlash + 1, endOfFqn);
                        else fqn = token.substring(firstSlash + 1, token.length());
                        pk = "";
                    }
                    final String parameters;
                    if (endOfFqn > 0) parameters = token.substring(endOfFqn + 1, token.length());
                    else parameters = "";

                    return new HistoryFormEvent(fqn, pk, parameters);
                }
                else return new HistoryFormEvent(token);
            }

            return NONE;
        }

        private static final HistoryFormEvent NONE = new HistoryFormEvent();
    }  // end class HistoryFormEvent

    /**
     * History value change handler with Ok/Cancel dialog impl.
     */
    private class HistoryValueChangeHandler implements ValueChangeHandler<String>, ModalOkCancelAlert.OkCancelCallback {
        final ArrayList<HistoryListener> listeners;

        HistoryValueChangeHandler() {
            listeners = new ArrayList<>();
        }

        @Override public void cancel() {
            History.newItem(currentHistoryToken, false);
        }

        @Override public void ok() {
            load();
        }

        @Override public void onValueChange(ValueChangeEvent<String> event) {
            if (BoxHandler.isSomeoneDirty()) Application.getInstance().headsUp(this);
            else load();
        }

        void add(HistoryListener listener) {
            listeners.add(listener);
        }

        void load() {
            final HistoryFormEvent e = HistoryFormEvent.create();
            if (Application.getInstance().isAuthenticated()) currentHistoryToken = e.getToken();
            if (e.isRootOrNull()) formHistoryCleared();
            else {
                final String url = e.getUrl();
                if (isNotEmpty(url)) formHistoryChanged(url);
                else formHistoryChanged(e.getFqn(), e.getPk(), e.getParameters());
            }
        }  // end method load

        void remove(HistoryListener listener) {
            listeners.remove(listener);
        }

        private void formHistoryChanged(String url) {
            for (final HistoryListener listener : listeners)
                listener.formHistoryChanged(url);
        }

        private void formHistoryChanged(String fqn, String pk, String parameters) {
            for (final HistoryListener listener : listeners)
                listener.formHistoryChanged(fqn, pk, parameters);
        }

        private void formHistoryCleared() {
            for (final HistoryListener listener : listeners)
                listener.formHistoryCleared();
        }
    }  // end class HistoryValueChangeHandler
}  // end class FormHistory
