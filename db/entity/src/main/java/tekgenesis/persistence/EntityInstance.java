
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

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.database.HasRowMapper;

import static tekgenesis.common.collections.Colls.listOf;

/**
 * An EntityInstance.
 */
public interface EntityInstance<This extends EntityInstance<This, K>, K> extends HasRowMapper {

    //~ Methods ......................................................................................................................................

    /** Returns the description for the instance. */
    @NotNull default Seq<String> describe() {
        return listOf(toString());
    }

    /** Returns true if the Instance has an EMPTY default key. */
    default boolean hasEmptyKey() {
        return false;
    }

    /** Method to invalidate references to other entities. */
    @SuppressWarnings("EmptyMethod")
    default void invalidate() {}

    /** Returns the Key of the entity as String. */
    @NotNull String keyAsString();

    /** Return the key Object of the Entity. */
    @NotNull K keyObject();

    /** The Metadata for the entity. */
    default TableMetadata<This, K> metadata() {
        return table().metadata();
    }

    /** Returns true if the instance has been modified. */
    default boolean modified() {
        return false;
    }

    /** The db table for the entity. */
    DbTable<This, K> table();

    /** Returns instance version field. */
    default long getInstanceVersion() {
        return 0;
    }

    /** Returns the Update Time. */
    @JsonIgnore @NotNull default DateTime getUpdateTime() {
        throw new IllegalStateException("Entity does not have update time");
    }
}  // end interface EntityInstance
