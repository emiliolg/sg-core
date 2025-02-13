package tekgenesis.authorization;


enum Messages "Messages" {
    NO_UPDATE_PERMISION : "Do not have update permission";
    NO_CREATE_PERMISION : "Do not have create permission";
    NO_DELETE_PERMISION : "Do not have delete permission";
    ROLE_ASSIGNMENTS_FOR : "Assignments for role '%s'";

    SUCCESFULLY_SENT_EMAIL : "An email was sent to the user's mail with instructions to reset the password.";
    ERROR_SENDING_EMAIL : "There was an error sending the email. Try again later or contact the administrator.";

    USER_NOT_FOUND_BY_EMAIL : "User not found with that email";
    USER_NOT_FOUND_BY_USERNAME : "User not found with that username";

    RESET_EMAIL_SUBJECT : "Reset password request";
    RESET_EMAIL_BODY : "To reset your password, go to <a href=\"%s\">%s</a>";

    EXPIRED_LINK : "Link has expired.";
    USED_LINK : "Link has already been used.";

    PASSWORD_RESET_SUCCESFULLY : "Password reset successfully.";

    EMAIL_ALREADY_IN_USE : "Email already in use. Sign into account and associate %s profile from threre.";

    UNATHORIZED : "Sorry, you do not have access rights to this area.";
    LOGIN: "Login";
    USERNAME: "Username:";
    PASSWORD: "Password:";
    RESET_PASSWORD: "Reset Password";
    WRONG_CREDENTIALS: "Wrong username or password.";
    EXPIRED_CREDENTIALS: "Password has expired.";
    INACTIVE_ACCOUNT: "Account has been inactivated.";
    FORGOT_PASSWORD_LABEL: "Forgot password?";

}
