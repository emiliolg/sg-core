
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import com.google.gwt.user.client.Timer;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Times;
import tekgenesis.view.client.service.ClientFormService;
import tekgenesis.view.client.service.DefaultCallback;
import tekgenesis.view.shared.feedback.FeedbackEventData;

@SuppressWarnings("MagicNumber")
class SteppedTimer {

    //~ Instance Fields ..............................................................................................................................

    private int delay = 0;

    private final int[] delays = { 1, 2, 3, 5, 10, 20, 60 };

    private final ClientFormService service;
    private int                     step  = 0;
    private Timer                   timer = null;

    //~ Constructors .................................................................................................................................

    SteppedTimer(ClientFormService service) {
        this.service = service;
    }

    //~ Methods ......................................................................................................................................

    void reset() {
        step  = 1;
        timer = null;
    }

    void schedule(@NotNull final String uuid, final DefaultCallback<FeedbackEventData> callback) {
        if (timer != null) timer.cancel();
        timer = new Timer() {
                @Override public void run() {
                    service.onFutureSync(uuid, callback);
                }
            };
        timer.schedule(getStepDelayMilliseconds());
        advance();
    }

    private void advance() {
        step = (step + 1) % 5;
        if (step == 0) delay++;
    }

    private int getStepDelayMilliseconds() {
        final int d = delays[delay < delays.length ? delay : delays.length - 1];
        return d * (int) Times.MILLIS_SECOND;
    }
}  // end class SteppedTimer
