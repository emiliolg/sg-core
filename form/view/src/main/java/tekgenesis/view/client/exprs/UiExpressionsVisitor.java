
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.exprs;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.aggregate.Aggregate;
import tekgenesis.check.CheckMsg;
import tekgenesis.code.Evaluator;
import tekgenesis.common.core.Option;
import tekgenesis.expr.Expression;
import tekgenesis.metadata.form.configuration.ChartConfig;
import tekgenesis.metadata.form.configuration.DateConfig;
import tekgenesis.metadata.form.configuration.DynamicConfig;
import tekgenesis.metadata.form.configuration.MapConfig;
import tekgenesis.metadata.form.configuration.RadioGroupConfig;
import tekgenesis.metadata.form.configuration.SubformConfig;
import tekgenesis.metadata.form.configuration.UploadConfig;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.model.MultipleModel;
import tekgenesis.metadata.form.model.RowModel;
import tekgenesis.metadata.form.model.UiModelBase;
import tekgenesis.metadata.form.model.WidgetDefModel;
import tekgenesis.metadata.form.widget.ButtonType;
import tekgenesis.metadata.form.widget.InputHandlerMetadata;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.PredefinedMask;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.type.Kind;
import tekgenesis.type.Type;
import tekgenesis.type.permission.PredefinedPermission;
import tekgenesis.view.client.formatter.InputHandler;
import tekgenesis.view.client.formatter.InputHandlerFactory;
import tekgenesis.view.client.ui.*;
import tekgenesis.view.client.ui.tablefilters.FilterUI;

import static java.lang.Math.min;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.collections.Colls.emptyList;
import static tekgenesis.common.collections.Colls.toList;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.metadata.form.exprs.Expressions.*;
import static tekgenesis.metadata.form.filter.FormFiltersClient.updateMask;
import static tekgenesis.metadata.form.model.Model.resolveModel;
import static tekgenesis.metadata.form.widget.ButtonType.DELETE;
import static tekgenesis.metadata.form.widget.ButtonType.SAVE;
import static tekgenesis.metadata.form.widget.WidgetTypes.isButtonOrLabel;
import static tekgenesis.type.permission.PredefinedPermission.CREATE;
import static tekgenesis.type.permission.PredefinedPermission.UPDATE;
import static tekgenesis.view.client.controller.ViewCreator.appendLabelAndIcon;
import static tekgenesis.view.client.exprs.UiExpressionsEvaluator.dispatchFilteringChange;
import static tekgenesis.view.client.exprs.UiExpressionsEvaluator.hide;

/**
 * Ui expressions visitor :S.
 */
@SuppressWarnings("OverlyComplexClass")
class UiExpressionsVisitor implements ModelUiVisitor {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Evaluator      evaluator;
    @NotNull private final WidgetUIFinder finder;
    @NotNull private final Model          local;
    private final boolean                 traverse;

    //~ Constructors .................................................................................................................................

    UiExpressionsVisitor(@NotNull final Evaluator evaluator, @NotNull WidgetUIFinder finder, @NotNull final Model local, boolean traverse) {
        this.finder    = finder;
        this.local     = local;
        this.traverse  = traverse;
        this.evaluator = evaluator;
    }

    //~ Methods ......................................................................................................................................

    @Override public void traverse(Iterable<WidgetUI> children) {
        // just this single child, except for explicit case
        if (traverse) ModelUiVisitor.super.traverse(children);
    }

    @Override public void visit(DynamicUI ui) {
        dynamicRenderType(ui);

        /* Update filters only if single dynamic has changed: not traversing a multiple! */
        if (!traverse) dynamicFiltering(ui);
    }

    @Override public void visit(ButtonUI button) {
        final ButtonType buttonType  = button.getModel().getButtonType();
        final Expression e           = button.getModel().getDisableExpression();
        final boolean    userDisable = e != Expression.FALSE && evaluateBoolean(evaluator, local, e);

        if (buttonType == DELETE) button.setDisabled(userDisable || !local.hasPermission(PredefinedPermission.DELETE.getName()));
        else if (buttonType == SAVE) button.setDisabled(userDisable || !local.hasPermission(local.isUpdate() ? UPDATE.getName() : CREATE.getName()));

        confirmText(button);
    }

    @Override public void visit(ToggleButtonUI toggle) {
        if (toggle.isDeprecate() && !toggle.isDisabled() && !local.hasPermission(PredefinedPermission.HANDLE_DEPRECATED.getName()))
            toggle.setDisabled(true);

        confirmText(toggle);
    }

    @Override public void visit(HasLinkUI linkUI) {
        final Widget     widget = ((WidgetUI) linkUI).getModel();
        final Expression e      = widget.getLinkExpression();
        if (!e.isEmpty()) linkUI.setLink(evaluateString(evaluator, local, e));
        else {
            final Expression pkE = widget.getLinkPkExpression();
            if (!pkE.isEmpty()) linkUI.setLinkPk(evaluateString(evaluator, local, pkE));
        }
    }  // end method visit

    /** String type expression for placeholder text value (may be null). */
    @Override public void visit(HasTextLengthUI text) {
        final Expression e = ((WidgetUI) text).getModel().getTextLengthExpression();
        if (!e.isEmpty()) {
            final Number number = evaluateNumber(evaluator, local, e);
            if (number != null) text.setTextLength((Integer) number);
        }
    }

    @Override public void visit(LabelUI labelUi) {
        confirmText(labelUi);
    }

    @Override public void visit(FieldWidgetUI field) {
        final FieldWidgetUI valuedUi = cast(field);
        final CheckMsg      checkMsg = local.getFieldMsg(field.getModel());
        if (checkMsg != null && isNotEmpty(checkMsg.getText())) valuedUi.addMessage(checkMsg);
        else valuedUi.clearMessages();
        hintText(valuedUi);
        placeholderText(valuedUi);
    }

    @Override public void visit(HasValueUI ui) {
        if (ui instanceof HasMaskUI) mask((HasMaskUI) ui);
        if (ui instanceof DynamicUI) visit((DynamicUI) ui);

        // update the display valued of the ui
        FormDataHandler.modelToView(local, ui);

        if (ui instanceof HasFromTo) {
            from((HasFromTo) ui, ui.getModel());
            to((HasFromTo) ui, ui.getModel());
        }
    }

    @Override public void visit(BaseDateUI dateUI) {
        final DateConfig config = (DateConfig) local.getFieldConfig(dateUI.getModel());
        if (config != null) dateUI.applyConfig(config, true);
    }

    @Override public void visit(SectionUI section) {
        final MultipleModel multiple = local.getMultiple(section.getMultipleModel());
        sectionRowStyleClass(section, multiple);
        placeholderText(section);
    }

    @Override public void visit(AnchoredSubformUI subform) {
        final Widget        widget = subform.getModel();
        final SubformConfig config = (SubformConfig) local.getFieldConfig(widget);
        if (config != null) subform.applyConfig(config);
        displayLinkText(subform);
    }

    @Override public void visit(DialogGroupUI dialog) {
        ModelUiVisitor.super.visit(dialog);
        displayLinkText(dialog);
    }

    @Override public void visit(TableUI table) {
        final MultipleModel multiple = local.getMultiple(table.getMultipleModel());

        if (local instanceof UiModelBase) {
            final UiModelBase<?> model = (UiModelBase<?>) local;
            aggregates(table, model.metadata());
            if (table.getModel().isFilterable()) filters(table, model);
        }

        table.setPage(multiple.getCurrentPage());

        styleClass(table);
        placeholderText(table);
        rowDisable(table, multiple);
        rowStyleClass(table, multiple);
        label(table);

        columnExpressions(table, multiple);
    }

    @Override public void visit(RadioGroupUI radio) {
        final RadioGroupConfig fieldConfig = (RadioGroupConfig) local.getFieldConfig(radio.getModel());
        radio.applyConfig(fieldConfig);
    }

    @Override public void visit(UploadUI upload) {
        final UploadConfig config = (UploadConfig) local.getFieldConfig(upload.getModel());
        if (config != null) upload.applyConfig(config);
    }

    @Override public void visit(MapUI map) {
        final MapConfig config = (MapConfig) local.getFieldConfig(map.getMultipleModel());
        if (config != null) map.applyConfigAndRedraw(config);
    }

    @Override public void visit(ChartUI chart) {
        final ChartConfig   config   = (ChartConfig) local.getFieldConfig(chart.getMultipleModel());
        final MultipleModel multiple = local.getMultiple(chart.getMultipleModel());

        if (config != null) {
            chart.applyConfig(config);
            chart.updateModel(multiple);
        }

        seriesExpressions(chart, multiple);
    }

    @Override public void visit(SuggestUI suggest) {
        filter(suggest);
        onSuggestExpression(suggest);
    }

    @Override public void visit(WidgetUI widgetUI) {
        if (widgetUI instanceof TableUI) return;  // Hack until widget.accepts(this) is implemented \\ DYNAMICS allow ui expressions

        evaluateStylingExpressions(widgetUI);

        if (widgetUI instanceof HasTooltipUI) tooltipText((HasTooltipUI) widgetUI);
        if (widgetUI instanceof HasWidthUI) widgetUI.setMaxWidth();

        // if (widgetUI instanceof HasMaskUI) mask((HasMaskUI) widgetUI, model);
    }

    @Override public void visit(WidgetDefUI widget) {
        final Widget         w = widget.getModel();
        final WidgetDefModel m = resolveModel(local, w, widget.getContext().getItem()).getWidgetDef(w);
        if (m != null) {
            appendLabelAndIcon(widget, true);
            final UiExpressionsVisitor visitor = new UiExpressionsVisitor(evaluator, widget.finder(), m, traverse);
            visitor.traverse(widget);
        }
        evaluateStylingExpressions(widget);
    }

    private void aggregates(@NotNull final TableUI table, @NotNull final UiModel metadata) {
        for (final Aggregate aggregate : table.getModel().getAggregates())
        // Update aggregate displayed value
        {
            final Number value = evaluateNumber(evaluator, local, aggregate.getExpr());

            final String ref    = aggregate.getRef();
            final Widget widget = metadata.getElement(ref);

            final InputHandler<?> handler;

            if (hasMask(widget)) handler = createInputHandler(widget);
            else {
                final Type fnType = aggregate.getFn().getType();
                final Type type   = fnType.isNumber() ? fnType : widget.getType();
                handler = InputHandlerFactory.create(widget.getInputHandler(), type, widget.isSigned());
            }

            final InputHandler<Object> objectInputHandler = cast(handler);
            final String               formatted          = objectInputHandler.format(value);
            table.updateAggregate(aggregate, formatted);
        }
    }  // end method aggregates

    private void columnExpressions(TableUI table, MultipleModel multiple) {
        for (final Widget column : multiple.getMultipleWidget()) {
            final Expression     hide       = column.getHideColumnExpression();
            final Expression     style      = column.getColumnStyleClassExpression();
            final Option<String> styleClass = style.isEmpty() ? empty() : option(evaluateString(evaluator, local, style));
            if (hide != Expression.FALSE) table.visibleAndStyle(column, !evaluateBoolean(evaluator, local, hide), styleClass);
            else if (styleClass.isPresent()) table.visibleAndStyle(column, true, styleClass);

            if (!isButtonOrLabel(column.getWidgetType())) {
                final Expression e     = column.getLabelExpression();
                final String     label = notEmpty(evaluateString(evaluator, local, e), column.getLabel());
                table.setColumnLabel(column, label);
            }
            final int width = column.getTableColumnWidth();
            if (width > 0) table.setColumnWidth(column, width);
        }
    }

    /** String type expression for confirm text value (may be null). */
    private void confirmText(final HasConfirmUI ui) {
        final Expression e = ((WidgetUI) ui).getModel().getConfirm();
        if (!e.isEmpty()) ui.setConfirmationText(evaluateString(evaluator, local, e));
    }

    @SuppressWarnings("NonJREEmulationClassesInClientCode")
    private InputHandler<?> createInputHandler(Widget widget) {
        final PredefinedMask predefinedMask = widget.getPredefinedMask();

        final List<String> customMasks = evaluateCustomMasks(widget.getCustomMaskExpression());
        return InputHandlerFactory.create(InputHandlerMetadata.mask(predefinedMask, customMasks), widget.getType(), widget.isSigned());
    }

    /** Boolean type expression for disable value. */
    private void disable(final WidgetUI ui) {
        final Widget  widget   = ui.getModel();
        final boolean readOnly = local.isReadOnly() || UiExpressionsEvaluator.isReadOnly(local, widget);

        if (readOnly) ui.setReadOnly(true);
        else {
            boolean disabled = false;

            final Expression disable = widget.getDisableExpression();
            if (disable != Expression.FALSE) disabled = evaluateBoolean(evaluator, local, disable);
            else {
                // check if its in a multiple and it has disable expression
                final Expression multDisable = widget.getMultiple().map(Widget::getDisableExpression).orElse(Expression.FALSE);
                if (multDisable != Expression.FALSE) disabled = evaluateBoolean(evaluator, local, multDisable);
            }

            final Expression dependsOn = widget.getDependsOnExpression();
            if (dependsOn != Expression.FALSE) disabled |= evaluateBoolean(evaluator, local, dependsOn);

            ui.setReadOnly(false);
            ui.setDisabled(disabled);
        }
    }  // end method disable

    /** String type expression for sub form and dialog text value (may be null). */
    private void displayLinkText(final HasDisplayLinkUI ui) {
        final Expression e = ((WidgetUI) ui).getModel().getDisplayExpression();
        if (!e.isEmpty()) ui.setLinkText(evaluateString(evaluator, local, e));
    }

    private void dynamicFiltering(WidgetUI dynamic) {
        final String filtering = dynamic.getModel().getFiltering();

        if (isNotEmpty(filtering)) dynamic.getContext().getMultiple().ifPresent(section -> {
            final RowModel current = (RowModel) local;      // Currently filter data only on rows :)

            // Update the 'current' mask.
            updateMask(current, dynamic.getModel());                                            //
            final MultipleModel results = current.getMultiple(filtering);                       //
            final MultipleModel filters = current.getMultiple(section.getMultipleModel());      //
            dispatchFilteringChange((SectionUI) section, results, filters, dynamic.getModel(), some(current), finder);});
    }

    /** Dynamic widget render type. */
    private void dynamicRenderType(final DynamicUI dynamic) {
        final DynamicConfig dynamicConfig = (DynamicConfig) local.getFieldConfig(dynamic.getModel());
        if (dynamicConfig != null) dynamic.applyConfig(dynamicConfig);
    }

    @NotNull private List<String> evaluateCustomMasks(final Expression e) {
        final Iterable<String> expressionIterable = evaluateStringList(evaluator, local, e);
        return expressionIterable == null ? emptyList() : toList(expressionIterable);
    }

    private void evaluateStylingExpressions(WidgetUI widgetUI) {
        if (traverse) hide(evaluator, local, widgetUI);  // If group is not hidden, maybe inside widgets are!

        disable(widgetUI);
        styleClass(widgetUI);
        label(widgetUI);
        iconStyle(widgetUI);
    }

    private void filter(final SuggestUI suggest) {
        final Expression e = suggest.getModel().getFilterExpression();
        if (!e.isEmpty()) suggest.setFilterExpression(evaluateAssignmentList(evaluator, local, e));
    }

    private void filters(TableUI table, UiModelBase<?> model) {
        final MultipleWidget multipleWidget = table.getMultipleModel();
        final MultipleModel  multipleModel  = model.getMultiple(multipleWidget);

        for (final Widget w : multipleWidget) {
            if (w.getType().getKind() == Kind.REFERENCE || w.getType().getKind() == Kind.ENUM) {
                for (final FilterUI.FilterWidget filter : table.getFilterForCol(w)) {
                    int i = 0;
                    for (final Object key : multipleModel.getColumn(w)) {
                        if (key != null) {
                            final String label = multipleModel.getRow(i).getOptions(w).get(key);
                            filter.addOption(key.toString(), label);
                            i++;
                        }
                    }
                }
            }
        }
    }

    private void from(HasFromTo ui, Widget w) {
        final Expression e = w.getFromExpression();
        if (!e.isNull()) {
            final Object r = evaluate(evaluator, local, e);
            if (r instanceof Double) ui.setFrom((Double) r);
            else if (r instanceof Integer) ui.setFrom(((Integer) r).doubleValue());
            else if (r instanceof Long) ui.setFrom(((Long) r).doubleValue());
        }
    }

    private boolean hasMask(Widget widget) {
        return !widget.getCustomMaskExpression().isEmpty() || widget.getPredefinedMask() != PredefinedMask.NONE;
    }

    /** String type expression for hint text value (may be null). */
    private void hintText(final FieldWidgetUI ui) {
        final Expression e = ui.getModel().getHint();
        if (!e.isEmpty()) ui.setHint(evaluateString(evaluator, local, e));
    }

    private void iconStyle(final WidgetUI ui) {
        final Expression e = ui.getModel().getIconStyleExpression();
        if (!e.isEmpty()) ui.setIcon(notNull(evaluateString(evaluator, local, e), ""));
    }

    /** String type expression for label value (may be null). */
    private void label(final WidgetUI ui) {
        final Expression e = ui.getModel().getLabelExpression();
        if (!e.isEmpty()) ui.setLabelFromExpression(notEmpty(evaluateString(evaluator, local, e), ui.getModel().getLabel()));
    }

    @SuppressWarnings("NonJREEmulationClassesInClientCode")
    private void mask(final HasMaskUI ui) {
        final Widget widget = ((WidgetUI) ui).getModel();

        if (hasMask(widget)) {
            ui.setInputHandler(createInputHandler(widget));

            String             customMask  = "";
            final List<String> customMasks = evaluateCustomMasks(widget.getCustomMaskExpression());
            if (customMasks.size() == 1) customMask = customMasks.get(0);
            String inputMask = "";
            if (!customMask.isEmpty()) inputMask = customMask.replaceAll("[^A-Za-z0-9_#]", "");

            if (!inputMask.isEmpty()) {
                ui.setLength(Math.max(inputMask.length(), customMask.length()), widget.isExpand());
                final Expression placeHoldE = widget.getPlaceholderExpression();
                if (placeHoldE.isEmpty()) ui.setPlaceholder(inputMask);  // placeholders should have higher priority than masks
            }
        }
    }

    private void onSuggestExpression(final SuggestUI suggest) {
        final Expression e = suggest.getModel().getOnSuggestExpr();
        if (!e.isNull()) suggest.setOnSuggestExpression(evaluate(evaluator, local, e));
    }

    /** String type expression for placeholder text value (may be null). */
    private void placeholderText(final HasPlaceholderUI ui) {
        final Expression e = ((WidgetUI) ui).getModel().getPlaceholderExpression();
        if (!e.isEmpty()) ui.setPlaceholder(notNull(evaluateString(evaluator, local, e), ""));
    }

    private void rowDisable(@NotNull final TableUI table, @NotNull final MultipleModel multiple) {
        final Expression disableExpression = table.getModel().getDisableExpression();
        if (disableExpression == Expression.FALSE) return;
        for (int row = 0; row < min(multiple.size(), table.getSectionsCount()); row++) {
            final RowModel m = multiple.getRow(table.mapSectionToItem(row));
            table.disableRow(row, evaluateBoolean(evaluator, m, disableExpression));
        }
    }

    @SuppressWarnings("GWTStyleCheck")
    private void rowStyleClass(@NotNull final TableUI table, @NotNull final MultipleModel multiple) {
        final Expression classExpression       = table.getModel().getRowStyleClassExpression();
        final Expression inlineStyleExpression = table.getModel().getRowStyleExpression();
        if (classExpression.isEmpty()) table.addStyleName("table-striped");
        else {
            for (int row = 0; row < min(multiple.size(), table.getSectionsCount()); row++) {
                final RowModel m = multiple.getRow(table.mapSectionToItem(row));
                table.setRowElementStyle(row, notNull(evaluateString(evaluator, m, classExpression), ""));
                if (!inlineStyleExpression.isEmpty()) table.setRowInlineStyle(row, notNull(evaluateString(evaluator, m, inlineStyleExpression), ""));
            }
        }
    }

    private void sectionRowStyleClass(SectionUI section, MultipleModel multipleModel) {
        final Expression classExpression = section.getModel().getRowStyleClassExpression();
        final Expression styleExpression = section.getModel().getRowStyleExpression();
        if (classExpression.isEmpty() && styleExpression.isEmpty()) return;

        for (int i = 0; i < min(multipleModel.size(), section.getSectionsCount()); i++) {
            final RowModel part = multipleModel.getRow(i);
            if (!classExpression.isEmpty()) section.setSectionElementStyle(i, evaluateString(evaluator, part, classExpression));
            if (!styleExpression.isEmpty()) section.setSectionInlineStyle(i, evaluateString(evaluator, part, styleExpression));
        }
    }

    private void seriesExpressions(ChartUI chart, MultipleModel multiple) {
        int i = 0;
        for (final Widget column : multiple.getMultipleWidget()) {
            final Expression hideCol = column.getHideColumnExpression();
            if (hideCol != Expression.FALSE) chart.setColumnVisibility(i, !evaluateBoolean(evaluator, local, hideCol));

            final Expression labelExpr = column.getLabelExpression();
            if (!labelExpr.isEmpty()) chart.setSeriesLabel(i, notEmpty(evaluateString(evaluator, local, labelExpr), column.getLabel()));

            i++;
        }
    }

    /** String type expression for style class value (may be null). */
    private void styleClass(final WidgetUI ui) {
        // styleClass
        final Expression styleClass = ui.getModel().getStyleClassExpression();
        if (!styleClass.isEmpty()) ui.setStyleName(notNull(evaluateString(evaluator, local, styleClass), ""));

        // contentStyle
        final Expression content = ui.getModel().getContentStyleClassExpression();
        if (!content.isEmpty()) ui.setContentStyleName(notNull(evaluateString(evaluator, local, content), ""));

        // inlineStyle
        final Expression inline = ui.getModel().getInlineStyleExpression();
        if (!inline.isEmpty()) ui.setInlineStyle(notNull(evaluateString(evaluator, local, inline), ""));
    }

    private void to(HasFromTo ui, Widget w) {
        final Expression e = w.getToExpression();
        if (!e.isNull()) {
            final Object r = evaluate(evaluator, local, e);
            if (r instanceof Double) ui.setTo((Double) r);
            else if (r instanceof Integer) ui.setTo(((Integer) r).doubleValue());
            else if (r instanceof Long) ui.setTo(((Long) r).doubleValue());
        }
    }

    /** String type expression for tooltip text value (may be null). */
    private void tooltipText(final HasTooltipUI ui) {
        final Expression e = ((WidgetUI) ui).getModel().getTooltip();
        if (!e.isEmpty()) ui.setTooltip(notNull(evaluateString(evaluator, local, e), ""));
    }
}  // end class UiExpressionsVisitor
