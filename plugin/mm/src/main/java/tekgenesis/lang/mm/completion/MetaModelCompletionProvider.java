
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.field.FieldOption;
import tekgenesis.lang.mm.completion.SiblingsDecorator.Default;
import tekgenesis.lang.mm.psi.MMFile;
import tekgenesis.lang.mm.psi.PsiDomain;
import tekgenesis.lang.mm.psi.PsiUtils;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModelKind;

import static tekgenesis.common.collections.ImmutableList.fromArray;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.lang.mm.completion.LookupElements.lookupForFieldOption;
import static tekgenesis.lang.mm.psi.PsiUtils.getModule;

/**
 * Completions for metamodels inside element's scope.
 */
class MetaModelCompletionProvider extends CompletionProvider<CompletionParameters> {

    //~ Instance Fields ..............................................................................................................................

    private Option<FieldOption[]> addOptions;
    private SiblingsDecorator     decorator;
    private final MetaModelKind[] models;

    private SiblingsFilter siblings;

    //~ Constructors .................................................................................................................................

    MetaModelCompletionProvider(MetaModelKind... models) {
        this.models = models;
        addOptions  = empty();
        siblings    = new SiblingsFilter() {};
        decorator   = new Default();
    }

    //~ Methods ......................................................................................................................................

    public MetaModelCompletionProvider withExtraOptions(FieldOption... options) {
        addOptions = some(options);
        return this;
    }

    @Override protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context,
                                            @NotNull CompletionResultSet result) {
        final Editor          editor          = parameters.getEditor();
        final PsiElement      position        = parameters.getPosition();
        final Module          module          = getModule(position);
        final ModelRepository modelRepository = module != null ? PsiUtils.getModelRepository(module) : new ModelRepository();
        final PsiDomain       domain          = ((MMFile) position.getContainingFile()).getDomain();

        fromArray(models).forEach(model ->
                result.addAllElements(
                    siblings.filter(
                                LookupElements.lookupElementsForMetaModel(editor.getProject(),
                                    module,
                                    modelRepository,
                                    model,
                                    domain != null ? domain.getDomainName() : ""),
                                position)
                            .map(decorator)));

        addOptions.ifPresent(opts -> fromArray(opts).forEach(o -> result.addElement(lookupForFieldOption(o))));
    }

    MetaModelCompletionProvider withSiblingsDecorator(@NotNull final SiblingsDecorator d) {
        decorator = d;
        return this;
    }

    MetaModelCompletionProvider withSiblingsFilter(@NotNull final SiblingsFilter f) {
        siblings = f;
        return this;
    }
}  // end class MetaModelCompletionProvider
