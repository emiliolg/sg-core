
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

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.security.shiro.web.URLConstants;
import tekgenesis.view.client.controller.FormController;
import tekgenesis.view.client.dialog.FavoriteDialog;
import tekgenesis.view.client.dialog.HelpDialog;
import tekgenesis.view.client.dialog.LoginDialog;
import tekgenesis.view.client.ui.BaseHasScalarValueUI;
import tekgenesis.view.client.ui.FormUI;
import tekgenesis.view.client.ui.HasClickUI;
import tekgenesis.view.client.ui.TabGroupUI;
import tekgenesis.view.client.ui.TableUI;
import tekgenesis.view.client.ui.TextFieldUI;
import tekgenesis.view.client.ui.WidgetUI;
import tekgenesis.view.client.ui.base.CalculatorPopover;

import static com.google.gwt.event.dom.client.KeyCodes.*;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.core.Constants.HELP_URI;
import static tekgenesis.common.core.Constants.REFRESH_URI;
import static tekgenesis.common.core.Option.option;
import static tekgenesis.metadata.form.IndexedWidget.createFromReference;
import static tekgenesis.metadata.form.SourceWidget.NONE;
import static tekgenesis.metadata.form.model.FormConstants.BLANK;
import static tekgenesis.metadata.form.model.FormConstants.FLOATING_MODAL;
import static tekgenesis.metadata.form.model.FormConstants.OPEN;
import static tekgenesis.metadata.form.widget.ButtonType.CANCEL;
import static tekgenesis.metadata.form.widget.ButtonType.DELETE;
import static tekgenesis.metadata.form.widget.ButtonType.SAVE;
import static tekgenesis.view.client.ClientUiModelRetriever.getRetriever;
import static tekgenesis.view.client.ui.TableUI.TABLE_WIDGET_CLASSNAME;
import static tekgenesis.view.client.ui.base.HtmlDomUtils.*;

/**
 * Root document Handler for events. Its a singleton because there is a single document.
 */
@SuppressWarnings("OverlyComplexClass")
public class RootInputHandler implements KeyPressHandler, KeyUpHandler, KeyDownHandler, EventListener {

    //~ Instance Fields ..............................................................................................................................

    private boolean calculatorOpened;

    private TableUI current      = null;
    private Element modalTrigger = null;

    //~ Constructors .................................................................................................................................

    private RootInputHandler() {}

    //~ Methods ......................................................................................................................................

    /** Set calculator closed. */
    public void closedCalculator() {
        calculatorOpened = false;
    }

    /** Close opened drop downs and menus. */
    public void handleDropDowns() {
        // could close popups ?
        final JsArray<Element> elements = querySelectorAll(RootPanel.get().getElement(), "[data-toggle='dropdown']");
        for (int i = 0; i < elements.length(); i++) {
            final Element element = elements.get(i);
            final Element parent  = element.getParentElement();
            if (parent.hasClassName(OPEN)) parent.removeClassName(OPEN);
        }
    }

    /** Sets current to the given table and tells it to handle the selection event. */
    public void handleTableEvent(@NotNull TableUI tableUI, Event event) {
        if (current == null || tableUI != current) {
            deselectLastTable();
            current = tableUI;
        }

        // Handle Selection
        current.handleSelectionEvent(event);
    }

    @Override public void onBrowserEvent(Event event) {
        // clicks on maps return custom SVG Elements (not gwt Elements)
        if (event.getTypeInt() == Event.ONCLICK) {
            final Element eventTarget = Element.as(event.getEventTarget());
            final String  target      = eventTarget.toString();
            final boolean isNotMap    = !target.contains("SVG");
            if (isNotMap) {
                handleTableSelection(event);
                final Element parentPopup  = findParentElement(FLOATING_MODAL, eventTarget, false);
                final Element confirmPopup = findParentElement("modal-alert", eventTarget, false);
                if (parentPopup == null && confirmPopup == null) {
                    handleDropDowns();
                    hideActiveModal(true);
                    Application.getInstance().closeLeftMenu();
                }
            }
        }
    }

    @Override
    @SuppressWarnings({ "OverlyLongMethod", "OverlyComplexMethod" })
    public void onKeyDown(final KeyDownEvent e) {
        // key events not registered on KeyPress (cmd - enter - esc)
        final int     keyCode = e.getNativeKeyCode();
        final Element element = targetElement(e);
        final boolean noFocus = element != null && isNoFocus(element);
        switch (keyCode) {
        case KEY_TAB:
            if (noFocus) {
                for (final FormController c : getCurrent()) {
                    c.getView().focusFirst();
                    e.preventDefault();
                }
            }
            else handleTableSelection(e);
            break;
        case KEY_ESCAPE:
            if (element != null) element.blur();
            deselectLastTable();
            hideActiveModal();
            break;
        case KEY_ENTER:
            if (isTriggerKeyDown(e)) {
                // blur to effectively call onChange when saving with CTRL+S
                final Element activeElement = getActiveElement();
                for (final FormController c : getCurrent()) {
                    if (activeElement != null) activeElement.blur();
                    if (c.getView().hasButtonOf(SAVE)) c.save(NONE);
                }
            }
            else if (current != null && !isCalculatorOpened() && !focusedHasOnClick()) current.down(e);
            break;
        case KEY_D:
            if (isTriggerKeyDown(e) && e.isShiftKeyDown())
                for (final FormController c : getCurrent()) {
                    if (c.getView().hasButtonOf(DELETE)) c.delete(NONE);
                    e.preventDefault();
                }
            break;
        case KEY_C:
            if (isTriggerKeyDown(e) && e.isShiftKeyDown())
                for (final FormController c : getCurrent()) {
                    if (c.getView().hasButtonOf(CANCEL)) c.cancel(NONE);
                    e.preventDefault();
                }
            else if (!isTriggerKeyDown(e) && !e.isShiftKeyDown() && noFocus && !isCalculatorOpened()) showCalculator(null);
            break;
        case KEY_R:
            if (e.isControlKeyDown() && e.isShiftKeyDown()) refreshMetamodels(REFRESH_URI);
            break;
        case KEY_L:
            if (e.isAltKeyDown() && e.isShiftKeyDown()) Window.Location.assign(URLConstants.LOGOUT_URI);
            else if (e.isAltKeyDown()) Application.modal(LoginDialog.show(true, null));
            break;
        case KEY_F1:
            if (isTriggerKeyDown(e)) {
                Window.open(HELP_URI + getCurrent().map(FormController::getFormName).orElse(""), BLANK, "enabled");
                break;
            }
            break;
        case KEY_F2:
            for (final FormController controller : getCurrent()) {
                final String fqn        = controller.getFormName();
                final String pk         = controller.getPk();
                final String parameters = controller.getParameters();
                Application.modal(FavoriteDialog.show(fqn, pk, parameters));
            }
            break;
        case KEY_M:
            if (e.isAltKeyDown()) {
                final Element menu = querySelector(RootPanel.get().getElement(), ".nav.sui-menu");
                if (menu != null) menu.getFirstChildElement().getFirstChildElement().focus();
            }
            break;
        case KEY_Z:
            if (isTriggerKeyDown(e) && getActiveElement() != null) getCurrent()
                .map(fc -> {
                    final String activeElement = getActiveElement().getId().substring(0, getActiveElement().getId().length() - 1);if (isNotEmpty(
                            activeElement))
                    {
                        final List<BaseHasScalarValueUI> view = fc.getView().finder().allByPredicate(widgetUI ->
                                    widgetUI != null && activeElement.equals(widgetUI.getElement().getId()) &&
                                    widgetUI instanceof BaseHasScalarValueUI);for (final BaseHasScalarValueUI ui : view)
                            ui.undo();if (!view.isEmpty()) e.preventDefault();
                    }
                    return fc;
                });
            break;
        default:
            boolean unHandled = true;
            if (isTriggerKeyDown(e) && current != null) unHandled = !current.handleRootKeyDown(e);
            if (unHandled && (noFocus || e.isAnyModifierKeyDown())) handleCustomKeyShortcut(e);
        }
    }  // end method onKeyDown

    /** handle key navigation. */
    @Override public void onKeyPress(KeyPressEvent e) {
        // single key no focus shortcuts
        final Element element = targetElement(e);
        if (isNoFocus(element)) {
            final char code = e.getCharCode();
            switch (code) {
            case KEY_SLASH:
            case KEY_DASH:
                for (final FormController c : getCurrent()) {
                    c.getView().focusSearch();
                    e.preventDefault();
                }
                break;
            case KEY_QUESTION:
                Application.modal(HelpDialog.getInstance());
                break;

            default:
                if (code >= KEY_ONE && code <= KEY_NINE) {  // keys 1 to 9
                    for (final FormController cf : getCurrent()) {
                        final int index = code - KEY_ONE;

                        cf.getView().finder().byType(WidgetType.TABS)         //
                        .ifPresentOrElse(                                     //
                            tab -> ((TabGroupUI) tab).focusActiveTab(index),  //
                            () -> cf.getView().focus(index));
                        e.preventDefault();
                    }
                }
            }
        }
    }

    @Override public void onKeyUp(final KeyUpEvent e) {
        // entering on tab keyUp
        if (e.getNativeKeyCode() == KEY_TAB) handleTableSelection(e);
    }

    public void showCalculator(@Nullable TextFieldUI textFieldUI) {
        final CalculatorPopover calculator = new CalculatorPopover(textFieldUI);
        RootPanel.get().add(calculator);
        calculatorOpened = true;
    }

    /** Sets current selected table ui. */
    public void setCurrent(TableUI current) {
        this.current = current;
    }

    /** Sets current modal trigger. */
    public void setModalTrigger(@Nullable Element modalTrigger) {
        this.modalTrigger = modalTrigger;
    }

    void handleTableSelection(final Event event) {
        final Element e     = targetElement(event);
        final Element table = findParentElement(TABLE_WIDGET_CLASSNAME, e, true);

        if (table != null) {
            if (current == null || current.getElement() != table) {
                deselectLastTable();

                final Application instance = Application.getInstance();

                final FormBox box = instance.findParentBox(table)    //
                                    .or(() -> instance.findParentBox(modalTrigger))  //
                                    .orElseThrow(() -> new IllegalStateException("Not a parent box for a table widget."));

                final FormUI view = box.getCurrent().getView();

                final String id = table.getId();
                createFromReference(getRetriever(), view.getUiModel(), id)  //
                .flatMap(i -> view.finder().find(i))                        //
                .ifPresent(t -> current = (TableUI) t);
            }

            // Handle Selection
            if (current != null) current.handleSelectionEvent(event);
        }
        else if (findParentElement(DONT_DESELECT_TABLE, e, true) == null) deselectLastTable();
    }

    /** Register handlers. */
    void registerHandlers() {
        final RootPanel root = RootPanel.get();
        root.addDomHandler(this, KeyPressEvent.getType());
        root.addDomHandler(this, KeyDownEvent.getType());
        root.addDomHandler(this, KeyUpEvent.getType());

        // listens to clicks to the whole document, not only the body.
        final Element document = Document.get().cast();
        DOM.setEventListener(document, this);
        DOM.sinkEvents(document, Event.ONCLICK);
        current = null;
    }

    private void deselectLastTable() {
        if (current != null) {
            current.clearSelectedRow();
            current = null;
        }
    }

    private boolean focusedHasOnClick() {
        if (!getCurrent().isPresent()) return false;
        final FormModel model = getCurrent().get().getModel();
        if (model != null && model.getLastFocus() != null)
            return createFromReference(getRetriever(), model.metadata(), model.getLastFocus().getPath())  //
                   .map(i -> i.widget().hasOnClickMethod()).orElse(false);
        return false;
    }

    private void handleCustomKeyShortcut(KeyDownEvent e) {
        for (final FormController formController : getCurrent()) {
            final Form metadata = formController.getView().getUiModel();
            for (final Widget widget : metadata.getWidgetByKeyShortcut(parseKey(e))) {
                Option<WidgetUI> trigger = Option.empty();

                // If we have a table we could try to find the trigger on the selected row.
                if (current != null && widget.getMultiple().isPresent())
                    trigger = formController.getView().finder().byItemIndex(widget, current.getSelectedRow());

                // If we don't find it on the current table, we then search over the entire form.
                if (!trigger.isPresent()) trigger = formController.getView().finder().byWidget(widget);

                // Only if we have a trigger, set focus and fire an eventual click.
                for (final WidgetUI widgetUI : trigger) {
                    e.preventDefault();
                    if (widgetUI.isEnabled()) {
                        widgetUI.setFocus(true);
                        if (widgetUI instanceof HasClickUI) widgetUI.click();
                    }
                }
            }
        }
    }

    private void handleTableSelection(final DomEvent<?> event) {
        handleTableSelection(Event.as(event.getNativeEvent()));
    }

    private void hideActiveModal() {
        hideActiveModal(false);
    }

    private void hideActiveModal(boolean byClick) {
        Application.getInstance().hideActiveModal(false, byClick);
    }

    @NotNull private String parseKey(KeyDownEvent e) {
        String s = "";
        if (isTriggerKeyDown(e)) s += "ctrl+";
        if (e.isAltKeyDown()) s += "alt+";
        if (e.isShiftKeyDown()) s += "shift+";
        return (s + (char) e.getNativeKeyCode()).toLowerCase();
    }

    //J-
    private native void refreshMetamodels(final String refreshUri)  /*-{
        var xhr = new XMLHttpRequest();
        //noinspection JSUnusedLocalSymbols
        xhr.addEventListener("load", function(event) { $wnd.location.reload(); });
        xhr.open("GET",  refreshUri);
        xhr.send(null);
    }-*/;
    //J+

    private Option<FormController> getCurrent() {
        return option(Application.getInstance().getActiveOrMain().getCurrent());
    }

    /** Checks if calculator widget over text field UI is open. */
    private boolean isCalculatorOpened() {
        return calculatorOpened;
    }

    private boolean isNoFocus(Element element) {
        return element.hasTagName("BODY");
    }

    //~ Methods ......................................................................................................................................

    /** Returns the singleton instance. */
    public static RootInputHandler getInstance() {
        return INSTANCE;
    }

    /** Returns true if the meta key of control key is down for this event. */
    public static boolean isTriggerKeyDown(KeyEvent<?> e) {
        return e.isMetaKeyDown() || e.isControlKeyDown();
    }

    //~ Static Fields ................................................................................................................................

    @NonNls public static final String DONT_DESELECT_TABLE = "dontDeselect";

    private static final RootInputHandler INSTANCE = new RootInputHandler();
}  // end class RootInputHandler
