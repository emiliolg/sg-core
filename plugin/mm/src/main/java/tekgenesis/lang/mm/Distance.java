
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.Tuple;
import tekgenesis.lang.mm.psi.*;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.EnumType;
import tekgenesis.type.MetaModel;
import tekgenesis.util.visitor.DomainVisitor;

import static java.util.Collections.emptyList;

/**
 * Distance class has useful utils for checking Strings, and how much they differ.
 */
public class Distance {

    //~ Constructors .................................................................................................................................

    private Distance() {}

    //~ Methods ......................................................................................................................................

    /**
     * Returns a list of Attributes,found in the modelRepository, that are similar to the text
     * passed. Useful for typos
     */
    public static List<Tuple<String, String>> findSimilar(String text, Module module) {
        final ModelRepository repository = module.getComponent(MMModuleComponent.class).getRepository();
        final DistanceVisitor dv         = new DistanceVisitor(null, text);

        return dv.visit(repository);
    }

    /**
     * Returns a list of Attributes Filtered By AlreadyExisting,found in the modelRepository, that
     * are similar to the text passed. Useful for typos
     */
    public static List<Tuple<String, String>> findSimilarFilterSiblings(MetaModelAST node, @Nullable Module module) {
        if (module == null || !(node instanceof PsiTypeReference)) return emptyList();
        final ModelRepository  repository    = module.getComponent(MMModuleComponent.class).getRepository();
        final PsiTypeReference typeReference = (PsiTypeReference) node;

        final Option<PsiMetaModel<?>> parentModel = PsiUtils.findParentModel(typeReference);
        final Option<PsiForm>         form        = typeReference.getAncestor(PsiForm.class);
        final PsiMetaModel<?>         mm          = parentModel.getOrNull();

        final DistanceVisitor dv = form.isPresent() ? new DistanceVisitor(mm, node.getText(), form.get().getWidgets())
                                                    : new DistanceVisitor(mm, node.getText());

        return dv.visit(repository);
    }

    // Levenshtein Distance
    @SuppressWarnings("MethodOnlyUsedFromInnerClass")
    private static int calculateDistance(String s, String t) {
        if (s == null || t == null) throw new IllegalArgumentException("Strings must not be null");
        final int n = s.length();  // length of s
        final int m = t.length();  // length of t

        if (n == 0) return m;
        if (m == 0) return n;

        int[] p = new int[n + 1];  // 'previous' cost array, horizontally

        // indexes into strings s and t
        int i;  // iterates through s

        for (i = 0; i <= n; i++)
            p[i] = i;

        int[] d = new int[n + 1];              // cost array, horizontally
        for (int j = 1;                        // iterates through t
             j <= m; j++)
        {
            final char t_j = t.charAt(j - 1);  // jth character of t
            d[0] = j;

            for (i = 1; i <= n; i++) {
                final int cost = s.charAt(i - 1) == t_j ? 0 : 1;  // cost
                // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
                d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
            }

            // copy current distance counts to 'previous row' distance counts
            final int[] _d = p;  // placeholder to assist in swapping p and d
            p = d;
            d = _d;
        }

        // our last action in the above loop was to switch d and p, so p now
        // actually has the most recent cost counts
        return p[n];
    }

    //~ Inner Classes ................................................................................................................................

    public static class DistanceVisitor extends DomainVisitor<List<Tuple<String, String>>> {
        private final HashMap<Integer, List<Tuple<String, String>>> map;
        private final PsiMetaModel<?>                               parentModel;
        private final LinkedList<Tuple<String, String>>             possibleChoices;
        private final Seq<PsiWidget>                                siblings;
        private final String                                        text;

        private DistanceVisitor(@Nullable PsiMetaModel<?> model, String text) {
            this(model, text, Colls.emptyIterable());
        }

        private DistanceVisitor(@Nullable PsiMetaModel<?> model, String text, Seq<PsiWidget> siblings) {
            super();
            this.text       = text;
            possibleChoices = new LinkedList<>();
            this.siblings   = siblings;
            map             = new HashMap<>();
            parentModel     = model;
        }

        @Override public List<Tuple<String, String>> visit(DbObject dbObject) {
            for (final Attribute attribute : dbObject.attributes())
                visit(attribute);
            final int distance = calculateDistance(dbObject.getName(), text);
            if (distance <= COMPARISON_STRICTNESS) {
                final Tuple<String, String> choice = Tuple.tuple(dbObject.getName(), dbObject.getFullName());
                addToMap(choice, distance);
            }
            return possibleChoices;
        }

        @Override public List<Tuple<String, String>> visit(EnumType aEnum) {
            final int distance = calculateDistance(aEnum.getName(), text);
            if (distance <= COMPARISON_STRICTNESS) {
                final Tuple<String, String> choice = Tuple.tuple(aEnum.getName(), aEnum.getFullName());
                addToMap(choice, distance);
            }
            return possibleChoices;
        }

        @Override public List<Tuple<String, String>> visit(Attribute attribute) {
            final int distance = calculateDistance(attribute.getName(), text);
            if (distance <= COMPARISON_STRICTNESS) {
                final Tuple<String, String> choice = Tuple.tuple(attribute.getName(), attribute.getFullName());
                addToMap(choice, distance);
            }
            return possibleChoices;
        }

        @Override public List<Tuple<String, String>> visit(Widget attribute) {
            return possibleChoices;
        }

        private void addToMap(Tuple<String, String> choice, int distance) {
            if (map.get(distance) != null) map.get(distance).add(choice);
            else {
                final LinkedList<Tuple<String, String>> newList = new LinkedList<>();
                newList.add(choice);
                map.put(distance, newList);
            }
        }  // end method addToMap

        private List<Tuple<String, String>> filterAlreadyExisting(ModelRepository repository) {
            if ((siblings == null || siblings.isEmpty()) && !(parentModel instanceof PsiEntity)) return possibleChoices;

            final Seq<Tuple<String, String>> filteredSeq = Colls.filter(possibleChoices, new DistPredicate(parentModel, repository, siblings));

            return filteredSeq.toList();
        }

        private List<Tuple<String, String>> visit(ModelRepository repository) {
            for (final String domain : repository.getDomains())
                visit(domain, repository);
            setOrderedList();
            return filterAlreadyExisting(repository);
        }

        private void setOrderedList() {
            for (int i = 0; i <= COMPARISON_STRICTNESS; i++) {
                if (map.get(i) != null) possibleChoices.addAll(map.get(i));
            }
        }

        private static final int COMPARISON_STRICTNESS = 2;

        private static class DistPredicate implements Predicate<Tuple<String, String>> {
            private final PsiMetaModel<?> parentModel;
            private final ModelRepository repository;
            private final Seq<PsiWidget>  siblings;

            private DistPredicate(@Nullable PsiMetaModel<?> parentModel, ModelRepository repository, @Nullable Seq<PsiWidget> siblings) {
                this.repository  = repository;
                this.siblings    = siblings == null ? Colls.emptyList() : siblings;
                this.parentModel = parentModel;
            }

            @Override public boolean test(@Nullable Tuple<String, String> choice) {
                if (choice == null) return false;

                if (parentModel instanceof PsiDatabaseObject) return repository.getModel(QName.createQName(choice.second())).isPresent();
                else {
                    final PsiWidget     widget   = siblings.getFirst().getOrNull();
                    final PsiUiModel<?> psiModel = widget == null ? null : widget.getUiModel();
                    final MetaModel     model    = repository.getModel(QName.createQName(choice.second())).getOrNull();

                    final ASTNode                          entity    = psiModel == null ? null
                                                                                        : psiModel.findChildByType(MMElementType.DATA_OBJECT_REF);
                    final PsiMetaModelCodeReferenceElement entityRef = entity == null ? null
                                                                                      : (PsiMetaModelCodeReferenceElement) entity.getLastChildNode();
                    if (entityRef != null) {
                        final PsiReference reference = entityRef.getReference();
                        if (reference != null) {
                            final PsiNamedElement resolve = (PsiNamedElement) reference.resolve();
                            if (resolve != null) {
                                if ((model == null) &&
                                    !(choice.second().split("." + choice.first())[0].equals(entityRef.getDomain() + "." + resolve.getName())))
                                    return false;
                            }
                        }
                    }
                    if (model instanceof PsiForm) return false;
                    for (final PsiWidget sibling : siblings) {
                        final MMCommonComposite binding = sibling.getBinding();
                        if (binding != null) {
                            final boolean filter = (choice.first().equals(binding.getName()));
                            if (filter) return false;
                        }
                    }
                    return true;
                }
            }  // end method test
        }  // end class DistPredicate
    }  // end class DistanceVisitor
}  // end class Distance
// end class Distance
