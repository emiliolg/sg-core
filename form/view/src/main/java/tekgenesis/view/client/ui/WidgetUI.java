
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.function.Consumer;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.Tuple;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.ui.base.HtmlDomUtils;
import tekgenesis.view.client.ui.base.Icon;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.Predefined.unreachable;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.of;
import static tekgenesis.common.core.Option.ofNullable;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.metadata.form.model.FormConstants.*;
import static tekgenesis.view.client.ui.base.HtmlDomUtils.clickElement;
import static tekgenesis.view.client.ui.base.Icon.replaceInElement;

/**
 * Base class for all widget UI components.
 */
public abstract class WidgetUI extends Composite implements Focusable, BaseWidgetUI {

    //~ Instance Fields ..............................................................................................................................

    protected final String CUSTOM_ICON = "custom-icon";

    com.google.gwt.user.client.ui.Widget content = null;

    final ComplexPanel    div;
    private final ModelUI container;

    private final Context context;
    private boolean       disabled;

    private UiFocusListener focusListener = null;

    private Option<Focusable>   focusTarget         = empty();
    private HandlerRegistration handlerRegistration = null;
    private Option<Element>     iconElem            = empty();
    private boolean             isFixed             = false;

    private Option<Element> labelElem = empty();

    private final Widget model;

    @Nullable private String prevContentStyleName = null;

    @Nullable private String prevStyleName = null;
    private boolean          readOnly;

    private Consumer<Tuple<WidgetUI, Boolean>> visibilityListener = null;
    private boolean                            visible            = true;

    //~ Constructors .................................................................................................................................

    WidgetUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        this(container, model, new FlowPanel());
    }

    WidgetUI(@NotNull final ModelUI container, @NotNull final Widget model, @NotNull final ComplexPanel mainPanel) {
        this.container = container;
        this.model     = model;
        div            = mainPanel;
        context        = new Context();
    }

    //~ Methods ......................................................................................................................................

    /** Returns true if this Widget supports focus operations at the moment. */
    public boolean acceptsFocus() {
        return focusTarget.isPresent() && !isDisabled() && !isReadOnly() && isVisible() && HtmlDomUtils.isVisible(this);
    }

    /** Programmatic clicks the link, for testing. */
    public void click() {
        for (final Focusable f : focusTarget)
            clickElement(((UIObject) f).getElement());
    }

    /** Return {@link ModelUI ui model container}. */
    public ModelUI container() {
        return container;
    }

    /** Disables element but not modifies the model. */
    public void disableElement(boolean disable) {
        final com.google.gwt.user.client.ui.Widget widget = focusTarget.isPresent() ? (com.google.gwt.user.client.ui.Widget) focusTarget.get() : div;
        if (disable) widget.addStyleName(VIEW_DISABLED);
        else widget.removeStyleName(VIEW_DISABLED);
    }

    /** Adds an icon to the component. */
    public final WidgetUI withIcon() {
        createIcon().forEach(icon -> {
            iconElem = some(icon);
            setIcon(model.getIconStyle());
        });
        return this;
    }

    /** Adds a label to the component. */
    public final WidgetUI withLabel() {
        createLabel().forEach(label -> {
            labelElem = some(label);
            setLabelFromExpression(model.getLabel());
        });
        return this;
    }

    /** Set widget focus listener. */
    public void withUiFocusListener(UiFocusListener fl) {
        focusListener = fl;
    }

    @Override public void setAccessKey(char key) {
        getFocusDelegate().setAccessKey(key);
    }

    /** Default add style to component. */
    public void setContentStyleName(String styleName) {
        if (content == null) throw unreachable();
        else {
            if (isNotEmpty(prevContentStyleName)) content.removeStyleName(prevContentStyleName);
            content.addStyleName(styleName);
            prevContentStyleName = styleName;
        }
    }  // end method setContentStyleName

    /** Returns the context info (location inside a table). */
    public Context getContext() {
        return context;
    }

    /** Returns true if the component is disabled. */
    public boolean isDisabled() {
        return disabled;
    }

    /** Returns true if the component is Enabled. */
    public boolean isEnabled() {
        return !disabled;
    }

    /** Sets the enabled state of the component. */
    public void setDisabled(boolean disabled) {
        setDisabled(disabled, focusTarget.isPresent() ? (com.google.gwt.user.client.ui.Widget) focusTarget.get() : div);
    }

    public boolean isVisible() {
        return visible;
    }

    @Override public void setFocus(boolean focused) {
        getFocusDelegate().setFocus(focused);
    }

    /** Adds an icon to the iconElement if it was define previously. */
    public final void setIcon(String iconStyle) {
        if (iconElem.isPresent()) iconInWidget(iconElem.get(), iconStyle);
    }

    /** Returns the id of the component. */
    public String getId() {
        return getElement().getId();
    }

    /** Set element id from metadata and container. */
    public void setId() {
        // Set container id and widget name
        final String qualification = container().getId();
        setId((isEmpty(qualification) ? "" : qualification + ".") + model.getName());
    }

    /** Sets the id of the component. */
    public void setId(String id) {
        getElement().setId(id);
    }

    /** Adds inline raw style to widget. */
    public void setInlineStyle(final String inlineStyle) {
        getElement().setAttribute(STYLE_ATTR, notNull(inlineStyle));
    }

    /** Get label container Element. */
    public Element getLabelContainer() {
        return getElement();
    }

    /** Returns the label element. */
    public Option<Element> getLabelElem() {
        return labelElem;
    }

    /** Adds a label to the component. */
    public void setLabelFromExpression(final String label) {
        if (!isEmpty(label) && labelElem.isPresent()) {
            final Node textChild = labelElem.get().getLastChild();
            if (textChild != null && textChild.getNodeType() == Node.TEXT_NODE) textChild.removeFromParent();
            labelElem.get().appendChild(Document.get().createTextNode(label));
        }
    }

    /** Set Field Width. */
    public void setMaxWidth() {
        if (content != null && !getModel().isExpand() && !context.getMultiple().isPresent())
            HtmlDomUtils.setMaxWidth(content.getElement(), model.getMaxWidth());
    }

    /** Returns the MetaModel of the UI component. */
    public final Widget getModel() {
        return model;
    }

    /**
     * Sets widget to read-only. Gives the ability to override it if widget doesn't support
     * read-only, like cancel buttons or search boxes.
     */
    public void setReadOnly(boolean readOnly) {
        final boolean prevState = disabled;
        if (readOnly) {
            setDisabled(true);
            this.readOnly = true;
            disabled      = prevState;  // save old state to re-enable
        }
        else {
            this.readOnly = false;
            disabled      = !disabled;
            setDisabled(prevState);
        }
    }

    public void setStyleName(String styleName) {
        if (isNotEmpty(prevStyleName)) removeStyleName(prevStyleName);
        if (isNotEmpty(styleName)) addStyleName(styleName);
        prevStyleName = styleName;
    }

    @Override public int getTabIndex() {
        return getFocusDelegate().getTabIndex();
    }

    @Override public void setTabIndex(int index) {
        getFocusDelegate().setTabIndex(index);
    }

    @Override public void setVisible(boolean visible) {
        this.visible = visible;
        super.setVisible(visible);

        if (visibilityListener != null) visibilityListener.accept(tuple(this, visible));
    }

    /** Returns true if the component is read only. */
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override protected void initWidget(com.google.gwt.user.client.ui.Widget widget) {
        div.add(widget);
        initComposite();
        content = widget;
        if (widget instanceof Focusable) setFocusTarget(some((Focusable) widget));
    }

    void addAffix(final String affixStyle) {
        addAttachHandler(event -> {
            if (handlerRegistration == null) addScrollHandler(affixStyle);
            else handlerRegistration.removeHandler();
        });
    }

    void addStyleNames() {
        addStyleName(WIDGET);
        final int col    = getModel().getCol();
        final int offset = getModel().getOffsetCol();
        if (col > 0) addStyleName("col-sm-" + col);
        if (offset > 0) {
            addStyleName(OFFSET_PREFIX + offset);
            addStyleName(WITH_OFFSET);
        }
    }

    /** Clear user input data. */
    void clear() {}

    /**
     * Return the Element that will contains the icon. If the widget doesn't support icon, then the
     * return value must be empty.
     */
    @NotNull Option<Element> createIcon() {
        return labelElem;
    }

    /**
     * Creates a default label for the widget. This method returns the Element that will be the
     * label for the widget. In the case the return value is empty, then that widget doesn't have a
     * label and it doesn't support labels.
     */
    @NotNull Option<Element> createLabel() {
        final LabelElement labelElement = Document.get().createLabelElement();

        final int labelCol = getModel().getLabelCol();
        setLabelWidth(labelElement, labelCol > 0 ? labelCol : 2);

        final Element divElem = getLabelContainer();
        divElem.insertFirst(labelElement);

        for (final Focusable focusable : focusTarget) {
            final String uid = getId() + "I";  // inner elem //DOM.createUniqueId();
            labelElement.setHtmlFor(uid);
            ((com.google.gwt.user.client.ui.Widget) focusable).getElement().setId(uid);
        }

        return some(labelElement);
    }

    /** Returns the Icon that it was just added in the widget. */
    Option<Icon> iconInWidget(Element icon, String iconStyle) {
        final Option<Icon> iconOption = replaceInElement(icon, iconStyle);
        iconOption.forEach(opIcon -> opIcon.addStyleName(CUSTOM_ICON));
        return iconOption;
    }

    final void initComposite() {
        super.initWidget(div);
        addStyleNames();
    }

    /** Specify that the component is inside a multiple This adds extra data and changes the id. */
    @SuppressWarnings("UnusedReturnValue")
    WidgetUI withinMultiple(final MultipleUI multiple, int row, int column) {
        getContext().setMultiple(multiple).setRow(row).setCol(column);
        final ModelUI c             = multiple.container();
        final String  qualification = c.getId();
        setId((isEmpty(qualification) ? "" : qualification + ".") + getModel().getName() + "#" + row);
        return this;
    }

    /** Sets the enabled state of the component. */
    void setDisabled(boolean d, final com.google.gwt.user.client.ui.Widget widget) {
        if (disabled != d) {
            disabled = d;
            disable(widget);
        }
    }

    /** Returns focus target delegate. */
    Option<Focusable> getFocusTarget() {
        return focusTarget;
    }

    /** Set focus target delegate. */
    void setFocusTarget(Option<Focusable> focusTarget) {
        this.focusTarget = focusTarget;
        for (final Focusable focusable : focusTarget) {
            addFocusListeners(((com.google.gwt.user.client.ui.Widget) focusable));
            if (model.isSkipTab()) focusable.setTabIndex(-2);
        }
    }

    /** Set Label Width. */
    void setLabelWidth(LabelElement labelElement, int col) {
        if (col > 0) labelElement.setClassName("control-label col-sm-" + col);
    }

    /** Set visibility listener. */
    void setVisibilityListener(Consumer<Tuple<WidgetUI, Boolean>> visibilityListener) {
        this.visibilityListener = visibilityListener;
    }

    private void addFocusListeners(final com.google.gwt.user.client.ui.Widget acceptFocus) {
        acceptFocus.addDomHandler(event -> {
                addStyleName(FOCUSED);
                if (focusListener != null) focusListener.onFocus(this);
            },
            FocusEvent.getType());

        acceptFocus.addDomHandler(event -> removeStyleName(FOCUSED), BlurEvent.getType());
    }

    private void addScrollHandler(final String affixStyle) {
        final int offsetY = getAbsoluteTop() - NAV_BAR;

        handlerRegistration = Window.addWindowScrollHandler(event -> {
                if (Window.getScrollTop() >= offsetY) {
                    addStyleName(affixStyle);
                    isFixed = true;
                }
                else if (isFixed) {
                    removeStyleName(affixStyle);
                    isFixed = false;
                }
            });
    }

    private void disable(final com.google.gwt.user.client.ui.Widget widget) {
        if (widget instanceof HasEnabled) ((HasEnabled) widget).setEnabled(!disabled);

        if (widget instanceof HasWidgets) {
            for (final com.google.gwt.user.client.ui.Widget w : (HasWidgets) widget)
                disable(w);
        }
    }

    private Focusable getFocusDelegate() {
        return focusTarget.getOrFail("Attempting to operate on a Widget '" + model.getName() + "' that does not accept focus");
    }

    //~ Static Fields ................................................................................................................................

    private static final int NAV_BAR = 55;

    //~ Inner Classes ................................................................................................................................

    /**
     * Context of the component (the location inside a table).
     */
    public static final class Context {
        @NotNull private Option<Integer>     col      = empty();
        @NotNull private Option<MultipleUI>  multiple = empty();
        @NotNull private Option<Integer>     row      = empty();

        /** Returns an optional col. */
        @NotNull public Option<Integer> getCol() {
            return col;
        }

        /** Returns an optional item index (the index in the row model). */
        @NotNull public Option<Integer> getItem() {
            return row.flatMap(s -> multiple.map(m -> m.mapSectionToItem(s)));
        }

        /** Returns the optional containing multiple. */
        @NotNull public Option<MultipleUI> getMultiple() {
            return multiple;
        }

        /** Returns an optional row index. */
        @NotNull public Option<Integer> getRow() {
            return row;
        }

        Context setRow(int aRow) {
            row = of(aRow);
            return this;
        }

        private Context setCol(int aCol) {
            col = of(aCol);
            return this;
        }

        private Context setMultiple(@Nullable MultipleUI m) {
            multiple = ofNullable(m);
            return this;
        }
    }  // end class Context
}  // end class WidgetUI
