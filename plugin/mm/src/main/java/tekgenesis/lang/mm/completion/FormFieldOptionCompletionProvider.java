
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.completion;

import java.util.HashSet;
import java.util.Set;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.field.FieldOption;
import tekgenesis.lang.mm.psi.PsiUtils;
import tekgenesis.lang.mm.psi.PsiWidget;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.metadata.form.widget.WidgetTypes;
import tekgenesis.type.Type;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.lang.mm.completion.Completions.filterSiblings;
import static tekgenesis.lang.mm.completion.LookupElements.lookupElementsForFieldOptions;

/**
 * FormField option completion provider.
 */
class FormFieldOptionCompletionProvider extends CompletionProvider<CompletionParameters> {

    //~ Methods ......................................................................................................................................

    @Override protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context,
                                            @NotNull CompletionResultSet result) {  //
        getOptionsForField(parameters.getPosition()).ifPresent(opts ->
                result.addAllElements(filterSiblings(lookupElementsForFieldOptions(opts), parameters.getPosition())));
    }

    private Set<FieldOption> getFieldOptions(@NotNull PsiWidget widget) {
        final Set<FieldOption> options;
        final WidgetType       widgetType = widget.getWidgetType();
        if (widgetType != null) options = WidgetTypes.getOptionsForWidgetType(widgetType);
        else {
            final Type type = widget.getFieldInfoType();
            if (!type.isNull()) {
                final WidgetType wt = WidgetTypes.fromType(type, false, false);
                options = WidgetTypes.getOptionsForWidgetType(wt);
            }
            else options = new HashSet<>();
        }
        return options;
    }

    private Option<Set<FieldOption>> getOptionsForField(@NotNull PsiElement completionNode) {
        final Option<PsiWidget> field = cast(PsiUtils.findParentField(completionNode));
        return field.map(this::getFieldOptions);
    }
}
