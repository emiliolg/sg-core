
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.completion;

import java.util.*;
import java.util.stream.Stream;

import com.intellij.codeInsight.completion.AddSpaceInsertHandler;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ProcessingContext;

import org.jetbrains.annotations.NotNull;

import tekgenesis.check.CheckType;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.media.Mime;
import tekgenesis.common.service.Method;
import tekgenesis.field.FieldOption;
import tekgenesis.index.QueryMode;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.metadata.form.widget.*;
import tekgenesis.type.Modifier;
import tekgenesis.type.permission.PredefinedPermission;

import static java.util.stream.Collectors.toList;

import static com.intellij.codeInsight.lookup.LookupElementBuilder.create;

import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.of;
import static tekgenesis.lang.mm.MMElementType.*;
import static tekgenesis.lang.mm.completion.LookupElements.*;

/**
 * Completion variables holder.
 */
public class Completions {

    //~ Constructors .................................................................................................................................

    private Completions() {}

    //~ Methods ......................................................................................................................................

    /**  */
    public static List<LookupElementBuilder> forType(MMElementType type) {
        return COMPLETIONS.get(type);
    }

    static Seq<LookupElementBuilder> filterSiblings(@NotNull Iterable<LookupElementBuilder> lookups, @NotNull PsiElement position) {
        return new SiblingsFilter() {}.filter(lookups, position);
    }

    @NotNull static CompletionProvider<CompletionParameters> providerFromCompletions(MMElementType type) {
        return providerFromCompletions(type, true);
    }

    @NotNull static CompletionProvider<CompletionParameters> providerFromCompletions(@NotNull final MMElementType          type,
                                                                                     @NotNull final Option<SiblingsFilter> siblings) {
        return new CompletionProvider<CompletionParameters>() {
            @Override protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context,
                                                    @NotNull CompletionResultSet result) {
                final List<LookupElementBuilder> all = forType(type);
                siblings.ifPresentOrElse(f -> result.addAllElements(f.filter(all, parameters.getPosition())), () -> result.addAllElements(all));
            }
        };
    }

    @NotNull static CompletionProvider<CompletionParameters> providerFromCompletions(MMElementType type, boolean filterSiblings) {
        return providerFromCompletions(type, filterSiblings ? SIBLINGS_FILTER : empty());
    }

    private static <T extends Enum<T>> void enumCompletions(MMElementType type, Class<T> enumClass) {
        put(type, lookupElementsFromEnum(enumClass));
    }

    private static <T extends Enum<T>> void enumCompletions(MMElementType type, EnumSet<T> set) {
        put(type, lookupElementsFromEnumSet(set));
    }

    private static void fieldOptionCompletions(MMElementType type, FieldOption first, FieldOption... options) {
        put(type, lookupElementsForFieldOptions(EnumSet.of(first, options)));
    }

    private static void modelCompletions(MMElementType type, IElementType... options) {
        final LinkedList<LookupElementBuilder> elements = new LinkedList<>();
        for (final IElementType option : options)
            elements.add(create(String.valueOf(option)).withInsertHandler(AddSpaceInsertHandler.INSTANCE));
        put(type, elements);
    }

    private static void put(MMElementType type, List<LookupElementBuilder> elements) {
        COMPLETIONS.put(type, elements);
    }

    private static void stringCompletions(MMElementType type, List<String> list) {
        put(type, lookupElementsFromStrings(list));
    }

    //~ Static Fields ................................................................................................................................

    public static final Map<MMElementType, List<LookupElementBuilder>> COMPLETIONS   = new HashMap<>();
    private static final Stream<Modifier>                              taskModifiers = Stream.of(Modifier.LIFECYCLE,
            Modifier.PROCESSOR,
            Modifier.IMPORTER,
            Modifier.RUNNABLE);

    static {
        modelCompletions(ENTITY, ENTITY_OPTIONS.getTypes());
        modelCompletions(FORM, FORM_OPTIONS.getTypes());
        modelCompletions(HANDLER, HANDLER_OPTIONS.getTypes());
        modelCompletions(ENUM, ENUM_OPTIONS.getTypes());
        modelCompletions(CASE, CASE_OPTIONS.getTypes());
        modelCompletions(VIEW, VIEW_OPTIONS.getTypes());
        modelCompletions(TASK, TASK_OPTIONS.getTypes());
        modelCompletions(PACKAGE, PACKAGE);
        modelCompletions(MODEL_OPTIONS, MODELS.getTypes());
        modelCompletions(AFTER_SEARCHABLE, SEARCHABLE_OPTS.getTypes());
        modelCompletions(TRANSACTION, TRANSACTION_TYPES.getTypes());

        enumCompletions(ICON, IconType.class);
        enumCompletions(HTTP_METHOD, Method.class);
        enumCompletions(CHECK_TYPE, CheckType.class);
        enumCompletions(MASK, PredefinedMask.class);
        enumCompletions(FILE_TYPE, Mime.class);
        enumCompletions(BUTTON_TYPE, ButtonType.class);
        enumCompletions(DATE_TYPE, DateType.class);
        enumCompletions(TAB_TYPE, TabType.class);
        enumCompletions(POPOVER_TYPE, PopoverType.class);
        enumCompletions(MAP_TYPE, MapType.class);
        enumCompletions(EXPORT_TYPE, ExportType.class);
        enumCompletions(TOGGLE_BUTTON_TYPE, ToggleButtonType.class);
        enumCompletions(QUERY_MODE, QueryMode.class);
        enumCompletions(MAIL_VALIDATION_TYPE, MailValidationType.class);
        enumCompletions(RATING_TYPE, RatingType.class);
        enumCompletions(ROLE_PERMISSION, PredefinedPermission.class);

        stringCompletions(TASK_TYPE, taskModifiers.map(v -> v.name().toLowerCase() + " task ").collect(toList()));

        fieldOptionCompletions(ROUTE,
            FieldOption.METHOD,
            FieldOption.BODY,
            FieldOption.PARAMETERS,
            FieldOption.INTERNAL,
            FieldOption.SUMMARY,
            FieldOption.KEY);
        fieldOptionCompletions(SEARCHABLE, FieldOption.FILTER_ONLY, FieldOption.BOOST);
        fieldOptionCompletions(ENTITY_FIELD,
            FieldOption.ABSTRACT,
            FieldOption.COLUMN,
            FieldOption.CUSTOM_MASK,
            FieldOption.OPTIONAL,
            FieldOption.PROTECTED,
            FieldOption.READ_ONLY,
            FieldOption.SERIAL,
            FieldOption.START_WITH);
    }

    /** Default {@link SiblingsFilter}. */
    private static final Option<SiblingsFilter> SIBLINGS_FILTER = of(new SiblingsFilter() {});
}  // end class Completions
