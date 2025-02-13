
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.check.CheckType;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.view.client.controller.FormController;
import tekgenesis.view.client.ui.FormUI;
import tekgenesis.view.client.ui.HasScalarValueUI;
import tekgenesis.view.client.ui.WidgetUI;
import tekgenesis.view.shared.response.ResponseError;

import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.metadata.form.SourceWidget.NONE;
import static tekgenesis.metadata.form.widget.ButtonType.CANCEL;
import static tekgenesis.view.client.FormHistory.getFormHistory;

/**
 * Box bounded by history.
 */
public class HistoryFormBox extends DynamicFormBox implements FormHistory.HistoryListener {

    //~ Instance Fields ..............................................................................................................................

    private final HistoryRewrite boxListener;

    private final FormHistory.HistoryChangeHandlerRegistration historyListener;

    //~ Constructors .................................................................................................................................

    /** Box bounded by history default constructor. */
    HistoryFormBox() {
        historyListener = getFormHistory().addHistoryListener(this);
        boxListener     = new HistoryRewrite(this);
    }

    //~ Methods ......................................................................................................................................

    @Override public void attach(HasWidgets parent) {
        super.attach(parent);

        if (isNotEmpty(History.getToken())) History.fireCurrentHistoryState();
        else if (isEmpty(configuration.getInitialFqn())) home();
    }

    @Override public void detach() {
        removeListener(boxListener);
        historyListener.removeHandler();
        super.detach();
    }

    /** Form history changed. */
    @Override public void formHistoryChanged(@NotNull String url) {
        load(url);
    }

    /** Form history changed. May have PK. May have parameters */
    @Override public void formHistoryChanged(@NotNull String fqn, @Nullable String pk, @Nullable String parameters) {
        if (!matchesCallbacksPeek(fqn, pk, parameters)) load(fqn, pk, parameters);
        else {
            final FormController current = getCurrent();
            if (current != null && current.getView().hasButtonOf(CANCEL)) current.cancel(NONE);
        }
    }  // end method formHistoryChanged

    /** Form history cleared. */
    @Override public void formHistoryCleared() {
        unloadCurrent();
    }

    @Override public void load(@NotNull String fqn, @Nullable String pk) {
        load(fqn, pk, null);
    }

    @Override public void load(@NotNull String fqn, @Nullable String pk, @Nullable String parameters) {
        startLifeCycle();
        super.load(fqn, pk, parameters);
    }

    @Override void unloadCurrent() {
        super.unloadCurrent();
        updateWindowTitle("");
    }

    @Override void setCurrent(@NotNull String url, @NotNull String html) {
        super.setCurrent(url, html);
        updateWindowTitle(url);
    }

    @Override void setCurrent(@NotNull FormModel model, @Nullable String pk, @Nullable String parameters) {
        super.setCurrent(model, pk, parameters);
        updateWindowTitle(findFormTitle());
    }

    private String findFormTitle() {
        final FormController current = getCurrent();
        if (current == null) return "";

        final FormUI view = current.getView();

        for (final WidgetUI message : view.finder().byType(WidgetType.MESSAGE)) {
            if (message.getModel().getMsgType() == CheckType.TITLE) return "" + ((HasScalarValueUI) message).getValue();
        }

        return view.getUiModel().getLabel();
    }

    private boolean matchesCallbacksPeek(@NotNull final String fqn, @Nullable final String pk, @Nullable final String parameters) {
        if (callbacks.isEmpty()) return false;
        final CallbackStackItem last = callbacks.peek();
        return equal(last.getForm().getFullName(), fqn) && equal(notNull(last.getPk()), notNull(pk)) &&
               equal(notNull(last.getParameters()), notNull(parameters));
    }

    private void updateWindowTitle(final String title) {
        // get app title
        String appTitle = Window.getTitle();
        // noinspection NonJREEmulationClassesInClientCode
        final int divider = appTitle.indexOf(TITLE_DIVIDER);
        if (divider > -1) appTitle = appTitle.substring(0, divider);

        // change title to form app-title + title
        Window.setTitle(appTitle + (isEmpty(title) ? "" : TITLE_DIVIDER + title));
    }

    //~ Static Fields ................................................................................................................................

    @NonNls private static final String TITLE_DIVIDER = " - ";

    //~ Inner Classes ................................................................................................................................

    private static class HistoryRewrite implements FormBoxListener {
        private HistoryRewrite(@NotNull FormBox box) {
            box.addListener(this);
        }

        @Override public void onError(ResponseError error) {}

        @Override public void onLoad(String url) {
            getFormHistory().silentLoad(url);
        }

        @Override public void onLoad(String fqn, String pk, String parameters) {
            getFormHistory().silentLoad(fqn, pk, parameters);
        }

        @Override public void onUnload() {
            getFormHistory().silentUnload();
        }

        @Override public void onUpdate() {}
    }
}  // end class HistoryFormBox
