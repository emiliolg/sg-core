
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

import com.google.gwt.user.client.ui.FlowPanel;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.Predefined;
import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.Parent;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetDef;

import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.of;

/**
 * A Widget definition UI widget ;).
 */
public class WidgetDefUI extends ContainerUI implements ModelUI {

    //~ Instance Fields ..............................................................................................................................

    // public class WidgetDefUI extends WidgetUI implements ModelUI {

    private final List<WidgetUI>              children = new ArrayList<>();
    @NotNull private final WidgetUIFinder     finder;
    @NotNull private Option<WidgetDefHandler> handler;
    @NotNull private final WidgetDef          metadata;

    private final FlowPanel       panel;
    private final WidgetDefParent parent;

    //~ Constructors .................................................................................................................................

    public WidgetDefUI(@NotNull final ModelUI container, @NotNull final Widget model, @NotNull final WidgetDef def) {
        super(container, model);
        metadata = def;

        panel = new FlowPanel();
        div.add(panel);
        addContainerStyle(model);

        finder = new WidgetUIFinder(this);
        parent = new WidgetDefParent();

        handler = empty();
    }

    //~ Methods ......................................................................................................................................

    @Override public void addChild(WidgetUI w) {
        children.add(w);
        panel.add(w);
    }

    /** Add specified {@link WidgetDefHandler widget definition handler}. */
    public void addHandler(@NotNull final WidgetDefHandler h) {
        handler = of(h);
    }

    @NotNull @Override public WidgetUIFinder finder() {
        return finder;
    }

    /** Initialization for {@link WidgetDef optional widget definitions}. */
    public void initialized() {
        handler.ifPresent(WidgetDefHandler::initialized);
    }

    @Override public Iterator<WidgetUI> iterator() {
        return children.iterator();
    }

    @NotNull @Override public Option<Parent<ModelUI>> parent() {
        return of(parent);
    }

    @Override public void setId() {
        setId(parent.fqn());
    }

    @NotNull @Override public WidgetDef getUiModel() {
        return metadata;
    }

    /** Return true if widget has no children. */
    public boolean isEmpty() {
        return children.isEmpty();
    }

    @Override WidgetUI withinMultiple(MultipleUI multiple, int row, int column) {
        super.withinMultiple(multiple, row, column);
        for (final WidgetUI child : children)
            child.setId();
        return this;
    }

    private void addContainerStyle(@NotNull Widget model) {
        if (!model.getNoLabel()) {
            div.addStyleName("row");
            panel.addStyleName(GROUP_CONTAINER);
        }
    }

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Event handler for {@link WidgetDef widget definition}.
     */
    public interface WidgetDefHandler {
        /** Called when optional {@link WidgetDef widget definition} is initialized. */
        void initialized();
    }

    //~ Inner Classes ................................................................................................................................

    private class WidgetDefParent implements Parent<ModelUI> {
        @NotNull @Override public Widget anchor() {
            return getModel();
        }

        @NotNull @Override public String fqn() {
            final String qualification = container().getId();
            return (Predefined.isEmpty(qualification) ? "" : qualification + ".") + name();
        }

        @NotNull @Override public Option<Integer> item() {
            return getContext().getItem();
        }

        @NotNull @Override public ModelUI value() {
            return container();
        }
    }
}  // end class WidgetDefUI
