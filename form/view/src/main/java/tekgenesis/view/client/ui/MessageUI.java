
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Constants;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.common.Predefined.equal;

/**
 * Interface for Widget Messages and Title.
 */
public abstract class MessageUI extends WidgetUI implements HasScalarValueUI {

    //~ Constructors .................................................................................................................................

    MessageUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
    }

    //~ Methods ......................................................................................................................................

    @Override public void addChangeHandler(ValueChangeHandler<Object> changeHandler) {  /* Handlers not supported. */
    }

    @Override public void setDisabled(boolean disabled) {
        // Ignore, messages disabled by default.
    }

    @Override public void setValue(@Nullable Object value, boolean fireEvents) {
        if (fireEvents) throw new UnsupportedOperationException(Constants.TO_BE_IMPLEMENTED);
        setValue(value);
    }

    /** Removes last node from a element. */
    void removeLastNode(final Element element) {
        final Node lastNode = element.getLastChild();
        if (lastNode != null && (lastNode.getNodeType() == Node.TEXT_NODE || equal(lastNode.getNodeName(), "DIV"))) lastNode.removeFromParent();
    }
}
