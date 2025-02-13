
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.dependency;

import java.util.*;

import tekgenesis.common.core.Tuple;
import tekgenesis.expr.Expression;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetType;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.*;
import static tekgenesis.common.core.Constants.DEPENDANTS;
import static tekgenesis.common.core.Constants.ROW;
import static tekgenesis.common.core.QName.extractQualification;
import static tekgenesis.common.core.QName.isQualified;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.metadata.form.widget.WidgetTypes.isMultiple;

/**
 * Analyzes the precedence of the references in the expressions.
 */
public class PrecedenceAnalyzer {

    //~ Instance Fields ..............................................................................................................................

    private final DependencyScope dependsOnScope;

    private final DependencyScope globalScope;
    private final UiModel         model;
    private final DependencyScope modelScope;

    //~ Constructors .................................................................................................................................

    /** Creates the precedence Analyzer. */
    public PrecedenceAnalyzer(UiModel model) {
        this.model = model;

        globalScope    = new DependencyScope();
        modelScope     = new DependencyScope();
        dependsOnScope = new DependencyScope();

        createGraph();
    }

    //~ Methods ......................................................................................................................................

    /** Perform precedence analysis. */
    public PrecedenceData analyze() {
        cycleDetection(modelScope);
        cycleDetection(dependsOnScope);

        return calculate();
    }

    private void addData(PrecedenceData data, String widgetId, Iterable<String> recompute) {
        // Iterable to gwt serializable collection (ArrayList)
        if (!isEmpty(recompute)) data.addRecomputeData(widgetId, into(recompute, new ArrayList<>()));
    }

    private PrecedenceData calculate() {
        final PrecedenceData result = new PrecedenceData();

        final Set<String> analyzed = new HashSet<>();

        // Add data for partial form recomputing
        for (final Widget widget : model.getDescendants()) {
            if ((widget.hasValue() && widget.getWidgetType() != WidgetType.SUBFORM) || isMultiple(widget.getWidgetType())) {
                final String descendant = widget.getName();
                analyzed.add(descendant);
                addData(result, descendant, globalScope.topologicalSort(descendant, false));
            }
        }

        // Add data for qualified references
        for (final String node : globalScope.nodes()) {
            if (analyzed.add(node) && isQualified(node)) addData(result, node, globalScope.topologicalSort(node, false));
        }

        // Add data for initial (massive) form computing
        addData(result, model.getName(), globalScope.topologicalSort());

        // Add data for subforms update (any expression referencing subforms by dot notation)
        for (final Widget widget : model.getDescendants()) {
            if (widget.getWidgetType() == WidgetType.SUBFORM) {
                final String subformId = widget.getName();
                addData(result, subformId, filterQualifiedWith(globalScope.topologicalSort(subformId), subformId));
            }
        }

        // Add data for table row recomputing (and cardinality changes affecting out of table related widgets!)
        for (final Widget widget : model.getDescendants()) {
            final String widgetId = widget.getName();
            if (isMultiple(widget.getWidgetType())) {
                final MultipleWidget multiple = (MultipleWidget) widget;
                addData(result, widgetId + ROW, globalScope.topologicalSort(seq(multiple.getTableElements()).map(Widget::getName)));
            }
            addData(result, widgetId + DEPENDANTS, dependsOnScope.topologicalSort(widgetId, false).drop(1));
        }

        return result;
    }  // end method calculate

    private void createGraph() {
        for (final Widget widget : model.getDescendants()) {
            final Tuple<Collection<String>, Collection<String>> widgetDependencies = findWidgetDependencies(widget);

            globalScope.addDependencies(widget, widgetDependencies.first());
            if (widget.getMultiple().isPresent()) globalScope.addDependencies(widget.getMultiple().get(), widgetDependencies.second());

            modelScope.addDependencies(widget, findWidgetModelDependencies(widget));
            if (isNotEmpty(widget.getDependsOn())) dependsOnScope.addDependencies(widget, set(widget.getDependsOn()));
        }

        globalScope.initGraph();
        modelScope.initGraph();
        dependsOnScope.initGraph();
    }  // end method createGraph

    private void cycleDetection(DependencyScope scope) {
        try {
            for (final Widget widget : model.getDescendants()) {
                if ((widget.hasValue() && widget.getWidgetType() != WidgetType.SUBFORM) || isMultiple(widget.getWidgetType()))
                    scope.topologicalSort(widget.getName(), true);
            }
        }
        catch (final TopologicalSorter.CycleDetectedException e) {
            throw scope.createCyclicException(e);
        }
    }

    private Iterable<String> filterQualifiedWith(Iterable<String> references, final String subform) {
        return filter(references, s -> s != null && !subform.equals(extractQualification(s)));
    }

    //~ Methods ......................................................................................................................................

    /** Creates the list of ref dependencies for the given widget. */
    private static Tuple<Collection<String>, Collection<String>> findWidgetDependencies(Widget widget) {
        final Collection<String> widgetRefs = new TreeSet<>();
        widget.getExpressionList().forEach(e -> widgetRefs.addAll(e.retrieveReferences()));

        final Collection<String> multipleRefs = new TreeSet<>();
        if (widget.getMultiple().isPresent()) {
            final Expression labelExpression = widget.getLabelExpression();
            if (!labelExpression.isEmpty()) multipleRefs.addAll(labelExpression.retrieveReferences());

            final Expression hideColumnExpression = widget.getHideColumnExpression();
            if (hideColumnExpression != Expression.FALSE) multipleRefs.addAll(hideColumnExpression.retrieveReferences());
        }

        return tuple(widgetRefs, multipleRefs);
    }

    /** Creates the list of model ref dependencies for the given widget. */
    private static Collection<String> findWidgetModelDependencies(Widget widget) {
        final Collection<String> refs = new TreeSet<>();

        final Expression isExpression = widget.getIsExpression();
        if (!isExpression.isNull()) refs.addAll(isExpression.retrieveReferences());

        final Expression defaultExpression = widget.getDefaultValueExpression();
        if (!defaultExpression.isNull()) refs.addAll(defaultExpression.retrieveReferences());

        return refs;
    }
}  // end class PrecedenceAnalyzer
