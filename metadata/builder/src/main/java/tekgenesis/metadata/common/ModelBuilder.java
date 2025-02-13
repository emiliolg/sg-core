
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.common;

import java.util.EnumSet;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.exception.BuilderError;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.type.MetaModel;
import tekgenesis.type.Modifier;

import static java.util.Arrays.asList;

import static tekgenesis.common.Predefined.cast;

/**
 * The interface that any MetaModel (Form, Enum, Entity) must implement.
 */
public interface ModelBuilder<T extends MetaModel, This extends ModelBuilder<T, This>> {

    //~ Methods ......................................................................................................................................

    /** Builds the MetaModel. */
    T build()
        throws BuilderException;

    /** Performs desired checks in builders, and returns a List of BuilderErrors. */
    @NotNull List<BuilderError> check();

    /** Add Documentation to the Builder. */
    This withDocumentation(@NotNull String documentation);

    /** Add a Modifier to the Builder. */
    This withModifier(Modifier mod);

    /** Add Modifiers to the Builder. */
    This withModifiers(EnumSet<Modifier> modifiers);

    /** Add Modifiers to the Builder. */
    default This withModifiers(Modifier... ms) {
        return withModifiers(EnumSet.copyOf(asList(ms)));
    }

    /** Returns the FullName of the model. */
    @NotNull String getFullName();

    /** Returns the MetaModel id. */
    @NotNull String getId();

    //~ Inner Classes ................................................................................................................................

    /**
     * Default base implementation.
     */
    abstract class Default<T extends MetaModel, This extends ModelBuilder<T, This>> implements ModelBuilder<T, This> {
        @NotNull protected String            documentation;
        @NotNull protected final String      domain;
        @NotNull protected final String      id;
        @NotNull protected String            label;
        @NotNull protected EnumSet<Modifier> modifiers;
        @NotNull protected final String      sourceName;

        protected Default(@NotNull String src, @NotNull String pkg, @NotNull String name) {
            id            = name;
            label         = "";
            documentation = "";
            domain        = pkg;
            sourceName    = src;
            modifiers     = Modifier.emptySet();
        }

        /** Returns true if the builder contains the given modifier. */
        public boolean hasModifier(Modifier mod) {
            return modifiers.contains(mod);
        }

        /** Sets the Model label. */
        public final This label(@NotNull String str) {
            label = str;
            return cast(this);
        }

        @Override public This withDocumentation(@NotNull String doc) {
            documentation = doc;
            return cast(this);
        }
        @Override public This withModifier(Modifier mod) {
            modifiers.add(mod);
            return cast(this);
        }

        @Override public This withModifiers(EnumSet<Modifier> mod) {
            modifiers.addAll(mod);
            return cast(this);
        }

        /** Returns the entity domain. */
        @NotNull
        @SuppressWarnings("WeakerAccess")
        public final String getDomain() {
            return domain;
        }

        @NotNull @Override public final String getFullName() {
            return domain + "." + id;
        }

        @NotNull @Override public final String getId() {
            return id;
        }
    }  // end class Default
}  // end interface ModelBuilder
