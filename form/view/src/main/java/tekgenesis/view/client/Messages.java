
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.widget.CustomMsgType;
import tekgenesis.view.shared.response.ResponseError;

/**
 * Application messages service.
 */
public interface Messages {

    //~ Methods ......................................................................................................................................

    /** Add custom message. */
    void custom(@NotNull String msg, @NotNull FormBox formBox, CustomMsgType type, boolean autoHide);

    /** Add custom error message. */
    void customError(@NotNull String msg, @NotNull FormBox formBox, @NotNull List<String> errorDetails, boolean reload, CustomMsgType type,
                     boolean autoHide);

    /** Show error message. */
    void error(@NotNull final String msg, @NotNull final FormBox formBox);

    /** Show exception message. */
    void error(@NotNull final String msg, @NotNull final ResponseError exception, @NotNull final FormBox formBox);

    /** Return true if given formBox has an error message. */
    boolean hasError(@NotNull final FormBox formBox);

    /** Hide all current messages for given formBox. */
    void hide(@NotNull final FormBox formBox);

    /** Hide only info messages for given formBox. */
    void hideInfoMessages(FormBox formBox);

    /** Add info msg (auto-hides). */
    void info(@NotNull final String msg, @NotNull final FormBox formBox);

    /** Add success msg (auto-hides). */
    void success(@NotNull final String msg, @NotNull final FormBox formBox);

    /** Add success msg. */
    void success(@NotNull final String msg, @NotNull final FormBox formBox, final boolean autoHide);

    /** Add warning msg. */
    void warning(@NotNull final String msg, @NotNull final FormBox formBox);
}  // end interface Messages
