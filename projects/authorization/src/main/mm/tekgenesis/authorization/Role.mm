package tekgenesis.authorization schema authorization;


entity Role "Role"
    primary_key id
    described_by name
    cache
    searchable by {
        id;
        name;
    }
{
    id      : String(20);
    name    : String(25);
}

entity RolePermission
    unique application(role, domain, application)
    searchable by {
        desc;
    }
    index domain(role, domain)
{
    role           : Role;
    domain         : String(50);
    application    : String(50);
    permissions    : entity Permissions* described_by permission {
        permission     : String(50);
    };
    desc: String(150), abstract, read_only;
}


form RolesForm "Roles"
{
    header {
        message(title);
    };

    roles    : Role, table, on_change saveRole, on_load loadRoles, sortable, style "hide-4th-header" {
        edit : Boolean, internal;
        "Id"   : id, disable when edit, style "large", unique;
        "Name" : name, style "xlarge";
        horizontal, style "text-right", hide when !edit {
            assign "Assign" : button, content_style "btn-xs btn-primary", on_click assignRole;
            assignees "View assignees" : button, content_style "btn-xs btn-primary", on_click viewAssignees;
            permissions "View permissions" : button, content_style "btn-xs btn-primary", on_click viewPermissions;
        };
    };

    horizontal, style "margin-top-20" {
        addRowBottom "Add" : button(add_row, roles), style "margin-right-5";
        removeRowBottom "Remove" : button(remove_row, roles), on_click removeRole;
    };

    assignmentsDialog : dialog {
        assignmentsFor : display, style "no-label";
        assignments : table, placeholder "No assignments." {
            orgUnit "Organizational Unit" : display;
            user "User" : display;
        };
    };
}

form PermissionList
    primary_key roleId
{
    roleId : String, internal;
    permissionsList : table, style "table-condensed permissions-list", sortable {
        permission "Permission" : String(50), display;
        applicationId "Application ID" : String(200), display;
        applicationName "Application Name" : String(200), display;
    };

}

form PermissionsForm "Permissions"
{
    header { message(title); };

    role   "Role"   : Role, required, on_change updateApplications, disable when sum(dirty) > 0, style "reddish-icon",
        hint sum(dirty) > 0 ? "Modified permissions exists, please discard modifications first." : "", col 8, label_col 4;

    horizontal {
        domain "Domain" : combo_box, required, on_change updateApplications, disable when sum(dirty) > 0, style "reddish-icon",
            hint sum(dirty) > 0 ? "Modified permissions exists, please discard modifications first." : "", col 8, label_col 4;
        domainHelpLink : internal;
        domainHelp : label, icon info, link domainHelpLink, target_blank, tooltip "Domain documentation", hide when domain == null, col 1;
    };

    horizontal {
        massPermissions "Add permissions to selected" : tags_combo_box, optional, col 8, label_col 4;
        addToSelected : button, tooltip "Add to selected", icon plus, content_style "btn-small",
            on_click addToSelected, disable when sum(selected) < 1;
        removeFromSelected : button, tooltip "Remove from selected", icon times, content_style "btn-small",
            on_click removeFromSelected, disable when sum(selected) < 1;
    };


    horizontal {
        toAll : check_box, placeholder "All", style "margin-bottom-0", on_change selectAll, col 1;
        horizontal, col 2, offset_col 9,  hide when sum(dirty) < 1 {
			applyChanges "Apply all" : button, style "margin-bottom-0", content_style "btn-primary btn-sm", icon check, on_click applyChanges;
	        cancelChanges "Cancel all" : button, style "margin-bottom-0", content_style "btn-sm", icon times, on_click cancelChanges;
		};
    };

    applications : RolePermission, table, placeholder "You must choose Role and Domain." {
        helpLink : internal;
        dirty : Boolean, internal;
        applicationType : ApplicationType, internal;

        selected : Boolean;
        applicationId "Application ID" : display;
        application "Application" : display, link helpLink, target_blank;
        permissionsColumn "Permissions" : horizontal {
            permissions : tags_combo_box, disable when all, optional, on_ui_change validateSelection, col 10;
            all : check_box, placeholder "All", on_ui_change allSelected;
        };

        horizontal, hide when !dirty {
            update : button, content_style "btn-primary btn-sm", icon check, on_click updateRow;
            cancel : button, icon times, content_style "btn-sm", on_click cancelRow;
        };
    };
}

handler PermissionHandler
    on_route "/sg/permission"
{
    "/check/$fqn/$permission" : Boolean, check;
}

enum ApplicationType {
    FORM; LINK;
}

form ApplicationPermissionsForm "Application Permissions"
{
    header { message(title); };

    filter "Filter" : vertical {
        horizontal, label_col 4 {
            applicationId "Application ID" : String(100), suggest_box, content_style "input-xxlarge", on_suggest loadApplications, on_change search, change_threshold 0, optional;

            applicationHelpLink : internal;
            applicationName "Application Name" : display, hide when applicationId == null, link applicationHelpLink, target_blank;
        };

        role "Role" : Role, on_change search, change_threshold 0, optional;
    };

    results "Permissions" : table, placeholder "You must choose Application or Role." {
        userId "User ID" : display;
        userName "User" : display;

        roleId "Role ID" : display;
        roleName "Role" : display;

        permissionApplicationId : internal;
        permissionApplicationHelpLink : internal;
        permissionApplicationName "Application" : display, tooltip permissionApplicationId, link permissionApplicationHelpLink;

        permissions "Permissions" : horizontal {
            all : Boolean, internal;
            permissionTags : tags, disable, hide when all;
            permission : display, hide when !all;
        };
    };

}

form CustomPermissions "Custom Permissions"
{
    header { message(title); };

    d "Domain" : input_group {
        domain : combo_box, on_change updatePermissions, disable when all;
        all : check_box, placeholder "All", on_ui_change updatePermissions, default false;
    };

    results "Permissions" : table {
        permissionApplicationId : internal;
        permissionApplicationHelpLink : internal;
        permissionApplicationName "Application" : display, tooltip permissionApplicationId, link permissionApplicationHelpLink;

        permissions "Permissions" : String, display, multiple;
    };

    horizontal, style "margin-top-20", hide when count(permissionApplicationName) == 0 {
        button(export);
    };
}