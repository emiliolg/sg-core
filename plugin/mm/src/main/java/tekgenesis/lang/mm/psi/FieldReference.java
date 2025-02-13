
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import java.util.ArrayList;
import java.util.List;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.TextRange;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetType;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.ofNullable;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.lang.mm.psi.PsiUtils.findPsiMetaModel;
import static tekgenesis.lang.mm.psi.PsiUtils.getModelRepository;

/**
 * Reference to a metamodel field.
 */
public class FieldReference extends AbstractReference {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final String field;

    @Nullable private final String         model;
    @Nullable private final FieldReference parent;

    //~ Constructors .................................................................................................................................

    FieldReference(@NotNull String model, @NotNull String field, @NotNull TextRange range, @NotNull ElementWithReferences underlying) {
        this(null, model, field, range, underlying);
    }

    FieldReference(@NotNull FieldReference parent, @NotNull String field, @NotNull TextRange range, @NotNull ElementWithReferences underlying) {
        this(parent, null, field, range, underlying);
    }

    private FieldReference(@Nullable FieldReference parent, @Nullable String model, @NotNull String field, @NotNull TextRange range,
                           @NotNull ElementWithReferences underlying) {
        super((parent != null ? parent.getCanonicalText() : model) + "." + field, range, underlying);
        this.parent = parent;
        this.model  = model;
        this.field  = field;
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Option<PsiModelField> resolveInner() {
        return resolveParentFqn().flatMap(this::getPsiMetaModel).map(m -> m.getFieldNullable(field));
    }

    @NotNull @Override public PsiModelField[] getVariants() {
        return resolveParentFqn().flatMap(this::getPsiMetaModel).map(PsiMetaModel::getFields).orElse(PsiModelField.EMPTY_ARRAY);
    }

    private PsiModelField[] filterBlank(PsiModelField[] fields) {
        final List<PsiModelField> result = new ArrayList<>(fields.length);
        for (final PsiModelField f : fields) {
            if (isNotEmpty(f.getName())) result.add(f);
        }
        return result.toArray(new PsiModelField[result.size()]);
    }

    @Nullable private String resolveFqnForParent(@Nullable PsiModelField modelField) {
        if (modelField != null) {
            if (modelField instanceof PsiWidget) {
                for (final Widget widget : ((PsiWidget) modelField).getWidget()) {
                    if (widget.getWidgetType() == WidgetType.SUBFORM) return widget.getSubformFqn();
                }
            }
        }
        return null;
    }

    @NotNull private Option<String> resolveParentFqn() {  //
        return ofNullable(parent).flatMap(p -> p.resolveInner().map(this::resolveFqnForParent)).or(() -> ofNullable(model));
    }

    private Option<PsiMetaModel<?>> getPsiMetaModel(@NotNull String fqn) {
        final Module module = PsiUtils.getModule(getElement());
        if (module == null) return empty();

        return getModelRepository(getElement())  //
               .flatMap(repository -> findPsiMetaModel(getProject(), module, repository, createQName(fqn)));
    }

    //~ Static Fields ................................................................................................................................

    public static final FieldReference[] EMPTY_ARRAY = {};
}  // end class FieldReference
