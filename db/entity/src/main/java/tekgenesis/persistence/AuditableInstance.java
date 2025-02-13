
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

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateTime;

/**
 * An Auditable Instance.
 */
public interface AuditableInstance {

    //~ Methods ......................................................................................................................................

    /** Returns the Instance Creation Time. */
    @Nullable DateTime getCreationTime();

    /** Returns the Instance Creation UserId. */
    @Nullable String getCreationUser();

    /** Returns the Instance Last Update Time. */
    @Nullable DateTime getUpdateTime();

    /** Returns the Instance Last Update UserId. */
    @Nullable String getUpdateUser();
}
