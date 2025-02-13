
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.completion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.field.TypeField;
import tekgenesis.lang.mm.completion.LookupElements.BracketsAndSemicolonInsertHandler;
import tekgenesis.lang.mm.psi.PsiWidget;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.type.Type;

import static com.intellij.codeInsight.lookup.LookupElementBuilder.create;

import static tekgenesis.lang.mm.psi.PsiUtils.findParentField;
import static tekgenesis.metadata.form.widget.WidgetTypes.*;

/**
 * Extra widget options, like text_field.
 */
class WidgetCompletionProvider extends CompletionProvider<CompletionParameters> {

    //~ Methods ......................................................................................................................................

    @Override protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context,
                                            @NotNull CompletionResultSet result) {
        widgetNamesFor(getParentWidget(parameters.getPosition())).forEach(wt -> result.addElement(createWidgetLookup(wt)));
    }

    @NotNull private LookupElement createWidgetLookup(@NotNull WidgetType wt) {
        final LookupElementBuilder lookup = create(wt.toString());
        if (wt.isMultiple() || wt.isGroup()) return lookup.withInsertHandler(BracketsAndSemicolonInsertHandler.INSTANCE);
        return lookup;
    }

    /** Returns the names of possible widgets for given field. */
    private Collection<WidgetType> widgetNamesFor(@NotNull final PsiWidget widget) {
        final Type type = widget.getFieldInfoType();

        if (!type.isNull()) {
            final Option<TypeField> field         = widget.getBindingAttribute();
            final boolean           isNotMultiple = !field.isPresent() || !field.get().isMultiple();
            final List<WidgetType>  widgets       = new ArrayList<>();
            for (final WidgetType widgetType : ALL_WIDGETS) {
                if (supports(widgetType, type) && (isNotMultiple || isMultiple(widgetType))) widgets.add(widgetType);
            }
            return widgets;
        }
        return ALL_WIDGETS;
    }

    /** get parent formField. */
    @NotNull private PsiWidget getParentWidget(PsiElement element) {
        return findParentField(element).castTo(PsiWidget.class).getOrFail("Orphan widget!");
    }
}
