
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiNamedElement;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.field.ModelField;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.lang.mm.rename.ScopeInfo;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.type.MetaModel;
import tekgenesis.type.Scope;

import static tekgenesis.common.Predefined.equal;
import static tekgenesis.common.Predefined.unreachable;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.QName.qualify;
import static tekgenesis.lang.mm.rename.ScopeInfo.createScopeInfoForMultiple;
import static tekgenesis.lang.mm.rename.ScopeInfo.createScopeInfoForRoot;

/**
 * Psi field.
 */
public abstract class PsiModelField extends PsiMetaModelMember implements PsiNamedElement {

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    PsiModelField(MMElementType t) {
        super(t);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public String getDomain() {
        return findParentMetaModel().getFullName();
    }

    /** Attempt to return repository model field. */
    @NotNull public Option<ModelField> getModelField() {
        Option<ModelField> result = Option.empty();
        for (final MetaModel model : findParentMetaModel().getModel())
            result = findModelField(model);
        return result;
    }

    @Override public ItemPresentation getPresentation() {
        return new PsiModelFieldPresentation(this);
    }

    /** Get model field scope information. */
    public ScopeInfo getScopeInfo() {
        for (final MetaModel metaModel : findParentMetaModel().getModel()) {
            final Option<Scope> modelFieldScope = findModelFieldScope(metaModel);
            if (modelFieldScope.isPresent()) {
                final Scope scope = modelFieldScope.get();
                return metaModel.equals(scope) ? createScopeInfoForRoot(metaModel.getKey())
                                               : createScopeInfoForMultiple(metaModel.getKey(), ((MultipleWidget) scope).getName());
            }
        }
        throw unreachable();
    }

    @NotNull protected String getFqn() {
        return qualify(findParentMetaModel().getFullName(), getName());
    }

    private Option<ModelField> findModelField(Scope model) {
        Option<ModelField> result = Option.empty();
        for (final ModelField field : model.getChildren()) {
            if (matches(field)) result = some(field);
            else if (field instanceof Scope) result = findModelField((Scope) field);
            if (result.isPresent()) break;
        }
        return result;
    }

    private Option<Scope> findModelFieldScope(Scope model) {
        Option<Scope> result = Option.empty();
        for (final ModelField field : model.getChildren()) {
            if (matches(field)) result = some(model);
            else if (field instanceof Scope) result = findModelFieldScope((Scope) field);
            if (result.isPresent()) break;
        }
        return result;
    }

    /** Find parent meta model or fail. */
    @NotNull private PsiMetaModel<?> findParentMetaModel() {
        return PsiUtils.findParentModel(this).getOrFail("Orphan model field! Parent meta model not found!");
    }

    private boolean matches(@NotNull final ModelField field) {
        return equal(getName(), field.getName());
    }

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("FieldNameHidesFieldInSuperclass")
    public static final PsiModelField[] EMPTY_ARRAY = new PsiModelField[0];
}  // end class PsiModelField
