
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.service.Status;
import tekgenesis.service.html.Html;

/**
 * Interface that developers need to implement in order to implement their own Html errors.
 *
 * <p>To make a CustomError you must implement this interface and package it as a jar file with a
 * file named</p>
 *
 * <p>META-INF/services/tekgenesis.service.CustomError</p>
 *
 * <p>This file contains the single line:</p>
 *
 * <p>com.example.impl.CustomErrorImpl</p>
 */
public interface CustomError {

    //~ Methods ......................................................................................................................................

    /** Method to be called on each error. */
    @NotNull Option<Result<Html>> forError(@NotNull Factory factory, @NotNull Status status, @Nullable Throwable e);
}
