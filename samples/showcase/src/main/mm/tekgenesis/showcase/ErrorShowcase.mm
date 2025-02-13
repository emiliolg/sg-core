package tekgenesis.showcase;

form ErrorShowcase "Error Showcase Form"
{
    header { message(title); };

    vertical , col 6 {
        persistentMessage "Persistent message" : button, on_click persistentMessage;
        autoCloseMessage "Regular old fashioned message" : button, on_click autoCloseMessage;

        errorWithSuccess "Error action with success message" : button, on_click errorSuccessMessage;
        errorWithWarning "Error action with warning message" : button, on_click errorWarningMessage;
        errorAutoClose   "Error action with autoClose timer" : button, on_click errorAutoClose;
        defaultError     "Default with error message autoclosing": button, on_click defaultWithErrorAutoclosing;
        defaultWarning   "Default with warning message persistent" : button, on_click defaultWarningPersistent;

    };
    vertical , col 6 {
        navigateNormal "Normal navigation" : button, on_click navigateNormal;
        navigateError "Error navigation" : button, on_click navigatewithError;
        navigateWarning "Warning navigation persistent" : button, on_click navigatewithWarning;
    };

    button "Error!" : button, on_click error;
    logout "Logout" : button, on_click killSession;

    button(cancel);
}

form NiceToNavigateTo "I'm glad you navigate to me" {
    header {
        message(title);
    };

    cancel: button(cancel);
}
