
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.controller;

import com.google.gwt.user.client.Timer;
import com.google.web.bindery.event.shared.EventBus;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.logging.Logger;
import tekgenesis.metadata.form.IndexedWidget;
import tekgenesis.metadata.form.InvokeData;
import tekgenesis.metadata.form.SourceWidget;
import tekgenesis.metadata.form.model.*;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.DynamicFormBox.BodyOnlyFormBox;
import tekgenesis.view.client.event.*;
import tekgenesis.view.client.exprs.ExpressionEvaluatorFactory;
import tekgenesis.view.client.ui.*;
import tekgenesis.view.shared.response.SyncFormResponse;

import static java.lang.Math.min;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.collections.Colls.first;
import static tekgenesis.common.core.Option.of;
import static tekgenesis.metadata.form.model.ChangeListener.Registration;
import static tekgenesis.metadata.form.widget.FormAction.SAVE;
import static tekgenesis.view.client.Application.getInstance;
import static tekgenesis.view.client.FormViewMessages.MSGS;
import static tekgenesis.view.client.exprs.ExpressionEvaluatorFactory.ValidationResult;

/**
 * The controller for the Form View.
 */
public class FormController {

    //~ Instance Fields ..............................................................................................................................

    private final EventBus                   bus;
    private final ExpressionEvaluatorFactory factory;
    private final UiFocusListener            focusListener;

    private final Form      form;
    private Timer           method;
    private final FormModel model;
    private final String    parameters;
    private final String    pk;
    private Timer           schedule;
    private final FormUI    view;

    //~ Constructors .................................................................................................................................

    /** Construct the controller for the Form View. */
    public FormController(@NotNull final FormModel model, @NotNull final EventBus bus, @Nullable final String pk, @Nullable final String parameters) {
        form            = model.metadata();
        this.model      = model;
        this.bus        = bus;
        this.pk         = pk;
        this.parameters = parameters;
        schedule        = null;
        method          = null;
        view            = new FormUI(form, model.isDeprecated());
        factory         = new ExpressionEvaluatorFactory(this);
        focusListener   = ui -> {
                              model.updateFocus(ui.toSourceWidget());
                              if (ui instanceof BaseHasScalarValueUI) ((BaseHasScalarValueUI) ui).initPrev();
                          };
    }

    //~ Methods ......................................................................................................................................

    /** Behavior to be executed after model is load. */
    public void afterModelLoad() {
        if (model.metadata().isDeprecableBoundModel()) expressions().deprecated(model.isDeprecated());
        if (model.isReadOnly()) expressions().readOnly();
        expressions().filters();
        scheduleSyncInterval();
    }

    /** Behavior to be executed after model is sync. */
    public void afterModelSync() {
        if (model.metadata().isDeprecableBoundModel()) expressions().deprecated(model.isDeprecated());
        if (model.hasReadOnlyChanged()) expressions().readOnly();
        expressions().filters();
    }

    /** Behavior to be executed before model is unload. */
    public void beforeModelUnload() {
        if (schedule != null) schedule.cancel();
        if (method != null) method.cancel();
        view.beforeUnload();
    }

    /** Fires a cancel event in the bus. */
    public void cancel(@NotNull final SourceWidget source) {
        bus.fireEvent(new CancelEvent(model, source));
    }

    /** Cancels any on_schedule timer that might be running. */
    public void cancelScheduleIfAny() {
        if (schedule != null && schedule.isRunning()) schedule.cancel();
    }

    /** Check for dialogs and display them right. */
    public void checkDialogs() {
        checkDialogs(view, model);
    }

    /** Check for inline subforms and attach them. */
    public void checkInlineSubforms() {
        checkInlineSubforms(view, model);
    }

    /** Fires a delete event in the bus. */
    public void delete(@NotNull final SourceWidget source) {
        if (model.isUpdate()) bus.fireEvent(new DeleteEvent(model, source));
    }

    /** Fires a deprecate event in the bus. */
    public void deprecate(boolean deprecate) {
        bus.fireEvent(new DeprecateEvent(model, deprecate));
    }

    /** Initialize controller. */
    public void init() {
        ViewCreator.createView(form, view, model.isUpdate());
        EventHandlerVisitor.create(this, model).visit(view);
        expressions().widgets();
        expressions().uiLoad();
        checkInlineSubforms();
    }

    /** Updates the model and view of the FormController. */
    public void modelUpdated(final SyncFormResponse sync) {
        expressions().uiSync(() -> sync.syncModel(form, model));
        expressions().widgets();

        afterModelSync();

        checkInlineSubforms();

        checkDialogs();
    }

    /** Registers sync listener, returns a Registration that can be used to remove the listener. */
    public Registration registerSyncListener() {
        final ChangeListener evaluator = expressions().uiSync();
        model.addValueChangeListener(evaluator);
        return () -> model.removeValueChangeListener(evaluator);
    }

    /** Validates row and fires a change row event in the bus. */
    public void rowChange(final MultipleUI multiple, RowModel row, int i) {
        row.markInitElements();
        final MultipleWidget   widget = multiple.getMultipleModel();
        final ValidationResult result = expressions().validateRow(widget, row, i);

        if (result.isValid()) {
            final ModelUI       container = multiple.container();
            final IndexedWidget indexed   = container.indexed(widget, of(i));
            bus.fireEvent(new OnRowChangeEvent(model, indexed.toSourceWidget()));
        }
        else view.focusFirstError(first(result));
    }

    /** Validates and fires a submit event in the bus. Returns true if it was valid and submit. */
    public boolean save(@NotNull final SourceWidget source) {
        return submit(notNull(source.getPath(), SAVE), source);
    }

    /** Schedule asynchronous method invocation. */
    public void scheduleMethodInvocation(@NotNull final InvokeData m) {
        if (method != null) method.cancel();
        method = new Timer() {
                @Override public void run() {
                    bus.fireEvent(new MethodInvocationSyncEvent(model, m.getFunction()));
                }
            };

        if (m.getDelay() > 0) method.schedule(m.getDelay() * THOUSAND);
        else method.run();
    }

    /** Invoked when given subform model changed. */
    public void subformChanged(@NotNull SubformUI s) {
        expressions().uiSubformUpdate(s);
    }

    /**
     * Validates and fires a given event in the bus. Returns true if it was valid. It may receive a
     * group to validate instead of the entire FormBox.
     */
    public boolean submit(@NotNull final FormModelEvent<?> event, @NotNull Option<Widget> groupWidget) {
        final ValidationResult result = validate(groupWidget);
        if (result.isValid()) bus.fireEvent(event);
        return result.isValid();
    }

    /** Validates and fires a submit event in the bus. Returns true if it was valid and submit. */
    public boolean submit(@NotNull final String action, @NotNull final SourceWidget source) {
        return submit(new SubmitEvent(model, source, action), Option.empty());
    }

    /** Run expression validations. If a group passed, it will make them for that group. */
    public ValidationResult validate(@NotNull Option<Widget> groupWidget) {
        model.markInitElements();  // todo pcolunga jbucca: initializing all elements even if group is specified?
        return validateWithoutInit(groupWidget);
    }

    /** Run expression validations only. If a group passed, it will make them for that group. */
    public ValidationResult validateOnly(@NotNull Option<Widget> groupWidget) {
        return expressions().validate(groupWidget);
    }

    /**
     * Run expression validations without initializing elements. If a group passed, it will make
     * them for that group.
     */
    public ValidationResult validateWithoutInit(@NotNull Option<Widget> groupWidget) {
        final ValidationResult result = validateOnly(groupWidget);
        if (!result.isValid()) view.focusFirstError(first(result));
        return result;
    }

    /** Return controller bus. */
    public EventBus getBus() {
        return bus;
    }

    /** Get the form meta model label. */
    public String getFormLabel() {
        return form.getLabel();
    }

    /** Get the form meta model full name. */
    public String getFormName() {
        return form.getFullName();
    }

    /** Returns the Form Model. */
    public FormModel getModel() {
        return model;
    }

    /** Returns the query parameters. */
    @Nullable public String getParameters() {
        return parameters;
    }

    /** Returns the Pk. */
    @Nullable public String getPk() {
        return pk;
    }

    /** Get the view FormUI. */
    public FormUI getView() {
        return view;
    }

    ExpressionEvaluatorFactory expressions() {
        return factory;
    }

    /** Get controller focus listener. */
    UiFocusListener getFocusListener() {
        return focusListener;
    }

    private void attachInlineSubform(Model context, InlineSubformUI subformUI) {
        final FormModel subformModel = context.getSubform(subformUI.getModel());
        final String    subformPath  = context.getPath() + "/" + subformUI.getModel().getName();

        if (subformModel != null) {
            subformUI.getInlineAnchor().clear();
            subformModel.setPath(subformPath);

            final BodyOnlyFormBox box = new BodyOnlyFormBox(context, subformUI.getModel(), this);
            subformUI.setSubformModel(subformModel);
            box.getController().registerSyncListener();
            box.attach(subformUI.getInlineAnchor());
            box.register();

            getInstance().registerSubformView(subformModel.getPath(), box.getController().getView());
        }
        else logger.warning(MSGS.initSubformsOnLoad(subformPath));
    }

    private void checkDialogs(@NotNull final Iterable<WidgetUI> widgets, @NotNull final Model context) {
        for (final WidgetUI widgetUI : widgets)
            if (widgetUI instanceof DialogGroupUI) ((DialogGroupUI) widgetUI).setValue(context.get(widgetUI.getModel()));
    }

    private void checkInlineSubforms(@NotNull final Iterable<WidgetUI> widgets, @NotNull final Model context) {
        for (final WidgetUI widgetUI : widgets) {
            if (widgetUI instanceof InlineSubformUI) attachInlineSubform(context, (InlineSubformUI) widgetUI);
            else if (widgetUI instanceof ContainerUI && !(widgetUI instanceof WidgetDefUI)) checkInlineSubforms((ContainerUI) widgetUI, context);
            else if (widgetUI instanceof SectionUI) {
                final SectionUI     section  = (SectionUI) widgetUI;
                final MultipleModel multiple = context.getMultiple(section.getMultipleModel());
                for (int i = 0; i < min(multiple.size(), section.getSectionsCount()); i++)
                    checkInlineSubforms(section.getSection(i), multiple.getRow(section.mapSectionToItem(i)));
            }
        }
    }  // end method checkInlineSubforms

    private void scheduleSyncInterval() {
        if (isNotEmpty(form.getOnScheduleMethodName())) {
            schedule = new Timer() {
                    @Override public void run() {
                        bus.fireEvent(new OnScheduleSyncEvent(model));
                    }
                };
            schedule.scheduleRepeating(form.getOnScheduleInterval() * THOUSAND);
        }
    }

    //~ Static Fields ................................................................................................................................

    public static final Logger logger = Logger.getLogger(FormController.class);

    public static final int THOUSAND = 1000;
}  // end class FormController
