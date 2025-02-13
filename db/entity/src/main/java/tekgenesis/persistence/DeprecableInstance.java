
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

/*
 * Copyright (c) 2012 TekGenesis and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package tekgenesis.persistence;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateTime;

import static tekgenesis.common.Predefined.cast;

/**
 * A Deprecable EntityInstance.
 */
public interface DeprecableInstance<This extends EntityInstance<This, K>, K> extends EntityInstance<This, K> {

    //~ Methods ......................................................................................................................................

    /**
     * Sets the instance deprecation status.
     *
     * @param  status  Deprecation status. True in order to deprecate the instance, false otherwise.
     */
    @NotNull default This deprecate(boolean status) {
        return metadata().updateAuditFields(cast(this), false, status, !status);
    }

    /** Indicates whether the instance is deprecated or not. */
    default boolean isDeprecated() {
        return getDeprecationTime() != null;
    }

    /** Returns the Instance Deprecation Time. */
    @Nullable DateTime getDeprecationTime();

    /** Returns the Instance Deprecation User id. */
    @Nullable String getDeprecationUser();
}
