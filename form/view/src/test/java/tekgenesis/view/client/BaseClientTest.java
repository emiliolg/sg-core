
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

import com.google.gwt.dom.client.Element;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.view.client.controller.FormController;
import tekgenesis.view.shared.response.ResponseError;

import static java.util.Arrays.asList;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.collections.Colls.mkString;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.view.client.BoxConfiguration.Default;
import static tekgenesis.view.client.BoxHandler.attach;
import static tekgenesis.view.client.BoxHandler.detach;
import static tekgenesis.view.client.FormHistory.getFormHistory;
import static tekgenesis.view.client.InitTestContextService.App.getInstance;

/**
 * Base class for Client side testing.
 */
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public abstract class BaseClientTest extends GWTTestCase {

    //~ Methods ......................................................................................................................................

    @Override public String getModuleName() {
        return "tekgenesis.test";
    }

    @Override protected void gwtTearDown()
        throws Exception
    {
        super.gwtTearDown();

        getInstance().destroyContext(new AsyncCallback<Void>() {
                @Override public void onFailure(Throwable caught) {}
                @Override public void onSuccess(Void result) {}
            });
    }

    @NotNull protected abstract String[] getProjectPaths();

    TestBuilder load(@NotNull final String fqn, @NotNull final FormCall callBack) {
        return new TestBuilder().load(fqn, callBack);
    }

    TestBuilder load(@NotNull final String fqn, @Nullable final String pk, @Nullable final String parameters, @NotNull final FormCall callBack) {
        return new TestBuilder().load(fqn, pk, parameters, callBack);
    }

    //~ Static Fields ................................................................................................................................

    private static final String FORM_DIV_ID    = "form";
    private static final int    HALF_SECOND    = 500;
    private static final int    TIMEOUT_MILLIS = HALF_SECOND * 5;

    private static final EmptyCall EMPTY_CALL = new EmptyCall();

    //~ Enums ........................................................................................................................................

    enum TestPhaseType { LOAD, REDIRECT, SYNC, RELOAD, UNLOAD }

    //~ Inner Interfaces .............................................................................................................................

    interface FormCall extends Consumer<FormTester> {
        @Override void accept(FormTester f);
    }

    //~ Inner Classes ................................................................................................................................

    private class DelayTimer extends Timer {
        private final Queue<TestPhase> phases;

        private DelayTimer(Queue<TestPhase> phases) {
            this.phases = phases;
        }

        @Override public void run() {
            fail("Test '" + getName() + "' timeout expired! Expecting " + phases.peek());
        }

        /** Specify test timeout delay in milliseconds. */
        void finishIn(int timeout) {
            cancel();
            delayTestFinish(timeout + HALF_SECOND);
            schedule(timeout);
        }
    }

    private static class EmptyCall implements FormCall {
        @Override public void accept(FormTester f) {}
    }

    @SuppressWarnings("WeakerAccess")
    class TestBuilder implements FormBoxListener, AsyncCallback<Void> {
        private FormBox          box;
        private final DelayTimer delay;

        private final Queue<TestPhase> queue;

        private TestBuilder() {
            queue = new LinkedList<>();
            delay = new DelayTimer(queue);
            start();
        }

        @Override public void onError(ResponseError error) {
            if (!"testErrorShowcase".equals(getName()))  // this test is testing error panel.
                fail("Test Failed with error! " + error.getDevMessage() + "\n" + mkString(error.getStack(), "Caused by: ", "\n\t", "\n"));
            else stateChanged("", TestPhaseType.SYNC);   // Error messages tested on testErrorShowcase should call Sync phase
        }

        @Override public void onFailure(Throwable throwable)
        {
            fail("Test Failed with failure! " + throwable.getMessage());
        }

        @Override public void onLoad(String url) {
            stateChanged("{" + url + "}", TestPhaseType.LOAD, TestPhaseType.REDIRECT, TestPhaseType.RELOAD);
        }

        @Override public void onLoad(String fqn, String pk, String parameters) {
            stateChanged("{" + fqn + ", " + pk + ", " + parameters + "}", TestPhaseType.LOAD, TestPhaseType.REDIRECT, TestPhaseType.RELOAD);
        }

        @Override public void onSuccess(Void result) {}

        @Override public void onUnload() {
            stateChanged("", TestPhaseType.UNLOAD);
        }

        @Override public void onUpdate() {
            stateChanged("", TestPhaseType.SYNC);
        }

        TestBuilder emptySync() {
            queue.add(new TestPhase(TestPhaseType.SYNC, EMPTY_CALL));
            return this;
        }

        TestBuilder load(final String fqn, final FormCall call) {
            System.out.println("BaseClientTest$TestBuilder.load ADDING PHASE: " + fqn);
            queue.add(new TestPhase(TestPhaseType.LOAD, fqn, call));
            return this;
        }

        TestBuilder load(final String fqn, @Nullable final String pk, @Nullable final String parameters, final FormCall call) {
            System.out.println("BaseClientTest$TestBuilder.load ADDING PHASE: " + fqn);
            queue.add(new TestPhase(TestPhaseType.LOAD, fqn, call).withPk(pk).withParameters(parameters));
            return this;
        }

        TestBuilder redirect(final String form, final FormCall call) {
            queue.add(new TestPhase(TestPhaseType.REDIRECT, form, call));
            return this;
        }

        TestBuilder reload() {
            queue.add(new TestPhase(TestPhaseType.RELOAD));
            return this;
        }

        TestBuilder sync() {
            queue.add(new TestPhase(TestPhaseType.SYNC));
            return this;
        }

        TestBuilder sync(final FormCall call) {
            queue.add(new TestPhase(TestPhaseType.SYNC, call));
            return this;
        }

        void test() {
            try {
                delay();
                final TestPhase phase      = queue.peek();
                final String    fqn        = phase.getFqn();
                final String    pk         = phase.getPk();
                final String    parameters = phase.getParameters();
                getFormHistory().load(fqn, pk, parameters);
            }
            catch (final Exception e) {
                fail(e.getMessage());
            }
        }

        TestBuilder unload() {
            queue.add(new TestPhase(TestPhaseType.UNLOAD));
            return this;
        }

        private void delay() {
            delay.finishIn(TIMEOUT_MILLIS);
        }

        private FormBox initializeBox() {
            final RootPanel previous = RootPanel.get(FORM_DIV_ID);
            if (previous == null) {
                final Element panel = new SimplePanel().getElement();
                panel.setId(FORM_DIV_ID);
                DOM.appendChild(RootPanel.getBodyElement(), panel);
            }
            return attach(FORM_DIV_ID, new Default().boundedByHistory());
        }

        private void start() {
            // Prepare Application.
            box = initializeBox();
            getInstance().initTestContext(getProjectPaths(), getName(), this);
            box.addListener(this);
        }

        private void stateChanged(String debug, TestPhaseType... expected) {
            if (queue.isEmpty()) fail("Unexpected phase! Forgot to add an " + Arrays.toString(expected) + " phase? " + debug + " Test: " + getName());

            final TestPhase phase = queue.poll();

            phase.check(box.getCurrent());

            if (!asList(expected).contains(phase.getType()))
                fail("Unexpected phase! Expected " + Arrays.toString(expected) + " but " + phase.getType() + " found!");

            for (final FormCall call : phase.getCall())
                call.accept(new FormTester(box));

            if (queue.isEmpty()) stop();
            else delay();
        }

        private void stop() {
            box.removeListener(this);
            detach(FORM_DIV_ID);
            final Timer timer = new Timer() {
                    @Override public void run() {
                        delay.cancel();
                        finishTest();
                    }
                };
            timer.schedule(HALF_SECOND);
            delay();
        }
    }  // end class TestBuilder

    @SuppressWarnings("NonJREEmulationClassesInClientCode")
    private static class TestPhase {
        private final Option<FormCall> call;
        private final String           fqn;
        private String                 parameters = null;
        private String                 pk         = null;

        private final TestPhaseType type;

        TestPhase(TestPhaseType type) {
            this.type = type;
            call      = Option.empty();
            fqn       = "";
        }

        TestPhase(TestPhaseType type, FormCall call) {
            this.type = type;
            this.call = some(call);
            fqn       = "";
        }

        TestPhase(TestPhaseType type, String fqn, FormCall call) {
            this.type = type;
            this.fqn  = fqn;
            this.call = some(call);
        }

        @Override public String toString() {
            return "TestPhase { type = " + type + ", form = '" + fqn + "\' }";
        }

        void check(FormController controller) {
            if (!isEmpty(fqn))
                assertTrue("Unexpected form! fqn: " + fqn + " controller: " + controller.getFormName(), fqn.startsWith(controller.getFormName()));
        }

        TestPhase withParameters(@Nullable String params) {
            parameters = params;
            return this;
        }

        TestPhase withPk(@Nullable String primaryKey) {
            pk = primaryKey;
            return this;
        }

        @Nullable String getParameters() {
            return parameters;
        }

        @Nullable String getPk() {
            return pk;
        }

        private Option<FormCall> getCall() {
            return call;
        }

        private String getFqn() {
            return fqn;
        }

        private TestPhaseType getType() {
            return type;
        }
    }  // end class TestPhase
}  // end class BaseClientTest
