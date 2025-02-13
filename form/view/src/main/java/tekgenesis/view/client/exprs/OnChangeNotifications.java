
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.exprs;

import java.util.ArrayList;
import java.util.List;

import com.google.web.bindery.event.shared.EventBus;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.SourceWidget;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.view.client.event.OnChangeSyncEvent;

/**
 * Collects widget on_change pending notifications.
 */
public class OnChangeNotifications {

    //~ Instance Fields ..............................................................................................................................

    private final List<SourceWidget> pendings;

    //~ Constructors .................................................................................................................................

    OnChangeNotifications() {
        pendings = new ArrayList<>();
    }

    //~ Methods ......................................................................................................................................

    void add(@NotNull final SourceWidget widget) {
        if (!pendings.contains(widget)) pendings.add(widget);
    }

    void fire(@NotNull final EventBus bus, @NotNull final FormModel root) {
        if (!pendings.isEmpty()) bus.fireEvent(new OnChangeSyncEvent(root, pendings));
    }

    void remove(@NotNull final SourceWidget widget) {
        pendings.remove(widget);
    }
}
