
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.ref;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.expr.RefContextMapper;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.exception.UnmappedReferenceException;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.of;

/**
 * Type resolver for a {@link UiModel}.
 */
public class UiModelReferenceMapper extends RefContextMapper {

    //~ Instance Fields ..............................................................................................................................

    private Map<String, String>   bindings;
    private final ModelRepository repository;

    private final UiModel  root;
    private Option<String> tableEntity;

    //~ Constructors .................................................................................................................................

    /** Creates a type resolver for a {@link UiModel}. */
    public UiModelReferenceMapper(final UiModel root, final ModelRepository repository) {
        super(new UiModelRefTypeSolver(root, repository));
        this.root       = root;
        this.repository = repository;
        bindings        = null;
        tableEntity     = empty();
    }

    //~ Methods ......................................................................................................................................

    /** Map entity reference to form field. */
    @NotNull @Override public String doMap(@NotNull final String ref) {
        return tableEntity.or(this::boundEntity)                                      //
               .flatMap(fqn -> repository.getModel(fqn, DbObject.class))              //
               .flatMap(e -> e.getAttribute(ref))                                     //
               .map(attr -> getBindings().get(attr.getFullName()))                    //
               .orElseThrow(() -> new UnmappedReferenceException(ref, root.getFullName()));  //
    }

    /** Entity fqn used to find the attribute if its different from the form bounded entity. */
    public void usingTableEntity(Option<String> tableEntityFqn) {
        tableEntity = tableEntityFqn;
    }

    @NotNull private Option<String> boundEntity() {
        final String fqn = root.getBinding().getFullName();
        return isEmpty(fqn) ? empty() : of(fqn);
    }

    private Map<String, String> getBindings() {
        if (bindings == null) {
            bindings = new HashMap<>();
            for (final Widget widget : root.getDescendants()) {
                final String binding = widget.getBinding();
                if (isNotEmpty(binding)) bindings.put(binding, widget.getName());
            }
        }
        return bindings;
    }
}  // end class UiModelReferenceMapper
