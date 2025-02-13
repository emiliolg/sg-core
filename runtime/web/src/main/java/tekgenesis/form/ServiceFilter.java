
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NotNull;

/**
 * Interface that developers needs to implement in order to be called to set properties to a
 * request.
 *
 * <p>To make a ServiceFilter you must implement this interface and package it as a jar file with a
 * file named</p>
 *
 * <p>META-INF/services/tekgenesis.form.ServiceFilter</p>
 *
 * <p>This file contains the single line:</p>
 *
 * <p>com.example.impl.ServiceFilterImpl</p>
 */
@SuppressWarnings("WeakerAccess")
public interface ServiceFilter {

    //~ Methods ......................................................................................................................................

    /** Method that is called to know if the apply method should be called or not. */
    boolean accepts(@NotNull final String url);

    /** Method that is called to know if the filter needs authentication. */
    boolean needsAuthentication();

    /** Method to be called on each request before request is proceeded by SuiGeneris. */
    boolean process(@NotNull final HttpServletRequest request, @NotNull final HttpServletResponse response,
                    @NotNull final ApplicationContext context);

    /** Method to be called on each request after request is proceeded by SuiGeneris. */
    default void processAfter(@NotNull final HttpServletRequest request, @NotNull final HttpServletResponse response,
                              @NotNull final ApplicationContext context) {}
}
