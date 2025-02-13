
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.util;

import java.util.List;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.progress.impl.BackgroundableProcessIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ex.ProgressIndicatorEx;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.lang.mm.FileUtils;
import tekgenesis.lang.mm.ui.MMUIInformer;

/**
 * BackGround Tasks execution.
 */
public class BackgroundTask extends Task.Backgroundable implements Retryable {

    //~ Instance Fields ..............................................................................................................................

    private final List<? extends Item> items;
    private final String               name;
    private boolean                    retry = true;
    private final Double               step;

    //~ Constructors .................................................................................................................................

    /**
     * Background Task constructor.
     *
     * @param  name     task name
     * @param  project  project
     * @param  items    items
     */
    public BackgroundTask(@NotNull String name, @Nullable final Project project, List<? extends Item> items) {
        super(project, name, true, ALWAYS_BACKGROUND);
        this.items = items;
        step       = 1D / items.size();
        this.name  = name;
    }

    //~ Methods ......................................................................................................................................

    /** Execute task. */
    public void executeWithProgress() {
        final BackgroundableProcessIndicator indicator = new BackgroundableProcessIndicator(this);
        indicator.start();
        step(indicator, "Starting " + name + " task...", false);
        run(indicator);
    }

    /**
     * Run Task.
     *
     * @param  indicator  progress indicator
     */
    @Override public void run(@NotNull final ProgressIndicator indicator) {
        FileUtils.saveAndSynchronizeFiles();  // Properties files are not automatically saved

        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            for (final Item item : items) {
                final String result = processItem(item);
                step(indicator, result, true);
            }

            step(indicator, name + " task finished.", false);

            FileUtils.synchronizeFiles();  // Synchronize for new files to appear

            ((ProgressIndicatorEx) indicator).processFinish();
        });
    }

    /** @return  Should retry task */
    @Override public boolean shouldRetry() {
        return retry;
    }

    /** Stop retrying. */
    @Override public void stopRetrying() {
        retry = false;
    }

    /**
     * Process Item.
     *
     * @param   item  Item
     *
     * @return  processing result
     */
    protected String processItem(@NotNull Item item) {
        return item.process(this);
    }

    private void step(@NotNull final ProgressIndicator indicator, @NotNull final String text, boolean advance) {
        indicator.setText(text);
        MMUIInformer.showLogMessage(getProject(), text);
        if (advance) indicator.setFraction(indicator.getFraction() + step);
    }
}  // end class BackgroundTask
