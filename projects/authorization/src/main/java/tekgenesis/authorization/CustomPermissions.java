
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.form.Action;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.permission.Permission;

import static tekgenesis.authorization.shiro.AuthorizationUtils.getModelRepository;
import static tekgenesis.common.core.Constants.HELP_URI;
import static tekgenesis.form.FormUtils.localize;

/**
 * User class for form: CustomPermissions
 */
@SuppressWarnings("WeakerAccess")
public class CustomPermissions extends CustomPermissionsBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        final List<String> domains = getModelRepository().getDomains();
        setDomainOptions(domains);
        if (!domains.isEmpty()) setDomain(domains.get(0));
    }

    @NotNull @Override public Action updatePermissions() {
        getResults().clear();

        final ModelRepository repository = getModelRepository();

        final List<Form> fs = new ArrayList<>();

        if (isAll()) repository.getDomains().forEach(d -> fs.addAll(getApplicationPermissions(d).toList()));
        else fs.addAll(getApplicationPermissions(getDomain()).toList());

        fs.forEach(f -> getResults().add().populate(f));

        return actions.getDefault();
    }

    private Seq<Form> getApplicationPermissions(String domain) {
        return getModelRepository().getModels(domain, Form.class).filter(f -> !f.getPermissions().filter(p -> !p.isDefault()).isEmpty());
    }

    //~ Inner Classes ................................................................................................................................

    public class ResultsRow extends ResultsRowBase {
        /** Populate a results row based on a given form. */
        public void populate(@NotNull final Form f) {
            setPermissionApplicationId(f.getFullName());
            setPermissionApplicationName(localize(f).getLabel());
            setPermissionApplicationHelpLink(HELP_URI + f.getFullName());

            setPermissions(f.getPermissions().filter(p -> !p.isDefault()).map(Permission::getName));
        }
    }
}  // end class CustomPermissions
