
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.controller;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.code.Evaluator;
import tekgenesis.common.core.Option;
import tekgenesis.expr.Expression;
import tekgenesis.metadata.form.IndexedWidget;
import tekgenesis.metadata.form.LocalWidget;
import tekgenesis.metadata.form.SourceWidget;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.model.MultipleModel;
import tekgenesis.metadata.form.model.RowModel;
import tekgenesis.metadata.form.model.UiModelBase;
import tekgenesis.metadata.form.model.WidgetDefModel;
import tekgenesis.metadata.form.widget.ButtonType;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.model.KeyMap;
import tekgenesis.view.client.Application;
import tekgenesis.view.client.RootInputHandler;
import tekgenesis.view.client.event.FormModelEvent;
import tekgenesis.view.client.event.LazyFetchSyncEvent;
import tekgenesis.view.client.event.OnBlurSyncEvent;
import tekgenesis.view.client.event.OnClickSyncEvent;
import tekgenesis.view.client.event.OnLoadFormSyncEvent;
import tekgenesis.view.client.event.OnNewLocationSyncEvent;
import tekgenesis.view.client.event.OnRowSelectedEvent;
import tekgenesis.view.client.event.OnSuggestNewSyncEvent;
import tekgenesis.view.client.event.SubformEvent;
import tekgenesis.view.client.suggest.ItemSuggestOracle;
import tekgenesis.view.client.suggest.ItemSuggestion;
import tekgenesis.view.client.suggest.MailSuggestOracle;
import tekgenesis.view.client.suggest.TekSuggestOracle;
import tekgenesis.view.client.ui.*;
import tekgenesis.view.client.ui.multiple.FilterLens;
import tekgenesis.view.client.ui.multiple.ItemEvent;
import tekgenesis.view.client.ui.multiple.MultipleModelLens.ModelLens;
import tekgenesis.view.client.ui.multiple.RangeLens;
import tekgenesis.view.client.ui.multiple.SorterLens;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.core.Option.of;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.metadata.form.exprs.Expressions.evaluateBoolean;
import static tekgenesis.metadata.form.model.Model.resolveModel;
import static tekgenesis.metadata.form.widget.ButtonType.ADD_ROW;
import static tekgenesis.metadata.form.widget.ButtonType.REMOVE_ROW;
import static tekgenesis.metadata.form.widget.WidgetType.SEARCH_BOX;
import static tekgenesis.type.permission.PredefinedPermission.HANDLE_DEPRECATED;
import static tekgenesis.view.client.event.LoadFormEvent.loadForm;
import static tekgenesis.view.client.ui.multiple.ItemEvent.ItemEventType.ITEM_CREATED;
import static tekgenesis.view.client.ui.multiple.ItemEvent.ItemEventType.ITEM_DELETED;
import static tekgenesis.view.client.ui.multiple.LensEvents.REFRESH;
import static tekgenesis.view.client.ui.multiple.SorterLens.reactToStoredSort;

/**
 * Attach event listeners to the view.
 */
public class EventHandlerVisitor implements ModelUiVisitor {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final FormController controller;
    @NotNull private final UiModelBase<?> local;

    //~ Constructors .................................................................................................................................

    private EventHandlerVisitor(@NotNull final FormController controller, @NotNull final UiModelBase<?> local) {
        this.controller = controller;
        this.local      = local;
    }

    //~ Methods ......................................................................................................................................

    @Override public void visit(WidgetUI widget) {
        widget.withUiFocusListener(controller.getFocusListener());
    }

    @Override public void visit(WidgetDefUI widget) {
        final Widget w = widget.getModel();

        if (w.isRequired()) {
            final WidgetDefModel m = resolveModel(local, w, widget.getContext().getItem()).getWidgetDef(w);
            if (m != null) create(controller, m).traverse(widget);
        }
        else widget.addHandler(() -> {
            final WidgetDefModel m = resolveModel(local, w, widget.getContext().getItem()).getWidgetDef(w);  //
            if (m != null) create(controller, m).traverse(widget);
        });
    }

    @Override public void visit(final HasValueUI valued) {
        valued.addChangeHandler(valueChangeEvent -> handleOnChange(valued));
    }

    @Override public void visit(final FieldWidgetUI field) {
        field.addBlurHandler(blurEvent -> {
            if (field instanceof BaseHasScalarValueUI) ((BaseHasScalarValueUI) field).resetUndo();
            final Widget model = field.getModel();
            if (isNotEmpty(model.getOnBlurMethodName()))
                fireEvent(new OnBlurSyncEvent(root(), field.toSourceWidget(), model.isAbstractInvocation()), field);
            Application.getInstance().updateLastInteraction();
        });
    }

    @Override public void visit(final ChartUI chart) {
        final MultipleWidget widget = chart.getMultipleModel();
        final MultipleModel  model  = local.getMultiple(widget);
        chart.withLens(new ModelLens(model));

        chart.addChartHandler(new ChartUI.ChartHandler.Default() {
                // Clicked
                @Override public void onChartClicked(int row, int col) {
                    super.onChartClicked(row, col);
                    final Widget        column  = widget.getWidgetByFieldSlot(col);
                    final int           item    = chart.mapSectionToItem(row);
                    final IndexedWidget indexed = chart.container().indexed(column, of(item));
                    if (hasOnClickMethod(widget)) fireEvent(new OnClickSyncEvent(root(), indexed.toSourceWidget(), false), chart);
                }
            });
    }

    @Override public void visit(final MapUI map) {
        final MultipleWidget multiple = map.getMultipleModel();
        final MultipleModel  model    = local.getMultiple(multiple);
        map.withLens(new ModelLens(model));

        map.addMapHandler(new MapUI.MapHandler.Default() {
                @Override public void onMarkerCreated(int row) {
                    super.onMarkerCreated(row);
                    for (final WidgetUI widgetUI : map.getSection(row))
                        visitChild(widgetUI);

                    controller.expressions().itemCreated(map, row);
                }

                @Override public void onSectionChange(int section, Double lat, Double lng) {
                    super.onSectionChange(section, lat, lng);

                    final RowModel row = model.getRow(section);
                    row.setByFieldSlot(MapUI.LATITUDE, lat);
                    row.setByFieldSlot(MapUI.LONGITUDE, lng);

                    if (isNotEmpty(multiple.getOnChangeMethodName())) controller.rowChange(map, row, section);
                }

                @Override public void onNewLocation(double lat, double lng) {
                    super.onNewLocation(lat, lng);

                    if (isNotEmpty(multiple.getOnNewLocationMethodName()))
                        fireEvent(new OnNewLocationSyncEvent(root(), map.toSourceWidget(), lat, lng), map);
                }
            });
    }

    @Override public void visit(final SuggestBoxUI suggest) {
        suggest.addSelectionHandler(event -> {
            final Widget   widget = suggest.getModel();
            ItemSuggestion item   = cast(event.getSelectedItem());
            if (item == null) item = new ItemSuggestion();

            final FormModel controllerModel = root();
            final String    formFullName    = controllerModel.getFormFullName();
            if (widget.getWidgetType() == SEARCH_BOX) {
                if (isNotEmpty(item.getKey())) fireEvent(loadForm(formFullName, item.getKey()), suggest);
            }
            else {
                suggest.setItem(item);

                final Model model = resolveModel(local, widget, suggest.getContext().getItem());

                if (!widget.getType().isEnum() && item.getKey() != null) {
                    final KeyMap options = KeyMap.create();
                    options.putAll(model.getOptions(widget));
                    options.put(item.getKey(), item.getReplacementString());
                    model.setOptions(widget, options);
                }

                handleOnChange(suggest);
            }
        });
        suggest.setBoundModel(root().metadata().getBinding());
    }  // end method visit

    @Override public void visit(final SuggestUI suggest) {
        final FormModel model = root();
        final Widget    sm    = suggest.getModel();

        if (isNotEmpty(sm.getOnSuggestMethodName()) || isNotEmpty(sm.getOnSuggestSyncMethodName())) {
            final TekSuggestOracle suggestOracle = (TekSuggestOracle) suggest.getSuggestOracle();
            suggestOracle.setFormFqn(model.getFormFullName());
            if (suggestOracle instanceof ItemSuggestOracle) ((ItemSuggestOracle) suggestOracle).setFormModel(model);
            else if (suggestOracle instanceof MailSuggestOracle) ((MailSuggestOracle) suggestOracle).setFormModel(model);
        }

        suggest.setHandleDeprecated(local.hasPermission(HANDLE_DEPRECATED.getName()));

        if (suggest.hasOnNewMethod()) suggest.addCreateNewHandler(event ->
                fireEvent(new OnSuggestNewSyncEvent(model, suggest.toSourceWidget(), event.getValue()), (WidgetUI) suggest));
    }

    @Override public void visit(final MailFieldUI mailField) {
        mailField.addSelectionHandler(event -> doOnSelection(mailField, null));
    }

    @Override public void visit(final DynamicUI dynamic) {
        dynamic.addSelectionHandler(event -> {
            for (final MailFieldUI ui : dynamic.asMailField())
                doOnSelection(ui, dynamic);
        });
    }

    @Override public void visit(final TableUI table) {
        final MultipleWidget widget = table.getMultipleModel();
        final MultipleModel  model  = local.getMultiple(widget);

        table.withLens(new ModelLens(model));

        if (table.getModel().isSortable()) table.withLens(SorterLens.create(controller.getFormName()));

        if (table.getModel().isFilterable()) table.withLens(FilterLens.create());

        if (widget.isLazy() || widget.isLazyFetch() || widget.getVisibleRows() > 0) lazyPager(table, widget, model);

        table.setRowHandler(new TableUI.TableRowHandler() {
                /** On row creation attach event handlers. */
                @Override public void onRowCreated(int row) {
                    traverse(table.getSection(row));
                }

                /** On item creation, create a RowModel and populate. */
                @Override public int onItemCreated() {
                    final int slot = model.size();

                    final RowModel row = model.addRow();

                    table.react(new ItemEvent(ITEM_CREATED, slot));

                    // Compute model and ui expressions of the new row and it's dependencies (added in the last place).
                    controller.expressions().itemCreated(table, row.getRowIndex());

                    return slot;
                }

                /** On item deletion, remove RowModel. */
                @Override public void onItemDeleted(final int item) {
                    model.removeRow(item);

                    table.react(new ItemEvent(ITEM_DELETED, item));

                    // Compute model and ui expressions of the deleted row dependencies.
                    controller.expressions().itemDeleted(table);
                }  // end method onItemDeleted

                @Override public void onRowClicked(int row) {
                    if (hasOnClickMethod(model.getMultipleWidget())) {
                        final int           item    = table.mapSectionToItem(row);
                        final IndexedWidget indexed = table.container().indexed(table.getModel(), of(item));
                        fireEvent(new OnClickSyncEvent(root(), indexed.toSourceWidget(), false), table);
                    }
                }

                @Override public void onRowSelected(Option<Integer> row) {
                    final String onSelectionMethodName = widget.getOnSelectionMethodName();
                    if (isNotEmpty(onSelectionMethodName)) {
                        final Expression expression = widget.getOnSelectionExpression();
                        if (expression != Expression.FALSE) {
                            final Boolean exprEvaluation = evaluateBoolean(new Evaluator(), local, expression);
                            if (!exprEvaluation) return;
                        }

                        final Option<Integer> item    = row.map(table::mapSectionToItem);
                        final IndexedWidget   indexed = table.container().indexed(table.getModel(), item);

                        local.updateRow(new LocalWidget(table.getModel().getName(), item.getOrNull()));

                        fireEvent(new OnRowSelectedEvent(root(), indexed.toSourceWidget()), table);
                    }
                }

                /** On item focus leave. */
                @Override public void onItemFocusLeave(int item) {
                    if (item < model.size()) {  // check because row may have been deleted
                        if (model.getRow(item).isInit()) {
                            final RowModel row = model.getRow(item);

                            final MultipleWidget multiple = model.getMultipleWidget();
                            if (isNotEmpty(multiple.getOnChangeMethodName()) && row.isRowDirtyByUser()) controller.rowChange(table, row, item);
                        }
                    }
                }

                @Override public void onRowMoved(int fromRow, int toRow) {
                    if (fromRow > toRow) {
                        // moving row up
                        for (int i = fromRow; i > toRow; i--)
                            model.swap(i, i - 1);
                    }
                    else {
                        // moving row down
                        for (int i = fromRow; i < (toRow - 1); i++)
                            model.swap(i, i + 1);
                    }
                }
            });

        table.addLensRefreshHandler(event -> {
            // Compute the UI expressions of the table
            controller.expressions().rangeChanged(table);
        });

        if (table.getModel().isSortable()) reactToStoredSort(controller.getFormName(), table, model.getMultipleWidget());

        table.react(REFRESH);

        table.getPager().ifPresent(p ->
                table.addRangeChangeHandler(event -> {
                    // Update page in the model
                    model.setCurrentPage(table.getPage());
                    controller.expressions().rangeChanged(table);
                }));
    }  // end method visit

    @Override public void visit(final SectionUI section) {
        final MultipleModel model = local.getMultiple(section.getMultipleModel());
        section.withLens(new ModelLens(model));

        section.setSectionHandler(new SectionUI.SectionHandler.Default() {
                /** On section creation attach event handlers. */
                @Override public void onSectionCreated(final int item) {
                    super.onSectionCreated(item);
                    for (final WidgetUI widgetUI : section.getSection(item))
                        visitChild(widgetUI);
                }
            });

        section.react(REFRESH);
    }

    @Override public void visit(final HasLinkUI link) {
        final WidgetUI ui    = (WidgetUI) link;
        final Widget   model = ui.getModel();

        if (hasOnClickMethod(model)) link.addClickHandler(event -> {
            if (isDisabled(ui)) return;
            ui.disableElement(true);

            // prevent href (href is only to open in new tab)
            // it might be null in some weird cases, when delaying clicks for confirmation.
            if (event != null) event.preventDefault();

            fireEvent(new OnClickSyncEvent(root(), ui.toSourceWidget(), model.isFeedback(), model.isAbstractInvocation()), ui);
        });
        else if (model.getLinkForm().isNotEmpty()) link.addClickHandler(event -> {
            // prevent href (href is only to open in new tab)
            // it might be null in some weird cases, when delaying clicks for confirmation.
            if (event != null) event.preventDefault();

            // get the link pk
            final String pk = link.getLinkPk();

            // fire sync navigation event (goes to the server)
            fireEvent(new OnLoadFormSyncEvent(root(), ui.toSourceWidget(), pk), ui);
        });
    }

    @Override public void visit(final ButtonUI button) {
        button.addClickHandler(new ClickHandler() {
                @Override public void onClick(ClickEvent event) {
                    // Additional check for save, delete or cancel buttons (look fireEvent() for custom ones).
                    if (isDisabled(button)) return;

                    final Widget     model      = button.getModel();
                    final ButtonType buttonType = model.getButtonType();

                    final SourceWidget source = button.toSourceWidget();

                    if (hasOnClickMethod(model) && buttonType != REMOVE_ROW && buttonType != ADD_ROW) {
                        final FormModelEvent<?> e = new OnClickSyncEvent(root(), source, model.isFeedback(), model.isAbstractInvocation());

                        if (buttonType == ButtonType.VALIDATE) {
                            final String groupName = model.getButtonBoundId();
                            // if group is passed, then it will validate that group.
                            button.disableElement(
                                controller.submit(e, isNotEmpty(groupName) ? some(root().widgetByName(groupName)) : Option.empty()));
                        }
                        else {
                            button.disableElement(true);
                            fireEvent(e, button);
                        }
                    }
                    else {
                        switch (buttonType) {
                        case SAVE:
                            controller.save(source);
                            break;
                        case CANCEL:
                            controller.cancel(source);
                            break;
                        case DELETE:
                            controller.delete(source);
                            break;
                        case ADD_ROW:
                            addRow();
                            if (event != null) event.stopPropagation();
                            break;
                        case REMOVE_ROW:
                            removeRow();
                            if (event != null) event.stopPropagation();
                            break;
                        case EXPORT:
                        case VALIDATE:
                        case CUSTOM:
                            break;
                        }
                    }
                }  // end method onClick

                private void removeRow() {
                    final Widget model     = button.getModel();
                    final String tableName = model.getButtonBoundId();

                    if (isNotEmpty(tableName)) {
                        final Widget tableWidget = local.widgetByName(tableName);
                        for (final WidgetUI ui : controller.getView().finder().byWidget(tableWidget)) {
                            final TableUI tableUi = (TableUI) ui;

                            button.getContext().getRow().ifPresent(row -> {
                                tableUi.setSelectedRow(row);
                                // awful hack to let input handler deal with table deselection.
                                RootInputHandler.getInstance().setCurrent(tableUi);
                            });

                            // on_row_removed via on_click on remove_row button.
                            if (hasOnClickMethod(model)) fireOnClick(model, tableUi);
                            else tableUi.removeSelectedRow();
                        }
                    }
                }

                private void addRow() {
                    final Widget        model     = button.getModel();
                    final String        tableName = model.getButtonBoundId();
                    final Option<Model> mapping   = button.toIndexed().mapping(root());
                    if (isNotEmpty(tableName)) {
                        // todo pcolunga review for widget definitions
                        final Model         m           = mapping.orElse(root());
                        final Widget        tableWidget = m.widgetByName(tableName);
                        final IndexedWidget indexed     = button.container().indexed(tableWidget, m.section());
                        for (final WidgetUI ui : controller.getView().finder().find(indexed)) {
                            final TableUI tableUi = (TableUI) ui;
                            tableUi.addRow();

                            // awful hack to let input handler deal with table deselection.
                            RootInputHandler.getInstance().setCurrent(tableUi);

                            if (hasOnClickMethod(model)) fireOnClick(model, tableUi);
                        }
                    }
                }

                private void fireOnClick(Widget model, TableUI table) {
                    final int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1 && selectedRow < table.getSectionsCount()) {
                        button.disableElement(true);
                        final int           item    = table.mapSectionToItem(selectedRow);
                        final IndexedWidget indexed = table.container().indexed(model, of(item));
                        final SourceWidget  s       = indexed.toSourceWidget();
                        fireEvent(new OnClickSyncEvent(root(), s, model.isFeedback()), button);
                    }
                }
            });
    }  // end method visit

    @Override public void visit(final ToggleButtonUI toggle) {
        final Widget model = toggle.getModel();
        if (toggle.isDeprecate()) toggle.addClickHandler(event -> {
            if (isDisabled(toggle)) return;
            controller.deprecate(!root().isDeprecated());
        });
        else if (hasOnClickMethod(model)) toggle.addClickHandler(event ->
                fireEvent(new OnClickSyncEvent(root(), toggle.toSourceWidget(), model.isFeedback(), model.isAbstractInvocation()), toggle));
    }

    @Override public void visit(final ImageUI imageUI) {
        imageUI.addClickHandler(event -> {
            final Widget model = imageUI.getModel();
            if (hasOnClickMethod(model)) fireEvent(new OnClickSyncEvent(root(), imageUI.toSourceWidget(), model.isFeedback()), imageUI);
        });
    }

    //J-
    @Override public void visit(final AnchoredSubformUI subform) {
        subform.addClickHandler(event -> {
            // with focus in the model, we have to force subform link focus if clicked to get back to it later.
            subform.setFocusLink();
            fireEvent(SubformEvent.subformClicked(subform), subform);
            event.stopPropagation();
        });
    }
    //J+

    private void doOnSelection(MailFieldUI mailField, @Nullable DynamicUI dynamic) {
        mailField.updateModelValue();
        handleOnChange(dynamic == null ? mailField : dynamic);
    }

    /**
     * Receiving widget ui to check if the widget is disabled or not before firing event. Disabled
     * SuggestBox can still navigate
     */
    private void fireEvent(final GwtEvent<?> event, final WidgetUI ui) {
        if (ui instanceof SuggestBoxUI && !ui.getModel().getLinkForm().isEmpty()) {
            controller.getBus().fireEvent(event);
            return;
        }
        if (isDisabled(ui)) return;
        controller.getBus().fireEvent(event);
    }

    private void handleOnChange(final HasValueUI valued) {
        if (isDisabled((WidgetUI) valued)) return;
        controller.expressions().uiChanged(valued);
    }

    private boolean hasOnClickMethod(@NotNull final Widget widget) {
        return widget.hasOnClickMethod();
    }

    private void lazyPager(TableUI table, MultipleWidget widget, MultipleModel model) {
        final RangeLens rangeLens = new RangeLens(table);

        if (widget.isLazy()) table.withLazy(new LazyPopulator(table, rangeLens, widget.getVisibleRows()));
        else if (widget.isLazyFetch())
            table.withLazyFetch(
                new LazyFetchPopulator(model.size(),
                    widget.getVisibleRows(),
                    rangeLens,
                    (offset, limit) -> {
                        final IndexedWidget indexed = table.container().indexed(table);
                        rangeLens.setVisibleRange(new ItemsRange(0, offset + limit, false));
                        fireEvent(new LazyFetchSyncEvent(root(), indexed.toSourceWidget(), offset, limit), table);
                    }));
        else table.withPager(new PrevNextPager(rangeLens));

        table.withLens(rangeLens);
    }

    /** Return {@link FormModel root model}. */
    private FormModel root() {
        return local.root();
    }

    private boolean isDisabled(WidgetUI ui) {
        return ui.isDisabled() || ui.isReadOnly() || !ui.isVisible();
    }

    //~ Methods ......................................................................................................................................

    /** Creates an {@link EventHandlerVisitor}. */
    public static EventHandlerVisitor create(@NotNull final FormController c, @NotNull final UiModelBase<?> l) {
        return new EventHandlerVisitor(c, l);
    }
}  // end class EventHandlerVisitor
