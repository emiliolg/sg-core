
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.user.client.ui.ComplexPanel;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.core.Option.some;

/**
 * Container UI widget.
 */
public abstract class ContainerUI extends WidgetUI implements Iterable<WidgetUI>, HasWidgetsUI {

    //~ Instance Fields ..............................................................................................................................

    private final List<WidgetUI> children     = new ArrayList<>();
    private Consumer<String>     labelHandler = null;
    private Option<Element>      legend       = Option.empty();

    //~ Constructors .................................................................................................................................

    ContainerUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        initComposite();
    }

    ContainerUI(@NotNull final ModelUI container, @NotNull final Widget model, @NotNull final ComplexPanel containerPanel) {
        super(container, model, containerPanel);
        initComposite();
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean acceptsFocus() {
        return immutable(children).exists(WidgetUI::acceptsFocus);
    }

    @Override public void addChild(final WidgetUI w) {
        children.add(w);
        addChildPanel(w);
    }

    @Override public Iterator<WidgetUI> iterator() {
        return children.iterator();
    }

    @Override public void setFocus(boolean focused) {
        // can't know what to focus...just to un-focus
        if (!focused) immutable(children).getFirst(WidgetUI::acceptsFocus).ifPresent(ui -> ui.setFocus(false));
    }

    @Override public void setLabelFromExpression(String label) {
        super.setLabelFromExpression(label);
        if (labelHandler != null && !isEmpty(label) && getLabelElem().isPresent()) labelHandler.accept(label);
    }

    void addChildPanel(final WidgetUI w) {
        div.add(w);
    }

    @NotNull @Override Option<Element> createIcon() {
        if (!legend.isPresent() && (isAttached() || getModel().isRequired())) createLegendElement();
        return legend;
    }

    @NotNull @Override Option<Element> createLabel() {
        if (!legend.isPresent() && (isAttached() || getModel().isRequired())) createLegendElement();
        return legend;
    }

    void removeLegendElement() {
        if (legend.isPresent()) getElement().getFirstChildElement().removeFromParent();
    }

    @Override WidgetUI withinMultiple(final MultipleUI multiple, int row, int column) {
        super.withinMultiple(multiple, row, column);
        for (final WidgetUI child : children)
            child.withinMultiple(multiple, row, column);
        return this;
    }

    /**
     * Sets the label change handler.
     *
     * @param  labelHandler
     */
    void setLabelHandler(Consumer<String> labelHandler) {
        this.labelHandler = labelHandler;
    }

    private void createLegendElement() {
        final HeadingElement h4 = Document.get().createHElement(4);
        h4.addClassName("col-sm-12");
        legend = some(h4);
        addStyleName("named");
        getElement().insertFirst(h4);
    }

    //~ Static Fields ................................................................................................................................

    static final String GROUP_CONTAINER = "group-container";
}  // end class ContainerUI
