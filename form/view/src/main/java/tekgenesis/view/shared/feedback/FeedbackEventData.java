
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.feedback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.view.shared.response.FormModelResponse;

import static java.lang.Math.max;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.view.shared.feedback.FeedbackEventData.FutureStatus.*;

/**
 * FeedbackEventData used on long executions.
 */
@SuppressWarnings("FieldMayBeFinal")
public class FeedbackEventData implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private List<String> messages;
    private int          progress;

    private FormModelResponse response;
    private FutureStatus      status;
    private String            uuid;

    //~ Constructors .................................................................................................................................

    protected FeedbackEventData() {
        response = null;
        uuid     = null;
        status   = null;
        messages = null;
        progress = -1;
    }

    protected FeedbackEventData(FormModelResponse response, String uuid, FutureStatus status, List<String> messages, Integer progress) {
        this.response = response;
        this.uuid     = uuid;
        this.status   = status;
        this.messages = messages;
        this.progress = progress;
    }

    //~ Methods ......................................................................................................................................

    /** Join current feedback execution events with given. */
    public void join(@NotNull FeedbackEventData event) {
        progress = max(progress, event.getProgress());
        messages.addAll(event.getMessages());
    }

    /** Print out feedback execution messages. */
    public void printOutMessages() {
        if (!messages.isEmpty()) System.out.println(Colls.mkString(messages, "\n"));
    }

    /** Return true if feedback execution status is cancelled. */
    public boolean isCancelled() {
        return status == CANCELLED;
    }

    /** Return true if feedback execution status is completed. */
    public boolean isCompleted() {
        return status == COMPLETED;
    }

    /** Return true if feedback execution status is interrupted by an exception. */
    public boolean isInterrupted() {
        return status == FutureStatus.INTERRUPTED;
    }

    /** Return true if feedback execution status is started. */
    public boolean isStarted() {
        return status == FutureStatus.STARTED;
    }

    /** Return true if feedback execution status is terminated. */
    public boolean isTerminated() {
        return isCompleted() || isCancelled() || isInterrupted();
    }

    /** Return true if feedback execution status is running. */
    public boolean isRunning() {
        return status == RUNNING;
    }

    /** Return execution last collected message. */
    @NotNull public String getLastMessage() {
        return messages.isEmpty() ? "" : messages.get(messages.size() - 1);
    }

    /** Return execution collected messages. */
    @NotNull public List<String> getMessages() {
        return messages;
    }

    /** Return feedback execution progress. */
    public int getProgress() {
        return progress;
    }

    /** Return feedback execution response if completed successfully. */
    public FormModelResponse getResponse() {
        return response;
    }

    /** Return feedback execution uuid. */
    @NotNull public String getUuid() {
        return uuid;
    }

    //~ Methods ......................................................................................................................................

    /** Create cancellation feedback event. */
    public static FeedbackEventData cancellation() {
        return new FeedbackEventData(null, null, CANCELLED, Collections.emptyList(), -1);
    }
    /** Create exception feedback event. */
    public static FeedbackEventData exception(FormModelResponse response) {
        return new FeedbackEventData(response, null, INTERRUPTED, Collections.emptyList(), -1);
    }

    /** Create progress join feedback event. */
    public static FeedbackEventData join(String uuid) {
        return new FeedbackEventData(null, uuid, RUNNING, new ArrayList<>(), -1);
    }

    /** Create progress feedback event. */
    public static FeedbackEventData progress(int progress, @Nullable String message) {
        return new FeedbackEventData(null, null, RUNNING, isEmpty(message) ? new ArrayList<>() : Collections.singletonList(message), progress);
    }

    /** Create started feedback event. */
    public static FeedbackEventData started(String uuid) {
        return new FeedbackEventData(null, uuid, STARTED, new ArrayList<>(), -1);
    }

    /** Create termination feedback event. */
    public static FeedbackEventData termination(FormModelResponse response) {
        return new FeedbackEventData(response, null, COMPLETED, Collections.emptyList(), 100);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 8275997549531012014L;

    //~ Enums ........................................................................................................................................

    public enum FutureStatus { STARTED, RUNNING, COMPLETED, CANCELLED, INTERRUPTED }
}  // end class FeedbackEventData
