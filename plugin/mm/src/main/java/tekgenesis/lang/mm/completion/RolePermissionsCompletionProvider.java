
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
import java.util.List;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.lang.mm.psi.PsiForm;
import tekgenesis.lang.mm.psi.PsiRolePermission;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.type.permission.Permission;
import tekgenesis.type.permission.PredefinedPermission;

import static com.intellij.codeInsight.completion.PrioritizedLookupElement.withPriority;
import static com.intellij.codeInsight.lookup.LookupElementBuilder.create;

import static tekgenesis.common.collections.ImmutableList.fromArray;
import static tekgenesis.type.permission.PredefinedPermission.isPredefined;

/**
 * Completions for role element permissions.
 */
class RolePermissionsCompletionProvider extends CompletionProvider<CompletionParameters> {

    //~ Instance Fields ..............................................................................................................................

    private final SiblingsFilter siblings;

    //~ Constructors .................................................................................................................................

    RolePermissionsCompletionProvider() {
        siblings = createSiblingsFilter();
    }

    //~ Methods ......................................................................................................................................

    @Override protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context,
                                            @NotNull CompletionResultSet result) {
        final List<LookupElementBuilder> all = new ArrayList<>();

        for (final PredefinedPermission permission : PredefinedPermission.values()) {
            final LookupElementBuilder completion = create(permission, permission.getName()).appendTailText(" (predefined)", true);
            all.add(completion);
        }

        addCustomPermissions(parameters.getPosition(), all);

        result.addAllElements(siblings.filter(all, parameters.getPosition()).map(element -> {
                final int priority = isPredefined(((Permission) element.getObject()).getName())
                    ? 9 - ((PredefinedPermission) element.getObject()).ordinal()
                    : 10;
                return withPriority(element, priority);
            }));
    }

    private void addCustomPermissions(@NotNull final PsiElement position, @NotNull final List<LookupElementBuilder> all) {
        final PsiRolePermission parent = (PsiRolePermission) position.getParent();
        parent.resolveForm().flatMap(PsiForm::getModel).map(Form::getPermissions).ifPresent(permissions -> {
            for (final Permission permission : permissions) {
                if (!isPredefined(permission.getName())) {
                    final LookupElementBuilder completion = create(permission, permission.getName()).appendTailText(" (custom)", true).bold();
                    all.add(completion);
                }
            }
        });
    }

    @NotNull private SiblingsFilter createSiblingsFilter() {
        return new SiblingsFilter() {
            @Override public boolean allow(@NotNull PsiElement element) {
                return element instanceof PsiRolePermission;
            }

            @NotNull @Override public Seq<PsiElement> siblings(@NotNull PsiElement position) {
                return fromArray(position.getParent().getParent().getChildren());
            }
        };
    }
}  // end class RolePermissionsCompletionProvider
