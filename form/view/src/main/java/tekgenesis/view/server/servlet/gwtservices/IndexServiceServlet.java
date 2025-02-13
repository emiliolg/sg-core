
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.server.servlet.gwtservices;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Option;
import tekgenesis.common.logging.Logger;
import tekgenesis.expr.Expression;
import tekgenesis.form.ActionType;
import tekgenesis.form.ReflectedFormInstance;
import tekgenesis.form.Suggestion;
import tekgenesis.form.UiModelInstanceHandler;
import tekgenesis.index.IndexSearcher;
import tekgenesis.index.QueryMode;
import tekgenesis.index.SearchResult;
import tekgenesis.index.Searcher;
import tekgenesis.metadata.form.IndexedWidget;
import tekgenesis.metadata.form.OnSuggestParams;
import tekgenesis.metadata.form.SourceWidget;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.TableMetadata;
import tekgenesis.serializer.GwtSerializationWhiteList;
import tekgenesis.type.Kind;
import tekgenesis.type.assignment.AssignmentType;
import tekgenesis.view.shared.response.SuggestResponse;
import tekgenesis.view.shared.response.SyncFormResponse;
import tekgenesis.view.shared.service.IndexService;

import static java.lang.Math.min;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.collections.ImmutableList.empty;
import static tekgenesis.common.core.enumeration.Enumerations.valueOf;
import static tekgenesis.common.util.Reflection.findClass;
import static tekgenesis.common.util.Reflection.findFieldOrFail;
import static tekgenesis.common.util.Reflection.invokeStatic;
import static tekgenesis.common.util.Reflection.setFieldValue;
import static tekgenesis.common.util.Resources.image;
import static tekgenesis.form.FormUtils.mapSetValue;
import static tekgenesis.form.FormsImpl.createUserFormInstance;
import static tekgenesis.form.ServerUiModelRetriever.getRetriever;
import static tekgenesis.form.exprs.ServerExpressions.getUiModelInstanceHandler;
import static tekgenesis.form.exprs.ServerExpressions.invokeUserMethod;
import static tekgenesis.form.extension.FormExtensionRegistry.getLocalizedForm;
import static tekgenesis.metadata.form.IndexedWidget.createFromReference;
import static tekgenesis.persistence.EntityTable.findInstance;
import static tekgenesis.transaction.Transaction.invokeInTransaction;

/**
 * Index provider service.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public class IndexServiceServlet extends RemoteServiceServlet implements IndexService {

    //~ Methods ......................................................................................................................................

    @Override public GwtSerializationWhiteList _whiteList(final GwtSerializationWhiteList o) {
        return o;
    }

    /* req has request properties that you can use to perform a db search
     * or some other query. Then populate the suggestions up to req.getLimit() and
     * return in a SuggestOracle.Response object. */
    @Override public SuggestResponse getIndexSuggestions(SuggestOracle.Request req, List<AssignmentType> filter, QueryMode mode, String fqn) {
        final boolean  defaultSuggestions = isEmpty(req.getQuery());
        final int      limit              = min(req.getLimit(), MAX_SUGGESTIONS);
        final Searcher searcher           = getSearcher(filter, mode, fqn, limit + 1);

        final List<SearchResult> search = searcher != null ? searcher.search(req.getQuery()) : empty();

        final SuggestResponse suggestResponse = new SuggestResponse();

        final int lowerLimit = limit - PAGE_SIZE;
        final int upperLimit = min(limit, search.size());

        if (lowerLimit < upperLimit) {
            for (final SearchResult s : search.subList(lowerLimit, upperLimit)) {
                final Option<String> image            = s.getImage();
                final String         description      = s.getDescribedBy();
                final String         descriptionImage = image.isPresent() ? image(image.get()) + description : description;
                final String         toString         = s.getToString();

                suggestResponse.addSuggestion(s.getKey(), descriptionImage, toString);
            }
        }

        suggestResponse.setDefaultSuggestions(defaultSuggestions);
        suggestResponse.setHasMore(search.size() > limit && limit != MAX_SUGGESTIONS);

        return suggestResponse;
    }  // end method getIndexSuggestions

    @Override public SuggestResponse getUserSuggestions(@NotNull final String formFqn, @NotNull final SourceWidget source,
                                                        @NotNull final OnSuggestParams params) {
        return invokeInTransaction(() -> buildSuggestResponse(params, getInvocationResult(formFqn, source, params)));
    }

    @Override public SuggestResponse getUserSuggestionsSync(@NotNull final OnSuggestParams params, @NotNull final SourceWidget widget,
                                                            @NotNull final SyncFormResponse sync) {
        final Form      form  = FormServiceServlet.getLocalizedForm(sync);
        final FormModel model = sync.getModel(form);

        return invokeInTransaction(() -> buildSuggestResponse(params, execOnSuggest(model, widget, params.getQuery())));
    }

    private Option<IndexedWidget> asIndexedWidget(@NotNull final FormModel model, @NotNull final SourceWidget source) {
        return createFromReference(getRetriever(), model.metadata(), source.getPath());
    }

    @NotNull private SuggestResponse buildSuggestResponse(@NotNull OnSuggestParams params, @Nullable Object result) {
        final SuggestResponse suggestResponse = new SuggestResponse();

        processResult(suggestResponse, result);

        suggestResponse.setDefaultSuggestions("*".equals(params.getQuery()));
        suggestResponse.setHasMore(false);

        return suggestResponse;
    }

    /** Execute on suggest methods of the widget. */
    @Nullable private Object execOnSuggest(final FormModel model, final SourceWidget source, final String text) {
        final ReflectedFormInstance root = (ReflectedFormInstance) createUserFormInstance(model);

        final IndexedWidget indexed = asIndexedWidget(model, source).getOrFail("Widget '" + source.getPath() + "' not found!");

        final UiModelInstanceHandler instance = getUiModelInstanceHandler(model, root, indexed);

        // instance.getModel().updateRow(new SourceWidget(source.getWidget(), source.getItemIndex())); todo pcolunga review

        final Widget widget = indexed.widget();
        return invokeUserMethod(instance, indexed.item(), widget, widget.getOnSuggestSyncMethodName(), text);
    }

    private void processResult(SuggestResponse response, @Nullable Object result) {
        if (result instanceof Iterable) {
            for (final Object value : (Iterable<?>) result) {
                if (value instanceof EntityInstance) {
                    final EntityInstance<?, ?> entity = (EntityInstance<?, ?>) value;
                    response.addSuggestion(entity.keyAsString(), entity.describe().mkString("\t"), entity.toString());
                }
                else if (value instanceof Suggestion) {
                    final Suggestion suggestion = (Suggestion) value;
                    response.addSuggestion(suggestion.getKey(), suggestion.getDisplayString().mkString("\t"), suggestion.getReplacementString());
                }
                else response.addSuggestion(mapSetValue(value), value.toString(), value.toString());
            }
        }
        else if (result instanceof Map) {
            logger.warning("You should use Iterable<Suggestion> as return type of your on_suggest method instead of Map.");
            final Map<Object, Object> map = cast(result);
            for (final Map.Entry<Object, Object> e : map.entrySet())
                response.addSuggestion(mapSetValue(e.getKey()), e.getValue().toString(), e.getValue().toString());
        }
    }

    private Searcher searcherParams(List<AssignmentType> filter, QueryMode mode, int topHits, Searcher searcher) {
        setFieldValue(searcher, findFieldOrFail(Searcher.class, "topHits"), topHits);

        if (searcher instanceof IndexSearcher) {
            setFieldValue(searcher, findFieldOrFail(IndexSearcher.class, "mode"), mode);

            try {
                final Method method = IndexSearcher.class.getDeclaredMethod("setFilter", List.class);
                method.setAccessible(true);
                method.invoke(searcher, filter);
            }
            catch (final NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {}
        }
        return searcher;
    }

    @Nullable private Object getInvocationResult(@NotNull final String formFqn, @NotNull final SourceWidget source,
                                                 @NotNull final OnSuggestParams params) {
        final Form form = getLocalizedForm(formFqn);

        // todo pcolunga to be reviewed...
        final IndexedWidget indexed       = createFromReference(getRetriever(), form, source.getPath()).getOrFail(
                "Indexed '" + source.getPath() + "' not found!");
        final Widget        widget        = indexed.widget();
        final String        method        = widget.getOnSuggestMethodName();
        final Expression    onSuggestExpr = widget.getOnSuggestExpr();

        final List<Object> args = new ArrayList<>();
        args.add(params.getQuery());

        if (widget.getWidgetType() == WidgetType.SEARCH_BOX && widget.isBoundedToDeprecable()) args.add(params.isDeprecated());

        if (!onSuggestExpr.isNull()) {
            @Nullable final Object param;
            if (params.getArg() != null) {
                final Object expressionResult = params.getArg();

                final Kind type = onSuggestExpr.getType().getKind();
                switch (type) {
                case DATE:
                    param = DateOnly.fromMilliseconds((Long) expressionResult);
                    break;
                case DATE_TIME:
                    param = DateTime.fromMilliseconds((Long) expressionResult);
                    break;
                case REFERENCE:
                    param = findInstance(onSuggestExpr.getType().getImplementationClassName(), (String) expressionResult);
                    break;
                case ENUM:
                    final Class<ActionType> enumClass = findClass(onSuggestExpr.getType().getImplementationClassName());
                    param = valueOf(enumClass, (String) expressionResult);
                    break;
                case STRING:
                case BOOLEAN:
                    param = expressionResult;
                    break;
                default:
                    throw new UnsupportedOperationException("Type " + type + " not supported as on_suggest argument.");
                }
            }
            else param = null;
            args.add(param);
        }

        final String className = indexed.qualification().map(q -> {
                    final Widget w = q.widget();
                    return w.getWidgetDefinitionFqn();
                }).orElse(formFqn);

        return invokeStatic(findClass(className), method, args.toArray());
    }  // end method getInvocationResult

    @Nullable private Searcher getSearcher(List<AssignmentType> filter, QueryMode mode, String fqn, int topHits) {
        final Option<? extends Searcher> searcher = TableMetadata.forName(fqn).getSearcher();

        return searcher.map(s -> searcherParams(filter, mode, topHits, s)).getOrNull();
    }

    //~ Static Fields ................................................................................................................................

    private static final int MAX_SUGGESTIONS = 100;
    private static final int PAGE_SIZE       = 10;

    public static final Logger logger = Logger.getLogger(IndexServiceServlet.class);

    private static final long serialVersionUID = -4259526832630525310L;
}  // end class IndexServiceServlet
