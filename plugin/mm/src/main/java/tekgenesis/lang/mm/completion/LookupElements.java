
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.completion;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.TailType;
import com.intellij.codeInsight.completion.AddSpaceInsertHandler;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.lookup.LookupElementDecorator;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.util.PlatformIcons;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.field.FieldOption;
import tekgenesis.field.FieldOptionType;
import tekgenesis.lang.mm.MMFileType;
import tekgenesis.lang.mm.psi.PsiMetaModel;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.Kind;
import tekgenesis.type.MetaModel;
import tekgenesis.type.MetaModelKind;
import tekgenesis.type.Types;

import static java.util.EnumSet.allOf;

import static com.intellij.codeInsight.lookup.LookupElementBuilder.create;
import static com.intellij.codeInsight.lookup.TailTypeDecorator.withTail;

import static tekgenesis.common.collections.Colls.map;
import static tekgenesis.common.collections.ImmutableList.fromIterable;
import static tekgenesis.common.core.Constants.MAX_STRING;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.lang.mm.completion.MetaModelNameInsertionHandler.METAMODEL_INSERT_HANDLER;
import static tekgenesis.lang.mm.psi.PsiUtils.findPsiMetaModel;
import static tekgenesis.type.MetaModelKind.*;

/**
 * Some generic methods over Types.
 */
class LookupElements {

    //~ Constructors .................................................................................................................................

    private LookupElements() {}

    //~ Methods ......................................................................................................................................

    /** Return the {@link LookupElement} for all domains after given one. */
    static Seq<LookupElement> lookupDomains(ModelRepository repository, String qualification) {
        final String      upper    = createQName(qualification, MAX_STRING).getFullName();
        final String      lower    = createQName(qualification, "").getFullName();
        final Seq<String> packages = repository.getDomains().filter(d -> d != null && d.compareTo(upper) < 0 && d.compareTo(lower) > 0).map(s ->
                    s.substring(qualification.length() + 1));
        return packages.map(LookupElements::packageLookupElement);
    }

    /** Return the {@link LookupElement} for the basic types. */
    static Seq<LookupElement> lookupElementsForBasicTypes() {
        return basicTypes;
    }

    /** Field options to lookup elements for completion. */
    static ImmutableList<LookupElementBuilder> lookupElementsForFieldOptions(Set<FieldOption> options) {
        return fromIterable(map(options, LookupElements::lookupForFieldOption));
    }

    /** Return all the {@link LookupElement elements} for given MetaModel type. */
    static Seq<LookupElementBuilder> lookupElementsForMetaModel(Project project, Module module, ModelRepository repository, MetaModelKind kind,
                                                                String domain) {         //
        return models(repository, kind)                                                  //
               .flatMap(m -> findPsiMetaModel(project, module, repository, m.getKey()))  //
               .map(LookupElements::modelLookupElement)                                  //
               .toList();
    }

    /** Return all the {@link LookupElement elements} for given enum class. */
    static <T extends Enum<T>> List<LookupElementBuilder> lookupElementsFromEnum(Class<T> enumClass) {  //
        return map(allOf(enumClass), value -> create(value.name().toLowerCase())).toList();
    }

    /** Return all the {@link LookupElement elements} for given enum set. */
    static <T extends Enum<T>> List<LookupElementBuilder> lookupElementsFromEnumSet(EnumSet<T> set) {  //
        return map(set, value -> create(value.name().toLowerCase())).toList();
    }

    /** Return all the {@link LookupElement elements} for given String list. */
    static List<LookupElementBuilder> lookupElementsFromStrings(List<String> list) {
        return map(list, LookupElementBuilder::create).toList();
    }

    /** Field option to lookup element with corresponding insert handler. */
    @NotNull static LookupElementBuilder lookupForFieldOption(FieldOption option) {
        final LookupElementBuilder item = create(option.getId());
        final FieldOptionType      type = option.getType();
        if (type == FieldOptionType.STRING_EXPR_T) return item.withInsertHandler(QuotationInsertHandler.INSTANCE);
        if (option == FieldOption.CUSTOM_MASK) return item.withInsertHandler(QuotationInsertHandler.INSTANCE);
        if (type != FieldOptionType.BOOLEAN_T) return item.withInsertHandler(AddSpaceInsertHandler.INSTANCE);
        return item;
    }

    /** Return the {@link LookupElement} for all types (excluding Forms). */
    static Seq<LookupElement> lookupMetaModels(Project project, Module module, ModelRepository repository, EnumSet<MetaModelKind> scope,
                                               Option<String> qualification) {
        final Seq<MetaModel> models = qualification.isEmpty() ? repository.getModels() : repository.getModels(qualification.get());
        return models.filter(m -> scope.contains(m.getMetaModelKind()))
               .flatMap(m ->
                    findPsiMetaModel(project, module, repository, m.getKey()))
               .map(LookupElements::modelLookupElement);
    }

    private static Predicate<MetaModel> createPredicateForMMKind(final MetaModelKind kind) {
        return metaModel -> metaModel != null && metaModel.getMetaModelKind() == kind;
    }

    private static LookupElementBuilder modelLookupElement(@NotNull final PsiMetaModel<?> model) {
        return create(model, model.getName()).withTailText("(" + model.getDomain() + ")", true)
               .withIcon(MMFileType.getIconFor(model))
               .withInsertHandler(METAMODEL_INSERT_HANDLER);
    }

    private static Seq<MetaModel> models(ModelRepository repository, MetaModelKind kind) {
        return repository.getModels().filter(createPredicateForMMKind(kind));
    }

    private static LookupElement packageLookupElement(@NotNull final String domain) {
        return new PackageLookupItem(withTail(create(domain).withIcon(PlatformIcons.PACKAGE_ICON), TailType.DOT));
    }

    //~ Static Fields ................................................................................................................................

    private static final EnumSet<MetaModelKind> TYPES               = EnumSet.of(ENTITY, ENUM);
    private static final EnumSet<MetaModelKind> STRUCTURES          = EnumSet.of(TYPE, ENUM);
    private static final EnumSet<MetaModelKind> typeRefVariantKinds = EnumSet.of(ENTITY, ENUM, TYPE);

    public static final Predicate<MetaModel> MM_TYPE_SUGGESTION = metaModel -> metaModel != null && TYPES.contains(metaModel.getMetaModelKind());

    private static final ImmutableList<LookupElement> basicTypes = Types.basicTypes().map(type -> {
                LookupElementBuilder e = create(type.toString()).withBoldness(true);
                if (type.getKind() == Kind.DECIMAL) e = e.withInsertHandler(ParenthesesInsertHandler.WITH_PARAMETERS);
                return (LookupElement) e;
            }).toList();

    //~ Inner Classes ................................................................................................................................

    // todo make smart
    static class BracketsAndSemicolonInsertHandler implements InsertHandler<LookupElement> {
        @Override public void handleInsert(InsertionContext context, LookupElement item) {
            final Editor  editor  = context.getEditor();
            final Project project = editor.getProject();
            if (project != null) {
                EditorModificationUtil.insertStringAtCaret(editor, " {};");
                PsiDocumentManager.getInstance(project).commitDocument(editor.getDocument());
                editor.getCaretModel().moveToOffset(editor.getCaretModel().getOffset() - 4);
            }
        }

        public static final InsertHandler<LookupElement> INSTANCE = new BracketsAndSemicolonInsertHandler();
    }

    private static class PackageLookupItem extends LookupElementDecorator<LookupElement> {
        private PackageLookupItem(LookupElement delegate) {
            super(delegate);
        }

        @Override public void handleInsert(InsertionContext context) {
            super.handleInsert(context);
            AutoPopupController.getInstance(context.getProject()).scheduleAutoPopup(context.getEditor());
        }
    }

    // todo make smart
    private static class QuotationInsertHandler implements InsertHandler<LookupElement> {
        @Override public void handleInsert(InsertionContext context, LookupElement item) {
            final Editor  editor  = context.getEditor();
            final Project project = editor.getProject();
            if (project != null) {
                EditorModificationUtil.insertStringAtCaret(editor, " \"\"");
                PsiDocumentManager.getInstance(project).commitDocument(editor.getDocument());
                editor.getCaretModel().moveToOffset(editor.getCaretModel().getOffset() - 1);
            }
        }

        public static final InsertHandler<LookupElement> INSTANCE = new QuotationInsertHandler();
    }
}  // end class LookupElements
