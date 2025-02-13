
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.QName;
import tekgenesis.repository.ModelRepository;

/**
 * An interface for Models in the {@link ModelRepository}.
 */
public interface MetaModel extends Scope, Serializable {

    //~ Instance Fields ..............................................................................................................................

    long serialVersionUID = -3624910200230072016L;

    //~ Methods ......................................................................................................................................

    /** Returns true if the definition contains the given modifier. */
    boolean hasModifier(Modifier mod);

    /** Returns the metamodel documentation. Empty string if it doesn't have any. */
    @NotNull default String getDocumentation() {
        return "";
    }

    /** Returns the domain. */
    @NotNull String getDomain();

    /** Returns the 'full' (qualified) name. */
    @NotNull String getFullName();

    /** Return the Key to the MetaModel {@link QName}. */
    @NotNull QName getKey();

    /** Returns a descriptive label for the specified object. */
    @NotNull String getLabel();

    /** Returns the MetaModelKind. */
    @NotNull MetaModelKind getMetaModelKind();

    /** Returns the name. */
    @NotNull String getName();

    /** Returns true if the Model is an inner one (Defined inside another model(. */
    default boolean isInner() {
        return false;
    }

    /** Get all Models this model references. */
    Seq<MetaModel> getReferences();

    /** Returns the schema. */
    @NotNull String getSchema();

    /** Returns the path of the file where the Model was defined. */
    @NotNull String getSourceName();

    /** Get all Models that references this one. */
    Seq<MetaModel> getUsages();
}  // end class MetaModel
