
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.event;

import com.google.web.bindery.event.shared.*;

/**
 * Wraps an {@link EventBus}.
 */
public abstract class FormBus extends SimpleEventBus {

    //~ Constructors .................................................................................................................................

    protected FormBus() {
        addHandlers();
    }

    //~ Methods ......................................................................................................................................

    protected void hideSubform(SubformEvent e) {}

    protected void onCancel(CancelEvent e) {}

    protected void onDelete(DeleteEvent e) {}

    protected void onDeprecate(DeprecateEvent e) {}

    protected void onLoadForm(LoadFormEvent e) {}

    protected void onMethodInvocation(MethodInvocationSyncEvent e) {}

    protected void onRowChange(OnRowChangeEvent e) {}

    protected void onRowSelected(OnRowSelectedEvent e) {}

    protected void onSubmit(SubmitEvent e) {}

    protected void onSyncTriggered(OnLoadFormSyncEvent e) {}

    protected void onSyncTriggered(OnSuggestNewSyncEvent e) {}

    protected void onSyncTriggered(OnChangeSyncEvent e) {}

    protected void onSyncTriggered(OnClickSyncEvent e) {}

    protected void onSyncTriggered(LazyFetchSyncEvent e) {}

    protected void onSyncTriggered(OnBlurSyncEvent e) {}

    protected void onSyncTriggered(OnNewLocationSyncEvent e) {}

    protected void onSyncTriggered(OnAbstractSyncEvent e) {}

    protected void onSyncTriggered(OnScheduleSyncEvent e) {}

    protected void showSubform(SubformEvent e) {}

    @SuppressWarnings("OverlyCoupledMethod")
    private void addHandlers() {
        addHandler(LoadFormEvent.TYPE, this::onLoadForm);
        addHandler(OnClickSyncEvent.TYPE, this::onSyncTriggered);
        addHandler(LazyFetchSyncEvent.TYPE, this::onSyncTriggered);
        addHandler(OnBlurSyncEvent.TYPE, this::onSyncTriggered);
        addHandler(OnNewLocationSyncEvent.TYPE, this::onSyncTriggered);
        addHandler(OnAbstractSyncEvent.TYPE, this::onSyncTriggered);
        addHandler(OnChangeSyncEvent.TYPE, this::onSyncTriggered);
        addHandler(OnScheduleSyncEvent.TYPE, this::onSyncTriggered);
        addHandler(OnLoadFormSyncEvent.TYPE, this::onSyncTriggered);
        addHandler(OnSuggestNewSyncEvent.TYPE, this::onSyncTriggered);
        addHandler(MethodInvocationSyncEvent.TYPE, this::onMethodInvocation);
        addHandler(OnRowChangeEvent.TYPE, this::onRowChange);
        addHandler(OnRowSelectedEvent.TYPE, this::onRowSelected);
        addHandler(SubmitEvent.TYPE, this::onSubmit);
        addHandler(DeleteEvent.TYPE, this::onDelete);
        addHandler(DeprecateEvent.TYPE, this::onDeprecate);
        addHandler(CancelEvent.TYPE, this::onCancel);

        addHandler(SubformEvent.TYPE, new SubformEvent.LoadSubformEventHandler() {
                @Override public void showSubform(SubformEvent e) {
                    FormBus.this.showSubform(e);
                }

                @Override public void hideSubform(SubformEvent e) {
                    FormBus.this.hideSubform(e);
                }
            });
    }  // end method addHandlers
}  // end class FormBus
