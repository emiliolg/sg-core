
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui.base;

import java.util.function.Consumer;

import com.google.gwt.user.client.ui.FlowPanel;

import tekgenesis.common.core.IntIntTuple;
import tekgenesis.view.client.Application;
import tekgenesis.view.client.controller.FormController;
import tekgenesis.view.client.ui.FormUI;

import static tekgenesis.common.core.Tuple.tuple;

public abstract class AutoAligner {

    //~ Methods ......................................................................................................................................

    public abstract void checkLeft(int mainLeft);
    public abstract void checkRight(IntIntTuple main);

    public void leaveInsideScreen(Consumer<Integer> leftBlock, Consumer<IntIntTuple> rightBlock) {
        final Application instance = Application.getInstance();
        if (instance.isModalShowing()) {
            final FlowPanel container = instance.getModal() != null ? instance.getModal().getContainer() : null;
            if (container != null) {
                final int absoluteLeft = container.getAbsoluteLeft();
                leftBlock.accept(absoluteLeft);
                rightBlock.accept(tuple(absoluteLeft, container.getOffsetWidth()));
            }
        }
        else {
            final FormController current = instance.getActiveOrMain().getCurrent();
            if (current != null && current.getView() != null) {
                final FormUI view = current.getView();
                leftBlock.accept(view.getAbsoluteLeft());
                rightBlock.accept(tuple(view.getAbsoluteLeft(), view.getOffsetWidth()));
            }
        }
    }

    public abstract void setPosition();
}
