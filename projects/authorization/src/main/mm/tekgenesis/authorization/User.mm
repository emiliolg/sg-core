package tekgenesis.authorization schema authorization;

entity User "User"
    primary_key id
    described_by nameId
    searchable by { id; nameId; }
    deprecable
    cache
    auditable
    image picture
    unique confirmationCode
    index email
    index updateTime
{
    nameId : String(300), abstract, read_only;

    id        : String(256);
    name      : String(25);
    defaultOu : OrgUnit, optional;
    email     : String(100), optional;
    locale    : String(10), optional;
    password  : String(256), optional;
    picture   : Resource, optional;
    lastLogin : DateTime, optional;

    confirmationCode : String(50), optional; //for sign-up or recover-password
    confirmationCodeExpiration : DateTime, optional; //for sign-up or recover-password

    devices   : entity Device* {
        name      : String(25);
        deviceId  : String(256);
        disabled  : Boolean;
    };

    props : entity UserProperties* {
        property : Property;
        value       : String(512);
    };

    favorites : Favorite*;
}

entity SocialProfile
    primary_key provider, profile
    index user
{
    provider : String(30);
    profile : String(256);
    user : User;
}

entity Favorite "Favorite"
    described_by link
{
    user : User;
    link : String(150);
    index : Int;
}


entity RoleAssignment "Role Assignment"
    primary_key user, role, ou
    searchable
    index assignedTo(user, ou)
    index updateTime
{
    user    : User;
    role    : Role;
    ou      : OrgUnit;
}

entity Application "Application"
    primary_key id
    searchable by {
        id;
        token;
    }
    described_by id
    unique token
    cache
{
    id : String(256);
    token : String(512);
    user : User;
}

entity InvalidatedSession "Invalidated Session"
    primary_key sessionId
    index expired
    cache 50000
{
    sessionId : String;
    expired : DateTime;
}

entity ApplicationAudit
        primary_key application, user
{
    application: String;
    user: User;
    lastEvent: DateTime;
    yesterdayEvents: Int;
    dayEvents: Int;
    lastWeekEvents: Int;
    weekEvents: Int;
    lastMonthEvents: Int;
    monthEvents: Int;
}

form ApplicationAuditForm "Application Audit"
{

    header {
        message(title);
    };

    horizontal {
        lastUsages "Latest App Activity": horizontal {
            usages: table, style "no-thead table-condensed", content_style "borderless", on_click clickTable{
                appId: String, internal;
                pic: image, content_style "user-pic", column_style "user-pic-col";
                vertical, column_style "center-vertical"{
                    text: String, display, style "text";
                    timeAgo: DateTime, display, style "time-ago", mask time_ago;
                };
            };
        };

        topToday "Top Today Apps" : chart(bar), on_click clickToday {
            labelToday: String;
            valueToday: Int;
        };
    };

    horizontal {
        topWeek "Top Week Apps" : chart(bar), on_click clickWeek {
            labelWeek: String;
            valueWeek: Int;
        };
        topMonth "Top Month Apps" : chart(bar), on_click clickMonth{
            labelMonth: String;
            valueMonth: Int;
        };
    };

    searcher "Detailed App Audits": vertical{
        appSearcher "Select Application": String(100), suggest_box, on_suggest suggest, on_ui_change selectSuggest, expand, optional;

        applicationAudits: ApplicationAudit, table, sortable {
            user, display;
            application, display, hide_column;
            lastEvent, display;
            yesterdayEvents, display;
            dayEvents, display;
            lastWeekEvents, display;
            weekEvents, display;
            lastMonthEvents, display;
            monthEvents, display;
        };
    };
}

form UserAuditForm "User Audit"
{

    header {
        message(title);
    };

    horizontal {
        lastUsages "Latest User Activity": horizontal {
            usages: table, style "no-thead table-condensed", content_style "borderless", on_click clickTable{
                userId: String, internal;
                pic: image, content_style "user-pic", column_style "user-pic-col";
                vertical, column_style "center-vertical"{
                    text: String, display, style "text";
                    timeAgo: DateTime, display, style "time-ago", mask time_ago;
                };
            };
        };

        topToday "Top Today Users" : chart(bar), on_click clickToday {
            labelToday: String;
            valueToday: Int;
        };
    };

    horizontal {
        topWeek "Top Week Users" : chart(bar), on_click clickWeek {
            labelWeek: String;
            valueWeek: Int;
        };
        topMonth "Top Month Users" : chart(bar), on_click clickMonth{
            labelMonth: String;
            valueMonth: Int;
        };
    };

    searcher "Detailed User Audits": vertical{
        userSearcher "Select User": String(100), suggest_box, on_suggest suggest, on_ui_change selectSuggest, expand, optional;

        applicationAudits: ApplicationAudit, table, sortable {
            user, display, hide_column;
            application, display;
            lastEvent, display;
            yesterdayEvents, display;
            dayEvents, display;
            lastWeekEvents, display;
            weekEvents, display;
            lastMonthEvents, display;
            monthEvents, display;
        };
    };
}

enum Locale {
    EN : "English";
    ES : "Spanish";
    AR : "Arabic";
    BG : "Bulgarian";
    CA : "Catalan";
    ZH_CHS : "Chinese Simplified";
    ZH_CHT : "Chinese Traditional";
    CS : "Czech";
    DA : "Danish";
    NL : "Dutch";
    ET : "Estonian";
    FI : "Finnish";
    FR : "French";
    DE : "German";
    EL : "Greek";
    HT : "Haitian Creole";
    HE : "Hebrew";
    HI : "Hindi";
    MWW : "HmongDaw";
    HU : "Hungarian";
    ID : "Indonesian";
    IT : "Italian";
    JA : "Japanese";
    KO : "Korean";
    LV : "Latvian";
    LT : "Lithuanian";
    NO : "Norwegian";
    PL : "Polish";
    PT : "Portuguese";
    RO : "Romanian";
    RU : "Russian";
    SK : "Slovak";
    SL : "Slovenian";
    SV : "Swedish";
    TH : "Thai";
    TR : "Turkish";
    UK : "Ukranian";
    VI : "Vietnamese";
}

handler AuthorizationHandler on_route "/sg" unrestricted {
    "/login" : Html, login, parameters {
        username : String, optional;
    };
    "/login" : Html, login, method POST, parameters {
        username : String, optional;
    };
    "/success" : Html, success;
    "/success" : Html, success, method post;
    "/unauthorized" : Html, unauthorizedPage;
    "/password/forgot" : Html, forgot;
    "/password/reset/$confirmationCode" : Html, reset;
}

form AssignFavorites "Assign Favorites" {

    header { message(title); };

    vertical, label_col 4 {
		applicationId "Select an Application to add as Favorite" : String(100), suggest_box, on_ui_change search,
	                            content_style "input-xxlarge", on_suggest loadApplications, change_threshold 0, optional;
	    applicationHelpLink : internal;
	    applicationName "Application Name" : display, hide when applicationId == null, link applicationHelpLink, target_blank;
	    role "Role" : Role, on_ui_change roleChanged;
	};

    toAll : check_box, placeholder "All", on_ui_change selectAll;

    users "Select User(s) to add Favorite to" : User, table, placeholder "Select a role to filter." {
        user : User, internal;

        select : check_box;
        id, display;
        name, display;
    };

    horizontal, style "form-actions" {
        apply "Apply changes" : button, on_click apply, content_style "btn-primary", disable when applicationId == null;
        reset "Reset" : button, on_click roleChanged;
    };

}

form ForgotPassword "Forgot Password"
 unrestricted
{
    header { message(title); };

    showError : Boolean, internal, default false;
    showInfo : Boolean, internal, default false;
    error : message(error), hide when !showError;
    info : message(info), hide when !showInfo;

    id "Enter your email or username" : String(100), top_label, required, expand;

    horizontal, style "form-actions" {
        send "Send" : button(validate), on_click sendEmail, content_style "btn-primary";
    };
}

form ResetPassword "Reset Password"
    primary_key confirmationCode
    unrestricted
{
    header { message(title); };

    passwordScore : Int, internal, default 3;
    confirmationCode : String(50), internal;

    showError : Boolean, internal, default false;
    showInfo : Boolean, internal, default false;
    expired : Boolean, internal, default false;
    error : message(error), hide when !showError;
    info : message(info), hide when !showInfo;

    loginPage "Go to login page" : label, link "/login", hide when !expired;

    password "Password" : String, password_field, check length(trim(password)) > 0 : "Password cannot be empty.", hide when expired, expand, top_label, metering passwordScore;
    confirmPassword "Confirm password" : String, password_field, check confirmPassword == password : "Passwords mismatch", hide when expired, expand, top_label, metering passwordScore;

    horizontal, style "form-actions" {
        reset "Reset" : button(validate), on_click resetPassword, content_style "btn-primary";
    };
}

form UserForm "User"
    entity User
{
    header {
        message(title);
        search_box, style "pull-right";
    };

    edit : Boolean, internal;
    passwordScore : Int, internal, default 3;

    id   "Id"   : id, text_field, on_blur fixCase, check length(trim(id)) == length(id) : "User id cannot have leading and/or trailing spaces.";
    name "Name" : name;
    defaultOu "Default Organizational Unit" : defaultOu;
    email "Email" : email, text_field;

    password "Password" : String, password_field, hide when enablePass == false, check length(trim(password)) > 0 : "Password cannot be empty." , optional, metering passwordScore;
    confirmPassword "Confirm password" : String, password_field, hide when enablePass == false, check confirmPassword == password : "Passwords mismatch", optional, metering passwordScore;
    hashPass  : String, internal, optional;
    enablePass : Boolean, internal;
    internalLocale : locale, internal;
    locale "Locale" : String, combo_box, optional, on_change updateLocale;

    assignRoles "Assign roles from" : User, optional, hide when edit;
    picture "Picture" : upload(image), optional;

    "Devices" : devices, table, hide when count(deviceId) == 0 {
        deviceName : name;
        deviceId   : deviceId, text_field, style "xxlarge";
        disabled   : disabled;
        button(remove_row, devices), icon times;
    };

    "Properties" : props, table, hide when count(property) == 0 {
        required : Boolean, internal;
        "Property" : property, display;
        type "Type"     : PropertyType, display;
        value "Value"    : dynamic, optional when !required;
    };

    vertical, hide when !edit {
        chooseUserDialog : dialog {
            user "User" : User, optional;
            footer { copy "Copy roles" : button, content_style "btn-primary", on_click copyRoles; };
        };

        horizontal, col 4, offset_col 8 {
            assign "Assign role" : button, content_style "btn-primary btn-sm", style "margin-bottom-0", on_click addRole, disable when !edit || forbidden(update);
            copyRoles "Copy other's roles" : button, content_style "btn-primary btn-sm", style "margin-bottom-0", on_click openUserChooser, disable when !edit || forbidden(update);
            allPermissions "View all permissions" : button, content_style "btn-primary btn-sm", style "margin-bottom-0", on_click viewAllPermissions;
        };

        roles "Roles" : RoleAssignment, table,sortable, placeholder "User has no roles assigned."
        {
            role : role, display;
            ou : ou, display;
            horizontal, style "text-right" {
                permissions "View permissions" : button, content_style "btn-xs btn-primary", on_click viewPermissions;
                delete "Delete" : button, content_style "btn-xs btn-danger", confirm "Do you really want to delete this?", on_click deleteAssignment, disable when forbidden(update);
            };
        };
    };

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
        deprecate : toggle_button(deprecate), style "pull-right";
    };
}

form ApplicationForm "Applications"
    entity Application
{
    header {
        message(title);
        search_box, style "pull-right";
    };

    id   "Id"   : id, text_field;
    token "Token" : token, display;

    user "Associate to User" : User;


    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

form UserProfileForm "User Profile"
{
    header {
        message(title);
    };

    passwordScore : Int, internal, default 3;
    edit : Boolean, internal;

    id   "Id"   : display;
    name "Name" : String(25);
    defaultOu "Default Organizational Unit" : OrgUnit, display;
    email "Email" : String(100), text_field, optional;

    password "Password" : String(256), password_field, hide when enablePass == false, check length(trim(password)) > 0 : "Password cannot be empty." , optional, metering passwordScore;
    confirmPassword "Confirm password" : String(256), password_field, hide when enablePass == false, check confirmPassword == password : "Passwords mismatch", optional, metering passwordScore;
    hashPass  : String, internal, optional;
    enablePass : Boolean, internal;
    locale "Locale" : String, combo_box, optional;
    picture "Picture" : upload(image), optional, camera;

    devices "Devices" : table, hide when count(deviceId) == 0 {
        deviceId : String(256), internal;
        deviceName "Device name" : display;
        disabled "Disabled" : check_box;
        button(remove_row, devices), icon times;
    };

    footer {
        apply "Apply" : button(save);
        button(cancel);
    };
}

form RoleAssignmentForm "Role Assignment"
    entity RoleAssignment
{
    header {
        message(title);
        search_box, style "pull-right";
    };

    user "User" : user;
    role "Role" : role;
    ou   "Ou"   : ou;

    footer {
        button(save);
        button(cancel);
        button(delete), style "pull-right";
    };
}

final type DeviceInfo {
    name:String;
    id:String;
}

handler DeviceHandler
    on_route "/device"
    secure_by usernamepassword
{
    "/register": String, register, method post,body DeviceInfo;
    "/unregister/$deviceId": Void, unregister, method post;
}
