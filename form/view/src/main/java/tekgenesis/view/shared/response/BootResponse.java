
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.response;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.Form;

import static tekgenesis.metadata.form.model.FormConstants.VIEW_IMG_USERS_JPG;

/**
 * Boot response.
 */
@SuppressWarnings("FieldMayBeFinal")
public class BootResponse implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private boolean defaultImage;

    private boolean         error;
    @NotNull private String errorMessage;

    @Nullable private Form   feedback;
    private boolean          mailValidatorEnabled;
    private boolean          productionMode;
    @NotNull private String  resourceServerUrl;
    @NotNull private Integer timeout;
    @NotNull private String  unigisServerUrl;
    @NotNull private String  userId;
    @NotNull private String  userImage;
    @NotNull private String  username;

    //~ Constructors .................................................................................................................................

    /** Gwt constructor. */
    public BootResponse() {
        feedback             = null;
        unigisServerUrl      = "";
        resourceServerUrl    = "";
        userId               = "";
        username             = "";
        userImage            = "";
        timeout              = -1;
        productionMode       = false;
        mailValidatorEnabled = false;

        error        = false;
        errorMessage = "";
    }

    //~ Methods ......................................................................................................................................

    /** Sets the user info. */
    public BootResponse forUser(@NotNull final String name, @NotNull final String id, @NotNull final String image) {
        username  = name;
        userId    = id;
        userImage = image;
        return this;
    }

    /** Returns true if user has default image. */
    public boolean hasDefaultImage() {
        return userImage.equals(VIEW_IMG_USERS_JPG);
    }

    /** Returns true if there was an error during boot. */
    public boolean hasError() {
        return error;
    }

    /** Sets the application as connected to a production environment. */
    public BootResponse productionMode() {
        productionMode = true;
        return this;
    }

    /** Sets a feedback form. */
    public BootResponse withFeedbackForm(@NotNull final Form f) {
        feedback = f;
        return this;
    }

    public BootResponse withMailValidatorEnabled(boolean enable) {
        mailValidatorEnabled = enable;
        return this;
    }

    /** Sets the Resource server url. */
    public BootResponse withResourceServerUrl(@NotNull final String url) {
        resourceServerUrl = url;
        return this;
    }

    /** Sets the timeout to be used for the keep-alive mechanism. */
    public BootResponse withSessionTimeout(@NotNull final Integer t) {
        timeout = t;
        return this;
    }

    /** Sets Unigis server url to be used for map widgets. */
    public BootResponse withUnigisServerUrl(@NotNull final String url) {
        unigisServerUrl = url;
        return this;
    }

    /** Is mail address validator enabled. */
    public boolean isMailAddressEnabled() {
        return mailValidatorEnabled;
    }

    /** Is application connected to a production environment. */
    public boolean isProductionMode() {
        return productionMode;
    }

    /** Sets this boot response as an error one. */
    public void setError(boolean error) {
        this.error = error;
    }

    /** Returns error message. */
    @NotNull public String getErrorMessage() {
        return errorMessage;
    }

    /** Sets error message. */
    public void setErrorMessage(@NotNull String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /** Get feedback form option. None option means no feedback reporter if subscribed. */
    public Option<Form> getFeedback() {
        return Option.option(feedback);
    }

    /** Get resource server property. */
    @NotNull public String getResourceServerUrl() {
        return resourceServerUrl;
    }

    /** Get timeout. */
    @NotNull public Integer getTimeout() {
        return timeout;
    }

    /** Get unigis server property. */
    @NotNull public String getUnigisServerUrl() {
        return unigisServerUrl;
    }

    /** Get user id property. */
    @NotNull public String getUserId() {
        return userId;
    }

    /** Get user image property. */
    @NotNull public String getUserImage() {
        return userImage;
    }

    /** Get user name property. */
    @NotNull public String getUsername() {
        return username;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -8427387082522092011L;
}  // end class BootResponse
