
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.QName;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;
import tekgenesis.form.Suggestion;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.persistence.EntitySeq;

import static tekgenesis.authorization.Applications.applicationMapFromQuery;
import static tekgenesis.authorization.AssignFavoritesBase.Field.ROLE;
import static tekgenesis.authorization.g.RoleAssignmentTable.ROLE_ASSIGNMENT;
import static tekgenesis.authorization.shiro.AuthorizationUtils.getModelRepository;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.core.Constants.HELP_URI;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.metadata.form.widget.UiModelLocalizer.localizer;
import static tekgenesis.metadata.link.Links.formLink;

/**
 * User class for Form: AssignFavorites
 */
public class AssignFavorites extends AssignFavoritesBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action apply() {
        final FormTable<UsersRow> users = getUsers();

        if (isNotEmpty(getApplicationId())) {
            for (final UsersRow row : users) {
                if (row.isSelect()) {
                    final User                rowUser       = row.getUser();
                    final EntitySeq<Favorite> userFavorites = rowUser.getFavorites();
                    if (!userFavorites.exists(f -> f != null && f.getLink().contains(getApplicationId()))) {
                        final Favorite favorite = Favorite.create();
                        favorite.setLink(formLink(getApplicationId()));
                        favorite.setUser(rowUser);
                        favorite.setIndex(userFavorites.size() + 1);
                        favorite.persist();
                    }
                }
            }
        }

        return actions.getDefault();
    }

    /** Invoked when suggest_box(role) value ui changes. */
    @NotNull @Override public Action roleChanged() {
        final FormTable<UsersRow> users = getUsers();
        users.clear();
        setToAll(false);

        //J-
        if (isDefined(ROLE))
            RoleAssignment.listWhere(ROLE_ASSIGNMENT.ROLE_ID.eq(getRoleKey())).forEach(assignment -> users.add().populate(assignment.getUser()));
        //J+

        return actions.getDefault();
    }

    @NotNull @Override public Action search() {
        final String applicationId = getApplicationId();
        if (applicationId != null) {
            final QName fqn = createQName(applicationId);
            getModelRepository().getModel(fqn, Form.class).ifPresent(form -> {
                setApplicationName(localizer(form).localize().getLabel());
                setApplicationHelpLink(HELP_URI + fqn);
            });
        }

        return actions.getDefault();
    }

    @NotNull @Override public Action selectAll() {
        getUsers().forEach(r -> r.setSelect(isToAll()));
        return actions.getDefault();
    }

    //~ Methods ......................................................................................................................................

    /** Invoked when the user type something on suggest_box(application) to create suggest list. */
    @NotNull public static Iterable<Suggestion> loadApplications(String query) {
        return applicationMapFromQuery(query);
    }

    //~ Inner Classes ................................................................................................................................

    public class UsersRow extends UsersRowBase {
        @Override public void populate(@NotNull User user) {
            super.populate(user);
            setUser(user);
        }
    }
}  // end class AssignFavorites
