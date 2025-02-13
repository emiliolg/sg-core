
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.serializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.infinispan.Cache;
import org.junit.Rule;
import org.junit.Test;

import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.FormBuilderPredefined;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.repository.ModelRepository;
import tekgenesis.view.shared.feedback.FeedbackEventData;
import tekgenesis.view.shared.response.FormModelResponse;
import tekgenesis.view.shared.response.SyncFormResponse;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.common.core.Option.empty;
import static tekgenesis.metadata.form.IndexedWidget.createIndexed;
import static tekgenesis.metadata.form.widget.FormBuilderPredefined.field;

@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc", "MagicNumber" })
public class FeedbackEventDataTest {

    //~ Instance Fields ..............................................................................................................................

    @Rule public ISRule infSRule = new ISRule();

    //~ Methods ......................................................................................................................................

    @Test public void testFeedbackEventDataISSerialization()
        throws IOException, BuilderException
    {
        final Form form = FormBuilderPredefined.form("", "tekgenesis.test", "InfSpanTestForm")
                          .children(field("Text").id("text"))
                          .unrestricted()
                          .withRepository(new ModelRepository())
                          .build();

        final Widget text = form.getElement("text");

        final FormModel modelToMap = new FormModel(form);
        modelToMap.set(text, "value");
        modelToMap.updateFocus(createIndexed(text, empty()).toSourceWidget());

        final FeedbackEventData cancellation = FeedbackEventData.cancellation();
        final FeedbackEventData started      = FeedbackEventData.started("1");
        final FeedbackEventData progress     = FeedbackEventData.progress(50, "");
        final FormModelResponse response     = new FormModelResponse();
        response.sync(new SyncFormResponse(modelToMap));
        final FeedbackEventData termination = FeedbackEventData.termination(response);

        final Cache<Object, Object> queue = infSRule.getMap("q");

        // Serialize
        queue.put(1, cancellation);
        queue.put(2, started);
        queue.put(3, progress);
        queue.put(4, termination);

        // Deserialize
        final List<Object> events = new ArrayList<>();
        for (int i = 1; i <= queue.size(); i++)
            events.add(queue.get(i));
        queue.clear();

        final FeedbackEventData cancellationEvent = event(events, 0);
        assertThat(cancellationEvent.isCancelled()).isTrue();
        final FeedbackEventData startedEvent = event(events, 1);
        assertThat(startedEvent.isStarted()).isTrue();
        assertThat(startedEvent.getUuid()).isEqualTo("1");
        final FeedbackEventData progressEvent = event(events, 2);
        assertThat(progressEvent.isRunning()).isTrue();
        assertThat(progressEvent.getProgress()).isEqualTo(50);
        final FeedbackEventData terminationEvent = event(events, 3);
        assertThat(terminationEvent.isTerminated()).isTrue();
        final FormModel modelFromMap = terminationEvent.getResponse().getSync().getModel(form);
        assertThat(modelFromMap.get(text)).isEqualTo("value");
        assertThat(modelFromMap.getLastFocus().getPath()).isEqualTo(text.getName());
    }  // end method testFeedbackEventDataISSerialization

    private FeedbackEventData event(List<Object> events, int index) {
        return (FeedbackEventData) events.get(index);
    }
}  // end class HzFeedbackEventDataTest
