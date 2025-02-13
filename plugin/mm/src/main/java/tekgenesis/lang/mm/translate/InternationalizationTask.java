
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.translate;

import java.util.List;

import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.util.BackgroundTask;
import tekgenesis.lang.mm.util.Item;

/**
 * Internationalization Task.
 */
public class InternationalizationTask extends BackgroundTask {

    //~ Instance Fields ..............................................................................................................................

    private final boolean connected;

    //~ Constructors .................................................................................................................................

    /**
     * Default constructor.
     *
     * @param  name       task name
     * @param  project    project
     * @param  items      items
     * @param  connected  is connected
     */
    public InternationalizationTask(@NotNull String name, @Nullable Project project, List<? extends Item> items, boolean connected) {
        super(name, project, items);
        this.connected = connected;
    }

    //~ Methods ......................................................................................................................................

    /**
     * Items to process.
     *
     * @param   item  item
     *
     * @return  translation
     */
    @Override public String processItem(@NotNull Item item) {
        final TranslationItem translationItem = (TranslationItem) item;
        translationItem.setConnected(connected);
        return translationItem.process(this);
    }
}
