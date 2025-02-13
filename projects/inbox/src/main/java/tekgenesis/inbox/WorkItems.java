
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.inbox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.codegen.common.MMCodeGenConstants;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.util.Reflection;
import tekgenesis.field.ModelField;
import tekgenesis.form.Action;
import tekgenesis.form.ActionsImpl;
import tekgenesis.form.FormInstance;
import tekgenesis.form.NavigateImpl;
import tekgenesis.metadata.authorization.OrganizationalUnit;
import tekgenesis.metadata.authorization.Role;
import tekgenesis.metadata.authorization.User;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.form.InstanceReference;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.workflow.Case;
import tekgenesis.metadata.workflow.Task;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.persistence.Select;
import tekgenesis.repository.ModelRepository;
import tekgenesis.workflow.CaseInstance;
import tekgenesis.workflow.WorkItemInstance;

import static tekgenesis.codegen.common.MMCodeGenConstants.LIST_BY_ASSIGNEES;
import static tekgenesis.codegen.common.MMCodeGenConstants.WORK_ITEM_TABLE;
import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.core.Constants.META_INF_SERVICES;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.Strings.setterName;
import static tekgenesis.common.util.Reflection.findClass;
import static tekgenesis.common.util.Reflection.invokeStatic;
import static tekgenesis.common.util.Resources.readResources;
import static tekgenesis.workflow.WorkItemReferences.createWorkInstance;

/**
 * Utility class to deal with {@link WorkItemInstance work items}.
 */
public class WorkItems {

    //~ Constructors .................................................................................................................................

    private WorkItems() {}

    //~ Methods ......................................................................................................................................

    /** Close given work item reference. */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void close(@NotNull final InstanceReference workItem, @NotNull final String action)
        throws WorkItemAlreadyCloseException
    {
        final WorkItemInstance instance = cast(createWorkInstance(workItem));

        assertWorkItemIsOpen(instance);

        close(instance);
        final CaseInstance parentCase = instance.getParentCase();
        parentCase.process(instance, action);
    }

    /** Return navigation action to given instance. */
    public static Action navigate(@NotNull final WorkItemInstance<?, ?, ?, ?, ?, ?> instance)
        throws WorkItemAlreadyCloseException
    {
        assertWorkItemIsOpen(instance);

        final Case              parent    = getParentCaseModel(instance.getParentCase());
        final Task              task      = parent.getTask(instance.getTask()).get();
        final Form              form      = getForm(task.getForm().getFullName());
        final String            value     = resolveFormKey(parent, form, instance);
        final String            pk;
        final InstanceReference reference = createReferenceForInstance(instance);
        if (value == null) pk = reference.toString();
        else pk = ensureNotNull(value);
        return navigateToForm(reference, task, form, pk).leave();
    }

    /** Return all work items for given user and organization. */
    public static List<WorkItemInstance<?, ?, ?, ?, ?, ?>> getWorkItems(@NotNull final User user, @NotNull final OrganizationalUnit organization) {
        final Set<String> assignees = getCurrentUserAssignees(user, organization);

        final List<WorkItemInstance<?, ?, ?, ?, ?, ?>> result = new ArrayList<>();
        for (final String workItemClass : WorkItemsHolder.items) {
            final Select<WorkItemInstance<?, ?, ?, ?, ?, ?>> s = invokeStatic(findClass(workItemClass),
                    LIST_BY_ASSIGNEES,
                    user,
                    organization,
                    assignees);
            if (s != null) s.forEach(result::add);
        }
        return result;
    }

    private static void assertWorkItemIsOpen(WorkItemInstance<?, ?, ?, ?, ?, ?> instance)
        throws WorkItemAlreadyCloseException
    {
        if (instance.isClosed()) throw new WorkItemAlreadyCloseException(instance);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void close(WorkItemInstance<?, ?, ?, ?, ?, ?> instance) {
        Reflection.Instance.wrap(instance).invoke(setterName(MMCodeGenConstants.CLOSED), true);
        instance.persist();
    }

    private static InstanceReference createReferenceForInstance(@NotNull final EntityInstance<?, ?> instance) {
        return InstanceReference.createInstanceReference(createQName(instance.getClass()), instance.keyAsString());
    }

    private static NavigateImpl<?> navigateToForm(@NotNull final InstanceReference reference, @NotNull final Task task, @NotNull final Form form,
                                                  @NotNull final String pk) {
        final Class<FormInstance<?>> formClass = Reflection.findClass(form.getFullName());
        final NavigateImpl<?>        navigate  = cast(getActions().navigate(formClass, pk, reference));
        return navigate.withActions(task.getActions());
    }

    @Nullable private static String resolveFormKey(@NotNull final Case parent, @NotNull final Form form,
                                                   @NotNull final WorkItemInstance<?, ?, ?, ?, ?, ?> instance) {
        final QName            formBoundModel   = form.getBinding();
        final Option<DbObject> boundEntityModel = parent.getBoundEntity();
        final QName            boundEntity      = boundEntityModel.isPresent() ? boundEntityModel.get().getKey() : QName.EMPTY;

        if (formBoundModel.isEmpty()) {
            final ImmutableList<ModelField> pk = form.getPrimaryKey();
            if (pk.size() == 1) {
                final String name = pk.get(0).getName();
                if (boundEntity.getFullName().equals(form.getElement(name).getType().getImplementationClassName()))
                    return instance.getParentCase().getEntity().keyAsString();
            }
        }
        else if (formBoundModel.equals(boundEntity)) return instance.getParentCase().getEntity().keyAsString();

        return null;
    }

    private static ActionsImpl getActions() {
        return (ActionsImpl) ActionsImpl.getInstance();
    }

    private static Set<String> getCurrentUserAssignees(@NotNull final User user, @NotNull final OrganizationalUnit org) {
        final Set<String> assignees = new HashSet<>();
        assignees.add(user.asString());
        for (final Role role : user.getRolesForOrganization(org))
            assignees.add(role.asString());
        return assignees;
    }

    private static Form getForm(@NotNull final String form) {
        return getRepository().getModel(form, Form.class).get();
    }

    private static Case getParentCaseModel(@NotNull final EntityInstance<?, ?> caseInstance) {
        final Option<Case> result = getRepository().getModel(caseInstance.getClass().getName(), Case.class);
        return result.getOrFail("Case not found!");
    }

    private static ModelRepository getRepository() {
        return Context.getSingleton(ModelRepository.class);
    }

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Lazy initialization holder class idiom for static fields.
     */
    interface WorkItemsHolder {
        ImmutableList<String> items = immutable(readResources(META_INF_SERVICES + WORK_ITEM_TABLE));
    }
}  // end class WorkItems
